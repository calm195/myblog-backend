package com.chrissy.web.login.wx.callback;

import com.chrissy.model.vo.user.login.wx.BaseWechatMessageVo;
import com.chrissy.model.vo.user.login.wx.WechatMessageReq;
import com.chrissy.model.vo.user.login.wx.WechatTxtMessageRsp;
import com.chrissy.service.user.service.LoginService;
import com.chrissy.web.login.wx.helper.SseHelper;
import com.chrissy.web.login.wx.helper.WechatAckHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author chrissy
 * @Date 9/4/2024 22:09
 */
@RestController
@RequestMapping(path = "/wx")
public class WechatCallbackRestController {
    @Resource
    private LoginService loginService;
    @Resource
    private SseHelper sseHelper;
    @Resource
    private WechatAckHelper wechatAckHelper;

    @GetMapping(path = "/callback")
    public String test(HttpServletRequest request) {
        String echoStr = request.getParameter("echostr");
        if (StringUtils.isNoneEmpty(echoStr)) {
            return echoStr;
        }
        return "";
    }

    /**
     * fixme: 需要做防刷校验
     * 微信的响应返回<p>
     * 本地测试访问:
     * {@code curl -X POST 'http://localhost:8080/wx/callback' -H 'content-type:application/xml' -d '<xml><URL><![CDATA[https://hhui.top]]></URL><ToUserName><![CDATA[一灰灰blog]]></ToUserName><FromUserName><![CDATA[demoUser1234]]></FromUserName><CreateTime>1655700579</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[login]]></Content><MsgId>11111111</MsgId></xml>' -i }
     *
     * @param wechatMessageReq 微信信息请求体
     * @return 图文或者纯文字 微信信息返回体
     */
    @PostMapping(path = "/callback",
            consumes = {"application/xml", "text/xml"},
            produces = "application/xml;charset=utf-8")
    public BaseWechatMessageVo callBack(@RequestBody WechatMessageReq wechatMessageReq) {
        String content = wechatMessageReq.getContent();
        if ("subscribe".equals(wechatMessageReq.getEvent()) || "scan".equalsIgnoreCase(wechatMessageReq.getEvent())) {
            String key = wechatMessageReq.getEventKey();
            if (StringUtils.isNotBlank(key) || key.startsWith("qrscene_")) {
                // todo 个人公众号不能省去输入验证码的过程
                String code = key.substring("qrscene_".length());
                loginService.loginOrAutoRegisterByWechat(wechatMessageReq.getFromUserName());
                sseHelper.login(code);
                WechatTxtMessageRsp res = new WechatTxtMessageRsp();
                res.setContent("登录成功");
                res.setFromUserName(wechatMessageReq.getToUserName());
                res.setToUserName(wechatMessageReq.getFromUserName());
                res.setCreateTime(System.currentTimeMillis() / 1000);
                return res;
            }
        }

        BaseWechatMessageVo res = wechatAckHelper.buildResponseBody(wechatMessageReq.getEvent(), content, wechatMessageReq.getFromUserName());
        res.setFromUserName(wechatMessageReq.getToUserName());
        res.setToUserName(wechatMessageReq.getFromUserName());
        res.setCreateTime(System.currentTimeMillis() / 1000);
        return res;
    }
}

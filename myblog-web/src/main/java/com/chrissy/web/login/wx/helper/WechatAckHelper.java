package com.chrissy.web.login.wx.helper;

import com.chrissy.core.util.VerificationCodeUtil;
import com.chrissy.model.vo.user.login.wx.BaseWechatMessageVo;
import com.chrissy.model.vo.user.login.wx.WechatImageTxtItemVo;
import com.chrissy.model.vo.user.login.wx.WechatImageTxtMessageRsp;
import com.chrissy.model.vo.user.login.wx.WechatTxtMessageRsp;
import com.chrissy.service.user.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 微信公众号自动回复
 * @author chrissy
 * @Date 9/6/2024 22:59
 */
@Slf4j
@Component
public class WechatAckHelper {
    @Resource
    private LoginService loginService;
    @Resource
    private SseHelper sseHelper;

    public BaseWechatMessageVo buildResponseBody(String eventType, String content, String fromUser){
        String txtResult = "";
        // List<WechatImageTxtItemVo> imageTxtItemVoList = null;
        if (eventType.equalsIgnoreCase("subscribe")){
            txtResult = "thanks for subscribe.";
        }
        else if (content.equalsIgnoreCase("test")){
            txtResult = "thanks for test.";
        }
        else if (VerificationCodeUtil.isVerifyCode(content)){
            loginService.loginOrAutoRegisterByWechat(fromUser);
            if (sseHelper.login(content)){
                txtResult = "success for login.";
            } else {
                txtResult = "login failed, please reload the page and check the code.";
            }
        }

        if (StringUtils.isNotBlank(txtResult)){
            WechatTxtMessageRsp wechatTxtMessageRsp = new WechatTxtMessageRsp();
            wechatTxtMessageRsp.setContent(txtResult);
            return wechatTxtMessageRsp;
        } else {
            WechatImageTxtMessageRsp wechatImageTxtMessageRsp = new WechatImageTxtMessageRsp();
//            wechatImageTxtMessageRsp.setArticles(imageTxtItemVoList);
//            wechatImageTxtMessageRsp.setArticleCount(imageTxtItemVoList.size());
            wechatImageTxtMessageRsp.setArticles(null);
            wechatImageTxtMessageRsp.setArticleCount(0);
            return wechatImageTxtMessageRsp;
        }
    }
}

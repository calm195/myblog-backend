package com.chrissy.web.login.pwd;

import cn.hutool.http.server.HttpServerResponse;
import com.chrissy.core.util.CookieUtil;
import com.chrissy.model.res.Result;
import com.chrissy.model.res.WebStateEnum;
import com.chrissy.service.user.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * username password 登录方式入口
 * @author chrissy
 * @date 2024/8/15 15:37
 */
@RestController
@RequestMapping
public class LoginByPasswordRestController {
    @Resource
    private LoginService loginService;

    @PostMapping("/login/username")
    public Result<Boolean> login(@RequestParam(name = "username") String username,
                        @RequestParam(name = "password") String password,
                        HttpServletResponse response){
        String session = loginService.loginByUsernameAndPassword(username, password);
        if (StringUtils.isNotBlank(session)) {
            response.addCookie(CookieUtil.newCookie(LoginService.SESSION_KEY, session));
            return Result.ok(true);
        } else {
            // todo: 返回错误值多样化，如已注册，密码错误等。
            return Result.fail(WebStateEnum.LOGIN_FAILED_MIXED, "用户名密码登录失败，请稍后重试");
        }
    }
}

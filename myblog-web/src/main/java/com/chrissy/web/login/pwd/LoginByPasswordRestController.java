package com.chrissy.web.login.pwd;

import com.chrissy.core.permission.Permission;
import com.chrissy.core.permission.UserRole;
import com.chrissy.core.util.CookieUtil;
import com.chrissy.model.context.ReqInfoContext;
import com.chrissy.model.res.Result;
import com.chrissy.model.res.WebStateEnum;
import com.chrissy.model.vo.user.login.password.UserPwdRegisterReq;
import com.chrissy.service.user.repository.entity.req.UserPwdLoginReq;
import com.chrissy.service.user.service.LoginService;
import com.chrissy.service.user.service.RegisterService;
import liquibase.pro.packaged.B;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

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

    @Resource
    private RegisterService registerService;

    @PostMapping("/login/username")
    public Result<Boolean> login(@RequestBody UserPwdLoginReq loginReq,
                                 HttpServletResponse response){
        String session = loginService.loginByUsernameAndPassword(loginReq.getUsername(), loginReq.getPassword());
        if (StringUtils.isNotBlank(session)) {
            response.addCookie(CookieUtil.newCookie(LoginService.SESSION_KEY, session));
            return Result.ok(true);
        } else {
            // todo: 返回错误值多样化，如已注册，密码错误等。
            return Result.fail(WebStateEnum.LOGIN_FAILED_MIXED, "用户名密码登录失败，请稍后重试");
        }
    }

    @PostMapping("/register/username")
    public Result<String> register(@RequestBody UserPwdRegisterReq registerReq,
                                   HttpServletResponse response){
        Long userId = registerService.registerByUsernameAndPassword(registerReq);
        if (userId == null) {
            return Result.fail(WebStateEnum.LOGIN_FAILED_MIXED, "注册失败");
        } else {
            return Result.ok(registerReq.getNickname());
        }
    }

    @Permission(role = UserRole.LOGIN)
    @RequestMapping("/logout")
    public Result<Boolean> logout(HttpServletRequest request, HttpServletResponse response) throws Exception{
        request.getSession().invalidate();
        Optional.ofNullable(ReqInfoContext.getReqInfo()).ifPresent(s -> loginService.logout(s.getSession()));
        response.addCookie(CookieUtil.delCookie(LoginService.SESSION_KEY));
        String referer = request.getHeader("Referer");
        if (StringUtils.isBlank(referer)){
            referer = "/";
        }
        response.sendRedirect(referer);
        return Result.ok(true);
    }
}

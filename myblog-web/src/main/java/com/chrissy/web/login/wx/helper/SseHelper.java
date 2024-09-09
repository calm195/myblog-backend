package com.chrissy.web.login.wx.helper;

import com.chrissy.core.util.VerificationCodeUtil;
import com.chrissy.model.context.ReqInfoContext;
import com.chrissy.model.exception.NoValueInGuavaException;
import com.chrissy.service.user.helper.UserSessionHelper;
import com.chrissy.service.user.service.LoginService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author chrissy
 * @Date 9/2/2024 22:45
 */
@Slf4j
@Component
public class SseHelper {
    /**
     * sse的超时时间，默认15min
     */
    private final static Long SSE_EXPIRE_TIME = 15 * 60 * 1000L;
    private final LoginService loginService;
    /**
     * key = 验证码, value = 长连接
     */
    private final LoadingCache<String, SseEmitter> verifyCodeCache;
    /**
     * key = 设备 value = 验证码
     */
    private final LoadingCache<String, String> deviceCodeCache;

    @Resource
    private UserSessionHelper sessionHelper;

    public SseHelper(LoginService loginService) {
        this.loginService = loginService;
        verifyCodeCache = CacheBuilder.newBuilder()
                .maximumSize(300)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<String, SseEmitter>() {
                    @NotNull
                    @Override
                    public SseEmitter load(@NotNull String s) throws Exception {
                        throw new NoValueInGuavaException("no value in guava: " + s);
                    }
                });

        deviceCodeCache = CacheBuilder.newBuilder()
                .maximumSize(300)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @NotNull
                    @Override
                    public String load(@NotNull String s) {
                        int cnt = 0;
                        while (true) {
                            String code = VerificationCodeUtil.genCode(cnt++);
                            if (!verifyCodeCache.asMap().containsKey(code)) {
                                return code;
                            }
                        }
                    }
                });
    }

    public SseEmitter subscribe() throws IOException {
        String deviceId = ReqInfoContext.getReqInfo().getDeviceId();
        String realCode = deviceCodeCache.getUnchecked(deviceId);
        // fixme 设置15min的超时时间, 超时时间一旦设置不能修改；因此导致刷新验证码并不会增加连接的有效期
        SseEmitter sseEmitter = new SseEmitter(SSE_EXPIRE_TIME);
        SseEmitter oldSse = verifyCodeCache.getIfPresent(realCode);
        if (oldSse != null) {
            oldSse.complete();
        }
        verifyCodeCache.put(realCode, sseEmitter);
        sseEmitter.onTimeout(() -> {
            log.info("sse 超时中断 --> {}", realCode);
            verifyCodeCache.invalidate(realCode);
            sseEmitter.complete();
        });
        sseEmitter.onError((e) -> {
            log.warn("sse error! --> {}", realCode, e);
            verifyCodeCache.invalidate(realCode);
            sseEmitter.complete();
        });
        // 若实际的验证码与前端显示的不同，则通知前端更新
        sseEmitter.send("initCode!");
        sseEmitter.send("init#" + realCode);
        return sseEmitter;
    }

    public String resend() throws IOException {
        String deviceId = ReqInfoContext.getReqInfo().getDeviceId();
        String oldCode = deviceCodeCache.getIfPresent(deviceId);
        SseEmitter lastSse = oldCode == null ? null : verifyCodeCache.getIfPresent(oldCode);
        if (lastSse != null) {
            lastSse.send("resend!");
            lastSse.send("init#" + oldCode);
            return oldCode;
        }
        return "fail";
    }

    public String refreshCode() throws IOException {
        String deviceId = ReqInfoContext.getReqInfo().getDeviceId();
        String oldCode = deviceCodeCache.getIfPresent(deviceId);
        SseEmitter lastSse = oldCode == null ? null : verifyCodeCache.getIfPresent(oldCode);
        if (lastSse == null) {
            log.info("last deviceId:{}, code:{}, sse closed!", deviceId, oldCode);
            deviceCodeCache.invalidate(deviceId);
            return null;
        }

        // 重新生成一个验证码
        deviceCodeCache.invalidate(deviceId);
        String newCode = deviceCodeCache.getUnchecked(deviceId);
        log.info("generate new loginCode! deviceId:{}, oldCode:{}, newCode:{}", deviceId, oldCode, newCode);

        lastSse.send("updateCode!");
        lastSse.send("refresh#" + newCode);
        verifyCodeCache.invalidate(oldCode);
        verifyCodeCache.put(newCode, lastSse);
        return newCode;
    }

    public boolean login(String verifyCode) {
        // 通过验证码找到对应的长连接
        SseEmitter sseEmitter = verifyCodeCache.getIfPresent(verifyCode);
        if (sseEmitter == null) {
            return false;
        }

        String session = sessionHelper.generateSession(ReqInfoContext.getReqInfo().getUserId());
        try {
            // 登录成功，写入session
            sseEmitter.send(session);
            // 设置cookie的路径
            sseEmitter.send("login#" + LoginService.SESSION_KEY + "=" + session + ";path=/;");
            return true;
        } catch (Exception e) {
            log.error("登录异常: {}", verifyCode, e);
        } finally {
            sseEmitter.complete();
            verifyCodeCache.invalidate(verifyCode);
        }
        return false;
    }
}

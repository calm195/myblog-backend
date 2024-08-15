package com.chrissy.service.user.service.account;

import com.chrissy.model.context.ReqInfoContext;
import com.chrissy.service.user.helper.MyPasswordEncoder;
import com.chrissy.service.user.helper.UserSessionHelper;
import com.chrissy.service.user.repository.dao.UserAccountDao;
import com.chrissy.service.user.repository.entity.po.UserAccountPO;
import com.chrissy.service.user.repository.entity.vo.UserPwdLoginReq;
import com.chrissy.service.user.service.LoginService;
import com.chrissy.service.user.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 登录服务逻辑
 * @author chrissy
 * @date 2024/8/13 14:40
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private MyPasswordEncoder passwordEncoder;

    @Resource
    private UserAccountDao userAccountDao;

    @Resource
    private UserSessionHelper userSessionHelper;

    @Resource
    private RegisterService registerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String loginOrAutoRegisterByWechat(String wechatAccountId) {
        UserAccountPO userAccount = userAccountDao.getByThirdAccountId(wechatAccountId);
        Long res;
        if (userAccount == null){
            res = registerService.registerByWechatAccount(wechatAccountId);
        } else {
            res = userAccount.getId();
        }

        ReqInfoContext.getReqInfo().setUserId(res);
        return userSessionHelper.generateSession(res);
    }

    /**
     * 移除缓存中的session信息
     * @param session 用户会话
     */
    @Override
    public void logout(String session) {
        userSessionHelper.removeSession(session);
    }

    /**
     * 用户名密码方式登录
     * @param username 用户名
     * @param password 密码
     * @return jwt session
     */
    @Override
    public String loginByUsernameAndPassword(String username, String password) {
        UserAccountPO dbUserCount = userAccountDao.getByUserName(username);
        if (dbUserCount == null){
            // TODO: 加入报警，未注册
            return "";
        }

        if (!passwordEncoder.match(password, dbUserCount.getPassword())) {
            // TODO: 加入报警，密码错误
            return "";
        }

        long userId = dbUserCount.getId();

        ReqInfoContext.getReqInfo().setUserId(userId);
        return userSessionHelper.generateSession(userId);
    }
}

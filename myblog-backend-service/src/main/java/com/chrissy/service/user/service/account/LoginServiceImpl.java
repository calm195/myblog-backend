package com.chrissy.service.user.service.account;

import com.chrissy.model.context.ReqInfoContext;
import com.chrissy.service.user.helper.MyPasswordEncoder;
import com.chrissy.service.user.helper.UserSessionHelper;
import com.chrissy.service.user.repository.dao.UserAccountDao;
import com.chrissy.service.user.repository.entity.po.UserAccountPO;
import com.chrissy.service.user.repository.entity.vo.UserPwdLoginReq;
import com.chrissy.service.user.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Override
    public Long autoRegisterWxUserInfo(String uuid) {
        return 0L;
    }

    @Override
    public void logout(String session) {

    }

    @Override
    public String loginByWx(Long userId) {
        return "";
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

    @Override
    public String registerByUserPwd(UserPwdLoginReq loginReq) {
        return "";
    }
}

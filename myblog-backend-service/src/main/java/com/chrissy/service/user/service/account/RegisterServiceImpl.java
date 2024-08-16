package com.chrissy.service.user.service.account;

import com.chrissy.core.util.SpringUtil;
import com.chrissy.core.util.TransactionUtil;
import com.chrissy.model.enums.notice.NoticeTypeEnum;
import com.chrissy.model.enums.user.LoginTypeEnum;
import com.chrissy.model.event.NoticeEvent;
import com.chrissy.service.user.helper.MyPasswordEncoder;
import com.chrissy.service.user.helper.UserInfoHelper;
import com.chrissy.service.user.repository.dao.UserAccountDao;
import com.chrissy.service.user.repository.dao.UserInfoDao;
import com.chrissy.service.user.repository.entity.po.UserAccountPO;
import com.chrissy.service.user.repository.entity.po.UserInfoPO;
import com.chrissy.service.user.service.RegisterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 注册服务
 * @author chrissy
 * @date 2024/8/14 13:36
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    @Resource
    private MyPasswordEncoder passwordEncoder;

    @Resource
    private UserAccountDao userAccountDao;

    @Resource
    private UserInfoDao userInfoDao;

    // todo: 携带用户详细信息的注册逻辑

    /**
     * 根据用户名密码方式登录，自动生成昵称和头像，触发事件通知
     * @param username 用户名
     * @param password 密码
     * @return 用户id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long registerByUsernameAndPassword(String username, String password) {
        UserAccountPO userAccountPo = userAccountDao.getByUserName(username);
        if (userAccountPo != null){
            // todo: exception already registered.
            return -1L;
        }

        userAccountPo = new UserAccountPO();
        userAccountPo.setUsername(username);
        userAccountPo.setPassword(passwordEncoder.encodePassword(password));
        userAccountPo.setThirdAccountId("");
        userAccountPo.setLoginType(LoginTypeEnum.USERNAME.getCode());
        userAccountDao.saveUserAccount(userAccountPo);

        UserInfoPO userInfoPo = new UserInfoPO();
        userInfoPo.setUserId(userAccountPo.getId());
        userInfoPo.setNickName(UserInfoHelper.generateNickName());
        userInfoPo.setPhoto(UserInfoHelper.generateAvatar());
        userInfoDao.saveUserInfo(userInfoPo);

        processAfterRegister(userAccountPo.getId());
        return userAccountPo.getId();
    }

    /**
     * 根据微信账号登录，自动生成昵称和头像，触发事件通知
     * @param wechatAccountId 微信账户ID
     * @return 用户ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long registerByWechatAccount(String wechatAccountId) {
        UserAccountPO userAccountPo = new UserAccountPO();
        userAccountPo.setThirdAccountId(wechatAccountId);
        userAccountPo.setLoginType(LoginTypeEnum.WECHAT.getCode());
        userAccountDao.saveUserAccount(userAccountPo);

        UserInfoPO userInfoPo = new UserInfoPO();
        userInfoPo.setUserId(userAccountPo.getId());
        // todo: 获取微信昵称头像？
        userInfoPo.setNickName(UserInfoHelper.generateNickName());
        userInfoPo.setPhoto(UserInfoHelper.generateAvatar());
        userInfoDao.saveUserInfo(userInfoPo);

        processAfterRegister(userAccountPo.getId());
        return userAccountPo.getId();
    }

    /**
     * 注册成功后，发布通知
     * @param userId 用户ID
     */
    private void processAfterRegister(Long userId){
        TransactionUtil.registryAfterCommitOrImmediatelyRun(() ->
                SpringUtil.publishEvent(new NoticeEvent<>(this, NoticeTypeEnum.REGISTER, userId))
        );
    }
}

package com.chrissy.service.user.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chrissy.model.dao.PageParam;
import com.chrissy.model.enums.YesOrNoEnum;
import com.chrissy.service.user.repository.entity.po.UserAccountPO;
import com.chrissy.service.user.repository.mapper.UserAccountMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户登录表数据操作
 * @author chrissy
 * @date 2024/8/13 11:02
 */
@Repository
public class UserAccountDao extends ServiceImpl<UserAccountMapper, UserAccountPO> {
    @Resource
    private UserAccountMapper userAccountMapper;

    public void saveUserAccount(UserAccountPO user) {
        if (user.getId() == null) {
            userAccountMapper.insert(user);
        } else {
            userAccountMapper.updateById(user);
        }
    }

    public List<Long> listUserId(Long userId, Integer size) {
        return userAccountMapper.getUserIdsOrderByIdAsc(userId, size == null ? PageParam.DEFAULT_PAGE_SIZE : size);
    }

    /**
     * 第三方账号登录方式
     *
     * @param accountId 第三方登录ID
     * @return 用户登录信息
     */
    public UserAccountPO getByThirdAccountId(String accountId) {
        return userAccountMapper.getByThirdAccountId(accountId);
    }

    public UserAccountPO getByUserName(String userName) {
        LambdaQueryWrapper<UserAccountPO> queryUser = Wrappers.lambdaQuery();
        queryUser.eq(UserAccountPO::getUsername, userName)
                .eq(UserAccountPO::getDeleted, YesOrNoEnum.NO.getCode())
                .last("limit 1");
        return userAccountMapper.selectOne(queryUser);
    }

    public UserAccountPO getByUserId(Long userId) {
        return userAccountMapper.selectById(userId);
    }

    public void update(UserAccountPO userAccountPo) {
        userAccountMapper.updateById(userAccountPo);
    }
}

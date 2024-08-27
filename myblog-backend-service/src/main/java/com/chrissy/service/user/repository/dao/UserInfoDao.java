package com.chrissy.service.user.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chrissy.model.enums.YesOrNoEnum;
import com.chrissy.service.user.repository.entity.po.UserInfoPO;
import com.chrissy.service.user.repository.mapper.UserInfoMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 用户信息Dao
 * @author chrissy
 * @date 2024/8/13 13:46
 */
@Repository
public class UserInfoDao extends ServiceImpl<UserInfoMapper, UserInfoPO> {
    @Resource
    private UserInfoMapper userInfoMapper;

    public void saveUserInfo(UserInfoPO userInfo){
        if (userInfo == null){
            // todo: 报错
        } else {
            userInfoMapper.insert(userInfo);
        }
    }

    /**
     * 根据用户昵称来模糊查询，like语句
     *
     * @param nickname 昵称
     * @return 相似名称的用户列表
     */
    public List<UserInfoPO> getByNicknameLike(String nickname) {
        LambdaQueryWrapper<UserInfoPO> query = Wrappers.lambdaQuery();
        query.select(UserInfoPO::getUserId, UserInfoPO::getNickname, UserInfoPO::getPhoto, UserInfoPO::getProfile)
                .and(!StringUtils.isEmpty(nickname),
                        v -> v.like(UserInfoPO::getNickname, nickname)
                )
                .eq(UserInfoPO::getDeleted, YesOrNoEnum.NO.getCode());
        return userInfoMapper.selectList(query);
    }

    public UserInfoPO getByUserId(Long userId) {
        LambdaQueryWrapper<UserInfoPO> query = Wrappers.lambdaQuery();
        query.eq(UserInfoPO::getUserId, userId)
                .eq(UserInfoPO::getDeleted, YesOrNoEnum.NO.getCode());
        return userInfoMapper.selectOne(query);
    }

    public List<UserInfoPO> getByUserIds(Collection<Long> userIds) {
        LambdaQueryWrapper<UserInfoPO> query = Wrappers.lambdaQuery();
        query.in(UserInfoPO::getUserId, userIds)
                .eq(UserInfoPO::getDeleted, YesOrNoEnum.NO.getCode());
        return userInfoMapper.selectList(query);
    }

    public Long getUserCount() {
        return lambdaQuery()
                .eq(UserInfoPO::getDeleted, YesOrNoEnum.NO.getCode())
                .count();
    }

    public void updateUserInfo(UserInfoPO user) {
        UserInfoPO record = getByUserId(user.getUserId());
        if (record.equals(user)) {
            return;
        }
        if (StringUtils.isEmpty(user.getPhoto())) {
            user.setPhoto(null);
        }
        if (StringUtils.isEmpty(user.getNickname())) {
            user.setNickname(null);
        }
        user.setId(record.getId());
        updateById(user);
    }
}

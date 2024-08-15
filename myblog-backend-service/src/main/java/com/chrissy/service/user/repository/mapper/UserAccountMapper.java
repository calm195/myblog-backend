package com.chrissy.service.user.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chrissy.service.user.repository.entity.po.UserAccountPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户登录 mybatis mapper
 * @author chrissy
 * @date 2024/8/13 10:46
 */
public interface UserAccountMapper extends BaseMapper<UserAccountPO> {
    /**
     * 根据第三方唯一id进行查询
     *
     * @param accountId 第三方登录id
     * @return 用户登录信息
     */
    @Select("select * from user_login where third_account_id = #{account_id} limit 1")
    UserAccountPO getByThirdAccountId(@Param("account_id") String accountId);


    /**
     * 在大于{@code offsetUserId}中按顺序获取{@code size}大小的用户ID列表
     *
     * @param offsetUserId 基准用户ID
     * @param size 大小
     * @return 用户ID列表
     */
    @Select("select id from user_login where id > #{offsetUserId} order by id asc limit #{size}")
    List<Long> getUserIdsOrderByIdAsc(@Param("offsetUserId") Long offsetUserId, @Param("size") Long size);
}

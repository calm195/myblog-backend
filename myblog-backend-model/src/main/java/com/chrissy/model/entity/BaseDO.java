package com.chrissy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chrissy
 * @description 基础DO，所有DO都具有的属性，可序列化
 * @date 2024/8/9 9:33
 */
@Data
public class BaseDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;
}

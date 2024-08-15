package com.chrissy.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chrissy
 * @description 基础DO，所有DO都具有的属性，可序列化
 * @date 2024/8/13 10:23
 */
@Data
public class BaseDO implements Serializable {
    @ApiModelProperty(value = "业务主键")
    private Long id;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "最后编辑时间")
    private Date updateTime;
}

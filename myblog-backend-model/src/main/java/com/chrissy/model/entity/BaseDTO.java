package com.chrissy.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chrissy
 * @description 基础DTO，所有DTO都具有的属性，可序列化
 * @date 2024/8/9 10:17
 */
@Data
public class BaseDTO implements Serializable {
    @ApiModelProperty(value = "业务主键")
    private Long id;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "最后编辑时间")
    private Date updateTime;
}

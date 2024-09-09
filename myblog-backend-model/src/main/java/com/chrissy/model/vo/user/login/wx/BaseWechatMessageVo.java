package com.chrissy.model.vo.user.login.wx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * 微信接口数据公用基础信息
 * @author chrissy
 * @link <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html">
 *     微信官方文档 - 基础消息能力
 *     </a>
 * @Date 9/6/2024 23:56
 */
@Data
@JacksonXmlRootElement(localName = "xml")
public class BaseWechatMessageVo {
    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;
    @JacksonXmlProperty(localName = "FromUserName")
    private String fromUserName;
    @JacksonXmlProperty(localName = "CreateTime")
    private Long createTime;
    @JacksonXmlProperty(localName = "MsgType")
    private String msgType;
}

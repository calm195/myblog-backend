package com.chrissy.model.vo.user.login.wx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信信息事件请求体
 * @author chrissy
 * @Date 9/7/2024 0:23
 * @link <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html">
 *      微信官方文档 - 基础消息能力 - 接收普通消息
 *     </a>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JacksonXmlRootElement(localName = "xml")
public class WechatMessageReq extends BaseWechatMessageVo{
    @JacksonXmlProperty(localName = "Event")
    private String event;
    @JacksonXmlProperty(localName = "EventKey")
    private String eventKey;
    @JacksonXmlProperty(localName = "Ticket")
    private String ticket;
    @JacksonXmlProperty(localName = "Content")
    private String content;
    @JacksonXmlProperty(localName = "MsgId")
    private String msgId;
    @JacksonXmlProperty(localName = "MsgDataId")
    private String msgDataId;
    @JacksonXmlProperty(localName = "Idx")
    private String idx;
}

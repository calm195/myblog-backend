package com.chrissy.model.vo.user.login.wx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author chrissy
 * @Date 9/7/2024 0:14
 * @link <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Passive_user_reply_message.html">
 *     微信官方文档 - 基础消息能力 - 被动回复用户消息
 *     </a>
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JacksonXmlRootElement(localName = "xml")
public class WechatTxtMessageRsp extends BaseWechatMessageVo{
    @JacksonXmlProperty(localName = "Content")
    private String content;

    public WechatTxtMessageRsp() {
        setMsgType("text");
    }
}

package com.chrissy.model.vo.user.login.wx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 微信公众号回复的图文信息
 * @author chrissy
 * @Date 9/9/2024 14:26
 * @link <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Passive_user_reply_message.html">
 *      微信官方文档 - 基础消息能力 - 被动回复用户消息
 *     </a>
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JacksonXmlRootElement(localName = "xml")
public class WechatImageTxtMessageRsp extends BaseWechatMessageVo{
    @JacksonXmlProperty(localName = "ArticleCount")
    private Integer articleCount;
    @JacksonXmlElementWrapper(localName = "Articles")
    @JacksonXmlProperty(localName = "item")
    private List<WechatImageTxtItemVo> articles;

    public WechatImageTxtMessageRsp() {
        setMsgType("news");
    }
}

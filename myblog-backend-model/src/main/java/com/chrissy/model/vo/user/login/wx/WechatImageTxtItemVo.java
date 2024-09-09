package com.chrissy.model.vo.user.login.wx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * 微信公众号回复的单项图文信息
 * @author chrissy
 * @Date 9/9/2024 14:23
 * @link <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Passive_user_reply_message.html#%E5%9B%9E%E5%A4%8D%E5%9B%BE%E6%96%87%E6%B6%88%E6%81%AF">
 *       微信官方文档 - 基础消息能力 - 回复图文消息 item
 *     </a>
 */
@Data
@JacksonXmlRootElement(localName = "item")
public class WechatImageTxtItemVo {
    @JacksonXmlProperty(localName = "Title")
    private String title;
    @JacksonXmlProperty(localName = "Description")
    private String description;
    @JacksonXmlProperty(localName = "PicUrl")
    private String picUrl;
    @JacksonXmlProperty(localName = "Url")
    private String url;
}

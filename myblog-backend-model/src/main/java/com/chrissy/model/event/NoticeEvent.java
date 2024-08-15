package com.chrissy.model.event;

import com.chrissy.model.enums.notice.NoticeTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * 通知事件
 * @author chrissy
 * @date 2024/8/15 9:51
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class NoticeEvent<T> extends ApplicationEvent {
    private NoticeTypeEnum noticeType;
    private T content;

    public NoticeEvent(Object object, NoticeTypeEnum noticeType, T content) {
        super(object);
        this.noticeType = noticeType;
        this.content = content;
    }
}

package com.starix.chat.enums;

/**
 * 消息类型枚举定义
 * @author Tobu
 * @date 2019-12-04 22:40
 */
public enum  MessageTypeEm {
    SINGLE(0, "单聊消息"),
    ROOM(1, "聊天室消息"),
    GROUP(2, "群聊消息"),
    SYSTEM(3,"系统推送消息"),
    FRIEND_REQUEST(4,"好友请求"),
    STATUS_CHANGE(5,"用户上下线通知消息"),
    CALL(6, "呼叫类型消息"),
    CALL_REPLY(7, "呼叫回复消息");

    public Integer type;
    public String content;

    MessageTypeEm(Integer type, String content) {
        this.type = type;
        this.content = content;
    }
}

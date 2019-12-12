package com.starix.chat.common;

import com.starix.chat.enums.MessageTypeEm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 自定义消息体
 * @author Tobu
 * @date 2019-12-04 22:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageData {

    //消息发送方
    private String fromUid;

    //发送方用户名
    private String username;

    //消息接收方
    private String toUid;

    //消息内容
    private String content;

    //消息类型，取值定义在MessageTypeEm枚举类
    private Integer type;

    //消息发送时间
    private Date time;


    /**
     * 构建系统消息
     * @param content 消息内容
     * @return
     */
    public static MessageData serverMsg(String content){
        return new MessageData(null, null, null, content, MessageTypeEm.SYSTEM.type, new Date());
    }


    /**
     * 构建指定接收用户的系统消息
     * @param content 消息内容
     * @return
     */
    public static MessageData serverMsg(String content, String toUid){
        return new MessageData(null, null, toUid, content, MessageTypeEm.SYSTEM.type, new Date());
    }


    /**
     * 构建单聊消息
     * @param fromUid 发送方ID
     * @param toUid 接收方ID
     * @param content 消息内容
     * @return
     */
    public static MessageData singleMsg(String fromUid, String username, String toUid, String content){
        return new MessageData(fromUid, username, toUid, content, MessageTypeEm.SINGLE.type, new Date());
    }

    /**
     * 构建聊天室消息
     * @param fromUid 发送方ID
     * @param content 消息内容
     * @return
     */
    public static MessageData roomMsg(String fromUid, String username, String content){
        return new MessageData(fromUid, username, null, content, MessageTypeEm.ROOM.type, new Date());
    }


    /**
     * 构建在线状态变更消息
     * @param content 消息内容
     * @return
     */
    public static MessageData statusChangeMsg(String content){
        return new MessageData(null, null, null, content, MessageTypeEm.STATUS_CHANGE.type, new Date());
    }

}

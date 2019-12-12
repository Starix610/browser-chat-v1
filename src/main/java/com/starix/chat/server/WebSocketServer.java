package com.starix.chat.server;

import com.starix.chat.config.MessageDecoder;
import com.starix.chat.config.MessageEncoder;
import com.starix.chat.common.MessageData;
import com.starix.chat.entity.User;
import com.starix.chat.enums.MessageTypeEm;
import com.starix.chat.service.MessageService;
import com.starix.chat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Tobu
 * @date 2019-12-03 22:08
 */
@ServerEndpoint(value = "/websocket/{uid}", decoders = {MessageDecoder.class}, encoders ={MessageEncoder.class})
@Component
@Slf4j
public class WebSocketServer {

    //静态变量，记录当前总在线连接数。应该把它设计成线程安全的。
    public static int onlineCount = 0;

    //concurrent包的线程安全HashMap，用来存放每个客户端对应的WebSocketServer对象。
    public static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //当前用户uid
    private String uid = "";

    //记录上线时间
    public Date loginTime = new Date();

    //这里通过配置类注入service
    public static MessageService messageService;

    public static UserService userService;


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {
        this.session = session;
        webSocketMap.put(uid, this);
        addOnlineCount();
        this.uid = uid;
        User user = userService.getById(uid);
        log.info("有新客户端连接:{},当前在线人数为{}", uid, getOnlineCount());
        try {
            sendMessage(MessageData.serverMsg("连接成功", uid));
            sendRoomMessage(MessageData.statusChangeMsg(String.format("用户[%s]已上线",user.getUsername())));
        } catch (Exception e) {
            log.error("websocket连接异常");
        }
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketMap.remove(uid);
        subOnlineCount();
        User user = userService.getById(uid);
        //发送下线消息给客户端
        sendRoomMessage(MessageData.statusChangeMsg(String.format("用户[%s]已下线", user.getUsername())));
        log.info("有一连接关闭！当前在线人数:{}", getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param msgData 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(MessageData msgData, Session session) {
        log.info("收到来自客户端[{}]的信息:{}", uid, msgData);

        msgData.setTime(new Date());

        //聊天室消息，转发给所有其他所有在线客户端
        if (msgData.getType() == MessageTypeEm.ROOM.type){
            sendRoomMessage(msgData);
        }

        //单聊或者CALL消息，转发给指定用户
        if(msgData.getType() == MessageTypeEm.SINGLE.type
                || msgData.getType() == MessageTypeEm.CALL.type
                || msgData.getType() == MessageTypeEm.CALL_REPLY.type){
            try {
                sendMessage(msgData);
            } catch (Exception e) {
                log.error("发送消息给[{}]失败", msgData.getToUid(), e);
            }
        }

    }

    /**
     * 连接发生错误的时候
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误", error);
    }


    /**
     * 发送信息给某个客户端
     * @param msgData
     */
    public static void sendMessage(MessageData msgData) throws Exception{
        if (webSocketMap.get(msgData.getToUid()) == null){
            log.warn("用户[{}]不在线", msgData.getToUid());
            // TODO: 2019-12-05 这里可以对未接收的消息做处理·

        } else {
            webSocketMap.get(msgData.getToUid()).session.getBasicRemote().sendObject(msgData);
        }
    }



    /**
     * 发送信息给其他所有在线客户端(适用于聊天室和上下线通知)
     * @param msgData
     */
    public void sendRoomMessage(MessageData msgData) {
        for(Map.Entry<String, WebSocketServer> entry: webSocketMap.entrySet()){
            try {
                //不发送给当前用户自己
                if (entry.getKey().equals(this.uid)){
                    continue;
                }
                entry.getValue().session.getBasicRemote().sendObject(msgData);
            } catch (Exception e) {
                log.error("发送消息给[{}]失败", entry.getKey(), e);
            }
        }
    }

    /**
     * 发送信息给所有在线客户端(适用于系统通知)
     * @param msgData
     */
    public static void sendSystemMessage(MessageData msgData) {

        for(Map.Entry<String, WebSocketServer> entry: webSocketMap.entrySet()){
            try {
                entry.getValue().session.getBasicRemote().sendObject(msgData);
            } catch (Exception e) {
                log.error("发送消息给[{}]失败", entry.getKey(), e);
            }
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}

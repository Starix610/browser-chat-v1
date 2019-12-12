package com.starix.chat.controller;

import com.starix.chat.common.MessageData;
import com.starix.chat.server.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Tobu
 * @date 2019-12-04 19:48
 */
@RestController
@Slf4j
public class PushController {

    @PostMapping("/websocket/push")
    public String push(@RequestParam(value = "uid",required = false) String uid,
                       @RequestParam("message") String message){
        try {
            if (StringUtils.isEmpty(uid)){
                log.info("推送消息给所有客户端,推送内容:{}", message);
                WebSocketServer.sendSystemMessage (MessageData.serverMsg(message));
            }else{
                log.info("推送消息给客户端[{}],推送内容:{}", uid, message);
                WebSocketServer.sendMessage(MessageData.serverMsg(message));
            }
        } catch (Exception e) {
            log.error("推送消息失败", e);
        }
        return "success";
    }



}

package com.starix.chat.config;

import com.starix.chat.server.WebSocketServer;
import com.starix.chat.service.GroupService;
import com.starix.chat.service.MessageService;
import com.starix.chat.service.UserGroupService;
import com.starix.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author Tobu
 * @date 2019-12-03 21:36
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


    /**
     * 因 SpringBoot WebSocket 对每个客户端连接都会创建一个 WebSocketServer（@ServerEndpoint 注解对应的）对象，
     * Bean 注入操作会被直接略过，因而手动注入一个全局变量
     * @param messageService
     */
    @Autowired
    public void setMessageService(MessageService messageService){
        WebSocketServer.messageService = messageService;
    }

    @Autowired
    public void setGroupService(UserService userService){
        WebSocketServer.userService = userService;
    }


}

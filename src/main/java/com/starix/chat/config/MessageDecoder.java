package com.starix.chat.config;

import com.alibaba.fastjson.JSON;
import com.starix.chat.common.MessageData;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * 自定义消息解码器
 * @author Tobu
 * @date 2019-12-05 13:06
 */
public class MessageDecoder implements Decoder.Text<MessageData> {
    @Override
    public MessageData decode(String message) throws DecodeException {
        return JSON.parseObject(message, MessageData.class);
    }


    /**
     * 在这里可以对本次消息做预处理
     * 这个方法决定是否进行解码
     */
    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

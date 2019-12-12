package com.starix.chat.config;

import com.alibaba.fastjson.JSON;
import com.starix.chat.common.MessageData;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * 自定义消息编码器
 * @author Tobu
 * @date 2019-12-05 13:05
 */
public class MessageEncoder implements Encoder.Text<MessageData> {
    @Override
    public String encode(MessageData object) throws EncodeException {
        return JSON.toJSONString(object);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

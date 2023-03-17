package com.example.iotnettyrabbitmq.util;


import com.example.iotnettyrabbitmq.pohoVo.SocketMsgVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * @Author : baye
 * @Date : 2023/3/23 15:06
 * @Code : bug and work
 * @Description : Websocket发送信息SocketMsg对象编码类
 */
public class ServerEncoder implements Encoder.Text<SocketMsgVo>{
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        // 这里不重要
    }

    @Override
    public void init(EndpointConfig arg0) {
        // TODO Auto-generated method stub
        // 这里也不重要

    }

    /*
     *  encode()方法里的参数和Text<T>里的T一致，如果你是Student，这里就是encode（Student student）
     */
    @Override
    public String encode(SocketMsgVo socketMsgVo) throws EncodeException {
        try {
            /*
             * 这里是重点，只需要返回Object序列化后的json字符串就行
             * 你也可以使用gosn，fastJson来序列化。
             */
            JsonMapper jsonMapper = new JsonMapper();
            return jsonMapper.writeValueAsString(socketMsgVo);

        } catch ( JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

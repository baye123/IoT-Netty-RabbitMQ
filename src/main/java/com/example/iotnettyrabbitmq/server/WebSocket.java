package com.example.iotnettyrabbitmq.server;

import com.example.iotnettyrabbitmq.pohoVo.SocketMsgVo;
import com.example.iotnettyrabbitmq.pojo.SocketMsg;
import com.example.iotnettyrabbitmq.util.Analysis;
import com.example.iotnettyrabbitmq.util.RabbitUtil;
import com.example.iotnettyrabbitmq.util.ServerEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author : baye
 * @Date : 2023/3/22 13:58
 * @Code : bug and work
 * @Description : websocket服务
 */
@Component
@Slf4j
@ServerEndpoint(value = "/websocket/web", encoders = {ServerEncoder.class}) //暴露的ws应用的路径
public class WebSocket {

    // 用来存储服务连接对象
    private static Map<String , Session> clientMap = new ConcurrentHashMap<>();

    //用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //解析命令的方法对象
    private Analysis analysis = new Analysis();
    //消息数据存储的实体类对象
    private SocketMsgVo socketMsgVo = new SocketMsgVo();

    /**
     * 客户端与服务端连接成功
     * @param session
     */
    @OnOpen
    public void onOpen(Session session){
        /*
            do something for onOpen
            与当前客户端连接成功时
         */
        System.out.println("连接成功");
        clientMap.put(session.getId(),session);

    }

    /**
     * 客户端与服务端连接关闭
     * @param session
     */
    @OnClose
    public void onClose(Session session){
        /*
            do something for onClose
            与当前客户端连接关闭时
         */
        System.out.println("连接关闭");
        clientMap.remove(session.getId());
    }

    /**
     * 客户端与服务端连接异常
     * @param error
     * @param session
     */
    @OnError
    public void onError(Throwable error,Session session) {

        System.out.println("连接错误");
        error.printStackTrace();
    }

    /**
     * 客户端向服务端发送消息
     * @param message
     * @throws IOException
     */
    @OnMessage
    public void onMsg(Session session,String message) throws IOException {
        /*
            do something for onMessage
            收到来自当前客户端的消息时
         */
//        sendAllMessage(message);


        this.session = session;
        ObjectMapper objectMapper = new ObjectMapper();
        SocketMsgVo socketMsgVo;
        try{
            socketMsgVo = objectMapper.readValue(message, SocketMsgVo.class);
            Channel channel = RabbitUtil.getChannel();
            RabbitUtil rabbitUtil = new RabbitUtil();
            rabbitUtil.channelReceive(
                    channel,
                    socketMsgVo.getExchangName(),
                    socketMsgVo.getQueueName(),
                    socketMsgVo.getRoutingKey());
            this.socketMsgVo = socketMsgVo;
            // RabbitMQ收到消息的回调接口
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message2 = new String(delivery.getBody(), "UTF-8");
                System.out.println(" 接收绑定键 :" + delivery.getEnvelope().getRoutingKey() + ", 消息:" + message2);
                this.socketMsgVo.setTime(analysis.cutmsg(message2));
                String news =analysis.getNews();
                String[] bitmap = analysis.hexToIntarray(news);
                this.socketMsgVo.setEquipmentId(analysis.getEquipmentId(news));
                this.socketMsgVo.setStatus(analysis.descriptionStatus(bitmap));
                this.socketMsgVo.setStatus_value(bitmap);

                this.session.getAsyncRemote().sendObject(this.socketMsgVo);

                System.out.println("接收数据并发送至前端。");


            };
//         接收信息
            channel.basicConsume(socketMsgVo.getQueueName(), true, deliverCallback, consumerTag -> {
            });
        }catch (Exception e){
        }


    }

    //向所有客户端发送消息（广播）
    private void sendAllMessage(String message){
        Set<String> sessionIdSet = clientMap.keySet(); //获得Map的Key的集合
        // 此处相当于一个广播操作
        for (String sessionId : sessionIdSet) { //迭代Key集合
            Session session = clientMap.get(sessionId); //根据Key得到value
            session.getAsyncRemote().sendText(message); //发送消息给客户端
        }
    }
}

package com.example.iotnettyrabbitmq.server;

import com.example.iotnettyrabbitmq.mapper.SocketMsgMapper;
import com.example.iotnettyrabbitmq.pohoVo.SocketMsgVo;
import com.example.iotnettyrabbitmq.pojo.SocketMsg;
import com.example.iotnettyrabbitmq.service.SocketMsgService;
import com.example.iotnettyrabbitmq.service.impl.SocketMsgServiceImpl;
import com.example.iotnettyrabbitmq.util.Analysis;
import com.example.iotnettyrabbitmq.util.RabbitUtil;
import com.example.iotnettyrabbitmq.util.ServerEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;


import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
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

    //保留当前数据和前一个历史数据，用于实时数据更新
    private  static SocketMsgVo[] socketMsgVos = new SocketMsgVo[2];

    //


    @Autowired
    private SocketMsgMapper socketMsgMapper;

    public static WebSocket webSocket;
    @PostConstruct
    public void init(){
        webSocket = this;
        webSocket.socketMsgMapper = this.socketMsgMapper;
    }

    public static int saveMessage(SocketMsg socketMsg){
        return webSocket.socketMsgMapper.saveMessage(socketMsg);
    }



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
        SocketMsgServiceImpl socketMsgServiceImpl;

        try{
            //前端收到的数组字符串转化为实体类对象
            socketMsgVo = objectMapper.readValue(message, SocketMsgVo.class);
            //消费消RabbitMQ队列中的消息
            Channel channel = RabbitUtil.getChannel();
            RabbitUtil rabbitUtil = new RabbitUtil();
            rabbitUtil.channelReceive(
                    channel,
                    socketMsgVo.getExchangName(),
                    socketMsgVo.getQueueName(),
                    socketMsgVo.getRoutingKey());
            this.socketMsgVo = socketMsgVo;
            System.out.println("绑定成功？");

            // RabbitMQ收到消息的回调接口
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message2 = new String(delivery.getBody(), "UTF-8");
                System.out.println(" 接收绑定键 :" + delivery.getEnvelope().getRoutingKey() + ", 消息:" + message2);

                //当前时间
                this.socketMsgVo.setTime(analysis.cutmsg(message2));
                //切割时间戳之后的消息
                String news =analysis.getNews();
                this.socketMsgVo.setOriginal_information(news);
                //解析消息后的位图
                String[] bitmap = analysis.hexToIntarray(news);
                //解析消息后的设备ID
                String ID = analysis.getEquipmentId(news);
                this.socketMsgVo.setEquipmentId(ID);
                //解析消息后的设备表名
                String table = "equipment_" + ID.toLowerCase();
                this.socketMsgVo.setTable(table);
                //解析消息后的设备状态状况
//                this.socketMsgVo.setStatus(analysis.descriptionStatus(bitmap));
                //解析消息后的设备状态值
                this.socketMsgVo.setStatus_value(bitmap);
                //解析消息后的设备安灯状态值
                this.socketMsgVo.setLight(analysis.lightStatus(bitmap));
                //解析消息后的设备各DI通道单独情况
                this.socketMsgVo.setDI_1(bitmap[7]);
                this.socketMsgVo.setDI_2(bitmap[6]);
                this.socketMsgVo.setDI_3(bitmap[5]);
                this.socketMsgVo.setDI_4(bitmap[4]);
                this.socketMsgVo.setDI_5(bitmap[3]);
                this.socketMsgVo.setDI_6(bitmap[2]);
                this.socketMsgVo.setDI_7(bitmap[1]);
                this.socketMsgVo.setDI_8(bitmap[0]);
                if(socketMsgVos[0] == null){
//                    this.socketMsgVo.setCause("初次启动/重新启动");
                    //浅拷贝对象
                    socketMsgVos[0] =(SocketMsgVo)this.socketMsgVo.clone();
                    socketMsgVos[1] =(SocketMsgVo)this.socketMsgVo.clone();
                }else {
                    //记录先前状态值
//                    this.socketMsgVo.setStatus_value2(socketMsgVos[0].getStatus_value());
                    //记录先前时间
//                    this.socketMsgVo.setTime2(socketMsgVos[0].getTime());
                    //记录先前状况
//                    this.socketMsgVo.setStatus2(socketMsgVos[0].getStatus());
                    //记录改变原因
//                    this.socketMsgVo.setCause(analysis.statusChange(socketMsgVos[0].getStatus_value(),this.socketMsgVo.getStatus_value()));

                    socketMsgVos[1] =(SocketMsgVo)socketMsgVos[0].clone();
                    socketMsgVos[0] =(SocketMsgVo)this.socketMsgVo.clone();
                }
                SocketMsg socketMsg = new SocketMsg();
                socketMsg.setExchangName(socketMsgVos[0].getExchangName());
                socketMsg.setRoutingKey(socketMsgVos[0].getRoutingKey());
                socketMsg.setQueueName(socketMsgVos[0].getQueueName());
                socketMsg.setEquipmentId(socketMsgVos[0].getEquipmentId());
                socketMsg.setTable(socketMsgVos[0].getTable());
                socketMsg.setOriginal_information(socketMsgVos[0].getOriginal_information());
                String str = (Arrays.toString(socketMsgVos[0].getStatus_value()));
                socketMsg.setStatus_value(str);
                socketMsg.setTime(socketMsgVos[0].getTime());
//                socketMsg.setStatus(socketMsgVos[0].getStatus());
//                socketMsg.setCause(socketMsgVos[0].getCause());
                socketMsg.setLight(socketMsgVos[0].getLight());
                socketMsg.setDI_1(socketMsgVos[0].getDI_1());
                socketMsg.setDI_2(socketMsgVos[0].getDI_2());
                socketMsg.setDI_3(socketMsgVos[0].getDI_3());
                socketMsg.setDI_4(socketMsgVos[0].getDI_4());
                socketMsg.setDI_5(socketMsgVos[0].getDI_5());
                socketMsg.setDI_6(socketMsgVos[0].getDI_6());
                socketMsg.setDI_7(socketMsgVos[0].getDI_7());
                socketMsg.setDI_8(socketMsgVos[0].getDI_8());
                socketMsg.setOriginal_information(socketMsgVos[0].getOriginal_information());
                int flag = saveMessage(socketMsg);
//                System.out.println(flag);

                this.session.getAsyncRemote().sendObject(socketMsgVos[0]);

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

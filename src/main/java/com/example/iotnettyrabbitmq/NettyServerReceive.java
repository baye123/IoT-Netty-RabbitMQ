package com.example.iotnettyrabbitmq;

import com.example.iotnettyrabbitmq.netty.NettyClient;
import com.example.iotnettyrabbitmq.netty.NettyServer;
import com.example.iotnettyrabbitmq.pojo.PosttingObject;
import com.example.iotnettyrabbitmq.util.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.Scanner;

/**
 * @Author : baye
 * @Date : 2023/3/17 9:51
 * @Code : bug and work
 * @Description : 服务器端接收者
 */
public class NettyServerReceive {
    private static final String EXCHANGE_NAME = "direct_IoT";
    private static final String queueName = "console-1";


    public static void main(String[] args) throws Exception {
        String host = "169.254.152.77";
        String ClientIp = "169.254.152.78";
        final NettyServer nettyServer = new NettyServer("direct_IoT","equipment-1");
        new Thread() {
            @Override
            public void run() {
                int port = 9999;
                nettyServer.initNetty("1",host, port);
            }
        }.start();

        String msg = "000100000006FF0500640000";
        System.out.println("你好2");
//        String msg = "000100000006FF050064FF00";

        nettyServer.writeMsg(msg,ClientIp);


        Channel channel = RabbitUtil.getChannel();
        RabbitUtil rabbitUtil = new RabbitUtil();
        rabbitUtil.channelReceive(channel,EXCHANGE_NAME,queueName,"equipment-1");
        //声明交换模式
//        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //        channel.queueDeclare(param 1, param2, param3, param4, param5)
//        生成一个队列
//        1.队列名称
//        2.队列里面的消息是否持久化 默认消息存储在内存中
//        3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
//        4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
//        5.其他参数
//        nettyClient.clientHandler.channel.queueDeclare(queueName, true, false, false, null);
        // 绑定队列和交换机
//        channel.queueBind(queueName, EXCHANGE_NAME, "equipment-1");
//
//        System.out.println("等待接收消息........... ");
//        // 收到消息的回调接口
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String message = new String(delivery.getBody(), "UTF-8");
//            System.out.println(" 接收绑定键 :" + delivery.getEnvelope().getRoutingKey() + ", 消息:" + message);
//        };
////         接收信息
//        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
//        });
    }
}

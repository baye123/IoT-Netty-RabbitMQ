package com.example.iotnettyrabbitmq;

import com.example.iotnettyrabbitmq.netty.NettyClient;
import com.example.iotnettyrabbitmq.pojo.PosttingObject;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Author : baye
 * @Date : 2023/3/16 11:53
 * @Code : bug and work
 * @Description : 测试Netty+RabbitMQ连接
 */
public class ReceiveLogsDirect {
    private static final String EXCHANGE_NAME = "direct_IoT";
    private static final String queueName = "console-1";

    public static void main(String[] argv) throws Exception {
        String userId = "1";
        String DO_1open = "000100000006FF050064FF00";
        String DO_1close = "000100000006FF0500640000";
        NettyClient nettyClient = new NettyClient();
        nettyClient.initNetty(userId, "169.254.152.78", 502);
        PosttingObject posttingObject = NettyClient.concurrentHashMap.get(userId);
        try {
            // 发送消息
            nettyClient = posttingObject.getNettyClient();
//            nettyClient.clientHandler.channelHandlerContext.writeAndFlush(DO_1open);
            nettyClient.clientHandler.channelHandlerContext.writeAndFlush(DO_1close);

        } finally {
            // 优雅关闭连接
//            posttingObject.getNioEventLoopGroup().shutdownGracefully();
        }
        //声明交换模式
        nettyClient.clientHandler.channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //        channel.queueDeclare(param 1, param2, param3, param4, param5)
//        生成一个队列
//        1.队列名称
//        2.队列里面的消息是否持久化 默认消息存储在内存中
//        3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
//        4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
//        5.其他参数
//        nettyClient.clientHandler.channel.queueDeclare(queueName, true, false, false, null);
        // 绑定队列和交换机
        nettyClient.clientHandler.channel.queueBind(queueName, EXCHANGE_NAME, "equipment-1");
        System.out.println("等待接收消息........... ");
        // 收到消息的回调接口
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" 接收绑定键 :" + delivery.getEnvelope().getRoutingKey() + ", 消息:" + message);
        };
//         接收信息
        nettyClient.clientHandler.channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}

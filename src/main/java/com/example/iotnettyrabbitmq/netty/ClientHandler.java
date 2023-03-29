package com.example.iotnettyrabbitmq.netty;

import com.example.iotnettyrabbitmq.util.DateString;
import com.example.iotnettyrabbitmq.util.RabbitUtil;
import com.rabbitmq.client.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : baye
 * @Date : 2023/3/15 16:37
 * @Code : bug and work
 * @Description : Netty客户端操作类
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {
    public ChannelHandlerContext channelHandlerContext;
    //rabbitmq交换机名称
    private String EXCHANGE_NAME = "direct_IoT";

    private String RoutingKey = "equipment-1";
    //创建多个 bindingKey
    Map<String, String> bindingKeyMap = new HashMap<>();

    public Channel channel;

    private NettyClient nettyClient;

    ClientHandler(){
        EXCHANGE_NAME = "direct_IoT";

        RoutingKey = "equipment-1";
    }
    ClientHandler(String exchangeName,String routingKey,NettyClient nettyClient){
        EXCHANGE_NAME = exchangeName;
        RoutingKey = routingKey;
        this.nettyClient = nettyClient;

    }



    /**
     * 活跃通道可以发送消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channelHandlerContext = ctx;
        //channelHandlerContext.writeAndFlush("11111");
        //channelHandlerContext.channel().close();
    }

    /**
     *
     * 通道不活跃时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("运行中断开重连。。。");
        nettyClient.connect();
    }


    /**
     * 读取消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        //声明交换机、类型
//        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        long millisecond = System.currentTimeMillis();
        
        String data = DateString.millisecondToStringLong(millisecond);

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //创建多个 bindingKey
//        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put(RoutingKey, millisecond + "\n" + msg);

        for (Map.Entry<String, String> bindingKeyEntry :
                bindingKeyMap.entrySet()) {
            String bindingKey = bindingKeyEntry.getKey();
            String message = bindingKeyEntry.getValue();
            //消息持久化 MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish(EXCHANGE_NAME, bindingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息:" + message);
        }
        



        //声明发送到哪个消息队列
//        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

//        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        System.out.println("Netty客户端接收消息：" + msg);
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, String msg) throws Exception {
//    }
    //channelHandlerContext.channel().close();
//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Byte msg) throws Exception {
//        System.out.println("接收消息：" + msg);
//    }


}

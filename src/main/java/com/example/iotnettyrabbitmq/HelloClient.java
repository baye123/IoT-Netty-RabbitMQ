package com.example.iotnettyrabbitmq;

import com.example.iotnettyrabbitmq.netty.NettyClient;
import com.example.iotnettyrabbitmq.pojo.PosttingObject;

/**
 * @Author : baye
 * @Date : 2023/3/15 17:10
 * @Code : bug and work
 * @Description : Netty测试类
 */
public class HelloClient {
    public static void main(String[] args) throws Exception {
        String userId = "1";
        String DO_1open = "000100000006FF050064FF00";
        String DO_1close = "000100000006FF0500640000";
        new NettyClient("direct_IoT","equipment-1").initNetty(userId, "169.254.152.78", 502);
        PosttingObject posttingObject = NettyClient.concurrentHashMap.get(userId);
        try{
            // 发送消息
            NettyClient nettyClient = posttingObject.getNettyClient();
//            nettyClient.clientHandler.channelHandlerContext.writeAndFlush(DO_1open);
            nettyClient.clientHandler.channelHandlerContext.writeAndFlush(DO_1open);

        }finally {
            // 优雅关闭连接
//            posttingObject.getNioEventLoopGroup().shutdownGracefully();
        }
    }
}

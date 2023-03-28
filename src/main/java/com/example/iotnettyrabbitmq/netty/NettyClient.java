package com.example.iotnettyrabbitmq.netty;

import com.example.iotnettyrabbitmq.pojo.PosttingObject;
import com.example.iotnettyrabbitmq.util.MyDecoder;
import com.example.iotnettyrabbitmq.util.MyEncoder;
import com.example.iotnettyrabbitmq.util.RabbitUtil;
import com.rabbitmq.client.Channel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author : baye
 * @Date : 2023/3/15 17:03
 * @Code : bug and work
 * @Description : Netty客户端实现长连接
 */
public class NettyClient {
    /** 存储用户id和netty连接关系 */
    public static ConcurrentHashMap<String, PosttingObject> concurrentHashMap = new ConcurrentHashMap();
    public ClientHandler clientHandler;


    public NettyClient(String exchangName,String routingKey){
        this.clientHandler = new ClientHandler(exchangName,routingKey);
    }
    public void initNetty(String userId, String host, int port){
        //创建nioEventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("正在连接中...");
//                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
//                        ch.pipeline().addLast(new StringDecoder());
//                        ch.pipeline().addLast(new StringEncoder());
//                        ch.pipeline().addLast(clientHandler);
                        ChannelPipeline p = ch.pipeline();
//                        p.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
//                        p.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                        p.addLast("decoder", new MyEncoder());
                        p.addLast("encoder", new MyDecoder());
                        p.addLast(clientHandler);
                    }
                });

        try {
            // 发起连接
            ChannelFuture sync = bootstrap.connect().sync();
            System.out.println("用户："+userId + "->服务端连接成功...");

            //Netty连接同时，建立RabbitMQ通道
            clientHandler.channel = RabbitUtil.getChannel();
            /* // 发送消息
            sync.channel().writeAndFlush(msg);
            System.out.println("消息发送完成");
            // 关闭连接
            sync.channel().close();*/

            // 异步等待关闭连接channel
            // sync.channel().closeFuture().sync();
            // System.out.println("连接已关闭..");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //group.shutdownGracefully();
            //System.out.println("优雅关闭连接");
        }

        PosttingObject posttingObject = new PosttingObject();
        posttingObject.setNioEventLoopGroup(group);
        posttingObject.setNettyClient(this);
        concurrentHashMap.put(userId, posttingObject);
    }
}

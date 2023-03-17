package com.example.iotnettyrabbitmq.pojo;

import com.example.iotnettyrabbitmq.netty.NettyClient;
import com.example.iotnettyrabbitmq.netty.NettyServer;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Data;

/**
 * @Author : baye
 * @Date : 2023/3/15 17:04
 * @Code : bug and work
 * @Description : Netty消息实体类
 */
@Data
public class PosttingObject {
    private NettyClient nettyClient;

    private NettyServer nettyServer;

    private NioEventLoopGroup nioEventLoopGroup;
}

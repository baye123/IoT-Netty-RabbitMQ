package com.example.iotnettyrabbitmq.controller;

import com.example.iotnettyrabbitmq.netty.NettyClient;
import com.example.iotnettyrabbitmq.netty.NettyServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.transform.sax.SAXResult;

/**
 * @Author : baye
 * @Date : 2023/3/21 15:18
 * @Code : bug and work
 * @Description : Netty连接控制类
 */
@Controller
@RequestMapping("/Netty")
public class NettyController {

    @ResponseBody
    @PostMapping("/server")
    public void startNettyServer(@RequestParam("host")String host,@RequestParam("port") String port,@RequestParam("exchangName") String exchangName,@RequestParam("routingKey") String routingKey){
        System.out.println(host);
        System.out.println(port);
        System.out.println(exchangName);
        System.out.println(routingKey);
        NettyServer nettyServer = new NettyServer(exchangName,routingKey);
        int port1 = Integer.parseInt(port);
        nettyServer.initNetty("1",host,port1);
    }

    @ResponseBody
    @PostMapping("/client")
    public void startNettyClient(@RequestParam("clientIp")String clientIp,@RequestParam("port")String port,@RequestParam("exchangName")String exchangName,@RequestParam("routingKey")String routingKey){
        System.out.println(clientIp);
        System.out.println(port);
        System.out.println(exchangName);
        System.out.println(routingKey);
        NettyClient nettyClient = new NettyClient(exchangName,routingKey);
        int port1 = Integer.parseInt(port);
        try {
            nettyClient.initNetty("1",clientIp,port1);
            nettyClient.connect();
        }catch (Exception e){
            System.out.println("Netty连接失败");
            e.printStackTrace();
        }

    }



    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/socketclient")
    public String socketclient(){ return "socketclient";}
}

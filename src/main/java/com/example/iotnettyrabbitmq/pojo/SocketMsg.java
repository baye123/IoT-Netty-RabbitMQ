package com.example.iotnettyrabbitmq.pojo;

import lombok.Data;

/**
 * @Author : baye
 * @Date : 2023/3/22 14:56
 * @Code : bug and work
 * @Description :
 */
@Data
public class SocketMsg {

        String exchangName; //RabbitMQ交换机名称
        String routingKey; //交换机路由键
        String queueName; //队列名
        String equipmentId; //设备id
        String[] status_value; //当前状态值
        String time;//时间
        String status; //通道状况
        String cause; //改变原因
}

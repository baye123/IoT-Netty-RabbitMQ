package com.example.iotnettyrabbitmq.pojo;

import lombok.Data;

/**
 * @Author : baye
 * @Date : 2023/3/22 14:56
 * @Code : bug and work
 * @Description :通信数据实体类
 */
@Data
public class SocketMsg {
        private Long id;

        private String exchangName; //RabbitMQ交换机名称

        private String routingKey; //交换机路由键

        private String queueName; //队列名

        private String equipmentId; //设备id

        private String status_value; //当前状态值

        private String time;//时间

        private String status; //通道状况

        private String cause; //改变原因
}

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

        private String table; //对应表名

        private String light; //安灯状态

        private String status_value; //当前状态值

        private String time;//时间

        private String DI_1;
        private String DI_2;
        private String DI_3;
        private String DI_4;
        private String DI_5;
        private String DI_6;
        private String DI_7;
        private String DI_8;


        private String original_information; //原始消息

        private int mold_closing; //合模次数
}

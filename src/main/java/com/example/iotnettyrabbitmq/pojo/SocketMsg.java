package com.example.iotnettyrabbitmq.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author : baye
 * @Date : 2023/3/22 14:56
 * @Code : bug and work
 * @Description :通信数据实体类
 */
@Data
@EqualsAndHashCode
public class SocketMsg {
        //导入忽略该参数
        @ExcelIgnore
        private Long id;
        @ExcelIgnore
        private String exchangName; //RabbitMQ交换机名称
        @ExcelIgnore
        private String routingKey; //交换机路由键
        @ExcelIgnore
        private String queueName; //队列名
        @ExcelProperty("设备号")
        private String equipmentId; //设备id
        @ExcelIgnore
        private String table; //对应表名
        @ExcelProperty("安灯状态")
        private String light; //安灯状态
        @ExcelProperty("DI通道状态")
        private String status_value; //当前状态值
        @ExcelProperty("接收消息时间")
        private String time;//时间
        @ExcelProperty("DI_1")
        private String DI_1;
        @ExcelProperty("DI_2")
        private String DI_2;
        @ExcelProperty("DI_3")
        private String DI_3;
        @ExcelProperty("DI_4")
        private String DI_4;
        @ExcelProperty("DI_5")
        private String DI_5;
        @ExcelProperty("DI_6")
        private String DI_6;
        @ExcelProperty("DI_7")
        private String DI_7;
        @ExcelProperty("DI_8")
        private String DI_8;
        @ExcelProperty("原始消息")
        private String original_information; //原始消息
        @ExcelIgnore
        private int mold_closing; //合模次数
}

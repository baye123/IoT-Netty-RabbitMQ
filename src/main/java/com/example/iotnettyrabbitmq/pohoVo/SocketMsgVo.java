package com.example.iotnettyrabbitmq.pohoVo;

import lombok.Data;

/**
 * @Author : baye
 * @Date : 2023/3/23 10:33
 * @Code : bug and work
 * @Description : 消息对象Vo类
 */
@Data
public class SocketMsgVo implements Cloneable{
    String exchangName; //RabbitMQ交换机名称
    String routingKey; //交换机路由键
    String queueName; //队列名
    String equipmentId; //设备id

    String[] status_value; //当前状态值
    String time;//时间
    String status; //通道状况

    String[] status_value2; //先前状态值
    String time2; //先前时间
    String status2; //先前通道状况
    String cause; //改变原因

    //继承浅拷贝
    @Override
    public Object clone(){
        try{
            return (SocketMsgVo) super.clone();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}

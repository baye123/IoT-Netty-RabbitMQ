package com.example.iotnettyrabbitmq.service;

import com.example.iotnettyrabbitmq.pojo.SocketMsg;

/**
 * @Author : baye
 * @Date : 2023/3/24 9:40
 * @Code : bug and work
 * @Description : socket数据服务类
 */
public interface SocketMsgService {

    //保存一条设备数据
    public int saveMessage(SocketMsg socketMsg);
}

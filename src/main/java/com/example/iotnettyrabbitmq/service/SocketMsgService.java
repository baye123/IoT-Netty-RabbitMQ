package com.example.iotnettyrabbitmq.service;

import com.example.iotnettyrabbitmq.pojo.SocketMsg;

import java.util.List;

/**
 * @Author : baye
 * @Date : 2023/3/24 9:40
 * @Code : bug and work
 * @Description : socket数据服务类
 */
public interface SocketMsgService {

    //保存一条设备数据
    public int saveMessage(SocketMsg socketMsg);

    //查询设备表
    List<SocketMsg> searchMessage(String equipmentId,String startTime,String endTime);
}

package com.example.iotnettyrabbitmq.service.impl;

import com.example.iotnettyrabbitmq.mapper.SocketMsgMapper;
import com.example.iotnettyrabbitmq.pojo.SocketMsg;
import com.example.iotnettyrabbitmq.service.SocketMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author : baye
 * @Date : 2023/3/24 9:43
 * @Code : bug and work
 * @Description :
 */
@Service
public class SocketMsgServiceImpl implements SocketMsgService {

    @Autowired
    private SocketMsgMapper socketMsgMapper;

    @Override
    public int saveMessage(SocketMsg socketMsg){
        return socketMsgMapper.saveMessage(socketMsg);
    }
    @Override
    public List<SocketMsg> searchMessage(String equipmentId,String startTime,String endTime){
        return socketMsgMapper.searchMessage(equipmentId,startTime,endTime);
    }

}

package com.example.iotnettyrabbitmq.mapper;

import com.example.iotnettyrabbitmq.pojo.SocketMsg;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author : baye
 * @Date : 2023/3/24 9:45
 * @Code : bug and work
 * @Description : SocketMsg Mapper类 持久层
 */
@Repository
@Mapper
public interface SocketMsgMapper {
    //保存一条设备类数据
    int saveMessage(SocketMsg socketMsg);
}

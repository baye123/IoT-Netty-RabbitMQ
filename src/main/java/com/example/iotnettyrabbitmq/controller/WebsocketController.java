package com.example.iotnettyrabbitmq.controller;

import com.example.iotnettyrabbitmq.pojo.SocketMsg;
import com.example.iotnettyrabbitmq.service.SocketMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author : baye
 * @Date : 2023/3/23 10:46
 * @Code : bug and work
 * @Description : websocket连接控制类
 */
@Controller
@RequestMapping("/Websocket")
public class WebsocketController {
    @Autowired
    private SocketMsgService socketMsgService;


    @ResponseBody
    @GetMapping("/list")
    public List<SocketMsg> searchEquipmentList(Model model){
         List<SocketMsg> socketMsgList = socketMsgService.searchMessage();
         return socketMsgList;
    }




}

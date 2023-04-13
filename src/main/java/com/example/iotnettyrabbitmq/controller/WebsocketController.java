package com.example.iotnettyrabbitmq.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.example.iotnettyrabbitmq.pojo.SocketMsg;
import com.example.iotnettyrabbitmq.service.SocketMsgService;
import com.example.iotnettyrabbitmq.util.EasyExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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


    private List<SocketMsg> socketMsgList0;


//    @ResponseBody
//    @GetMapping("/list")
//    public List<SocketMsg> searchEquipmentList(Model model){
//         List<SocketMsg> socketMsgList = socketMsgService.searchMessage();
//         return socketMsgList;
//    }

    @ResponseBody
    @GetMapping("/list")
    public List<SocketMsg> searchEquipmentList(@RequestParam(value = "equipmentId",defaultValue = "FF") String equipmentId,@RequestParam(value = "startTime",defaultValue = "0") String startTime,@RequestParam(value = "endTime",defaultValue = "0") String endTime){
        List<SocketMsg> socketMsgList = socketMsgService.searchMessage(equipmentId,startTime,endTime);
        socketMsgList0 = socketMsgList;
        return socketMsgList;
    }

    @ResponseBody
    @GetMapping("/write")
    public void excelWrite(HttpServletResponse response)throws IOException {

        String fileName = System.currentTimeMillis() + ".xlsx";
          EasyExcelUtils.downloadUnfilledToXlsx(fileName,response, SocketMsg.class,socketMsgList0);
    }





}

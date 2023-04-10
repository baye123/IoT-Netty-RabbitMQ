package com.example.iotnettyrabbitmq.util;

import java.util.Arrays;

/**
 * @Author : baye
 * @Date : 2023/3/21 10:23
 * @Code : bug and work
 * @Description : 自定义模块DI命令解析类
 */
public class Analysis {

    private String news;

    public String getNews() {
        return news;
    }

    //将从RabbitMQ消息队列中的消息分解回时间和命令并返回转化后的时间
    public String cutmsg(String msg){
        String[] the_split = msg.split("\n");
        this.news = the_split[1];
        String data = DateString.millisecondToStringLong(Long.valueOf(the_split[0]));

        return data;
    }



    //获取设备ID
    public String getEquipmentId(String msg){
        //设备id
        String Id = msg.substring(12,14);

        System.out.println("设备ID为："+ Id);

        return Id;
    }
    //获取设备数据位图
    public String[] hexToIntarray(String msg){

        //设备状态
        String Hexdata = msg.substring(18,20);
        //状态十六进制字符串
        String BitmapByStr;
        //状态二进制字符串数组
        String[] Bitmap = new String[8];
        //十六进制字符串转二进制字符串
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 2; i++){
            //每1个十六进制位转换为4个二进制位
            String temp = Hexdata.substring(i, i + 1);
            int tempInt = Integer.parseInt(temp, 16);
            String tempBin = Integer.toBinaryString(tempInt);
            //如果二进制数不足4位，补0
            if (tempBin.length() < 4){
                int num = 4 - tempBin.length();
                for (int j = 0; j < num; j++){
                    sb.append("0");
                }
            }
            sb.append(tempBin);
        }

        BitmapByStr = sb.toString();
        for(int i = 0; i <BitmapByStr.length();i++){
            Bitmap[i] = BitmapByStr.substring(i,i + 1);

        }
//        System.out.println("状态数据为：" +Hexdata);
        return Bitmap;
    }

    //根据位图数组解析数据，说明当前状态
    public String descriptionStatus(String[] Bitmap){
        //状态消息头
        String news = "当前状态为";
        //通道消息
        String DI = new String();
        for(int i = 0 ;i < 8; i++){
            if(Bitmap[7-i].equals("0")){
                //DI通道状态
                DI ="DI-"+ (i+1) + "通道: 断开  ";
            }
            else {
                DI ="DI-"+ (i+1) + "通道: 闭合  ";
            }
            news += DI;
        }
        System.out.println("状态数据为：" + Arrays.toString(Bitmap));
        System.out.println(news);
        return news;
    }

    //根据位图数组，分析安灯状态
    public String lightStatus(String[] Bitmap){
        String light = "";

        if(Bitmap[6].equals("1")){
            light = "绿灯";
        }
        else if(Bitmap[5].equals("1")){
            light = "黄灯";
        }
        else if(Bitmap[4].equals("1")){
            light = "红灯";
        }
        return light;

    }

    //状态发生变化，Bitmap1为先前状态，Bitmap2为当前状态，该方法主要反馈状态的变化
    public String statusChange(String[] Bitmap1,String[] Bitmap2){
        //状态改变消息
        String DI_Change = "";
        for(int i = 0; i < 8 ; i++){
            if(Bitmap2[7-i].equals(Bitmap1[7-i])){
                DI_Change += " ";
            }
            else {
                if(Bitmap2[7-i].equals("0")) {
                    DI_Change += "DI-" + (i + 1) + "通道断开了  ";
                }
                else {
                    DI_Change += "DI-" + (i + 1) + "通道闭合了  ";
                }
            }
        }
        System.out.println(DI_Change);
        return DI_Change;
    }
}

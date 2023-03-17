package com.example.iotnettyrabbitmq;

import com.example.iotnettyrabbitmq.util.Analysis;
import com.example.iotnettyrabbitmq.util.DateString;

import java.util.Arrays;

/**
 * @Author : baye
 * @Date : 2023/3/16 9:14
 * @Code : bug and work
 * @Description : 测试类
 */
public class test {
    public static void main(String[] args) {
        // 获取当前时间的Unix时间戳
        long millisecond = System.currentTimeMillis();
        System.out.println(millisecond);

        String data = DateString.millisecondToStringLong(millisecond);
        System.out.println(data);
//        System.out.println(System.getProperty("line.separator"));
//        Analysis analysis = new Analysis();
//        analysis.getEquipmentId("000100000004FF020160");
////        System.out.println("当前状态位图为："+ Arrays.toString(analysis.hexToIntarray("000100000004FF020160")));
//
//        analysis.descriptionStatus(analysis.hexToIntarray("000100000004FF020100"));
//        analysis.statusChange(analysis.hexToIntarray("000100000004FF020100"),analysis.hexToIntarray("000100000004FF020160"));
//        analysis.descriptionStatus(analysis.hexToIntarray("000100000004FF020160"));
//        String msg ="11231231\nsfwefwef";
//        String[] the_split = msg.split("\n");
//        for(String s : the_split){
//            System.out.println(the_split[0]);
//        }

    }
}

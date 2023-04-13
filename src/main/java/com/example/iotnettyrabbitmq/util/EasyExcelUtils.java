package com.example.iotnettyrabbitmq.util;

import com.alibaba.excel.EasyExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Author : baye
 * @Date : 2023/4/13 13:53
 * @Code : bug and work
 * @Description : easyExcel工具类
 */
public class EasyExcelUtils {
    public static void downloadUnfilledToXlsx(String excelName, HttpServletResponse response, Class cla, List list) throws IOException {
        // 这里注意 使用swagger 会导致各种问题，easyexcel官方文档推荐直接用浏览器或者用postman测试
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(excelName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), cla).sheet(excelName).doWrite(list);
    }
}

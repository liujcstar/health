package com.itheima;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import java.io.*;

public class PoiTest {

    //获取Excel数据方式一
    @Test
    public void test1() throws IOException {
        XSSFWorkbook sheets = null;
        try {
             sheets = new XSSFWorkbook(this.getClass().getResourceAsStream("/24期4月份课表.xlsx"));
            XSSFSheet sheetAt = sheets.getSheetAt(0);
            for (Row row: sheetAt) {
                for (Cell cell : row) {
                    System.out.print(cell+"  ");
                }
                System.out.println("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(sheets!=null){
                //若资源不释放就会驻留在内存很长时间，占用很多资源，
                // 唯一释放机会就是等待JVM的垃圾回收机制，判断到这个类不在使用后将其当做垃圾清理
                //但是垃圾回收机制是不可控的，若是访问量大，根本等不到垃圾回收机制来清理，最终导致系统崩溃
                sheets.close();
            }
        }
    }

    //创建excel
    @Test
    public void test3() throws IOException {
        FileOutputStream fileOutputStream = null;
        XSSFWorkbook xssfWorkbook =null;
        try {
             xssfWorkbook = new XSSFWorkbook();
            //创建工作表
            XSSFSheet xssfSheet = xssfWorkbook.createSheet("第一页");
            //创建第一行
            XSSFRow row1 = xssfSheet.createRow(0);
            //创建列,并填入数据
            row1.createCell(0).setCellValue("姓名");
            row1.createCell(1).setCellValue("年龄");
            row1.createCell(2).setCellValue("地址");

            XSSFRow row2 = xssfSheet.createRow(1);
            //创建列,并填入数据
            row2.createCell(0).setCellValue("张三");
            row2.createCell(1).setCellValue(10);
            row2.createCell(2).setCellValue("北京");

            XSSFRow row3 = xssfSheet.createRow(2);
            //创建列,并填入数据
            row3.createCell(0).setCellValue("李四");
            row3.createCell(1).setCellValue(20);
            row3.createCell(2).setCellValue("上海");

            String resource = this.getClass().getResource("/").getPath()+"test.xlsx";
            fileOutputStream = new FileOutputStream(new File(resource));

            xssfWorkbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream!=null){
                fileOutputStream.close();
            }
            if (xssfWorkbook != null){
                xssfWorkbook.close();
            }
        }
    }
}

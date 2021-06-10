import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        System.out.println("git3");
        System.out.println("mastertest");
        //第一步，创建一个workbook对应一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //第二部，在workbook中创建一个sheet对应excel中的sheet
        HSSFSheet sheet = workbook.createSheet("sheet1");
        HSSFSheet sheet2 = workbook.createSheet("sheet2");

        //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
        HSSFRow row = sheet.createRow(0);
        //第四步，创建单元格，设置表头
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("tag");
        row.createCell(1).setCellValue("Voc/V");

        //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
        HSSFRow row2 = sheet2.createRow(0);
        //第四步，创建单元格，设置表头
        HSSFCell cell2 = row2.createCell(0);
        cell2.setCellValue("tag");
        row2.createCell(1).setCellValue("Voc/V");

        //将文件保存到指定的位置
        try {
            FileOutputStream fos = new FileOutputStream("C:\\Users\\96203\\Desktop\\12\\1.xls");
            workbook.write(fos);
            System.out.println("写入成功");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

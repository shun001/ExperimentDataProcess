import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.*;

public class DataProcess {

    private static String currentFileName = "210609";
    
    private static File dataFile = new File("D:\\nh\\研究生\\课题组\\实验\\Sn-Pb\\experiment\\"+currentFileName);
    private static String prifixName = "L";

    private static Map<String,String[][]> map = new LinkedHashMap<>();
    //4:tag   0：Voc   1：Jsc   2：FF   3：PCE
    private static String[][] maxResults;



    public static void main(String[] args) {
        ArrayList<File> files = fileNameProcess(dataFile);

//        for(File f:files){
//            System.out.println(f.getName());
//        }
        readToMap(files);
        writeToExcelSheet();

    }

    public static void writeToExcelSheet(){
        //第一步，创建一个workbook对应一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //第二部，在workbook中创建一个sheet对应excel中的sheet
        HSSFSheet sheet = workbook.createSheet(dataFile.getName());
        //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
        HSSFRow row = sheet.createRow(0);
        //第四步，创建单元格，设置表头
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("tag");
        row.createCell(1).setCellValue("Voc/V");
        row.createCell(2).setCellValue("Jsc/(mA·cm-2)");
        row.createCell(3).setCellValue("FF");
        row.createCell(4).setCellValue("PCE/%");


        //第五步，写入实体数据，实际应用中这些数据从数据库得到,对象封装数据，集合包对象。对象的属性值对应表的每行的值

        Set<String> set = map.keySet();
        int p = 0;
        for(String tag:set){
            String[][] perRes = map.get(tag);
            for (int i = 0; i < perRes.length; i++) {
                HSSFRow row1 = sheet.createRow(++p);
                if(i==0) row1.createCell(0).setCellValue(tag);
                if(i==1) row1.createCell(0).setCellValue(perRes[i][4]);
                row1.createCell(1).setCellValue(perRes[i][0]);
                row1.createCell(2).setCellValue(perRes[i][1]);
                row1.createCell(3).setCellValue(perRes[i][2]);
                row1.createCell(4).setCellValue(perRes[i][3]);
            }
            HSSFRow row2 = sheet.createRow(++p);
        }

        //第二部，在workbook中创建一个sheet对应excel中的sheet
        HSSFSheet sheet2 = workbook.createSheet("PerMax");
        //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
        HSSFRow sheet2Row = sheet2.createRow(0);
        //第四步，创建单元格，设置表头
        sheet2Row.createCell(0).setCellValue("tag");
        sheet2Row.createCell(1).setCellValue("material");
        sheet2Row.createCell(2).setCellValue("Voc/V");
        sheet2Row.createCell(3).setCellValue("Jsc/(mA·cm-2)");
        sheet2Row.createCell(4).setCellValue("FF");
        sheet2Row.createCell(5).setCellValue("PCE/%");


        int p2 = 0;
        for (int i = 0; i < maxResults.length; i++) {
            HSSFRow sheet2Row1 = sheet2.createRow(++p2);
            sheet2Row1.createCell(0).setCellValue(maxResults[i][5]);
            sheet2Row1.createCell(1).setCellValue(maxResults[i][4]);
            sheet2Row1.createCell(2).setCellValue(maxResults[i][0]);
            sheet2Row1.createCell(3).setCellValue(maxResults[i][1]);
            sheet2Row1.createCell(4).setCellValue(maxResults[i][2]);
            sheet2Row1.createCell(5).setCellValue(maxResults[i][3]);
            HSSFRow sheet2Row2 = sheet2.createRow(++p2);

        }

        //将文件保存到指定的位置
        try {
            FileOutputStream fos = new FileOutputStream(dataFile.getPath()+"/"+dataFile.getName()+".xls");
            workbook.write(fos);
            System.out.println("写入成功");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void readToMap(ArrayList<File> files){
        //初始化maxResults
        //0：Voc   1：Jsc   2：FF   3：PCE  4:material 5:tag
        maxResults = new String[files.size()][6];

        for (int i = 0; i < maxResults.length; i++) {
            maxResults[i][3] = "0";
        }
        int maxIndex = -1;
        for(File file:files){
            maxIndex++;
            String name = file.getName();
            File newFile = new File(file.getPath()+"/Light-Normal");
            File[] newFiles = newFile.listFiles();
//            System.out.println(newFiles.length);

            String[][] perRes = new String[newFiles.length/2][5];
            int row = -1;

            for(File f : newFiles){
                if(f.getName().endsWith(".txt")){
                    row++;
                    String material = f.getName().split("_", 2)[0];
                    //读取数据
                    try (FileInputStream fis = new FileInputStream(f);
                         InputStreamReader isr = new InputStreamReader(fis, "utf-8");
                         BufferedReader reader = new BufferedReader(isr);
                    ){

                        for (int i = 0; i < 14; i++) {
                            reader.readLine();
                        }

                        char[] chars = new char[25];

                        perRes[row][4]=material;
                        //读voc
                        reader.read(chars,0,18);
                        String voc = String.valueOf(chars,10,6);
//                        System.out.println(voc);
                        perRes[row][0]=voc;
                        for (int i = 0; i < 5; i++) {
                            reader.readLine();
                        }

                        //读pce
                        reader.read(chars,0,25);
                        String pce = String.valueOf(chars,16,6);
//                        System.out.println(pce);
                        perRes[row][3]=pce;
                        reader.readLine();

                        //读FF
                        reader.read(chars,0,23);
                        String fillFactor = String.valueOf(chars,14,6);
//                        System.out.println(fillFactor);
                        perRes[row][2]=fillFactor;
                        reader.readLine();

                        //读jsc
                        reader.read(chars,0,23);
                        String jsc = String.valueOf(chars,14,6);
//                        System.out.println(jsc);
                        perRes[row][1]=jsc;

                        if(Double.parseDouble(pce)>Double.parseDouble(maxResults[maxIndex][3])){
                            maxResults[maxIndex][5]=name;
                            maxResults[maxIndex][4]=material;
                            maxResults[maxIndex][3]=pce;
                            maxResults[maxIndex][0]=voc;
                            maxResults[maxIndex][1]=jsc;
                            maxResults[maxIndex][2]=fillFactor;
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            map.put(name,perRes);
        }
    }

    public static ArrayList<File> fileNameProcess(File dataFile){
        File[] fileList = dataFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.startsWith(prifixName)) {
                        return true;
                }
                return false;
            }
        });
        ArrayList<File> list =new ArrayList<>();
        for(File file : fileList){
            list.add(file);
        }
        //文件排序
        Collections.sort(list, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return Integer.parseInt(o1.getName().substring(1))-(Integer.parseInt(o2.getName().substring(1)));
            }
        });
        return list;
    }

}

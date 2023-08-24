package com.example.cov.untils;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.example.cov.service.CovPersonGisService;
import com.example.cov.service.CovPersonService;
import org.springframework.beans.factory.annotation.Autowired;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class ImportCsv {
    static char separator = ',';

    public static void main(String[] args) throws Exception {

        // 批量导入
//        for(int i = 1; i < 2000; i++) {
//            String no = String.valueOf(i);
//            String filePath = "D:/A-VUE/DATA/newState/state/(" + no + ").csv";
//           new CSVUtil().batchImportCSV(i,filePath);
//        }
        String str = "[120.150116,30.284829],".replace("[","").replace("]","");
        String gpsArr[] = str.split(",");
        float lng = Float.parseFloat(gpsArr[0]);
        float lat = Float.parseFloat(gpsArr[1]);
        System.out.println(lng + "," + lat);
    }

    /**
     * java导入csv文件
     *
     * @param filePath
     *            导入路径
     * @return
     * @throws Exception
     */
    public static List<String[]> importCsv(String filePath) throws Exception {
        CsvReader reader = null;
        List<String[]> dataList = new ArrayList<String[]>();
        try {
            reader = new CsvReader(filePath, separator, Charset.forName("GBK"));

            // 读取表头
            reader.readHeaders();
            System.out.println("occupationType:" + reader.getHeader(0));
            System.out.println("targetAge:" + reader.getHeader(1));
            System.out.println("targetSex:" + reader.getHeader(2));
            // 逐条读取记录，直至读完
            dataList.add(reader.getHeaders());
            while (reader.readRecord()) {
                String[] gisItem = new String[5];
                for(int i = 0; i < gisItem.length; i++){
                    System.out.println("NO"+i+": "+reader.get(i));
                    gisItem[i] = reader.get(i);
                }
                dataList.add(gisItem);
            }
        } catch (Exception e) {
            System.out.println("读取CSV出错..." + e);
            throw e;
        } finally {
            if (null != reader) {
                reader.close();
            }
        }

        return dataList;
    }

    /**
     * java导出cvs文件
     *
     * @param dataList
     *            数据集
     * @param filePath
     *            导出路径
     * @return
     * @throws Exception
     */
    public static boolean exportCsv(List<String[]> dataList, String filePath) throws Exception {
        boolean isSuccess = false;
        CsvWriter writer = null;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath, true);
            writer = new CsvWriter(out, separator, Charset.forName("GBK"));
            for (String[] strs : dataList) {
                writer.writeRecord(strs);
            }

            isSuccess = true;
        } catch (Exception e) {
            System.out.println("生成CSV出错..." + e);
            throw e;
        } finally {
            if (null != writer) {
                writer.close();
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.out.println("exportCsv close Exception: " + e);
                    throw e;
                }
            }
        }


        return isSuccess;
    }
}

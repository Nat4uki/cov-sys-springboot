package com.example.cov;


import com.example.cov.untils.CSVUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.cov.mapper")
public class CovApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(CovApplication.class, args);
        // 内置方法，根据路径，批量导入csv文件。
//        for(int i = 1; i < 2001; i++) {
//            String no = String.valueOf(i);
//            String filePath = "D:/A-VUE/DATA/newState/state/(" + no + ").csv";
//            new CSVUtil().batchImportCSV(i,filePath);
//            System.out.println("import success " + i);
//        }
    }
}

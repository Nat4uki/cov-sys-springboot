package com.example.cov.untils;


import com.example.cov.entity.CovPerson;
import com.example.cov.entity.CovPersonGis;
import com.example.cov.service.CovPersonGisService;
import com.example.cov.service.CovPersonService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 利用javacsv2.0做导入导出csv文件工具类<br/>
 *
 *
 * @author
 *
 */
@Component
@Data
public class CSVUtil {
//    public static void main(String[] args) throws Exception {
//        for(int i = 1; i < 2; i++) {
//            String no = String.valueOf(i);
//            String filePath = "D:/A-VUE/DATA/newState/state/(" + no + ").csv";
//            csv.batchImportCSV(no,filePath);
//        }
//    }
    @Autowired
    private CovPersonService covPersonService;
    @Autowired
    private  CovPersonGisService covPersonGisService;

    public static CSVUtil csv;

    @PostConstruct
    public void init(){
        csv = this;
        csv.covPersonService = this.covPersonService;
        csv.covPersonGisService = this.covPersonGisService;
    }


    public static void batchImportCSV(int no, String filePath) throws Exception {
            List<String[]> data = ImportCsv.importCsv(filePath);
            boolean isMainMsg = true;
            int key = 1000000 + no;
            for (String[] targetData : data) {
                if (isMainMsg) {
                    CovPerson covPerson = new CovPerson();
                    covPerson.setTargetId(key);
                    covPerson.setTargetName("null");
                    covPerson.setOccupationType(targetData[0]);
                    covPerson.setTargetAge(targetData[1]);
                    covPerson.setTargetSex(targetData[2]);
                    csv.covPersonService.saveData(covPerson);
                    isMainMsg = false;
//                    csv.covPersonGisService.deleteExistTable(no);
//                    csv.covPersonGisService.createTable(no);
                    System.out.println("table " + no + " import success");
                } else {
                    CovPersonGis covPersonGis = new CovPersonGis();
                    covPersonGis.setIndexId(Integer.parseInt(targetData[0]));
                    covPersonGis.setPersonKey(key);
                    covPersonGis.setIndexTime(targetData[1]);
                    String indexGpsStr = targetData[2].replace("[","").replace("]","");
                    String gpsStrArr[] = indexGpsStr.split(",");
                    float lng = Float.parseFloat(gpsStrArr[0]);
                    float lat = Float.parseFloat(gpsStrArr[1]);
                    //System.out.println(lng+"  "+lat);
                    covPersonGis.setLng(lng);
                    covPersonGis.setLat(lat);
                    covPersonGis.setPersonStatus(Integer.parseInt(targetData[3]));
                    csv.covPersonGisService.addData("cov_person_gis", covPersonGis);
                }

            }
        }
    }


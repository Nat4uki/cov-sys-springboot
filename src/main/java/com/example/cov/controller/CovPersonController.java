package com.example.cov.controller;

import com.example.cov.entity.*;
import com.example.cov.service.CovPersonGisService;
import com.example.cov.service.CovPersonService;
import com.example.cov.service.CovPersonStatuesService;
import com.example.cov.service.CovSumStateService;
import com.example.cov.untils.ImportCsv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin
@RestController
public class CovPersonController {
    @Autowired
    private CovPersonService covPersonService;
    @Autowired
    private CovPersonGisService covPersonGisService;
    @Autowired
    private CovPersonStatuesService covPersonStatuesService;

    @Autowired
    private CovSumStateService covSumStateService;

    @RequestMapping(value = "/findCovData")
    public Object showAll(){
        return covPersonService.showAll();
    }


    @RequestMapping(value = "/saveNoIdCovData")
    public void savePerson(@RequestBody CovPerson covPerson){
        covPersonService.saveNoIdData(covPerson);
        System.out.println(covPerson);
    }


    /**
     * 分页查询
     * @param curPage
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCovGisData/{curPage}/{pageSize}")
    public List<CovPersonGis> getCovGisData(@PathVariable("curPage") int curPage, @PathVariable("pageSize") int pageSize){
        return covPersonGisService.showData(curPage,pageSize);
    }

    /**
     * 时间过滤
     * @param filterTime
     * @return
     */
    @RequestMapping(value = "/timeFilterCovGisData")
    public List<CovPersonGis> getCovGisData(@RequestBody FilterTime filterTime){
        System.out.println(filterTime.getStartTime()+" // "+filterTime.getEndTime());
        return covPersonGisService.timeFilterData(filterTime);
    }

    /**
     * 根据时间更新每天个人状态的变化
     * @param filterTime
     */
    @RequestMapping(value = "/getLeastState")
    public void refreshLeastState(@RequestBody FilterTime filterTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date startDate = dateFormat.parse(filterTime.getStartTime());
            Date endDate = dateFormat.parse(filterTime.getEndTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            // 创建日期列表并将开始日期添加到列表中
            List<Date> dateList = new ArrayList<>();
            dateList.add(startDate);
            // 将日历对象的日期增加一天，直到达到结束日期为止，并将每个日期添加到列表中
            while (calendar.getTime().before(endDate)) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                dateList.add(calendar.getTime());
            }
            // 将结束日期添加到列表中
            dateList.add(endDate);

            // 将日期列表转换为时间字符串列表
            for (int i = 0; i < dateList.size()-1; i++) {
                CovSumState covSumState = new CovSumState();
                String start = dateFormat.format(dateList.get(i));
                String end = dateFormat.format(dateList.get(i+1));
                System.out.println(start+" **State** "+end);
                FilterTime daysFilter = new FilterTime();
                daysFilter.setStartTime(start);
                daysFilter.setEndTime(end);
                // 取数据
                List<CovPersonGis> gisList = covPersonGisService.getLeastData(daysFilter);
                // 状态0表示未知
                int[] seir = new int[]{0,0,0,0,0};
                // 循环 统计
                for(CovPersonGis personGis:gisList){
                    seir[personGis.getPersonStatus()]++;
                }
                String dateKey = start.substring(0,10);
                // 日期作为id
                boolean isExistSumState = covSumStateService.idIsExist(dateKey);
                covSumState.setDate(dateKey);
                covSumState.setSType(seir[1]);
                covSumState.setEType(seir[2]);
                covSumState.setIType(seir[3]);
                covSumState.setRType(seir[4]);

                if(isExistSumState){
                    covSumStateService.updateCovSumState(covSumState);
                }else{
                    covSumStateService.addCovSumState(covSumState);
                }
                System.out.println("日期" + dateKey + "状态数据表更新成功");
            }

        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    /**
     * 展示全部状态数据
     */
    @RequestMapping(value = "/getCovSumState")
    public List<CovSumState> getCovSumState(){
        return covSumStateService.showAll();
    }

    /**
     * 更新状态
     * @param personKey
     */
    @RequestMapping(value = "/updatePersonStatues/{personKey}")
    public void refresh(@PathVariable("personKey") String personKey){
        boolean gisDataIsExist = covPersonGisService.countOfKey(personKey) > 0;
        if(!gisDataIsExist){
            System.out.println(personKey + "无GIS数据，无法更新状态");
            return;
        }
        CovPersonGis leastData = covPersonGisService.getLeastDataByKey(personKey).get(0);
        CovPersonStatues covPersonStatues = new CovPersonStatues();
        boolean personStatuesIsExist = covPersonStatuesService.idIsExist(personKey);
        covPersonStatues.setPersonKey(leastData.getPersonKey());
        covPersonStatues.setRefreshTime(leastData.getIndexTime());
        covPersonStatues.setPersonStatues(leastData.getPersonStatus());
        if(personStatuesIsExist){
            covPersonStatuesService.updatePersonStatues(covPersonStatues);
        }else{
            covPersonStatuesService.addPersonStatues(covPersonStatues);
        }
        System.out.println(personKey + "状态更新成功");
    }

    /**
     * 批量更新状态
     */
    @RequestMapping(value = "/addPersonStatues")
    public void addPersonStatues(){
        List<String> allId = covPersonService.showAllId();
        for(String no:allId){
            boolean gisDataIsExist = covPersonGisService.countOfKey(no) > 0;
            if(!gisDataIsExist){
                System.out.println(no + "无GIS数据，无法更新状态");
                continue;
            }
            CovPersonGis leastData = covPersonGisService.getLeastDataByKey(no).get(0);
            CovPersonStatues covPersonStatues = new CovPersonStatues();
            covPersonStatues.setPersonKey(leastData.getPersonKey());
            covPersonStatues.setRefreshTime(leastData.getIndexTime());
            covPersonStatues.setPersonStatues(leastData.getPersonStatus());
            boolean personStatuesIsExist = covPersonStatuesService.idIsExist(no);
            if(personStatuesIsExist){
                covPersonStatuesService.updatePersonStatues(covPersonStatues);
            }else{
                covPersonStatuesService.addPersonStatues(covPersonStatues);
            }
            System.out.println(no + "更新成功");
        }
//        for(int i = 1; i<=2000; i++){
//            String no = "" + (1000000+i);
//
//        }
    }

    /**
     * 展示全部状态数据
     */
    @RequestMapping(value = "/getPersonStatues")
    public List<CovPersonStatues> getPersonStatues(){
        return covPersonStatuesService.showAll();
    }

    /**
     * id批量查询
     */
    @RequestMapping(value = "/idList")
    public List<CovPersonGis> searchIdList(@RequestBody IdList body){
        int[] list = body.getIdList();
        System.out.println(list);
        return covPersonGisService.keyFilterData(list);
    }

    /**
     * 添加人员
     */
    @RequestMapping(value = "/addCovPerson")
    public String addCovPerson(@RequestBody CovPerson covPerson){
        String id = "" + covPerson.getTargetId();
        if(id == ""){
            return "非法输入！";
        }
        boolean idIsExist = covPersonService.countOfId(id) > 0;
        if(idIsExist){
            return "id已存在无法添加";
        }else{
            covPersonService.saveData(covPerson);

            CovPersonStatues covPersonStatues = new CovPersonStatues();
            covPersonStatues.setPersonKey(Integer.parseInt(id));
            covPersonStatues.setPersonStatues(0);
            covPersonStatuesService.addPersonStatues(covPersonStatues);
            System.out.println("人员添加成功");
            return "数据录入添加成功";
        }
    }

    /**
     * 删除人员
     */
    @RequestMapping(value = "/deleteCovData")
    public String deleteCovData(@RequestBody CovPersonGis covPersonGis){
        int id = covPersonGis.getPersonKey();
        String strId = String.valueOf(id);
        int deleteLevel = covPersonGis.getPersonStatus();
        boolean isExist = covPersonService.countOfId(strId) > 0;
        if(!isExist){
            return "删除失败，未找到对应角色数据";
        }
        // 0-删除轨迹  1-删除个人数据(删除前会将外键数据清除，执行0)
        if(deleteLevel >= 0){
            covPersonStatuesService.deletePersonStatues(strId);
            covPersonGisService.deletePersonGisData(strId);
        }
        if(deleteLevel >= 1){
            covPersonService.deletePerson(id);
        }
        return "删除成功";
    }

    /**
     * 添加Gis数据
     */
    @RequestMapping(value = "/addCovPersonGis")
    public String addCovPerson(@RequestBody CovPersonGis covPersonGis){
        // todo 判断存在相同时间，相同不能录入
        String id = "" + covPersonGis.getPersonKey();
        boolean idIsExist = covPersonService.countOfId(id) > 0;
        if(idIsExist){
            covPersonGisService.addData("cov_person_gis", covPersonGis);
            System.out.println(id+"轨迹数据录入成功");
            refresh(id);
            return "轨迹数据录入成功";
        }else{
            System.out.println("人员不存在，添加GIS数据失败");
            return "添加失败 ID:" + id + "不存在";
        }
    }

    /**
     * 路径上传文件
     */
    @RequestMapping(value = "/uploadFile")
    public String uploadForm(@RequestBody UploadParams body) throws Exception {
        System.out.println(body);
        String path = body.getFilePath();
        String[] fileName = path.split("/");
        String no = fileName[fileName.length-1].replaceAll("[^0-9]","");
        int personKey = 1000000 + Integer.parseInt(no);
        int idCount = covPersonService.countOfId(String.valueOf(personKey));
        System.out.println("personKey: "+ personKey + "   idCount: "+ idCount);
        if(idCount > 0){
            System.out.println("角色已存在");
            return "添加数据失败，角色已存在";
        }
        List<String[]> data = ImportCsv.importCsv(path);
        boolean isMainMsg = true;
        for (String[] targetData : data) {
            if(isMainMsg){
                CovPerson covPerson = new CovPerson();
                covPerson.setTargetId(personKey);
                covPerson.setTargetName("null");
                covPerson.setOccupationType(targetData[0]);
                covPerson.setTargetAge(targetData[1]);
                covPerson.setTargetSex(targetData[2]);
                covPersonService.saveData(covPerson);
                isMainMsg = false;
//                covPersonGisService.deleteExistTable(no);
//                covPersonGisService.createTable(no);
                System.out.println("import success");
            }else{
                if(targetData.length < 4) continue;
                CovPersonGis covPersonGis = new CovPersonGis();
                covPersonGis.setPersonKey(personKey);

                covPersonGis.setIndexTime(targetData[1]);
                String indexGpsStr = targetData[2].replace("[","").replace("]","");
                String[] gpsStrArr = indexGpsStr.split(",");
//                Map<String,Float> gpsMap = new HashMap<>();
//                gpsMap.put()
                System.out.println(gpsStrArr[0]+ " " +gpsStrArr[1]);

                float lng = Float.parseFloat(gpsStrArr[0]);
                covPersonGis.setLng(lng);

                float lat = Float.parseFloat(gpsStrArr[1]);
                covPersonGis.setLat(lat);

                covPersonGis.setPersonStatus(Integer.parseInt(targetData[3]));
                covPersonGisService.addData("cov_person_gis", covPersonGis);
            }
        }
        refresh(""+personKey);
        return "文件数据录入成功";
    }
}

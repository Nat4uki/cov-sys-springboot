package com.example.cov.service;

import com.example.cov.entity.CovPerson;
import com.example.cov.entity.CovPersonGis;
import com.example.cov.entity.FilterTime;
import com.example.cov.mapper.CovPersonGisMapping;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class CovPersonGisService {
    @Autowired
    private CovPersonGisMapping covPersonGisMapping;


    public void createTable(String name){
        covPersonGisMapping.createTab(name);
    };

    public void deleteExistTable(String name){
        covPersonGisMapping.deleteExistTab(name);
    }

    public void addData(@Param("name") String name, @Param("covPersonGis") CovPersonGis covPersonGis){ covPersonGisMapping.addData(name,covPersonGis); }

    public List<CovPersonGis> showData(int curPage, int pageSize){
        return covPersonGisMapping.getCovGisData(curPage,pageSize);
    }

    public List<CovPersonGis> timeFilterData(FilterTime filterTime){
        return covPersonGisMapping.timeFilterCovGisData(filterTime);
    }

    public List<CovPersonGis> keyFilterData(int[] personKeyArr){
        String str = "";
        for(int i = 0; i < personKeyArr.length; i++){
            str = str + personKeyArr[i];
            if(i != personKeyArr.length - 1){
                str = str + ',';
            }
        }
        System.out.println(str);
        if(str == ""){
            return null;
        }
        List<CovPersonGis> cov = covPersonGisMapping.keyFilterCovGisData(str);
        return cov;
    }

    public List<CovPersonGis> getLeastDataByKey(String personKey){
        return covPersonGisMapping.getLeastDataByKey(personKey);
    }

    public List<CovPersonGis> getLeastData(FilterTime filterTime){
        return covPersonGisMapping.getLeastData(filterTime);
    }

    public int countOfKey(String personKey){
        return covPersonGisMapping.countOfKey(personKey);
    }

    public void deletePersonGisData(String personKey){
        covPersonGisMapping.delete(personKey);
    }
}

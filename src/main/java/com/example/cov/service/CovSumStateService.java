package com.example.cov.service;

import com.example.cov.entity.CovPersonStatues;
import com.example.cov.entity.CovSumState;
import com.example.cov.mapper.CovSumStateMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CovSumStateService {
    @Autowired
    private CovSumStateMapping covSumStateMapping;

    public List<CovSumState> showAll(){
        return covSumStateMapping.getCovSumState();
    }

    public void updateCovSumState(CovSumState covSumState){
        covSumStateMapping.updateState(covSumState);
    }

    public boolean idIsExist(String date){
        return covSumStateMapping.countOfKey(date) > 0;
    }

    public void addCovSumState(CovSumState covSumState){
        covSumStateMapping.addState(covSumState);
    }
}

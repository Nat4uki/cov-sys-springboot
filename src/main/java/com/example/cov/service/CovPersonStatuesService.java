package com.example.cov.service;

import com.example.cov.entity.CovPerson;
import com.example.cov.entity.CovPersonStatues;
import com.example.cov.mapper.CovPersonStatuesMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CovPersonStatuesService {
    @Autowired
    private CovPersonStatuesMapping covPersonStatuesMapping;

    public List<CovPersonStatues> showAll(){
        return covPersonStatuesMapping.findAll();
    }

    public void updatePersonStatues(CovPersonStatues covPersonStatues){
        covPersonStatuesMapping.updatePersonStatues(covPersonStatues);
    }

    public boolean idIsExist(String personKey){
        return covPersonStatuesMapping.countOfKey(personKey) > 0;
    }

    public void addPersonStatues(CovPersonStatues covPersonStatues){
        covPersonStatuesMapping.addPersonStatues(covPersonStatues);
    }

    public void deletePersonStatues(String personKey){
        covPersonStatuesMapping.delete(personKey);
    }

}

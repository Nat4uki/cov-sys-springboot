package com.example.cov.service;

import com.example.cov.entity.CovPerson;
import com.example.cov.mapper.CovPersonMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CovPersonService {
    @Autowired
    private CovPersonMapping covPersonMapping;
    public List<CovPerson> showAll(){
        return covPersonMapping.findAll();
    }

    public void saveData(CovPerson covPerson){
        covPersonMapping.save(covPerson);
    }

    public void saveNoIdData(CovPerson covPerson){
        covPersonMapping.noIdSave(covPerson);
    }

    public int countOfId(String targetId){ return covPersonMapping.countOfId(targetId); }

    public List<String> showAllId(){ return covPersonMapping.findAllId(); }

    public void deletePerson(int targetId){
        covPersonMapping.delete(targetId);
    }
}
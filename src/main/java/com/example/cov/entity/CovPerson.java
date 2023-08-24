package com.example.cov.entity;

import lombok.Data;

@Data
public class CovPerson {
    private int targetId;
    private String targetName;
    private String occupationType;
    private String targetAge;
    private String targetSex;
}

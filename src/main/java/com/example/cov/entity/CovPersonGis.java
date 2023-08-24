package com.example.cov.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CovPersonGis {
    @JsonIgnore
    private int indexId;
    private int personKey;
    private String indexTime;
    private float lng;
    private float lat;
    private int personStatus;
}

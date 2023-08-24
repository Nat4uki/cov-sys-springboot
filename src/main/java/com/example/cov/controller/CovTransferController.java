package com.example.cov.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CovTransferController {
    @GetMapping("/")
    public String hello(){
        return "hello,world";
    }
}

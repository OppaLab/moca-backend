package com.moca.springboot.controller;

import com.moca.springboot.dto.ReportDTO;
import com.moca.springboot.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/report")
    public long report(ReportDTO.ReportRequest reportRequest) {
        System.out.println(reportRequest);
        return reportService.report(reportRequest);
    }
}

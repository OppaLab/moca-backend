package com.moca.springboot.controller;

import com.moca.springboot.dto.ReportDTO;
import com.moca.springboot.service.MailService;
import com.moca.springboot.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private MailService mailService;

    @PostMapping("/report")
    public void report(ReportDTO.ReportRequest reportRequest) throws MessagingException {
        reportService.report(reportRequest);
    }

    // http://localhost:8080/report?page=0 처럼 page만 request parameter로 넘겨주면 됨
    @GetMapping("/report")
    public Page<ReportDTO.GetReportResponse> getActivities(@PageableDefault(size = 30, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return reportService.getReports(pageable);
    }

}

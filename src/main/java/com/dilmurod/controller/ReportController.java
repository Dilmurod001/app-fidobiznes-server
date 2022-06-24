package com.dilmurod.controller;

import com.dilmurod.payload.ApiResponse;
import com.dilmurod.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/cb-email")
    public HttpEntity<?> cbEmailRegDate(@RequestParam String docSender){
        ApiResponse apiResponse = reportService.cbElectronPacta(docSender);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

}

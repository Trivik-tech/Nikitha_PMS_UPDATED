package com.triviktech.controllers.krakpi;

import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.services.krakpi.KraKpiService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class KraKpiControllerImpl implements KraKpiController{

    private final KraKpiService kraKpiService;

    public KraKpiControllerImpl(KraKpiService kraKpiService) {
        this.kraKpiService = kraKpiService;
    }


    @Override
    public ResponseEntity<Map<String, List<KraKpiResponseDto>>> listOfKraKpiYearWise(String empId, String year) {
        Map<String, List<KraKpiResponseDto>> krakpis = kraKpiService.listOfKraKpiByYearWise(empId, year);
        return ResponseEntity.ok(krakpis);
    }

    @Override
    public ResponseEntity<Map<String, List<KraKpiResponseDto>>> listOfKraKpiQuarterWise(String empId, String year, String quarter) {
        int qt=0;
        switch (quarter){
            case "Q1": qt=1; break;
            case "Q2": qt=2; break;
            case "Q3": qt=3; break;
            case "Q4": qt=4; break;
            default: break;
        }
        return ResponseEntity.ok(kraKpiService.listOfKraKpiByQuarterWise(empId,year,qt));
    }

    @Override
    public ResponseEntity<Map<String, List<KraKpiResponseDto>>> listOfKraKpiMonthWise(String empId, String year, String quarter, String month) {
        int qt=0;
        switch (quarter){
            case "Q1": qt=1; break;
            case "Q2": qt=2; break;
            case "Q3": qt=3; break;
            case "Q4": qt=4; break;
            default: break;
        }
        int m=Integer.parseInt(month);
        return ResponseEntity.ok(kraKpiService.listOfKraKpisByMonthsWise(empId,year,qt,m));
    }
}

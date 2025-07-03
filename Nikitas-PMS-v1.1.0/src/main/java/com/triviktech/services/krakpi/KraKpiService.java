package com.triviktech.services.krakpi;

import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;

import java.util.Map;

public interface KraKpiService {

    Map<String,String> registerKraKpi(KraKpiRequestDto kraKpiRequestDto);

    KraKpiResponseDto kraKpiForEmployee(String employeeId);

    Map<String,String> employeeReview(KraKpiRequestDto kraKpiRequestDto,String employeeId);

    Map<String,Boolean> existsByEmployee(String employeeId);

}

package com.triviktech.services.krakpi;

import com.triviktech.entities.krakpi.KraKpi;
import com.triviktech.payloads.request.kra.KraRequestDto;
import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;

import java.util.Map;

public interface KraKpiService {

    KraKpiResponseDto registerKraKpi(KraKpiRequestDto kraKpiRequestDto);

    KraKpiResponseDto kraKpiForEmployee(String employeeId);

    String managerReview(String managerId, KraKpiRequestDto data);

}

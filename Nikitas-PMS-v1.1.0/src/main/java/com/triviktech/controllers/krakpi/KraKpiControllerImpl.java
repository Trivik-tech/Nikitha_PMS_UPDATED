package com.triviktech.controllers.krakpi;

import com.triviktech.entities.krakpi.KraKpi;
import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.services.krakpi.KraKpiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KraKpiControllerImpl implements KraKpiController{

    private final KraKpiService kraKpiService;

    public KraKpiControllerImpl(KraKpiService kraKpiService) {
        this.kraKpiService = kraKpiService;
    }

    @Override
    public ResponseEntity<KraKpiResponseDto> register(KraKpiRequestDto kraKpiRequestDto) {
        return new ResponseEntity<>(kraKpiService.registerKraKpi(kraKpiRequestDto), HttpStatus.CREATED);
    }
}

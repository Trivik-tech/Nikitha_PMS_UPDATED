package com.triviktech.controllers.krakpi;

import com.triviktech.services.krakpi.KraKpiService;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class KraKpiControllerImpl implements KraKpiController{

    private final KraKpiService kraKpiService;

    public KraKpiControllerImpl(KraKpiService kraKpiService) {
        this.kraKpiService = kraKpiService;
    }


}

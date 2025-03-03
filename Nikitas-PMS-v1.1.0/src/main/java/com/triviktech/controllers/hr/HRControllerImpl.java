package com.triviktech.controllers.hr;

import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.hr.HrResponseDto;
import com.triviktech.services.hr.HrService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HRControllerImpl implements HRController{

    private final HrService hrService;

    public HRControllerImpl(HrService hrService) {
        this.hrService = hrService;
    }

    @Override
    public ResponseEntity<?> registerHr(HrRequestDto hrRequestDto) {
        return new ResponseEntity<>(hrService.registerHr(hrRequestDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<HrResponseDto> getHrById(String hrId) {
        return ResponseEntity.ok(hrService.findHrById(hrId));
    }
}

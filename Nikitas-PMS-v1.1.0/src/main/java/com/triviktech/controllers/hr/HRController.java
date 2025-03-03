package com.triviktech.controllers.hr;

import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.hr.HrResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/pms/hr")
public interface HRController {

    @PostMapping("/register-hr")
    ResponseEntity<?> registerHr(@RequestBody HrRequestDto hrRequestDto);

    @GetMapping("/{hrId}")
    ResponseEntity<HrResponseDto> getHrById(@PathVariable String hrId);
}

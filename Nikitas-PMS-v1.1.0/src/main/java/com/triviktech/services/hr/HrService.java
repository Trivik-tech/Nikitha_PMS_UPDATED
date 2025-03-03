package com.triviktech.services.hr;

import com.triviktech.entities.hr.HR;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.hr.HrResponseDto;

public interface HrService {

    HrResponseDto registerHr(HrRequestDto hrRequestDto);

    HrResponseDto findHrById(String hrId);
}

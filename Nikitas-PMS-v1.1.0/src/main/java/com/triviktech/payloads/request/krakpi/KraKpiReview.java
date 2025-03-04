package com.triviktech.payloads.request.krakpi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.triviktech.payloads.request.kra.KraRequestDto;

import java.util.Date;
import java.util.Set;

public class KraKpiReview {
    private long kraKpiId;
    private String employeeId;
    private String remark;
    private Set<KraRequestDto> kra;
    private boolean review2;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dueDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date selfReviewDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date managerReviewDate;
    private Boolean pmsInitiated;
    private Boolean managerApproval;
    private boolean selfCompleted ;  // Default value to avoid null

    private boolean managerCompleted;
}

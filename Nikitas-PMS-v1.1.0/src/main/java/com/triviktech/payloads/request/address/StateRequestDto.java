package com.triviktech.payloads.request.address;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class StateRequestDto {

    private Long stateId;

    @Size(min = 3,message = "State name should be at least 3 characters")
    @NotEmpty(message = "State name should not be empty")
    private String name;


    private long countryId;

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }
}
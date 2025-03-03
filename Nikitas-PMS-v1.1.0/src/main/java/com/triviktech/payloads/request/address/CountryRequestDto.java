package com.triviktech.payloads.request.address;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CountryRequestDto {


    private Long countryId;

    @NotEmpty(message = "Country name should not be empty")
    @Size(min = 3,message = "Country name should at least contains 3 characters ")
    private String name;

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
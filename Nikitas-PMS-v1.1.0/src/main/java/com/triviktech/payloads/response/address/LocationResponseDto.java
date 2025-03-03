package com.triviktech.payloads.response.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class LocationResponseDto {

    @JsonIgnore
    private HttpStatus status;
    private String name;
    private String zipCode;
    private StateResponseDto state;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public StateResponseDto getState() {
        return state;
    }

    public void setState(StateResponseDto state) {
        this.state = state;
    }
}
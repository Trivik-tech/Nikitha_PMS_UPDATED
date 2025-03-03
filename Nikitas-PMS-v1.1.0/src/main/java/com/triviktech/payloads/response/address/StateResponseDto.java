package com.triviktech.payloads.response.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;


public class StateResponseDto {

    @JsonIgnore
    private HttpStatus status;

    private long stateId;
    private String name;
    private CountryResponseDto country;

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

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

    public CountryResponseDto getCountry() {
        return country;
    }

    public void setCountry(CountryResponseDto country) {
        this.country = country;
    }
}
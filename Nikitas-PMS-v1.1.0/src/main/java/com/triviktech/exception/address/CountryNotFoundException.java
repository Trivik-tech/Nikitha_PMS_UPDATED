package com.triviktech.exception.address;

public class CountryNotFoundException extends RuntimeException {
    public CountryNotFoundException(long countryId) {
        super("Country Not Found With Id : "+countryId);
    }
}

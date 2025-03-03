package com.triviktech.exception.address;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(long locationId) {
        super("Location not found with id : "+locationId);
    }
}

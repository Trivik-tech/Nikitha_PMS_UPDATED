package com.triviktech.exception.address;

public class StateNotFoundException extends RuntimeException{
    public StateNotFoundException(long stateId) {

        super("State is not found with id : "+stateId);
    }
}

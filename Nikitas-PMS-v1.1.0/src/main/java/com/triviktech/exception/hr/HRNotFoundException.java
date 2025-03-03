package com.triviktech.exception.hr;

public class HRNotFoundException extends RuntimeException{
    public HRNotFoundException(String hrId) {
        super("HR not found with id : "+hrId);
    }
}

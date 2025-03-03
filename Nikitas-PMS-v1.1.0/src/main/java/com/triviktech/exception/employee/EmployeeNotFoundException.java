package com.triviktech.exception.employee;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String username) {
        super("Employee not found with id : "+username);
    }
}

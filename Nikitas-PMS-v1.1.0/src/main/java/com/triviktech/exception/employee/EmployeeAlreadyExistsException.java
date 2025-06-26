package com.triviktech.exception.employee;

public class EmployeeAlreadyExistsException extends RuntimeException {
    public EmployeeAlreadyExistsException(String emailId) {
        super("Employee with given id/email id is already exists "+emailId);
    }
}

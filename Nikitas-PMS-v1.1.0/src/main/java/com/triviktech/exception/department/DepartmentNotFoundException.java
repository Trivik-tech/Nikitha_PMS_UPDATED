package com.triviktech.exception.department;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(long departmentId) {
        super("Department not found with id : "+departmentId);
    }
}

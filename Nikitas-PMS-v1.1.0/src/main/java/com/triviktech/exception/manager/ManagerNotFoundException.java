package com.triviktech.exception.manager;

public class ManagerNotFoundException extends RuntimeException{
    public ManagerNotFoundException(String managerId) {
        super("Manager not found with id : "+ managerId);
    }
}

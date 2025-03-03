package com.triviktech.exception.manager;

public class ManagerAlreadyExistsException extends RuntimeException {
    public ManagerAlreadyExistsException(String managerId) {
        super("Manager is already exists with id : "+managerId);
    }
}

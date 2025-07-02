package com.triviktech.exception.global;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.triviktech.exception.address.CountryNotFoundException;
import com.triviktech.exception.address.StateNotFoundException;
import com.triviktech.exception.department.DepartmentNotFoundException;
import com.triviktech.exception.employee.EmployeeAlreadyExistsException;
import com.triviktech.exception.employee.EmployeeNotFoundException;
import com.triviktech.exception.manager.ManagerAlreadyExistsException;
import com.triviktech.exception.manager.ManagerNotFoundException;
import com.triviktech.exception.project.ProjectNotFoundException;
import com.triviktech.exception.token.TokenExpiredException;
import com.triviktech.exception.validation.InvalidEmailIdException;
import com.triviktech.exception.validation.ValidationException;
import com.triviktech.payloads.exception.ExceptionMessage;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleCountryNotFoundException(CountryNotFoundException exception){

        ExceptionMessage exceptionMessage=new ExceptionMessage();
        exceptionMessage.setMessage(exception.getMessage());


        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StateNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleStateNotFoundException(StateNotFoundException exception){
        ExceptionMessage exceptionMessage=new ExceptionMessage();
        exceptionMessage.setMessage(exception.getMessage());

        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleDepartmentNotFoundException(DepartmentNotFoundException e){
        ExceptionMessage exceptionMessage=new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());

        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ManagerAlreadyExistsException.class)
    public ResponseEntity<ExceptionMessage> handleManagerAlreadyExistsException(ManagerAlreadyExistsException e){
        ExceptionMessage exceptionMessage=new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ManagerNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleManagerNotFoundException(ManagerNotFoundException e){
        ExceptionMessage exceptionMessage=new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleProjectNotFoundException(ProjectNotFoundException e){
        ExceptionMessage exceptionMessage=new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(TokenExpiredException.class)
//    public ResponseEntity<ExceptionMessage> handleTokenExpiredException(TokenExpiredException e){
//        ExceptionMessage exceptionMessage=new ExceptionMessage();
//        exceptionMessage.setMessage("Token expired. Please login again.");
//        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ExceptionMessage> handleJWTVerificationException(JWTVerificationException e){

        ExceptionMessage exceptionMessage=new ExceptionMessage();
        exceptionMessage.setMessage("Invalid token. Please login again.");
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ExceptionMessage> handleServletException(ServletException e){
        ExceptionMessage exceptionMessage=new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<ExceptionMessage> handleRootExceptions(Exception e){
    //     ExceptionMessage exceptionMessage=new ExceptionMessage();
    //     exceptionMessage.setMessage(e.getMessage());
    //     return new ResponseEntity<>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    // }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ExceptionMessage> handleTokenExpiredException(TokenExpiredException exception){
        ExceptionMessage exceptionMessage=new ExceptionMessage();
        exceptionMessage.setMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleEmployeeNotFoundException(EmployeeNotFoundException exception){

        ExceptionMessage exceptionMessage=new ExceptionMessage();
        exceptionMessage.setMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionMessage> handleValidationException(ValidationException e){
        ExceptionMessage exceptionMessage=new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage,HttpStatus.NOT_ACCEPTABLE);

    }

    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ResponseEntity<ExceptionMessage> handleEmployeeAlreadyExistsException(EmployeeAlreadyExistsException e){
        ExceptionMessage exceptionMessage=new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidEmailIdException.class)
    public ResponseEntity<ExceptionMessage> handleInvalidEmailIdException(InvalidEmailIdException e){
        ExceptionMessage exceptionMessage=new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

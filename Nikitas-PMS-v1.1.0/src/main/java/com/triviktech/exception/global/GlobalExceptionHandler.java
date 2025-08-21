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

/**
 * GlobalExceptionHandler handles all custom and system exceptions
 * across the application in a centralized way.
 *
 * <p>This ensures that the application provides a consistent
 * error response structure (ExceptionMessage) with proper
 * HTTP status codes.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exception when a country is not found.
     *
     * @param exception CountryNotFoundException
     * @return ResponseEntity with error message and HTTP 404
     */
    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleCountryNotFoundException(CountryNotFoundException exception) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exception when a state is not found.
     *
     * @param exception StateNotFoundException
     * @return ResponseEntity with error message and HTTP 404
     */
    @ExceptionHandler(StateNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleStateNotFoundException(StateNotFoundException exception) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exception when a department is not found.
     *
     * @param e DepartmentNotFoundException
     * @return ResponseEntity with error message and HTTP 404
     */
    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleDepartmentNotFoundException(DepartmentNotFoundException e) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exception when a manager already exists.
     *
     * @param e ManagerAlreadyExistsException
     * @return ResponseEntity with error message and HTTP 409
     */
    @ExceptionHandler(ManagerAlreadyExistsException.class)
    public ResponseEntity<ExceptionMessage> handleManagerAlreadyExistsException(ManagerAlreadyExistsException e) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.CONFLICT);
    }

    /**
     * Handles exception when a manager is not found.
     *
     * @param e ManagerNotFoundException
     * @return ResponseEntity with error message and HTTP 404
     */
    @ExceptionHandler(ManagerNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleManagerNotFoundException(ManagerNotFoundException e) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exception when a project is not found.
     *
     * @param e ProjectNotFoundException
     * @return ResponseEntity with error message and HTTP 404
     */
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleProjectNotFoundException(ProjectNotFoundException e) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exception when JWT token is invalid.
     *
     * @param e JWTVerificationException
     * @return ResponseEntity with error message and HTTP 401
     */
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ExceptionMessage> handleJWTVerificationException(JWTVerificationException e) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage("Invalid token. Please login again.");
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles Servlet-related exceptions.
     *
     * @param e ServletException
     * @return ResponseEntity with error message and HTTP 500
     */
    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ExceptionMessage> handleServletException(ServletException e) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exception when token is expired.
     *
     * @param exception TokenExpiredException
     * @return ResponseEntity with error message and HTTP 401
     */
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ExceptionMessage> handleTokenExpiredException(TokenExpiredException exception) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles exception when employee is not found.
     *
     * @param exception EmployeeNotFoundException
     * @return ResponseEntity with error message and HTTP 404
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleEmployeeNotFoundException(EmployeeNotFoundException exception) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles validation-related exceptions.
     *
     * @param e ValidationException
     * @return ResponseEntity with error message and HTTP 406
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionMessage> handleValidationException(ValidationException e) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Handles exception when employee already exists.
     *
     * @param e EmployeeAlreadyExistsException
     * @return ResponseEntity with error message and HTTP 500
     */
    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ResponseEntity<ExceptionMessage> handleEmployeeAlreadyExistsException(EmployeeAlreadyExistsException e) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exception when email ID is invalid.
     *
     * @param e InvalidEmailIdException
     * @return ResponseEntity with error message and HTTP 500
     */
    @ExceptionHandler(InvalidEmailIdException.class)
    public ResponseEntity<ExceptionMessage> handleInvalidEmailIdException(InvalidEmailIdException e) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

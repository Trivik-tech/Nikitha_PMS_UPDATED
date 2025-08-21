package com.triviktech.services.employee;

import com.triviktech.payloads.response.employee.EmployeeInfo;

/**
 * Service interface that defines operations related to employees.
 * <p>
 * This layer acts as a contract for employee-related functionalities
 * such as retrieving employee profiles.
 * </p>
 */
public interface EmployeeService {

    /**
     * Fetches the profile information of an employee using their unique employee ID.
     *
     * @param employeeId the unique identifier of the employee
     * @return {@link EmployeeInfo} containing employee profile details
     */
    EmployeeInfo profile(String employeeId);

}

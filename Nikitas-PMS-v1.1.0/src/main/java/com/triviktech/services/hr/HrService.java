package com.triviktech.services.hr;

import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.employee.PmsStatusCountDto;
import com.triviktech.payloads.response.hr.HrResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Service interface for handling all HR-related business operations.
 * <p>
 * This interface defines the core methods for managing HR profiles,
 * employee data, PMS (Performance Management System) operations,
 * department analysis, and report generation.
 * </p>
 */
public interface HrService {

    /**
     * Registers a new HR in the system.
     *
     * @param hrRequestDto the HR registration request details
     * @return {@link HrResponseDto} containing HR details after registration
     */
    HrResponseDto registerHr(HrRequestDto hrRequestDto);

    /**
     * Finds HR details by their ID.
     *
     * @param hrId the unique HR identifier
     * @return {@link HrResponseDto} containing HR details
     */
    HrResponseDto findHrById(String hrId);

    /**
     * Uploads and processes bulk employee data from an Excel/CSV file.
     *
     * @param file the uploaded file containing employee details
     * @return list of results (e.g., success/failure messages per employee)
     * @throws IOException if file processing fails
     */
    List<Object> uploadEmployeesData(MultipartFile file) throws IOException;

    /**
     * Retrieves all employees from the system.
     *
     * @return list of {@link EmployeeInfo} objects
     */
    List<EmployeeInfo> getAllEmployees();

    /**
     * Fetches employee details by ID.
     *
     * @param employeeId the unique employee identifier
     * @return {@link EmployeeInfo} containing employee details
     */
    EmployeeInfo getEmployeeById(String employeeId);

    /**
     * Gets the total number of employees in the organization.
     *
     * @return total employee count
     */
    Integer totalEmployees();

    /**
     * Searches employees by name, ID, or other searchable fields.
     *
     * @param search search keyword
     * @return list of matching {@link EmployeeInfo}
     */
    List<EmployeeInfo> searchEmployee(String search);

    /**
     * Deletes an employee by ID.
     *
     * @param employeeId employee identifier
     * @return response map with status message
     */
    Map<String, String> deleteEmployee(String employeeId);

    /**
     * Updates an employee’s details.
     *
     * @param empId employee identifier
     * @param employee updated employee request object
     * @return response map with status message
     */
    Map<String, String> updateEmployee(String empId, Employee employee);

    /**
     * Retrieves department information.
     *
     * @return map containing department details
     */
    Map<String, Object> getdepartment();

    /**
     * Fetches employee count grouped by department.
     *
     * @return list of employee counts per department
     */
    List<Long> getEmployeeCountByDepartment();

    /**
     * Retrieves key assessment matrix data.
     *
     * @return map with assessment-related metrics
     */
    Map<String, Integer> assessmentKeyMatrix();

    /**
     * Retrieves employees whose KRA/KPI are pending approval.
     *
     * @return list of {@link EmployeeInfo}
     */
    List<EmployeeInfo> employeesWithKraKpiApproval();

    /**
     * Initiates the PMS cycle for an employee.
     *
     * @param employeeId employee identifier
     * @param pms map containing PMS initiation details
     * @return response map with status message
     */
    Map<String, String> initiatePms(String employeeId, Map<String, Boolean> pms);

    /**
     * Fetches employees who have PMS initiated.
     *
     * @return list of {@link EmployeeInfo}
     */
    List<EmployeeInfo> pmsInitiatedEmployees();

    /**
     * Registers a new employee from HR side.
     *
     * @param employee employee request object
     * @return response map with registration status
     */
    Map<String, String> employeeRegistration(Employee employee);

    /**
     * Retrieves employees who have completed PMS.
     *
     * @return list of {@link EmployeeWithPmsStatus}
     */
    List<EmployeeWithPmsStatus> getCompletedPmsForHR();

    /**
     * Retrieves employees with pending PMS.
     *
     * @return list of {@link EmployeeWithPmsStatus}
     */
    List<EmployeeWithPmsStatus> getPendingPmsForHR();

    /**
     * Calculates PMS completion percentage for HR dashboard.
     *
     * @return {@link PmsPercentageDto} containing percentage details
     */
    PmsPercentageDto getPmsPercentageForHR();

    /**
     * Retrieves HR profile details.
     *
     * @param hrId HR identifier
     * @return {@link HrResponseDto} containing HR profile
     */
    HrResponseDto profile(String hrId);

    /**
     * Retrieves counts of PMS statuses (e.g., completed, pending).
     *
     * @return {@link PmsStatusCountDto} with PMS counts
     */
    PmsStatusCountDto getPmsCountsForHR();

    /**
     * Retrieves completed and pending PMS counts grouped by department.
     *
     * @return nested map: {department -> {completed, pending}}
     */
    Map<String, Map<String, Integer>> getCompletedPendingByDepartment();

    /**
     * Generates PMS PDF report for an employee.
     *
     * @param employeeId employee identifier
     * @return {@link ByteArrayInputStream} representing the generated PDF
     */
    ByteArrayInputStream generatePmsPdfReport(String employeeId);

}

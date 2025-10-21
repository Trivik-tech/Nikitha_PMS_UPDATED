package com.triviktech.services.hr;

import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.ExitEmployeeResponseDto;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.employee.PmsStatusCountDto;
import com.triviktech.payloads.response.hr.HrResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
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

    HrResponseDto registerHr(HrRequestDto hrRequestDto);

    HrResponseDto findHrById(String hrId);

    List<Object> uploadEmployeesData(MultipartFile file) throws IOException;

    List<EmployeeInfo> getAllEmployees();

    EmployeeInfo getEmployeeById(String employeeId);

    Integer totalEmployees();

    List<EmployeeInfo> searchEmployee(String search);

    Map<String, String> deleteEmployee(String employeeId);

    Map<String, String> updateEmployee(String empId, Employee employee);

    Map<String, Object> getdepartment();

    List<Long> getEmployeeCountByDepartment();

    Map<String, Integer> assessmentKeyMatrix();

    List<EmployeeInfo> employeesWithKraKpiApproval();

    Map<String, String> initiatePms(String employeeId, Map<String, Boolean> pms);

    List<EmployeeInfo> pmsInitiatedEmployees();

    Map<String, String> employeeRegistration(Employee employee);

    List<EmployeeWithPmsStatus> getCompletedPmsForHR();

    List<EmployeeWithPmsStatus> getPendingPmsForHR();

    PmsPercentageDto getPmsPercentageForHR();

    HrResponseDto profile(String hrId);

    PmsStatusCountDto getPmsCountsForHR();

    Map<String, Map<String, Integer>> getCompletedPendingByDepartment();

    /**
     * Generates PMS PDF report for an employee.
     *
     * @param employeeId employee identifier
     * @return {@link ByteArrayInputStream} representing the generated PDF
     */
    ByteArrayInputStream generatePmsPdfReport(String employeeId);

    String processEmployeeExit(String empId, LocalDate lastWorkingDay);
    public ByteArrayInputStream generateAllEmployeesPmsPdfReport();
    
}

package com.triviktech.services.manager;

import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.request.manager.ManagerRequestDto;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.employee.PmsStatusCountDto;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.payloads.response.manager.ManagerResponseDto;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing operations related to managers and their employees.
 * <p>
 * This includes registering managers, retrieving manager and employee information,
 * managing KRA/KPI approvals and reviews, and fetching PMS (Performance Management System)
 * statistics such as pending/completed assessments and percentages.
 * </p>
 */
public interface ManagerService {

    /**
     * Registers a new manager in the system.
     *
     * @param managerRequestDto The data of the manager to register.
     * @return ManagerResponseDto containing details of the registered manager.
     */
    ManagerResponseDto registerManager(ManagerRequestDto managerRequestDto);

    /**
     * Retrieves a list of all managers in the system.
     *
     * @return List of ManagerResponseDto objects representing all managers.
     */
    List<ManagerResponseDto> listOfManager();

    /**
     * Finds a manager by their unique ID.
     *
     * @param managerId The ID of the manager to find.
     * @return ManagerResponseDto containing manager details.
     */
    ManagerResponseDto findManagerById(String managerId);

    /**
     * Lists all employees reporting to a specific manager.
     *
     * @param reportingManager The ID or name of the manager.
     * @return List of EmployeeWithPmsStatus for employees reporting to the manager.
     */
    List<EmployeeWithPmsStatus> listOfEmployeesForManager(String reportingManager);

    /**
     * Retrieves employees who have completed PMS under a specific manager.
     *
     * @param managerId The ID of the manager.
     * @return List of EmployeeWithPmsStatus representing completed PMS employees.
     */
    List<EmployeeWithPmsStatus> listOfPMSCompletedEmployees(String managerId);

    /**
     * Retrieves employees whose PMS is pending under a specific manager.
     *
     * @param managerId The ID of the manager.
     * @return List of EmployeeWithPmsStatus representing pending PMS employees.
     */
    List<EmployeeWithPmsStatus> listOfPMSPendingEmployees(String managerId);

    /**
     * Fetches KRA/KPI details for a specific employee under a manager.
     *
     * @param managerName The name of the manager.
     * @param employeeId  The ID of the employee.
     * @return KraKpiResponseDto containing KRA/KPI information of the employee.
     */
    KraKpiResponseDto getEmployeeKarKpi(String managerName, String employeeId);

    /**
     * Approves the KRA submitted by an employee.
     *
     * @param kraKpiRequestDto The KRA/KPI details for approval.
     * @param employeeId       The ID of the employee.
     * @param managerId        The ID of the manager approving the KRA.
     * @return Map containing status and message of the approval operation.
     */
    Map<String, String> approveKra(KraKpiRequestDto kraKpiRequestDto, String employeeId, String managerId);

    /**
     * Allows a manager to submit a review for an employee's KRA/KPI.
     *
     * @param managerId  The ID of the manager submitting the review.
     * @param employeeId The ID of the employee being reviewed.
     * @param data       The KRA/KPI data for the review.
     * @return Map containing status and message of the review operation.
     */
    Map<String, String> managerReview(String managerId, String employeeId, KraKpiRequestDto data);

    /**
     * Retrieves the list of employees with pending assessments for a manager.
     *
     * @param reportingManager The ID or name of the manager.
     * @return List of EmployeeWithPmsStatus representing pending assessments.
     */
    List<EmployeeWithPmsStatus> getPendingAssessmentListForManager(String reportingManager);

    /**
     * Retrieves the list of employees who completed assessments under a manager.
     *
     * @param reportingManager The ID or name of the manager.
     * @return List of EmployeeWithPmsStatus representing completed assessments.
     */
    List<EmployeeWithPmsStatus> getCompletedAssessmentListForManager(String reportingManager);

    /**
     * Retrieves PMS completion percentage for a manager's team.
     *
     * @param reportingManager The ID or name of the manager.
     * @return PmsPercentageDto containing PMS completion statistics.
     */
    PmsPercentageDto getPmsPercentageForManager(String reportingManager);

    /**
     * Retrieves time or size metrics related to a manager's PMS workflow.
     *
     * @param managerId The ID of the manager.
     * @return Map with relevant metrics (e.g., task counts, durations).
     */
    Map<String, Integer> getTimeSize(String managerId);

    /**
     * Retrieves PMS counts such as pending and completed assessments for a manager.
     *
     * @param managerId The ID of the manager.
     * @return PmsStatusCountDto containing counts of PMS statuses.
     */
    PmsStatusCountDto getPmsCountsForManager(String managerId);
}

package com.triviktech.services.krakpi;

import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.utilities.xlsxsupport.XlsxSupport;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing Key Result Areas (KRA) and Key Performance Indicators (KPI).
 * <p>
 * This service provides methods for registering, retrieving, and managing KRA/KPI details
 * for employees. It supports employee reviews, status checks, and fetching all KRA/KPI records.
 * </p>
 */
public interface KraKpiService {

    /**
     * Registers a new KRA/KPI for an employee.
     *
     * @param kraKpiRequestDto the {@link KraKpiRequestDto} containing details of the KRA/KPI to be registered
     * @return a {@link Map} containing a status message or confirmation
     */
    Map<String, String> registerKraKpi(KraKpiRequestDto kraKpiRequestDto);

    /**
     * Retrieves the KRA/KPI details for a specific employee.
     *
     * @param employeeId the unique identifier of the employee
     * @return a {@link KraKpiResponseDto} containing the employee's KRA/KPI details
     */
    KraKpiResponseDto kraKpiForEmployee(String employeeId);

    /**
     * Submits an employee's self-review for their KRA/KPI.
     *
     * @param kraKpiRequestDto the {@link KraKpiRequestDto} containing review details
     * @param employeeId       the unique identifier of the employee
     * @return a {@link Map} containing a status message of the review submission
     */
    Map<String, String> employeeReview(KraKpiRequestDto kraKpiRequestDto, String employeeId);

    /**
     * Checks if KRA/KPI records exist for a specific employee.
     *
     * @param employeeId the unique identifier of the employee
     * @return a {@link Map} containing a boolean value indicating existence
     */
    Map<String, Boolean> existsByEmployee(String employeeId);

    /**
     * Retrieves a list of all KRA/KPI records for a specific employee.
     *
     * @param employeeId the unique identifier of the employee
     * @return a {@link Map} where the key is a category (like KRA) and
     *         the value is a {@link List} of {@link KraKpiResponseDto}
     */
    /**
     * Get the list of KRA/KPI details for a given employee.
     *
     * @param employeeId the unique ID of the employee
     * @return a map containing KRA/KPI data grouped by category (e.g., "KRA", "KPI")
     */
    Map<String, List<KraKpiResponseDto>> listOfKraKpi(String employeeId);

    /**
     * Upload KRA/KPI details from an Excel file.
     *
     * @param file the Excel file containing KRA/KPI data
     * @return a map with parsed KRA data grouped by category
     */
    Map<String, List<XlsxSupport.KRA>> uploadKraKpi(MultipartFile file);

    /**
     * Get the list of KRA/KPI details for an employee filtered by a specific year.
     *
     * @param empId the unique ID of the employee
     * @param year  the year for which KRA/KPI data should be fetched
     * @return a map containing year-wise KRA/KPI details grouped by category
     */
    Map<String, List<KraKpiResponseDto>> listOfKraKpiByYearWise(String empId, String year);



    Map<String, List<KraKpiResponseDto>> listOfKraKpiByQuarterWise(String empId, String year,int quarter);

    Map<String,List<KraKpiResponseDto>> listOfKraKpisByMonthsWise(String empId,String year,int quarter,int month);



}

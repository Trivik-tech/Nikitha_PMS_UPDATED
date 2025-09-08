package com.triviktech.controllers.krakpi;

import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * REST controller interface for handling KRA/KPI related operations.
 *
 * <p>This controller defines endpoints to fetch KRA/KPI details for employees.
 * It is mapped to the base path <b>/api/v1/pms/krakpi</b> and allows cross-origin
 * requests from <b>http://localhost:3000</b>.</p>
 */
@RequestMapping("/api/v1/pms/krakpi")
@CrossOrigin(origins = "http://localhost:3000")
public interface KraKpiController {

    /**
     * Retrieves the list of KRA/KPI records for a specific employee, filtered by year.
     *
     * <p>The client must provide the employee ID and year as request parameters.
     * The response contains a map with a string key and a list of {@link KraKpiResponseDto}
     * objects representing the employee’s KRA/KPI data.</p>
     *
     * @param empId the unique identifier of the employee
     * @param year  the year (e.g., "2025") for which KRA/KPI records are requested
     * @return a {@link ResponseEntity} containing a map of KRA/KPI data grouped by string key
     */
    @GetMapping("/kra-kpi-year-wise")
    ResponseEntity<Map<String, List<KraKpiResponseDto>>> listOfKraKpiYearWise(
            @RequestParam String empId,
            @RequestParam String year);

    @GetMapping("/kra-kpi-quarter-wise")
    ResponseEntity<Map<String,List<KraKpiResponseDto>>> listOfKraKpiQuarterWise(
            @RequestParam String empId,
            @RequestParam String year,
            @RequestParam String quarter
    );

    @GetMapping("/kra-kpi-month-wise")
    ResponseEntity<Map<String,List<KraKpiResponseDto>>> listOfKraKpiMonthWise(
            @RequestParam String empId,
            @RequestParam String year,
            @RequestParam String quarter,
            @RequestParam String month
    );
}

package com.triviktech.controllers.hr;

import com.triviktech.entities.employee.ExitEmployee;
import com.triviktech.exception.validation.ValidationException;
import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.employee.*;
import com.triviktech.payloads.response.hr.HrResponseDto;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.krakpi.KraKpiRepository;
import com.triviktech.services.hr.HrService;
import com.triviktech.services.krakpi.KraKpiService;
import com.triviktech.services.notification.NotificationService;
import com.triviktech.utilities.reports.EmployeeReport;
import com.triviktech.utilities.xlsxsupport.XlsxSupport;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/v1/pms/hr")
public class HRControllerImpl implements HRController {

    private final NotificationService notificationService;
    private final KraKpiRepository kraKpiRepository;
    private final EmployeeInformationRepository employeeRepository;
    private final HrService hrService;
    private final EmployeeReport employeeReport;
    private final KraKpiService kraKpiService;

    public HRControllerImpl(NotificationService notificationService,
                            KraKpiRepository kraKpiRepository,
                            EmployeeInformationRepository employeeRepository,
                            HrService hrService,
                            EmployeeReport employeeReport,
                            KraKpiService kraKpiService) {
        this.notificationService = notificationService;
        this.kraKpiRepository = kraKpiRepository;
        this.employeeRepository = employeeRepository;
        this.hrService = hrService;
        this.employeeReport = employeeReport;
        this.kraKpiService = kraKpiService;
    }

    @Override
    public ResponseEntity<?> registerHr(@RequestBody HrRequestDto hrRequestDto) {
        return new ResponseEntity<>(hrService.registerHr(hrRequestDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<HrResponseDto> getHrById(String hrId) {
        return ResponseEntity.ok(hrService.findHrById(hrId));
    }

    @Override
    public ResponseEntity<List<Object>> uploadEmployeesData(MultipartFile file) throws IOException {
        return new ResponseEntity<>(hrService.uploadEmployeesData(file), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<EmployeeInfo>> allEmployees() {
        return ResponseEntity.ok(hrService.getAllEmployees());
    }

    @Override
    public ResponseEntity<EmployeeInfo> getEmployee(String employeeId) {
        return ResponseEntity.ok(hrService.getEmployeeById(employeeId));
    }

    @Override
    public ResponseEntity<Map<String, Integer>> totalEmployees() {
        return ResponseEntity.ok(Map.of("totalEmployees", hrService.totalEmployees()));
    }

    @Override
    public ResponseEntity<List<EmployeeInfo>> searchEmployee(String search) {
        return ResponseEntity.ok(hrService.searchEmployee(search));
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteEmployee(String employeeId) {
        return ResponseEntity.ok(hrService.deleteEmployee(employeeId));
    }

    @Override
    public ResponseEntity<Map<String, String>> updateEmployee(String empId, Employee employee) {
        return ResponseEntity.ok(hrService.updateEmployee(empId, employee));
    }

    @Override
    public ResponseEntity<List<EmployeeInfo>> employeeListWithKraKpiApproved() {
        return ResponseEntity.ok(hrService.employeesWithKraKpiApproval());
    }

    @Override
    public ResponseEntity<Map<String, Object>> getDepartment() {
        return ResponseEntity.ok(hrService.getdepartment());
    }

    @Override
    public ResponseEntity<Map<String, List<Long>>> employeeCount() {
        return ResponseEntity.ok(Map.of("employees", hrService.getEmployeeCountByDepartment()));
    }

    @Override
    public ResponseEntity<Map<String, Integer>> keyMatrix() {
        return ResponseEntity.ok(hrService.assessmentKeyMatrix());
    }

    @Override
    public ResponseEntity<Map<String, String>> pmsInitiated(String employeeId, Map<String, Boolean> pms) {
        Map<String, String> response = hrService.initiatePms(employeeId, pms);
        if ("success".equalsIgnoreCase(response.get("status"))) {
            notificationService.sendMessageWithRecent(
                    "System",
                    employeeId,
                    "PMS has been initiated for you. Please complete your self-review.",
                    "/queue/employee-notification"
            );
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EmployeeInfo>> pmsInitiatedEmployees() {
        return ResponseEntity.ok(hrService.pmsInitiatedEmployees());
    }

    @Override
    public ResponseEntity<List<EmployeeWithPmsStatus>> getCompletedPmsForHR() {
        return ResponseEntity.ok(hrService.getCompletedPmsForHR());
    }

    @Override
    public ResponseEntity<Map<String, String>> registerEmployee(Employee employee, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(hrService.employeeRegistration(employee), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<EmployeeWithPmsStatus>> getPendingPmsForHR() {
        return ResponseEntity.ok(hrService.getPendingPmsForHR());
    }

    @Override
    public ResponseEntity<PmsPercentageDto> getPmsPercentageForHR() {
        return ResponseEntity.ok(hrService.getPmsPercentageForHR());
    }

    @Override
    public ResponseEntity<Map<String, String>> notifyEmployeeAndManager(String employeeId) {
        return ResponseEntity.ok(Map.of("message", "Notification feature under implementation"));
    }

    @Override
    public ResponseEntity<Map<String, HrResponseDto>> profile(UserDetails hr) {
        HrResponseDto dto = hrService.profile(hr.getUsername());
        return ResponseEntity.ok(Map.of("profile", dto));
    }

    @Override
    public ResponseEntity<PmsStatusCountDto> getPmsStatusCountForHR() {
        return ResponseEntity.ok(hrService.getPmsCountsForHR());
    }

    @Override
    public ResponseEntity<Map<String, String>> generateEmployeeInfoReport(String id) {
        boolean ok = employeeReport.generateEmployeeInfoReport(id);
        return ResponseEntity.ok(Map.of("status", ok ? "Report Generated" : "Report Failed"));
    }

    @Override
    public ResponseEntity<Map<String, String>> generateEmployeeList() {
        boolean ok = employeeReport.generateEmployeeListPdf();
        return ResponseEntity.ok(Map.of("status", ok ? "Report Generated" : "Report Failed"));
    }

    @Override
    public ResponseEntity<Map<String, Map<String, Integer>>> getCompletedPendingByDepartments() {
        return ResponseEntity.ok(hrService.getCompletedPendingByDepartment());
    }

    @Override
    public ResponseEntity<InputStreamResource> exportPmsPdf(String employeeId) {
        ByteArrayInputStream bis = hrService.generatePmsPdfReport(employeeId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + employeeId + "_PMS.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @Override
    public ResponseEntity<Map<String, List<XlsxSupport.KRA>>> uploadKraKpi(MultipartFile file) {
        return ResponseEntity.ok(kraKpiService.uploadKraKpi(file));
    }

    @Override
    public ResponseEntity<String> processEmployeeExit(String empId, LocalDate lastWorkingDay) {
        return ResponseEntity.ok(hrService.processEmployeeExit(empId, lastWorkingDay));
    }

    @Override
    public ResponseEntity<List<ExitEmployee>> getAllExitEmployees() {
        return ResponseEntity.ok(hrService.getAllExitEmployees());
    }

    @Override
    public ResponseEntity<Map<String, Map<String, Integer>>> getMonthlyDepartmentReport(int year, int quarter, int month) {
        return ResponseEntity.ok(kraKpiService.getCompletedPendingByDepartmentMonthly(year, quarter, month));
    }

    @Override
    public ResponseEntity<Map<String, Map<String, Integer>>> getQuarterlyDepartmentReport(int year, int quarter) {
        return ResponseEntity.ok(kraKpiService.getCompletedPendingByDepartmentQuarterWise(year, quarter));
    }

    @Override
    public ResponseEntity<Map<String, Map<String, Integer>>> getYearlyDepartmentReport(int year) {
        return ResponseEntity.ok(kraKpiService.getCompletedPendingByDepartmentYearWise(year));
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadAllEmployeesReport() {
        ByteArrayInputStream bis = hrService.generateAllEmployeesPmsPdfReport();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=All_Employees_PMS_Report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    // @Override
    // public ResponseEntity<Map<String, String>> notifyAllEmployeesAndManagers() {
    //     hrService.notifyAllEmployeesAndManagers();
    //     return ResponseEntity.ok(Map.of("status", "Notifications sent successfully"));
    // }
}

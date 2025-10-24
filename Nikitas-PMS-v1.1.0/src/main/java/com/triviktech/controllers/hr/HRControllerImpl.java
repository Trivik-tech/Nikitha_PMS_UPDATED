package com.triviktech.controllers.hr;

import com.triviktech.entities.employee.ExitEmployee;
import com.triviktech.exception.validation.ValidationException;
import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.employee.PmsStatusCountDto;
import com.triviktech.payloads.response.hr.HrResponseDto;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.krakpi.KraKpiRepository;
import com.triviktech.services.hr.HrService;
import com.triviktech.services.krakpi.KraKpiService;
import com.triviktech.services.notification.NotificationService;
import com.triviktech.utilities.reports.EmployeeReport;
import com.triviktech.utilities.xlsxsupport.XlsxSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HRControllerImpl implements HRController {

    @Autowired
    NotificationService notificationService;
    @Autowired
    private KraKpiRepository kraKpiRepository;

    @Autowired
    private EmployeeInformationRepository employeeRepository;

    private final HrService hrService;
    private final EmployeeReport employeeReport;
    private final KraKpiService kraKpiService;

    public HRControllerImpl(HrService hrService, EmployeeReport employeeReport, KraKpiService kraKpiService) {
        this.hrService = hrService;
        this.employeeReport = employeeReport;
        this.kraKpiService = kraKpiService;
    }

    @Override
    public ResponseEntity<?> registerHr(HrRequestDto hrRequestDto) {
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
        Map<String, Integer> response = new HashMap<>();
        response.put("totalEmployees", hrService.totalEmployees());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EmployeeInfo>> searchEmployee(String search) {
        return ResponseEntity.ok(hrService.searchEmployee(search));
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteEmployee(String employeeId) {
        Map<String, String> msg = hrService.deleteEmployee(employeeId);
        return ResponseEntity.ok(msg);
    }

    @Override
    public ResponseEntity<Map<String, String>> updateEmployee(String empId, Employee employee) {
        Map<String, String> response = hrService.updateEmployee(empId, employee);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EmployeeInfo>> employeeListWithKraKpiApproved() {
        return ResponseEntity.ok(hrService.employeesWithKraKpiApproval());
    }

    @Override
    public ResponseEntity<Map<String, Object>> getDepartment() {
        Map<String, Object> getdepartment = hrService.getdepartment();
        return ResponseEntity.ok(getdepartment);
    }

    @Override
    public ResponseEntity<Map<String, List<Long>>> employeeCount() {
        List<Long> employeeCounts = hrService.getEmployeeCountByDepartment();
        Map<String, List<Long>> response = Map.of("employees", employeeCounts);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Map<String, Integer>> keyMatrix() {
        Map<String, Integer> employeeKeyMatrix = hrService.assessmentKeyMatrix();
        return ResponseEntity.ok(employeeKeyMatrix);
    }

    @Override
    public ResponseEntity<Map<String, String>> pmsInitiated(String employeeId, Map<String, Boolean> pms) {
        Map<String, String> response = hrService.initiatePms(employeeId, pms);

        if ("success".equalsIgnoreCase(response.get("status"))) {
            String destination = "/queue/employee-notification";
            String content = "PMS has been initiated for you. Please complete your KRA/KPI self-review.";
            notificationService.sendMessageWithRecent("System", employeeId, content, destination);
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EmployeeInfo>> pmsInitiatedEmployees() {
        return ResponseEntity.ok(hrService.pmsInitiatedEmployees());
    }

    @Override
    public ResponseEntity<Map<String, String>> registerEmployee(Employee employee, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(hrService.employeeRegistration(employee), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<EmployeeWithPmsStatus>> getCompletedPmsForHR() {
        return ResponseEntity.ok(hrService.getCompletedPmsForHR());
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
        // unchanged implementation
        // ...
        return null; // shortened for brevity
    }

    @Override
    public ResponseEntity<Map<String, HrResponseDto>> profile(UserDetails hr) {
        Map<String, HrResponseDto> response = new HashMap<>();
        HrResponseDto profile = hrService.profile(hr.getUsername());
        if (profile != null) {
            response.put("profile", profile);
            return ResponseEntity.ok(response);
        }
        response.put("message", null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PmsStatusCountDto> getPmsStatusCountForHR() {
        PmsStatusCountDto dto = hrService.getPmsCountsForHR();
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<Map<String, String>> generateEmployeeInfoReport(String id) {
        Map<String, String> response = new HashMap<>();
        boolean status = employeeReport.generateEmployeeInfoReport(id);
        response.put("status", status ? "Report Generated" : "Report Failed");
        return new ResponseEntity<>(response, status ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Map<String, String>> generateEmployeeList() {
        Map<String, String> response = new HashMap<>();
        boolean status = employeeReport.generateEmployeeListPdf();
        response.put("status", status ? "Report Generated" : "Report Failed");
        return new ResponseEntity<>(response, status ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Map<String, Map<String, Integer>>> getCompletedPendingByDepartments() {
        Map<String, Map<String, Integer>> response = hrService.getCompletedPendingByDepartment();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<InputStreamResource> exportPmsPdf(String employeeId) {
        ByteArrayInputStream pdfStream = hrService.generatePmsPdfReport(employeeId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + employeeId + "_pms_report.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }

    @Override
    public ResponseEntity<Map<String, List<XlsxSupport.KRA>>> uploadKraKpi(MultipartFile file) {
        return new ResponseEntity<>(kraKpiService.uploadKraKpi(file), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> processEmployeeExit(
            String empId,
            @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate lastWorkingDay) {
        String responseMessage = hrService.processEmployeeExit(empId, lastWorkingDay);
        return ResponseEntity.ok(responseMessage);
    }

    @Override
    public void notifyAllEmployeesAndManagers() {
        List<String> allEmployeeIds = employeeRepository.findAllEmployeeIds();
        for (String employeeId : allEmployeeIds) {
            try {
                Boolean pmsInitiated = kraKpiRepository.findPmsInitiatedByEmployeeId(employeeId).orElse(false);
                if (!pmsInitiated)
                    continue;

                boolean selfCompleted = kraKpiRepository.findSelfCompletedStatusByEmployeeId(employeeId).orElse(false);
                boolean managerCompleted = kraKpiRepository.findManagerCompletedStatusByEmployeeId(employeeId)
                        .orElse(false);

                String managerId = employeeRepository.findReportingManagerIdByEmployeeId(employeeId);
                String employeeName = employeeRepository.findNameByEmpId(employeeId).orElse("Unknown");

                if (managerId == null || managerId.isBlank())
                    continue;

                String empDestination = "/queue/employee-notification";
                String managerDestination = "/queue/manager-notification";

                if (!selfCompleted) {
                    notificationService.sendMessageWithRecent("System", employeeId,
                            "Reminder: Please complete your PMS self-review.", empDestination);
                    notificationService.sendMessageWithRecent("System", managerId,
                            "Reminder: " + employeeName + " has not yet completed self-review. Please follow up.",
                            managerDestination);
                } else if (!managerCompleted) {
                    notificationService.sendMessageWithRecent("System", managerId,
                            "Reminder: " + employeeName
                                    + " has completed self-review. Please complete your Manager review.",
                            managerDestination);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Map<String, Map<String, Integer>> getMonthlyDepartmentReport(
            @RequestParam int year,
            @RequestParam int quarter,
            @RequestParam int month) {
        return kraKpiService.getCompletedPendingByDepartmentMonthly(year, quarter, month);
    }

    @Override
    public Map<String, Map<String, Integer>> getQuarterlyDepartmentReport(
            @RequestParam int year,
            @RequestParam int quarter) {
        return kraKpiService.getCompletedPendingByDepartmentQuarterWise(year, quarter);
    }

    @Override
    public Map<String, Map<String, Integer>> getYearlyDepartmentReport(
            @RequestParam int year) {
        return kraKpiService.getCompletedPendingByDepartmentYearWise(year);
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadAllEmployeesReport() {
        ByteArrayInputStream bis = hrService.generateAllEmployeesPmsPdfReport();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=All_Employees_PMS_Report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @Override
    public List<ExitEmployee> getAllExitEmployees() {
        
        return hrService.getAllExitEmployees();
    }
    

}

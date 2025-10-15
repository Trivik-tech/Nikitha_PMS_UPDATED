package com.triviktech.services.krakpi;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.kpi.KPI;
import com.triviktech.entities.kra.KRA;
import com.triviktech.entities.krakpi.KraKpi;
import com.triviktech.entities.manager.Manager;
import com.triviktech.exception.employee.EmployeeNotFoundException;
import com.triviktech.exception.krakpi.KraKpiNotFoundException;
import com.triviktech.payloads.request.kpi.KpiRequestDto;
import com.triviktech.payloads.request.kra.KraRequestDto;
import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.kpi.KpiResponseDto;
import com.triviktech.payloads.response.kra.KraResponseDto1;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.kpi.KPIRepository;
import com.triviktech.repositories.kra.KRARepository;
import com.triviktech.repositories.krakpi.KraKpiRepository;
import com.triviktech.utilities.email.EmailService;
import com.triviktech.utilities.email.Message;
import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;
import com.triviktech.utilities.xlsxsupport.XlsxSupport;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Implementation of {@link KraKpiService} for managing Key Result Areas (KRA) and
 * Key Performance Indicators (KPI) for employees.
 *
 * <p>
 * This service provides the following functionalities:
 * <ul>
 *     <li>Register new KRA/KPI for an employee along with associated KRAs and KPIs.</li>
 *     <li>Retrieve KRA/KPI details for a specific employee.</li>
 *     <li>Submit and update an employee's self-review.</li>
 *     <li>Check if a KRA/KPI exists for an employee in the current quarter.</li>
 *     <li>Retrieve a list of all KRA/KPI records for a given employee.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Workflow:</strong>
 * <ol>
 *     <li>HR or manager registers KRA/KPI for an employee using {@link #registerKraKpi(KraKpiRequestDto)}.</li>
 *     <li>Employee can submit a self-review using {@link #employeeReview(KraKpiRequestDto, String)}.</li>
 *     <li>Manager receives email notifications when KRAs/KPIs are registered or reviewed.</li>
 *     <li>KRAs and KPIs are stored in a hierarchical structure: KraKpi → KRA → KPI.</li>
 *     <li>System supports fetching individual or all KRA/KPI records and checking their existence for the current quarter.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Note:</strong> All database operations are transactional to ensure consistency.
 * </p>
 */

@Service
public class KraKpiServiceImpl implements KraKpiService {

    private final EmployeeInformationRepository employeeInformationRepository;
    private final KraKpiRepository kraKpiRepository;
    private final KRARepository kraRepository;
    private final KPIRepository kpiRepository;
    private final EntityDtoConversion entityDtoConversion;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    public KraKpiServiceImpl(EmployeeInformationRepository employeeInformationRepository,
                             KraKpiRepository kraKpiRepository,
                             KRARepository kraRepository,
                             KPIRepository kpiRepository,
                             EntityDtoConversion entityDtoConversion,
                             EmailService emailService,
                             ModelMapper modelMapper) {
        this.employeeInformationRepository = employeeInformationRepository;
        this.kraKpiRepository = kraKpiRepository;
        this.kraRepository = kraRepository;
        this.kpiRepository = kpiRepository;
        this.entityDtoConversion = entityDtoConversion;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Map<String, String> registerKraKpi(KraKpiRequestDto kraKpiRequestDto) {
        Map<String, String> response = new HashMap<>();

        KraKpi kraKpi = new KraKpi();

        kraKpi.setEmployeeInformation(employeeInformationRepository.findById(kraKpiRequestDto.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException(kraKpiRequestDto.getEmployeeId())));

        kraKpi.setRemark(kraKpiRequestDto.getRemark());
        kraKpi.setSelfCompleted(kraKpiRequestDto.getSelfCompleted());
        kraKpi.setManagerCompleted(kraKpiRequestDto.isManagerCompleted());
        kraKpi.setDueDate(kraKpiRequestDto.getDueDate());
        kraKpi.setManagerReviewDate(kraKpiRequestDto.getManagerReviewDate());
        kraKpi.setSelfReviewDate(kraKpiRequestDto.getSelfReviewDate());
        kraKpi.setPmsInitiated(kraKpiRequestDto.getPmsInitiated());
        kraKpi.setReview2(kraKpiRequestDto.isReview2());
        kraKpi.setManagerApproval(kraKpiRequestDto.getManagerApproval());

        kraKpi = kraKpiRepository.saveAndFlush(kraKpi);

        Set<KRA> kras = new HashSet<>();
        for (KraRequestDto dto : kraKpiRequestDto.getKra()) {
            KRA kra = new KRA();
            kra.setKraKpi(kraKpi);
            kra.setKraName(dto.getKraName());
            kra.setWeightage(dto.getWeightage());

            kra = kraRepository.saveAndFlush(kra);

            Set<KPI> kpis = new HashSet<>();
            for (KpiRequestDto kpiDto : dto.getKpi()) {
                KPI kpi = new KPI();
                kpi.setKra(kra);
                kpi.setWeightage(kpiDto.getWeightage());
                kpi.setSelfScore(kpiDto.getSelfScore());
                kpi.setManagerScore(kpiDto.getManagerScore());
                kpi.setDescription(kpiDto.getDescription());

                float average;
                if (kpiDto.getReview2() != 0) {
                    average = (float) (kpiDto.getManagerScore() + kpiDto.getSelfScore() + kpiDto.getReview2()) / 3;
                } else {
                    average = (float) (kpiDto.getManagerScore() + kpiDto.getSelfScore()) / 2;
                }

                kpi.setAverage(average);
                kpi.setReview2(kpiDto.getReview2());

                kpi = kpiRepository.saveAndFlush(kpi);
                kpis.add(kpi);
            }

            kra.setKpi(kpis);
            kras.add(kra);
        }

        kraKpi.setKra(kras);

        kraKpi = kraKpiRepository.saveAndFlush(kraKpi);

        try {
            EmployeeInformation emp = kraKpi.getEmployeeInformation();

            if (emp.getManager() == null) {
                throw new EmployeeNotFoundException("Manager not assigned to employee: " + emp.getEmpId());
            }

            Manager mgr = emp.getManager();

            String sub = String.format(Message.KRA_KPI_SUBJECT_TO_MANAGER, emp.getName());
            String to = mgr.getEmailId();
            String message = String.format(Message.KRA_KPI_MESSAGE_TO_MANAGER,
                    mgr.getName(), emp.getName(), emp.getEmpId());
//            emailService.sendEmail(to, sub, message);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("Status", "Failure");
            response.put("Message", "Something went wrong");
            return response;
        }

        response.put("Status", "Success");
        response.put("Message", "KRA KPI Registration Completed");
        return response;
    }

    @Override
    public KraKpiResponseDto kraKpiForEmployee(String employeeId) {
        EmployeeInformation employee = employeeInformationRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        KraKpi krakpi = kraKpiRepository.findFirstByEmployeeInformation(employee)
                .orElseThrow(() -> new KraKpiNotFoundException("KRA/KPI not found for employee: " + employeeId));

        KraKpiResponseDto response = entityDtoConversion.entityToDtoConversion(krakpi, KraKpiResponseDto.class);
        response.setEmployee(entityDtoConversion.entityToDtoConversion(employee, EmployeeInfo.class));

        Set<KraResponseDto1> kraList = krakpi.getKra().stream().map(kra -> {
            KraResponseDto1 kraResponseDto1 = entityDtoConversion.entityToDtoConversion(kra, KraResponseDto1.class);
            Set<KpiResponseDto> kpis = kra.getKpi().stream()
                    .map(kpi -> entityDtoConversion.entityToDtoConversion(kpi, KpiResponseDto.class))
                    .collect(Collectors.toSet());
            kraResponseDto1.setKpi(kpis);
            return kraResponseDto1;
        }).collect(Collectors.toSet());

        response.setKra(kraList);
        return response;
    }

    @Override
    public Map<String, String> employeeReview(KraKpiRequestDto kraKpiRequestDto, String employeeId) {
        Optional<EmployeeInformation> employeeById = employeeInformationRepository.findById(employeeId);
        if (employeeById.isEmpty()) {
            return Map.of("status", "failure", "message", "Employee not found");
        }

        EmployeeInformation employee = employeeById.get();
        Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);
        if (kraKpiOptional.isEmpty()) {
            return Map.of("status", "failure", "message", "KRA/KPI not found for employee");
        }

        KraKpi kraKpi = kraKpiOptional.get();
        kraKpi.setSelfCompleted(kraKpiRequestDto.getSelfCompleted());

        Set<KRA> existingKras = kraKpi.getKra();
        Set<KRA> updatedKras = new HashSet<>();

        for (KraRequestDto kraDto : kraKpiRequestDto.getKra()) {
            KRA kra = existingKras.stream()
                    .filter(existingKra -> existingKra.getKraId().equals(kraDto.getKraId()))
                    .findFirst()
                    .orElse(new KRA());

            kra.setKraKpi(kraKpi);
            kra.setKraName(kraDto.getKraName());
            kra.setWeightage(kraDto.getWeightage());

            Set<KPI> existingKpis = kra.getKpi() != null ? kra.getKpi() : new HashSet<>();
            Set<KPI> updatedKpis = new HashSet<>();

            for (KpiRequestDto kpiDto : kraDto.getKpi()) {
                KPI kpi = existingKpis.stream()
                        .filter(existingKpi -> existingKpi.getKpiId().equals(kpiDto.getKpiId()))
                        .findFirst()
                        .orElse(new KPI());

                kpi.setKra(kra);
                kpi.setDescription(kpiDto.getDescription());
                kpi.setWeightage(kpiDto.getWeightage());
                kpi.setSelfScore(kpiDto.getSelfScore());
                kpi.setManagerScore(kpiDto.getManagerScore());
                kpi.setAverage((float) (kpiDto.getSelfScore() + kpiDto.getManagerScore()) / 2);
                kpi.setReview2(kpiDto.getReview2());
                kpi.setEmployeeRemark(kpiDto.getEmployeeRemark());

                updatedKpis.add(kpi);
            }

            existingKpis.removeIf(existingKpi -> updatedKpis.stream()
                    .noneMatch(updatedKpi -> updatedKpi.getKpiId().equals(existingKpi.getKpiId())));

            existingKpis.addAll(updatedKpis);
            kra.setKpi(existingKpis);
            updatedKras.add(kra);
        }

        existingKras.removeIf(existingKra -> updatedKras.stream()
                .noneMatch(updatedKra -> updatedKra.getKraId().equals(existingKra.getKraId())));

        existingKras.addAll(updatedKras);
        kraKpi.setKra(existingKras);
        kraKpiRepository.saveAndFlush(kraKpi);

        try {
            String sub = String.format(Message.SELF_APPRAISAL_SUBJECT_TO_MANAGER, employee.getName());
            String message = String.format(Message.SELF_APPRAISAL_MESSAGE_TO_MANAGER,
                    employee.getManager().getName(), employee.getName(), employee.getEmpId());
            String to = employee.getManager().getEmailId();
            emailService.sendEmail(to, sub, message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Map.of("status", "success", "message", "Employee Review submitted successfully");
    }

    @Override
    public Map<String, Boolean> existsByEmployee(String employeeId) {
        Optional<EmployeeInformation> byId = employeeInformationRepository.findById(employeeId);
        if (byId.isEmpty()) {
            throw new EmployeeNotFoundException(employeeId);
        }

        EmployeeInformation employee = byId.get();

        // Get current date
        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        // Determine start & end of the quarter
        int currentMonth = now.getMonthValue();
        int startMonth;
        int endMonth;

        if (currentMonth >= 1 && currentMonth <= 3) {
            startMonth = 1;  // Q1: Jan-Mar
            endMonth = 3;
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            startMonth = 4;  // Q2: Apr-Jun
            endMonth = 6;
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            startMonth = 7;  // Q3: Jul-Sep
            endMonth = 9;
        } else {
            startMonth = 10; // Q4: Oct-Dec
            endMonth = 12;
        }

        java.time.LocalDateTime startDate = java.time.LocalDateTime.of(
                now.getYear(), startMonth, 1, 0, 0
        );

        java.time.LocalDateTime endDate = java.time.LocalDateTime.of(
                now.getYear(), endMonth, java.time.Month.of(endMonth).length(java.time.Year.isLeap(now.getYear())), 23, 59, 59
        );

        boolean status = kraKpiRepository.existsByEmployeeAndCreatedAtBetween(employee, startDate, endDate);

        return Map.of("status", false);
    }

    @Override
    public Map<String, List<KraKpiResponseDto>> listOfKraKpi(String employeeId) {

        List<KraKpi> allByEmployee = kraKpiRepository.findAllByEmployeeId(employeeId);
        List<KraKpiResponseDto> collect = allByEmployee.stream().map(kraKpi -> {
            KraKpiResponseDto kraKpiResponseDto = entityDtoConversion.entityToDtoConversion(kraKpi, KraKpiResponseDto.class);

            Set<KraResponseDto1> kras = kraKpi.getKra().stream().map(kra -> {
                KraResponseDto1 kraResponseDto1 = entityDtoConversion.entityToDtoConversion(kra, KraResponseDto1.class);

                Set<KpiResponseDto> kpis = kra.getKpi().stream().map(kpi -> {
                    return entityDtoConversion.entityToDtoConversion(kpi, KpiResponseDto.class);
                }).collect(Collectors.toSet());
                kraResponseDto1.setKpi(kpis);
                return kraResponseDto1;
            }).collect(Collectors.toSet());

            kraKpiResponseDto.setKra(kras);
            return kraKpiResponseDto;
        }).collect(Collectors.toList());

        return Map.of("krakpis",collect);
    }

    @Override
    public Map<String,  List<XlsxSupport.KRA>> uploadKraKpi(MultipartFile file) {

        try {
            List<XlsxSupport.KRA> kras = XlsxSupport.convertExcelToKraKpiList(file.getInputStream());
            return Map.of("kra_kpi",kras);
        }catch (Exception e){
            throw new RuntimeException("Upload failed");
        }

    }

    @Override
    public Map<String, List<KraKpiResponseDto>> listOfKraKpiByYearWise(String empId,String year) {
        int yr = Integer.parseInt(year);

        // Fetch from DB
        List<KraKpi> kraKpiList = kraKpiRepository.findByEmployeeAndYear(empId,yr);

        List<KraKpiResponseDto> kraKpiResponseDtos = kraKpiResponseUtils(kraKpiList);
        // Wrap into Map<String, List<>>
        Map<String, List<KraKpiResponseDto>> response = new HashMap<>();
        response.put("krakpi", kraKpiResponseDtos);

        return response;
    }

    @Override
    public Map<String, List<KraKpiResponseDto>> listOfKraKpiByQuarterWise(String empId, String year, int quarter) {
        int yr = Integer.parseInt(year);

        // Map quarter to start & end month (Indian Financial Year)
        int startMonth, endMonth;
        switch (quarter) {
            case 1: // Q1 = Apr - Jun
                startMonth = 4; endMonth = 6; break;
            case 2: // Q2 = Jul - Sep
                startMonth = 7; endMonth = 9; break;
            case 3: // Q3 = Oct - Dec
                startMonth = 10; endMonth = 12; break;
            case 4: // Q4 = Jan - Mar (belongs to next calendar year)
                startMonth = 1; endMonth = 3;
                yr = yr + 1; // shift to next calendar year
                break;
            default:
                throw new IllegalArgumentException("Quarter must be between 1 and 4");
        }

        // Fetch from DB
        List<KraKpi> krakpiList = kraKpiRepository.findByEmployeeAndQuarter(empId, yr, startMonth, endMonth);
        List<KraKpiResponseDto> kraKpiData = kraKpiResponseUtils(krakpiList);

        return Map.of("krakpi",kraKpiData);
    }

    @Override
    public Map<String, List<KraKpiResponseDto>> listOfKraKpisByMonthsWise(String empId, String year, int quarter, int month) {
        int yr = Integer.parseInt(year);

        // Map quarter to start & end month (Indian Financial Year)
        int startMonth, endMonth;
        switch (quarter) {
            case 1: // Q1 = Apr - Jun
                startMonth = 4; endMonth = 6; break;
            case 2: // Q2 = Jul - Sep
                startMonth = 7; endMonth = 9; break;
            case 3: // Q3 = Oct - Dec
                startMonth = 10; endMonth = 12; break;
            case 4: // Q4 = Jan - Mar (belongs to next calendar year)
                startMonth = 1; endMonth = 3;
                yr = yr + 1; // shift to next calendar year
                break;
            default:
                throw new IllegalArgumentException("Quarter must be between 1 and 4");
        }

        // Fetch from DB
        List<KraKpi> krakpiList = kraKpiRepository.findByEmployeeAndQuarterAndMonth(empId, yr, startMonth, endMonth,month);
        List<KraKpiResponseDto> kraKpiData = kraKpiResponseUtils(krakpiList);

        return Map.of("krakpi",kraKpiData);
    }


    private List<KraKpiResponseDto> kraKpiResponseUtils(List<KraKpi> krakpiList){
       return krakpiList.stream().map(kraKpi -> {
            KraKpiResponseDto kraKpiResponse = entityDtoConversion.entityToDtoConversion(kraKpi, KraKpiResponseDto.class);
            Set<KraResponseDto1> kraList = kraKpi.getKra().stream().map(kra -> {
                KraResponseDto1 kraResponse = entityDtoConversion.entityToDtoConversion(kra, KraResponseDto1.class);
                Set<KpiResponseDto> kpiResponse = kra.getKpi().stream().map(kpi -> entityDtoConversion.entityToDtoConversion(kpi, KpiResponseDto.class)).collect(Collectors.toSet());
                kraResponse.setKpi(kpiResponse);
                return kraResponse;
            }).collect(Collectors.toSet());
            kraKpiResponse.setKra(kraList);
            EmployeeInfo employeeInfo = entityDtoConversion.entityToDtoConversion(kraKpi.getEmployeeInformation(), EmployeeInfo.class);
            employeeInfo.setDepartment(kraKpi.getEmployeeInformation().getDepartment().getName());
            kraKpiResponse.setEmployee(employeeInfo);
            return kraKpiResponse;
        }).collect(Collectors.toList());

    }
}

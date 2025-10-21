package com.triviktech.services.krakpi;

import com.triviktech.entities.EmployeeReview;
import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.kpi.KPI;
import com.triviktech.entities.kra.KRA;
import com.triviktech.entities.krakpi.KraKpi;
import com.triviktech.entities.krakpibridge.KraKpiBridge;
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
import com.triviktech.repositories.employeereviewrepository.EmployeeReviewRepository;
import com.triviktech.repositories.kpi.KPIRepository;
import com.triviktech.repositories.kra.KRARepository;
import com.triviktech.repositories.krakpi.KraKpiRepository;
import com.triviktech.repositories.krakpibridgerepository.KraKpiBridgeRepository;
import com.triviktech.utilities.email.EmailService;
import com.triviktech.utilities.email.Message;
import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;
import com.triviktech.utilities.xlsxsupport.XlsxSupport;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of {@link KraKpiService} for managing Key Result Areas (KRA)
 * and
 * Key Performance Indicators (KPI) for employees.
 *
 * <p>
 * This service provides the following functionalities:
 * <ul>
 * <li>Register new KRA/KPI for an employee along with associated KRAs and
 * KPIs.</li>
 * <li>Retrieve KRA/KPI details for a specific employee.</li>
 * <li>Submit and update an employee's self-review.</li>
 * <li>Check if a KRA/KPI exists for an employee in the current quarter.</li>
 * <li>Retrieve a list of all KRA/KPI records for a given employee.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Workflow:</strong>
 * <ol>
 * <li>HR or manager registers KRA/KPI for an employee using
 * {@link #registerKraKpi(KraKpiRequestDto)}.</li>
 * <li>Employee can submit a self-review using
 * {@link #employeeReview(KraKpiRequestDto, String)}.</li>
 * <li>Manager receives email notifications when KRAs/KPIs are registered or
 * reviewed.</li>
 * <li>KRAs and KPIs are stored in a hierarchical structure: KraKpi → KRA →
 * KPI.</li>
 * <li>System supports fetching individual or all KRA/KPI records and checking
 * their existence for the current quarter.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Note:</strong> All database operations are transactional to ensure
 * consistency.
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
    private final KraKpiBridgeRepository kraKpiBridgeRepository;
    private final EmployeeReviewRepository employeeReviewRepository;

    public KraKpiServiceImpl(EmployeeInformationRepository employeeInformationRepository,
            KraKpiRepository kraKpiRepository,
            KRARepository kraRepository,
            KPIRepository kpiRepository,
            EntityDtoConversion entityDtoConversion,
            EmailService emailService,
            ModelMapper modelMapper, KraKpiBridgeRepository kraKpiBridgeRepository,EmployeeReviewRepository employeeReviewRepository) {
        this.employeeInformationRepository = employeeInformationRepository;
        this.kraKpiRepository = kraKpiRepository;
        this.kraRepository = kraRepository;
        this.kpiRepository = kpiRepository;
        this.entityDtoConversion = entityDtoConversion;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
        this.kraKpiBridgeRepository = kraKpiBridgeRepository;
        this.employeeReviewRepository=employeeReviewRepository;
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
            // emailService.sendEmail(to, sub, message);

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

    // @Override
    // public KraKpiResponseDto kraKpiForEmployee(String employeeId) {
    //     EmployeeInformation employee = employeeInformationRepository.findById(employeeId)
    //             .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

    //     KraKpi krakpi = kraKpiRepository.findFirstByEmployeeInformation(employee)
    //             .orElseThrow(() -> new KraKpiNotFoundException("KRA/KPI not found for employee: " + employeeId));

    //     KraKpiResponseDto response = entityDtoConversion.entityToDtoConversion(krakpi, KraKpiResponseDto.class);
    //     response.setEmployee(entityDtoConversion.entityToDtoConversion(employee, EmployeeInfo.class));

    //     Set<KraResponseDto1> kraList = krakpi.getKra().stream().map(kra -> {
    //         KraResponseDto1 kraResponseDto1 = entityDtoConversion.entityToDtoConversion(kra, KraResponseDto1.class);
    //         Set<KpiResponseDto> kpis = kra.getKpi().stream()
    //                 .map(kpi -> entityDtoConversion.entityToDtoConversion(kpi, KpiResponseDto.class))
    //                 .collect(Collectors.toSet());
    //         kraResponseDto1.setKpi(kpis);
    //         return kraResponseDto1;
    //     }).collect(Collectors.toSet());

    //     response.setKra(kraList);
    //     return response;
    // }

@Override
public KraKpiResponseDto kraKpiForEmployee(String employeeId) {
    // 1️⃣ Fetch employee
    EmployeeInformation employee = employeeInformationRepository.findById(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

    // 2️⃣ Fetch all KRA/KPI sets for the employee (latest first)
    List<KraKpi> allKraKpi = kraKpiRepository.findTopByEmployeeInformationOrderByCreatedAtDesc(employee);

    if (allKraKpi.isEmpty()) {
        throw new KraKpiNotFoundException("No KRA/KPI found for employee: " + employeeId);
    }

    // 3️⃣ Filter the KRA/KPI set for the current quarter
    LocalDateTime now = LocalDateTime.now();
    KraKpi currentQuarterKraKpi = allKraKpi.stream()
            .filter(k -> {
                LocalDateTime start = k.getCreatedAt();
                LocalDateTime end = start.plusMonths(3); // quarter = 3 months
                return now.isAfter(start) && now.isBefore(end);
            })
            .findFirst()
            .orElseThrow(() -> new KraKpiNotFoundException("No KRA/KPI found for the current quarter"));

    // 4️⃣ Prepare response DTO
    KraKpiResponseDto response = entityDtoConversion.entityToDtoConversion(currentQuarterKraKpi, KraKpiResponseDto.class);
    response.setEmployee(entityDtoConversion.entityToDtoConversion(employee, EmployeeInfo.class));

    // 5️⃣ Fetch KRAs via the bridge table
    List<KraKpiBridge> bridgeList = kraKpiBridgeRepository.findAllByKraKpi(currentQuarterKraKpi);

    Set<KraResponseDto1> kraList = bridgeList.stream()
            .map(bridge -> {
                KRA kra = bridge.getKra();
                KraResponseDto1 kraDto = entityDtoConversion.entityToDtoConversion(kra, KraResponseDto1.class);

                // 6️⃣ Fetch KPIs of this KRA
                Set<KpiResponseDto> kpiDtos = kra.getKpi().stream()
                        .map(kpi -> entityDtoConversion.entityToDtoConversion(kpi, KpiResponseDto.class))
                        .collect(Collectors.toSet());

                kraDto.setKpi(kpiDtos);
                return kraDto;
            })
            .collect(Collectors.toSet());

    response.setKra(kraList);

    return response;
}



    // @Override
    // public Map<String, String> employeeReview(KraKpiRequestDto kraKpiRequestDto, String employeeId) {
    //     Optional<EmployeeInformation> employeeById = employeeInformationRepository.findById(employeeId);
    //     if (employeeById.isEmpty()) {
    //         return Map.of("status", "failure", "message", "Employee not found");
    //     }

    //     EmployeeInformation employee = employeeById.get();
    //     Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);
    //     if (kraKpiOptional.isEmpty()) {
    //         return Map.of("status", "failure", "message", "KRA/KPI not found for employee");
    //     }

    //     KraKpi kraKpi = kraKpiOptional.get();
    //     kraKpi.setSelfCompleted(kraKpiRequestDto.getSelfCompleted());

    //     Set<KRA> existingKras = kraKpi.getKra();
    //     Set<KRA> updatedKras = new HashSet<>();

    //     for (KraRequestDto kraDto : kraKpiRequestDto.getKra()) {
    //         KRA kra = existingKras.stream()
    //                 .filter(existingKra -> existingKra.getKraId().equals(kraDto.getKraId()))
    //                 .findFirst()
    //                 .orElse(new KRA());

    //         kra.setKraKpi(kraKpi);
    //         kra.setKraName(kraDto.getKraName());
    //         kra.setWeightage(kraDto.getWeightage());

    //         Set<KPI> existingKpis = kra.getKpi() != null ? kra.getKpi() : new HashSet<>();
    //         Set<KPI> updatedKpis = new HashSet<>();

    //         for (KpiRequestDto kpiDto : kraDto.getKpi()) {
    //             KPI kpi = existingKpis.stream()
    //                     .filter(existingKpi -> existingKpi.getKpiId().equals(kpiDto.getKpiId()))
    //                     .findFirst()
    //                     .orElse(new KPI());

    //             kpi.setKra(kra);
    //             kpi.setDescription(kpiDto.getDescription());
    //             kpi.setWeightage(kpiDto.getWeightage());
    //             kpi.setSelfScore(kpiDto.getSelfScore());
    //             kpi.setManagerScore(kpiDto.getManagerScore());
    //             kpi.setAverage((float) (kpiDto.getSelfScore() + kpiDto.getManagerScore()) / 2);
    //             kpi.setReview2(kpiDto.getReview2());
    //             kpi.setEmployeeRemark(kpiDto.getEmployeeRemark());

    //             updatedKpis.add(kpi);
    //         }

    //         existingKpis.removeIf(existingKpi -> updatedKpis.stream()
    //                 .noneMatch(updatedKpi -> updatedKpi.getKpiId().equals(existingKpi.getKpiId())));

    //         existingKpis.addAll(updatedKpis);
    //         kra.setKpi(existingKpis);
    //         updatedKras.add(kra);
    //     }

    //     existingKras.removeIf(existingKra -> updatedKras.stream()
    //             .noneMatch(updatedKra -> updatedKra.getKraId().equals(existingKra.getKraId())));

    //     existingKras.addAll(updatedKras);
    //     kraKpi.setKra(existingKras);
    //     kraKpiRepository.saveAndFlush(kraKpi);

    //     try {
    //         String sub = String.format(Message.SELF_APPRAISAL_SUBJECT_TO_MANAGER, employee.getName());
    //         String message = String.format(Message.SELF_APPRAISAL_MESSAGE_TO_MANAGER,
    //                 employee.getManager().getName(), employee.getName(), employee.getEmpId());
    //         String to = employee.getManager().getEmailId();
    //         emailService.sendEmail(to, sub, message);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     return Map.of("status", "success", "message", "Employee Review submitted successfully");
    // }


  @Transactional
public Map<String, String> employeeReview(KraKpiRequestDto kraKpiRequestDto, String employeeId) {
EmployeeInformation employee = employeeInformationRepository.findById(employeeId)
.orElseThrow(() -> new EmployeeNotFoundException(employeeId));

List<KraKpi> kraKpiList = kraKpiRepository.findTopByEmployeeInformationOrderByCreatedAtDesc(employee);  
if (kraKpiList.isEmpty()) {  
    return Map.of("status", "failure", "message", "No KRA/KPI found for employee");  
}  

LocalDateTime now = LocalDateTime.now();  
KraKpi currentQuarterKraKpi = kraKpiList.stream()  
        .filter(k -> {  
            LocalDateTime start = k.getCreatedAt();  
            LocalDateTime end = start.plusMonths(3);  
            return !now.isBefore(start) && !now.isAfter(end);  
        })  
        .findFirst()  
        .orElseThrow(() -> new KraKpiNotFoundException("No KRA/KPI found for current quarter"));  

//  Fetch the KRA-KPI relationships  
List<KraKpiBridge> bridgeList = kraKpiBridgeRepository.findAllByKraKpi(currentQuarterKraKpi);  
Set<KRA> kras = bridgeList.stream().map(KraKpiBridge::getKra).collect(Collectors.toSet());  

for (KraRequestDto kraDto : kraKpiRequestDto.getKra()) {  
    KRA kra = kras.stream()  
            .filter(k -> k.getKraId().equals(kraDto.getKraId()))  
            .findFirst()  
            .orElse(null);  
    if (kra != null) {  
        for (KpiRequestDto kpiDto : kraDto.getKpi()) {  
            KPI kpi = kra.getKpi().stream()  
                    .filter(kp -> kp.getKpiId().equals(kpiDto.getKpiId()))  
                    .findFirst()  
                    .orElse(null);  
            if (kpi != null) {  
                // 🔹 Create new EmployeeReview record for this employee-KRA-KPI  
                EmployeeReview review = new EmployeeReview();  
                review.setKraKpi(currentQuarterKraKpi);  
                review.setKra(kra);  
                review.setKpi(kpi);  
                review.setSelfScore(kpiDto.getSelfScore());  
                review.setEmployeeRemark(kpiDto.getEmployeeRemark());  
                  
                float selfScore = kpiDto.getSelfScore() != null ? kpiDto.getSelfScore() : 0F;  
                float managerScore = 0F; // manager yet to review  
                review.setAverageScore((selfScore + managerScore) / 2);  

                employeeReviewRepository.save(review);  
            }  
        }  
    }  
}  

currentQuarterKraKpi.setSelfCompleted(true);  
kraKpiRepository.saveAndFlush(currentQuarterKraKpi);  

// send email to manager  
try {  
    String subject = String.format(Message.SELF_APPRAISAL_SUBJECT_TO_MANAGER, employee.getName());  
    String message = String.format(Message.SELF_APPRAISAL_MESSAGE_TO_MANAGER,  
                                   employee.getManager().getName(), employee.getName(), employee.getEmpId());  
    emailService.sendEmail(employee.getManager().getEmailId(), subject, message);  
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
            startMonth = 1; // Q1: Jan-Mar
            endMonth = 3;
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            startMonth = 4; // Q2: Apr-Jun
            endMonth = 6;
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            startMonth = 7; // Q3: Jul-Sep
            endMonth = 9;
        } else {
            startMonth = 10; // Q4: Oct-Dec
            endMonth = 12;
        }

        java.time.LocalDateTime startDate = java.time.LocalDateTime.of(
                now.getYear(), startMonth, 1, 0, 0);

        java.time.LocalDateTime endDate = java.time.LocalDateTime.of(
                now.getYear(), endMonth, java.time.Month.of(endMonth).length(java.time.Year.isLeap(now.getYear())), 23,
                59, 59);

        boolean status = kraKpiRepository.existsByEmployeeAndCreatedAtBetween(employee, startDate, endDate);

        return Map.of("status", status);
    }

    @Override
    public Map<String, List<KraKpiResponseDto>> listOfKraKpi(String employeeId) {

        List<KraKpi> allByEmployee = kraKpiRepository.findAllByEmployeeId(employeeId);
        List<KraKpiResponseDto> collect = allByEmployee.stream().map(kraKpi -> {
            KraKpiResponseDto kraKpiResponseDto = entityDtoConversion.entityToDtoConversion(kraKpi,
                    KraKpiResponseDto.class);

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

        return Map.of("krakpis", collect);
    }

    @Override
    @Transactional
    public Map<String, List<XlsxSupport.KRA>> uploadKraKpi(MultipartFile file) {
        try {
            // Step 1: Extract KRAs + KPIs from Excel
            List<XlsxSupport.KRA> extractedKras = XlsxSupport.convertExcelToKraKpiList(file.getInputStream());

            for (XlsxSupport.KRA extractedKra : extractedKras) {

                // Step 2: Check if KRA already exists
                Optional<KRA> existingKraOpt = kraRepository.findByKraNameIgnoreCase(extractedKra.getKraName());
                KRA kraEntity;

                if (existingKraOpt.isPresent()) {
                    // KRA exists -> just reuse it, skip update
                    kraEntity = existingKraOpt.get();
                } else {
                    // New KRA -> create & save
                    kraEntity = new KRA();
                    kraEntity.setKraName(extractedKra.getKraName());
                    kraEntity.setWeightage(extractedKra.getWeightage());
                    kraEntity = kraRepository.save(kraEntity);
                }

                // Step 3: Handle KPIs for this KRA
                if (extractedKra.getKpis() != null && !extractedKra.getKpis().isEmpty()) {
                    for (XlsxSupport.KPI extractedKpi : extractedKra.getKpis()) {

                        Optional<KPI> existingKpiOpt = kpiRepository.findByDescriptionIgnoreCaseAndKra(
                                extractedKpi.getDescription(), kraEntity);

                        if (existingKpiOpt.isPresent()) {
                            // KPI exists -> skip (no update)
                            continue;
                        }

                        // New KPI -> create & save
                        KPI kpiEntity = new KPI();
                        kpiEntity.setDescription(extractedKpi.getDescription());
                        kpiEntity.setWeightage(extractedKpi.getWeightage());
                        kpiEntity.setKra(kraEntity);

                        kpiRepository.save(kpiEntity);
                    }
                }
            }

            // Step 4: Return structure to frontend
            return Map.of("kra_kpi", extractedKras);

        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, List<KraKpiResponseDto>> listOfKraKpiByYearWise(String empId, String year) {
        int yr = Integer.parseInt(year);

        // Fetch from DB
        List<KraKpi> kraKpiList = kraKpiRepository.findByEmployeeAndYear(empId, yr);

        List<KraKpiResponseDto> kraKpiResponseDtos = kraKpiResponseUtils(kraKpiList);
        // Wrap into Map<String, List<>>
        Map<String, List<KraKpiResponseDto>> response = new HashMap<>();
        response.put(year, kraKpiResponseDtos);

        return response;
    }

    @Override
    public Map<String, List<KraKpiResponseDto>> listOfKraKpiByQuarterWise(String empId, String year, int quarter) {
        int yr = Integer.parseInt(year);

        // Map quarter to start & end month (Indian Financial Year)
        int startMonth, endMonth;
        switch (quarter) {
            case 1: // Q1 = Apr - Jun
                startMonth = 4;
                endMonth = 6;
                break;
            case 2: // Q2 = Jul - Sep
                startMonth = 7;
                endMonth = 9;
                break;
            case 3: // Q3 = Oct - Dec
                startMonth = 10;
                endMonth = 12;
                break;
            case 4: // Q4 = Jan - Mar (belongs to next calendar year)
                startMonth = 1;
                endMonth = 3;
                yr = yr + 1; // shift to next calendar year
                break;
            default:
                throw new IllegalArgumentException("Quarter must be between 1 and 4");
        }

        // Fetch from DB
        List<KraKpi> krakpiList = kraKpiRepository.findByEmployeeAndQuarter(empId, yr, startMonth, endMonth);
        List<KraKpiResponseDto> kraKpiData = kraKpiResponseUtils(krakpiList);

        return Map.of("krakpi", kraKpiData);
    }

    @Override
    public Map<String, List<KraKpiResponseDto>> listOfKraKpisByMonthsWise(String empId, String year, int quarter,
            int month) {
        int yr = Integer.parseInt(year);

        // Map quarter to start & end month (Indian Financial Year)
        int startMonth, endMonth;
        switch (quarter) {
            case 1: // Q1 = Apr - Jun
                startMonth = 4;
                endMonth = 6;
                break;
            case 2: // Q2 = Jul - Sep
                startMonth = 7;
                endMonth = 9;
                break;
            case 3: // Q3 = Oct - Dec
                startMonth = 10;
                endMonth = 12;
                break;
            case 4: // Q4 = Jan - Mar (belongs to next calendar year)
                startMonth = 1;
                endMonth = 3;
                yr = yr + 1; // shift to next calendar year
                break;
            default:
                throw new IllegalArgumentException("Quarter must be between 1 and 4");
        }

        // Fetch from DB
        List<KraKpi> krakpiList = kraKpiRepository.findByEmployeeAndQuarterAndMonth(empId, yr, startMonth, endMonth,
                month);
        List<KraKpiResponseDto> kraKpiData = kraKpiResponseUtils(krakpiList);

        return Map.of("krakpi", kraKpiData);
    }

    private List<KraKpiResponseDto> kraKpiResponseUtils(List<KraKpi> krakpiList) {
        return krakpiList.stream().map(kraKpi -> {
            KraKpiResponseDto kraKpiResponse = entityDtoConversion.entityToDtoConversion(kraKpi,
                    KraKpiResponseDto.class);
            Set<KraResponseDto1> kraList = kraKpi.getKra().stream().map(kra -> {
                KraResponseDto1 kraResponse = entityDtoConversion.entityToDtoConversion(kra, KraResponseDto1.class);
                Set<KpiResponseDto> kpiResponse = kra.getKpi().stream()
                        .map(kpi -> entityDtoConversion.entityToDtoConversion(kpi, KpiResponseDto.class))
                        .collect(Collectors.toSet());
                kraResponse.setKpi(kpiResponse);
                return kraResponse;
            }).collect(Collectors.toSet());
            kraKpiResponse.setKra(kraList);
            EmployeeInfo employeeInfo = entityDtoConversion.entityToDtoConversion(kraKpi.getEmployeeInformation(),
                    EmployeeInfo.class);
            employeeInfo.setDepartment(kraKpi.getEmployeeInformation().getDepartment().getName());
            kraKpiResponse.setEmployee(employeeInfo);
            return kraKpiResponse;
        }).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public Map<String, String> assignKrasToEmployees(String employeeId, Set<Long> kraIds) {
        Map<String, String> response = new HashMap<>();

        // Step 1: Check if employee exists
        EmployeeInformation emp = employeeInformationRepository.findById(employeeId)
                .orElse(null);
        if (emp == null) {
            response.put("status", "failure");
            response.put("message", "Employee not found");
            return response;
        }

        // Step 2: Check if KRAs already assigned in the current quarter
        Map<String, Boolean> existsMap = existsByEmployee(employeeId);
        if (existsMap.getOrDefault("status", false)) {
            response.put("status", "failure");
            response.put("message",
                    "KRAs already assigned for this employee in the current quarter. Please try after 3 months.");
            return response;
        }

        // Step 3: Create KraKpi record
        KraKpi kraKpi = new KraKpi();
        kraKpi.setEmployeeInformation(emp);
        kraKpi = kraKpiRepository.save(kraKpi); // auto-generated ID

        // Step 4: Fetch and assign KRAs
        Set<KRA> kraSet = new HashSet<>(kraRepository.findAllById(kraIds));
        kraKpi.setKra(kraSet);
        kraKpiRepository.save(kraKpi);

        // Step 5: Create bridge entries
        for (KRA kra : kraSet) {
            KraKpiBridge bridge = new KraKpiBridge();
            bridge.setKra(kra);
            bridge.setKraKpi(kraKpi);
            kraKpiBridgeRepository.save(bridge);
        }

        // Step 6: Return success response
        response.put("status", "success");
        response.put("message", "Assigned " + kraIds.size() + " KRA(s) to employee "
                + emp.getEmpId() + " (" + emp.getName() + ")");
        return response;
    }

    @Override
    public List<KRA> getAllKraWithKpi() {
        return kraRepository.findAll();
    }
@Override
public Map<String, Map<String, Integer>> getCompletedPendingByDepartmentYearWise(int year) {
    List<EmployeeInformation> employees = employeeInformationRepository.findAll();

    // Group employees by department
    Map<String, List<EmployeeInformation>> groupedByDepartment = employees.stream()
            .filter(e -> e.getDepartment() != null)
            .collect(Collectors.groupingBy(e -> e.getDepartment().getName()));

    Map<String, Map<String, Integer>> result = new HashMap<>();

    for (Map.Entry<String, List<EmployeeInformation>> entry : groupedByDepartment.entrySet()) {
        String department = entry.getKey();
        List<EmployeeInformation> deptEmployees = entry.getValue();

        int completed = 0;
        int pending = 0;

        for (EmployeeInformation employee : deptEmployees) {
            List<KraKpi> kraKpis = kraKpiRepository.findByEmployeeAndYear(employee.getEmpId(), year);

            for (KraKpi kraKpi : kraKpis) {
                boolean pmsInitiated = Boolean.TRUE.equals(kraKpi.getPmsInitiated());

                if (kraKpi.isManagerCompleted() && kraKpi.isSelfCompleted() && pmsInitiated) {
                    completed++;
                } else if (pmsInitiated) {
                    pending++;
                }
            }
        }

        Map<String, Integer> countMap = new HashMap<>();
        countMap.put("completed", completed);
        countMap.put("pending", pending);
        result.put(department, countMap);
    }

    return result;
}

@Override
public Map<String, Map<String, Integer>> getCompletedPendingByDepartmentMonthly(int year, int quarter, int month) {
    int startMonth, endMonth;
    switch (quarter) {
        case 1: startMonth = 4; endMonth = 6; break;
        case 2: startMonth = 7; endMonth = 9; break;
        case 3: startMonth = 10; endMonth = 12; break;
        case 4: startMonth = 1; endMonth = 3; year++; break;
        default: throw new IllegalArgumentException("Quarter must be between 1 and 4");
    }

    List<EmployeeInformation> employees = employeeInformationRepository.findAll();

    Map<String, List<EmployeeInformation>> groupedByDepartment = employees.stream()
            .filter(e -> e.getDepartment() != null)
            .collect(Collectors.groupingBy(e -> e.getDepartment().getName()));

    Map<String, Map<String, Integer>> result = new HashMap<>();

    for (Map.Entry<String, List<EmployeeInformation>> entry : groupedByDepartment.entrySet()) {
        String department = entry.getKey();
        List<EmployeeInformation> deptEmployees = entry.getValue();

        int completed = 0;
        int pending = 0;

        for (EmployeeInformation employee : deptEmployees) {
            List<KraKpi> kraKpis = kraKpiRepository.findByEmployeeAndQuarterAndMonth(
                    employee.getEmpId(), year, startMonth, endMonth, month);

            for (KraKpi kraKpi : kraKpis) {
                boolean pmsInitiated = Boolean.TRUE.equals(kraKpi.getPmsInitiated());
                if (kraKpi.isManagerCompleted() && kraKpi.isSelfCompleted() && pmsInitiated) {
                    completed++;
                } else if (pmsInitiated) {
                    pending++;
                }
            }
        }

        Map<String, Integer> countMap = new HashMap<>();
        countMap.put("completed", completed);
        countMap.put("pending", pending);
        result.put(department, countMap);
    }

    return result;
}


@Override
public Map<String, Map<String, Integer>> getCompletedPendingByDepartmentQuarterWise(int year, int quarter) {
    int startMonth, endMonth;
    switch (quarter) {
        case 1: startMonth = 4; endMonth = 6; break;   // Apr-Jun
        case 2: startMonth = 7; endMonth = 9; break;   // Jul-Sep
        case 3: startMonth = 10; endMonth = 12; break; // Oct-Dec
        case 4: startMonth = 1; endMonth = 3; year++; break; // Jan-Mar next year
        default: throw new IllegalArgumentException("Quarter must be between 1 and 4");
    }

    List<EmployeeInformation> employees = employeeInformationRepository.findAll();

    Map<String, List<EmployeeInformation>> groupedByDepartment = employees.stream()
            .filter(e -> e.getDepartment() != null)
            .collect(Collectors.groupingBy(e -> e.getDepartment().getName()));

    Map<String, Map<String, Integer>> result = new HashMap<>();

    for (Map.Entry<String, List<EmployeeInformation>> entry : groupedByDepartment.entrySet()) {
        String department = entry.getKey();
        List<EmployeeInformation> deptEmployees = entry.getValue();

        int completed = 0;
        int pending = 0;

        for (EmployeeInformation employee : deptEmployees) {
            List<KraKpi> kraKpis = kraKpiRepository.findByEmployeeAndQuarter(
                    employee.getEmpId(), year, startMonth, endMonth);

            for (KraKpi kraKpi : kraKpis) {
                boolean pmsInitiated = Boolean.TRUE.equals(kraKpi.getPmsInitiated());
                if (kraKpi.isManagerCompleted() && kraKpi.isSelfCompleted() && pmsInitiated) {
                    completed++;
                } else if (pmsInitiated) {
                    pending++;
                }
            }
        }

        Map<String, Integer> countMap = new HashMap<>();
        countMap.put("completed", completed);
        countMap.put("pending", pending);
        result.put(department, countMap);
    }

    return result;
}


}

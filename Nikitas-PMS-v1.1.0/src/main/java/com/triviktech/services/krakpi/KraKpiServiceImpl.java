package com.triviktech.services.krakpi;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.kpi.KPI;
import com.triviktech.entities.kra.KRA;
import com.triviktech.entities.krakpi.KraKpi;
import com.triviktech.entities.manager.Manager;
import com.triviktech.exception.employee.EmployeeNotFoundException;
import com.triviktech.exception.krakpi.KraKpiNotFoundException;
import com.triviktech.exception.manager.ManagerNotFoundException;
import com.triviktech.payloads.request.kpi.KpiRequestDto;
import com.triviktech.payloads.request.kra.KraRequestDto;
import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeResponseDto;
import com.triviktech.payloads.response.kpi.KpiResponseDto;
import com.triviktech.payloads.response.kra.KraResponseDto1;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.kpi.KPIRepository;
import com.triviktech.repositories.kra.KRARepository;
import com.triviktech.repositories.krakpi.KraKpiRepository;
import com.triviktech.repositories.manager.ManagerRepository;
import com.triviktech.utilities.email.EmailService;
import com.triviktech.utilities.email.Message;
import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KraKpiServiceImpl implements KraKpiService {

    private final EmployeeInformationRepository employeeInformationRepository;
    private final KraKpiRepository kraKpiRepository;
    private final KRARepository kraRepository;
    private final KPIRepository kpiRepository;
    private final EntityDtoConversion entityDtoConversion;
    private final EmailService emailService;

    public KraKpiServiceImpl(EmployeeInformationRepository employeeInformationRepository,
            KraKpiRepository kraKpiRepository, KRARepository kraRepository, KPIRepository kpiRepository,
            EntityDtoConversion entityDtoConversion, EmailService emailService) {
        this.employeeInformationRepository = employeeInformationRepository;
        this.kraKpiRepository = kraKpiRepository;
        this.kraRepository = kraRepository;
        this.kpiRepository = kpiRepository;
        this.entityDtoConversion = entityDtoConversion;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public Map<String, String> registerKraKpi(KraKpiRequestDto kraKpiRequestDto) {
        Map<String, String> response = new HashMap<>();

        // Convert DTO to Entity
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

        // Save KraKpi first to get a valid ID
        kraKpi = kraKpiRepository.saveAndFlush(kraKpi);

        Set<KRA> kras = new HashSet<>();
        for (KraRequestDto dto : kraKpiRequestDto.getKra()) {
            KRA kra = new KRA();
            kra.setKraKpi(kraKpi); // Set foreign key relation
            kra.setKraName(dto.getKraName());
            kra.setWeightage(dto.getWeightage());

            // Save KRA first before adding KPIs
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
                // Save KPI separately to prevent ID issues
                kpi = kpiRepository.saveAndFlush(kpi);
                kpis.add(kpi);
            }

            kra.setKpi(kpis);
            kras.add(kra);
        }

<<<<<<< HEAD
        // ✅ Set KRA for KraKpi
=======
>>>>>>> c3381adc3c5e01e2fb2a25216ef404b3abfcc4b7
        kraKpi.setKra(kras);

        kraKpi = kraKpiRepository.saveAndFlush(kraKpi); // Final update

<<<<<<< HEAD
        try {
            EmployeeInformation emp = kraKpi.getEmployeeInformation();
=======
        // Unified git conflict resolution: Use robust manager/employee null-check and
        // send email
        try {
            EmployeeInformation emp = kraKpi.getEmployeeInformation();
            // For debug (can be removed in prod):
            // System.out.println("✅ Employee Info: " + emp.getEmpId() + ", " +
            // emp.getName());
>>>>>>> c3381adc3c5e01e2fb2a25216ef404b3abfcc4b7

            if (emp.getManager() == null) {
                throw new EmployeeNotFoundException("Manager not assigned to employee: " + emp.getEmpId());
            }

            Manager mgr = emp.getManager();
<<<<<<< HEAD
=======
            // For debug (can be removed in prod):
            // System.out.println("✅ Manager Info: " + mgr.getManagerId() + ", " +
            // mgr.getName());
>>>>>>> c3381adc3c5e01e2fb2a25216ef404b3abfcc4b7

            String sub = String.format(Message.KRA_KPI_SUBJECT_TO_MANAGER, emp.getName());
            String to = mgr.getEmailId();
            String message = String.format(
                    Message.KRA_KPI_MESSAGE_TO_MANAGER,
                    mgr.getName(),
                    emp.getName(),
                    emp.getEmpId());
            emailService.sendEmail(to, sub, message);

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

        KraKpi krakpi = kraKpiRepository.findByEmployeeInformation(employee)
                .orElseThrow(() -> new RuntimeException("Could not find"));
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
            return Map.of(
                    "status", "failure",
                    "message", "Employee not found");
        }

        EmployeeInformation employee = employeeById.get();
        Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);
        if (kraKpiOptional.isEmpty()) {
            return Map.of(
                    "status", "failure",
                    "message", "KRA/KPI not found for employee");
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

        return Map.of(
                "status", "success",
                "message", "Employee Review submitted successfully");
    }

    @Override
    public Map<String, Boolean> existsByEmployee(String employeeId) {
        Optional<EmployeeInformation> byId = employeeInformationRepository.findById(employeeId);
        if (byId.isEmpty()) {
            throw new EmployeeNotFoundException(employeeId);
        }
        EmployeeInformation employee = byId.get();
        boolean status = kraKpiRepository.existsByEmployeeInformation(employee);
        return Map.of("status", status);
    }
}

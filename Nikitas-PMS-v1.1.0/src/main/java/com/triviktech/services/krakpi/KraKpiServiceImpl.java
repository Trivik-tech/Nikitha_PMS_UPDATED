package com.triviktech.services.krakpi;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.kpi.KPI;
import com.triviktech.entities.kra.KRA;
import com.triviktech.entities.krakpi.KraKpi;
import com.triviktech.entities.manager.Manager;
import com.triviktech.exception.employee.EmployeeNotFoundException;
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
import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KraKpiServiceImpl implements KraKpiService{

    private final EmployeeInformationRepository employeeInformationRepository;
    private final KraKpiRepository kraKpiRepository;
    private final KRARepository kraRepository;
    private final KPIRepository kpiRepository;
    private final EntityDtoConversion entityDtoConversion;
    private final ManagerRepository managerRepository;

    public KraKpiServiceImpl(EmployeeInformationRepository employeeInformationRepository, KraKpiRepository kraKpiRepository, KRARepository kraRepository, KPIRepository kpiRepository, EntityDtoConversion entityDtoConversion, ManagerRepository managerRepository) {
        this.employeeInformationRepository = employeeInformationRepository;
        this.kraKpiRepository = kraKpiRepository;
        this.kraRepository = kraRepository;
        this.kpiRepository = kpiRepository;
        this.entityDtoConversion = entityDtoConversion;
        this.managerRepository = managerRepository;
    }

    @Override
    @Transactional
    public KraKpiResponseDto registerKraKpi(KraKpiRequestDto kraKpiRequestDto) {

        // Convert DTO to Entity
        KraKpi kraKpi = new KraKpi();

        kraKpi.setEmployeeInformation(employeeInformationRepository.findById(kraKpiRequestDto.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException(kraKpiRequestDto.getEmployeeId())));

        kraKpi.setRemark(kraKpiRequestDto.getRemark());
        kraKpi.setSelfCompleted(kraKpiRequestDto.isSelfCompleted());
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
            KRA kra =new KRA();
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
                if(kpiDto.getReview2()!=0){
                    average= (float) (kpiDto.getManagerScore() + kpiDto.getSelfScore() + kpiDto.getReview2()) /3;
                }else{
                    average= (float) (kpiDto.getManagerScore() + kpiDto.getSelfScore()) /2;
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

        kraKpi.setKra(kras);
        kraKpi = kraKpiRepository.saveAndFlush(kraKpi); // Final update

        // Convert Entity to DTO
        KraKpiResponseDto kraKpiResponseDto = entityDtoConversion.entityToDtoConversion(kraKpi, KraKpiResponseDto.class);
        kraKpiResponseDto.setEmployee(entityDtoConversion.entityToDtoConversion(kraKpi.getEmployeeInformation(), EmployeeInfo.class));

        Set<KraResponseDto1> kraList = kraKpi.getKra().stream().map(kra -> {
            KraResponseDto1 kraResponseDto1 = entityDtoConversion.entityToDtoConversion(kra, KraResponseDto1.class);

            Set<KpiResponseDto> kpis = kra.getKpi().stream()
                    .map(kpi -> entityDtoConversion.entityToDtoConversion(kpi, KpiResponseDto.class))
                    .collect(Collectors.toSet());
            kraResponseDto1.setKpi(kpis);

            return kraResponseDto1;
        }).collect(Collectors.toSet());

        kraKpiResponseDto.setKra(kraList);
        return kraKpiResponseDto;
    }

    @Override
    public KraKpiResponseDto kraKpiForEmployee(String employeeId) {

        EmployeeInformation employee = employeeInformationRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        KraKpi krakpi = kraKpiRepository.findByEmployeeInformation(employee).orElseThrow(()->new RuntimeException("Could not find"));
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
    public String managerReview(String managerId, KraKpiRequestDto data) {
//        Manager manager = managerRepository.findById(managerId)
//                .orElseThrow(() -> new ManagerNotFoundException(managerId));
//
////        EmployeeInformation employee = employeeInformationRepository.findByEmpIdAndManager(data.getEmployeeId(), manager)
////                .orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found"));
//
////        KraKpi kraKpi = kraKpiRepository.findByEmployeeInformationAndKraKpiId(employee, data.getKraKpiId())
////                .orElseThrow(() -> new RuntimeException("Couldn't find kraKpi data"));
//
//        // Update KraKpi level fields
//        kraKpi.setRemark(data.getRemark());
//        kraKpi.setSelfCompleted(data.isSelfCompleted());
//        kraKpi.setManagerCompleted(data.isManagerCompleted());
//        kraKpi.setDueDate(data.getDueDate());
//        kraKpi.setManagerReviewDate(data.getManagerReviewDate());
//        kraKpi.setSelfReviewDate(data.getSelfReviewDate());
//        kraKpi.setPmsInitiated(data.getPmsInitiated());
//        kraKpi.setReview2(data.isReview2());
//        kraKpi.setManagerApproval(data.getManagerApproval());
//
//        // Track existing KRAs for potential removal
//        Set<KRA> existingKras = kraKpi.getKra();
//        Set<KRA> updatedKras = new HashSet<>();
//
//        for (KraRequestDto kraDto : data.getKra()) {
//            // Try to find existing KRA, or create new if not found
//            KRA kra = existingKras.stream()
//                    .filter(existingKra -> existingKra.getKraId() == kraDto.getKraId())
//                    .findFirst()
//                    .orElse(new KRA());  // New KRA if not found
//
//            kra.setKraKpi(kraKpi);
//            kra.setKraName(kraDto.getKraName());
//            kra.setWeightage(kraDto.getWeightage());
//
//            // Update KPIs within this KRA
//            Set<KPI> existingKpis = kra.getKpi() != null ? kra.getKpi() : new HashSet<>();
//            Set<KPI> updatedKpis = new HashSet<>();
//
//            for (KpiRequestDto kpiDto : kraDto.getKpi()) {
//                // Try to find existing KPI or create new
//                KPI kpi = existingKpis.stream()
//                        .filter(existingKpi -> existingKpi.getKpiId() == kpiDto.getKpiId())
//                        .findFirst()
//                        .orElse(new KPI());  // New KPI if not found
//
//                kpi.setKra(kra);  // Set parent
//                kpi.setDescription(kpiDto.getDescription());
//                kpi.setWeightage(kpiDto.getWeightage());
//                kpi.setSelfScore(kpiDto.getSelfScore());
//                kpi.setManagerScore(kpiDto.getManagerScore());
//                kpi.setAverage((float) (kpiDto.getSelfScore() + kpiDto.getManagerScore()) / 2);
//                kpi.setReview2(kpiDto.getReview2());
//
//                updatedKpis.add(kpi);
//            }
//
//            // Remove KPIs that were not in the new request (to handle deletions)
//            existingKpis.removeIf(existingKpi ->
//                    updatedKpis.stream().noneMatch(updatedKpi -> updatedKpi.getKpiId() == existingKpi.getKpiId())
//            );
//
//            // Combine updated and remaining existing KPIs
//            existingKpis.addAll(updatedKpis);
//            kra.setKpi(existingKpis);
//
//            updatedKras.add(kra);
//        }
//
//        // Remove KRAs that are no longer in the request (handle deletion if needed)
//        existingKras.removeIf(existingKra ->
//                updatedKras.stream().noneMatch(updatedKra -> updatedKra.getKraId() == existingKra.getKraId())
//        );
//
//        // Combine updated and remaining existing KRAs
//        existingKras.addAll(updatedKras);
//        kraKpi.setKra(existingKras);
//
//        // Save the entire structure (KraKpi -> KRA -> KPI)
//        kraKpiRepository.saveAndFlush(kraKpi);

        return "KRA/KPI review updated successfully";
    }




}


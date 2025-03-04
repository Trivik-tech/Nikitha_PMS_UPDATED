package com.triviktech.services.krakpi;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.kpi.KPI;
import com.triviktech.entities.kra.KRA;
import com.triviktech.entities.krakpi.KraKpi;
import com.triviktech.exception.employee.EmployeeNotFoundException;
import com.triviktech.payloads.request.kpi.KpiRequestDto;
import com.triviktech.payloads.request.kra.KraRequestDto;
import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.employee.EmployeeResponseDto;
import com.triviktech.payloads.response.kpi.KpiResponseDto;
import com.triviktech.payloads.response.kra.KraResponseDto1;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.kpi.KPIRepository;
import com.triviktech.repositories.kra.KRARepository;
import com.triviktech.repositories.krakpi.KraKpiRepository;
import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KraKpiServiceImpl implements KraKpiService{

    private final EmployeeInformationRepository employeeInformationRepository;
    private final KraKpiRepository kraKpiRepository;
    private final KRARepository kraRepository;
    private final KPIRepository kpiRepository;
    private final EntityDtoConversion entityDtoConversion;

    public KraKpiServiceImpl(EmployeeInformationRepository employeeInformationRepository, KraKpiRepository kraKpiRepository, KRARepository kraRepository, KPIRepository kpiRepository, EntityDtoConversion entityDtoConversion) {
        this.employeeInformationRepository = employeeInformationRepository;
        this.kraKpiRepository = kraKpiRepository;
        this.kraRepository = kraRepository;
        this.kpiRepository = kpiRepository;
        this.entityDtoConversion = entityDtoConversion;
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
        kraKpi.setManagerCompleted(kraKpi.isManagerCompleted());
        kraKpi.setDueDate(kraKpiRequestDto.getDueDate());
        kraKpi.setManagerReviewDate(kraKpiRequestDto.getManagerReviewDate());
        kraKpi.setSelfReviewDate(kraKpiRequestDto.getSelfReviewDate());
        kraKpi.setPmsInitiated(kraKpiRequestDto.getPmsInitiated());
        kraKpi.setReview2(kraKpiRequestDto.isReview2());

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
        kraKpiResponseDto.setEmployee(entityDtoConversion.entityToDtoConversion(kraKpi.getEmployeeInformation(), EmployeeResponseDto.class));

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

        KraKpi krakpi = kraKpiRepository.findByEmployeeInformation(employee);
        KraKpiResponseDto response = entityDtoConversion.entityToDtoConversion(krakpi, KraKpiResponseDto.class);
        response.setEmployee(entityDtoConversion.entityToDtoConversion(employee, EmployeeResponseDto.class));
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


}


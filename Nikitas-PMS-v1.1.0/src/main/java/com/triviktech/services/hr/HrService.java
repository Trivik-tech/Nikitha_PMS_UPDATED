package com.triviktech.services.hr;

import com.triviktech.entities.hr.HR;
import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employeeslist.EmployeesList;
import com.triviktech.payloads.response.global.Response;
import com.triviktech.payloads.response.hr.HrResponseDto;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public interface HrService {

    HrResponseDto registerHr(HrRequestDto hrRequestDto);

    HrResponseDto findHrById(String hrId);

    List<Object> uploadEmployeesData(MultipartFile file) throws IOException;

    List<EmployeeInfo> getAllEmployees();

    EmployeeInfo getEmployeeById(String employeeId);

    Integer totalEmployees();

    List<EmployeeInfo> searchEmployee(String search);

    Map<String,String> deleteEmployee(String employeeId);

    EmployeeInfo updateEmployee(String empId, Employee employee);


}

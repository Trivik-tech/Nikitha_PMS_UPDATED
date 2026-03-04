package com.triviktech.config;

import com.triviktech.entities.department.Department;
import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.hr.HR;
import com.triviktech.entities.manager.Manager;
import com.triviktech.repositories.department.DepartmentRepository;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.hr.HRRepository;
import com.triviktech.repositories.manager.ManagerRepository;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class DummyDataSeeder implements CommandLineRunner {

    private final HRRepository hrRepository;
    private final ManagerRepository managerRepository;
    private final EmployeeInformationRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public DummyDataSeeder(HRRepository hrRepository, ManagerRepository managerRepository,
            EmployeeInformationRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.hrRepository = hrRepository;
        this.managerRepository = managerRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if dummy HR already exists
        if (hrRepository.findById("HR999").isPresent()) {
            System.out.println("Dummy data already exists. Skipping seeding.");
            return;
        }

        System.out.println("Seeding dummy data (HR999, MGR999, EMP999)...");

        // 1. Create or get Department
        Department dept = departmentRepository.findByName("Demo Engineering");
        if (dept == null) {
            dept = new Department();
            dept.setName("Demo Engineering");
            dept = departmentRepository.save(dept);
        }

        org.springframework.security.crypto.password.PasswordEncoder passwordEncoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("trivik");

        // 2. Create HR
        HR hr = new HR();
        hr.setHrId("HR999");
        hr.setName("John HR");
        hr.setEmailId("hr999@triviktech.com");
        hr.setOfficialEmailId("hr999@triviktech.com");
        hr.setMobileNumber("9999999991");
        hr.setRole("HR");
        hr.setPassword(encodedPassword);
        hr.setDepartment(dept);
        hr.setDateOfJoining(new Date());
        hr.setDob(new Date(90, 0, 1)); // Jan 1, 1990
        hr.setBranch("Head Office");
        hr = hrRepository.save(hr);

        // 3. Create Manager
        Manager manager = new Manager();
        manager.setManagerId("MGR999");
        manager.setName("Jane Manager");
        manager.setEmailId("mgr999@triviktech.com");
        manager.setOfficialEmailId("mgr999@triviktech.com");
        manager.setMobileNumber("9999999992");
        manager.setRole("MANAGER");
        manager.setPassword(encodedPassword);
        manager.setDepartment(dept);
        manager.setDateOfJoining(new Date());
        manager.setDob(new Date(92, 5, 15)); // Jun 15, 1992
        manager.setBranch("Head Office");
        manager.sethR(hr);
        manager.setReportingManager(hr.getHrId());
        manager = managerRepository.save(manager);

        // 4. Create Employee
        EmployeeInformation emp = new EmployeeInformation();
        emp.setEmpId("EMP999");
        emp.setName("Bob Employee");
        emp.setEmailId("emp999@triviktech.com");
        emp.setOfficialEmailId("emp999@triviktech.com");
        emp.setMobileNumber("9999999993");
        emp.setRole("EMPLOYEE");
        emp.setPassword(encodedPassword);
        emp.setDepartment(dept);
        emp.setDateOfJoining(new Date());
        emp.setDob(new Date(95, 10, 20)); // Nov 20, 1995
        emp.setBranch("Head Office");
        emp.setManager(manager);
        emp.sethR(hr);
        employeeRepository.save(emp);

        System.out.println("Dummy data seeding complete!");
    }
}

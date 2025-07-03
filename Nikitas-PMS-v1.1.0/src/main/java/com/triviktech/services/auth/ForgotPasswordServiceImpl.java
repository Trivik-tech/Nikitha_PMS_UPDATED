package com.triviktech.services.auth;

import java.security.SecureRandom;
import java.util.Optional;

import com.triviktech.entities.hr.HR;
import com.triviktech.entities.manager.Manager;
import com.triviktech.repositories.hr.HRRepository;
import com.triviktech.repositories.manager.ManagerRepository;
import com.triviktech.utilities.email.EmailService;
import com.triviktech.utilities.email.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.repositories.employee.EmployeeInformationRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final HttpSession session;
    private final CacheManager cacheManager;
    private final EmployeeInformationRepository employeeInformationRepository;
    private final EmailService emailService;
    private final HRRepository hrRepository;
    private final ManagerRepository managerRepository;

    public ForgotPasswordServiceImpl(
            HttpSession session,
            CacheManager cacheManager,
            EmployeeInformationRepository employeeInformationRepository, EmailService emailService, EmailService emailService1, HRRepository hrRepository, ManagerRepository managerRepository) {
        this.session = session;
        this.cacheManager = cacheManager;
        this.employeeInformationRepository = employeeInformationRepository;


        this.emailService = emailService1;
        this.hrRepository = hrRepository;
        this.managerRepository = managerRepository;
    }

    private static final int OTP_LENGTH = 6;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Cacheable(value = "otpCache", key = "#email")
    @Override
    public String genrateOtp(String email) {
        boolean isRegistered =
        employeeInformationRepository.existsByEmailId(email) || hrRepository.existsByEmailId(email) || managerRepository.existsByEmailId(email);
        if (!isRegistered) {
            throw new IllegalArgumentException("Email not Registered");
        } else {
            session.setAttribute("email", email);
            StringBuilder otp = new StringBuilder(OTP_LENGTH);
            for (int i = 0; i < OTP_LENGTH; i++) {
                otp.append(secureRandom.nextInt(10));
            }
            Cache cache = cacheManager.getCache("otpCache");
            if (cache != null) {
                cache.put(email, otp.toString());
            }


            try {

                String subject= String.format(Message.OTP_SUBJECT,"Reset Password");
                String message=String.format(Message.OTP_MESSAGE,"User",otp,10);

                emailService.sendEmail(email, subject, message);
                System.out.println("OTP Email sent to: " + email);

            } catch (Exception e) {
                throw new RuntimeException("Failed to send OTP", e);
            }


            return otp.toString();
        }

    }

    @Override
    public boolean validateotp(String inputotp) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            throw new IllegalStateException("Session expired");
        }

        Cache cache = cacheManager.getCache("otpCache");
        if (cache == null) {
            throw new IllegalStateException("Otp cache not found");
        }

        Cache.ValueWrapper valueWrapper = cache.get(email);
        if (valueWrapper == null) {
            return false;
        }


        String cachedOtp = (String) valueWrapper.get();
        if (cachedOtp == null) {
            return false;
        }

        boolean match = inputotp.equals(cachedOtp);

        return match;
    }

    @Override
    public boolean resetPassword(String otp, String newPassword) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return false;
        }

        Cache cache = cacheManager.getCache("otpCache");
        if (cache == null) {
            return false;
        }

        Cache.ValueWrapper wrapper = cache.get(email);
        if (wrapper == null || !otp.equals(wrapper.get())) {
            return false;
        }

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(10));

        // Check and update in Employee repository
        Optional<EmployeeInformation> empOptional = employeeInformationRepository.findByEmailId(email);
        if (empOptional.isPresent()) {
            empOptional.get().setPassword(hashedPassword);
            employeeInformationRepository.save(empOptional.get());
            String subject=String.format(Message.PASSWORD_RESET_SUCCESS_SUBJECT);
            String message=String.format(Message.PASSWORD_RESET_SUCCESS_MESSAGE,"User");

            emailService.sendEmail(email,subject,message);

            try{

            }catch (Exception e){
                e.printStackTrace();
            }
            cleanupAfterReset(email);
            return true;
        }

        // Check and update in HR repository
        Optional<HR> hrOptional = hrRepository.findByEmailId(email);
        if (hrOptional.isPresent()) {
            hrOptional.get().setPassword(hashedPassword);
            hrRepository.save(hrOptional.get());
            cleanupAfterReset(email);
            return true;
        }

        // Check and update in Manager repository
        Optional<Manager> managerOptional = managerRepository.findByEmailId(email);
        if (managerOptional.isPresent()) {
            managerOptional.get().setPassword(hashedPassword);
            managerRepository.save(managerOptional.get());
            cleanupAfterReset(email);
            return true;
        }

        // Not found in any repository
        return false;
    }


    private void cleanupAfterReset(String email) {
        Cache cache = cacheManager.getCache("otpCache");
        if (cache != null) {
            cache.evict(email);
        }
        session.removeAttribute("email");
    }
}

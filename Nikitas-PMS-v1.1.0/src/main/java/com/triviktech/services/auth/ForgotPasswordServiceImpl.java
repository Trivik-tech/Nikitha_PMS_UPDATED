package com.triviktech.services.auth;

import java.security.SecureRandom;
import java.util.Optional;

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

    public ForgotPasswordServiceImpl(
            HttpSession session,
            CacheManager cacheManager,
            EmployeeInformationRepository employeeInformationRepository) {
        this.session = session;
        this.cacheManager = cacheManager;
        this.employeeInformationRepository = employeeInformationRepository;
    }

    private static final int OTP_LENGTH = 6;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override

    @Cacheable(value = "otpCache", key = "#email")
    public String genrateOtp(String email) {
        Optional<EmployeeInformation> user = employeeInformationRepository.findByOfficialEmailId(email);
        if (!user.isPresent()) {
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
            // try {
            // gmailOAuth2Service.sendOtpMail(email, otp.toString());
            // } catch (Exception e) {
            // throw new RuntimeException("Failed to send OTP", e);
            // }
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

        Optional<EmployeeInformation> userOptional = employeeInformationRepository.findByOfficialEmailId(email);
        if (!userOptional.isPresent()) {
            return false;
        }

        EmployeeInformation user = userOptional.get();
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(10));
        user.setPassword(hashedPassword);
        employeeInformationRepository.save(user);

        // Clear the OTP and session
        cache.evict(email);
        session.removeAttribute("email");
        return true;
    }

}

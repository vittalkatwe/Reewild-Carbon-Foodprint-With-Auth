package org.example.authenticationwithverification.service;

import org.example.authenticationwithverification.model.AppUser;
import org.example.authenticationwithverification.repository.UserRepository;
import org.example.authenticationwithverification.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(String email, String password) {
        if (userRepo.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists!");
        }

        String otp = mailService.sendOtp(email);

        AppUser user = AppUser.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .otp(otp)
                .otpExpiry(LocalDateTime.now().plusMinutes(5)) // 5 mins expiry
                .enabled(false)
                .createdAt(LocalDateTime.now())
                .build();

        userRepo.save(user);
        return "OTP sent to email! Valid for 5 minutes.";
    }

    public String verifyOtp(String email, String otp) {
        AppUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired! Please request a new one.");
        }

        if (user.getOtp().equals(otp)) {
            user.setEnabled(true);
            user.setOtp(null);
            user.setOtpExpiry(null);
            user.setVerifiedAt(LocalDateTime.now());
            userRepo.save(user);
            return "Verification successful!";
        }
        throw new RuntimeException("Invalid OTP!");
    }

    public String resendOtp(String email) {
        AppUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        String newOtp = mailService.sendOtp(email);

        user.setOtp(newOtp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepo.save(user);

        return "New OTP sent to email! Valid for 5 minutes.";
    }

    public String login(String email, String password) {
        AppUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!user.isEnabled()) {
            throw new RuntimeException("User not verified. Please verify OTP first.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials!");
        }
        user.setLastLoginAt(LocalDateTime.now());
        userRepo.save(user);

        return jwtUtils.generateToken(email);
    }
}
package org.example.authenticationwithverification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class AppUser {

    @Id
    private String id;

    private String email;
    private String password;

    private String otp;
    private LocalDateTime otpExpiry;
    private LocalDateTime createdAt;
    private LocalDateTime verifiedAt;
    private LocalDateTime lastLoginAt;

    private boolean enabled = false;

    public AppUser() {}

    public AppUser(String id, String email, String password, String otp, LocalDateTime otpExpiry, LocalDateTime createdAt, LocalDateTime verifiedAt, LocalDateTime lastLoginAt, boolean enabled) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.otp = otp;
        this.otpExpiry = otpExpiry;
        this.createdAt = createdAt;
        this.verifiedAt = verifiedAt;
        this.lastLoginAt = lastLoginAt;
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

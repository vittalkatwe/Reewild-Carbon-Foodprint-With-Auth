package org.example.authenticationwithverification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}

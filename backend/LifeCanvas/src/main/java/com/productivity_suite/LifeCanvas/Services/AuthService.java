package com.productivity_suite.LifeCanvas.Services;

import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import com.productivity_suite.LifeCanvas.Repository.UserRepository;
import com.productivity_suite.LifeCanvas.Requests.SignupRequestDTO;
import com.productivity_suite.LifeCanvas.Responses.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService emailService;

    public SignupResponse createProfile(SignupRequestDTO request) {
        UserEntity newUser = convertToUserEntity(request);
        if(!userRepository.existsByEmail(request.getEmail())){
            newUser = userRepository.save(newUser);
            emailService.sendWelcomeEmail(request.getEmail(), request.getName());
            return convertToAuthResponse(newUser);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email Already Exists");
    }

    public SignupResponse getProfile(String email) {
        UserEntity existUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not fount" + email));

        return convertToAuthResponse(existUser);
    }

    public void sendRequestOtp(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found "+email));
        // Generate a 6 digit OTP
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

        // Calculate the expiry time (current time + 5 mins)
        long expiryTime = System.currentTimeMillis()+(5 * 50 * 1000);

        // Update Profile Entity
        userEntity.setResetOtp(otp);
        userEntity.setResetOtpExpiredAt(expiryTime);

        // save into the db
        userRepository.save(userEntity);

        try{
            // Send RESET OTP
            emailService.sendResetOtpEmail(email, otp);

        } catch (RuntimeException e) {
            throw new RuntimeException("Unable to send email");
        }

    }

    public void resetPassword(String email, String otp, String newPassword) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Email not found"));

        if(existingUser.getResetOtp() == null || !existingUser.getResetOtp().equals(otp)){
            throw new RuntimeException("Invalid OTP");
        }

        if(existingUser.getResetOtpExpiredAt() < System.currentTimeMillis()){
            throw new RuntimeException("OTP Expired");
        }

        existingUser.setPassword(passwordEncoder.encode(newPassword));
        existingUser.setResetOtp(null);
        existingUser.setResetOtpExpiredAt(0);

        userRepository.save(existingUser);
    }

    public Boolean isAccountVerified(String email){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        if(user.getIsAccountActivated() == true){
            return true;
        }
        return false;
    }

    public void sendOtp(String email) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found - "+email));

        if(existingUser.getIsAccountActivated() != null && existingUser.getIsAccountActivated()){
            return;
        }

        // Generate a 6 digit OTP
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

        // Calculate the expiry time (current time + 24 hours)
        long expiryTime = System.currentTimeMillis()+(24 * 60 * 60 * 1000);

        // Update DB
        existingUser.setVerifyOtp(otp);
        existingUser.setVerifyOtpExpiredAt(expiryTime);
        userRepository.save(existingUser);

        // Send Email
        emailService.sendVerifyOtpEmail(email, otp);
    }

    public void verifyOtp(String email, String otp) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found - "+email));
        if(existingUser.getVerifyOtp() == null || !existingUser.getVerifyOtp().equals(otp)){
            throw new RuntimeException("Invalid OTP");
        }

        if(existingUser.getVerifyOtpExpiredAt() < System.currentTimeMillis()){
            throw new RuntimeException("OTP is Expired");
        }

        existingUser.setIsAccountActivated(true);
        existingUser.setVerifyOtp(null);
        existingUser.setVerifyOtpExpiredAt(0L);

        userRepository.save(existingUser);
    }

    private SignupResponse convertToAuthResponse(UserEntity newUser) {
        return SignupResponse.builder()
                .name(newUser.getName())
                .email(newUser.getEmail())
                .userId(newUser.getUserId())
                .isAccountVerified(newUser.getIsAccountActivated())
                .build();
    }

    private UserEntity convertToUserEntity(SignupRequestDTO request) {
        return UserEntity.builder()
                .email(request.getEmail())
                .userId(UUID.randomUUID().toString())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountActivated(false)
                .resetOtpExpiredAt(0L)
                .verifyOtp(null)
                .verifyOtpExpiredAt(0)
                .resetOtp(null)
                .build();
    }

    // OTP Generator
    private String otpGenerator() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }


}

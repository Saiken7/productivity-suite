package com.productivity_suite.LifeCanvas.Controller;

import com.productivity_suite.LifeCanvas.Repository.UserRepository;
import com.productivity_suite.LifeCanvas.Requests.LoginRequestDTO;
import com.productivity_suite.LifeCanvas.Requests.ResetPasswordRequest;
import com.productivity_suite.LifeCanvas.Requests.SignupRequestDTO;
import com.productivity_suite.LifeCanvas.Responses.AuthResponse;
import com.productivity_suite.LifeCanvas.Responses.SignupResponse;
import com.productivity_suite.LifeCanvas.Services.AppUserDetailsService;
import com.productivity_suite.LifeCanvas.Services.AuthService;
import com.productivity_suite.LifeCanvas.Utils.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AppUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/api/auth/v1/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDTO request) {
        SignupResponse response = authService.createProfile(request);

        // TODO
        //1. JWT Token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(response.getEmail());
        final String jwtToken = jwtUtil.generateToken(userDetails);

        //2. Generate and Save token in Cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/")
                .buildAndExpand(response.getUserId())
                .toUri();

        // Update it Afterwords --> Updated
        return ResponseEntity.created(location)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AuthResponse(response.getEmail(), jwtToken));
    }

    @PostMapping("/api/auth/v1/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            authenticate(request.getEmail(), request.getPassword());
            // TODO
            //1. JWT Token
            final UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());
            final String jwtToken = jwtUtil.generateToken(user);

            //2. Generate and Save Token in Cookie
            ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .maxAge(Duration.ofDays(1))
                    .path("/")
                    .build();
            // Update it Afterwords --> Updated
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new AuthResponse(request.getEmail(), jwtToken));

        } catch (BadCredentialsException e) {
            Map<String, Object> mp = new HashMap<>();
            mp.put("error", true);
            mp.put("message", "Email or Password is Incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mp);
        } catch (DisabledException e) {
            Map<String, Object> mp = new HashMap<>();
            mp.put("error", true);
            mp.put("message", "Account is Disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mp);
        } catch (Exception e) {
            Map<String, Object> mp = new HashMap<>();
            mp.put("error", true);
            mp.put("message", "Authentication Failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mp);
        }
    }

    @GetMapping("/api/auth/v1/profile")
    public SignupResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        return authService.getProfile(email);
    }

    @GetMapping("/api/v1/auth/v1/is-authenticated")
    public Boolean isAuthenticated(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        Boolean answer = authService.isAccountVerified(email);
        return answer;

    }

    @PostMapping("/api/auth/v1/send-reset-otp")
    public ResponseEntity<String> sendResetOtp(@RequestParam String email) {
        if (email != null) {
            authService.sendRequestOtp(email);
            return ResponseEntity.ok("OTP has been sent to your email");
        }
        return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/api/auth/v1/reset-password")
    public void resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/api/auth/v1/send-otp")
    public ResponseEntity<String> sendVerifyOtp(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        if (email != null) {
            authService.sendOtp(email);
            return ResponseEntity.ok("OTP has been send to your Email");
        }
        return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/auth/v1/verify-email")
    public void verifyEmail(@RequestBody Map<String, Object> request,
                            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        if (request.get("otp").toString() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing Details");
        }

        try {
            authService.verifyOtp(email, request.get("otp").toString());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/api/auth/v1/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(0))
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged Out Successfully");
    }

    private void authenticate(@NotBlank(message = "Email should not be Blank") @Email(message = "Enter Valid Email")
                              String email, @NotBlank(message = "Password Field must not be blank")
                              @Size(min = 6, message = "Password must be of atleast of 6 in length") String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

}

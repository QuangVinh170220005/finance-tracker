package com.vinh.financetracker.controller;

import com.vinh.financetracker.common.ApiResponse;
import com.vinh.financetracker.dto.request.*;
import com.vinh.financetracker.dto.response.*;
import com.vinh.financetracker.security.JwtService;
import com.vinh.financetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid
                                                              @RequestBody UserRegistrationRequest request){
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        // Nếu pass qua dòng trên, nghĩa là login đúng -> Generate Token
        var userDetails = userDetailsService.loadUserByUsername(request.email());
        var token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(ApiResponse.success(new TokenResponse(token, "refresh-token-placeholder", "Bearer")));
    }

    @RestController
    @RequestMapping("/api")
    public class HealthCheckController {

        @GetMapping("/health")
        public ResponseEntity<?> checkHealth() {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "UP");
            response.put("message", "Hệ thống đang hoạt động bình thường!");
            response.put("user_authenticated", "Bạn đã truy cập thành công bằng Token!");

            return ResponseEntity.ok(response);
        }
    }
}

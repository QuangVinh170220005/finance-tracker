package com.vinh.financetracker.service;

import com.vinh.financetracker.domain.entity.AuthProvider;
import com.vinh.financetracker.domain.entity.User;
import com.vinh.financetracker.domain.repository.UserRepository;
import com.vinh.financetracker.dto.request.UserRegistrationRequest;
import com.vinh.financetracker.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(UserRegistrationRequest request){
        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setFullName(request.fullName());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setProvider(AuthProvider.LOCAL);

        User saveUser = userRepository.save(user);
        return new UserResponse(
                saveUser.getId(),
                saveUser.getEmail(),
                saveUser.getFullName(),
                saveUser.getProvider()
        );
    }
}

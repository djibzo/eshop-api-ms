package com.eshop.userservice.service;

import com.eshop.userservice.dto.AuthRequest;
import com.eshop.userservice.dto.AuthResponse;
import com.eshop.userservice.dto.RegisterRequest;
import com.eshop.userservice.entity.User;
import com.eshop.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé !");
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getEmail().split("@")[0])
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
    }

//    public String authenticate(AuthRequest request) {
//        System.out.println("authservice "+request.getEmail());
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("NOT FOUND"));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("NOT FOUND P");
//        }
//        return jwtService.generateToken(user.getEmail());
//    }
public AuthResponse authenticate(AuthRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new RuntimeException("Mot de passe incorrect");
    }

    String accessToken = jwtService.generateToken(user.getEmail());
    String refreshToken = jwtService.generateRefreshToken(user.getEmail());

    user.setRefreshToken(refreshToken);
    userRepository.save(user);

    return new AuthResponse(accessToken, refreshToken);
    }
    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new RuntimeException("Refresh token invalide ou expiré");
        }

        String newAccessToken = jwtService.generateToken(email);
        String newRefreshToken = jwtService.generateRefreshToken(email);

        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }


}
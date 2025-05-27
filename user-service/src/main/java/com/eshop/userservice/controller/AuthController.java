package com.eshop.userservice.controller;
import com.eshop.userservice.dto.AuthRequest;
import com.eshop.userservice.dto.AuthResponse;
import com.eshop.userservice.dto.RegisterRequest;
import com.eshop.userservice.entity.User;
import com.eshop.userservice.repository.UserRepository;
import com.eshop.userservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService,UserRepository userRepository) {
        this.authService = authService;
        this.userRepository=userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterRequest> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = authService.authenticate(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }
    @GetMapping("/me")
    public ResponseEntity<RegisterRequest> me(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        RegisterRequest userDTO = new RegisterRequest(user.getEmail(), user.getUsername(), user.getRole());
        return ResponseEntity.ok(userDTO);
    }


}


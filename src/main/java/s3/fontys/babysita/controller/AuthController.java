package s3.fontys.babysita.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import s3.fontys.babysita.business.AuthService;
import s3.fontys.babysita.domain.LoginRequest;
import s3.fontys.babysita.domain.LoginResponse;

@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
    }
}

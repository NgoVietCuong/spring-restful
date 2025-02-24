package com.nvc.spring_boot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nvc.spring_boot.domain.dto.LoginDTO;
import com.nvc.spring_boot.domain.dto.ResLoginDTO;
import com.nvc.spring_boot.service.AuthService;

import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${jwt.expiration.refresh-token}")
    private Long refreshTokenExpiration;

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDto) {
        Map<String, Object> result = authService.login(loginDto);
        ResLoginDTO resLogin = (ResLoginDTO) result.get("resLogin");
        String refreshToken = (String) result.get("refreshToken");

        //set cookies
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(resLogin);
    }

    @GetMapping("/account")
    public ResponseEntity<ResLoginDTO.UserLogin> account() {
        return ResponseEntity.ok(authService.getAccount());
    }
}

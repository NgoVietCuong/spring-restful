package com.nvc.spring_boot.controller;

import com.nvc.spring_boot.util.annotation.ApiMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nvc.spring_boot.dto.auth.request.LoginRequest;
import com.nvc.spring_boot.dto.auth.response.LoginResponse;
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
    @ApiMessage("Login with email and password")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Map<String, Object> result = authService.login(loginRequest);
        LoginResponse resLogin = (LoginResponse) result.get("resLogin");
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
    @ApiMessage("Get current user account")
    public ResponseEntity<LoginResponse.UserLogin> account() {
        return ResponseEntity.ok(authService.getAccount());
    }

    @PostMapping("/refresh")
    @ApiMessage("Refresh token")
    public ResponseEntity<LoginResponse> refreshToken(@CookieValue(name="refresh_token") String token) {
        Map<String, Object> result = authService.refreshToken(token);
        LoginResponse resLogin = (LoginResponse) result.get("resLogin");
        String refreshToken = (String) result.get("refreshToken");

        //set cookies
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(resLogin);
    }

    @PostMapping("/logout")
    @ApiMessage("Logout")
    public ResponseEntity<Void> logout() {
        authService.logout();

        //delete cookies
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(null);
    }
}

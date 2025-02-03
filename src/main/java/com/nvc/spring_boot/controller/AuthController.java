package com.nvc.spring_boot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nvc.spring_boot.domain.dto.LoginDTO;
import com.nvc.spring_boot.domain.dto.ResLoginDTO;
import com.nvc.spring_boot.util.SecurityUtil;

import jakarta.validation.Valid;

@RestController
public class AuthController {
    private final SecurityUtil securityUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(SecurityUtil securityUtil, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.securityUtil = securityUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());
                
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String accessToken = securityUtil.createToken(authentication);
        ResLoginDTO res = new ResLoginDTO();
        res.setAccessToken(accessToken);
        return ResponseEntity.ok().body(res);
    }
}

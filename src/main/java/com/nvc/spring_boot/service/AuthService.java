package com.nvc.spring_boot.service;

import com.nvc.spring_boot.domain.User;
import com.nvc.spring_boot.domain.dto.LoginDTO;
import com.nvc.spring_boot.domain.dto.ResLoginDTO;
import com.nvc.spring_boot.util.SecurityUtil;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserService userService;
    private final SecurityUtil securityUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthService(UserService userService, SecurityUtil securityUtil, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.userService = userService;
        this.securityUtil = securityUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    public Map<String, Object> login(LoginDTO loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLogin = new ResLoginDTO();

        String accessToken = securityUtil.createAccessToken(authentication);
        resLogin.setAccessToken(accessToken);

        User user = userService.findUserByEmail(loginDto.getEmail());
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(user.getId(), user.getName(), user.getEmail());
        resLogin.setUser(userLogin);

        // create refresh token and update user
        String refreshToken = securityUtil.createRefreshToken(loginDto.getEmail(), resLogin);
        userService.updateUserToken(user, refreshToken);

        Map<String, Object> result =  new HashMap<>();
        result.put("resLogin", resLogin);
        result.put("refreshToken", refreshToken);

        return result;
    }
}

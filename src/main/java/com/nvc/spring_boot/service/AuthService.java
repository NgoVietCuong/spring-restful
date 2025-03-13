package com.nvc.spring_boot.service;

import com.nvc.spring_boot.entity.User;
import com.nvc.spring_boot.dto.auth.request.LoginRequest;
import com.nvc.spring_boot.dto.auth.response.LoginResponse;
import com.nvc.spring_boot.util.SecurityUtil;

import com.nvc.spring_boot.util.error.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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

    public Map<String, Object> login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginResponse resLogin = new LoginResponse();

        User user = userService.findUserByEmail(loginRequest.getEmail());
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin(user.getId(), user.getName(), user.getEmail());
        resLogin.setUser(userLogin);

        //create access token
        String accessToken = securityUtil.createAccessToken(authentication.getName(), resLogin);
        resLogin.setAccessToken(accessToken);

        // create refresh token and update user
        String refreshToken = securityUtil.createRefreshToken(loginRequest.getEmail(), resLogin);
        userService.updateUserToken(user, refreshToken);

        Map<String, Object> result =  new HashMap<>();
        result.put("resLogin", resLogin);
        result.put("refreshToken", refreshToken);

        return result;
    }

    public LoginResponse.UserLogin getAccount() {
        String email = securityUtil.getCurrentUserLogin().isPresent() ? securityUtil.getCurrentUserLogin().get() : "";
        User user = userService.findUserByEmail(email);
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin();
        if (user != null) {
            BeanUtils.copyProperties(user, userLogin);
        }

        return userLogin;
    }

    public Map<String, Object> refreshToken(String token) throws ResourceNotFoundException {
        Jwt jwt = securityUtil.checkValidRefreshToken(token);
        String email = jwt.getSubject();

        //check user by email and refresh token
        User user = userService.findUserByEmailAndRefreshToken(email, token);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        LoginResponse resLogin = new LoginResponse();

        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin(user.getId(), user.getName(), user.getEmail());
        resLogin.setUser(userLogin);

        //create access token
        String accessToken = securityUtil.createAccessToken(email, resLogin);
        resLogin.setAccessToken(accessToken);

        // create refresh token and update user
        String newRefreshToken = securityUtil.createRefreshToken(email, resLogin);
        userService.updateUserToken(user, newRefreshToken);

        Map<String, Object> result =  new HashMap<>();
        result.put("resLogin", resLogin);
        result.put("refreshToken", newRefreshToken);

        return result;
    }

    public void logout() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User user = userService.findUserByEmail(email);
        userService.updateUserToken(user, null);
    }
}

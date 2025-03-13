package com.nvc.spring_boot.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvc.spring_boot.dto.RestResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    //handle jwt exception
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        this.delegate.commence(request, response, authException);
        response.setContentType("application/json");

        RestResponse<Object> res = new RestResponse<Object>();
        
        String errorMessage = Optional.ofNullable(authException.getCause()) //empty token
                .map(Throwable::getMessage)
                .orElse(authException.getMessage());

        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        res.setError(errorMessage);
        res.setMessage("Invalid token");
        mapper.writeValue(response.getWriter(), res);
    }
}

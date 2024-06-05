package com.app.springapp.controller;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.app.springapp.service.RecaptchaService;

public class RecaptchaFilter extends OncePerRequestFilter {

    private RecaptchaService recaptchaService;

    public RecaptchaFilter(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("/login".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String recaptchaResponse = request.getParameter("g-recaptcha-response");
            boolean isCaptchaValid = recaptchaService.verifyRecaptcha(recaptchaResponse);

            if (!isCaptchaValid) {
                response.sendRedirect("/login?captchaError=true");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

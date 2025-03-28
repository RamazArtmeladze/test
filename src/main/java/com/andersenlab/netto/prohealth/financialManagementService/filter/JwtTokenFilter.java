package com.andersenlab.netto.prohealth.financialManagementService.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.andersenlab.netto.prohealth.financialManagementService.util.MessageConstants.TWO_FACTOR_AUTHENTICATION_NOT_COMPLETE;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    public static final String OTP_REQUIRED_CLAIM = "otp_required";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Jwt token = (Jwt) authentication.getPrincipal();
            boolean otpRequired = (Boolean) token.getClaims().getOrDefault(OTP_REQUIRED_CLAIM, true);

            if (otpRequired) {
                response.sendError(SC_FORBIDDEN, TWO_FACTOR_AUTHENTICATION_NOT_COMPLETE);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

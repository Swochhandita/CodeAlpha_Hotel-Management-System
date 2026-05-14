package com.codealpha.hotel_management_system.config.jwt;

import com.codealpha.hotel_management_system.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!jwtUtils.validateToken(token)) {
            log.warn("Invalid JWT token for request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
        String email = jwtUtils.extractEmail(token);
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            if (jwtUtils.isTokenValid(token, userDetails)) {
                //Create an authentication object only if the token is valid
                // This is Spring Security's way of saying
                // "this user is authenticated and has these roles."
                // First parameter  = who is the user (userDetails)
                // Second parameter = credentials (null — we use JWT not password here)
                // Third parameter  = what roles does the user have
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                //Attach extra request details to the auth token
                // like IP address and session ID.
                // Not required but good practice for audit logging.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Any subsequent @PreAuthorize check in controllers
                // reads from this SecurityContext.
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("User {} authenticated successfully", email);
            }
        }
        //Always call filterChain.doFilter() at the end.
        // This passes the request to the next filter or controller.
        // Without this line the request would be stuck in this filter forever.
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // StringUtils.hasText checks the string is not null,
        // not empty and not just whitespace — cleaner than doing all three checks manually
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // substring(7) removes "Bearer " (7 characters)
            // and returns just the raw token string
            return bearerToken.substring(7);// removes the first 7 characters and provide the remaining string which is the token.
        }
        return null;
    }
}

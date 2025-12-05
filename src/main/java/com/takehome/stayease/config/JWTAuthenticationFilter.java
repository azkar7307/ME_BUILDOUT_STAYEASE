package com.takehome.stayease.config;

import java.io.IOException;
import com.takehome.stayease.service.JWTService;
import com.takehome.stayease.service.impl.UserServiceImpl;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter{

    private final JWTService jwtService;
    private final UserServiceImpl userServiceImpl;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userName;
        final String path = request.getRequestURI();
        final String method = request.getMethod();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            if (path.startsWith("/api/users")
                || (path.startsWith("/api/hotels") && method.equals("GET"))
            ) {
                filterChain.doFilter(request, response);
                return;
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Authentication token required\"}");
                return;
            }
        }

        // Extract the token from the header
        jwt = authHeader.substring(7);
        // Extract the username from the token (username is the email)
        userName = jwtService.extractUserName(jwt);
        // Authenticate if not already authenticated
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = userServiceImpl.loadUserByUsername(userName);


            // Check if token is valid
            if (jwtService.isTokenValid(jwt, user)) {
                // Update the security context holder
                UsernamePasswordAuthenticationToken authenticationToken = 
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        
                // Set additional details such as user's IP address, browser, or other
                // attributes
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } 
        }
        // Call the next filter
        filterChain.doFilter(request, response);
    }
}

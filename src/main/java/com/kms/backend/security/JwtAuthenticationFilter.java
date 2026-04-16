package com.kms.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Look for the "Authorization" header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. If it's missing or doesn't start with "Bearer ", skip this filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract the token (everything after "Bearer ")
        jwt = authHeader.substring(7);
        username = jwtUtil.extractUsername(jwt);

        // 4. If there's a username and the user isn't authenticated yet
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // 5. Check if the token is valid using your JwtUtil
            if (jwtUtil.validateToken(jwt, username)) {
                
                // 6. Create an authentication object for Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>() // No roles for now
                );
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 7. Tell Spring Security "This user is valid"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 8. Continue to the next filter/controller
        filterChain.doFilter(request, response);
    }
}

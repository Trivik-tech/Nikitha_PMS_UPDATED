package com.triviktech.config.filter;

import com.triviktech.exception.token.TokenExpiredException;
import com.triviktech.utilities.jwt.JwtService;
import com.triviktech.utilities.userdatils.EmployeeDetailsService;
import com.triviktech.utilities.userdatils.HrDetailsService;
import com.triviktech.utilities.userdatils.ManagerDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    public static String jwtToken = "";

    private final JwtService jwtService;
    private final ManagerDetailsService managerDetailsService;
    private final HrDetailsService hrDetailsService;
    private final EmployeeDetailsService employeeDetailsService;

    public JwtRequestFilter(JwtService jwtService, ManagerDetailsService managerDetailsService,
                            HrDetailsService hrDetailsService, EmployeeDetailsService employeeDetailsService) {
        this.jwtService = jwtService;
        this.managerDetailsService = managerDetailsService;
        this.hrDetailsService = hrDetailsService;
        this.employeeDetailsService = employeeDetailsService;
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/ws") || path.contains("sockjs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Debug: Print path and token
//        System.out.println("🔍 Path: " + request.getRequestURI());
//        System.out.println("🔐 Auth Header: " + request.getHeader("Authorization"));

        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            jwtToken = token;
//            System.out.println("JWT Token: " + token);

            try {
                if (!jwtService.isTokenValid(token)) {
                    throw new TokenExpiredException("Token Expired , Please log in again");
                }

                String role = jwtService.getRole(token);
//                System.out.println("Role from token: " + role);
                UserDetails userDetails = null;
                String username = jwtService.getUsername(token);

                if (role.equalsIgnoreCase("MANAGER")) {
                    userDetails = managerDetailsService.loadUserByUsername(username);
                } else if (role.equalsIgnoreCase("HR")) {
                    userDetails = hrDetailsService.loadUserByUsername(username);
                } else if (role.equalsIgnoreCase("EMPLOYEE")) {
                    userDetails = employeeDetailsService.loadUserByUsername(username);
                }

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                filterChain.doFilter(request, response);

            } catch (TokenExpiredException e) {
                sendErrorResponse(response, "Token Expired. Please log in again.", HttpServletResponse.SC_UNAUTHORIZED);
            } catch (Exception e) {
                e.printStackTrace();
                sendErrorResponse(response, "Invalid Token: " + e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{ \"error\": \"" + message + "\" }");
    }
}
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

/**
 * JwtRequestFilter is a Spring Security filter that intercepts HTTP requests
 * and validates JWT tokens for authentication.
 * <p>
 * Responsibilities:
 * - Skip filtering for WebSocket and SockJS endpoints.
 * - Extract JWT from the "Authorization" header.
 * - Validate the JWT and extract username and role.
 * - Load UserDetails based on role (Manager, HR, Employee).
 * - Set authentication in SecurityContext if token is valid.
 * - Handle token expiration or invalid tokens gracefully.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    // Holds the latest JWT token globally (optional, can be used elsewhere)
    public static String jwtToken = "";

    private final JwtService jwtService;
    private final ManagerDetailsService managerDetailsService;
    private final HrDetailsService hrDetailsService;
    private final EmployeeDetailsService employeeDetailsService;

    /**
     * Constructor for JwtRequestFilter.
     *
     * @param jwtService             JWT utility service for validation and extraction
     * @param managerDetailsService  Service to load Manager user details
     * @param hrDetailsService       Service to load HR user details
     * @param employeeDetailsService Service to load Employee user details
     */
    public JwtRequestFilter(JwtService jwtService, ManagerDetailsService managerDetailsService,
                            HrDetailsService hrDetailsService, EmployeeDetailsService employeeDetailsService) {
        this.jwtService = jwtService;
        this.managerDetailsService = managerDetailsService;
        this.hrDetailsService = hrDetailsService;
        this.employeeDetailsService = employeeDetailsService;
    }

    /**
     * Specifies which requests should be skipped from this filter.
     * For example, WebSocket and SockJS endpoints are not filtered.
     *
     * @param request incoming HTTP request
     * @return true if the request should NOT be filtered, false otherwise
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/ws") || path.contains("sockjs");
    }

    /**
     * Main filter logic for JWT validation.
     * <p>
     * Steps:
     * 1. Extract JWT from Authorization header.
     * 2. Validate the token.
     * 3. Determine user role and load UserDetails.
     * 4. Set authentication in SecurityContext.
     * 5. Forward the request or return error for invalid/expired token.
     *
     * @param request     incoming HTTP request
     * @param response    HTTP response
     * @param filterChain filter chain
     * @throws ServletException in case of servlet errors
     * @throws IOException      in case of I/O errors
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tokenHeader = request.getHeader("Authorization");

        // Check if Authorization header contains a Bearer token
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7); // Extract token
            jwtToken = token; // store globally if needed

            try {
                // Validate token
                if (!jwtService.isTokenValid(token)) {
                    throw new TokenExpiredException("Token Expired , Please log in again");
                }

                // Extract role and username from token
                String role = jwtService.getRole(token);
                String username = jwtService.getUsername(token);

                // Load UserDetails based on role
                UserDetails userDetails = null;
                if (role.equalsIgnoreCase("MANAGER")) {
                    userDetails = managerDetailsService.loadUserByUsername(username);
                } else if (role.equalsIgnoreCase("HR")) {
                    userDetails = hrDetailsService.loadUserByUsername(username);
                } else if (role.equalsIgnoreCase("EMPLOYEE")) {
                    userDetails = employeeDetailsService.loadUserByUsername(username);
                }

                // Set authentication in SecurityContext if userDetails is available
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

                // Continue filter chain
                filterChain.doFilter(request, response);

            } catch (TokenExpiredException e) {
                // Handle expired token
                sendErrorResponse(response, "Token Expired. Please log in again.", HttpServletResponse.SC_UNAUTHORIZED);
            } catch (Exception e) {
                // Handle any other token errors
                e.printStackTrace();
                sendErrorResponse(response, "Invalid Token: " + e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            // No token present, continue filter chain without authentication
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Sends a JSON error response with a custom message and HTTP status.
     *
     * @param response HttpServletResponse object
     * @param message  Error message
     * @param status   HTTP status code
     * @throws IOException in case of I/O errors
     */
    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{ \"error\": \"" + message + "\" }");
    }
}

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

    private final JwtService jwtService;
    private final ManagerDetailsService managerDetailsService;
    private final HrDetailsService hrDetailsService;
    private final EmployeeDetailsService employeeDetailsService;

    public JwtRequestFilter(JwtService jwtService, ManagerDetailsService managerDetailsService, HrDetailsService hrDetailsService, EmployeeDetailsService employeeDetailsService) {
        this.jwtService = jwtService;
        this.managerDetailsService = managerDetailsService;
        this.hrDetailsService = hrDetailsService;
        this.employeeDetailsService = employeeDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")){
            String token = tokenHeader.substring(7);

            try{
                if(!jwtService.isTokenValid(token)){
                    throw new TokenExpiredException("Token Expired , Please log in again");
                }

                String role = jwtService.getRole(token);

                if(role.equalsIgnoreCase("MANAGER")){
                    UserDetails userDetails = managerDetailsService.loadUserByUsername(jwtService.getUsername(token));
                    if(userDetails !=null){
                        UsernamePasswordAuthenticationToken authToken
                                = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }

                }
                else if(role.equalsIgnoreCase("HR")){
                    UserDetails userDetails = hrDetailsService.loadUserByUsername(jwtService.getUsername(token));
                    if(userDetails !=null){
                        UsernamePasswordAuthenticationToken authToken
                                = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                    }

                }
                else {
                    UserDetails userDetails = employeeDetailsService.loadUserByUsername(jwtService.getUsername(token));
                    if(userDetails !=null){
                        UsernamePasswordAuthenticationToken authToken
                                = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                    }
                }
                filterChain.doFilter(request,response);

            }catch (TokenExpiredException e) {
                sendErrorResponse(response, "Token Expired. Please log in again.", HttpServletResponse.SC_UNAUTHORIZED);
            } catch (Exception e) {
                sendErrorResponse(response, "Invalid Token", HttpServletResponse.SC_UNAUTHORIZED);
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




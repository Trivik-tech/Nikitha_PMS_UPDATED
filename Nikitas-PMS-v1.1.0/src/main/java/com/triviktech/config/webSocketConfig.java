package com.triviktech.config;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.triviktech.utilities.jwt.JwtService;

@Configuration
@EnableWebSocketMessageBroker
public class webSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    JwtService jwt;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("http://localhost:3000")
                .setHandshakeHandler(new DefaultHandshakeHandler() {

                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wSocketHandler,
                            Map<String, Object> attributes) {

                        System.out.println(" [WebSocket] Handshake started...");
                        String token = extractTokenFromRequest(request);

                        if (token != null) {
                            System.out.println(" [WebSocket] Extracted token: " + token);
                            boolean isValid = jwt.isTokenValid(token);
                            System.out.println(" [WebSocket] Is token valid? " + isValid);

                            if (isValid) {
                                String username = jwt.getUsername(token);
                                System.out.println(" [WebSocket] Extracted username from token: " + username);

                                return () -> username;
                            } else {
                                System.out.println(" [WebSocket] Token is invalid.");
                            }
                        } else {
                            System.out.println(" [WebSocket] No token found in request.");
                        }

                        return null;
                    }

                }).withSockJS();
    }

    private String extractTokenFromRequest(ServerHttpRequest request) {
        List<String> authHeaders = request.getHeaders().get("Authorization");

        if (authHeaders != null && !authHeaders.isEmpty()) {
            String bearer = authHeaders.get(0);
            if (bearer.startsWith("Bearer ")) {
                return bearer.substring(7);
            }
        }

        URI uri = request.getURI();
        String query = uri.getQuery();

        if (query != null && query.contains("token=")) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("token=")) {
                    return param.substring(6); // token=NB238 -> NB238
                }
            }
        }

        return null;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }
}

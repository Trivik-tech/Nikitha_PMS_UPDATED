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
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtService jwt;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:3000", "http://192.168.0.184:3000")
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request,
                                                      WebSocketHandler wsHandler,
                                                      Map<String, Object> attributes) {
//                        System.out.println("🔵 [WebSocket] Handshake started...");

                        String token = extractTokenFromRequest(request);

                        if (token != null) {
//                            System.out.println("🔵 [WebSocket] Extracted token: " + token);

                            boolean isValid = jwt.isTokenValid(token);
//                            System.out.println("🔵 [WebSocket] Is token valid? " + isValid);

                            if (isValid) {
                                String username = jwt.getUsername(token);
//                                System.out.println("🟢 [WebSocket] Authenticated user: " + username);

                                return () -> username; // returning Principal object
                            } else {
//                                System.out.println("🔴 [WebSocket] Invalid token.");
                            }
                        } else {
//                            System.out.println("⚠️ [WebSocket] Token not found in request.");
                        }

                        return null; // Returning null rejects the handshake with 403
                    }
                })
                .withSockJS(); // Enable SockJS fallback
    }

    private String extractTokenFromRequest(ServerHttpRequest request) {
        // 1. Try Authorization header first
        List<String> authHeaders = request.getHeaders().get("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String bearer = authHeaders.get(0);
            if (bearer.startsWith("Bearer ")) {
                return bearer.substring(7);
            }
        }

        // 2. Try URL query param ?token=...
        URI uri = request.getURI();
        String query = uri.getQuery();
        if (query != null && query.contains("token=")) {
            for (String param : query.split("&")) {
                if (param.startsWith("token=")) {
                    return param.substring(6); // remove 'token='
                }
            }
        }

        return null; // not found
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // for subscription
        registry.setApplicationDestinationPrefixes("/app"); // for sending
        registry.setUserDestinationPrefix("/user"); // for user-specific messaging
    }
}

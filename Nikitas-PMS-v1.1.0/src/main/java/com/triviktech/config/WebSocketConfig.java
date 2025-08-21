package com.triviktech.config;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import com.triviktech.config.security.Origins;
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

/**
 * WebSocketConfig class configures STOMP over WebSocket for real-time communication.
 *
 * Key features:
 * - Registers a STOMP endpoint (/ws) with SockJS fallback.
 * - Validates JWT token during the WebSocket handshake to authenticate users.
 * - Configures message broker for subscription and application message routing.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtService jwt; // Service to validate and parse JWT tokens

    /**
     * Registers STOMP endpoints for WebSocket connections.
     * Enables SockJS fallback and allows connections from specified origins.
     *
     * @param registry StompEndpointRegistry to register endpoints
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // STOMP endpoint URL
                .setAllowedOriginPatterns(Origins.serverUrl, Origins.localUrl) // Allowed frontend origins
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    /**
                     * Custom handshake handler to determine user Principal from JWT token.
                     *
                     * @param request incoming HTTP request during handshake
                     * @param wsHandler WebSocket handler
                     * @param attributes handshake attributes
                     * @return Principal representing authenticated user, or null if invalid
                     */
                    @Override
                    protected Principal determineUser(ServerHttpRequest request,
                                                      WebSocketHandler wsHandler,
                                                      Map<String, Object> attributes) {

                        String token = extractTokenFromRequest(request); // Extract token from header or query

                        if (token != null) {
                            boolean isValid = jwt.isTokenValid(token); // Validate JWT token

                            if (isValid) {
                                String username = jwt.getUsername(token); // Extract username from token
                                return () -> username; // Return Principal object for authenticated user
                            }
                        }

                        return null; // Null rejects handshake (403 forbidden)
                    }
                })
                .withSockJS(); // Enable SockJS fallback for browsers without WebSocket support
    }

    /**
     * Extracts JWT token from the incoming WebSocket request.
     * Looks for Authorization header first, then URL query parameter `token`.
     *
     * @param request ServerHttpRequest object
     * @return JWT token string, or null if not found
     */
    private String extractTokenFromRequest(ServerHttpRequest request) {
        // 1. Check Authorization header
        List<String> authHeaders = request.getHeaders().get("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String bearer = authHeaders.get(0);
            if (bearer.startsWith("Bearer ")) {
                return bearer.substring(7);
            }
        }

        // 2. Check URL query parameter ?token=...
        URI uri = request.getURI();
        String query = uri.getQuery();
        if (query != null && query.contains("token=")) {
            for (String param : query.split("&")) {
                if (param.startsWith("token=")) {
                    return param.substring(6); // remove 'token='
                }
            }
        }

        return null; // Token not found
    }

    /**
     * Configures the message broker for STOMP messaging.
     * Sets prefixes for destinations and enables simple in-memory broker for subscriptions.
     *
     * @param registry MessageBrokerRegistry object
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // Topics for broadcast, queues for private messages
        registry.setApplicationDestinationPrefixes("/app"); // Prefix for messages sent from clients
        registry.setUserDestinationPrefix("/user"); // Prefix for user-specific messages
    }
}

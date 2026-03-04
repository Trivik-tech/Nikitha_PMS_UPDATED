package com.triviktech.config.security;

/**
 * Origins class holds constant URLs used for Cross-Origin Resource Sharing
 * (CORS)
 * configuration or other network-related references in the application.
 * <p>
 * This class is made non-instantiable by providing a private constructor.
 */
public class Origins {

    // Private constructor to prevent instantiation
    private Origins() {
    }

    /**
     * URL of the server environment.
     * Used for requests coming from a specific networked server.
     * Example: Team member's server (ARPITA's change applied)
     */
    public static final String serverUrl = "http://192.168.0.110:3000";

    /**
     * URL of the local development environment.
     * Typically used when running the frontend locally during development.
     */
    public static final String localUrl = "http://localhost:3000";
    public static final String localUrl2 = "http://localhost:3001";

}

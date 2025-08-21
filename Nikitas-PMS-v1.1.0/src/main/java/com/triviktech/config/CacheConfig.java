package com.triviktech.config;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CacheConfig class configures caching for the application using Caffeine cache.
 *
 * Key features:
 * - Enables Spring's annotation-driven caching support via @EnableCaching.
 * - Configures a Caffeine-based cache manager with a specific expiration time.
 */
@Configuration
@EnableCaching // Enables Spring's annotation-driven cache management
public class CacheConfig {

    // Cache expiration time in minutes
    private final long expirationTime = 10;

    /**
     * Configures the CacheManager bean using Caffeine.
     *
     * The cache manager automatically handles caching for methods annotated
     * with @Cacheable, @CachePut, and @CacheEvict.
     *
     * @return CacheManager instance configured with Caffeine cache
     */
    @Bean
    public CacheManager cacheManager() {
        // Create a CaffeineCacheManager
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // Configure Caffeine cache with expiration after write
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(expirationTime, TimeUnit.MINUTES)
        );

        return cacheManager;
    }
}

package com.bootteam.springcloudgateway.filter;

import com.bootteam.springcloudgateway.domain.ApiKey;
import com.bootteam.springcloudgateway.exceptions.ApiKeyNotFoundException;
import com.bootteam.springcloudgateway.exceptions.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.cloud.gateway.filter.GlobalFilter;


import java.time.LocalDateTime;

import static com.bootteam.springcloudgateway.configuration.AppConstant.RECORD_KEY;

@Slf4j
@Component
public class ApiKeyFilter implements GlobalFilter, Ordered {

    public static final String X_API_KEY = "X-API-KEY";

    private final ReactiveRedisTemplate<String, ApiKey> redisTemplate;

    public ApiKeyFilter(ReactiveRedisTemplate<String, ApiKey> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String apiKeyValue = exchange.getRequest()
                .getHeaders()
                .getFirst(X_API_KEY);

        if (apiKeyValue == null || apiKeyValue.isBlank()) {
            return Mono.error(new ApiKeyNotFoundException("API key is missing"));
        }

        String path = exchange.getRequest().getPath().value();
        String serviceName = extractServiceName(path);

        return redisTemplate.opsForValue()
                .get(RECORD_KEY + apiKeyValue)
                .switchIfEmpty(Mono.error(new ApiKeyNotFoundException("API key not found")))
                .flatMap(apiKey -> processApiKey(apiKey, serviceName, exchange, chain));
    }



    private Mono<Void> processApiKey(ApiKey apiKey, String serviceName,
                                     ServerWebExchange exchange, GatewayFilterChain chain) {
        if (isServiceAllowed(apiKey, serviceName)) {
            log.debug("API key authorized for service {}", serviceName);
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-API-KEY-VALID", "true")
                    .header("X-API-KEY-ID", apiKey.getKey())
                    .build();
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } else {
            log.warn("API key not allowed for service {}", serviceName);
            return Mono.error(new AccessDeniedException("Access denied for this service"));
        }
    }

    private boolean isServiceAllowed(ApiKey apiKey, String requestedServiceName) {
        if (apiKey.getServices() == null) {
            return false;
        }

        return apiKey.getServices().stream()
                .anyMatch(service -> {
                    boolean nameMatches = service.getName().equalsIgnoreCase(requestedServiceName);
                    boolean notExpired = service.getExpiresAt() == null ||
                            service.getExpiresAt().isAfter(LocalDateTime.now());
                    return nameMatches && notExpired;
                });
    }

    private String extractServiceName(String path) {
        // Remove leading slash and get first segment
        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        String[] parts = cleanPath.split("/");
        return parts.length > 0 ? parts[0] : "";
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

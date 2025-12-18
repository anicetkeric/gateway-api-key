package com.bootteam.springcloudgateway.configuration;

import com.bootteam.springcloudgateway.domain.ApiKey;
import com.bootteam.springcloudgateway.domain.AppService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

import static com.bootteam.springcloudgateway.configuration.AppConstant.*;


@Slf4j
@Component
public class ApiKeyInitializer {

    private final ReactiveRedisTemplate<String, ApiKey> redisTemplate;

    public ApiKeyInitializer(ReactiveRedisTemplate<String, ApiKey> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void initData() {
        List<ApiKey> apiKeys = List.of(
                new ApiKey("343C-ED0B-4137-B27E",
                        List.of(
                                new AppService(CUSTOMERS_SERVICE_NAME, LocalDateTime.now().plusDays(30)),
                                new AppService(PAYMENTS_SERVICE_NAME, null)
                        )),
                new ApiKey("FA48-EF0C-427E-8CCF",
                        List.of(
                                new AppService(CUSTOMERS_SERVICE_NAME, LocalDateTime.now().minusDays(25)),
                                new AppService(PAYMENTS_SERVICE_NAME, LocalDateTime.now().minusHours(3))
                        ))
        );

        Flux.fromIterable(apiKeys)
                .flatMap(apiKey -> redisTemplate.opsForValue().set(getKey(apiKey), apiKey))
                .doOnComplete(() -> log.info("API keys initialized in Redis"))
                .subscribe();
    }

    private static String getKey(ApiKey apiKey) {
        return RECORD_KEY + apiKey.getKey();
    }
}


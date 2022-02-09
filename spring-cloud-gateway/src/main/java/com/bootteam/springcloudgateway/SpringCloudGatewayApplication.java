package com.bootteam.springcloudgateway;

import com.bootteam.springcloudgateway.configuration.AppConstant;
import com.bootteam.springcloudgateway.configuration.RedisHashComponent;
import com.bootteam.springcloudgateway.domain.ApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringCloudGatewayApplication {

    @Autowired
    RedisHashComponent redisHashComponent;


    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGatewayApplication.class, args);
    }


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(AppConstant.SERVICE_A_KEY,
                        r -> r.path("/api/service-a/**")
                                .filters(f -> f.stripPrefix(2)).uri("http://localhost:8081"))
                .route(AppConstant.SERVICE_B_KEY,
                        r -> r.path("/api/service-b/**")
                                .filters(f -> f.stripPrefix(2)).uri("http://localhost:8082"))
                .build();
    }


    @PostConstruct
    public void initData() {
        List<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey("343C-ED0B-4137-B27E", List.of(AppConstant.SERVICE_B_KEY, AppConstant.SERVICE_A_KEY)));
        apiKeys.add(new ApiKey("FA48-EF0C-427E-8CCF", List.of(AppConstant.SERVICE_B_KEY)));


        List<Object> list = redisHashComponent.hVals(AppConstant.RECORD_KEY);
        if(list.isEmpty()){
            apiKeys.forEach(k -> redisHashComponent.hSet(AppConstant.RECORD_KEY,k.getKey() , k));
        }
    }


}

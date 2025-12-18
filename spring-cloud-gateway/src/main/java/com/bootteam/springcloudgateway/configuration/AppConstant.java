package com.bootteam.springcloudgateway.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConstant {
    public static final String RECORD_KEY = "api_key:";

    public static final String CUSTOMERS_SERVICE_NAME = "customer-service";
    public static final String PAYMENTS_SERVICE_NAME = "payment-service";

}

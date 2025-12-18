package com.bootteam.springcloudgateway.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class ApiKey {

    @NotNull
    private String key;

    private List<AppService> services;

}
package com.bootteam.springcloudgateway.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppService {

    @NotNull
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime expiresAt;
}

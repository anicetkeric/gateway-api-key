package com.bootteam.springcloudgateway.filter;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Component
public class GatewayErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = super.getError(request);
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("message", error.getMessage());
        errorAttributes.put("method", request.methodName());
        errorAttributes.put("path", request.path());

        MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations
                .from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class);

        HttpStatus errorStatus = getHttpStatus(error, responseStatusAnnotation);
        errorAttributes.put("status", errorStatus.value());
        errorAttributes.put("requestId", request.exchange().getRequest().getId());

        return errorAttributes;
    }

    private HttpStatus getHttpStatus(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
        if (error instanceof ResponseStatusException) {
            return ((ResponseStatusException) error).getStatus();
        }
        return responseStatusAnnotation.getValue("code", HttpStatus.class).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
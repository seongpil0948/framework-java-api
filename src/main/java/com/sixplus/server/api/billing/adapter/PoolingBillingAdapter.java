package com.sixplus.server.api.billing.adapter;

import com.sixplus.server.api.billing.controller.ApiResponse;
import com.sixplus.server.api.billing.controller.CreateCodeRequest;
import com.sixplus.server.api.billing.controller.CreateCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@Component
public class PoolingBillingAdapter {
    private static final ParameterizedTypeReference<ApiResponse<CreateCodeResponse>> TYPE_REFERENCE;

    static {
        TYPE_REFERENCE = new ParameterizedTypeReference<>() {
        };
    }

    private final RestTemplate poolingRestTemplate;

    public PoolingBillingAdapter(RestTemplate poolingRestTemplate) {
        this.poolingRestTemplate = poolingRestTemplate;
    }

    public CreateCodeResponse createBillingCode(List<Long> hotelIds) {
        URI uri = UriComponentsBuilder.fromPath("/billing-codes")
                .scheme("http").host("127.0.0.1").port(8080)
                .build(false).encode()
                .toUri();

        CreateCodeRequest request = new CreateCodeRequest(1, hotelIds);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateCodeRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<ApiResponse<CreateCodeResponse>> responseEntity =
                poolingRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, TYPE_REFERENCE);

        if (HttpStatus.OK != responseEntity.getStatusCode()) {
            log.error("Error from Billing. status:{}, hotelIds:{}", responseEntity.getStatusCode(), hotelIds);
            throw new RuntimeException("Error from Billing. " + responseEntity.getStatusCode());
        }

        return responseEntity.getBody().getData();
    }
}

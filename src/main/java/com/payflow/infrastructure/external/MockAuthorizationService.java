package com.payflow.infrastructure.external;

import com.payflow.application.port.out.AuthorizationPort;
import com.payflow.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
class MockAuthorizationService implements AuthorizationPort {

    private static final String AUTHORIZATION_URL = "https://util.devi.tools/api/v2/authorize";

    private final RestTemplate restTemplate;

    @Override
    public boolean authorize(Transaction transaction) {
        try {
            AuthorizationResponse response = restTemplate.getForObject(AUTHORIZATION_URL, AuthorizationResponse.class);
            return response != null && "success".equals(response.status());
        } catch (Exception e) {
            return false;
        }
    }

    private record AuthorizationResponse(String status) {}
}

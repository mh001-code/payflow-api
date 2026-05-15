package com.payflow.infrastructure.external;

import com.payflow.application.port.out.NotificationPort;
import com.payflow.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
class MockNotificationService implements NotificationPort {

    private static final String NOTIFICATION_URL = "https://util.devi.tools/api/v1/notify";

    private final RestTemplate restTemplate;

    @Async
    @Override
    public void notify(Transaction transaction) {
        try {
            restTemplate.postForObject(NOTIFICATION_URL, null, String.class);
            log.info("Notification sent for transaction {}", transaction.getId());
        } catch (Exception e) {
            log.error("Notification failed for transaction {}: {}", transaction.getId(), e.getMessage());
        }
    }
}

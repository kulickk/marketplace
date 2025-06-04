package com.project.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final YooKassaClient ykClient;

    public Map<String, Object> payOrder(BigDecimal amount, String description) {
        String idempKey = UUID.randomUUID().toString();
        return ykClient.createPayment(idempKey, amount.toString(), description)
                .block();
    }

    public Map<String, Object> getPaymentInfo(String paymentId) {
        return ykClient.getPayment(paymentId)
                .block();
    }

    public Map<String, Object> capturePayment(String paymentId,String amount, String idempotenceKey) {
        return ykClient.capturePayment(paymentId, idempotenceKey, amount)
                .block(); 
    }
}
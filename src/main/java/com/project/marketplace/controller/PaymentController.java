package com.project.marketplace.controller;

import com.project.marketplace.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Создать платёж через YooKassa")
    public ResponseEntity<Map<String,Object>> createPayment(
            @RequestParam BigDecimal amount,
            @RequestParam String description) {
        Map<String,Object> result = paymentService.payOrder(amount, description);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{paymentId}")
    @Operation(summary =  "Узнать статус платежа")
    public ResponseEntity<Map<String, Object>> getPayment(
            @PathVariable String paymentId) {
        Map<String, Object> payment = paymentService.getPaymentInfo(paymentId);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/{paymentId}/capture")
    @Operation(summary = "Подтвердить (capture) платеж")
    public ResponseEntity<Map<String,Object>> capturePayment(
            @PathVariable String paymentId,
            @RequestParam("amount") String amount,
            @RequestHeader("Idempotence-Key") String idempotenceKey) {
        Map<String,Object> result = paymentService.capturePayment(
                paymentId, amount, idempotenceKey);
        return ResponseEntity.ok(result);
    }
}
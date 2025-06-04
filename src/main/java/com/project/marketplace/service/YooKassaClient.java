package com.project.marketplace.service;

import com.project.marketplace.config.YooKassaProperties;
import lombok.RequiredArgsConstructor;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class YooKassaClient {

        private final WebClient ykClient;
        private final YooKassaProperties props;

        public Mono<Map> createPayment(String idempotenceKey, String amountValue, String description) {
                Map<String, Object> body = Map.of(
                                "amount", Map.of("value", amountValue, "currency", "RUB"),
                                "confirmation", Map.of("type", "embedded"),
                                "capture", true,
                                "description", description);

                return ykClient.post()
                                .uri("/payments")
                                .header("Idempotence-Key", idempotenceKey)
                                .bodyValue(body)
                                .retrieve()
                                .onStatus(s -> !s.is2xxSuccessful(), resp -> resp.bodyToMono(String.class)
                                                .flatMap(err -> Mono
                                                                .error(new RuntimeException("YooKassa error: " + err))))
                                .bodyToMono(Map.class);
        }

        public Mono<Map<String, Object>> getPayment(String paymentId) {
                return ykClient.get()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/payments/{id}")
                                                .build(paymentId))
                                .retrieve()
                                .onStatus(status -> status.value() == 404,
                                                resp -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                "Платёж не найден")))
                                .onStatus(status -> !status.is2xxSuccessful(),
                                                resp -> resp.bodyToMono(String.class)
                                                                .flatMap(body -> Mono.error(new RuntimeException(
                                                                                "YooKassa error: " + body))))
                                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                                });
        }

        public Mono<Map<String, Object>> capturePayment(String paymentId, String idempotenceKey, String amountValue) {
                Map<String, Object> body = Map.of("amount", Map.of("value", amountValue, "currency", "RUB"));

                return ykClient.post()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/payments/{id}/capture")
                                                .build(paymentId))
                                .header("Idempotence-Key", idempotenceKey)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(body)
                                .retrieve()
                                .onStatus(status -> status.value() == 404,
                                                resp -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                "Платёж не найден")))
                                .onStatus(status -> status.value() == 409,
                                                resp -> Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,
                                                                "Неверный статус платежа для capture")))
                                .onStatus(status -> !status.is2xxSuccessful(),
                                                resp -> resp.bodyToMono(String.class)
                                                                .flatMap(err -> Mono.error(new RuntimeException(
                                                                                "YooKassa error: " + err))))
                                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                                });
        }

}
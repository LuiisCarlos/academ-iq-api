package dev.luiiscarlos.academ_iq_api.domain.billing.payment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dev.luiiscarlos.academ_iq_api.domain.billing.payment.dto.PaymentRequest;
import dev.luiiscarlos.academ_iq_api.domain.billing.payment.dto.PaymentResponse;
import dev.luiiscarlos.academ_iq_api.domain.billing.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(
            @AuthenticationPrincipal Long userId,
            @RequestBody PaymentRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentService.create(userId, request));
    }

    @GetMapping
    public ResponseEntity<Page<PaymentResponse>> getAll(Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentService.getAll(pageable));
    }

    @GetMapping("/@me")
    public ResponseEntity<PaymentResponse> get(@AuthenticationPrincipal Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentService.get(userId));
    }

}

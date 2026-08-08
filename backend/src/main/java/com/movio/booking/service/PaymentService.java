package com.movio.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class PaymentService {

    public PaymentResult processPayment(Double amount) {
        log.info("Processing payment of ₹{}", amount);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean success = ThreadLocalRandom.current().nextInt(100) < 90;
        String transactionRef = "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();

        if (success) {
            log.info("Payment succeeded: {} for ₹{}", transactionRef, amount);
        } else {
            log.warn("Payment failed: {} for ₹{}", transactionRef, amount);
        }

        return new PaymentResult(success, transactionRef);
    }

    public record PaymentResult(boolean success, String transactionRef) {}
}
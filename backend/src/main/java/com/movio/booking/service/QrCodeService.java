package com.movio.booking.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class QrCodeService {

    @Value("${jwt.secret}") // reusing the same secret is fine here — same trust boundary
    private String secret;

    public String generateSignedPayload(Long bookingId) {
        String data = "BOOKING:" + bookingId;
        String signature = sign(data);
        return data + ":" + signature;
    }

    public boolean verifyPayload(String payload) {
        String[] parts = payload.split(":");
        if (parts.length != 3) return false;

        String data = parts[0] + ":" + parts[1];
        String providedSignature = parts[2];
        String expectedSignature = sign(data);

        return expectedSignature.equals(providedSignature);
    }

    public Long extractBookingId(String payload) {
        String[] parts = payload.split(":");
        return Long.parseLong(parts[1]);
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes());
            return HexFormat.of().formatHex(hash).substring(0, 16); // shortened for a smaller QR
        } catch (Exception e) {
            throw new RuntimeException("Failed to sign QR payload", e);
        }
    }

    public byte[] generateQrImage(String payload) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(payload, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            return out.toByteArray();
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR image", e);
        }
    }
}
package com.movio.booking.controller;

import com.movio.booking.dto.CreateScreenRequest;
import com.movio.booking.entity.Screen;
import com.movio.booking.service.ScreenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScreenController {
    private final ScreenService screenService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/admin/screens")
    public Screen createScreen(@Valid @RequestBody CreateScreenRequest req) {
        return screenService.createScreen(req);
    }
}

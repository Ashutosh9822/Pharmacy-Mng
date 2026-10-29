
package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.ExpiryAlertResponse;
import com.becoder.service.ExpiryAlertService;

@RestController
@RequestMapping("/api/v1/expiry-alerts")
public class ExpiryAlertController {

	@Autowired
    private ExpiryAlertService expiryAlertService;

    @GetMapping
    public List<ExpiryAlertResponse> getExpiryAlerts(@RequestParam(defaultValue = "30") int days) {
        return expiryAlertService.getExpiryAlerts(days);
    }
    
}

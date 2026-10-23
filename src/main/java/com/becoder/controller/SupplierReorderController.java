package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.SupplierReorderResponse;
import com.becoder.service.SupplierReorderService;

@RestController
@RequestMapping("/api/v1/supplier-reorders")
public class SupplierReorderController {

	@Autowired
    private SupplierReorderService supplierReorderService;

    // Get reorder recommendations for all suppliers
    @GetMapping
    public ResponseEntity<List<SupplierReorderResponse>> getRecommendations(
            @RequestParam(defaultValue = "10") int lowStockThreshold,
            @RequestParam(defaultValue = "50") int targetStock) {

        return ResponseEntity.ok(
                supplierReorderService.getReorderRecommendations(
                        lowStockThreshold, targetStock));
    }

    // Get reorder recommendations for one supplier
    @GetMapping("/supplier")
    public ResponseEntity<List<SupplierReorderResponse>> getBySupplier(
            @RequestParam Long supplierId,
            @RequestParam(defaultValue = "10") int lowStockThreshold,
            @RequestParam(defaultValue = "50") int targetStock) {

        return ResponseEntity.ok(
                supplierReorderService.getReorderRecommendationsBySupplier(
                        supplierId, lowStockThreshold, targetStock));
    }
}

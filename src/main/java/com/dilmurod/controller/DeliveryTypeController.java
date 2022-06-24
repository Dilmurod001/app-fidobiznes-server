package com.dilmurod.controller;

import com.dilmurod.entity.DeliveryType;
import com.dilmurod.payload.ApiResponse;
import com.dilmurod.repository.DeliveryTypeRepository;
import com.dilmurod.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/delivery-type")
public class DeliveryTypeController {
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final DeliveryService deliveryService;

    @PostMapping
    public HttpEntity<?> addDeliverType(@RequestBody DeliveryType deliveryType) {
        ApiResponse apiResponse = deliveryService.add(deliveryType);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping
    private HttpEntity<?> list() {
        return ResponseEntity.ok(deliveryTypeRepository.findAllByActive(true));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable Integer id) {
        ApiResponse apiResponse = deliveryService.byId(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/unique")
    public HttpEntity<?> uniqueName(@RequestParam String name, @RequestParam Integer id) {
        boolean uniqueName = deliveryService.uniqueName(name, id);
        return ResponseEntity.ok(uniqueName);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@RequestBody DeliveryType deliveryType, @PathVariable Integer id) {
        ApiResponse apiResponse = deliveryService.edit(deliveryType, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        ApiResponse apiResponse = deliveryService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}

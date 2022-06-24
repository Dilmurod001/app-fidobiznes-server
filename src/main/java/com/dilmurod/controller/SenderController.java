package com.dilmurod.controller;

import com.dilmurod.entity.DocSender;
import com.dilmurod.payload.ApiResponse;
import com.dilmurod.repository.DocSenderRepository;
import com.dilmurod.service.SenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sender")
public class SenderController {
    private final DocSenderRepository docSenderRepository;
    public final SenderService senderService;

    @PostMapping
    public HttpEntity<?> addSender(@RequestBody DocSender docSender) {
        ApiResponse apiResponse = senderService.add(docSender);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping
    private HttpEntity<?> list() {
        return ResponseEntity.ok(docSenderRepository.findAllByActive(true));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable Integer id) {
        ApiResponse apiResponse = senderService.byId(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/unique")
    public HttpEntity<?> uniqueName(@RequestParam String name, @RequestParam Integer id) {
        boolean uniqueName = senderService.uniqueName(name, id);
        return ResponseEntity.ok(uniqueName);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@RequestBody DocSender docSender, @PathVariable Integer id) {
        ApiResponse apiResponse = senderService.edit(docSender, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        ApiResponse apiResponse = senderService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}

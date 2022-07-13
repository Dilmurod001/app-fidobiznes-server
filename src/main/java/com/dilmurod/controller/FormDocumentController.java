package com.dilmurod.controller;

import com.dilmurod.entity.FormDocument;
import com.dilmurod.payload.ApiResponse;
import com.dilmurod.payload.FormDocSort;
import com.dilmurod.payload.FormDocumentDto;
import com.dilmurod.repository.FormDocumentRepository;
import com.dilmurod.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/document")
public class FormDocumentController {
    private final FormDocumentRepository formDocumentRepository;
    private final DocumentService documentService;

    @PostMapping
    public HttpEntity<?> addDocument(@RequestBody FormDocumentDto documentDto) {
        ApiResponse apiResponse = documentService.add(documentDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);

    }

    @GetMapping
    private HttpEntity<?> list() {
        ApiResponse apiResponse = documentService.getAll();
        return ResponseEntity.ok(apiResponse);
//        return ResponseEntity.ok(formDocumentRepository.findAll(Sort.by("regDate").descending()));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable Integer id) {
        ApiResponse apiResponse = documentService.byId(id);
        Optional<FormDocument> optionalFormDocument = formDocumentRepository.findById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(optionalFormDocument.get());
    }

    @GetMapping("/sort")
    public HttpEntity<?> sort(@RequestParam String name, @RequestParam Boolean isSort) {
        ApiResponse apiResponse = documentService.sort(isSort, name);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/unique")
    public HttpEntity<?> exitsByRegNum(@RequestParam Long regNum, @RequestParam Integer id) {
        boolean uniqueRegNum = documentService.uniqueRegNum(regNum, id);
        return ResponseEntity.ok(uniqueRegNum);
    }

    @GetMapping("/search")
    public HttpEntity<?> searchFast(@RequestParam String value) {
        ApiResponse apiResponse = documentService.search(value);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/filter")
    public HttpEntity<?> filter(@RequestBody FormDocSort formDocSort) {
        ApiResponse apiResponse = documentService.filter(formDocSort);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@RequestBody FormDocumentDto formDocumentDto, @PathVariable Integer id) {
        ApiResponse apiResponse = documentService.edit(formDocumentDto, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        ApiResponse apiResponse = documentService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}

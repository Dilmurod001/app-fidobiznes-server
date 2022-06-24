package com.dilmurod.controller;

import com.dilmurod.component.MessageService;
import com.dilmurod.entity.Attachment;
import com.dilmurod.entity.AttachmentContent;
import com.dilmurod.payload.ApiResponse;
import com.dilmurod.repository.AttachmentContentRepository;
import com.dilmurod.repository.AttachmentRepository;
import com.dilmurod.utils.Constants;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attachment")
public class AttachmendController {

    private final AttachmentRepository attachmentRepository;

    private final AttachmentContentRepository attachmentContentRepository;
    private static final String uploadDirectorys = "files";


    //    upload database
    @PostMapping("/upload")
    public ApiResponse saveToDb(MultipartHttpServletRequest request) throws IOException {

        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());
        if (file != null) {
            if (Objects.requireNonNull(file.getContentType()).endsWith("/msword") || file.getContentType().endsWith("/pdf") || file.getContentType().endsWith("/docx") || file.getContentType().endsWith("/PDF")) {

                if (file.getSize() >= 1048576)
                    return new ApiResponse(MessageService.getMessage("FILE_MAX_SIZE"), false, "MAX_SIZE");

                Attachment attachment = new Attachment();
                attachment.setOrginal_name(file.getOriginalFilename());
                attachment.setName(file.getName());
                attachment.setSize(file.getSize());
                attachment.setContentType(file.getContentType());

                Attachment save = attachmentRepository.save(attachment);

                AttachmentContent attachmentContent = new AttachmentContent();
                attachmentContent.setAttachment(save);
                attachmentContent.setBytes(file.getBytes());

                attachmentContentRepository.save(attachmentContent);
                return new ApiResponse(MessageService.getMessage("SUCCESS_SAVE"), true, attachment);
            } else {
                return new ApiResponse(MessageService.getMessage("FILE_TYPE"), false, "TYPE");
            }
        }
        return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
    }

    @PostMapping("/uploadId")
    public Integer saveToDbId(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());

        if (file != null) {
            Attachment attachment = new Attachment();
            attachment.setOrginal_name(file.getOriginalFilename());
            attachment.setSize(file.getSize());
            attachment.setName(file.getName());
            attachment.setContentType(file.getContentType());

            Attachment save = attachmentRepository.save(attachment);

            AttachmentContent attachmentContent = new AttachmentContent();
            attachmentContent.setAttachment(save);
            attachmentContent.setBytes(file.getBytes());

            attachmentContentRepository.save(attachmentContent);

            Optional<Attachment> attachment1 = attachmentRepository.maxId();

            return (attachment1.get().getId());
        }
        return (null);
    }

    // Multi upload
    @PostMapping("/uploadSystem")
    public boolean saveMultiple(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> fileNames = request.getFileNames();
        while (fileNames.hasNext()) {

            MultipartFile file = request.getFile(fileNames.next());
            if (file != null) {
                String originalFilename = file.getOriginalFilename();
                Attachment attachment = new Attachment();
                attachment.setOrginal_name(originalFilename);
                attachment.setSize(file.getSize());
                attachment.setContentType(file.getContentType());
                attachment.setName(file.getName());

                attachmentRepository.save(attachment);
                Path path = Paths.get(uploadDirectorys + "/" + originalFilename);
                Files.copy(file.getInputStream(), path);
            } else {
                return false;
            }
        }

        return true;
    }


    @GetMapping("/downloadSytem/{id}")
    public void getone(@PathVariable Integer id, HttpServletResponse response) throws IOException {

        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (optionalAttachment.isPresent()) {
            Attachment attachment = optionalAttachment.get();
            response.setHeader("Content-Disposition", "attachmentId; filename=\"" + attachment.getName() + "\"");

            response.setContentType(attachment.getContentType());
            FileInputStream fileInputStream = new FileInputStream(uploadDirectorys + "/" + attachment.getName());
            FileCopyUtils.copy(fileInputStream, response.getOutputStream());
        }
    }

    @GetMapping("/download/{id}")
    public void getFromDb(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (optionalAttachment.isPresent()) {
            Attachment attachment = optionalAttachment.get();
            Optional<AttachmentContent> optionalAttachmentContent = attachmentContentRepository.findByAttachment_Id(id);
            if (optionalAttachmentContent.isPresent()) {
                AttachmentContent attachmentContent = optionalAttachmentContent.get();
                response.setContentType(attachment.getContentType());
                response.setHeader("Content-Disposition", attachment.getOrginal_name() + "/:" + attachment.getSize());
                FileCopyUtils.copy(attachmentContent.getBytes(), response.getOutputStream());
            }

        }
    }

    @GetMapping("/download")
    public void getAll(HttpServletResponse response) throws IOException {
        List<Attachment> all = attachmentRepository.findAll();
        for (Attachment attachment : all) {
            List<AttachmentContent> all1 = attachmentContentRepository.findAll();
            for (AttachmentContent attachmentContent : all1) {
                response.setContentType(attachment.getContentType());
                response.setHeader("Content-Disposition", attachment.getOrginal_name() + "/:" + attachment.getSize());
                FileCopyUtils.copy(attachmentContent.getBytes(), response.getOutputStream());
            }
        }
    }

    @GetMapping("/info/{id}")
    public ApiResponse getOne(@PathVariable Integer id) {
        Optional<Attachment> byId = attachmentRepository.findById(id);
        return byId.map(attachment -> new ApiResponse("FOUND", true, attachment)).orElseGet(() -> new ApiResponse("NOT FOUND", false));
    }

    @GetMapping("/info")
    public ApiResponse getOne() {
        return new ApiResponse("FOUND", true, attachmentRepository.findAll());
    }
}


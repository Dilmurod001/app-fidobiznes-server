package com.dilmurod.service;

import com.dilmurod.component.MessageService;
import com.dilmurod.entity.DocSender;
import com.dilmurod.payload.ApiResponse;
import com.dilmurod.repository.DocSenderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class SenderService {
    private final DocSenderRepository docSenderRepository;

    public ApiResponse add(DocSender docSender) {
        try {
            if (docSender.getName() == null) return new ApiResponse(MessageService.getMessage("NOT_FOUND"), false);

            Optional<DocSender> byNameAndActive = docSenderRepository.findByNameAndActive(docSender.getName(), false);
            if (byNameAndActive.isPresent()) {
                DocSender sender1 = byNameAndActive.get();
                sender1.setActive(true);
                docSenderRepository.save(sender1);
                return new ApiResponse(MessageService.getMessage("SUCCESS_SAVE"), true);
            }

            boolean exists = docSenderRepository.existsByNameAndActive(docSender.getName(), true);
            if (exists) return new ApiResponse(MessageService.getMessage("ALREADY_EXISTS"), false);

            DocSender sender = new DocSender();
            sender.setName(docSender.getName());
            sender.setActive(true);

            docSenderRepository.save(sender);
            return new ApiResponse(MessageService.getMessage("SUCCESS_SAVE"), true);

        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }

    }

    public ApiResponse byId(Integer id) {
        try {
            Optional<DocSender> docSenderOptional = docSenderRepository.findByIdAndActive(id, true);
            return docSenderOptional.map(docSender -> new ApiResponse("Ok", true, docSender)).orElseGet(() -> new ApiResponse("Not found Sender !", false));

        } catch (Exception e) {
            return new ApiResponse("INTERNAL_SERVER_ERROR", false);
        }
    }

    public ApiResponse delete(Integer id) {
        try {
            boolean exists = docSenderRepository.existsByIdAndActive(id, true);
            if (!exists) return new ApiResponse(MessageService.getMessage("NOT_FOUND"), false);

            Optional<DocSender> byId = docSenderRepository.findById(id);
            byId.get().setActive(false);
            docSenderRepository.save(byId.get());
            return new ApiResponse(MessageService.getMessage("SUCCESS_DELETE"), true);

        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }
    }

    public ApiResponse edit(DocSender docSender, Integer id) {
        try {
            if (docSender.getName() == null) return new ApiResponse(MessageService.getMessage("NAME_REQUIRED"), false);

            Optional<DocSender> docSenderOptional = docSenderRepository.findByIdAndActive(id, true);
            if (!docSenderOptional.isPresent()) return new ApiResponse(MessageService.getMessage("NOT_FOUND"), false);

            Optional<DocSender> byNameAndActive = docSenderRepository.findByNameAndActive(docSender.getName(), false);
            if (byNameAndActive.isPresent()) {
                DocSender sender = byNameAndActive.get();
                sender.setActive(true);
                docSenderRepository.save(sender);

                docSenderOptional.get().setActive(false);
                docSenderRepository.save(docSenderOptional.get());
                return new ApiResponse(MessageService.getMessage("SUCCESS_EDIT"), true);
            }


            boolean exists = docSenderRepository.existsByNameAndActiveAndIdIsNot(docSender.getName(), true, id);
            if (exists) return new ApiResponse(MessageService.getMessage("ALREADY_EXISTS"), false);

            DocSender sender = docSenderOptional.get();
//            sender.setId(docSenderOptional.get().getId());
            sender.setName(docSender.getName());

            docSenderRepository.save(sender);
            return new ApiResponse(MessageService.getMessage("SUCCESS_EDIT"), true);

        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }
    }

    public boolean uniqueName(String name, Integer id) {
        try {
            boolean bool = false;
            if (id == 0) {
                bool = docSenderRepository.existsByNameAndActive(name, true);

            } else if (id > 0) {
                bool = docSenderRepository.existsByNameAndActiveAndIdIsNot(name, true, id);
            }
            return bool;

        } catch (Exception e) {
            return false;
        }
    }

}

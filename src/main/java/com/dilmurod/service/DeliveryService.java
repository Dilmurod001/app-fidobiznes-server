package com.dilmurod.service;

import com.dilmurod.component.MessageService;
import com.dilmurod.entity.DeliveryType;
import com.dilmurod.payload.ApiResponse;
import com.dilmurod.repository.DeliveryTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DeliveryService {
    private final DeliveryTypeRepository deliveryTypeRepository;

    public ApiResponse add(DeliveryType deliveryType) {
        try {
            if (deliveryType.getName() == null)
                return new ApiResponse(MessageService.getMessage("NAME_REQUIRED"), false);

            Optional<DeliveryType> byNameAndActive = deliveryTypeRepository.findByNameAndActive(deliveryType.getName(), false);
            if (byNameAndActive.isPresent()) {
                DeliveryType deliveryType1 = byNameAndActive.get();
                deliveryType1.setActive(true);
                deliveryTypeRepository.save(deliveryType1);
                return new ApiResponse(MessageService.getMessage("SUCCESS_SAVE"), true);
            }

            boolean exists = deliveryTypeRepository.existsByNameAndActive(deliveryType.getName(), true);
            if (exists) return new ApiResponse(MessageService.getMessage("ALREADY_EXISTS"), false);

            DeliveryType deliveryType1 = new DeliveryType();
            deliveryType1.setName(deliveryType.getName());
            deliveryType1.setActive(true);

            deliveryTypeRepository.save(deliveryType1);
            return new ApiResponse(MessageService.getMessage("SUCCESS_SAVE"), true);

        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }
    }

    public ApiResponse byId(Integer id) {

        try {
            Optional<DeliveryType> deliveryTypeOptional = deliveryTypeRepository.findByIdAndActive(id, true);
            return deliveryTypeOptional.map(deliveryType -> new ApiResponse("Ok", true, deliveryType)).orElseGet(() -> new ApiResponse("Not Found delivery type", false));

        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }
    }

    public ApiResponse delete(Integer id) {
        try {
            boolean exists = deliveryTypeRepository.existsByIdAndActive(id, true);
            if (!exists) return new ApiResponse(MessageService.getMessage("NOT_FOUND"), false);

            Optional<DeliveryType> byId = deliveryTypeRepository.findById(id);
            byId.get().setActive(false);
            deliveryTypeRepository.save(byId.get());
            return new ApiResponse(MessageService.getMessage("SUCCESS_DELETE"), true);

        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }
    }

    public ApiResponse edit(DeliveryType deliveryType, Integer id) {
        try {
            if (deliveryType.getName() == null)
                return new ApiResponse(MessageService.getMessage("NAME_REQUIRED"), false);

            Optional<DeliveryType> deliveryTypeOptional = deliveryTypeRepository.findByIdAndActive(id, true);
            if (!deliveryTypeOptional.isPresent()) return new ApiResponse(MessageService.getMessage("NOT_FOUND"), false);

            Optional<DeliveryType> byNameAndActive = deliveryTypeRepository.findByNameAndActive(deliveryType.getName(), false);
            if (byNameAndActive.isPresent()) {
                DeliveryType deliveryType1 = byNameAndActive.get();
                deliveryType1.setActive(true);
                deliveryTypeRepository.save(deliveryType1);

                deliveryTypeOptional.get().setActive(false);
                deliveryTypeRepository.save(deliveryTypeOptional.get());
                return new ApiResponse(MessageService.getMessage("SUCCESS_EDIT"), true);
            }

            boolean exists = deliveryTypeRepository.existsByNameAndActiveAndIdIsNot(deliveryType.getName(), true, id);
            if (exists) return new ApiResponse(MessageService.getMessage("ALREADY_EXISTS"), false);

            DeliveryType deliveryType1 = deliveryTypeOptional.get();
            deliveryType1.setName(deliveryType.getName());

            deliveryTypeRepository.save(deliveryType1);
            return new ApiResponse(MessageService.getMessage("SUCCESS_EDIT"), true);

        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }

    }

    public boolean uniqueName(String name, Integer id) {
        try {
            boolean bool = false;
            if (id == 0) {
                bool = deliveryTypeRepository.existsByNameAndActive(name, true);

            } else if (id > 0) {

                bool = deliveryTypeRepository.existsByNameAndActiveAndIdIsNot(name, true, id);
            }
            return bool;

        } catch (Exception e) {
            return false;
        }
    }

}

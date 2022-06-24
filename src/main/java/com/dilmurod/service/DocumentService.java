package com.dilmurod.service;

import com.dilmurod.component.MessageService;
import com.dilmurod.entity.Attachment;
import com.dilmurod.entity.DeliveryType;
import com.dilmurod.entity.DocSender;
import com.dilmurod.entity.FormDocument;
import com.dilmurod.payload.ApiResponse;
import com.dilmurod.payload.FormDocSort;
import com.dilmurod.payload.FormDocumentDto;
import com.dilmurod.repository.AttachmentRepository;
import com.dilmurod.repository.DeliveryTypeRepository;
import com.dilmurod.repository.DocSenderRepository;
import com.dilmurod.repository.FormDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DocumentService {
    private final FormDocumentRepository formDocumentRepository;
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final DocSenderRepository docSenderRepository;
    private final AttachmentRepository attachmentRepository;
    private final JdbcTemplate jdbcTemplate;

    RowMapper<FormDocSort> rowMapper = (rs, rowNum) -> {
        FormDocSort formDoc = new FormDocSort();
        formDoc.setId(rs.getInt("id"));
        formDoc.setAccess(rs.getBoolean("access"));
        formDoc.setCardControl(rs.getBoolean("card_control"));
        formDoc.setRegNum(rs.getString("reg_num"));
        formDoc.setExpireDate(rs.getDate("expire_date"));
        formDoc.setRegDate(rs.getDate("reg_date"));
        formDoc.setSendDate(rs.getDate("send_date"));
        formDoc.setSendDocNum(rs.getString("send_doc_num"));
        formDoc.setDescriptionReference(rs.getString("description_reference"));
        formDoc.setTheme(rs.getString("theme"));
        formDoc.setDeliveryType(rs.getString("name"));
        formDoc.setSenderName(rs.getString(2));
        formDoc.setAttachmentId(rs.getInt("attachment_id"));
        return formDoc;
    };

    public ApiResponse add(FormDocumentDto documentDto) {
        try {
            FormDocument formDocument = new FormDocument();

            if (documentDto.getAttachmentId() != null && documentDto.getAttachmentId() != 0) {

                Optional<Attachment> attachmentOptional = attachmentRepository.findById(documentDto.getAttachmentId());
                if (!attachmentOptional.isPresent())
                    return new ApiResponse(MessageService.getMessage("NOT_FOUND"), false);
                formDocument.setAttachment(attachmentOptional.get());
            }

            Optional<DeliveryType> optionalDeliveryType = deliveryTypeRepository.findById(documentDto.getDeliveryTypeId());
            if (!optionalDeliveryType.isPresent())
                return new ApiResponse(MessageService.getMessage("NOT_FOUND"), false, "Delivery Type");

            Optional<DocSender> docSenderOptional = docSenderRepository.findById(documentDto.getDocSenderId());
            if (!docSenderOptional.isPresent())
                return new ApiResponse(MessageService.getMessage("NOT_FOUND"), false, "Doc Sender");

            if (documentDto.getExpireDate() != null && documentDto.getExpireDate().getTime() < documentDto.getRegDate().getTime()) {
                return new ApiResponse(MessageService.getMessage("DUE_DATE"), false);
            }

            boolean exists = formDocumentRepository.existsByRegNum(documentDto.getRegNum());
            if (exists) return new ApiResponse(MessageService.getMessage("ALREADY_EXISTS"), false);

            formDocument.setAccess(documentDto.getAccess());
            formDocument.setDocSender(docSenderOptional.get());
            formDocument.setDeliveryType(optionalDeliveryType.get());
            formDocument.setCardControl(documentDto.getCardControl());
            formDocument.setRegDate(new Date(System.currentTimeMillis()));
            formDocument.setRegNum(documentDto.getRegNum());
            formDocument.setExpireDate(documentDto.getExpireDate());
            formDocument.setSendDate(documentDto.getSendDate());
            formDocument.setSendDocNum(documentDto.getSendDocNum());
            formDocument.setTheme(documentDto.getTheme());
            formDocument.setDescriptionReference(documentDto.getDescriptionReference());

            formDocumentRepository.save(formDocument);
            return new ApiResponse(MessageService.getMessage("SUCCESS_SAVE"), true, formDocument.getId());

        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }
    }

    public ApiResponse getAll() {
        try {
            List<FormDocSort> formDocSortList = new ArrayList<>();
            for (FormDocument formDocument : formDocumentRepository.findAll(Sort.by("regDate").descending())) {
                FormDocSort formDocSort = new FormDocSort();
                if (formDocument.getDescriptionReference() != null) {
                    formDocSort.setDescriptionReference(formDocument.getDescriptionReference());
                }
                formDocSort.setAccess(formDocument.getAccess());
                formDocSort.setCardControl(formDocument.getCardControl());
                if (formDocument.getAttachment() != null) {
                    formDocSort.setAttachmentId(formDocument.getAttachment().getId());
                }
                formDocSort.setSendDate(formDocument.getSendDate());
                formDocSort.setDeliveryType(formDocument.getDeliveryType().getName());
                formDocSort.setSendDocNum(formDocument.getSendDocNum());
                formDocSort.setRegDate(formDocument.getRegDate());
                formDocSort.setId(formDocument.getId());
                formDocSort.setTheme(formDocument.getTheme());
                formDocSort.setSenderName(formDocument.getDocSender().getName());
                formDocSort.setRegNum(formDocument.getRegNum());
                if (formDocument.getExpireDate() != null) {
                    formDocSort.setExpireDate(formDocument.getExpireDate());
                }

                formDocSortList.add(formDocSort);
            }
            return new ApiResponse("Ok !", true, formDocSortList);


        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }
    }

    public ApiResponse edit(FormDocumentDto documentDto, Integer id) {
        try {
            Optional<FormDocument> formDocumentOptional = formDocumentRepository.findById(id);
            if (formDocumentOptional.isPresent())
                return new ApiResponse(MessageService.getMessage("NOT_FOUND"), false, "Document");

            FormDocument formDocument = formDocumentOptional.get();

            if (documentDto.getAttachmentId() != null && documentDto.getAttachmentId() != 0) {

                Optional<Attachment> attachmentOptional = attachmentRepository.findById(documentDto.getAttachmentId());
                if (!attachmentOptional.isPresent())
                    return new ApiResponse(MessageService.getMessage("NOT_FOUND"), false, "Attachment");
                formDocument.setAttachment(attachmentOptional.get());
            }

            Optional<DeliveryType> optionalDeliveryType = deliveryTypeRepository.findById(documentDto.getDeliveryTypeId());
            if (!optionalDeliveryType.isPresent())
                return new ApiResponse(MessageService.getMessage("NOT_FOUND"), false, "DeliveryType");

            Optional<DocSender> docSenderOptional = docSenderRepository.findById(documentDto.getDocSenderId());
            if (!docSenderOptional.isPresent())
                return new ApiResponse(MessageService.getMessage("NOT_FOUND"), false, "Correspondent");

            if (documentDto.getExpireDate() != null && documentDto.getExpireDate().getTime() < documentDto.getRegDate().getTime()) {
                return new ApiResponse(MessageService.getMessage("DUE_DATE"), false);
            }

            boolean exists = formDocumentRepository.existsByRegNumAndIdIsNot(documentDto.getRegNum(), id);
            if (exists) return new ApiResponse(MessageService.getMessage("ALREADY_EXISTS"), false);

            formDocument.setAccess(documentDto.getAccess());
            formDocument.setDocSender(docSenderOptional.get());
            formDocument.setDeliveryType(optionalDeliveryType.get());
            formDocument.setCardControl(documentDto.getCardControl());
//            formDocument.setRegDate(new java.util.Date(System.currentTimeMillis()));
            formDocument.setRegNum(documentDto.getRegNum());
            formDocument.setExpireDate(documentDto.getExpireDate());
            formDocument.setSendDate(documentDto.getSendDate());
            formDocument.setSendDocNum(documentDto.getSendDocNum());
            formDocument.setTheme(documentDto.getTheme());
            formDocument.setDescriptionReference(documentDto.getDescriptionReference());

            formDocumentRepository.save(formDocument);
            return new ApiResponse(MessageService.getMessage("SUCCESS_EDIT"), true);

        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }
    }

    public ApiResponse byId(Integer id) {
        try {
            Optional<FormDocument> formDocumentOptional = formDocumentRepository.findById(id);
            return formDocumentOptional.map(formDocument -> new ApiResponse("Ok !", true, formDocument)).orElseGet(() -> new ApiResponse(MessageService.getMessage("NOT_FOUND"), false));

        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }
    }

    public ApiResponse delete(Integer id) {
        try {
            boolean exists = formDocumentRepository.existsById(id);
            if (!exists) return new ApiResponse(MessageService.getMessage("NOT_FOUND"), false);

            formDocumentRepository.deleteById(id);
            return new ApiResponse(MessageService.getMessage("SUCCESS_DELETE"), true);

        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }
    }

    public ApiResponse sort(Boolean isSort, String name) {
        try {
            String query = "select d.name , doc.name,f.attachment_id,f.access,f.card_control,f.reg_num,f.expire_date,f.reg_date,f.send_date,f.send_doc_num,f.description_reference,f.theme, f.id from form_document f join delivery_type d  on f.delivery_type_id = d.id join doc_sender doc on f.doc_sender_id = doc.id";
            List<FormDocument> formDocumentList = new ArrayList<>();
            List<FormDocSort> formDocSortList = new ArrayList<>();

            if (name.equals("deliveryType") && isSort.equals(false)) {
                String sql = query + " order by d.name desc";
                List<FormDocSort> formDocSorts = jdbcTemplate.query(sql, rowMapper);

                for (FormDocSort docSort : formDocSorts) {
                    FormDocument formDocument1 = new FormDocument();

                    if (docSort.getAttachmentId() != 0) {
                        Optional<Attachment> optionalAttachment = attachmentRepository.findById(docSort.getAttachmentId());
                        formDocument1.setAttachment(optionalAttachment.get());
                    }
                    Optional<DeliveryType> optionalDeliveryType = deliveryTypeRepository.findByName(docSort.getDeliveryType());
                    Optional<DocSender> optionalDocSender = docSenderRepository.findByName(docSort.getSenderName());

                    formDocument1.setDeliveryType(optionalDeliveryType.get());
                    formDocument1.setDocSender(optionalDocSender.get());
                    formDocument1.setTheme(docSort.getTheme());
                    formDocument1.setSendDocNum(docSort.getSendDocNum());
                    formDocument1.setAccess(docSort.getAccess());
                    formDocument1.setDescriptionReference(docSort.getDescriptionReference());
                    formDocument1.setSendDate(docSort.getSendDate());
                    if (docSort.getExpireDate() != null) {
                        formDocument1.setExpireDate(docSort.getExpireDate());
                    }
                    formDocument1.setRegNum(docSort.getRegNum());
                    formDocument1.setRegDate(docSort.getRegDate());
                    formDocument1.setCardControl(docSort.getCardControl());
                    formDocument1.setId(docSort.getId());

                    formDocumentList.add(formDocument1);
                }
                return new ApiResponse("Ok !", true, formDocSorts);

            } else if (name.equals("deliveryType") && isSort.equals(true)) {
                String sql = query + " order by d.name";
                List<FormDocSort> formDocSorts = jdbcTemplate.query(sql, rowMapper);

                for (FormDocSort docSort : formDocSorts) {
                    FormDocument formDocument1 = new FormDocument();

                    if (docSort.getAttachmentId() != 0) {
                        Optional<Attachment> optionalAttachment = attachmentRepository.findById(docSort.getAttachmentId());
                        formDocument1.setAttachment(optionalAttachment.get());
                    }
                    Optional<DeliveryType> optionalDeliveryType = deliveryTypeRepository.findByName(docSort.getDeliveryType());
                    Optional<DocSender> optionalDocSender = docSenderRepository.findByName(docSort.getSenderName());

                    formDocument1.setDeliveryType(optionalDeliveryType.get());
                    formDocument1.setDocSender(optionalDocSender.get());
                    formDocument1.setTheme(docSort.getTheme());
                    formDocument1.setSendDocNum(docSort.getSendDocNum());
                    formDocument1.setAccess(docSort.getAccess());
                    formDocument1.setDescriptionReference(docSort.getDescriptionReference());
                    formDocument1.setSendDate(docSort.getSendDate());
                    if (docSort.getExpireDate() != null) {
                        formDocument1.setExpireDate(docSort.getExpireDate());
                    }
                    formDocument1.setRegNum(docSort.getRegNum());
                    formDocument1.setRegDate(docSort.getRegDate());
                    formDocument1.setCardControl(docSort.getCardControl());
                    formDocument1.setId(docSort.getId());

                    formDocumentList.add(formDocument1);
                }
                return new ApiResponse("Ok !", true, formDocSorts);

            } else if (name.equals("docSender") && isSort.equals(true)) {
                String sql = query + " order by doc.name";
                List<FormDocSort> formDocSorts = jdbcTemplate.query(sql, rowMapper);

                for (FormDocSort docSort : formDocSorts) {
                    FormDocument formDocument1 = new FormDocument();

                    if (docSort.getAttachmentId() != 0) {
                        Optional<Attachment> optionalAttachment = attachmentRepository.findById(docSort.getAttachmentId());
                        formDocument1.setAttachment(optionalAttachment.get());
                    }
                    Optional<DeliveryType> optionalDeliveryType = deliveryTypeRepository.findByName(docSort.getDeliveryType());
                    Optional<DocSender> optionalDocSender = docSenderRepository.findByName(docSort.getSenderName());

                    formDocument1.setDeliveryType(optionalDeliveryType.get());
                    formDocument1.setDocSender(optionalDocSender.get());
                    formDocument1.setTheme(docSort.getTheme());
                    formDocument1.setSendDocNum(docSort.getSendDocNum());
                    formDocument1.setAccess(docSort.getAccess());
                    formDocument1.setDescriptionReference(docSort.getDescriptionReference());
                    formDocument1.setSendDate(docSort.getSendDate());
                    if (docSort.getExpireDate() != null) {
                        formDocument1.setExpireDate(docSort.getExpireDate());
                    }
                    formDocument1.setRegNum(docSort.getRegNum());
                    formDocument1.setRegDate(docSort.getRegDate());
                    formDocument1.setCardControl(docSort.getCardControl());
                    formDocument1.setId(docSort.getId());

                    formDocumentList.add(formDocument1);
                }
                return new ApiResponse("Ok !", true, formDocSorts);

            } else if (name.equals("docSender") && isSort.equals(false)) {
                String sql = query + " order by doc.name desc ";
                List<FormDocSort> formDocSorts = jdbcTemplate.query(sql, rowMapper);

                for (FormDocSort docSort : formDocSorts) {
                    FormDocument formDocument1 = new FormDocument();

                    if (docSort.getAttachmentId() != 0) {
                        Optional<Attachment> optionalAttachment = attachmentRepository.findById(docSort.getAttachmentId());
                        formDocument1.setAttachment(optionalAttachment.get());
                    }
                    Optional<DeliveryType> optionalDeliveryType = deliveryTypeRepository.findByName(docSort.getDeliveryType());
                    Optional<DocSender> optionalDocSender = docSenderRepository.findByName(docSort.getSenderName());

                    formDocument1.setDeliveryType(optionalDeliveryType.get());
                    formDocument1.setDocSender(optionalDocSender.get());
                    formDocument1.setTheme(docSort.getTheme());
                    formDocument1.setSendDocNum(docSort.getSendDocNum());
                    formDocument1.setAccess(docSort.getAccess());
                    formDocument1.setDescriptionReference(docSort.getDescriptionReference());
                    formDocument1.setSendDate(docSort.getSendDate());
                    if (docSort.getExpireDate() != null) {
                        formDocument1.setExpireDate(docSort.getExpireDate());
                    }
                    formDocument1.setRegNum(docSort.getRegNum());
                    formDocument1.setRegDate(docSort.getRegDate());
                    formDocument1.setCardControl(docSort.getCardControl());
                    formDocument1.setId(docSort.getId());

                    formDocumentList.add(formDocument1);
                }
                return new ApiResponse("Ok !", true, formDocSorts);
            }

            if (isSort == null) {
                for (FormDocument formDocument : formDocumentRepository.findAll(Sort.by("regDate").descending())) {
                    FormDocSort formDocSort = new FormDocSort();
                    if (formDocument.getDescriptionReference() != null) {
                        formDocSort.setDescriptionReference(formDocument.getDescriptionReference());
                    }
                    formDocSort.setAccess(formDocument.getAccess());
                    formDocSort.setCardControl(formDocument.getCardControl());
                    if (formDocument.getAttachment() != null) {
                        formDocSort.setAttachmentId(formDocument.getAttachment().getId());
                    }
                    formDocSort.setSendDate(formDocument.getSendDate());
                    formDocSort.setDeliveryType(formDocument.getDeliveryType().getName());
                    formDocSort.setSendDocNum(formDocument.getSendDocNum());
                    formDocSort.setRegDate(formDocument.getRegDate());
                    formDocSort.setId(formDocument.getId());
                    formDocSort.setTheme(formDocument.getTheme());
                    formDocSort.setSenderName(formDocument.getDocSender().getName());
                    formDocSort.setRegNum(formDocument.getRegNum());
                    if (formDocument.getExpireDate() != null) {
                        formDocSort.setExpireDate(formDocument.getExpireDate());
                    }

                    formDocSortList.add(formDocSort);
                }
                return new ApiResponse("Ok !", true, formDocSortList);
            }

            if (isSort.equals(true)) {
                formDocumentList = formDocumentRepository.findAll(Sort.by(name).ascending());
                for (FormDocument formDocument : formDocumentRepository.findAll(Sort.by(name).ascending())) {
                    FormDocSort formDocSort = new FormDocSort();
                    if (formDocument.getDescriptionReference() != null) {
                        formDocSort.setDescriptionReference(formDocument.getDescriptionReference());
                    }
                    formDocSort.setAccess(formDocument.getAccess());
                    formDocSort.setCardControl(formDocument.getCardControl());
                    if (formDocument.getAttachment() != null) {
                        formDocSort.setAttachmentId(formDocument.getAttachment().getId());
                    }
                    formDocSort.setSendDate(formDocument.getSendDate());
                    formDocSort.setDeliveryType(formDocument.getDeliveryType().getName());
                    formDocSort.setSendDocNum(formDocument.getSendDocNum());
                    formDocSort.setRegDate(formDocument.getRegDate());
                    formDocSort.setId(formDocument.getId());
                    formDocSort.setTheme(formDocument.getTheme());
                    formDocSort.setSenderName(formDocument.getDocSender().getName());
                    formDocSort.setRegNum(formDocument.getRegNum());
                    if (formDocument.getExpireDate() != null) {
                        formDocSort.setExpireDate(formDocument.getExpireDate());
                    }

                    formDocSortList.add(formDocSort);
                }
                return new ApiResponse("Ok !", true, formDocSortList);

            } else if (isSort.equals(false)) {
//                formDocumentList = formDocumentRepository.findAll(Sort.by(name).descending());
                for (FormDocument formDocument : formDocumentRepository.findAll(Sort.by(name).descending())) {
                    FormDocSort formDocSort = new FormDocSort();
                    if (formDocument.getDescriptionReference() != null) {
                        formDocSort.setDescriptionReference(formDocument.getDescriptionReference());
                    }
                    formDocSort.setAccess(formDocument.getAccess());
                    formDocSort.setCardControl(formDocument.getCardControl());
                    if (formDocument.getAttachment() != null) {
                        formDocSort.setAttachmentId(formDocument.getAttachment().getId());
                    }
                    formDocSort.setSendDate(formDocument.getSendDate());
                    formDocSort.setDeliveryType(formDocument.getDeliveryType().getName());
                    formDocSort.setSendDocNum(formDocument.getSendDocNum());
                    formDocSort.setRegDate(formDocument.getRegDate());
                    formDocSort.setId(formDocument.getId());
                    formDocSort.setTheme(formDocument.getTheme());
                    formDocSort.setSenderName(formDocument.getDocSender().getName());
                    formDocSort.setRegNum(formDocument.getRegNum());
                    if (formDocument.getExpireDate() != null) {
                        formDocSort.setExpireDate(formDocument.getExpireDate());
                    }

                    formDocSortList.add(formDocSort);
                }
            }

            return new ApiResponse("Ok !", true, formDocSortList);

        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }
    }

    public boolean uniqueRegNum(String num, Integer id) {
        try {
            boolean bool = false;
            if (id == 0) {
                bool = formDocumentRepository.existsByRegNum(num);

            } else if (id > 0) {

                bool = formDocumentRepository.existsByRegNumAndIdIsNot(num, id);
            }
            return bool;

        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse filter(FormDocSort filterDto) {
        try {
            List<FormDocSort> formDocSorts = new ArrayList<>();

            if (filterDto != null) {
                String baseOperator = "and";
                StringBuilder query = new StringBuilder()
                        .append("select d.name , doc.name,f.attachment_id,f.access,f.card_control,f.reg_num,f.expire_date,f.reg_date,f.send_date,f.send_doc_num,f.description_reference,f.theme, f.id from form_document f join delivery_type d  on f.delivery_type_id = d.id join doc_sender doc on f.doc_sender_id = doc.id");

                query.append(" where ");

                if (filterDto.getRegNum() != null) {
                    query
                            .append(" f.reg_num ilike '")
                            .append(filterDto.getRegNum())
                            .append("%' ")
                            .append(baseOperator);
                }
                if (filterDto.getRegDate() != null && filterDto.getRegDateEnd() != null) {
                    query
                            .append(" f.reg_date between '")
                            .append(filterDto.getRegDate())
                            .append("' and '")
                            .append(filterDto.getRegDateEnd())
                            .append("' ")
                            .append(baseOperator);
                }
                if (filterDto.getTheme() != null) {
                    query
                            .append(" f.theme ilike '")
                            .append(filterDto.getTheme())
                            .append("%' ")
                            .append(baseOperator);
                }
                if (filterDto.getSendDate() != null && filterDto.getSendDateEnd() != null) {
                    query.append(" f.send_date between '")
                            .append(filterDto.getSendDate())
                            .append("' and '")
                            .append(filterDto.getSendDateEnd())
                            .append("' ")
                            .append(baseOperator);
                }
                if (filterDto.getCorrespondentId() != null) {
                    query.append(" f.doc_sender_id=")
                            .append(filterDto.getCorrespondentId()).append(" ")
                            .append(baseOperator);

                }
                if (filterDto.getDeliveryTypeId() != null) {
                    query.append(" f.delivery_type_id=")
                            .append(filterDto.getDeliveryTypeId()).append(" ")
                            .append(baseOperator);
                }
                if (filterDto.getDescriptionReference() != null && !filterDto.getDescriptionReference().isEmpty()) {
                    query.append(" f.description_reference ilike '")
                            .append(filterDto.getDescriptionReference())
                            .append("%' ")
                            .append(baseOperator);
                }
                if (filterDto.getSendDocNum() != null) {
                    query.append(" f.send_doc_num ilike '")
                            .append(filterDto.getSendDocNum())
                            .append("%' ")
                            .append(baseOperator);

                }
                String substring = query.substring(0, query.length() - baseOperator.length());
                substring = substring + ";";
//                List<FormDocSort> query1 = jdbcTemplate.query(substring, rowMapper);
                formDocSorts = jdbcTemplate.query(substring, rowMapper);
            }
            return new ApiResponse("Ok !", true, formDocSorts);

        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }
    }
}

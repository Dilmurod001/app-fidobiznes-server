package com.dilmurod.service;

import com.dilmurod.component.MessageService;
import com.dilmurod.entity.DeliveryType;
import com.dilmurod.entity.DocSender;
import com.dilmurod.entity.FormDocument;
import com.dilmurod.payload.ApiResponse;
import com.dilmurod.repository.DeliveryTypeRepository;
import com.dilmurod.repository.DocSenderRepository;
import com.dilmurod.repository.FormDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final FormDocumentRepository formDocumentRepository;
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final DocSenderRepository docSenderRepository;

    public ApiResponse cbElectronPacta(String docSender) {
        try {
            List<FormDocument> formDocumentList = new ArrayList<>();
            switch (docSender) {
                case "CB":
                    LocalDate todayDate = LocalDate.now();
                    LocalDate firstDayOfMonth = todayDate.withDayOfMonth(1);
                    Date date = Date.valueOf(firstDayOfMonth);
                    Date date2 = new Date(System.currentTimeMillis());

                    formDocumentList = formDocumentRepository.findByDeliveryType_IdAndDocSender_IdAndRegDateBetween(2, 3, date, date2);
                    break;
                case "GNI":
                    LocalDate localDate = LocalDate.now();
                    int year = localDate.getYear();
                    String sDate = year + "/01/01";
                    String sDate2 = year + "/03/31";

                    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");

                    java.util.Date date1 = formatter1.parse(sDate);
                    java.util.Date date02 = formatter1.parse(sDate2);

                    Date date3 = new Date(date1.getTime());
                    Date date4 = new Date(date02.getTime());

                    Optional<DeliveryType> optionalDeliveryType = deliveryTypeRepository.findById(1);
                    Optional<DocSender> optionalDocSender = docSenderRepository.findById(1);

                    formDocumentList = formDocumentRepository.findByDeliveryTypeIsNotAndDocSenderIsNotAndRegDateBetween(optionalDeliveryType.get(), optionalDocSender.get(), date3, date4);
                    break;
                case "TSJ":
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MONTH, -1);

                    int lastDayMonth = calendar.getActualMaximum(Calendar.DATE);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int yearOf = calendar.get(Calendar.YEAR);

                    String strDate = "" + yearOf + '/' + month + "/01";
                    String strDate2 = "" + yearOf + '/' + month + '/' + lastDayMonth;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

                    java.util.Date date001 = dateFormat.parse(strDate);
                    java.util.Date date002 = dateFormat.parse(strDate2);

                    Date date01 = new Date(date001.getTime());
                    Date date5 = new Date(date002.getTime());

                    formDocumentList = formDocumentRepository.findByDocSender_IdAndThemeNotLikeAndRegDateBetween(2, "%kredit%", date01, date5);
                    break;
                default:
                    return new ApiResponse(MessageService.getMessage("NOT_FOUND"), false);
            }
            return new ApiResponse("Ok !", true, formDocumentList);

        } catch (Exception e) {
            return new ApiResponse(MessageService.getMessage("INTERNAL_SERVER_ERROR"), false);
        }

    }
}

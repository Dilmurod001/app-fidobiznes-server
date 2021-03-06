package com.dilmurod.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterMark {

    private String deliveryType = "";
    private String senderName = "";
    private String expireDate = "";
    private String regDate = "";
    private String sendDate = "";
    private String regNum = "";
    private String sendDocNum = "";
    private String descriptionReference = "";
    private String theme = "";

}

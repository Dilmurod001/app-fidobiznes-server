package com.dilmurod.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormDocSort {
    private Integer attachmentId;
    private String deliveryType;
    private String senderName;
    private Integer id;
    private Boolean access;
    private Date expireDate;
    private java.util.Date regDate;
    private String regDateStr;
    private java.util.Date regDateEnd;
    private Date sendDate;
    private String sendDateStr;
    private Date sendDateEnd;
    private Boolean cardControl;
    private String regNum;
    private String sendDocNum;
    private String descriptionReference;
    private String theme;

    private Integer correspondentId;
    private Integer deliveryTypeId;

}

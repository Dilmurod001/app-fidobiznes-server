package com.dilmurod.payload;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
public class FormDocumentDto {

    private Boolean access;
    private Date expireDate;
    private Boolean cardControl;

    @NotNull(message = "regData empty !")
    private Date regDate;

    @NotNull(message = "sendDate empty !")
    private Date sendDate;

    @NotNull(message = "regNum empty !")
    @Length(max = 20)
    private Long regNum;

    @NotNull(message = "sendDocNum empty !")
    @Length(max = 20)
    private Long sendDocNum;

    @Length(max = 1000)
    private String descriptionReference;

    @NotNull(message = "theme empty !")
    @Length(max = 100)
    private String theme;

    @NotNull(message = "deliveryType empty !")
    private Integer deliveryTypeId;

    @NotNull(message = "docSender empty !")
    private Integer docSenderId;

    private Integer attachmentId;
}

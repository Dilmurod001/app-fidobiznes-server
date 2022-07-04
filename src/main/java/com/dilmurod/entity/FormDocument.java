package com.dilmurod.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class FormDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean access;
    private Date expireDate;

    @Column(nullable = false)
    private java.util.Date regDate;

    private String regDateStr;

    @Column(nullable = false)
    private Date sendDate;

    private String sendDateStr;

    private Boolean cardControl;

    @Column(length = 20, nullable = false, unique = true)
    private String regNum;

    @Column(length = 20, nullable = false)
    private String sendDocNum;

    @Column(length = 1000)
    private String descriptionReference;

    @Column(length = 100, nullable = false)
    private String theme;

    @JoinColumn(nullable = false)
    @ManyToOne
    private DeliveryType deliveryType;

    @JoinColumn(nullable = false)
    @ManyToOne
    private DocSender docSender;

    @ManyToOne
    private Attachment attachment;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FormDocument that = (FormDocument) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

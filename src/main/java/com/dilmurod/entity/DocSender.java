package com.dilmurod.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class DocSender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    private boolean active;

    public DocSender(Integer id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DocSender sender = (DocSender) o;
        return id != null && Objects.equals(id, sender.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

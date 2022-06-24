package com.dilmurod.repository;

import com.dilmurod.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    @Query( value = "select * from Attachment order by id desc limit 1", nativeQuery = true)
    Optional<Attachment> maxId();
}

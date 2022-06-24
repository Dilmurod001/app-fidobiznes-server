package com.dilmurod.repository;

import com.dilmurod.entity.DocSender;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocSenderRepository extends JpaRepository<DocSender, Integer> {
    boolean existsByNameAndActive(String name, boolean active);

    boolean existsByNameAndActiveAndIdIsNot(String name, boolean active, Integer id);

    Optional<DocSender> findByName(String name);

    Optional<DocSender> findByNameAndActive(String name, boolean active);

    Optional<DocSender> findByIdAndActive(Integer id, boolean active);

    boolean existsByIdAndActive(Integer id, boolean active);

    List<DocSender> findAllByActive(boolean active);
}

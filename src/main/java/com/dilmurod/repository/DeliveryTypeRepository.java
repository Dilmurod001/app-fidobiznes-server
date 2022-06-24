package com.dilmurod.repository;

import com.dilmurod.entity.DeliveryType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryTypeRepository extends JpaRepository<DeliveryType, Integer> {
    boolean existsByNameAndActive(String name, boolean active);

    boolean existsByNameAndActiveAndIdIsNot(String name, boolean active, Integer id);

    Optional<DeliveryType> findByName(String name);

    Optional<DeliveryType> findByNameAndActive(String name, boolean active);

    Optional<DeliveryType> findByIdAndActive(Integer id, boolean active);

    boolean existsByIdAndActive(Integer id, boolean active);

    List<DeliveryType> findAllByActive(boolean active);
}

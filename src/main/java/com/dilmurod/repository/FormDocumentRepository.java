package com.dilmurod.repository;

import com.dilmurod.entity.DeliveryType;
import com.dilmurod.entity.DocSender;
import com.dilmurod.entity.FormDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface FormDocumentRepository extends JpaRepository<FormDocument, Integer> {
//    findAllByRoleAndFullNameContainingIgnoreCaseOrRoleAndRegions_LocationNameContainingIgnoreCaseOrRoleAndRegions_RegionNameContainingIgnoreCaseOrRoleAndNameShopContainingIgnoreCaseOrRoleAndShopOrienterContainingIgnoreCase

    @Query("select f from FormDocument f where f.deliveryType.id = :deliveryType_id and f.docSender.id = :docSender_id and f.regDate between :regDate and :regDate2")
    List<FormDocument> findByDeliveryType_IdAndDocSender_IdAndRegDateBetween(@Param("deliveryType_id") Integer deliveryType_id, @Param("docSender_id") Integer docSender_id, @Param("regDate") Date regDate, @Param("regDate2") Date regDate2);

    @Query("select f from FormDocument f where f.deliveryType <> :deliveryType and f.docSender <> :docSender and f.regDate between :regDate and :regDate2")
    List<FormDocument> findByDeliveryTypeIsNotAndDocSenderIsNotAndRegDateBetween(@Param("deliveryType") DeliveryType deliveryType, @Param("docSender") DocSender docSender, @Param("regDate") Date regDate, @Param("regDate2") Date regDate2);

    @Query("select f from FormDocument f where f.docSender.id = :docSender_id and f.theme not like :theme and f.regDate between :regDate and :regDate2")
    List<FormDocument> findByDocSender_IdAndThemeNotLikeAndRegDateBetween(@Param("docSender_id") Integer docSender_id, @Param("theme") String theme, @Param("regDate") Date regDate, @Param("regDate2") Date regDate2);

    boolean existsByRegNum(Long regNum);

    boolean existsByRegNumAndIdIsNot(Long regNum, Integer id);

    List<FormDocument> findAllByThemeContainingIgnoreCaseOrRegNumContainingOrSendDocNumContainingOrRegDateStrContainingIgnoreCaseOrSendDateStrContainingIgnoreCaseOrDocSender_NameContainingIgnoreCaseAndDocSender_ActiveOrDeliveryType_NameContainingIgnoreCaseAndDeliveryType_ActiveOrderByRegDateDesc(String theme, Long regNum, Long sendDocNum, String regDateStr, String sendDateStr, String docSender_name, boolean docSender_active, String deliveryType_name, boolean deliveryType_active);

}


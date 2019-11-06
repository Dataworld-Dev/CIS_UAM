package com.dw.ngms.cis.im.repository;

import com.dw.ngms.cis.im.entity.BulkSubTypes;
import com.dw.ngms.cis.im.entity.BulkTypes;
import com.dw.ngms.cis.im.entity.CostSubCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Created by swaroop on 2019/04/16.
 */

@Repository
public interface BulkSubRepository extends JpaRepository<BulkSubTypes, UUID> {


    @Query(value = "SELECT SUBBULKTYPES_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getSubBulkID();

    @Query("SELECT u FROM BulkSubTypes u WHERE u.bulkTypeCode = :bulkTypeCode and isActive = 'Y' order by u.subBulkName")
    List<BulkSubTypes> getBulkRequestSubTypesByTypeCode(@Param("bulkTypeCode") String bulkTypeCode);



}



package com.dw.ngms.cis.im.repository;

import com.dw.ngms.cis.im.entity.BulkTypes;
import com.dw.ngms.cis.im.entity.CostCategories;
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
public interface BulkTypeRepository extends JpaRepository<BulkTypes, UUID> {

    @Query(value = "SELECT BULKTYPES_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getBulkTypeId();


    @Query("SELECT u FROM BulkTypes u WHERE u.isActive = 'Y' order by u.bulkTypeName")
    List<BulkTypes> findActiveBulkTypes();

    @Query("SELECT u FROM CostCategories u WHERE u.categoryCode = :categoryCode")
    CostCategories findByCategoryCode(@Param("categoryCode") String categoryCode);


}

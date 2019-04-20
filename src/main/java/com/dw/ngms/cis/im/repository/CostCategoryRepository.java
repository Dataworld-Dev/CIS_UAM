package com.dw.ngms.cis.im.repository;

import com.dw.ngms.cis.im.entity.CostCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created by swaroop on 2019/04/16.
 */

@Repository
public interface CostCategoryRepository extends JpaRepository<CostCategories, UUID> {

    @Query(value = "SELECT COSTCATEGORY_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getCategoryId();

}

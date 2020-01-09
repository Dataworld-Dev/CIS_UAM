package com.dw.ngms.cis.im.repository;

import com.dw.ngms.cis.im.entity.CostCategories;
import com.dw.ngms.cis.uam.entity.ExternalUserAssistant;
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
public interface CostCategoryRepository extends JpaRepository<CostCategories, UUID> {

    @Query(value = "SELECT COSTCATEGORY_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getCategoryId();


    @Query("SELECT u FROM CostCategories u WHERE u.isActive = 'Y' order by categoryName asc")
    List<CostCategories> findActiveCategories();

    @Query("SELECT u FROM CostCategories u WHERE u.categoryCode = :categoryCode")
    CostCategories findByCategoryCode(@Param("categoryCode") String categoryCode);
    
    @Query("SELECT u FROM CostCategories u WHERE u.categoryName = :categoryName")
    CostCategories findByCategoryName(@Param("categoryName") String categoryName);


}

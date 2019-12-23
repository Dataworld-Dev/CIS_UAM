package com.dw.ngms.cis.im.repository;

import com.dw.ngms.cis.im.entity.CostCategories;
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
public interface CostSubRepository extends JpaRepository<CostSubCategories, UUID> {


    @Query(value = "SELECT SUBCOSTCATEGORY_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getCostSubCategoryId();

    @Query("SELECT u FROM CostSubCategories u WHERE u.costCategoryCode = :costCategoryCode and isActive = 'Y' order by costCategoryName asc")
    List<CostSubCategories> getSubCostCategoriesByCostCategoryCode(@Param("costCategoryCode") String costCategoryCode);

    @Query("SELECT u FROM CostSubCategories u WHERE u.costSubCategoryCode = :costSubCategoryCode")
    CostSubCategories findBycostSubCategoryCode(@Param("costSubCategoryCode") String costSubCategoryCode);

    @Query("SELECT u FROM CostSubCategories u WHERE u.costSubCategoryName = :costSubCategoryName")
    List<CostSubCategories>  getCostOfCategory(@Param("costSubCategoryName") String costSubCategoryName);



}



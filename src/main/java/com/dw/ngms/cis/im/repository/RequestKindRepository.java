package com.dw.ngms.cis.im.repository;

import com.dw.ngms.cis.im.entity.CostCategories;
import com.dw.ngms.cis.im.entity.RequestKinds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created by swaroop on 2019/04/19.
 */
@Repository
public interface RequestKindRepository extends JpaRepository<RequestKinds, UUID>  {


    @Query(value = "SELECT REQUESTKINDS_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getRequestKind();
}

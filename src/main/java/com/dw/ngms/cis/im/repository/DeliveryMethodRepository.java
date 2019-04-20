package com.dw.ngms.cis.im.repository;

import com.dw.ngms.cis.im.entity.DeliveryMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created by swaroop on 2019/04/19.
 */
@Repository
public interface DeliveryMethodRepository extends JpaRepository<DeliveryMethods, UUID>  {


    @Query(value = "SELECT DELIVERYMETHOD_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getDeleviryMethodId();
}

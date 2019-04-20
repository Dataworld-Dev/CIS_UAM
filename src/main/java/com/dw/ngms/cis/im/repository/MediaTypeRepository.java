package com.dw.ngms.cis.im.repository;

import com.dw.ngms.cis.im.entity.MediaTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created by swaroop on 2019/04/19.
 */
@Repository
public interface MediaTypeRepository extends JpaRepository<MediaTypes, UUID>  {


    @Query(value = "SELECT MEDIATYPES_SEQ.nextval FROM dual", nativeQuery =
            true)
    Long getMediaType();
}

package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.ExternalUserAssistant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by swaroop on 2019/03/26.
 */
@Repository
public interface ExternalUserAssistantRepository extends JpaRepository<ExternalUserAssistant, Long> {

    @Query("SELECT u FROM ExternalUserAssistant u WHERE u.surveyorusercode = :surveyorusercode and u.surveyorusername = :surveyorusername and u.assistantusercode = :assistantusercode and u.assistantusername = :assistantusername")
    ExternalUserAssistant findByUserCodeName(@Param("surveyorusercode") String surveyorusercode, @Param("surveyorusername") String surveyorusername, @Param("assistantusercode") String assistantusercode, @Param("assistantusername") String assistantusername);

}

package com.dw.ngms.cis.uam.repository;

import java.util.List;

import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.ExternalUserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   @Query("SELECT u FROM User u WHERE u.userName = :userName")
   User findByLoginName(@Param("userName") String userName);

   @Query("SELECT u FROM User u WHERE u.email = :email")
   User findByEmail(@Param("email") String email);

   @Query("SELECT u FROM User u WHERE u.userTypeName = :userTypeName")
   List<User> findByUserTypeName(@Param("userTypeName") String userTypeName);

   @Query("SELECT u FROM User u WHERE u.isApproved = :approvalStatus")
   List<User> findByApprovalStatus(@Param("approvalStatus") String approvalStatus);
   
   @Query(value = "SELECT U.* FROM USERS U INNER JOIN EXTERNALUSERASSISTANTS EUA ON (EUA.USERID = U.USERID) "
	   		+ "WHERE U.USERTYPENAME = 'EXTERNAL' AND U.ISAPPROVED = ?1 AND EUA.ISAPPROVED = ?2", nativeQuery = true)
   List<User> findExternalUsersByApprovalStatus(String userApprovalStatus, String assistantApprovalStatus);
   
   @Query(value = "SELECT U.* FROM USERS U INNER JOIN EXTERNALUSERROLES EUR ON (EUR.USERCODE = U.USERCODE) INNER JOIN EXTERNALUSERASSISTANTS EUA ON (EUA.USERID = U.USERID) "
	   		+ "WHERE U.USERTYPENAME = 'EXTERNAL' AND U.ISAPPROVED = ?1 AND EUA.ISAPPROVED = ?2 AND EUR.USERPROVINCECODE = ?3", nativeQuery = true)
   List<User> findExternalUsersByApprovalStatusAndProvinceCode(String userApprovalStatus, String assistantApprovalStatus, String provincecode);
   
   @Query(value = "SELECT U.* FROM USERS U INNER JOIN EXTERNALUSERASSISTANTS EUA ON (EUA.USERID = U.USERID) "
	   		+ "WHERE U.USERTYPENAME = 'EXTERNAL' AND U.ISAPPROVED = ?1 AND EUA.ISAPPROVED = ?2 AND EUA.SURVEYORUSERCODE = ?3", nativeQuery = true)
   List<User> findAssistantsForPendingApprovalStatusAndProvinceCode(String userApprovalStatus, String assistantApprovalStatus, String surveyorusercode);

   @Query(value = "select U.* from USERS U where U.USERCODE in (select ea.assistantusercode from EXTERNALUSERASSISTANTS ea where ea.SURVEYORUSERCODE = ?1)", nativeQuery = true)
   List<User> findAssistantsSurveyorUserCode(String surveyorusercode);


   @Query(value = "select U.* from USERS U where U.USERCODE in (select ea.SURVEYORUSERCODE from EXTERNALUSERASSISTANTS ea where ea.ASSISTANTUSERCODE = ?1)", nativeQuery = true)
   List<User> findSurveyorsByAssistantsUserCode(String assistantusercode);



   
   @Query(value = "SELECT U.* FROM USERS U INNER JOIN INTERNALUSERROLES IUR ON (IUR.USERCODE = u.USERCODE) "
   		+ "WHERE U.USERTYPENAME = 'INTERNAL' AND IUR.USERPROVINCECODE = ?1", nativeQuery = true)
   List<User> findInternalUsersByProvinceCode(String provincecode);
   
   @Query(value = "SELECT U.* FROM USERS U INNER JOIN EXTERNALUSERROLES EUR ON (EUR.USERCODE = u.USERCODE) "
	   		+ "WHERE U.USERTYPENAME = 'EXTERNAL' AND EUR.USERPROVINCECODE = :provincecode", nativeQuery = true)
   List<User> findExternalUsersByProvinceCode(@Param("provincecode") String provincecode);
   
   @Query("SELECT u FROM User u WHERE u.userCode = :usercode and u.userName = :username")
   User findByUserByNameAndCode(@Param("usercode") String usercode,@Param("username") String username);

   @Query("SELECT u FROM User u WHERE u.userCode = :usercode and u.userName = :username and u.userTypeName = :usertypename")
   User findByUserCodeUserNameAndTypeName(@Param("usercode") String usercode,@Param("username") String username, @Param("usertypename") String usertypename);


   @Query("SELECT u FROM User u WHERE u.userCode = :usercode")
   User findByUserCode(@Param("usercode") String usercode);


   @Query("SELECT u FROM User u WHERE u.userCode = :usercode")
   User deleteUserAndChild(@Param("usercode") String usercode);

   @Query(value = "SELECT user_seq.nextval FROM dual", nativeQuery =
           true)
   Long getUserId();




   @Query("SELECT u FROM ExternalUser u WHERE u.usercode = :usercode")
    ExternalUser getChildElements(@Param("usercode") String usercode);

   @Query("SELECT u FROM ExternalUserRoles u WHERE u.userCode = :userCode")
   List<ExternalUserRoles> getExternalUserRolesChildElements(@Param("userCode") String userCode);


   @Query(value = "SELECT U.* FROM USERS U INNER JOIN INTERNALUSERROLES IUR ON (IUR.USERCODE = u.USERCODE) "
           + "WHERE IUR.USERPROVINCECODE = ?1", nativeQuery = true)
   List<User> findAllUsersByProvinceCode(@Param("usercode") String usercode);


   @Query("SELECT usercode FROM ExternalUser u WHERE u.ppno = :ppno")
   String getUserCode(@Param("ppno") String ppno);

   @Query("SELECT userName FROM User u WHERE u.userCode = :userCode")
   String getUserName(@Param("userCode") String userCode);

   @Query("SELECT ppno FROM ExternalUser u WHERE u.ppno = :ppno")
   String getpPNumber(@Param("ppno") String ppno);

   @Query(value = "SELECT DISTINCT(externaluserdata.ppnno) FROM externaluserdata, externaluserroles WHERE " +
           "externaluserdata.usercode = externaluserroles.usercode AND externaluserdata.ppnno = ?1  AND " +
           "externaluserroles.userrolecode = 'EX010' ", nativeQuery = true)
   String getpPNumberForAssistant(@Param("ppno") String ppno);

   @Query(value = "SELECT DISTINCT(externaluserdata.usercode) FROM externaluserdata, externaluserroles WHERE " +
           "externaluserdata.usercode = externaluserroles.usercode AND externaluserdata.ppnno = ?1  AND " +
           "externaluserroles.userrolecode = 'EX010' ", nativeQuery = true)
   String getUserCodeForAssistant(@Param("ppno") String ppno);




}

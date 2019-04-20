package com.dw.ngms.cis.uam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.dto.UserUpdateDTO;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.ExternalUserRoles;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.enums.ApprovalStatus;
import com.dw.ngms.cis.uam.ldap.LdapClient;
import com.dw.ngms.cis.uam.ldap.UserProfile;
import com.dw.ngms.cis.uam.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LdapClient ldapClient;

	public List<User> getAllUsersByUserTypeName(String userTypeName){
		return userRepository.findByUserTypeName(userTypeName);
	}//getAllUsersByUserTypeName
	
	public User submitInternalUserForApproval(String usercode, String username, String userType, ApprovalStatus approvalStatus) {
		if(StringUtils.isEmpty(usercode) || StringUtils.isEmpty(username)) {
    		throw new RuntimeException("User code and user name required");
    	}
		//fetch internal user by code and name
    	User user = getUserByUserCodeUserNameAndTypeName(usercode, username, userType);
    	if(user == null) {
    		throw new RuntimeException("User(s) not found");
    	}
    	//update approval status
    	user.setIsApproved(approvalStatus);
    	//persist to db
    	user = userRepository.save(user);
    	
    	return user;
	}//submitInternalUserForApproval
	
	public User getUserByUserCodeUserNameAndTypeName(String userCode, String userName, String userTypeName){
		return userRepository.findByUserCodeUserNameAndTypeName(userCode, userName, userTypeName);
	}//getAllUsersByUserTypeName
	
	public List<User> getAllInternalUsersByProvinceCode(String provinceCode){
		return userRepository.findInternalUsersByProvinceCode(provinceCode);
	}//getAllUsersByUserTypeNameAndProvinceCode

	public List<User> getAllExternalUsersByProvinceCode(String provinceCode){
		return userRepository.findExternalUsersByProvinceCode(provinceCode);
	}//getAllUsersByUserTypeNameAndProvinceCode
	
	public List<User> getAllApprovalPendingUsers(String approvalStatus){
		return userRepository.findByApprovalStatus(approvalStatus);
	}//getAllApprovalPendingUsers
	
	public List<User> getAllExternalApprovalPendingUsers(String userApprovalStatus, String assistantApprovalStatus){
		return userRepository.findExternalUsersByApprovalStatus(userApprovalStatus, assistantApprovalStatus);
	}//getAllExternalApprovalPendingUsers
	
	public List<User> getAllExternalApprovalPendingUsersByProvinceCode(String userApprovalStatus, String assistantApprovalStatus, String provinceCode){
		return userRepository.findExternalUsersByApprovalStatusAndProvinceCode(userApprovalStatus, assistantApprovalStatus, provinceCode);
	}//getAllExternalApprovalPendingUsersByProvinceCode
	
	public List<User> getAllAssistantsForPendingApprovalBySurveyorUserCode(String userApprovalStatus, String assistantApprovalStatus,
			String surveyorusercode) {
		return userRepository.findAssistantsForPendingApprovalStatusAndProvinceCode(userApprovalStatus, assistantApprovalStatus, surveyorusercode);
	}//getAllAssistantsForPendingApprovalBySurveyorUserCode
	
	public List<User> getAllAssistantsBySurveyorUserCode(String surveyorusercode) {
		System.out.println("surveyorusercode "+surveyorusercode);
		return userRepository.findAssistantsSurveyorUserCode(surveyorusercode);
	}//getAllAssistantsBySurveyorUserCode
	
	public List<User> getAllSurveyorsByAssistantsUserCode(String assistantusercode){
		return userRepository.findSurveyorsByAssistantsUserCode(assistantusercode);
	}//getAllSurveyorByAssistantsUserCode
	
	public User findByEmail(String email) {
		return this.userRepository.findByEmail(email);
	} //FindUserByEmail

	public Long getUserId() {
		return this.userRepository.getUserId();
	} //FindUserByEmail

	public ExternalUser getChildElements(String userCode) {
		return this.userRepository.getChildElements(userCode);
	} //FindUserByEmail


	public List<ExternalUserRoles> getExternalUserRolesChildElements(String userCode) {
		return this.userRepository.getExternalUserRolesChildElements(userCode);
	} //FindUserByEmail

	public String getUserCode(String pPNumber) {
		return this.userRepository.getUserCode(pPNumber);
	} //getUserCode

	public String getUserName(String userCode) {
		return this.userRepository.getUserName(userCode);
	} //getUserCode

	public String getpPNumber(String pPNumber) {
		return this.userRepository.getpPNumber(pPNumber);
	} //getUserCode

	public String getpPNumberForAssistant(String pPNumber) {
		return this.userRepository.getpPNumberForAssistant(pPNumber);
	}

	public String getUserCodeForAssistant(String ppNumber) {
		return this.userRepository.getUserCodeForAssistant(ppNumber);
	}






	public User findByUserByNameAndCode(UserDTO userDTO) {
		System.out.println("userDTO.getUsercode() "+userDTO.getUsercode());
		System.out.println("userDTO.getUsername() "+userDTO.getUsername());
		return this.userRepository.findByUserByNameAndCode(userDTO.getUsercode(), userDTO.getUsername());
	}//FindUserByNameAndCode


	public User findByUserCode(UserDTO userDTO) {
		return this.userRepository.findByUserCode(userDTO.getUsercode());
	}

	public void deleteUserAndChild(User user) {
		 this.userRepository.delete(user);
	}




	public User updateUserApproval(User user) {
		return this.userRepository.save(user);
	}//updateUserApproval

	public User updatePassword(User user) {
		return this.userRepository.save(user);
	}//updateUserApproval

	public User saveExternalUser(User user) {
		return this.userRepository.save(user);
	}//saveExternalUser

	public User updateExternalUser(User uiUser) {
		if(uiUser == null || uiUser.getUserCode() == null || uiUser.getUserName() == null) return null;
		
		User user = userRepository.findByUserByNameAndCode(uiUser.getUserCode(), uiUser.getUserName());
		if(user == null) return null;

		return this.userRepository.save(getPopulatedUserWithModifiedDetails(user, uiUser));
	}//updateExternalUser

	private User getPopulatedUserWithModifiedDetails(User user, User uiUser) {
		if(uiUser.getEmail() != null) user.setEmail(uiUser.getEmail());
		if(uiUser.getExternaluser() != null) user.setExternaluser(uiUser.getExternaluser());
		if(uiUser.getEmail() != null) user.setEmail(uiUser.getEmail());
		if(uiUser.getFirstName() != null) user.setFirstName(uiUser.getFirstName());
		if(uiUser.getIsActive() != null) user.setIsActive(uiUser.getIsActive());
		if(uiUser.getIsApprejDate() != null) user.setIsApprejDate(uiUser.getIsApprejDate());
		if(uiUser.getIsApprejuserCode() != null) user.setIsApprejuserCode(uiUser.getIsApprejuserCode());
		if(uiUser.getIsApprejuserName() != null) user.setIsApprejuserName(uiUser.getIsApprejuserName());
		if(uiUser.getIsApproved() != null) user.setIsApproved(uiUser.getIsApproved());
		if(uiUser.getMobileNo() != null) user.setMobileNo(uiUser.getMobileNo());
//		if(uiUser.getPassword() != null) user.setPassword(uiUser.getPassword());//TODO confirm
		if(uiUser.getRejectionReason() != null) user.setRejectionReason(uiUser.getRejectionReason());
		if(uiUser.getSurname() != null) user.setSurname(uiUser.getSurname());
		if(uiUser.getTelephoneNo() != null) user.setTelephoneNo(uiUser.getTelephoneNo());
		if(uiUser.getTitle() != null) user.setTitle(uiUser.getTitle());
		if(uiUser.getUserTypeCode() != null) user.setUserTypeCode(uiUser.getUserTypeCode());
		if(uiUser.getUserTypeName() != null) user.setUserTypeName(uiUser.getUserTypeName());
		return user;
	}//getPopulatedUserWithModifiedDetails

	public User saveInternalUser(User user) {
		if(user == null) return null;
		return this.userRepository.save(user);
	}//saveInternalUser

	public User updateUser(UserUpdateDTO uiUser) {
		if(uiUser == null || uiUser.getUserCode() == null) return null;
		User user = this.userRepository.findByUserCode(uiUser.getUserCode());
		System.out.println("Test update  user coder" +uiUser.getUserCode());
		if(user == null) return null;
		return this.userRepository.save(getPopulatedUserWithUpdate(user, uiUser));
	}//updateExternalUser


	public User updateInternalUser(UserUpdateDTO uiUser) {
		if(uiUser == null || uiUser.getUserCode() == null) return null;
		User user = this.userRepository.findByUserCode(uiUser.getUserCode());
		System.out.println("Test update internal Update" +uiUser.getUserCode());
		if(user == null) return null;
		return this.userRepository.save(getPopulatedInternalUserWithUpdate(user, uiUser));
	}//updateExternalUser



	private User getPopulatedInternalUserWithUpdate(User user, UserUpdateDTO uiUser) {
		if(uiUser.getFirstName() != null && uiUser.getFirstName() != "") user.setFirstName(uiUser.getFirstName());

		if(uiUser.getUserTypeCode() != null && uiUser.getUserTypeCode() != "") user.setFirstName(uiUser.getUserTypeCode());
		if(uiUser.getUserTypeName() != null && uiUser.getUserTypeName() != "") user.setUserTypeName(uiUser.getUserTypeName());
		if(uiUser.getTitle() != null && uiUser.getTitle() != "") user.setTitle(uiUser.getTitle());
		if(uiUser.getUserName() != null && uiUser.getUserName() != "") user.setUserName(uiUser.getUserName());
		if(uiUser.getSurname() != null && uiUser.getSurname() != "") user.setSurname(uiUser.getSurname());
		if(uiUser.getFirstLogin() != null && uiUser.getFirstLogin() != "") user.setFirstLogin(uiUser.getFirstLogin());


		if(uiUser.getMobileNo() != null && uiUser.getMobileNo() != "") user.setMobileNo(uiUser.getMobileNo());
		if(uiUser.getTelephoneNo() != null && uiUser.getTelephoneNo() != "") user.setTelephoneNo(uiUser.getTelephoneNo());
		if(uiUser.getEmail() != null && uiUser.getEmail() != "") user.setEmail(uiUser.getEmail());
		//if(uiUser.getIsApproved().getDisplayString() != null && uiUser.getIsApproved().getDisplayString() != "") user.setIsApproved(uiUser.getIsApproved());
		return user;
	}//getPopulatedUserWithModifiedDetails





	private User getPopulatedUserWithUpdate(User user, UserUpdateDTO uiUser) {
		if(uiUser.getFirstName() != null && uiUser.getFirstName() != "") user.setFirstName(uiUser.getFirstName());

		if(uiUser.getUserTypeCode() != null && uiUser.getUserTypeCode() != "") user.setFirstName(uiUser.getUserTypeCode());
		if(uiUser.getUserTypeName() != null && uiUser.getUserTypeName() != "") user.setUserTypeName(uiUser.getUserTypeName());
		if(uiUser.getTitle() != null && uiUser.getTitle() != "") user.setTitle(uiUser.getTitle());
		if(uiUser.getUserName() != null && uiUser.getUserName() != "") user.setUserName(uiUser.getUserName());
		if(uiUser.getSurname() != null && uiUser.getSurname() != "") user.setSurname(uiUser.getSurname());
		if(uiUser.getFirstLogin() != null && uiUser.getFirstLogin() != "") user.setFirstLogin(uiUser.getFirstLogin());


		if(uiUser.getMobileNo() != null && uiUser.getMobileNo() != "") user.setMobileNo(uiUser.getMobileNo());
		if(uiUser.getTelephoneNo() != null && uiUser.getTelephoneNo() != "") user.setTelephoneNo(uiUser.getTelephoneNo());
		if(uiUser.getEmail() != null && uiUser.getEmail() != "") user.setEmail(uiUser.getEmail());
		//if(uiUser.getIsApproved().getDisplayString() != null && uiUser.getIsApproved().getDisplayString() != "") user.setIsApproved(uiUser.getIsApproved());

		if(uiUser.getRejectionReason() != null && uiUser.getRejectionReason() != "") user.setRejectionReason(uiUser.getRejectionReason());
		if(uiUser.getIsApprejuserCode() != null && uiUser.getIsApprejuserCode() != "") user.setIsApprejuserCode(uiUser.getIsApprejuserCode());
		if(uiUser.getIsApprejuserName() != null && uiUser.getIsApprejuserName() != "") user.setIsApprejuserName(uiUser.getIsApprejuserName());
		if(uiUser.getIsApprejDate() != null) user.setIsApprejDate(uiUser.getIsApprejDate());

		ExternalUser externalUser = this.userRepository.getChildElements(uiUser.getUserCode());
		if(externalUser != null)
			user.setExternaluser(externalUser);			
		if(uiUser.getExternaluser() != null) {
			if(uiUser.getExternaluser().getOrganizationtypecode() != null && uiUser.getExternaluser().getOrganizationtypecode() != "")user.getExternaluser().setOrganizationtypecode(uiUser.getExternaluser().getOrganizationtypecode());
			if(uiUser.getExternaluser().getOrganizationtypename() != null && uiUser.getExternaluser().getOrganizationtypecode() != "") user.getExternaluser().setOrganizationtypename(uiUser.getExternaluser().getOrganizationtypename());
	
			if(uiUser.getExternaluser().getSectorCode() != null && uiUser.getExternaluser().getSectorCode() != ""){
				user.getExternaluser().setSectorcode(uiUser.getExternaluser().getSectorCode());
			}
			if(uiUser.getExternaluser().getSectorName() != null && uiUser.getExternaluser().getSectorName() != "") user.getExternaluser().setSectorname(uiUser.getExternaluser().getSectorName());
			if(uiUser.getExternaluser().getPostaladdressline1() != null && uiUser.getExternaluser().getPostaladdressline1() != "") user.getExternaluser().setPostaladdressline1(uiUser.getExternaluser().getPostaladdressline1());
			if(uiUser.getExternaluser().getPostaladdressline2() != null && uiUser.getExternaluser().getPostaladdressline2() != "") user.getExternaluser().setPostaladdressline2(uiUser.getExternaluser().getPostaladdressline2());
			if(uiUser.getExternaluser().getPostaladdressline3() != null && uiUser.getExternaluser().getPostaladdressline3() != "") user.getExternaluser().setPostaladdressline3(uiUser.getExternaluser().getPostaladdressline3());
			if(uiUser.getExternaluser().getPostalcode() != null && uiUser.getExternaluser().getPostalcode() != "") user.getExternaluser().setPostalcode(uiUser.getExternaluser().getPostalcode());
			if(uiUser.getExternaluser().getCommunicationmodetypecode() != null && uiUser.getExternaluser().getCommunicationmodetypecode() != "") user.getExternaluser().setCommunicationmodetypecode(uiUser.getExternaluser().getCommunicationmodetypecode());
			if(uiUser.getExternaluser().getCommunicationmodetypename() != null && uiUser.getExternaluser().getCommunicationmodetypename() != "") user.getExternaluser().setCommunicationmodetypename(uiUser.getExternaluser().getCommunicationmodetypename());
			if(uiUser.getExternaluser().getSubscribenotifications() != null && uiUser.getExternaluser().getSubscribenotifications() != "") user.getExternaluser().setSubscribenotifications(uiUser.getExternaluser().getSubscribenotifications());
			if(uiUser.getExternaluser().getSubscribeevents() != null && uiUser.getExternaluser().getSubscribeevents() != "") user.getExternaluser().setSubscribeevents(uiUser.getExternaluser().getSubscribeevents());
			if(uiUser.getExternaluser().getSubscribenews() != null && uiUser.getExternaluser().getSubscribenews() != "") user.getExternaluser().setSubscribenews(uiUser.getExternaluser().getSubscribenews());
		}
		return user;
	}//getPopulatedUserWithModifiedDetails




	public UserProfile isADUserExists(String username, String password) {
		UserProfile userProfile = new UserProfile();
		try {
			if (ldapClient.authenticate(username.trim(), password.trim())) {
				List<UserProfile> userProfileList = ldapClient.searchUser(username.trim());
				userProfile = (userProfileList != null && userProfileList.size() == 1) ? userProfileList.get(0)
						: userProfile;
			}
		} catch (Exception e) {
			log.error("User '{}' LDAP authentication failed", username);
		}
		return userProfile;
	}// isADUserExists

}

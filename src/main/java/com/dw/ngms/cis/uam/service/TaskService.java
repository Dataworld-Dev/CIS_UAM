package com.dw.ngms.cis.uam.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.entity.TaskLifeCycle;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.repository.InternalRoleRepository;
import com.dw.ngms.cis.uam.repository.TaskLifeCycleRepository;
import com.dw.ngms.cis.uam.repository.TaskRepository;
import com.dw.ngms.cis.workflow.api.ProcessAdditionalInfo;
import com.dw.ngms.cis.workflow.api.ProcessEngine;
import com.dw.ngms.cis.workflow.model.Assignee;
import com.dw.ngms.cis.workflow.model.Assigner;
import com.dw.ngms.cis.workflow.model.Process;
import com.dw.ngms.cis.workflow.model.Target;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by swaroop on 2019/04/06.
 */
@Slf4j
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @SuppressWarnings("unused")
	@Autowired
	private UserService userService;
    @Autowired
	private ProcessEngine<Task> processEngine;
    @Autowired
    private TaskLifeCycleRepository taskLifeCycleRepository;
    @Autowired
    private InternalRoleRepository internalRoleRepository;
    
    public Task saveTask(Task task) {
        return this.taskRepository.save(task);
    } //FindUserByEmail
    
    @SuppressWarnings({ "unchecked", "serial" })
	public List<Task> findByCriteria(String taskStatus, String taskType, String taskAllProvinceCode, String taskAllOCSectionCode, 
			String taskAllOCRoleCode, String userName, String omitTaskStatus, Date fromDate, Date toDate) {
        return this.taskRepository.findAll(new Specification<Task>() {
            @Override
            public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (taskStatus != null && !StringUtils.isEmpty(taskStatus)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskStatus"), taskStatus)));
                }
                if (taskType != null && !StringUtils.isEmpty(taskType)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskType"), taskType)));
                }
                if (taskAllProvinceCode != null && !StringUtils.isEmpty(taskAllProvinceCode)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskAllProvinceCode"), taskAllProvinceCode)));
                }
                if (taskAllOCSectionCode != null && !StringUtils.isEmpty(taskAllOCSectionCode)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskAllOCSectionCode"), taskAllOCSectionCode)));
                }
                if (taskAllOCRoleCode != null && !StringUtils.isEmpty(taskAllOCRoleCode)) {               	                	
					Join<Task, InternalRole> internalRoleJoin = (Join<Task, InternalRole>) root.<Task, InternalRole>fetch("internalRoleList", JoinType.INNER);
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(internalRoleJoin.get("internalRoleCode"), taskAllOCRoleCode)));
                }
                if (userName != null && !StringUtils.isEmpty(userName)) {               	                	
                	Join<Task, User> userJoin = (Join<Task, User>) root.<Task, User>fetch("userList", JoinType.INNER);
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(userJoin.get("userName"), userName)));                   
                }
                if (omitTaskStatus != null && !StringUtils.isEmpty(omitTaskStatus)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.notEqual(root.get("taskStatus"), omitTaskStatus)));
                }

				if (fromDate != null) {
					predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), fromDate)));
				}

				if (toDate != null) {
					predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), toDate)));
				}

                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
    }//findByCriteria

    public void startProcess(String processId, Requests requests) {
    	if(processId == null)
    		throw new RuntimeException("Process id is reqired to start process");
    	Task task = populateTask(requests);
    	ProcessAdditionalInfo additionalInfo = populateAdditionalInfo(task, requests);
    	processEngine.startProcess(processId, task, additionalInfo);
    	if(additionalInfo.isInternalCapturer()) {
    		additionalInfo = updateAdditionalInfo(additionalInfo, requests);
    		additionalInfo.setTargetSequenceId(
    				processEngine.getSequenceByStateAndProcessId(
    						"Reassigned", processId).getId());
    		this.processUserState(additionalInfo);
    	}    		
    }//startProcess
    
	public Target processUserState(ProcessAdditionalInfo additionalInfo) {
		if(additionalInfo == null)
    		throw new RuntimeException("Task process details reqired to process");		
    	return this.processUserState(getTask(additionalInfo), additionalInfo);
    }//processUserState
    
	public Target processUserState(Task task, ProcessAdditionalInfo additionalInfo) {
		if(additionalInfo == null)
    		throw new RuntimeException("Task process details reqired to process");
		if(task == null) 
    		throw new RuntimeException("Task not found with ID: "+ additionalInfo.getTaskId());
		
    	return processEngine.processUserState(task, additionalInfo);
    }//processUserState
	
	public void cancelProcess(String processId, Requests requests) {
    	if(processId == null)
    		throw new RuntimeException("Process id is reqired to start process");
    	Process process = processEngine.getProcessById(processId);
    	Task task = getTask(requests.getRequestCode());
    	task.setTaskCLoseDESC("Cancelled by user: "+requests.getDescription());
    	ProcessAdditionalInfo additionalInfo = populateAdditionalInfo(task, requests);
    	updateAdditionalInfo(additionalInfo, requests);
    	this.endProcess(process, task, additionalInfo);
    }//cancelProcess
	
    public void endProcess(Process process, Task task, ProcessAdditionalInfo additionalInfo) {
    	processEngine.endProcess(process, task, additionalInfo);
    }//processUserState
    
    public List<Target> getTaskTargetFlows(Long taskId){
    	if(taskId == null)
    		throw new RuntimeException("Task id is reqired");
    	Task task = getTask(taskId);
    	if(task == null) 
    		throw new RuntimeException("Task not found with ID: "+ taskId);
    	
    	return processEngine.getSequenceTargetFlows(task.getTaskType(), task.getTaskStatus());
    }//getTaskTargetFlows
    
    public List<Target> getTaskTargetFlows(Long taskId, String internalrolecode) {
    	List<Target> filteredList = new ArrayList<>();
    	List<Target> targetList = getTaskTargetFlows(taskId);
    	if(!CollectionUtils.isEmpty(targetList)) {
    		InternalRole role = internalRoleRepository.findByInternalRoleCode(internalrolecode);
    		if(role != null) {
    			for(Target target : targetList) {
    				if(!CollectionUtils.isEmpty(target.getAssignerList())) {
	    				for(Assigner assigner: target.getAssignerList()) {
	    					if(assigner.getName().equals(role.getRoleName())) {
	    						filteredList.add(target);
	    						break;
	    					}
	    				}//inner
    				}else {
    					filteredList.add(target);
    				}
    			}//outer
    		}
    	}
		return filteredList;
	}//getTaskTargetFlows
    
    public Task getTask(ProcessAdditionalInfo additionalInfo) {
    	if(additionalInfo == null) return null;    	
    	return (additionalInfo.getTaskId() != null) ? getTask(additionalInfo.getTaskId()) : getTask(additionalInfo.getRequestCode());
    }//getTask
    
    public Task getTask(String requestCode) {
		return (StringUtils.isEmpty(requestCode)) ? null : taskRepository.findByReferenceCode(requestCode);
	}//getTask

	public Task getTask(Long id) {
        return (id == null) ? null : taskRepository.findById(id).get();
    }//getTask

    public Long getTaskID() {
        return this.taskRepository.getTaskID();
    }

    public Task getCloseTask(String taskCode, String taskReferenceCode, String taskReferenceType) {
        return this.taskRepository.getCloseTask(taskCode, taskReferenceCode, taskReferenceType);
    } //FindUserByEmail

    private Task populateTask(Requests requests) {
    	Task task = new Task();
    	Long taskId = this.getTaskID();
        log.info("taskId: {0}", taskId);
        task.setTaskId(taskId);
        task.setTaskCode("TASK000" + Long.toString(taskId));
        task.setCreatedDate(new Date());
        task.setUpdatedDate(task.getCreatedDate());
        task.setTaskAllProvinceCode(requests.getProvinceCode());
        task.setTaskAllOCSectionCode(requests.getSectionCode());
        task.setTaskOpenDate(task.getCreatedDate());        
        task.setTaskReferenceCode(requests.getRequestCode());
        if(requests.isInternalCapturer() && 
        		!StringUtils.isEmpty(requests.getCapturerCode())) {
        	task.setTaskDoneUserCode(requests.getCapturerCode());
            task.setTaskDoneUserName(requests.getCapturerName());
            task.setTaskDoneUserFullName(requests.getCapturerFullName());
            task.setInitiatedUser(requests.getCapturerName());
        }else if(!StringUtils.isEmpty(requests.getUserCode())) {
        	task.setTaskDoneUserCode(requests.getUserCode());
            task.setTaskDoneUserName(requests.getUserName());
            task.setTaskDoneUserFullName(requests.getUserFullName());
            task.setInitiatedUser(requests.getUserName());
        }
		return task;
	}//populateTask

	private ProcessAdditionalInfo populateAdditionalInfo(Task task, Requests requests) {
		ProcessAdditionalInfo additionalInfo = new ProcessAdditionalInfo();
		
		additionalInfo.setProvinceCode(requests.getProvinceCode());
		additionalInfo.setSectionCode(requests.getSectionCode());
		additionalInfo.setSequenceRequest(requests.getSequenceRequest());
		additionalInfo.setRequestCode(requests.getRequestCode());
		additionalInfo.setInternalCapturer(requests.isInternalCapturer());
		if(additionalInfo.isInternalCapturer() && 
				!StringUtils.isEmpty(requests.getAssigneeInfoManager())) {
			additionalInfo.getAssigneeList().add(new Assignee("User", requests.getAssigneeInfoManager()));
		}		
		return additionalInfo;
	}//populateAdditionalInfo

	private ProcessAdditionalInfo updateAdditionalInfo(ProcessAdditionalInfo additionalInfo, Requests requests) {
		additionalInfo.getAssigneeList().clear();
		if(additionalInfo.isInternalCapturer() && 
				!StringUtils.isEmpty(requests.getAssigneeInfoOfficer())) {
			additionalInfo.getAssigneeList().add(new Assignee("User", requests.getAssigneeInfoOfficer()));
		}
		if(!StringUtils.isEmpty(requests.getUserCode())) {
			additionalInfo.setUserCode(requests.getUserCode());
			additionalInfo.setUserName(requests.getUserName());
			additionalInfo.setUserFullName(requests.getUserFullName());
        }
		return additionalInfo;
	}//updateAdditionalInfo
	
	public String getTaskCurrentStatus(String requestcode) {
		if(StringUtils.isEmpty(requestcode))
    		throw new RuntimeException("Request code is reqired");
		Task task = getTask(requestcode);
    	if(task == null) 
    		throw new RuntimeException("Task not found with request code: "+ requestcode);		
		
		return task.getTaskStatus();
	}//getTaskCurrentStatus

    public List<TaskLifeCycle> getTasksLifeCycleByTaskReferenceCode(String taskReferenceCode) {
        return this.taskLifeCycleRepository.getTasksLifeCycleByTaskReferenceCode(taskReferenceCode);
    }//getTasksLifeCycleByTaskReferenceCode

}

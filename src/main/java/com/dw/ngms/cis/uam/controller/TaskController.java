package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.exception.ExceptionConstants;
import com.dw.ngms.cis.uam.dto.MailDTO;
import com.dw.ngms.cis.uam.dto.TaskDTO;
import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.InternalUserRoleService;
import com.dw.ngms.cis.uam.service.TaskService;
import com.dw.ngms.cis.uam.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by swaroop on 2019/04/06.
 */

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class TaskController extends MessageController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private InternalUserRoleService internalUserRoleService;

    @Autowired
    private UserService userService;

    @PostMapping("/createTask")
    public ResponseEntity<?> createTask(HttpServletRequest request, @RequestBody @Valid Task task) {
        try {

            Long taskId = this.taskService.getTaskID();
            System.out.println(taskId);
            task.setTaskCode("TASK000" + Long.toString(taskId));
            Task taskService = this.taskService.saveTask(task);

            // MailDTO mailDTO = getMailDTO(taskService);
            //sendMailToTaskUser(taskService, mailDTO);
            return ResponseEntity.status(HttpStatus.OK).body(taskService);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createTask


    private MailDTO getMailDTO(@RequestBody @Valid Task task) {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setHeader(ExceptionConstants.header);
        mailDTO.setFooter(ExceptionConstants.footer);
        mailDTO.setSubject(ExceptionConstants.subject);
        return mailDTO;
    }


    private void sendMailToTaskUser(@RequestBody @Valid Task task, MailDTO mailDTO) throws IOException {
        String mailResponse = null;
        String userCode = null;

        List<InternalUserRoles> userRolesList = this.internalUserRoleService.getInternalUserName(task.getTaskAllProvinceCode(),task.getTaskAllOCSectionCode(),task.getTaskAllOCRoleCode());
        System.out.println("user code is " +userRolesList.get(0).getUserCode());
        System.out.println("user name is " +userRolesList.get(0).getUserName());
        for(InternalUserRoles user: userRolesList){
            System.out.println("user code are " +user.getUserCode());
        }
        String userName = this.userService.getUserName(userRolesList.get(0).getUserCode());
        mailDTO.setHeader(ExceptionConstants.header + " " + userName + ",");
        mailDTO.setSubject("New Task Created");
        mailDTO.setBody1("New Task have been created for you.");
        mailDTO.setBody2("Task type is " +task.getTaskReferenceType());
        mailDTO.setBody3("");
        mailDTO.setBody4("");;
        mailDTO.setToAddress(userRolesList.get(0).getUserName());//admin user for later
        mailResponse = sendMail(mailDTO);
        System.out.println("mailResponse is "+mailResponse);
    }


    @PostMapping("/closeTask")
    public ResponseEntity<?> closeTask(HttpServletRequest request, @RequestBody @Valid TaskDTO taskDTO) {
        try {
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            String json = null;
            Gson gson = new Gson();
            System.out.println("taskDoneUserName " + taskDTO.getTaskDoneByUserName());
            Task task = this.taskService.getCloseTask(taskDTO.getTaskCode(), taskDTO.getTaskReferenceCode(), taskDTO.getTaskReferenceType());
            if (task != null && task.getTaskId() != null) {
                task.setTaskCode(taskDTO.getTaskCode());
                task.setTaskReferenceCode(taskDTO.getTaskReferenceCode());
                task.setTaskReferenceType(taskDTO.getTaskReferenceType());
                task.setTaskCLoseDESC(taskDTO.getTaskCloseDesc());
                task.setTaskDoneUserCode(taskDTO.getTaskDoneByUserCode());
                task.setTaskDoneUserName(taskDTO.getTaskDoneByUserName());
                task.setTaskStatus("CLOSE");
                this.taskService.saveTask(task);
                return ResponseEntity.status(HttpStatus.OK).body(task);
            }
            return generateEmptyWithOKResponse(request, "No Task for the given task code and task reference code and reference type");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createTask



    @GetMapping("/getAllTasks")
    public List<Task> findByCriteria(  @RequestParam(required = false) String taskStatus,
                                       @RequestParam(required = false) String taskType,
                                       @RequestParam(required = false) String taskAllProvinceCode,
                                       @RequestParam(required = false) String taskAllOCSectionCode,
                                       @RequestParam(required = false) String taskAllOCRoleCode) {

        return  this.taskService.findByCriteria(taskStatus,taskType,taskAllProvinceCode,taskAllOCSectionCode,taskAllOCRoleCode);
    }
}

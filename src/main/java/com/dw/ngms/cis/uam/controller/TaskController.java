package com.dw.ngms.cis.uam.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.uam.dto.TaskDTO;
import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.entity.TaskLifeCycle;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.InternalUserRoleService;
import com.dw.ngms.cis.uam.service.TaskService;
import com.dw.ngms.cis.uam.service.UserService;
import com.google.gson.Gson;

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




   /* private void sendMailToTaskUser(@RequestBody @Valid Task task, MailDTO mailDTO) throws IOException {
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
    }*/


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
                task.setTaskDoneUserFullName(taskDTO.getTaskDoneByUserFullName());
                task.setTaskStatus("Closed");
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
                                       @RequestParam(required = false) String taskAllOCRoleCode,
                                       @RequestParam(required = false) String userName,
                                       @RequestParam(required = false) String omitTaskStatus,
                                       @RequestParam(required = false) String fromDate,
                                       @RequestParam(required = false) String toDate) throws ParseException {

           //@RequestParam(value="fromDate") @DateTimeFormat(pattern="dd-M-yyyy") Date fromDate,
                                       //@RequestParam(value="toDate") @DateTimeFormat(pattern="dd-M-yyyy") Date toDate) throws ParseException {


       /* System.out.println("userName is "+userName);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
        String formatFromDate = formatter.format(fromDate);
        System.out.println("Format From Date String" +formatFromDate);
        String formatToDate = formatter.format(toDate);
        Date formatDateFrom = formatter.parse(formatFromDate);
        System.out.println("Format From Date Date" +formatDateFrom);
        Date formatDateTo = formatter.parse(formatToDate);
*/

        Date todate = null;
        Date fromdate = null;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yy HH:mm:ss");
        if(fromDate!=null) {
            fromdate = formatter.parse(fromDate+" 00:00:00");
            System.out.println("test is" +formatter.format(fromdate));
        }


        if(toDate!=null) {
            todate = formatter.parse(toDate+" 23:59:59");
            System.out.println("todate is" +formatter.format(todate));
        }
        List<Task> taskList = this.taskService.findByCriteria(taskStatus,taskType,taskAllProvinceCode,taskAllOCSectionCode,taskAllOCRoleCode,userName,omitTaskStatus,fromdate,todate);
        return taskList;
    }


    @GetMapping("/getTasksLifeCycle")
    public ResponseEntity<?> getTasksLifeCycle(HttpServletRequest request,
                                                 @RequestParam String taskReferenceCode) throws IOException {
        try {
            List<TaskLifeCycle> taskLifeCycleList = this.taskService.getTasksLifeCycleByTaskReferenceCode(taskReferenceCode);
            return (CollectionUtils.isEmpty(taskLifeCycleList)) ? ResponseEntity.status(HttpStatus.OK).body(taskLifeCycleList)
                    : ResponseEntity.status(HttpStatus.OK).body(taskLifeCycleList);
        } catch(Exception exception)
        {
            return generateFailureResponse(request, exception);
        }
    }//getTasksLifeCycle



}

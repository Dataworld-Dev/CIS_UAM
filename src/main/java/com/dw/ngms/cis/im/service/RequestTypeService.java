package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.RequestTypes;
import com.dw.ngms.cis.im.repository.RequestTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/04/19.
 */
@Service
public class RequestTypeService {

    @Autowired
    private RequestTypeRepository requestTypeRepository;


    public Long getRequestTypeID() {
        return this.requestTypeRepository.getRequestTypeID();
    } //getRequestTypeID


    public RequestTypes saveRequestType(RequestTypes requestTypes) {
        return this.requestTypeRepository.save(requestTypes);
    } //saveRequestType



}

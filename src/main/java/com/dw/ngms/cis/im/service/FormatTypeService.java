package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.FormatTypes;
import com.dw.ngms.cis.im.repository.FormatTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/04/19.
 */
@Service
public class FormatTypeService {

    @Autowired
    private FormatTypeRepository formatTypeRepository;


    public Long getFormatType() {
        return this.formatTypeRepository.getFormatType();
    } //getFormatType


    public FormatTypes saveFormatType(FormatTypes formatTypes) {
        return this.formatTypeRepository.save(formatTypes);
    } //saveRequestType



}

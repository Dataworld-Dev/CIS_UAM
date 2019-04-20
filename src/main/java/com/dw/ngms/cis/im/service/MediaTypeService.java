package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.MediaTypes;
import com.dw.ngms.cis.im.repository.MediaTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/04/19.
 */
@Service
public class MediaTypeService {

    @Autowired
    private MediaTypeRepository mediaTypeRepository;


    public Long getMediaType() {
        return this.mediaTypeRepository.getMediaType();
    } //getMediaType


    public MediaTypes saveMediaType(MediaTypes mediaTypes) {
        return this.mediaTypeRepository.save(mediaTypes);
    } //saveRequestType



}

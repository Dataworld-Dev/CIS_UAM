package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.FormatTypes;
import com.dw.ngms.cis.im.entity.GazetteTypes;
import com.dw.ngms.cis.im.repository.FormatTypeRepository;
import com.dw.ngms.cis.im.repository.GazetteTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/04/19.
 */
@Service
public class GazetteTypeService {

    @Autowired
    private GazetteTypeRepository gazetteTypeRepository;


    public Long getGazetteType() {
        return this.gazetteTypeRepository.getGazetteType();
    } //getGazetteType


    public GazetteTypes saveGazetteType(GazetteTypes gazetteTypes) {
        return this.gazetteTypeRepository.save(gazetteTypes);
    } //saveGazetteType



}

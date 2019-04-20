package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.DeliveryMethods;
import com.dw.ngms.cis.im.repository.DeliveryMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/04/19.
 */
@Service
public class DeliveryMethodService {

    @Autowired
    private DeliveryMethodRepository deliveryMethodRepository;


    public Long getDeleviryMethodId() {
        return this.deliveryMethodRepository.getDeleviryMethodId();
    } //getDeleviryMethodId


    public DeliveryMethods saveDeliveryMethod(DeliveryMethods deliveryMethods) {
        return this.deliveryMethodRepository.save(deliveryMethods);
    } //saveRequestType



}

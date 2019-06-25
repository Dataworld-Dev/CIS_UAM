package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.DeliveryMethods;
import com.dw.ngms.cis.im.entity.FormatTypes;
import com.dw.ngms.cis.im.repository.DeliveryMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<DeliveryMethods> getAllDeliveryMethos() {
        return this.deliveryMethodRepository.findAll();
    } //getAllDeliveryMethos


    public DeliveryMethods getDeliveryMethodByCode(String deliveryMethodCode) {
        return this.deliveryMethodRepository.getDeliveryMethodByCode(deliveryMethodCode);
    } //getDeliveryMethodByCode


    public void deleteDeliveyMethod(DeliveryMethods deliveryMethods) {
         this.deliveryMethodRepository.delete(deliveryMethods);
    } //deleteDeliveyMethod






}

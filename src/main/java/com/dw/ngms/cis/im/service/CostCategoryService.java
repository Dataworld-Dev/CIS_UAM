package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.ApplicationProperties;
import com.dw.ngms.cis.im.entity.CostCategories;
import com.dw.ngms.cis.im.repository.CostCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swaroop on 2019/04/16.
 */

@Service
public class CostCategoryService {


    @Autowired
    private CostCategoryRepository costCategoryRepository;

    public List<CostCategories> getAllCostCategories() {
        return this.costCategoryRepository.findAll();
    }//getAllCostCategories

    public CostCategories saveCostCategory(CostCategories costCategories) {
        return this.costCategoryRepository.save(costCategories);
    } //saveCostCategory

    public Long getCategoryId() {
        return this.costCategoryRepository.getCategoryId();
    } //getCategoryId



}

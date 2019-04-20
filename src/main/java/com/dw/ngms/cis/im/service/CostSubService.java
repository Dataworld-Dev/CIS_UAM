package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.CostCategories;
import com.dw.ngms.cis.im.entity.CostSubCategories;
import com.dw.ngms.cis.im.repository.CostSubRepository;
import com.dw.ngms.cis.uam.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swaroop on 2019/04/16.
 */
@Service
public class CostSubService {


    @Autowired
    private CostSubRepository costSubRepository;

    public Long getCostSubCategoryId() {
        return this.costSubRepository.getCostSubCategoryId();
    } //getCostSubCategoryId

    public CostSubCategories saveCostSubCategories(CostSubCategories costSubCategories) {
        return this.costSubRepository.save(costSubCategories);
    } //saveCostSubCategories



 /*   public List<CostSubCategories> getSubCostCategoriesByCostCategoryCode(String costCategoryCode) {
        return this.costSubRepository.getSubCostCategoriesByCostCategoryCode(costCategoryCode);


    }*/

}

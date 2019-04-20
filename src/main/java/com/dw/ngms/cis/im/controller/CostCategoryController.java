package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.im.entity.ApplicationProperties;
import com.dw.ngms.cis.im.entity.CostCategories;
import com.dw.ngms.cis.im.service.CostCategoryService;
import com.dw.ngms.cis.uam.controller.MessageController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by swaroop on 2019/04/16.
 */
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class CostCategoryController extends MessageController {


    @Autowired
    private CostCategoryService costCategoryService;


  /*  @GetMapping("/getCostCategories")
    public ResponseEntity<?> getCostCategories(HttpServletRequest request) {
        try {
            List<CostCategories> costCategoriesList = costCategoryService.getAllCostCategories();
            return (CollectionUtils.isEmpty(costCategoriesList)) ? generateEmptyResponse(request, "CostCategories(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(costCategoriesList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getCostCategories
*/

     @PostMapping("/createCategory")
    public ResponseEntity<?> setPropertyValueByName(HttpServletRequest request, @RequestBody @Valid CostCategories costCategories) {
        try {
            Long categoryId = this.costCategoryService.getCategoryId();
            System.out.println("categoryId is "+categoryId);
            costCategories.setCategoryCode("COST" + Long.toString(categoryId));
            CostCategories costCategoriesSave = this.costCategoryService.saveCostCategory(costCategories);
            return ResponseEntity.status(HttpStatus.OK).body(costCategoriesSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createCategory



}

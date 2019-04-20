package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.im.entity.CostCategories;
import com.dw.ngms.cis.im.entity.CostSubCategories;
import com.dw.ngms.cis.im.service.CostSubService;
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
public class CostSubCategoryController extends MessageController {

    @Autowired
    private CostSubService costSubService;

   /* @GetMapping("/getSubCostCategoriesByCostCategoryCode")
    public ResponseEntity<?> getSubCostCategoriesByCostCategoryCode(HttpServletRequest request,
                                                                    @RequestParam String costCategoryCode) {
        try {
            List<CostSubCategories> costCategoriesList = costSubService.getSubCostCategoriesByCostCategoryCode(costCategoryCode);
            return (CollectionUtils.isEmpty(costCategoriesList)) ? ResponseEntity.status(HttpStatus.OK).body(costCategoriesList)
                    : ResponseEntity.status(HttpStatus.OK).body(costCategoriesList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getSubCostCategoriesByCostCategoryCode

*/
  @PostMapping("/createSubCategory")
    public ResponseEntity<?> createSubCategory(HttpServletRequest request, @RequestBody @Valid CostSubCategories costSubCategories) {
        try {
            Long subCategoryId = this.costSubService.getCostSubCategoryId();
            System.out.println("sub categoryId is "+subCategoryId);
            costSubCategories.setCostSubCategoryCode("SUBCOST" + Long.toString(subCategoryId));
            CostSubCategories costSubCategoriesSave = this.costSubService.saveCostSubCategories(costSubCategories);
            return ResponseEntity.status(HttpStatus.OK).body(costSubCategoriesSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createCategory


}

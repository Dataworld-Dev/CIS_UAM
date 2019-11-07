package com.dw.ngms.cis.im.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.im.repository.InvoiceStatusRepository;
import com.dw.ngms.cis.uam.entity.Task;

@Service
public class InvoiceStatusService {

	@Autowired
	private InvoiceStatusRepository invoiceStatusRepository;

	public List<Task> findByCriteria(String taskAllProvinceCode, Date fromDate, Date toDate, String taskStatus) {
		return this.invoiceStatusRepository.findAll(new Specification<Task>() {
		
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();

				if (taskStatus != null && !StringUtils.isEmpty(taskStatus)) {
					predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskStatus"), taskStatus)));
				}

				if (taskAllProvinceCode != null && !StringUtils.isEmpty(taskAllProvinceCode) && !"all".equalsIgnoreCase(taskAllProvinceCode)) {
					predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskAllProvinceCode"), taskAllProvinceCode)));
				}

				if (fromDate != null) {
					predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), fromDate)));
				}

				if (toDate != null) {
					predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), toDate)));
				}

				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
	}

}

package com.dw.ngms.cis.im.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.Task;

@Repository
public interface InvoiceStatusRepository extends JpaRepository<Task, Long> {
	List<Task> findAll(Specification<Task> specification);
}

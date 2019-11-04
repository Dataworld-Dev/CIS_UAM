package com.dw.ngms.cis.im.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.Task;

@Repository
public interface InvoiceStatusRepository extends JpaRepository<Task, Long> {

	@Query("SELECT u FROM Task u WHERE u.taskAllProvinceCode = :provienceCode and u.taskStatus=:taskStatus and u.createdDate BETWEEN :fromDate AND :toDate")
	List<Task> findInvoiceClosedTasks(@Param("provienceCode") String provienceCode, @Param("fromDate") Date fromDate,
									  @Param("toDate") Date toDate, @Param("taskStatus") String taskStatus);
	
	
	 List<Task> findAll(Specification<Task> specification);

}

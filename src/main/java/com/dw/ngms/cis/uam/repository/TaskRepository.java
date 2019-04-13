package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swaroop on 2019/04/06.
 */

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>,JpaSpecificationExecutor<Task> {




    @Query(value = "SELECT task_seq.nextval FROM dual", nativeQuery =
            true)
    Long getTaskID();

    @Query("SELECT u FROM Task u WHERE u.taskCode = :taskCode and u.taskReferenceCode = :taskReferenceCode and u.taskReferenceType = :taskReferenceType")
    public Task getCloseTask(@Param("taskCode") String taskCode,
                             @Param("taskReferenceCode") String taskReferenceCode,
                             @Param("taskReferenceType") String taskReferenceType);

    List<Task> findAll(Specification<Task> specification);

}
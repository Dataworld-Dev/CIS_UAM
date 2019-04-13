package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.SecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, Integer> {
}

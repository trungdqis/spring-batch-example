package com.trungdq.springbatch.repository;

import com.trungdq.springbatch.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
}

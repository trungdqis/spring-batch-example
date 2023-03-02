package com.trungdq.springbatch.processor;

import com.trungdq.springbatch.postgresql.entity.Student;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FirstItemProcessor implements ItemProcessor<Student, com.trungdq.springbatch.mysql.entity.Student> {

	@Override
	public com.trungdq.springbatch.mysql.entity.Student process(Student item) throws Exception {
		
		System.out.println(item.getId());
		
		com.trungdq.springbatch.mysql.entity.Student student = new
				com.trungdq.springbatch.mysql.entity.Student();
		
		student.setId(item.getId());
		student.setFirstName(item.getFirstName());
		student.setLastName(item.getLastName());
		student.setEmail(item.getEmail());
		student.setDeptId(item.getDeptId());
		student.setIsActive(item.getIsActive() != null && Boolean.parseBoolean(item.getIsActive()));
		
		return student;
		
	}

}

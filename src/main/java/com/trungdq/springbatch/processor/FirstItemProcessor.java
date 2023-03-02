package com.trungdq.springbatch.processor;

import com.trungdq.springbatch.model.StudentCsv;
import com.trungdq.springbatch.model.StudentJdbc;
import com.trungdq.springbatch.model.StudentJson;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FirstItemProcessor implements ItemProcessor<StudentCsv, StudentJson> {

    @Override
    public StudentJson process(StudentCsv item) throws Exception {
        if (6 == item.getId()) {
            System.out.println("Inside Item Processor");
            throw new NullPointerException();
        }
        StudentJson studentJson = new StudentJson();
        studentJson.setId(item.getId());
        studentJson.setFirstName(item.getFirstName());
        studentJson.setLastName(item.getLastName());
        studentJson.setEmail(item.getEmail());

        return studentJson;
    }
}

package com.trungdq.springbatch.writer;

import com.trungdq.springbatch.model.StudentCsv;
import com.trungdq.springbatch.model.StudentJdbc;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FirstItemWriter implements ItemWriter<StudentJdbc> {

    @Override
    public void write(List<? extends StudentJdbc> list) throws Exception {
        System.out.println("Inside Item Writer");
        list.forEach(System.out::println);
    }
}

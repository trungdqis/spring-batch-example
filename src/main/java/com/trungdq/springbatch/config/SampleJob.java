package com.trungdq.springbatch.config;

import com.trungdq.springbatch.model.StudentCsv;
import com.trungdq.springbatch.model.StudentJdbc;
import com.trungdq.springbatch.writer.FirstItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
public class SampleJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private FirstItemWriter firstItemWriter;

    @Autowired
    private DataSource dataSource;

    @Bean
    public Job readSourceJob() {
        return jobBuilderFactory.get("Read Source Job")
                .start(chunkStepForReadSource())
                .build();
    }

    private Step chunkStepForReadSource() {
        return stepBuilderFactory.get("Chunk Step")
//                .<StudentCsv, StudentCsv>chunk(3)
                .<StudentJdbc, StudentJdbc>chunk(3)
//                .reader(flatFileItemReader())
                .reader(jdbcCursorItemReader())
//                .processor(firstItemProcessor)
                .writer(firstItemWriter)
                .build();
    }

    private FlatFileItemReader<StudentCsv> flatFileItemReader() {
        FlatFileItemReader<StudentCsv> flatFileItemReader = new FlatFileItemReader<>();

        flatFileItemReader.setResource(new FileSystemResource("src/main/resources/students.csv"));
        flatFileItemReader.setLineMapper(lineMapper());
        flatFileItemReader.setLinesToSkip(1);

        return flatFileItemReader;
    }

    private LineMapper<StudentCsv> lineMapper() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("ID", "First Name", "Last Name", "Email");

        BeanWrapperFieldSetMapper<StudentCsv> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(StudentCsv.class);

        DefaultLineMapper<StudentCsv> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

    private JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader() {
        JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader = new JdbcCursorItemReader<>();

        jdbcCursorItemReader.setDataSource(dataSource);
        jdbcCursorItemReader.setSql("SELECT id, first_name as firstName, last_name as lastName, email FROM student");

        BeanPropertyRowMapper<StudentJdbc> beanPropertyRowMapper = new BeanPropertyRowMapper<>();
        beanPropertyRowMapper.setMappedClass(StudentJdbc.class);
        jdbcCursorItemReader.setRowMapper(beanPropertyRowMapper);

        return jdbcCursorItemReader;
    }
}

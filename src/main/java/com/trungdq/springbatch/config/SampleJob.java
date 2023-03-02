package com.trungdq.springbatch.config;

import com.trungdq.springbatch.listener.SkipListener;
import com.trungdq.springbatch.model.StudentCsv;
import com.trungdq.springbatch.model.StudentJdbc;
import com.trungdq.springbatch.model.StudentJson;
import com.trungdq.springbatch.processor.FirstItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class SampleJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private FirstItemProcessor firstItemProcessor;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SkipListener skipListener;

    @Bean
    public Job readSourceJob() {
        return jobBuilderFactory.get("Read Source Job")
                .start(chunkStep())
                .build();
    }

    private Step chunkStep() {
        return stepBuilderFactory.get("Chunk Step")
                .<StudentCsv, StudentJson>chunk(3)
                .reader(flatFileItemReader())
                .processor(firstItemProcessor)
                .writer(jsonFileItemWriter())
                .faultTolerant()
//                .skip(FlatFileParseException.class)
//                .skip(NullPointerException.class)
                .skip(Throwable.class)
//                .skipLimit(1) // how many records want to skip - Integer.MAX_VALUE
                .skipPolicy(new AlwaysSkipItemSkipPolicy()) // the other one -> skipping all
                .listener(skipListener)
                .build();

        // like try catch exception
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

    @StepScope
    @Bean
    public JsonFileItemWriter<StudentJson>  jsonFileItemWriter() {
        return new JsonFileItemWriter<>(
                new FileSystemResource("src/main/resources/students.json"),
                new JacksonJsonObjectMarshaller<>()) {
            @Override
            public String doWrite(List<? extends StudentJson> items) {
                items.forEach(item -> {
                    if (3 == item.getId()) {
                        throw new NullPointerException();
                    }
                });
                return super.doWrite(items);
            }
        };
//        return new JsonFileItemWriter<>(new FileSystemResource("src/main/resources/students.json"),
//                new JacksonJsonObjectMarshaller<>());
    }
}

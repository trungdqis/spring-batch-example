package com.trungdq.springbatch.config;

import com.trungdq.springbatch.listener.SkipListener;
import com.trungdq.springbatch.listener.SkipListenerImpl;
import com.trungdq.springbatch.postgresql.entity.Student;
import com.trungdq.springbatch.processor.FirstItemProcessor;
import com.trungdq.springbatch.reader.FirstItemReader;
import com.trungdq.springbatch.writer.FirstItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class SampleJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private FirstItemReader firstItemReader;

    @Autowired
    private FirstItemProcessor firstItemProcessor;

    @Autowired
    private FirstItemWriter firstItemWriter;

    @Autowired
    private SkipListener skipListener;

    @Autowired
    private SkipListenerImpl skipListenerImpl;

    @Autowired
    @Qualifier("datasource")
    private DataSource datasource;

    @Autowired
    @Qualifier("universitydatasource")
    private DataSource universitydatasource;

    @Autowired
    @Qualifier("postgresdatasource")
    private DataSource postgresdatasource;

    @Autowired
    @Qualifier("postgresqlEntityManagerFactory")
    private EntityManagerFactory postgresqlEntityManagerFactory;

    @Autowired
    @Qualifier("mysqlEntityManagerFactory")
    private EntityManagerFactory mysqlEntityManagerFactory;

    @Autowired
    private JpaTransactionManager jpaTransactionManager;

    @Bean
    public Job chunkJob() {
        return jobBuilderFactory.get("Migrate Chunk Job")
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep())
                .build();
    }

    private Step firstChunkStep() {
        return stepBuilderFactory.get("Migrate Chunk Step")
                .<Student, com.trungdq.springbatch.mysql.entity.Student>chunk(3)
                .reader(jpaCursorItemReader())
                .processor(firstItemProcessor)
                .writer(jpaItemWriter())
                .faultTolerant()
                .skip(Throwable.class)
                .skipLimit(100)
                .retryLimit(3)
                .retry(Throwable.class)
                .listener(skipListenerImpl)
                .transactionManager(jpaTransactionManager)
                .build();
    }

    public JpaCursorItemReader<Student> jpaCursorItemReader() {
        JpaCursorItemReader<Student> jpaCursorItemReader =
                new JpaCursorItemReader<Student>();

        jpaCursorItemReader.setEntityManagerFactory(postgresqlEntityManagerFactory);

        jpaCursorItemReader.setQueryString("From Student");

        return jpaCursorItemReader;
    }

    public JpaItemWriter<com.trungdq.springbatch.mysql.entity.Student> jpaItemWriter() {
        JpaItemWriter<com.trungdq.springbatch.mysql.entity.Student> jpaItemWriter =
                new JpaItemWriter<>();

        jpaItemWriter.setEntityManagerFactory(mysqlEntityManagerFactory);

        return jpaItemWriter;
    }
}

package com.trungdq.springbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan({"com.trungdq.springbatch.config", "com.trungdq.springbatch.listener",
        "com.trungdq.springbatch.reader", "com.trungdq.springbatch.processor",
        "com.trungdq.springbatch.writer", "com.trungdq.springbatch.listener"})
public class SpringBatchExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchExampleApplication.class, args);
    }

}

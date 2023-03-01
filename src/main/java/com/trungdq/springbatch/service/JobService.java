package com.trungdq.springbatch.service;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    @Autowired
    private JobLauncher jobLauncher;

    @Qualifier("readSourceJob")
    @Autowired
    private Job readSourceJob;

    @Qualifier("writeCsvToDBJob")
    @Autowired
    private Job writeCsvToDBJob;

    public void startJob(String jobName) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            if ("Read Source Job".equals(jobName)) {
                jobLauncher.run(readSourceJob, jobParameters);
            } else if ("Csv To DB Job".equals(jobName)) {
                jobLauncher.run(writeCsvToDBJob, jobParameters);
            }

        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}

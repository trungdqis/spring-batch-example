package com.trungdq.springbatch.controller;

import com.trungdq.springbatch.service.JobService;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobOperator jobOperator;

    @GetMapping("/start/{jobName}")
    public String startJob(@PathVariable String jobName) {
        jobService.startJob(jobName);
        return "Job started...";
    }

    @GetMapping("/stop/{jobExecutionId}")
    public String stopJob(@PathVariable long jobExecutionId) {
        try {
            jobOperator.stop(jobExecutionId);
        } catch (NoSuchJobExecutionException | JobExecutionNotRunningException e) {
            e.printStackTrace();
        }
        return "Job stopped...";
    }
}

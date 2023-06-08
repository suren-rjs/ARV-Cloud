package com.api.arv.service.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class ApplicationThread {

    @Autowired
    private ApplicationContext context;

    @Bean
    public CommandLineRunner schedulingRunnerBean(TaskExecutor executor) {
        ApplicationJobsImplementation runnableThreads = new ApplicationJobsImplementation(context);
        return args -> executor.execute(runnableThreads);
    }
}
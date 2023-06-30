package com.arv.api.service.thread;

import com.arv.api.helper.GlobalCommonService;
import com.arv.api.repository.jobs.ApplicationJobs;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("ALL")
@Service
public class ApplicationJobsImplementation implements Runnable, ApplicationJobs {

    private final Timer timer = new Timer();
    private GlobalCommonService globalCommonService;
    private boolean isInitThreadExecuted = false;
    private boolean isUpdateTaskThreadInitiated = false;
    private TimerTask timerTaskForUserNotification;
    private TimerTask jafrisAliveMethod;

    public ApplicationJobsImplementation(ApplicationContext context) {
        this.globalCommonService = context.getBean(GlobalCommonService.class);
    }


    @Override
    public void run() {
        keepAlive();
    }

    @Override
    public void keepAlive() {
        if (isInitThreadExecuted) return;
        isInitThreadExecuted = true;

    }
}
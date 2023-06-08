package com.api.arv.service.thread;

import com.api.arv.helper.GlobalCommonService;
import com.api.arv.model.dto.response.ResponseMessage;
import com.api.arv.repository.jobs.ApplicationJobs;
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

        if (jafrisAliveMethod != null)
            jafrisAliveMethod.cancel();
        jafrisAliveMethod = new TimerTask() {
            @Override
            public void run() {
                globalCommonService.getForObject("https://dev-arv-api.onrender.com/", ResponseMessage.class);
            }
        };

        //  Init call for every minute to alive server
        timer.schedule(jafrisAliveMethod, 1000, 1000 * 60);

    }
}
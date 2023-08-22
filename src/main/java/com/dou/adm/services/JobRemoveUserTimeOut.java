package com.dou.adm.services;

import com.dou.accounting.services.impl.CloseSoldoutService;
import com.dou.adm.models.UserLogin;
import com.dou.adm.shared.LoginAccountSaved;
import com.dou.adm.shared.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
public class JobRemoveUserTimeOut {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloseSoldoutService.class);

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

    public JobRemoveUserTimeOut() {
        threadPoolTaskScheduler.setPoolSize(3);
        threadPoolTaskScheduler.setThreadNamePrefix("Job-remove-user-timeout");
        threadPoolTaskScheduler.initialize();
    }



    ScheduledFuture<?> jobRemove;

    public ResponseObject jobStart() {

        LoginAccountSaved userSaving = LoginAccountSaved.getInstance();
        threadPoolTaskScheduler.shutdown();
        threadPoolTaskScheduler.setPoolSize(3);
        threadPoolTaskScheduler.setThreadNamePrefix("Job-remove-user-timeout");
        threadPoolTaskScheduler.initialize();
        jobRemove = threadPoolTaskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    List<UserLogin> userLogin = userSaving.getAllUserLogin();
                    if(userLogin.size() > 0){
                        for(int i = 0; i<userLogin.size(); i++){
                            Date date = new Date();
                            userLogin.get(i).setEndTimeLogin(date);
                            int countMilisecond2Date = (int)((userLogin.get(i).getEndTimeLogin().getTime() - userLogin.get(i).getStartTimeLogin().getTime())/1000);
                            LOGGER.info("User Current Login:" + userLogin.get(i).getAccountId() + " Start time Login: " +userLogin.get(i).getStartTimeLogin());
                            if(userLogin.get(i).getTimeKeepAccount() >= 900){
                                userSaving.removeUserLogin(userLogin.get(i).getAccountId());
                            }

                            if(countMilisecond2Date >=900){
                                userSaving.removeUserLogin(userLogin.get(i).getAccountId());
                            }
                        }
                    }else{
                        jobStop();
                        System.out.println("Job User Login Is Stop!");
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt(); // restore interrupted status
                }
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                return new CronTrigger("*/3 * * * * ?").nextExecutionTime(triggerContext);
            }
        });
        return new ResponseObject("Job is start");
    }
    public void jobStop() {
        jobRemove.cancel(true);
    }
}

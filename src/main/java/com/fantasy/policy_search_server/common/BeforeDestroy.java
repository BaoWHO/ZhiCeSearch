package com.fantasy.policy_search_server.common;

import javax.annotation.PreDestroy;

import com.fantasy.policy_search_server.utils.EmailUtil;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class BeforeDestroy {
    @PreDestroy
    public void destroy() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = formatter.format(new Date());
        EmailUtil.save("["+currentTime+"]" + " " + "<span style=\"color: #5a952a;\">"+"PolicySearchServerApplication stop"+"</span>");
        MyThread thread = new MyThread();
        Thread t = new Thread(thread);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

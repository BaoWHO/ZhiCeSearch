package com.fantasy.policy_search_server.common;

import com.fantasy.policy_search_server.utils.EmailUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class AfterStart implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = formatter.format(new Date());
        EmailUtil.save("["+currentTime+"]" + " " + "<span style=\"color: #5a952a;\">"+"PolicySearchServerApplication start"+"</span>");
    }
}

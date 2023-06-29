package com.fantasy.policy_search_server.shedule;

import com.fantasy.policy_search_server.dao.PolicyDBModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class shedule {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    PolicyDBModelMapper policyDBModeMapper;
    @Scheduled(cron = "0 */30 * * * ?")
    public void task() {
        Long end = redisTemplate.opsForZSet().size("policyRank");
        Set<ZSetOperations.TypedTuple<Object>> viewmap = redisTemplate.opsForZSet().rangeWithScores("policyRank", 0, end - 1);
        for (ZSetOperations.TypedTuple<Object> i : viewmap) {
            String policyid = i.getValue().toString().split(":")[1];
            int view = policyDBModeMapper.getViewByID(Long.parseLong(policyid));
            view += i.getScore().intValue();
            policyDBModeMapper.addView(Long.parseLong(policyid), view);
        }
    }
}

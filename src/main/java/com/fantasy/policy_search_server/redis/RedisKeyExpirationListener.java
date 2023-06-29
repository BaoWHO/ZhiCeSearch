package com.fantasy.policy_search_server.redis;

import com.fantasy.policy_search_server.dao.PolicyDBModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    @Autowired
    PolicyDBModelMapper policyDBModelMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
//        if (expiredKey.equals("policyRank1")) {
//            // 处理过期键事件
//
//                Long end = redisTemplate.opsForZSet().size("policyRank1");
//                Set<ZSetOperations.TypedTuple<Object>> viewmap = redisTemplate.opsForZSet().rangeWithScores("policyRank1", 0, end - 1);
//
//                for (ZSetOperations.TypedTuple<Object> i : viewmap) {
//                    String policyid = i.getValue().toString().split(":")[1];
//                    int view = policyDBModelMapper.getViewByID(Long.parseLong(policyid));
//                    view += i.getScore().intValue();
//                    policyDBModelMapper.addView(Long.parseLong(policyid), view);
                }

}



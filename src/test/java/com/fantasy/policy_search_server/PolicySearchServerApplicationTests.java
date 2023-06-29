package com.fantasy.policy_search_server;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import com.fantasy.policy_search_server.dao.PolicyDBModelMapper;
import com.fantasy.policy_search_server.model.SuggestModel;
import com.fantasy.policy_search_server.service.PolicyService;
import com.fantasy.policy_search_server.service.impl.PolicyServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class PolicySearchServerApplicationTests {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private ElasticsearchClient client;

    @Test
    void testString() {
        //写入一条String数据
        redisTemplate.opsForValue().set("name","老葛");
        //获取String数据
        Object name=redisTemplate.opsForValue().get("name");
        System.out.println("name="+name);
    }

    @Test
    void testSaveUser(){
        redisTemplate.opsForValue().set("suggest1",new SuggestModel(2,"老葛","23"));
       Object o=redisTemplate.opsForValue().get("suggest1");
        System.out.println("o="+o);
    }

    @Test
    void testHyperloglog(){
        redisTemplate.opsForHyperLogLog().add("id:"+1,"192.168.163.131");
        redisTemplate.opsForHyperLogLog().add("id:"+1,"192.168.163.132");
        double sum=redisTemplate.opsForHyperLogLog().size("id:"+1);
        System.out.println("sum="+sum);
    }

    @Test
    void testZSet(){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,0);
        calendar.set(Calendar.HOUR_OF_DAY,15);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        //System.out.println("设置后的日期：" + calendar.getTime());

        Long timeOut = (calendar.getTimeInMillis()-System.currentTimeMillis()) / 1000;
        int r=4,u=4;
//       Double score=redisTemplate.opsForZSet().score("policyRank","policyId:"+r);
//        System.out.println(score);
//        if(score==null){
//            redisTemplate.opsForZSet().addIfAbsent("policyRank","policyId:"+r,1);
//            redisTemplate.expire("policyRank",timeOut, TimeUnit.SECONDS);
//        }else redisTemplate.opsForZSet().add("policyRank","policyId:"+r,2);

        redisTemplate.opsForHyperLogLog().add("policyId:"+r,u);
        int view=redisTemplate.opsForHyperLogLog().size("policyId:"+r).intValue();
        System.out.println(view);
        redisTemplate.expire("policyId:"+r,timeOut, TimeUnit.SECONDS);




//
//            Set<Object> policy_id=redisTemplate.opsForZSet().reverseRange("policyRank",0,9);
//        for (Object i: policy_id) {
//            String id=i.toString().split(":")[1];
//            System.out.println(id);
//        }

    }

    @Test
    void testStringRedis(){
//        Set<Object> policy_id=redisTemplate.opsForZSet().reverseRange("policyRank",0,9);
//        int r=1;
//        for (Object i: policy_id) {
//            String id=i.toString().split(":")[1];
//            redisTemplate.opsForValue().set("rank:"+r,id);
//            System.out.println(redisTemplate.opsForValue().get("rank:"+r));
//            //三天后更新一次
//            redisTemplate.expire("rank:"+r,3, TimeUnit.HOURS);
//            r++;
//        }

        System.out.println(redisTemplate.opsForZSet().size("policyRank"));
    }

    @Test
    void testSQL(){

//
//        Long o=redisTemplate.opsForZSet().getOperations().getExpire("policyRank");
//        System.out.println(o);
//        if (0<60) {
//            Long end = redisTemplate.opsForZSet().size("policyRank");
//
//            Set<ZSetOperations.TypedTuple<Object>> viewmap = redisTemplate.opsForZSet().rangeWithScores("policyRank", 0, end - 1);
//
//            for (ZSetOperations.TypedTuple<Object> i : viewmap) {
//                String policyid = i.getValue().toString().split(":")[1];
//                int view = policyDBModelMapper.getViewByID(policyid);
//                view += i.getScore().intValue();
//                policyDBModelMapper.addView(Long.parseLong(policyid), view);
//            }
//        }
        //System.out.println(viewmap);
    }

    @Test
    void getSplit() throws IOException {
        AnalyzeResponse analyzeResponse = client.indices().analyze(a -> a
                .analyzer("ik_smart").text("内蒙古的一级研究院")
        );
        System.out.println(analyzeResponse.toString());
        List<String> ret = null;
    }
}

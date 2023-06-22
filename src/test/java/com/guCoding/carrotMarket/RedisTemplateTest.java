package com.guCoding.carrotMarket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTemplateTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void save_test() throws Exception {
        //given
        String key = "token";
        String value = "value 1";
        String value2 = "value 2";
        //when
        redisTemplate.opsForValue().set(key,value);

        //then
        System.out.println("테스트 : " + redisTemplate.opsForValue().get(key));

    }
}

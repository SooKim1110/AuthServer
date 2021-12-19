package sookim.authServer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;


@Service
public class RedisService {
//    private final RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    public String getData(String key){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    };

    public void setData(String key, String value, long duration){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, duration, TimeUnit.MILLISECONDS);
    };

    public void deleteData(String key){
        redisTemplate.delete(key);
    }


}

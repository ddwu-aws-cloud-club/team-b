package org.course.registration.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {

    private String host;
    private String port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("127.0.0.1", 6379);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        // Key를 String으로 직렬화
        template.setKeySerializer(new StringRedisSerializer());

        // Hash key도 String으로 직렬화
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value는 JSON 형태로 직렬화
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        // Hash value도 JSON 형태로 직렬화
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return template;
    }
}
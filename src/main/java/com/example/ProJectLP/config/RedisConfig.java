package com.example.ProJectLP.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

//    @Value("${spring.data.redis.host}")
//    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

//    @Value("${spring.data.redis.username}")
//    private String username;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${spring.data.a.redis.index}")
    private int aIndex;

    @Value("${spring.data.b.redis.index}")
    private int bIndex;

    @Bean
    @Primary
    LettuceConnectionFactory connectionFactory1() { return createConnectionFactoryWith(aIndex); }

    @Bean
    StringRedisTemplate redisTemplate1(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    @Qualifier("2")
    LettuceConnectionFactory connectionFactory2() { return createConnectionFactoryWith(bIndex); }

    @Bean
    StringRedisTemplate redisTemplate2(@Qualifier("2") RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    public LettuceConnectionFactory createConnectionFactoryWith(int index) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("43.201.57.32");
        redisStandaloneConfiguration.setPort(port);
//        redisStandaloneConfiguration.setUsername(username);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setDatabase(index);

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
}

package com.example.ProJectLP.domain.refreshToken;

import com.example.ProJectLP.domain.TimeStamped;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 14400)
public class RefreshToken extends TimeStamped {

    @Id
    private String refreshToken;

    private Long memberId;

    public RefreshToken(String refreshToken, Long memberId) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }

}

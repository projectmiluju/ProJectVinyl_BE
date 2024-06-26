package com.example.ProJectLP.domain.refreshToken;

import com.example.ProJectLP.domain.TimeStamped;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "refreshToken")
public class RefreshToken extends TimeStamped {

    private static final Long DEFAULT_TTL = (long) (1 * 60 * 60 * 24);

    @Id
    private String refreshToken;

    private Long memberId;

    @TimeToLive
    private Long expiration = DEFAULT_TTL;

    public RefreshToken(String refreshToken, Long memberId, Long expiration) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.expiration = expiration;
    }

    public RefreshToken(String refreshToken, Long memberId) {
        this(refreshToken, memberId, DEFAULT_TTL);
    }

}

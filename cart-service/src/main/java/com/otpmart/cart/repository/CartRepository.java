package com.otpmart.cart.repository;

import com.otpmart.cart.entity.Cart;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class CartRepository {

    private static final String KEY_PREFIX = "cart:";
    private static final long CACHE_TTL_HOURS = 1;

    private final RedisTemplate<String, Object> redisTemplate;
    private final CartJpaRepository cartJpaRepository;

    public CartRepository(RedisTemplate<String, Object> redisTemplate,
                          CartJpaRepository cartJpaRepository) {
        this.redisTemplate = redisTemplate;
        this.cartJpaRepository = cartJpaRepository;
    }

    public Cart findByUserId(String userId) {
        Cart cached = (Cart) redisTemplate.opsForValue().get(KEY_PREFIX + userId);
        if (cached != null) {
            return cached;
        }

        Cart fromDb = cartJpaRepository.findById(userId).orElse(null);

        if (fromDb != null) {
            redisTemplate.opsForValue().set(KEY_PREFIX + userId, fromDb, CACHE_TTL_HOURS, TimeUnit.HOURS);
        }

        return fromDb;
    }

    public void save(Cart cart) {
        cartJpaRepository.save(cart);
        redisTemplate.opsForValue().set(KEY_PREFIX + cart.getUserId(), cart, CACHE_TTL_HOURS, TimeUnit.HOURS);
    }

    public void deleteByUserId(String userId) {
        cartJpaRepository.deleteById(userId);
        redisTemplate.delete(KEY_PREFIX + userId);
    }
}
package com.em.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redicache 工具类
 * 
 */
@SuppressWarnings("unchecked")
//@Component
public class RedisUtil {
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;

	@Value("${lock_duration}")
	private String lock_duration;

	@Value("${lock_times}")
	private String lock_times;

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 批量删除key
	 * 
	 * @param pattern
	 */
	public void removePattern(final String pattern) {
		Set<Serializable> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0)
			redisTemplate.delete(keys);
	}

	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	public Object get(final String key) {
		Object result = null;
		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
		result = operations.get(key);
		return result;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
//			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
//			operations.set(key, value);
//			result = true;
			this.set(key, value, Long.parseLong(lock_duration));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * 写入缓存 
	 * 
	 * @param key
	 * @param value List
	 * @return
	 */
	public boolean setList(final String key, List<?> value) {
		boolean result = false;
		try {
			ListOperations<Serializable, Object> listOperations= redisTemplate.opsForList();
			for (Object object : value) {
				listOperations.leftPush(key, object);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void list(){
//		redisTemplate.opsForList().rightPopAndLeftPush();
	}

	public void zAdd(String queName,Long score,String value){
		ZSetOperations zSetOperations = redisTemplate.opsForZSet();
		zSetOperations.add(queName, value, score);
	}

	public Set<String> zGet(String queName,Long min,Long max){
		ZSetOperations zSetOperations = redisTemplate.opsForZSet();
		Set<String> set = zSetOperations.rangeByScore(queName, min, max);
		return  set;
	}

	public void zRemove(String queName,String value){
		ZSetOperations zSetOperations = redisTemplate.opsForZSet();
		zSetOperations.remove(queName, value);
	}

	public Set<ZSetOperations.TypedTuple<String>> zrangeWithScores(String queName, Long start, Long end){
		ZSetOperations zSetOperations = redisTemplate.opsForZSet();
		Set<ZSetOperations.TypedTuple<String>> set = zSetOperations.rangeWithScores(queName, start, end);
		return  set;
	}
	public Long getIncr(String key){
		return (Long) redisTemplate.execute(new RedisCallback() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
				byte[] rowKey = stringSerializer.serialize(key);
				byte[] rowVal = connection.get(rowKey);
				String val = (String) stringSerializer.deserialize(rowVal);

				return val==null?null:Long.parseLong(val);
			}
		});
	}

}
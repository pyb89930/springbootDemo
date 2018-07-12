package com.forezp.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


/**
 *  redis 服务类
 *  全局配置文件 配置好redis相关参数后，可以直接通过注入 redis的模板类 StringRedisTemplate 进行读写redis
 *
 *
 */
@Service
public class RedisService {
	@Resource
	protected StringRedisTemplate stringTemplate;
	/**
	 * 添加set
	 * 
	 * @param key
	 * @param value
	 */
	public void addSet(String key, String... value) {
		stringTemplate.opsForSet().add(key, value);

	}

	/**
	 * 添加hash
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public void addHash(String key, String field, String value) {
		stringTemplate.opsForHash().put(key, field, value);
	}

	public void addHashMap(String key, Map<Object, Object> map) {
		stringTemplate.opsForHash().putAll(key, map);
	}

	/**
	 * 获得set集合
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> getSet(String key) {
		return stringTemplate.opsForSet().members(key);
	}

	/**
	 * 获得hash的指定file值
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public String getHashValue(String key, String field) {
		Object ret = stringTemplate.opsForHash().get(key, field);
		return null == ret ? null : ret.toString();
	}

	/**
	 * 获得hasMap
	 * 
	 * @param key
	 * @return
	 */
	public Map<Object, Object> getHash(String key) {
		return stringTemplate.opsForHash().entries(key);
	}

	/**
	 * 删除set值
	 * 
	 * @param key
	 * @param value
	 */
	public void deleteSet(String key, Object... value) {
		BoundSetOperations<String, String> bs = stringTemplate.boundSetOps(key);
		if (null == bs)
			return;
		bs.remove(value);
		if (bs.size() == 0) {
			stringTemplate.delete(key);
		}
	}

	/**
	 * 删除hash键
	 * 
	 * @param key
	 * @param field
	 */
	public void deleteHash(String key, Object... field) {
		stringTemplate.opsForHash().delete(key, field);
	}

	/**
	 * 删除key
	 * 
	 * @param key
	 */
	public void deleteKey(String key) {
		stringTemplate.delete(key);
	}

	/**
	 * 取交集
	 * 
	 * @param key1
	 * @param key2
	 */
	public void intersect(String key1, String key2) {
		stringTemplate.opsForSet().intersect(key1, key2);
	}

	
	public String get(String key){
		return stringTemplate.opsForValue().get(key);
	}
	
	
	public List<Object> boundHashOps(String key){
        return stringTemplate.boundHashOps(key).values();
    }
	
	public List<Object> hashValues(String key){
	    return stringTemplate.opsForHash().values(key);
	}
	
}

/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource(name="redisTemplate")
    private ValueOperations<String, String> valueOperations;
    @Resource(name="redisTemplate")
    private HashOperations<String, String, Object> hashOperations;
    @Resource(name="redisTemplate")
    private ListOperations<String, Object> listOperations;
    @Resource(name="redisTemplate")
    private SetOperations<String, Object> setOperations;
    @Resource(name="redisTemplate")
    private ZSetOperations<String, Object> zSetOperations;
    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;

    public void set(String key, Object value, long expire){
        valueOperations.set(key, toJson(value));
        setNotExpire(key,expire);
    }

    public void set(String key, Object value){
        set(key, value, NOT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        setNotExpire(key,expire);
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        setNotExpire(key,expire);
        return value;
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void setHash(String key,Map<String,Object> map) {
        setHash(key,map,NOT_EXPIRE);
    }

    public void setHash(String key, Map<String,Object> map, long expire) {
        hashOperations.putAll(key,map);
        setNotExpire(key,expire);
    }

    public void setHash(String key,String item, Object value) {
        setHash(key,item,value,NOT_EXPIRE);
    }

    public void setHash(String key,String item, Object value,long expire) {
        hashOperations.put(key,item,value);
        setNotExpire(key,expire);
    }



    public Object getHash(String key, String item,long expire) {
        Object obj = hashOperations.get(key,item);
        setNotExpire(key,expire);
        return obj;
    }

    public Object getHash(String key, String item) {
        return getHash(key,item,NOT_EXPIRE);
    }

    public Map<String,Object> getHash(String key,long expire) {
        Map<String,Object> map = hashOperations.entries(key);
        setNotExpire(key,expire);
        return map;
    }

    public Map<String,Object> getHash(String key) {
        return getHash(key,NOT_EXPIRE);
    }

    public Long setSet(String key, Object...values) {
        return setSet(key,NOT_EXPIRE,values);
    }

    public Long setSet(String key,long expire, Object...values ) {
        Long count = setOperations.add(key,values);
        setNotExpire(key,expire);
        return count;
    }



    public Set<Object> getSet(String key) {
        return setOperations.members(key);
    }

    public Long getSetSize(String key) {
        return setOperations.size(key);
    }

    public Long removeSet(String key, Object... values) {
        return setOperations.remove(key,values);
    }

    public Long setList(String key, Object value) {
        return setList(key, value,NOT_EXPIRE);
        //return setList(key, value);
    }

    public Long setList(String key, Object value,long expire) {
        Long count = listOperations.rightPush(key, value);
        setNotExpire(key,expire);
        return count;
    }

    public Long setList(String key, List<Object> value) {
        return setList(key,value,NOT_EXPIRE);
    }

    public Long setList(String key, List<Object> value,long expire) {
        Long count = listOperations.rightPushAll(key,value);
        setNotExpire(key,expire);
        return count;
    }

    public Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public List<Object> getList(String key) {
        return getList(key, 0, -1);
    }

    public List<Object> getList(String key, long start, long end) {
        return listOperations.range(key, start, end);
    }

    public Object getListIndex(String key, long index) {
        return listOperations.index(key, index);
    }

    public Long removeList(String key, long count, Object value) {
        return listOperations.remove(key, count, value);
    }

    public boolean isKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public boolean isKeyHash(String key, String item) {
        return hashOperations.hasKey(key, item);
    }

    public void delete(String...key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    public long getExpireByKey(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    private void setNotExpire(String key,long expire) {
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void expire(String key,long expire) {
        if (expire < -1) {
            return;
        }
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }
    /**
     * Object转成JSON数据
     */
    private String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz){
        return JSON.parseObject(json, clazz);
    }


}

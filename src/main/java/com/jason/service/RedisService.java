package com.jason.service;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.applet.Main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by jason on 2017/12/17.
 */
@Service
public class RedisService {

    //使用redisTemplate模板
    //redisTemplate是需要自己实现序列化,并且实现序列化的话我们就可以往数据库中插入对象
    @Autowired
    private RedisTemplate redisTemplate;

    public void afterPropertiesSet() throws Exception{
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //TODO 效率优化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
    }

    //使用StringRedisTemplate模板
    //这里的StringRedisTemplate是完全用来操作字符进行操作
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 对String 进行操作
     * @param key
     * @param value
     */
    public void setStr(String key, String value) {
        //一般在写代码的时候,我们是一般是空参数调用有多参数的方法
        this.setStr(key, value, null);
    }

    /**
     * 对String 进行操作
     * @param key
     * @param value
     * @param time 指定过期时间
     */
    public void setStr(String key, String value, Long time) {
        stringRedisTemplate.opsForValue().set(key, value);
        if (time != null)
            //timeUnit 指定过期的单位
            stringRedisTemplate.opsForValue().set(key,value, time, TimeUnit.SECONDS);
    }

    /**
     *
     * 对String 进行操作
     * @param key
     * @param Value
     * @param offset 设定的偏移量,注意下标从0开始,开始将value替换从offset开始之后的值
     */
    public void setStrFromoffset(String key,String Value ,long offset){
        try {
            stringRedisTemplate.opsForValue().set(key,Value,offset);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("redis 中 没有此"+ key +" 对应的"+ Value);
        }
    }

    /**
     * 判断是否存在改Key value,如果存在返回false ,不过不存在返回true
     * @param key
     * @param value
     * @return
     */
    public  boolean setIfAbsent(String key , String value){
       return  stringRedisTemplate.opsForValue().setIfAbsent(key,value);
    }

    /**
     * 为多个键分别设置它们的值
     * @param m
     */
    public void multiSet(Map<? extends String, ? extends String> m){
        stringRedisTemplate.opsForValue().multiSet(m);
    }

    /**
     * 获得多个键对应的值
     * @param collection 获得多个键对应的值
     * @return
     */
    public Object muliGet(Collection<String> collection){
        return stringRedisTemplate.opsForValue().multiGet(collection);
    }

    /**判断是否存在多个键对应的值
     * 如果存在则返回false,如果不存在则返回true
     * @param var1
     * @return
     */
    public boolean multiSetIfAbsent(Map<? extends String, ? extends String> var1){
        return stringRedisTemplate.opsForValue().multiSetIfAbsent(var1);
    }

    /**
     * 获取Key对应的值
     * @param key
     * @return
     */
    public Object getKey(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 获取key对应的value,并且设置新的value
     * @param key
     * @param value
     * @return
     */
    public Object getAndSet(String key ,String value){
        return stringRedisTemplate.opsForValue().getAndSet(key,value);
    }

    /**
     * 返回多个键对应的值
     * @param var1
     * @return
     */
    public List<String> multiGet(Collection<String> var1){
        return   stringRedisTemplate.opsForValue().multiGet(var1);
    }

    /**
     * 如果key已经存在并且是一个字符串，则该命令将该值追加到字符串的末尾。如果键不存在，则它被创建并设置为空字符串
     * @param key
     * @param value
     */
    public void append(String key ,String value){
        stringRedisTemplate.opsForValue().append(key,value);
    }
    /**
     * 删除对应key
     * @param key
     */
    public void delKey(String key) {
        stringRedisTemplate.delete(key);
    }
    /**
     * 返回存储在键中的列表的指定元素。偏移开始和停止是基于零的索引，
     * 其中0是列表的第一个元素（列表的头部），1是下一个元素
     * @param var1
     * @param var2
     * @param var4
     * @return
     */
    public List<String> range(String var1, long var2, long var4){
        return stringRedisTemplate.opsForList().range(var1,var2,var4);
    }

    /**
     *将所有指定的值插入存储在键的列表的头部。如果键不存在，
     * 则在执行推送操作之前将其创建为空列表。（从左边插入）
     * @param key
     * @param value
     */
    public  long  leftPush(String key,String value){
        return  stringRedisTemplate.opsForList().leftPush(key,value);
    }

    /**
     * 批量把一个数组插入到列表中
     * @param var1
     * @param var2
     * @return
     */
    public  long leftPushAll(String var1, String... var2){
        return stringRedisTemplate.opsForList().leftPushAll(var1,var2);
    }

    /**
     * 批量把一个集合插入到列表中
     * @param var1
     * @param var2
     * @return
     */
    public long  leftPushAll(String var1, Collection<String> var2){
        return stringRedisTemplate.opsForList().leftPushAll(var1,var2);
    }

    /**
     * 这样可以通过var1和var2和var3可以存储我们的对象
     * @param var1
     * @param var2
     * @param var3
     */
    public void HashPut(String var1, Object var2, Object var3){
    redisTemplate.opsForHash().put(var1,var2,var3);
    }

    /**
     * 通过key1 和key2可以获取对象
     * @param var1
     * @param var2
     * @return
     */
    public Object HashGet(String var1, Object var2){
       return redisTemplate.opsForHash().get(var1,var2);
    }

    /**
     * 往set中添加集合
     * @param var1 指代的是key
     * @param var2 指代的是value的集合
     * @return
     */
    public long SetAdd(String var1, String... var2){
        return stringRedisTemplate.opsForSet().add(var1,var2);
    }

    /**
     *
     * @param var1 需要删除key
     * @param var2 需要删除集合的列表或者是列表集合的key
     * @return
     */
    public long SetRemove(String var1, Object... var2){
        return stringRedisTemplate.opsForSet().remove(var1,var2);
    }

    /**
     * 向数据库中插入对象
     * @param redisKey
     * @param value
     */
   public void ValueSet(String redisKey,Object value){
        redisTemplate.opsForValue().set(redisKey,value,100L,TimeUnit.SECONDS);
   }

    /**
     *  从redis中获取对象
     * @return 直接从数据库中返回对象
     */
   public Object ValueGet(String key){
   return   redisTemplate.opsForValue().get(key);
   }
}

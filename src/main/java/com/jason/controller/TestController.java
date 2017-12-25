package com.jason.controller;

import com.jason.dao.PersonMapper;
import com.jason.entity.Person;
import com.jason.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jason on 2017/12/17.
 */
@RestController
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);


    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/test")
    public String test(){
        redisService.setStr("jason","jason");
        return "success";
    }

    @RequestMapping("/getValue")
    public Object getValue(String key){
        Object key1 = redisService.getKey(key);
        return key1;
    }

    @RequestMapping("/InsertKeyAndValue")
    public String InsertKeyAndValue(String key,String value){
      redisService.setStr(key,value,100L);
      return "success";
    }

    /** 测试opsForValue().get()方法
     * 测试向数据库中插入对象
     */
    @RequestMapping("/InsertPerson1")
    public String InsertPerson1(){
        Person p  = new Person();
        p.setSchool("aa大学");
        p.setName("xyz");
        p.setId(1);
        redisService.ValueSet(p.getName(),p);
        return "success";
    }

    /**
     * 测试opsForValue().get()方法
     * @return 返回是否成功
     */
    @RequestMapping("/GetPerson")
    public Object GetPerson(String key){
      Person p = (Person) redisService.ValueGet(key);
      return p;
    }

    /**
     * 测试hashput方法
     * 发现同样是可以成功的
     */
    @RequestMapping("/HashPut")
    public String HashPut(){
        Person p = new Person();
        p.setId(1);
        p.setName("jerry");
        p.setSchool("aa大学");
        redisService.HashPut("key",p.getName(),p);
        return "success";
    }

    @RequestMapping("/HashGet")
    public Object HashGet(String key){
        Person person = (Person) redisService.HashGet("key", key);
        return person;
    }

    /**
     * 此方法用来测试数据库是否连接成功
     * @return
     */
    @RequestMapping("/TestDb")
    public String TestDb(){
        return personMapper.SelectPerson("jason").toString();
    }

   @RequestMapping("/SelectPerson")
    public Object SelectPerson(String key){
        log.info("是否进入该方法");
        Person p = null;
      //先从缓存中取,如果没有,则到数据库中取
       Object o = redisService.ValueGet(key);
       if(o == null){
           log.info("第一次缓存中没有该用户的信息");
          p = personMapper.SelectPerson(key);
          log.info("从数据库中取用户的信息");
       }else{
           p = (Person) o;
           log.info("从redis中间直接取的person对象");
       }
      //将用户信息放到redis中
       redisService.ValueSet(key,p);
       return p;
    }

}

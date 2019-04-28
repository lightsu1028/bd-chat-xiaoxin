package com.em.controller;

import com.em.common.Result;
import com.em.model.MenuNode;
import com.em.service.MenuService;
import com.em.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lu Baikang on 2017/10/25.
 */
//@RestController
//@RequestMapping("/login")
public class TestRedisController extends  BaseController{

//    @Autowired
//    private Producer kaptchaProducer;

//    @Autowired
//    private UserInfoService userInfoService;

    @Autowired
    private MenuService menuService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedisTemplate redisTemplate;


    @RequestMapping(value="/menu",method = RequestMethod.POST)
    public Result menu(@RequestParam(value="pId",required = true)String pId){
        List<MenuNode> allMenus = menuService.getAllMenus(Integer.parseInt(pId));
        return new Result(allMenus);
    }

    @RequestMapping("multi")
    public String multi(@RequestParam("key") String key){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        List<Object> txResults = (List<Object>) redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {

                operations.multi();
                operations.opsForValue().set(key,30);
//                operations.opsForValue().increment(key, 1L);
                return operations.exec();
            }
        });
        System.out.println("Number of items added to set: " + txResults.get(0));
        return txResults.get(0) + "";
    }

    @RequestMapping("eval")
    public String eval(@RequestParam("k1") String k1,@RequestParam("v1") String v1,@RequestParam("v1") String v2){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        String script = "local" +
                " current = redis.call('GET', KEYS[1])" +
                " if current == ARGV[1]" +
                "   then redis.call('SET', KEYS[1], ARGV[2])" +
                "   return true" +
                " end" +
                " return false";
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<Boolean>();
        redisScript.setScriptText(script);
        redisScript.setResultType(Boolean.class);
        Object key = redisTemplate.execute(redisScript, Collections.singletonList(k1), v1, v2);
        return "success";
    }

    @RequestMapping("set")
    public String set(@RequestParam("key") String key,@RequestParam("value") String value){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(key,value);
        return "success";
    }

    @RequestMapping("get")
    public String get(@RequestParam("key") String key){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        String sr = (String)redisTemplate.opsForValue().get(key);
        return "sr";
    }


    @RequestMapping("incr")
    public String incr(@RequestParam("k") String k,
                       @RequestParam("time") String time){
//        final long rawTimeout = TimeoutUtils.toMillis(Long.parseLong(time), TimeUnit.SECONDS);
        long specificTime = getSpecificTime();
//        long seconds = specificTime / 1000L;
        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());

        String script = "local" +
                " current = redis.call('incr', KEYS[1])" +
                " if tonumber(current) == 1" +
                "   then redis.call('pexpireat', KEYS[1], ARGV[1])" +
                " end";
        DefaultRedisScript redisScript = new DefaultRedisScript();
        redisScript.setScriptText(script);
//        redisScript.setResultType(Boolean.class);
//        Object key = redisTemplate.execute(redisScript,Collections.singletonList(k),time);
       redisTemplate.execute(redisScript,new StringRedisSerializer(),new StringRedisSerializer(),Collections.singletonList(k),specificTime+"");

        Long incr = redisUtil.getIncr(k);
        return incr+"";
    }

    public static long getSpecificTime(){
        long current = System.currentTimeMillis();
        long afterExpire = current + 180 * 1000L;
        return afterExpire;
    }

    public static void main(String[] args) {
        long specificTime = getSpecificTime();
        System.out.println(new Date(specificTime).toLocaleString());
    }

    @RequestMapping("incr2")
    public String incr2(){
        Long k1 = redisTemplate.opsForValue().increment("k1", 1L);
        return k1+"";
    }

    @RequestMapping("getincr2")
    public String getincr2(){
        Object k1 = redisTemplate.opsForValue().get("k1");
//        String result  = new String(k11);
        return "success";
    }


}

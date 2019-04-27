package com.em.bd.bddemo;

import com.em.utils.RedisUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BdDemoApplicationTests {

//    @Before
//    public void init(){
//        RedisUtil redisUtil = new RedisUtil();
//    }

	@Test
	public void contextLoads() {
        RedisUtil redisUtil = new RedisUtil();
        boolean flag = redisUtil.set("hero", "Libai");
        if(flag){
            Object result = redisUtil.get("hero");
            System.out.println(result);
        }

    }

}

package com.em.bd.bddemo;

import com.em.utils.RedisUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author Baikang Lu
 * @date 2018/9/4
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DelayQue {
    private static final String QUEUE_NAME = "deplay_queue";

    @Autowired
    private RedisUtil redisUtil;

    //    @Before
//    public void setUp(){
//
//    }

// 模拟任务处理队列
    public static void addToTaskQue(String taskInfo) {
        System.out.println(taskInfo + "已经从延时队列中转至队列" + "当前时间:" + new Date().toLocaleString());
        System.out.println();
    }

    public void addToDeplayQueue(Task task){
        System.out.println(task.toString()+ "已经加入延时队列");
        redisUtil.zAdd(QUEUE_NAME,task.getTime(),task.toString());
    }


    public void transferFromDelayQueue() throws InterruptedException{
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                long curTime = System.currentTimeMillis();
//                Set<String> items = redisUtil.zGet(QUEUE_NAME, 0L, curTime);
//                Iterator<String> iterator = items.iterator();
//                while(iterator.hasNext()){
//                    addToTaskQue(iterator.next());
//                    redisUtil.zRemove(QUEUE_NAME,iterator.next());
//                }
//            }
//        },0,1000);
//

        while(true){
            //按位置元素，每次取出一个，按元素score小==>大取出
            Set<ZSetOperations.TypedTuple<String>> items = redisUtil.zrangeWithScores(QUEUE_NAME, 0L, 0L);

            if(items!=null&&!items.isEmpty()){
                ZSetOperations.TypedTuple<String> tuple = items.iterator().next();
                if(System.currentTimeMillis() >= tuple.getScore()){
                    // TODO 获取锁
                    redisUtil.zRemove(QUEUE_NAME,tuple.getValue());
                    addToTaskQue(tuple.getValue()); //任务推入延时队列，因为这里只是延时
                    // TODO 释放锁
                }

            }

            Thread.sleep(100);
        }
    }

    @Test
    public void run() throws InterruptedException {
        long now = System.currentTimeMillis();
        System.out.println("当前时间"+new Date(now).toLocaleString());
        Long time1 = now+10*1000;
        Long time2 = now+20*1000;
        Long time3 = now+30*1000;
        String time1Desc = new Date(time1).toLocaleString();
        String time2Desc = new Date(time2).toLocaleString();
        String time3Desc = new Date(time3).toLocaleString();

        Task task;
        task = new Task(UUID.randomUUID().toString(), now+30*1000, time3Desc+"后执行");
        addToDeplayQueue(task);
        task = new Task(UUID.randomUUID().toString(), now+20*1000, time2Desc+"后执行");
        addToDeplayQueue(task);
        task= new Task(UUID.randomUUID().toString(), now+10*1000, time1Desc+"后执行");
        addToDeplayQueue(task);


//        task = new Task(UUID.randomUUID().toString(), now+40*1000, 40*1000+"后执行");
        transferFromDelayQueue();
    }

    @Test
    public void zGet(){

        Set<String> strings = redisUtil.zGet(QUEUE_NAME, 0L, System.currentTimeMillis());
        if(strings!=null||strings.size()>0){
            for(String task:strings){
                System.out.println(task);
                redisUtil.zRemove(QUEUE_NAME,task);
            }
        }
    }

    @Test
    public void zAddTest(){
        redisUtil.zAdd("zset2",10L,"jack");
        redisUtil.zAdd("zset2",2L,"jim");
        redisUtil.zAdd("zset2",4L,"tom");
        redisUtil.zAdd("zset2",7L,"lucy");

    }



    @Test
    public void  zGetTest() throws InterruptedException {
        while(true){
            Set<ZSetOperations.TypedTuple<String>> strings = redisUtil.zrangeWithScores("zset2", 0L, 0L);
            ZSetOperations.TypedTuple<String> tuple = strings.iterator().next();
//            strings.forEach(e-> System.out.println(e.getValue()+"=======>"+e.getScore()));
            System.out.println(tuple.getValue()+"=======>"+tuple.getScore());
            redisUtil.zRemove("zset2",tuple.getValue());
            Thread.sleep(1000);
        }

    }


    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("执行Time任务"+new Date().toLocaleString());
            }
        },0,5000);
    }

    static class Task {
        // 任务id
        private String id;
        // 任务执行时间
        private long time;
        // 描述
        private String desc;

        public Task(String id, long time, String desc) {
            this.id = id;
            this.time = time;
            this.desc = desc;
        }

        public String getId() {
            return id;
        }

        public long getTime() {
            return time;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public String toString() {
            return "Task [id=" + id + ", time=" + time + ", desc=" + desc + "]";
        }
    }

}

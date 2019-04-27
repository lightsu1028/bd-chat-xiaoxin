package com.em.controller;

import com.em.utils.RedisUtil;

import java.util.*;

/**
 * @author Baikang Lu
 * @date 2018/9/4
 */
public class DelayQueTest {
    private static final String QUEUE_NAME = "deplay_queue";
    private RedisUtil redisUtil;
    public DelayQueTest(){
        redisUtil = new RedisUtil();
    }

    // 模拟任务处理队列
    public static void addToTaskQue(String taskInfo) {
        System.out.println(taskInfo + "已经从延时队列中转至队列" + "当前时间:" + new Date().toLocaleString());
        System.out.println();
    }

    public void addToDeplayQueue(Task task){
        System.out.println(task.toString()+ "已经加入延时队列");
        redisUtil.zAdd(QUEUE_NAME,task.getTime(),task.toString());
    }


    public void transferFromDelayQueue() throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long curTime = System.currentTimeMillis();
                Set<String> items = redisUtil.zGet(QUEUE_NAME, 0L, curTime);
                Iterator<String> iterator = items.iterator();
                while (iterator.hasNext()) {
                    addToTaskQue(iterator.next());
                    redisUtil.zRemove(QUEUE_NAME, iterator.next());
                }
            }
        }, 0, 1000);
    }

    public void run() throws InterruptedException {
        long now = System.currentTimeMillis();
        System.out.println("当前时间"+new Date(now).toLocaleString());
        Long time1 = now+10*1000;
        Long time2 = now+20*1000;
        Long time3 = now+30*1000;
        String time1Desc = new Date(time1).toLocaleString();
        String time2Desc = new Date(time1).toLocaleString();
        String time3Desc = new Date(time1).toLocaleString();

        Task task = new Task(UUID.randomUUID().toString(), now+10*1000, time1Desc+"后执行");
        addToDeplayQueue(task);
        task = new Task(UUID.randomUUID().toString(), now+20*1000, time2Desc+"后执行");
        addToDeplayQueue(task);
        task = new Task(UUID.randomUUID().toString(), now+30*1000, time3Desc+"后执行");
        addToDeplayQueue(task);
//        task = new Task(UUID.randomUUID().toString(), now+40*1000, 40*1000+"后执行");
        transferFromDelayQueue();
    }

    public static void main(String[] args) throws Exception {
        DelayQueTest delayQue = new DelayQueTest();
        delayQue.run();
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

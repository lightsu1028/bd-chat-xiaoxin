package com.em.bd.bddemo;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** 环形延时队列
 * @author Baikang Lu
 * @date 2018/9/4
 */
public class CircleQue {

    private static int currentIndex = -1;
    private static Slot[] slots;
    private ExecutorService pool = Executors.newFixedThreadPool(1);


    public CircleQue(int size) {
        //初始化槽
        this.slots= new Slot[size];
        for (int i = 0; i < size; i++) {
            slots[i] = new Slot();
        }
    }

    public int scanQue() {
        this.currentIndex++;
        int index = this.currentIndex % slots.length;
        return index;
    }

    static class Slot {
        //此处使用set结构，也可以使用que类结构
        private Set<Task> tasks = new HashSet<>();

        public Set<Task> getTasks() {
            return tasks;
        }

    }

    static class Task {
        private int cycleNum;
        private int index;
        private String id;
        private String msg;
        private Long delayTime;

        @Override
        public String toString() {
            return "Task{" +
                    "id='" + id + '\'' +
                    ", msg='" + msg + '\'' +
                    ", delayTime=" + delayTime +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Task task = (Task) o;

            if (!id.equals(task.id)) return false;
            if (!msg.equals(task.msg)) return false;
            return delayTime.equals(task.delayTime);
        }

        @Override
        public int hashCode() {
            int result = id.hashCode();
            result = 31 * result + msg.hashCode();
            result = 31 * result + delayTime.hashCode();
            return result;
        }

        public Task(String id, String msg, Long delayTime) {
            this.id = id;
            this.msg = msg;
            this.delayTime = delayTime;
            this.index = (int)(delayTime+currentIndex)%slots.length;
            this.cycleNum = (int)(this.delayTime/slots.length);
        }


        public void cycleNumDecrease() {
            cycleNum--;
        }

        public int getIndex() {
            return index;
        }
    }

    public void addTask(Task task) {
        Slot slot = slots[task.getIndex()];
        Set<Task> tasks = slot.getTasks();
        System.out.println("加入环形队列" + task.toString() + new Date().toLocaleString());
        tasks.add(task);
    }

    public void startUp() {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                while(true){
                    int currentIndex = scanQue();
                    Slot slot = slots[currentIndex];

                    Set<Task> tasks = slot.getTasks();
                    if (tasks != null || !tasks.isEmpty()) {
                        for (Task task : tasks) {
                            if (task.cycleNum > 0) {
                                task.cycleNumDecrease();
                            } else {
                                tasks.remove(task);
                                //考虑异步执行任务
                                System.out.println("正在执行任务：" + task.toString() + new Date().toLocaleString());
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000L);//控制延时精度1s
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }




    public static void main(String[] args) {
        Task task = new Task(UUID.randomUUID().toString(), "任务1", 10L);
        Task task2 = new Task(UUID.randomUUID().toString(), "任务2", 30L);
        Task task3 = new Task(UUID.randomUUID().toString(), "任务3", 15L);

        CircleQue circleQue = new CircleQue(3600);
        circleQue.startUp();
        circleQue.addTask(task);
        circleQue.addTask(task2);
        circleQue.addTask(task3);

    }
}

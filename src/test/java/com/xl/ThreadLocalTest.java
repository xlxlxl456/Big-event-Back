package com.xl;

import org.junit.jupiter.api.Test;

public class ThreadLocalTest {

    @Test
    public void testTreadLocalSetAndGet(){
        ThreadLocal tl = new ThreadLocal();

        new Thread(()->{
            tl.set("aaa");
            System.out.println(Thread.currentThread().getName()+":"+tl.get());
        },"Blue").start();

        new Thread(()->{
            tl.set("bbb");
            System.out.println(Thread.currentThread().getName()+":"+tl.get());
        },"Yellow").start();
    }
}

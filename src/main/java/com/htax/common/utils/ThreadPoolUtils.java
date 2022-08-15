package com.htax.common.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @BelongsProject: htax-web-master
 * @BelongsPackage: com.htax.common.utils
 * @Author: ppzz
 * @CreateTime: 2022-08-11  20:12
 * @Description: TODO 线程池工具类
 * @Version: 1.0
 */
public class ThreadPoolUtils<T> {
    protected static Logger logger = LoggerFactory.getLogger(ThreadPoolUtils.class);
    // 等待队列长度
    private static final int BLOCKING_QUEUE_LENGTH = 1000;
    // 闲置线程存活时间
    private static final int KEEP_ALIVE_TIME = 60;
    // 闲置线程存活时间单位
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    // 线程名称
    private static final String THREAD_NAME = "myPool";

    // 线程池执行器
    private static ThreadPoolExecutor executor;

    // 私有化构造子,阻止外部直接实例化对象
    private ThreadPoolUtils() {
    }

    /**
     * 获取单例的线程池对象--单例的双重校验
     *
     * @return 线程池
     */
    public static ThreadPoolExecutor getThreadPool() {
        if (executor == null) {
            synchronized (ThreadPoolUtils.class) {
                if (executor == null) {
                    // 获取处理器数量
                    int cpuNum = Runtime.getRuntime().availableProcessors();
                    // 根据cpu数量,计算出合理的线程并发数
                    int maximumPoolSize = cpuNum * 2 + 1;
                    //
                    executor = new ThreadPoolExecutor(
                            // 核心线程数
                            maximumPoolSize - 1,
                            // 最大线程数
                            maximumPoolSize,
                            // 活跃时间
                            KEEP_ALIVE_TIME,
                            // 活跃时间单位
                            KEEP_ALIVE_TIME_UNIT,
                            // 线程队列
                            new LinkedBlockingDeque<>(BLOCKING_QUEUE_LENGTH),
                            // 线程工厂
                            Executors.defaultThreadFactory(),
                            // 队列已满,而且当前线程数已经超过最大线程数时的异常处理策略（这里可以自定义拒绝策略）
                            new ThreadPoolExecutor.AbortPolicy() {
                                @Override
                                public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                                    logger.warn("线程等待队列已满，当前运行线程总数：{}，活动线程数：{}，等待运行任务数：{}",
                                            e.getPoolSize(),
                                            e.getActiveCount(),
                                            e.getQueue().size());
                                }
                            }
                    );
                }
            }
        }
        return executor;
    }

    /**
     * 向线程池提交一个任务,返回线程结果
     *
     * @param callable 任务
     * @return 处理结果
     */
    public static <T> Future<T> submit(Callable<T> callable) {
        return getThreadPool().submit(callable);
    }

    /**
     * 向线程池提交一个任务，不关心处理结果
     *
     * @param runnable 任务
     */
    public static void execute(Runnable runnable) {
        if (runnable == null) throw new NullPointerException();
        getThreadPool().execute(runnable);
    }

    /**
     * 获取当前线程池线程数量
     */
    public static int getSize() {
        return getThreadPool().getPoolSize();
    }

    /**
     * 获取当前活动的线程数量
     */
    public static int getActiveCount() {
        return getThreadPool().getActiveCount();
    }

    /**
     * 从线程队列中移除对象
     */
    public static void cancel(Runnable runnable) {
        if (executor != null) {
            getThreadPool().getQueue().remove(runnable);
        }
    }
}

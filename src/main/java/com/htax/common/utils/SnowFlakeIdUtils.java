package com.htax.common.utils;

/**
 * @author ppzz
 * @date 2022-05-13 15:57
 */
public class SnowFlakeIdUtils {
    // 开始时间戳
    private final long twepoch = 687888001020L;
    // 机器ID所占位数
    private final long workerIdBits = 10L;
    // 支持最大机器ID，结果是1023
    private final long maxWorkerId = -1L ^(-1L << workerIdBits);
    // 序列在id中站的位数
    private final long sequenceBits = 12L;
    // 机器id向做移12位
    private final long  workerIdShift = sequenceBits;

    // 时间向左移动22位
    private final long timesTampLeftShift = sequenceBits + workerIdBits;

    // 生成序列编码
    private final long sequenceMask = -1L ^ (1L << sequenceBits);

    // 工作机器ID
    private long workerId;

    // 毫秒内序列
    private long sequence = 0L;
    // 上次生成时间
    private long lastTimeStamp = -1L;

    public SnowFlakeIdUtils(long workerId){
        if (workerId>maxWorkerId || workerId < 0){
            throw new IllegalArgumentException(String.format("失败！"));
        }
        this.workerId = workerId;
    }
    // 获得下一个ID
    public synchronized  long nextId(){
        long timestamp = timeGen();
        if (timestamp < lastTimeStamp){
            throw  new RuntimeException("失败");
        }
        // 如果同一个时间生成的，则进行毫秒内序列化
        if (lastTimeStamp == timestamp){
            // 如果毫秒相同，则从0递增生成序列号
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内内存溢出
            if (sequence == 0){
                // 阻塞到下一个毫秒，获取新的时间戳
                timestamp = tilNextMills(lastTimeStamp);
            }
        }else { // 时间戳改变，毫秒内序列重置
            sequence = 0L;
        }
        // 上次生成的ID时间戳
        lastTimeStamp = timestamp;
        // 移位并通过或运算拼到一起组成位的ID
        return ((timestamp - twepoch) << timesTampLeftShift) | (workerId << workerIdShift) | sequence;
    }


    // 阻塞到下一个毫秒，知道获取最新的时间戳
    protected long tilNextMills(long lastTimeStamp){
        long timestamp = timeGen();
        while (timestamp <= lastTimeStamp){
            timestamp = timeGen();
        }
        return timestamp;
    }
    // 返回以毫秒为单位的当前时间
    protected long timeGen(){
        return System.currentTimeMillis();
    }

}

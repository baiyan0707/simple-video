package com.simple.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * @ClassName OrderIdUtils
 * @Author BaiYanHu
 * @Date 2019/11/15 17:58
 * @Version 1.0
 * @description
 **/
public class OrderIdUtils {
    // 这里读取的是配置文件
    // 机器id(我这里是01,正式环境建议使用机器IP)
    // 注意：分布式环境,注意每台机器的id要保证不同;也可以使用机器ip,映射成一个数字编号（如01:192.168.55.12）
    private static String myid= "127.0.0.1";
    // 示例
    private static OrderIdUtils instance = new OrderIdUtils(myid);
    //机器id 2位
    private final String machineId;
    // 序列号的最大值
    private final int sequenceMax = 9999;
    // 最近的时间戳
    private long lastTimestamp=0;
    // 0，并发控制
    private long sequence = 0L;

    private OrderIdUtils(String machineId) {
        this.machineId = machineId;
    }

    private static OrderIdUtils getInstance() {
        return instance;
    }

    /**
     *
     * @Title: 获取订单号
     * @return String
     * @Description:
     * @author: 王延飞
     * @date: 2018年3月22日 下午7:56:56
     */
    public static  String getOrderId() {
        OrderIdUtils orderId = OrderIdUtils.getInstance();
        return orderId.nextId();
    }

    /**
     * 生成订单号
     */
    private synchronized String nextId(){
        Date now=new Date();
        String time= DateFormatUtils.format(now,"yyyyMMddHHmmssSSS");
        long timestamp = now.getTime();
        if (this.lastTimestamp == timestamp) {
            // 如果上一个timestamp与新产生的相等，则sequence加一(0-4095循环);
            // 对新的timestamp，sequence从0开始
            this.sequence = this.sequence + 1 % this.sequenceMax;
            if (this.sequence == 0) {
                // 重新生成timestamp
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }
        this.lastTimestamp= timestamp;
        return time + machineId + leftPad(sequence);
    }

    /**
     * 补码
     * @param i
     * @return
     */
    private String leftPad(long i){
        String s = String.valueOf(i);
        StringBuilder sb=new StringBuilder();
        int c= 1 -s.length();
        c= Math.max(c, 0);
        for (int t=0;t<c;t++){
            sb.append("0");
        }
        return sb.append(s).toString();
    }

    /**
     * 等待下一个毫秒的到来, 保证返回的毫秒数在参数lastTimestamp之后
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}

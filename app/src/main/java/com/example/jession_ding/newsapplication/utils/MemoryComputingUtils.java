package com.example.jession_ding.newsapplication.utils;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/29 15:15
 * Description 内存计算
 */
public class MemoryComputingUtils {
    private static final String TAG = "MemoryComputingUtils";

    // 最大内存
    public static long getMaxMemory() {
        // 单位：比特(byte)
        long maxMemory = Runtime.getRuntime().maxMemory() / 1024;
        LogUtil.i(TAG,"maxMemory = " + maxMemory);
        return maxMemory;
    }

    // 剩余内存
    public static long getFreeMemory() {
        // 单位：比特(byte)
        long freeMemory = Runtime.getRuntime().freeMemory() / 1024;
        LogUtil.i(TAG,"freeMemory = " + freeMemory);
        return freeMemory;
    }
    // 使用的内存 = 最大内存 - 剩余内存
    // long usedMemory = maxMemory - freeMemory;
}
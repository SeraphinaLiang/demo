package com.example.strangeclass.util;

import java.sql.Timestamp;

public class MyTimestamp {
    private static long GMT_0800 = 28800000L;

    /**
     * 获取格林尼治标准时间戳
     *
     * @return
     */
    public static Timestamp getTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取东八区时间戳
     *
     * @return
     */
    public static Timestamp getTimeGmt0800() {
        return new Timestamp(System.currentTimeMillis() + GMT_0800);
    }

}

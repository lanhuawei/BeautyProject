package com.lanhuawei.beautyproject.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/15.
 * 时间工具类
 * 未完
 */

public class TimeUtil {


    public static String dtFormat(Date date, String dateFormat) {
        return getFormat(dateFormat).format(date);
    }

    public static DateFormat getFormat(String format) {
        return new SimpleDateFormat(format);
    }
}

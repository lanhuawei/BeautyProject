package com.lanhuawei.beautyproject.imageHandle.sticker.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/15.
 */

public class IOUtil {
    /**
     * 关闭流
     * @param stream 可关闭的流
     */
    public static void closeStream(Closeable stream) {
        try {
            if (stream != null)
                stream.close();
        } catch (IOException e) {

        }
    }
}

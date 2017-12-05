package com.lanhuawei.beautyproject.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/8.
 * 图片加载工具类
 * 异步加载图片
 * 未完
 */

public class UniversalImageLoadTool {
    /*
     * 如果经常出现OOM（别人那边看到的，觉得很有提的必要） ①减少配置之中线程池的大小，(.threadPoolSize).推荐1-5；
     * ②使用.bitmapConfig(Bitmap.config.RGB_565)代替ARGB_8888;
     * ③使用.imageScaleType(ImageScaleType.IN_SAMPLE_INT)或者
     * try.imageScaleType(ImageScaleType.EXACTLY)；
     * ④避免使用RoundedBitmapDisplayer.他会创建新的ARGB_8888格式的Bitmap对象；
     * ⑤使用.memoryCache(new WeakMemoryCache())，不要使用.cacheInMemory();
     */

    private static ImageLoader imageLoader = ImageLoader.getInstance();

    /**
     * 初始化图片加载类配置信息,    MyApplication
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration configuration;
        configuration = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800)// 保存的每个缓存文件的最大长宽
                .threadPoolSize(3)// 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)// 加载图片的线程数
                .denyCacheImageMultipleSizesInMemory()// 解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸。
                .discCacheFileNameGenerator(new Md5FileNameGenerator())// 设置磁盘缓存文件名称
                .tasksProcessingOrder(QueueProcessingType.LIFO)// 设置加载显示图片队列进程
                .discCacheFileCount(100)// 缓存的文件数量
                .diskCache(
                        new UnlimitedDiskCache(StorageUtil.getImgDirector())
                        // .writeDebugLogs() // Remove for release app
                ).build();

        imageLoader.init(configuration);
    }


    /**
     * 加载image,加载相册用到PhotoFolderAdapter,GridImageAdapter
     * @param uri
     * @param imageAware
     * @param default_pic
     */
    public static void disPlay(String uri, ImageAware imageAware, int default_pic) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(default_pic)
                .showImageForEmptyUri(default_pic)
                .showImageOnFail(default_pic)
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).build();

        imageLoader.displayImage(uri, imageAware, options);
    }

    /**
     * 加载image
     * PhotoCropAndStickerViewAdapter 图片裁剪和贴签大图
     * @param uri
     * @param imageView
     * @param default_pic
     */
    public static void disPlayTrue(String uri, ImageView imageView, int default_pic) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(default_pic)
                .showImageForEmptyUri(default_pic).showImageOnFail(default_pic)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).build();
        imageLoader.displayImage(uri, imageView, options);
    }

    /**
     * 重启加载
     */
    public static void resume() {
        imageLoader.resume();
    }

    /**
     * 暂停加载
     */
    public static void pause() {
        imageLoader.pause();
    }

}

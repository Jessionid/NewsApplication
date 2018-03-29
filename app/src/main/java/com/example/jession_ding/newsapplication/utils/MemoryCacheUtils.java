package com.example.jession_ding.newsapplication.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/28 16:19
 * Description 内存缓存
 */
public class MemoryCacheUtils {
    private static final String TAG = "MemoryCacheUtils";
    // static HashMap<String,Bitmap> memCache = new HashMap<String,Bitmap>();
    // 改进版(防止 OOM)
    // static HashMap<String,SoftReference<Bitmap>> memCache = new HashMap<String,SoftReference<Bitmap>>();
    // 改进第二版
    // Lru：Leaset Recently Used
    // size 为虚拟机内存大小的：1/4 或者 1/8
    /**
     * 内存缓存:
     * 因为从 Android 2.3 (API Level 9)开始，垃圾回收器会更倾向于回收持有软引用或弱引用的对象，
     * 这让软引用和弱引用变得不再可靠。  Google建议使用 LruCache
     */
    static LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (MemoryComputingUtils.getMaxMemory() / 4)) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            int byteCount = value.getByteCount();
            LogUtil.i(TAG, "byteCount = " + byteCount);
            return byteCount;//super.sizeOf(key, value);
        }
    };
    // 使用这种写法，我们所有的 bitmap 都维护在 memCache 这个集合里，GC 的时候，任何 bitmap 都无法释放
    // 随着加载的图片越来越多，该集合中维护的 bitmap,可能会造成 OOM

    // 强引用，java 中的引用，默认都是强引用。（我们之前讲的垃圾回收机制，是基于强引用）
    // 对于 GC 回收来说，如果该对象有另一个对象的到它的强引用，则该对象无法回收
    // 软引用，当 GC 回收内存的时候，一般不会去回收软引用，但是当内存不够用的时候，就可以去回收软引用的内存
    // 弱引用，GC 回收的时候，即便是内存够的时候，也有可能回收
    // 虚引用(一般用不上)，对于引用对象之间的关系，没有任何影响，配合引用队列使用。每次要回收这种对象，会
    // 把这个对象加入到一个引用队列。程序可以通过遍历这个引用队列，来看队列中是否有对应的虚引用。如果有，说明马上要被回收了。

    /**
     * <h3>Avoid Soft References for Caching</h3>
     * In practice, soft references are inefficient for caching. The runtime doesn't
     * have enough information on which references to clear and which to keep. Most
     * fatally, it doesn't know what to do when given the choice between clearing a
     * soft reference and growing the heap.
     * <p>
     * <p>The lack of information on the value to your application of each reference
     * limits the usefulness of soft references. References that are cleared too
     * early cause unnecessary work; those that are cleared too late waste memory.
     * <p>
     * <p>Most applications should use an {@code android.util.LruCache} instead of
     * soft references. LruCache has an effective eviction policy and lets the user
     * tune how much memory is allotted.
     */
    public static void saveBitmapToMem(Bitmap bitmap, String url) {
        // memCache.put(url,new SoftReference<Bitmap>(bitmap));
        Bitmap put = memCache.put(url, bitmap);
        LogUtil.i(TAG, "put = " + put);
    }

    public static Bitmap getBitmapFromMem(String url) {
/*        SoftReference<Bitmap> bitmapSoftReference = memCache.get(url);
        if(bitmapSoftReference!=null) {
            Bitmap bitmap = bitmapSoftReference.get();
            return bitmap;
        }*/
        Bitmap bitmap = memCache.get(url);
        if (bitmap != null) {
            LogUtil.i(TAG, "bitmap = " + bitmap.toString());
        } else {
            LogUtil.i(TAG, "bitmap is null!");
        }
        return bitmap;
    }
}
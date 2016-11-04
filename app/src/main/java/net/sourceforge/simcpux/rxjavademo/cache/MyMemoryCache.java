package net.sourceforge.simcpux.rxjavademo.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by BlueSky on 16/11/4.
 * 缓存文字
 */

public class MyMemoryCache extends LruCache<String, Bitmap> implements Cache {

    private static MyMemoryCache memeryCache = null;
    private static HashMap<String, SoftReference<Bitmap>> softReferenceMap = null;

    private static int maxCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public MyMemoryCache(int maxSize) {
        super(maxSize);
    }

    public  static MyMemoryCache getMyMemeryCache() {
        if (memeryCache == null) {
            memeryCache = new MyMemoryCache(maxCacheSize);
        }
        softReferenceMap = new HashMap<>();
        return memeryCache;
    }

    @Override
    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);
        //表示从内存(强引用)中移除的数据后期可能还要用到放入到软缓存
        if (evicted) {
            SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(oldValue);
            softReferenceMap.put(key, softReference);
        }
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount();
    }


    @Override
    public Bitmap getBitmap(String s) {
        Bitmap bmp = null;
        if (memeryCache.get(s) != null) {
            bmp = memeryCache.get(s);
        } else if (softReferenceMap != null) {
            SoftReference<Bitmap> softReference = softReferenceMap.get(s);
            if (softReference != null) {
                bmp = softReference.get();
            }
        }
        return bmp;
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        memeryCache.put(s, bitmap);

    }
}

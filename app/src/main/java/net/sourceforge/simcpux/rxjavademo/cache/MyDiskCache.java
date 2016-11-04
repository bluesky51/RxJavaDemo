package net.sourceforge.simcpux.rxjavademo.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by BlueSky on 16/11/4.
 */

public class MyDiskCache implements Cache{

    private static MyDiskCache myDiskCache = null;
    private static Context ctx;

    public static MyDiskCache getMyDiskCache(Context context) {
        if (myDiskCache == null) {
            myDiskCache = new MyDiskCache();
        }
        ctx = context;
        return myDiskCache;
    }

    public File getCacheFile(String name) {
        File file = new File(ctx.getExternalCacheDir(), name.substring(name.lastIndexOf("/") + 1));
        return file;
    }

    @Override
    public Bitmap getBitmap(String s) {
        File file = getCacheFile(s);
        Bitmap bmp1 = BitmapFactory.decodeFile(file.getAbsolutePath());
        if (bmp1 != null) {
            return bmp1;
        }
        return null;
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        File file = getCacheFile(s);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

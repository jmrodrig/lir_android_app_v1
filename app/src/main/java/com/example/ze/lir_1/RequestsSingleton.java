package com.example.ze.lir_1;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Ze on 11/04/2015.
 */
public class RequestsSingleton extends Application {
    private static RequestsSingleton mInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static AbstractHttpClient mClient;


    private RequestsSingleton(Context ctx){
        mClient = new DefaultHttpClient();
        mRequestQueue = Volley.newRequestQueue(ctx, new HttpClientStack(mClient));
        mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    public static synchronized RequestsSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestsSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return this.mImageLoader;
    }

    public static AbstractHttpClient getHttpClient() {
        return mClient;
    }

}

package com.duodian.admore.glide;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by duodian on 2017/12/28.
 * AdmoreGlideModule
 */

public class AdmoreGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        int cacheSize100MegaBytes = 1024 * 1024 * 100;// or any other path
        String downloadDirectoryPath = Environment.getDownloadCacheDirectory().getPath();

//        builder.setDiskCache(
//                new DiskLruCacheFactory(downloadDirectoryPath, cacheSize100MegaBytes)
//        );


//        builder.setDiskCache(
//                new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes)
//        );

        builder.setDiskCache(
                new ExternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));
    }


    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

    }
}

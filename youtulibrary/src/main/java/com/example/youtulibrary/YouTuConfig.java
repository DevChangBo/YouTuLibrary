package com.example.youtulibrary;

import android.app.Application;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import timber.log.Timber;

/**
 * ================================================================
 * 创建时间：2018/6/6 16:14
 * 创 建 人：Mr.常
 * 文件描述：存放插件的全局参数和工具
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class YouTuConfig {
    private static YouTuConfig mYouTuConfig;//单例模式
    public static final int REQUEST_CODE_HOME_TAKECAMERA = 0x01;//从插件首页跳转拍照页面的请求码

    public static YouTuConfig getInstance() {
        mYouTuConfig = mYouTuConfig == null ? new YouTuConfig() : mYouTuConfig;
        return mYouTuConfig;
    }

}

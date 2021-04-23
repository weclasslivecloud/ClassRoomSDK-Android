package com.weclassroom.lightweight;

import android.os.Build;
import android.webkit.WebView;

import androidx.multidex.MultiDexApplication;

import com.weclassroom.livecore.LiveRoomManager;
import com.weclassroom.livecore.URL;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author wangyh
 * @date 2019-08-12
 */
public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName();
            if (!"com.weclassroom.com.weclassroom.lightweight".equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }
        //是否启用web内容调试
        WebView.setWebContentsDebuggingEnabled(true);

        closeAndroidPDialog();

        LiveRoomManager.init(this, URL.ENVIRONMENT_VARIABLE.TEST);
    }

    /**
     * 去掉在Android P上的提醒弹窗
     */
    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

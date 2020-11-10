package com.flonger.liveness;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import ai.advance.liveness.lib.GuardianLivenessDetectionSDK;
import ai.advance.liveness.lib.LivenessResult;
import ai.advance.liveness.lib.Market;
import ai.advance.liveness.sdk.activity.LivenessActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class PluginDelegate implements
        Application.ActivityLifecycleCallbacks,
        PluginRegistry.RequestPermissionsResultListener,
        PluginRegistry.ActivityResultListener
{

    private final Context context;
    private final Application application;

    public PluginDelegate(PluginRegistry.Registrar registrar) {
        this.context = registrar.context();
        this.application = (Application) context;
    }

    public void callLiveness(MethodCall call, MethodChannel.Result result){
        // do something...

        Log.e("","recive click  "+call.arguments);
        HashMap keys = (HashMap) call.arguments;
        GuardianLivenessDetectionSDK.init((Application) context, (String) keys.get("accessKey"), (String) keys.get("secretKey"), Market.India);
        startLivenessActivity();
    }

    /**
     * 请求状态码
     */
    public static final int REQUEST_CODE_LIVENESS = 111;

    /**
     * 启动活体检测
     */
    private void startLivenessActivity() {
        Intent intent = new Intent(context, LivenessActivity.class);

        getActivity().startActivityForResult(intent, REQUEST_CODE_LIVENESS);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        application.unregisterActivityLifecycleCallbacks(this);

    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LIVENESS) {
            if (LivenessResult.isSuccess()) {// 活体检测成功
                String livenessId = LivenessResult.getLivenessId();// 本次活体id 成功将livenessId发给后台
                //Bitmap livenessBitmap = LivenessResult.getLivenessBitmap();// 本次活体图片
            } else {// 活体检测失败
                String errorCode = LivenessResult.getErrorCode();// 失败错误码
                String errorMsg = LivenessResult.getErrorMsg();// 失败原因
            }
        }

        return false;
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        return false;
    }

    public static Activity getActivity() {
        Class activityThreadClass = null;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.flonger.liveness;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import ai.advance.liveness.lib.GuardianLivenessDetectionSDK;
import ai.advance.liveness.lib.Market;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** LivenessPlugin */
public class LivenessPlugin implements MethodCallHandler {
  /** Plugin registration. */
  private final Context context;

  private final PluginDelegate delegate;


  public LivenessPlugin(Registrar registrar, PluginDelegate delegate){
    this.context = registrar.context();
    this.delegate = delegate;
    // 声明周期回调
    ((Application) context).registerActivityLifecycleCallbacks(delegate);

    // 权限声明回调
    registrar.addRequestPermissionsResultListener(delegate);

    // 页面跳转回调
    registrar.addActivityResultListener(delegate);

  }

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "liveness");
    final PluginDelegate delegate = new PluginDelegate(registrar);
    channel.setMethodCallHandler(new LivenessPlugin(registrar, delegate));
  }

  @Override
  public void onMethodCall(MethodCall call, final Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("callLiveness")){
      delegate.callLiveness(call,result);
      delegate.setOnResultListener(new PluginDelegate.OnResultListener() {
        @Override
        public void onResult(String livenessId) {
          result.success(livenessId);
        }
      });
    } else {
      result.notImplemented();
    }
  }
}

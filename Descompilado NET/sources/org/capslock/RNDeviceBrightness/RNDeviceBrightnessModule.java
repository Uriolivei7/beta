package org.capslock.RNDeviceBrightness;

import android.app.Activity;
import android.provider.Settings;
import android.view.WindowManager;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/* JADX INFO: loaded from: classes.dex */
public class RNDeviceBrightnessModule extends ReactContextBaseJavaModule {

    class a implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ Activity f10209b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ float f10210c;

        a(Activity activity, float f3) {
            this.f10209b = activity;
            this.f10210c = f3;
        }

        @Override // java.lang.Runnable
        public void run() {
            WindowManager.LayoutParams attributes = this.f10209b.getWindow().getAttributes();
            attributes.screenBrightness = this.f10210c;
            this.f10209b.getWindow().setAttributes(attributes);
        }
    }

    public RNDeviceBrightnessModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @ReactMethod
    public void getBrightnessLevel(Promise promise) {
        promise.resolve(Float.valueOf(getCurrentActivity().getWindow().getAttributes().screenBrightness));
    }

    @Override // com.facebook.react.bridge.NativeModule
    public String getName() {
        return "RNDeviceBrightness";
    }

    @ReactMethod
    public void getSystemBrightnessLevel(Promise promise) {
        promise.resolve(Float.valueOf(Integer.parseInt(Settings.System.getString(getCurrentActivity().getContentResolver(), "screen_brightness")) / 255.0f));
    }

    @ReactMethod
    public void setBrightnessLevel(float f3) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }
        currentActivity.runOnUiThread(new a(currentActivity, f3));
    }
}

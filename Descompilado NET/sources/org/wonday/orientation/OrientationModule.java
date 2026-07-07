package org.wonday.orientation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;
import android.view.OrientationEventListener;
import android.view.WindowManager;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class OrientationModule extends ReactContextBaseJavaModule implements org.wonday.orientation.b {
    final ReactApplicationContext ctx;
    private boolean isConfigurationChangeReceiverRegistered;
    private boolean isLocked;
    private String lastDeviceOrientationValue;
    private String lastOrientationValue;
    final OrientationEventListener mOrientationListener;
    final BroadcastReceiver mReceiver;

    class a extends OrientationEventListener {
        a(Context context, int i3) {
            super(context, i3);
        }

        @Override // android.view.OrientationEventListener
        public void onOrientationChanged(int i3) {
            Y.a.b("ReactNative", "DeviceOrientation changed to " + i3);
            String str = OrientationModule.this.lastDeviceOrientationValue;
            if (i3 == -1) {
                str = "UNKNOWN";
            } else if (i3 > 355 || i3 < 5) {
                str = "PORTRAIT";
            } else if (i3 > 85 && i3 < 95) {
                str = "LANDSCAPE-RIGHT";
            } else if (i3 > 175 && i3 < 185) {
                str = "PORTRAIT-UPSIDEDOWN";
            } else if (i3 > 265 && i3 < 275) {
                str = "LANDSCAPE-LEFT";
            }
            if (!OrientationModule.this.lastDeviceOrientationValue.equals(str)) {
                OrientationModule.this.lastDeviceOrientationValue = str;
                WritableMap writableMapCreateMap = Arguments.createMap();
                writableMapCreateMap.putString("deviceOrientation", str);
                if (OrientationModule.this.ctx.hasActiveCatalystInstance()) {
                    ((DeviceEventManagerModule.RCTDeviceEventEmitter) OrientationModule.this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("deviceOrientationDidChange", writableMapCreateMap);
                }
            }
            String currentOrientation = OrientationModule.this.getCurrentOrientation();
            if (OrientationModule.this.lastOrientationValue.equals(currentOrientation)) {
                return;
            }
            OrientationModule.this.lastOrientationValue = currentOrientation;
            Y.a.b("ReactNative", "Orientation changed to " + currentOrientation);
            WritableMap writableMapCreateMap2 = Arguments.createMap();
            writableMapCreateMap2.putString("orientation", currentOrientation);
            if (OrientationModule.this.ctx.hasActiveCatalystInstance()) {
                ((DeviceEventManagerModule.RCTDeviceEventEmitter) OrientationModule.this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("orientationDidChange", writableMapCreateMap2);
            }
        }
    }

    class b extends BroadcastReceiver {
        b() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String currentOrientation = OrientationModule.this.getCurrentOrientation();
            OrientationModule.this.lastOrientationValue = currentOrientation;
            Y.a.b("ReactNative", "Orientation changed to " + currentOrientation);
            WritableMap writableMapCreateMap = Arguments.createMap();
            writableMapCreateMap.putString("orientation", currentOrientation);
            if (OrientationModule.this.ctx.hasActiveCatalystInstance()) {
                ((DeviceEventManagerModule.RCTDeviceEventEmitter) OrientationModule.this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("orientationDidChange", writableMapCreateMap);
            }
        }
    }

    public OrientationModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.isLocked = false;
        this.isConfigurationChangeReceiverRegistered = false;
        this.lastOrientationValue = "";
        this.lastDeviceOrientationValue = "";
        this.ctx = reactApplicationContext;
        a aVar = new a(reactApplicationContext, 2);
        this.mOrientationListener = aVar;
        if (aVar.canDetectOrientation()) {
            Y.a.b("ReactNative", "orientation detect enabled.");
            aVar.enable();
        } else {
            Y.a.b("ReactNative", "orientation detect disabled.");
            aVar.disable();
        }
        this.mReceiver = new b();
        org.wonday.orientation.a.a().b(this);
    }

    private void compatRegisterReceiver(Context context, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, boolean z3) {
        if (Build.VERSION.SDK_INT < 34 || context.getApplicationInfo().targetSdkVersion < 34) {
            context.registerReceiver(broadcastReceiver, intentFilter);
        } else {
            context.registerReceiver(broadcastReceiver, intentFilter, z3 ? 2 : 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getCurrentOrientation() {
        int rotation = ((WindowManager) getReactApplicationContext().getSystemService("window")).getDefaultDisplay().getRotation();
        return rotation != 0 ? rotation != 1 ? rotation != 2 ? rotation != 3 ? "UNKNOWN" : "LANDSCAPE-RIGHT" : "PORTRAIT-UPSIDEDOWN" : "LANDSCAPE-LEFT" : "PORTRAIT";
    }

    @ReactMethod
    public void addListener(String str) {
    }

    @ReactMethod
    public void getAutoRotateState(Callback callback) {
        callback.invoke(Boolean.valueOf(Settings.System.getInt(this.ctx.getContentResolver(), "accelerometer_rotation", 0) == 1));
    }

    @Override // com.facebook.react.bridge.BaseJavaModule
    public Map<String, Object> getConstants() {
        HashMap map = new HashMap();
        map.put("initialOrientation", getCurrentOrientation());
        return map;
    }

    @ReactMethod
    public void getDeviceOrientation(Callback callback) {
        callback.invoke(this.lastDeviceOrientationValue);
    }

    @Override // com.facebook.react.bridge.NativeModule
    public String getName() {
        return "Orientation";
    }

    @ReactMethod
    public void getOrientation(Callback callback) {
        callback.invoke(getCurrentOrientation());
    }

    @ReactMethod
    public void lockToLandscape() {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }
        currentActivity.setRequestedOrientation(6);
        this.isLocked = true;
        this.lastOrientationValue = "LANDSCAPE-LEFT";
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putString("orientation", this.lastOrientationValue);
        if (this.ctx.hasActiveCatalystInstance()) {
            ((DeviceEventManagerModule.RCTDeviceEventEmitter) this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("orientationDidChange", writableMapCreateMap);
        }
        WritableMap writableMapCreateMap2 = Arguments.createMap();
        writableMapCreateMap2.putString("orientation", this.lastOrientationValue);
        if (this.ctx.hasActiveCatalystInstance()) {
            ((DeviceEventManagerModule.RCTDeviceEventEmitter) this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("lockDidChange", writableMapCreateMap2);
        }
    }

    @ReactMethod
    public void lockToLandscapeLeft() {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }
        currentActivity.setRequestedOrientation(0);
        this.isLocked = true;
        this.lastOrientationValue = "LANDSCAPE-LEFT";
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putString("orientation", this.lastOrientationValue);
        if (this.ctx.hasActiveCatalystInstance()) {
            ((DeviceEventManagerModule.RCTDeviceEventEmitter) this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("orientationDidChange", writableMapCreateMap);
        }
        WritableMap writableMapCreateMap2 = Arguments.createMap();
        writableMapCreateMap2.putString("orientation", this.lastOrientationValue);
        if (this.ctx.hasActiveCatalystInstance()) {
            ((DeviceEventManagerModule.RCTDeviceEventEmitter) this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("lockDidChange", writableMapCreateMap2);
        }
    }

    @ReactMethod
    public void lockToLandscapeRight() {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }
        currentActivity.setRequestedOrientation(8);
        this.isLocked = true;
        this.lastOrientationValue = "LANDSCAPE-RIGHT";
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putString("orientation", this.lastOrientationValue);
        if (this.ctx.hasActiveCatalystInstance()) {
            ((DeviceEventManagerModule.RCTDeviceEventEmitter) this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("orientationDidChange", writableMapCreateMap);
        }
        WritableMap writableMapCreateMap2 = Arguments.createMap();
        writableMapCreateMap2.putString("orientation", this.lastOrientationValue);
        if (this.ctx.hasActiveCatalystInstance()) {
            ((DeviceEventManagerModule.RCTDeviceEventEmitter) this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("lockDidChange", writableMapCreateMap2);
        }
    }

    @ReactMethod
    public void lockToPortrait() {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }
        currentActivity.setRequestedOrientation(1);
        this.isLocked = true;
        this.lastOrientationValue = "PORTRAIT";
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putString("orientation", this.lastOrientationValue);
        if (this.ctx.hasActiveCatalystInstance()) {
            ((DeviceEventManagerModule.RCTDeviceEventEmitter) this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("orientationDidChange", writableMapCreateMap);
        }
        WritableMap writableMapCreateMap2 = Arguments.createMap();
        writableMapCreateMap2.putString("orientation", this.lastOrientationValue);
        if (this.ctx.hasActiveCatalystInstance()) {
            ((DeviceEventManagerModule.RCTDeviceEventEmitter) this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("lockDidChange", writableMapCreateMap2);
        }
    }

    @ReactMethod
    public void lockToPortraitUpsideDown() {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }
        currentActivity.setRequestedOrientation(9);
        this.isLocked = true;
        this.lastOrientationValue = "PORTRAIT-UPSIDEDOWN";
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putString("orientation", this.lastOrientationValue);
        if (this.ctx.hasActiveCatalystInstance()) {
            ((DeviceEventManagerModule.RCTDeviceEventEmitter) this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("orientationDidChange", writableMapCreateMap);
        }
        WritableMap writableMapCreateMap2 = Arguments.createMap();
        writableMapCreateMap2.putString("orientation", this.lastOrientationValue);
        if (this.ctx.hasActiveCatalystInstance()) {
            ((DeviceEventManagerModule.RCTDeviceEventEmitter) this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("lockDidChange", writableMapCreateMap2);
        }
    }

    @Override // org.wonday.orientation.b
    public void release() {
        Y.a.b("ReactNative", "orientation detect disabled.");
        this.mOrientationListener.disable();
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }
        try {
            if (this.isConfigurationChangeReceiverRegistered) {
                currentActivity.unregisterReceiver(this.mReceiver);
                this.isConfigurationChangeReceiverRegistered = false;
            }
        } catch (Exception e4) {
            Y.a.J("ReactNative", "Receiver already unregistered", e4);
        }
    }

    @ReactMethod
    public void removeListeners(Integer num) {
    }

    @Override // org.wonday.orientation.b
    public void start() {
        Y.a.s("ReactNative", "orientation detect enabled.");
        this.mOrientationListener.enable();
        compatRegisterReceiver(this.ctx, this.mReceiver, new IntentFilter("onConfigurationChanged"), false);
        this.isConfigurationChangeReceiverRegistered = true;
    }

    @Override // org.wonday.orientation.b
    public void stop() {
        Y.a.b("ReactNative", "orientation detect disabled.");
        this.mOrientationListener.disable();
        try {
            if (this.isConfigurationChangeReceiverRegistered) {
                this.ctx.unregisterReceiver(this.mReceiver);
                this.isConfigurationChangeReceiverRegistered = false;
            }
        } catch (Exception e4) {
            Y.a.J("ReactNative", "Receiver already unregistered", e4);
        }
    }

    @ReactMethod
    public void unlockAllOrientations() {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }
        currentActivity.setRequestedOrientation(4);
        this.isLocked = false;
        this.lastOrientationValue = getCurrentOrientation();
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putString("orientation", this.lastOrientationValue);
        if (this.ctx.hasActiveCatalystInstance()) {
            ((DeviceEventManagerModule.RCTDeviceEventEmitter) this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("orientationDidChange", writableMapCreateMap);
        }
        WritableMap writableMapCreateMap2 = Arguments.createMap();
        writableMapCreateMap2.putString("orientation", "UNKNOWN");
        if (this.ctx.hasActiveCatalystInstance()) {
            ((DeviceEventManagerModule.RCTDeviceEventEmitter) this.ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("lockDidChange", writableMapCreateMap2);
        }
    }
}

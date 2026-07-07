package com.facebook.react.modules.intent;

import D2.h;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.facebook.fbreact.specs.NativeIntentAndroidSpec;
import com.facebook.react.bridge.BaseJavaModule;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.r;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "IntentAndroid")
public class IntentModule extends NativeIntentAndroidSpec {
    public static final a Companion = new a(null);
    private static final String EXTRA_MAP_KEY_FOR_VALUE = "value";
    public static final String NAME = "IntentAndroid";
    private LifecycleEventListener initialURLListener;
    private final List<Promise> pendingOpenURLPromises;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public /* synthetic */ class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f6987a;

        static {
            int[] iArr = new int[ReadableType.values().length];
            try {
                iArr[ReadableType.String.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[ReadableType.Number.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[ReadableType.Boolean.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f6987a = iArr;
        }
    }

    public static final class c implements LifecycleEventListener {
        c() {
        }

        @Override // com.facebook.react.bridge.LifecycleEventListener
        public void onHostDestroy() {
        }

        @Override // com.facebook.react.bridge.LifecycleEventListener
        public void onHostPause() {
        }

        @Override // com.facebook.react.bridge.LifecycleEventListener
        public void onHostResume() {
            IntentModule.this.getReactApplicationContext().removeLifecycleEventListener(this);
            IntentModule intentModule = IntentModule.this;
            synchronized (intentModule) {
                try {
                    Iterator it = intentModule.pendingOpenURLPromises.iterator();
                    while (it.hasNext()) {
                        intentModule.getInitialURL((Promise) it.next());
                    }
                    intentModule.initialURLListener = null;
                    intentModule.pendingOpenURLPromises.clear();
                    r rVar = r.f10584a;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public IntentModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        h.f(reactApplicationContext, "reactContext");
        this.pendingOpenURLPromises = new ArrayList();
    }

    private final void sendOSIntent(Intent intent, boolean z3) {
        String packageName;
        Activity currentActivity = getCurrentActivity();
        String packageName2 = getReactApplicationContext().getPackageName();
        PackageManager packageManager = getReactApplicationContext().getPackageManager();
        ComponentName component = packageManager == null ? intent.getComponent() : intent.resolveActivity(packageManager);
        if (component == null || (packageName = component.getPackageName()) == null) {
            packageName = "";
        }
        if (z3 || currentActivity == null || !h.b(packageName2, packageName)) {
            intent.addFlags(268435456);
        }
        if (currentActivity != null) {
            currentActivity.startActivity(intent);
        } else {
            getReactApplicationContext().startActivity(intent);
        }
    }

    private final synchronized void waitForActivityAndGetInitialURL(Promise promise) {
        this.pendingOpenURLPromises.add(promise);
        if (this.initialURLListener != null) {
            return;
        }
        this.initialURLListener = new c();
        getReactApplicationContext().addLifecycleEventListener(this.initialURLListener);
    }

    @Override // com.facebook.fbreact.specs.NativeIntentAndroidSpec
    public void canOpenURL(String str, Promise promise) {
        h.f(promise, BaseJavaModule.METHOD_TYPE_PROMISE);
        if (str == null || str.length() == 0) {
            promise.reject(new JSApplicationIllegalArgumentException("Invalid URL: " + str));
            return;
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
            intent.addFlags(268435456);
            PackageManager packageManager = getReactApplicationContext().getPackageManager();
            promise.resolve(Boolean.valueOf((packageManager == null || intent.resolveActivity(packageManager) == null) ? false : true));
        } catch (Exception e4) {
            promise.reject(new JSApplicationIllegalArgumentException("Could not check if URL '" + str + "' can be opened: " + e4.getMessage()));
        }
    }

    @Override // com.facebook.fbreact.specs.NativeIntentAndroidSpec
    public void getInitialURL(Promise promise) {
        h.f(promise, BaseJavaModule.METHOD_TYPE_PROMISE);
        try {
            Activity currentActivity = getCurrentActivity();
            if (currentActivity == null) {
                waitForActivityAndGetInitialURL(promise);
                return;
            }
            Intent intent = currentActivity.getIntent();
            String action = intent.getAction();
            Uri data = intent.getData();
            promise.resolve((data == null || !(h.b("android.intent.action.VIEW", action) || h.b("android.nfc.action.NDEF_DISCOVERED", action))) ? null : data.toString());
        } catch (Exception e4) {
            promise.reject(new JSApplicationIllegalArgumentException("Could not get the initial URL : " + e4.getMessage()));
        }
    }

    @Override // com.facebook.react.bridge.BaseJavaModule, com.facebook.react.bridge.NativeModule, com.facebook.react.turbomodule.core.interfaces.TurboModule
    public void invalidate() {
        synchronized (this) {
            try {
                this.pendingOpenURLPromises.clear();
                LifecycleEventListener lifecycleEventListener = this.initialURLListener;
                if (lifecycleEventListener != null) {
                    getReactApplicationContext().removeLifecycleEventListener(lifecycleEventListener);
                    r rVar = r.f10584a;
                }
                this.initialURLListener = null;
            } catch (Throwable th) {
                throw th;
            }
        }
        super.invalidate();
    }

    @Override // com.facebook.fbreact.specs.NativeIntentAndroidSpec
    public void openSettings(Promise promise) {
        h.f(promise, BaseJavaModule.METHOD_TYPE_PROMISE);
        try {
            Intent intent = new Intent();
            Activity currentActivity = getCurrentActivity();
            if (currentActivity == null) {
                throw new IllegalStateException("Required value was null.");
            }
            String packageName = getReactApplicationContext().getPackageName();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("package:" + packageName));
            intent.addFlags(268435456);
            intent.addFlags(1073741824);
            intent.addFlags(8388608);
            currentActivity.startActivity(intent);
            promise.resolve(Boolean.TRUE);
        } catch (Exception e4) {
            promise.reject(new JSApplicationIllegalArgumentException("Could not open the Settings: " + e4.getMessage()));
        }
    }

    @Override // com.facebook.fbreact.specs.NativeIntentAndroidSpec
    public void openURL(String str, Promise promise) {
        h.f(promise, BaseJavaModule.METHOD_TYPE_PROMISE);
        if (str == null || str.length() == 0) {
            promise.reject(new JSApplicationIllegalArgumentException("Invalid URL: " + str));
            return;
        }
        try {
            sendOSIntent(new Intent("android.intent.action.VIEW", Uri.parse(str).normalizeScheme()), false);
            promise.resolve(Boolean.TRUE);
        } catch (Exception e4) {
            promise.reject(new JSApplicationIllegalArgumentException("Could not open URL '" + str + "': " + e4.getMessage()));
        }
    }

    @Override // com.facebook.fbreact.specs.NativeIntentAndroidSpec
    public void sendIntent(String str, ReadableArray readableArray, Promise promise) {
        h.f(promise, BaseJavaModule.METHOD_TYPE_PROMISE);
        if (str == null || str.length() == 0) {
            promise.reject(new JSApplicationIllegalArgumentException("Invalid Action: " + str + "."));
            return;
        }
        Intent intent = new Intent(str);
        PackageManager packageManager = getReactApplicationContext().getPackageManager();
        if (packageManager == null || intent.resolveActivity(packageManager) == null) {
            promise.reject(new JSApplicationIllegalArgumentException("Could not launch Intent with action " + str + "."));
            return;
        }
        if (readableArray != null) {
            int size = readableArray.size();
            for (int i3 = 0; i3 < size; i3++) {
                ReadableMap map = readableArray.getMap(i3);
                if (map != null) {
                    String string = map.getString("key");
                    int i4 = b.f6987a[map.getType(EXTRA_MAP_KEY_FOR_VALUE).ordinal()];
                    if (i4 == 1) {
                        intent.putExtra(string, map.getString(EXTRA_MAP_KEY_FOR_VALUE));
                    } else if (i4 == 2) {
                        intent.putExtra(string, map.getDouble(EXTRA_MAP_KEY_FOR_VALUE));
                    } else {
                        if (i4 != 3) {
                            promise.reject(new JSApplicationIllegalArgumentException("Extra type for " + string + " not supported."));
                            return;
                        }
                        intent.putExtra(string, map.getBoolean(EXTRA_MAP_KEY_FOR_VALUE));
                    }
                }
            }
        }
        sendOSIntent(intent, true);
    }
}

package com.facebook.react.uimanager;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

/* JADX INFO: renamed from: com.facebook.react.uimanager.x, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0463x {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0463x f7634a = new C0463x();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static DisplayMetrics f7635b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static DisplayMetrics f7636c;

    private C0463x() {
    }

    public static final WritableMap a(double d4) {
        if (f7635b == null) {
            throw new IllegalStateException("DisplayMetricsHolder must be initialized with initDisplayMetricsIfNotInitialized or initDisplayMetrics");
        }
        if (f7636c == null) {
            throw new IllegalStateException("DisplayMetricsHolder must be initialized with initDisplayMetricsIfNotInitialized or initDisplayMetrics");
        }
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        C0463x c0463x = f7634a;
        DisplayMetrics displayMetrics = f7635b;
        D2.h.d(displayMetrics, "null cannot be cast to non-null type android.util.DisplayMetrics");
        writableNativeMap.putMap("windowPhysicalPixels", c0463x.b(displayMetrics, d4));
        DisplayMetrics displayMetrics2 = f7636c;
        D2.h.d(displayMetrics2, "null cannot be cast to non-null type android.util.DisplayMetrics");
        writableNativeMap.putMap("screenPhysicalPixels", c0463x.b(displayMetrics2, d4));
        return writableNativeMap;
    }

    private final WritableMap b(DisplayMetrics displayMetrics, double d4) {
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        writableNativeMap.putInt("width", displayMetrics.widthPixels);
        writableNativeMap.putInt("height", displayMetrics.heightPixels);
        writableNativeMap.putDouble("scale", displayMetrics.density);
        writableNativeMap.putDouble("fontScale", d4);
        writableNativeMap.putDouble("densityDpi", displayMetrics.densityDpi);
        return writableNativeMap;
    }

    public static final DisplayMetrics c() {
        DisplayMetrics displayMetrics = f7636c;
        if (displayMetrics == null) {
            throw new IllegalStateException("DisplayMetricsHolder must be initialized with initDisplayMetricsIfNotInitialized or initDisplayMetrics");
        }
        D2.h.d(displayMetrics, "null cannot be cast to non-null type android.util.DisplayMetrics");
        return displayMetrics;
    }

    public static final DisplayMetrics d() {
        DisplayMetrics displayMetrics = f7635b;
        if (displayMetrics == null) {
            throw new IllegalStateException("DisplayMetricsHolder must be initialized with initDisplayMetricsIfNotInitialized or initDisplayMetrics");
        }
        D2.h.d(displayMetrics, "null cannot be cast to non-null type android.util.DisplayMetrics");
        return displayMetrics;
    }

    public static final void e(Context context) {
        D2.h.f(context, "context");
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        f7635b = displayMetrics;
        DisplayMetrics displayMetrics2 = new DisplayMetrics();
        displayMetrics2.setTo(displayMetrics);
        Object systemService = context.getSystemService("window");
        D2.h.d(systemService, "null cannot be cast to non-null type android.view.WindowManager");
        ((WindowManager) systemService).getDefaultDisplay().getRealMetrics(displayMetrics2);
        f7636c = displayMetrics2;
    }

    public static final void f(Context context) {
        D2.h.f(context, "context");
        if (f7636c != null) {
            return;
        }
        e(context);
    }
}

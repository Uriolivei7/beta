package com.facebook.soloader;

import android.os.Build;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public abstract class p {
    public static void a(String str, String str2) {
        if (e(str, 3)) {
            Log.d(str, str2);
        }
    }

    public static void b(String str, String str2) {
        Log.e(str, str2);
    }

    public static void c(String str, String str2, Throwable th) {
        Log.e(str, str2, th);
    }

    public static void d(String str, String str2) {
        if (e(str, 4)) {
            Log.i(str, str2);
        }
    }

    private static boolean e(String str, int i3) {
        return (Build.VERSION.SDK_INT > 25 || str.length() <= 23) ? Log.isLoggable(str, i3) : Log.isLoggable(str.substring(0, 23), i3);
    }

    public static void f(String str, String str2) {
        if (e(str, 2)) {
            Log.v(str, str2);
        }
    }

    public static void g(String str, String str2) {
        Log.w(str, str2);
    }

    public static void h(String str, String str2, Throwable th) {
        Log.w(str, str2, th);
    }
}

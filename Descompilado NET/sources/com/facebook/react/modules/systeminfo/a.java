package com.facebook.react.modules.systeminfo;

import D2.h;
import D2.u;
import K2.o;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import d1.AbstractC0506n;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import r2.n;
import s2.AbstractC0696D;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f7047a = new a();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f7048b = a.class.getSimpleName();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static String f7049c;

    private a() {
    }

    public static final String a(int i3) {
        return "adb reverse tcp:" + i3 + " tcp:" + i3;
    }

    public static final String b(Context context) {
        h.f(context, "context");
        return a(f7047a.c(context));
    }

    private final int c(Context context) {
        return context.getResources().getInteger(AbstractC0506n.f9254a);
    }

    public static final String d() {
        if (f7047a.j()) {
            String str = Build.MODEL;
            h.c(str);
            return str;
        }
        return Build.MODEL + " - " + Build.VERSION.RELEASE + " - API " + Build.VERSION.SDK_INT;
    }

    public static final Map e(Context context) {
        String packageName;
        String string;
        if (context != null) {
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            int i3 = applicationInfo.labelRes;
            packageName = context.getPackageName();
            if (i3 == 0) {
                string = applicationInfo.nonLocalizedLabel.toString();
            } else {
                string = context.getString(i3);
                h.c(string);
            }
        } else {
            packageName = null;
            string = null;
        }
        return AbstractC0696D.h(n.a("appDisplayName", string), n.a("appIdentifier", packageName), n.a("platform", "android"), n.a("deviceName", Build.MODEL), n.a("reactNativeVersion", f7047a.g()));
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0073  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final synchronized java.lang.String f() {
        /*
            r7 = this;
            monitor-enter(r7)
            java.lang.String r0 = com.facebook.react.modules.systeminfo.a.f7049c     // Catch: java.lang.Throwable -> La
            if (r0 == 0) goto Ld
            D2.h.c(r0)     // Catch: java.lang.Throwable -> La
            monitor-exit(r7)
            return r0
        La:
            r0 = move-exception
            goto L82
        Ld:
            r0 = 0
            java.lang.Runtime r1 = java.lang.Runtime.getRuntime()     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L58
            java.lang.String r2 = "/system/bin/getprop"
            java.lang.String r3 = "metro.host"
            java.lang.String[] r2 = new java.lang.String[]{r2, r3}     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L58
            java.lang.Process r1 = r1.exec(r2)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L58
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4e
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4e
            java.io.InputStream r4 = r1.getInputStream()     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4e
            java.lang.String r5 = "UTF-8"
            java.nio.charset.Charset r5 = java.nio.charset.Charset.forName(r5)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4e
            r3.<init>(r4, r5)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4e
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L49 java.lang.Exception -> L4e
            java.lang.String r0 = ""
        L34:
            java.lang.String r3 = r2.readLine()     // Catch: java.lang.Throwable -> L45 java.lang.Exception -> L47
            if (r3 == 0) goto L3c
            r0 = r3
            goto L34
        L3c:
            com.facebook.react.modules.systeminfo.a.f7049c = r0     // Catch: java.lang.Throwable -> L45 java.lang.Exception -> L47
            r2.close()     // Catch: java.lang.Throwable -> La
        L41:
            r1.destroy()     // Catch: java.lang.Throwable -> La
            goto L6f
        L45:
            r0 = move-exception
            goto L77
        L47:
            r0 = move-exception
            goto L5c
        L49:
            r2 = move-exception
            r6 = r2
            r2 = r0
            r0 = r6
            goto L77
        L4e:
            r2 = move-exception
            r6 = r2
            r2 = r0
            r0 = r6
            goto L5c
        L53:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
            goto L77
        L58:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
        L5c:
            java.lang.String r3 = com.facebook.react.modules.systeminfo.a.f7048b     // Catch: java.lang.Throwable -> L45
            java.lang.String r4 = "Failed to query for metro.host prop:"
            Y.a.J(r3, r4, r0)     // Catch: java.lang.Throwable -> L45
            java.lang.String r0 = ""
            com.facebook.react.modules.systeminfo.a.f7049c = r0     // Catch: java.lang.Throwable -> L45
            if (r2 == 0) goto L6c
            r2.close()     // Catch: java.lang.Throwable -> La
        L6c:
            if (r1 == 0) goto L6f
            goto L41
        L6f:
            java.lang.String r0 = com.facebook.react.modules.systeminfo.a.f7049c     // Catch: java.lang.Throwable -> La
            if (r0 != 0) goto L75
            java.lang.String r0 = ""
        L75:
            monitor-exit(r7)
            return r0
        L77:
            if (r2 == 0) goto L7c
            r2.close()     // Catch: java.lang.Throwable -> La
        L7c:
            if (r1 == 0) goto L81
            r1.destroy()     // Catch: java.lang.Throwable -> La
        L81:
            throw r0     // Catch: java.lang.Throwable -> La
        L82:
            monitor-exit(r7)     // Catch: java.lang.Throwable -> La
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.modules.systeminfo.a.f():java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x002f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final java.lang.String g() {
        /*
            r6 = this;
            java.util.Map r0 = com.facebook.react.modules.systeminfo.b.f7050a
            java.lang.String r1 = "major"
            java.lang.Object r1 = r0.get(r1)
            java.lang.String r2 = "minor"
            java.lang.Object r2 = r0.get(r2)
            java.lang.String r3 = "patch"
            java.lang.Object r3 = r0.get(r3)
            java.lang.String r4 = "prerelease"
            java.lang.Object r0 = r0.get(r4)
            if (r0 == 0) goto L2f
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "-"
            r4.append(r5)
            r4.append(r0)
            java.lang.String r0 = r4.toString()
            if (r0 != 0) goto L31
        L2f:
            java.lang.String r0 = ""
        L31:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r1)
            java.lang.String r1 = "."
            r4.append(r1)
            r4.append(r2)
            r4.append(r1)
            r4.append(r3)
            r4.append(r0)
            java.lang.String r0 = r4.toString()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.modules.systeminfo.a.g():java.lang.String");
    }

    public static final String h(Context context) {
        h.f(context, "context");
        a aVar = f7047a;
        return aVar.i(aVar.c(context));
    }

    private final String i(int i3) {
        String strF = f().length() > 0 ? f() : j() ? "10.0.3.2" : k() ? "10.0.2.2" : "localhost";
        u uVar = u.f192a;
        String str = String.format(Locale.US, "%s:%d", Arrays.copyOf(new Object[]{strF, Integer.valueOf(i3)}, 2));
        h.e(str, "format(...)");
        return str;
    }

    private final boolean j() {
        String str = Build.FINGERPRINT;
        h.e(str, "FINGERPRINT");
        return o.E(str, "vbox", false, 2, null);
    }

    private final boolean k() {
        String str = Build.FINGERPRINT;
        h.e(str, "FINGERPRINT");
        if (!o.E(str, "generic", false, 2, null)) {
            h.e(str, "FINGERPRINT");
            if (!o.z(str, "google/sdk_gphone", false, 2, null)) {
                return false;
            }
        }
        return true;
    }
}

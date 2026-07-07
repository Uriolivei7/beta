package com.facebook.soloader;

import a2.AbstractC0211a;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import b2.C0317b;
import c2.C0334f;
import c2.InterfaceC0336h;
import c2.InterfaceC0337i;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* JADX INFO: loaded from: classes.dex */
public class SoLoader {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    static x f8204b;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private static int f8215m;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final ReentrantReadWriteLock f8205c = new ReentrantReadWriteLock();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    static Context f8206d = null;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static volatile E[] f8207e = null;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final AtomicInteger f8208f = new AtomicInteger(0);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static InterfaceC0337i f8209g = null;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final Set f8210h = Collections.newSetFromMap(new ConcurrentHashMap());

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final Map f8211i = new HashMap();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static final Set f8212j = Collections.newSetFromMap(new ConcurrentHashMap());

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private static final Map f8213k = new HashMap();

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static boolean f8214l = true;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private static int f8216n = 0;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private static l f8217o = null;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static final boolean f8203a = true;

    public static final class a extends UnsatisfiedLinkError {
        a(Throwable th, String str) {
            super("APK was built for a different platform. Supported ABIs: " + Arrays.toString(SysUtil.j()) + " error: " + str);
            initCause(th);
        }
    }

    private static int A() {
        ReentrantReadWriteLock reentrantReadWriteLock = f8205c;
        reentrantReadWriteLock.writeLock().lock();
        try {
            int i3 = f8215m;
            int i4 = (i3 & 2) != 0 ? 1 : 0;
            if ((i3 & 256) != 0) {
                i4 |= 4;
            }
            if ((i3 & 128) == 0) {
                i4 |= 8;
            }
            reentrantReadWriteLock.writeLock().unlock();
            return i4;
        } catch (Throwable th) {
            f8205c.writeLock().unlock();
            throw th;
        }
    }

    private static int B(int i3) {
        return (i3 & 2048) != 0 ? 1 : 0;
    }

    private static InterfaceC0336h C(String str, UnsatisfiedLinkError unsatisfiedLinkError, InterfaceC0336h interfaceC0336h) {
        p.g("SoLoader", "Running a recovery step for " + str + " due to " + unsatisfiedLinkError.toString());
        ReentrantReadWriteLock reentrantReadWriteLock = f8205c;
        reentrantReadWriteLock.writeLock().lock();
        try {
            if (interfaceC0336h == null) {
                try {
                    interfaceC0336h = j();
                    if (interfaceC0336h == null) {
                        p.g("SoLoader", "No recovery strategy");
                        throw unsatisfiedLinkError;
                    }
                } catch (v e4) {
                    p.c("SoLoader", "Base APK not found during recovery", e4);
                    throw e4;
                } catch (Exception e5) {
                    p.c("SoLoader", "Got an exception during recovery, will throw the initial error instead", e5);
                    throw unsatisfiedLinkError;
                }
            }
            if (D(unsatisfiedLinkError, interfaceC0336h)) {
                f8208f.getAndIncrement();
                reentrantReadWriteLock.writeLock().unlock();
                return interfaceC0336h;
            }
            reentrantReadWriteLock.writeLock().unlock();
            p.g("SoLoader", "Failed to recover");
            throw unsatisfiedLinkError;
        } catch (Throwable th) {
            f8205c.writeLock().unlock();
            throw th;
        }
    }

    private static boolean D(UnsatisfiedLinkError unsatisfiedLinkError, InterfaceC0336h interfaceC0336h) {
        C0317b.h(interfaceC0336h);
        try {
            boolean zA = interfaceC0336h.a(unsatisfiedLinkError, f8207e);
            C0317b.g(null);
            return zA;
        } finally {
        }
    }

    private static void a(ArrayList arrayList, int i3) {
        C0480a c0480a = new C0480a(f8206d, i3);
        p.a("SoLoader", "Adding application source: " + c0480a.toString());
        arrayList.add(0, c0480a);
    }

    private static void b(Context context, ArrayList arrayList, boolean z3) {
        if ((f8215m & 8) != 0) {
            return;
        }
        arrayList.add(0, new C0482c(context, "lib-main", !z3));
    }

    private static void c(Context context, ArrayList arrayList) {
        C0483d c0483d = new C0483d(context);
        p.a("SoLoader", "validating/adding directApk source: " + c0483d.toString());
        if (c0483d.o()) {
            arrayList.add(0, c0483d);
        }
    }

    private static void d(ArrayList arrayList) {
        String str = SysUtil.k() ? "/system/lib64:/vendor/lib64" : "/system/lib:/vendor/lib";
        String str2 = System.getenv("LD_LIBRARY_PATH");
        if (str2 != null && !str2.equals("")) {
            str = str2 + ":" + str;
        }
        for (String str3 : new HashSet(Arrays.asList(str.split(":")))) {
            p.a("SoLoader", "adding system library source: " + str3);
            arrayList.add(new C0485f(new File(str3), 2));
        }
    }

    private static void e(Context context, ArrayList arrayList) {
        F f3 = new F();
        p.a("SoLoader", "adding systemLoadWrapper source: " + f3);
        arrayList.add(0, f3);
    }

    private static void f() {
        if (!r()) {
            throw new IllegalStateException("SoLoader.init() not yet called");
        }
    }

    private static void g(String str, String str2, int i3, StrictMode.ThreadPolicy threadPolicy) {
        boolean z3;
        ReentrantReadWriteLock reentrantReadWriteLock = f8205c;
        reentrantReadWriteLock.readLock().lock();
        try {
            if (f8207e == null) {
                p.b("SoLoader", "Could not load: " + str + " because SoLoader is not initialized");
                throw new UnsatisfiedLinkError("SoLoader not initialized, couldn't find DSO to load: " + str);
            }
            reentrantReadWriteLock.readLock().unlock();
            if (threadPolicy == null) {
                threadPolicy = StrictMode.allowThreadDiskReads();
                z3 = true;
            } else {
                z3 = false;
            }
            if (f8203a) {
                if (str2 != null) {
                    Api18TraceUtils.a("SoLoader.loadLibrary[", str2, "]");
                }
                Api18TraceUtils.a("SoLoader.loadLibrary[", str, "]");
            }
            try {
                reentrantReadWriteLock.readLock().lock();
                try {
                    try {
                        for (E e4 : f8207e) {
                            if (x(e4, str, i3, threadPolicy)) {
                                if (z3) {
                                    return;
                                } else {
                                    return;
                                }
                            }
                        }
                        throw B.b(str, f8206d, f8207e);
                    } catch (IOException e5) {
                        C c4 = new C(str, e5.toString());
                        c4.initCause(e5);
                        throw c4;
                    }
                } finally {
                }
            } finally {
                if (f8203a) {
                    if (str2 != null) {
                        Api18TraceUtils.b();
                    }
                    Api18TraceUtils.b();
                }
                if (z3) {
                    StrictMode.setThreadPolicy(threadPolicy);
                }
            }
        } finally {
        }
    }

    private static int h(Context context) {
        int i3 = f8216n;
        if (i3 != 0) {
            return i3;
        }
        if (context == null) {
            p.a("SoLoader", "context is null, fallback to THIRD_PARTY_APP appType");
            return 1;
        }
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int i4 = applicationInfo.flags;
        int i5 = (i4 & 1) != 0 ? (i4 & 128) != 0 ? 3 : 2 : 1;
        p.a("SoLoader", "ApplicationInfo.flags is: " + applicationInfo.flags + " appType is: " + i5);
        return i5;
    }

    private static int i() {
        int i3 = f8216n;
        if (i3 == 1) {
            return 0;
        }
        if (i3 == 2 || i3 == 3) {
            return 1;
        }
        throw new RuntimeException("Unsupported app type, we should not reach here");
    }

    public static void init(Context context, int i3) {
        k(context, i3, null);
    }

    private static synchronized InterfaceC0336h j() {
        InterfaceC0337i interfaceC0337i;
        interfaceC0337i = f8209g;
        return interfaceC0337i == null ? null : interfaceC0337i.get();
    }

    public static void k(Context context, int i3, x xVar) {
        if (r()) {
            p.g("SoLoader", "SoLoader already initialized");
            return;
        }
        p.g("SoLoader", "Initializing SoLoader: " + i3);
        StrictMode.ThreadPolicy threadPolicyAllowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
        try {
            boolean zO = o(context);
            f8214l = zO;
            if (zO) {
                int iH = h(context);
                f8216n = iH;
                if ((i3 & 128) == 0 && SysUtil.l(context, iH)) {
                    i3 |= 8;
                }
                p(context, xVar, i3);
                q(context, i3);
                p.f("SoLoader", "Init SoLoader delegate");
                AbstractC0211a.b(new u());
            } else {
                n();
                p.f("SoLoader", "Init System Loader delegate");
                AbstractC0211a.b(new a2.c());
            }
            p.g("SoLoader", "SoLoader initialized: " + i3);
            StrictMode.setThreadPolicy(threadPolicyAllowThreadDiskWrites);
        } catch (Throwable th) {
            StrictMode.setThreadPolicy(threadPolicyAllowThreadDiskWrites);
            throw th;
        }
    }

    public static void l(Context context, l lVar) {
        synchronized (SoLoader.class) {
            f8217o = lVar;
        }
        init(context, 0);
    }

    public static void m(Context context, boolean z3) {
        try {
            k(context, z3 ? 1 : 0, null);
        } catch (IOException e4) {
            throw new RuntimeException(e4);
        }
    }

    private static void n() {
        if (f8207e != null) {
            return;
        }
        ReentrantReadWriteLock reentrantReadWriteLock = f8205c;
        reentrantReadWriteLock.writeLock().lock();
        try {
            if (f8207e != null) {
                reentrantReadWriteLock.writeLock().unlock();
            } else {
                f8207e = new E[0];
                reentrantReadWriteLock.writeLock().unlock();
            }
        } catch (Throwable th) {
            f8205c.writeLock().unlock();
            throw th;
        }
    }

    private static boolean o(Context context) {
        String packageName;
        if (f8217o != null) {
            return true;
        }
        Bundle bundle = null;
        try {
            packageName = context.getPackageName();
        } catch (Exception e4) {
            e = e4;
            packageName = null;
        }
        try {
            bundle = context.getPackageManager().getApplicationInfo(packageName, 128).metaData;
        } catch (Exception e5) {
            e = e5;
            p.h("SoLoader", "Unexpected issue with package manager (" + packageName + ")", e);
        }
        return bundle == null || bundle.getBoolean("com.facebook.soloader.enabled", true);
    }

    private static synchronized void p(Context context, x xVar, int i3) {
        if (context != null) {
            try {
                Context applicationContext = context.getApplicationContext();
                if (applicationContext == null) {
                    p.g("SoLoader", "context.getApplicationContext returned null, holding reference to original context.ApplicationSoSource fallbacks to: " + context.getApplicationInfo().nativeLibraryDir);
                } else {
                    context = applicationContext;
                }
                f8206d = context;
                f8209g = new C0334f(context, B(i3));
            } catch (Throwable th) {
                throw th;
            }
        }
        if (xVar != null || f8204b == null) {
            if (xVar != null) {
                f8204b = xVar;
            } else {
                f8204b = new o(new y());
            }
        }
    }

    private static void q(Context context, int i3) {
        if (f8207e != null) {
            return;
        }
        ReentrantReadWriteLock reentrantReadWriteLock = f8205c;
        reentrantReadWriteLock.writeLock().lock();
        try {
            if (f8207e != null) {
                reentrantReadWriteLock.writeLock().unlock();
                return;
            }
            f8215m = i3;
            ArrayList arrayList = new ArrayList();
            boolean z3 = true;
            boolean z4 = (i3 & 512) != 0;
            boolean z5 = (i3 & 1024) != 0;
            if (z4) {
                e(context, arrayList);
            } else if (z5) {
                d(arrayList);
                arrayList.add(0, new C0484e("base"));
            } else {
                d(arrayList);
                if (context != null) {
                    if ((i3 & 1) != 0) {
                        a(arrayList, i());
                        p.a("SoLoader", "Adding exo package source: lib-main");
                        arrayList.add(0, new k(context, "lib-main"));
                    } else {
                        if (SysUtil.l(context, f8216n)) {
                            c(context, arrayList);
                        }
                        a(arrayList, i());
                        if ((i3 & 4096) == 0) {
                            z3 = false;
                        }
                        b(context, arrayList, z3);
                    }
                }
            }
            E[] eArr = (E[]) arrayList.toArray(new E[arrayList.size()]);
            int iA = A();
            int length = eArr.length;
            while (true) {
                int i4 = length - 1;
                if (length <= 0) {
                    f8207e = eArr;
                    f8208f.getAndIncrement();
                    p.d("SoLoader", "init finish: " + f8207e.length + " SO sources prepared");
                    f8205c.writeLock().unlock();
                    return;
                }
                p.d("SoLoader", "Preparing SO source: " + eArr[i4]);
                boolean z6 = f8203a;
                if (z6) {
                    Api18TraceUtils.a("SoLoader", "_", eArr[i4].getClass().getSimpleName());
                }
                eArr[i4].e(iA);
                if (z6) {
                    Api18TraceUtils.b();
                }
                length = i4;
            }
        } catch (Throwable th) {
            f8205c.writeLock().unlock();
            throw th;
        }
    }

    public static boolean r() {
        if (f8207e != null) {
            return true;
        }
        ReentrantReadWriteLock reentrantReadWriteLock = f8205c;
        reentrantReadWriteLock.readLock().lock();
        try {
            boolean z3 = f8207e != null;
            reentrantReadWriteLock.readLock().unlock();
            return z3;
        } catch (Throwable th) {
            f8205c.readLock().unlock();
            throw th;
        }
    }

    static void s(String str, int i3, StrictMode.ThreadPolicy threadPolicy) {
        C0317b.d(str, i3);
        try {
            C0317b.c(null, w(str, null, null, i3 | 1, threadPolicy));
        } finally {
        }
    }

    public static boolean t(String str) {
        return f8214l ? u(str, 0) : AbstractC0211a.d(str);
    }

    public static boolean u(String str, int i3) {
        Boolean boolZ = z(str);
        if (boolZ != null) {
            return boolZ.booleanValue();
        }
        if (!f8214l) {
            return AbstractC0211a.d(str);
        }
        if (f8216n != 2) {
        }
        return y(str, i3);
    }

    private static boolean v(String str, String str2, String str3, int i3, StrictMode.ThreadPolicy threadPolicy) {
        InterfaceC0336h interfaceC0336hC = null;
        while (true) {
            try {
                return w(str, str2, str3, i3, threadPolicy);
            } catch (UnsatisfiedLinkError e4) {
                interfaceC0336hC = C(str, e4, interfaceC0336hC);
            }
        }
    }

    private static boolean w(String str, String str2, String str3, int i3, StrictMode.ThreadPolicy threadPolicy) {
        boolean z3;
        Object obj;
        Object obj2;
        if (!TextUtils.isEmpty(str2) && f8212j.contains(str2)) {
            return false;
        }
        Set set = f8210h;
        if (set.contains(str) && str3 == null) {
            return false;
        }
        synchronized (SoLoader.class) {
            try {
                if (!set.contains(str)) {
                    z3 = false;
                } else {
                    if (str3 == null) {
                        return false;
                    }
                    z3 = true;
                }
                Map map = f8211i;
                if (map.containsKey(str)) {
                    obj = map.get(str);
                } else {
                    Object obj3 = new Object();
                    map.put(str, obj3);
                    obj = obj3;
                }
                Map map2 = f8213k;
                if (map2.containsKey(str2)) {
                    obj2 = map2.get(str2);
                } else {
                    Object obj4 = new Object();
                    map2.put(str2, obj4);
                    obj2 = obj4;
                }
                ReentrantReadWriteLock reentrantReadWriteLock = f8205c;
                reentrantReadWriteLock.readLock().lock();
                try {
                    synchronized (obj) {
                        if (!z3) {
                            if (set.contains(str)) {
                                if (str3 == null) {
                                    reentrantReadWriteLock.readLock().unlock();
                                    return false;
                                }
                                z3 = true;
                            }
                            if (!z3) {
                                try {
                                    p.a("SoLoader", "About to load: " + str);
                                    g(str, str2, i3, threadPolicy);
                                    p.a("SoLoader", "Loaded: " + str);
                                    set.add(str);
                                } catch (UnsatisfiedLinkError e4) {
                                    String message = e4.getMessage();
                                    if (message == null || !message.contains("unexpected e_machine:")) {
                                        throw e4;
                                    }
                                    throw new a(e4, message.substring(message.lastIndexOf("unexpected e_machine:")));
                                }
                            }
                        }
                        synchronized (obj2) {
                            if ((i3 & 16) == 0 && str3 != null) {
                                try {
                                    if (TextUtils.isEmpty(str2) || !f8212j.contains(str2)) {
                                        boolean z4 = f8203a;
                                        if (z4 && f8217o == null) {
                                            Api18TraceUtils.a("MergedSoMapping.invokeJniOnload[", str2, "]");
                                        }
                                        try {
                                            p.a("SoLoader", "About to invoke JNI_OnLoad for merged library " + str2 + ", which was merged into " + str);
                                            l lVar = f8217o;
                                            if (lVar != null) {
                                                lVar.a(str2);
                                            } else {
                                                r.a(str2);
                                            }
                                            f8212j.add(str2);
                                            if (z4 && f8217o == null) {
                                                Api18TraceUtils.b();
                                            }
                                        } catch (UnsatisfiedLinkError e5) {
                                            throw new RuntimeException("Failed to call JNI_OnLoad from '" + str2 + "', which has been merged into '" + str + "'.  See comment for details.", e5);
                                        }
                                    }
                                } catch (Throwable th) {
                                    if (f8203a && f8217o == null) {
                                        Api18TraceUtils.b();
                                    }
                                    throw th;
                                } finally {
                                }
                            }
                        }
                        reentrantReadWriteLock.readLock().unlock();
                        return !z3;
                    }
                } catch (Throwable th2) {
                    f8205c.readLock().unlock();
                    throw th2;
                }
            } finally {
            }
        }
    }

    private static boolean x(E e4, String str, int i3, StrictMode.ThreadPolicy threadPolicy) {
        C0317b.l(e4);
        try {
            boolean z3 = e4.d(str, i3, threadPolicy) != 0;
            C0317b.k(null);
            return z3;
        } finally {
        }
    }

    private static boolean y(String str, int i3) {
        l lVar = f8217o;
        String strB = lVar != null ? lVar.b(str) : r.b(str);
        String str2 = strB != null ? strB : str;
        C0317b.f(str, strB, i3);
        try {
            boolean zV = v(System.mapLibraryName(str2), str, strB, i3, null);
            C0317b.e(null, zV);
            return zV;
        } finally {
        }
    }

    private static Boolean z(String str) {
        Boolean boolValueOf;
        if (f8207e != null) {
            return null;
        }
        ReentrantReadWriteLock reentrantReadWriteLock = f8205c;
        reentrantReadWriteLock.readLock().lock();
        try {
            if (f8207e == null) {
                if (!"http://www.android.com/".equals(System.getProperty("java.vendor.url"))) {
                    synchronized (SoLoader.class) {
                        try {
                            boolean zContains = f8210h.contains(str);
                            boolean z3 = !zContains;
                            if (!zContains) {
                                System.loadLibrary(str);
                            }
                            boolValueOf = Boolean.valueOf(z3);
                        } finally {
                        }
                    }
                    reentrantReadWriteLock.readLock().unlock();
                    return boolValueOf;
                }
                f();
            }
            reentrantReadWriteLock.readLock().unlock();
            return null;
        } catch (Throwable th) {
            f8205c.readLock().unlock();
            throw th;
        }
    }
}

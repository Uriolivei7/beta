package androidx.startup;

import H.b;
import H.c;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static volatile a f5456d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final Object f5457e = new Object();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final Context f5460c;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final Set f5459b = new HashSet();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final Map f5458a = new HashMap();

    a(Context context) {
        this.f5460c = context.getApplicationContext();
    }

    private Object d(Class cls, Set set) {
        Object objB;
        if (J.a.h()) {
            try {
                J.a.c(cls.getSimpleName());
            } catch (Throwable th) {
                J.a.f();
                throw th;
            }
        }
        if (set.contains(cls)) {
            throw new IllegalStateException(String.format("Cannot initialize %s. Cycle detected.", cls.getName()));
        }
        if (this.f5458a.containsKey(cls)) {
            objB = this.f5458a.get(cls);
        } else {
            set.add(cls);
            try {
                H.a aVar = (H.a) cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                List<Class> listA = aVar.a();
                if (!listA.isEmpty()) {
                    for (Class cls2 : listA) {
                        if (!this.f5458a.containsKey(cls2)) {
                            d(cls2, set);
                        }
                    }
                }
                objB = aVar.b(this.f5460c);
                set.remove(cls);
                this.f5458a.put(cls, objB);
            } catch (Throwable th2) {
                throw new c(th2);
            }
        }
        J.a.f();
        return objB;
    }

    public static a e(Context context) {
        if (f5456d == null) {
            synchronized (f5457e) {
                try {
                    if (f5456d == null) {
                        f5456d = new a(context);
                    }
                } finally {
                }
            }
        }
        return f5456d;
    }

    void a() {
        try {
            try {
                J.a.c("Startup");
                b(this.f5460c.getPackageManager().getProviderInfo(new ComponentName(this.f5460c.getPackageName(), InitializationProvider.class.getName()), 128).metaData);
            } catch (PackageManager.NameNotFoundException e4) {
                throw new c(e4);
            }
        } finally {
            J.a.f();
        }
    }

    void b(Bundle bundle) {
        String string = this.f5460c.getString(b.f258a);
        if (bundle != null) {
            try {
                HashSet hashSet = new HashSet();
                for (String str : bundle.keySet()) {
                    if (string.equals(bundle.getString(str, null))) {
                        Class<?> cls = Class.forName(str);
                        if (H.a.class.isAssignableFrom(cls)) {
                            this.f5459b.add(cls);
                        }
                    }
                }
                Iterator it = this.f5459b.iterator();
                while (it.hasNext()) {
                    d((Class) it.next(), hashSet);
                }
            } catch (ClassNotFoundException e4) {
                throw new c(e4);
            }
        }
    }

    Object c(Class cls) {
        Object objD;
        synchronized (f5457e) {
            try {
                objD = this.f5458a.get(cls);
                if (objD == null) {
                    objD = d(cls, new HashSet());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return objD;
    }

    public Object f(Class cls) {
        return c(cls);
    }

    public boolean g(Class cls) {
        return this.f5459b.contains(cls);
    }
}

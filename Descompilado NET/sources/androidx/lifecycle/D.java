package androidx.lifecycle;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public abstract class D {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Map f5261a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Set f5262b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile boolean f5263c;

    public D() {
        this.f5261a = new HashMap();
        this.f5262b = new LinkedHashSet();
        this.f5263c = false;
    }

    private static void b(Object obj) {
        if (obj instanceof Closeable) {
            try {
                ((Closeable) obj).close();
            } catch (IOException e4) {
                throw new RuntimeException(e4);
            }
        }
    }

    final void a() {
        this.f5263c = true;
        Map map = this.f5261a;
        if (map != null) {
            synchronized (map) {
                try {
                    Iterator it = this.f5261a.values().iterator();
                    while (it.hasNext()) {
                        b(it.next());
                    }
                } finally {
                }
            }
        }
        Set set = this.f5262b;
        if (set != null) {
            synchronized (set) {
                try {
                    Iterator it2 = this.f5262b.iterator();
                    while (it2.hasNext()) {
                        b((Closeable) it2.next());
                    }
                } finally {
                }
            }
        }
        d();
    }

    Object c(String str) {
        Object obj;
        Map map = this.f5261a;
        if (map == null) {
            return null;
        }
        synchronized (map) {
            obj = this.f5261a.get(str);
        }
        return obj;
    }

    protected void d() {
    }

    Object e(String str, Object obj) {
        Object obj2;
        synchronized (this.f5261a) {
            try {
                obj2 = this.f5261a.get(str);
                if (obj2 == null) {
                    this.f5261a.put(str, obj);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (obj2 != null) {
            obj = obj2;
        }
        if (this.f5263c) {
            b(obj);
        }
        return obj;
    }

    public D(Closeable... closeableArr) {
        this.f5261a = new HashMap();
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        this.f5262b = linkedHashSet;
        this.f5263c = false;
        linkedHashSet.addAll(Arrays.asList(closeableArr));
    }
}

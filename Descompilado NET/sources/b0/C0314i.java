package b0;

import X.k;
import java.util.IdentityHashMap;
import java.util.Map;

/* JADX INFO: renamed from: b0.i, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0314i {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final Map f5583d = new IdentityHashMap();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Object f5584a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f5585b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final InterfaceC0313h f5586c;

    /* JADX INFO: renamed from: b0.i$a */
    public static class a extends RuntimeException {
        public a() {
            super("Null shared reference");
        }
    }

    public C0314i(Object obj, InterfaceC0313h interfaceC0313h, boolean z3) {
        this.f5584a = k.g(obj);
        this.f5586c = interfaceC0313h;
        this.f5585b = 1;
        if (z3) {
            a(obj);
        }
    }

    private static void a(Object obj) {
        Map map = f5583d;
        synchronized (map) {
            try {
                Integer num = (Integer) map.get(obj);
                if (num == null) {
                    map.put(obj, 1);
                } else {
                    map.put(obj, Integer.valueOf(num.intValue() + 1));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private synchronized int c() {
        int i3;
        e();
        k.b(Boolean.valueOf(this.f5585b > 0));
        i3 = this.f5585b - 1;
        this.f5585b = i3;
        return i3;
    }

    private void e() {
        if (!h(this)) {
            throw new a();
        }
    }

    public static boolean h(C0314i c0314i) {
        return c0314i != null && c0314i.g();
    }

    private static void i(Object obj) {
        Map map = f5583d;
        synchronized (map) {
            try {
                Integer num = (Integer) map.get(obj);
                if (num == null) {
                    Y.a.N("SharedReference", "No entry in sLiveObjects for value of type %s", obj.getClass());
                } else if (num.intValue() == 1) {
                    map.remove(obj);
                } else {
                    map.put(obj, Integer.valueOf(num.intValue() - 1));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public synchronized void b() {
        e();
        this.f5585b++;
    }

    public void d() {
        Object obj;
        if (c() == 0) {
            synchronized (this) {
                obj = this.f5584a;
                this.f5584a = null;
            }
            if (obj != null) {
                InterfaceC0313h interfaceC0313h = this.f5586c;
                if (interfaceC0313h != null) {
                    interfaceC0313h.a(obj);
                }
                i(obj);
            }
        }
    }

    public synchronized Object f() {
        return this.f5584a;
    }

    public synchronized boolean g() {
        return this.f5585b > 0;
    }

    public C0314i(Object obj, InterfaceC0313h interfaceC0313h) {
        this(obj, interfaceC0313h, false);
    }
}

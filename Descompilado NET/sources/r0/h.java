package R0;

/* JADX INFO: loaded from: classes.dex */
public final class h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final h f1962a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final int f1963b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static int f1964c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static volatile g f1965d;

    static {
        h hVar = new h();
        f1962a = hVar;
        f1963b = hVar.b();
        f1964c = 384;
    }

    private h() {
    }

    public static final g a() {
        if (f1965d == null) {
            synchronized (h.class) {
                try {
                    if (f1965d == null) {
                        f1965d = new g(f1964c, f1963b);
                    }
                    r2.r rVar = r2.r.f10584a;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        g gVar = f1965d;
        D2.h.c(gVar);
        return gVar;
    }

    private final int b() {
        int iMin = (int) Math.min(Runtime.getRuntime().maxMemory(), 2147483647L);
        return ((long) iMin) > 16777216 ? (iMin / 4) * 3 : iMin / 2;
    }
}

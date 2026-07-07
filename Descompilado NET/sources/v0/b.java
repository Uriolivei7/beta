package V0;

import D2.h;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final b f2674a = new b();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f2675b = new C0037b();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static c f2676c;

    public interface a {
    }

    /* JADX INFO: renamed from: V0.b$b, reason: collision with other inner class name */
    private static final class C0037b implements a {
    }

    public interface c {
        void a(String str);

        void b();

        boolean isTracing();
    }

    private b() {
    }

    public static final void a(String str) {
        h.f(str, "name");
        f2674a.c().a(str);
    }

    public static final void b() {
        f2674a.c().b();
    }

    private final c c() {
        V0.a aVar;
        c cVar = f2676c;
        if (cVar != null) {
            return cVar;
        }
        synchronized (b.class) {
            aVar = new V0.a();
            f2676c = aVar;
        }
        return aVar;
    }

    public static final boolean d() {
        return f2674a.c().isTracing();
    }
}

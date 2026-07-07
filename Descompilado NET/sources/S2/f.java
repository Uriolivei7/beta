package S2;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final f f2322a = new f();

    private f() {
    }

    public static final boolean b(String str) {
        D2.h.f(str, "method");
        return (D2.h.b(str, "GET") || D2.h.b(str, "HEAD")) ? false : true;
    }

    public static final boolean e(String str) {
        D2.h.f(str, "method");
        return D2.h.b(str, "POST") || D2.h.b(str, "PUT") || D2.h.b(str, "PATCH") || D2.h.b(str, "PROPPATCH") || D2.h.b(str, "REPORT");
    }

    public final boolean a(String str) {
        D2.h.f(str, "method");
        return D2.h.b(str, "POST") || D2.h.b(str, "PATCH") || D2.h.b(str, "PUT") || D2.h.b(str, "DELETE") || D2.h.b(str, "MOVE");
    }

    public final boolean c(String str) {
        D2.h.f(str, "method");
        return !D2.h.b(str, "PROPFIND");
    }

    public final boolean d(String str) {
        D2.h.f(str, "method");
        return D2.h.b(str, "PROPFIND");
    }
}

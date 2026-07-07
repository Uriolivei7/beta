package e1;

/* JADX INFO: renamed from: e1.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0524a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0524a f9346a = new C0524a();

    private C0524a() {
    }

    public static final boolean a() {
        return false;
    }

    public static final Class b(String str) {
        D2.h.f(str, "className");
        if (a()) {
            return Class.forName(str);
        }
        return null;
    }
}

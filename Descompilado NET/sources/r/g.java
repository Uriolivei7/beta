package R;

import R.a;

/* JADX INFO: loaded from: classes.dex */
public class g implements a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static g f1908a;

    private g() {
    }

    public static synchronized g b() {
        try {
            if (f1908a == null) {
                f1908a = new g();
            }
        } catch (Throwable th) {
            throw th;
        }
        return f1908a;
    }

    @Override // R.a
    public void a(a.EnumC0028a enumC0028a, Class cls, String str, Throwable th) {
    }
}

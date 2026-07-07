package Y;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class b implements c {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final b f2789c = new b();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f2790a = "unknown";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f2791b = 5;

    private b() {
    }

    public static b l() {
        return f2789c;
    }

    private static String m(String str, Throwable th) {
        return str + '\n' + n(th);
    }

    private static String n(Throwable th) {
        return th == null ? "" : Log.getStackTraceString(th);
    }

    private String o(String str) {
        if (this.f2790a == null) {
            return str;
        }
        return this.f2790a + ":" + str;
    }

    private void p(int i3, String str, String str2) {
        Log.println(i3, o(str), str2);
    }

    private void q(int i3, String str, String str2, Throwable th) {
        Log.println(i3, o(str), m(str2, th));
    }

    @Override // Y.c
    public void a(String str, String str2, Throwable th) {
        q(3, str, str2, th);
    }

    @Override // Y.c
    public void b(String str, String str2, Throwable th) {
        q(6, str, str2, th);
    }

    @Override // Y.c
    public void c(String str, String str2) {
        p(5, str, str2);
    }

    @Override // Y.c
    public void d(String str, String str2, Throwable th) {
        q(5, str, str2, th);
    }

    @Override // Y.c
    public boolean e(int i3) {
        return this.f2791b <= i3;
    }

    @Override // Y.c
    public void f(String str, String str2) {
        p(4, str, str2);
    }

    @Override // Y.c
    public void g(String str, String str2) {
        p(6, str, str2);
    }

    @Override // Y.c
    public void h(String str, String str2) {
        p(2, str, str2);
    }

    @Override // Y.c
    public void i(String str, String str2) {
        p(6, str, str2);
    }

    @Override // Y.c
    public void j(String str, String str2, Throwable th) {
        q(6, str, str2, th);
    }

    @Override // Y.c
    public void k(String str, String str2) {
        p(3, str, str2);
    }
}

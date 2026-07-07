package H0;

import android.net.Uri;

/* JADX INFO: loaded from: classes.dex */
public class p implements k {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static p f310a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static boolean f311b = false;

    protected p() {
    }

    public static synchronized p f() {
        try {
            if (f310a == null) {
                f310a = new p();
            }
        } catch (Throwable th) {
            throw th;
        }
        return f310a;
    }

    @Override // H0.k
    public R.d a(U0.b bVar, Object obj) {
        R.d dVar;
        String name;
        U0.d dVarL = bVar.l();
        if (dVarL != null) {
            R.d dVarB = dVarL.b();
            name = dVarL.getClass().getName();
            dVar = dVarB;
        } else {
            dVar = null;
            name = null;
        }
        C0164b c0164b = new C0164b(e(bVar.v()).toString(), bVar.r(), bVar.t(), bVar.h(), dVar, name);
        if (f311b) {
            c0164b.d(null);
        } else {
            c0164b.d(obj);
        }
        return c0164b;
    }

    @Override // H0.k
    public R.d b(U0.b bVar, Object obj) {
        C0164b c0164b = new C0164b(e(bVar.v()).toString(), bVar.r(), bVar.t(), bVar.h(), null, null);
        if (f311b) {
            c0164b.d(null);
        } else {
            c0164b.d(obj);
        }
        return c0164b;
    }

    @Override // H0.k
    public R.d c(U0.b bVar, Object obj) {
        return d(bVar, bVar.v(), obj);
    }

    @Override // H0.k
    public R.d d(U0.b bVar, Uri uri, Object obj) {
        return new R.i(e(uri).toString());
    }

    protected Uri e(Uri uri) {
        return uri;
    }
}

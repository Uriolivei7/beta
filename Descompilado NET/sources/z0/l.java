package z0;

import android.net.Uri;

/* JADX INFO: loaded from: classes.dex */
public final class l {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final l f11150a = new l();

    private l() {
    }

    public static final Uri a(Object obj, Object obj2, Object[] objArr, X.e eVar) {
        D2.h.f(eVar, "requestToUri");
        Uri uri = obj != null ? (Uri) eVar.a(obj) : null;
        if (uri != null) {
            return uri;
        }
        if (objArr != null && objArr.length != 0) {
            Object obj3 = objArr[0];
            Uri uri2 = obj3 != null ? (Uri) eVar.a(obj3) : null;
            if (uri2 != null) {
                return uri2;
            }
        }
        if (obj2 != null) {
            return (Uri) eVar.a(obj2);
        }
        return null;
    }
}

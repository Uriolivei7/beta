package X0;

import D2.h;
import android.graphics.Bitmap;
import b0.AbstractC0306a;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final b f2746a = new b();

    private b() {
    }

    public static final boolean a(a aVar, AbstractC0306a abstractC0306a) {
        if (aVar == null || abstractC0306a == null) {
            return false;
        }
        Object objP = abstractC0306a.P();
        h.e(objP, "get(...)");
        Bitmap bitmap = (Bitmap) objP;
        if (aVar.a()) {
            bitmap.setHasAlpha(true);
        }
        aVar.b(bitmap);
        return true;
    }
}

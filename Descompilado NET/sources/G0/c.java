package G0;

import D2.h;
import J0.C0167a;
import R0.D;
import R0.i;
import S0.f;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final c f255a = new c();

    private c() {
    }

    public static final b a(D d4, f fVar, C0167a c0167a) {
        h.f(d4, "poolFactory");
        h.f(fVar, "platformDecoder");
        h.f(c0167a, "closeableReferenceFactory");
        i iVarB = d4.b();
        h.e(iVarB, "getBitmapPool(...)");
        return new a(iVarB, c0167a);
    }
}

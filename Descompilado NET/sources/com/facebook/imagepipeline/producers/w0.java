package com.facebook.imagepipeline.producers;

/* JADX INFO: loaded from: classes.dex */
public final class w0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final w0 f6270a = new w0();

    private w0() {
    }

    public static final int a(int i3) {
        return (int) (i3 * 1.3333334f);
    }

    public static final boolean b(int i3, int i4, I0.g gVar) {
        if (gVar == null) {
            if (a(i3) < 2048.0f || a(i4) < 2048) {
                return false;
            }
        } else if (a(i3) < gVar.f421a || a(i4) < gVar.f422b) {
            return false;
        }
        return true;
    }

    public static final boolean c(O0.j jVar, I0.g gVar) {
        if (jVar == null) {
            return false;
        }
        int iN = jVar.N();
        return (iN == 90 || iN == 270) ? b(jVar.d(), jVar.h(), gVar) : b(jVar.h(), jVar.d(), gVar);
    }
}

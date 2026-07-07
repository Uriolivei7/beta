package W0;

import O0.j;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f2680a = new a();

    private a() {
    }

    public static final float a(I0.h hVar, I0.g gVar, j jVar) {
        D2.h.f(hVar, "rotationOptions");
        D2.h.f(jVar, "encodedImage");
        if (!j.u0(jVar)) {
            throw new IllegalStateException("Check failed.");
        }
        if (gVar == null || gVar.f422b <= 0 || gVar.f421a <= 0 || jVar.h() == 0 || jVar.d() == 0) {
            return 1.0f;
        }
        int iD = f2680a.d(hVar, jVar);
        boolean z3 = iD == 90 || iD == 270;
        int iD2 = z3 ? jVar.d() : jVar.h();
        int iH = z3 ? jVar.h() : jVar.d();
        float f3 = gVar.f421a / iD2;
        float f4 = gVar.f422b / iH;
        float fB = H2.d.b(f3, f4);
        Y.a.D("DownsampleUtil", "Downsample - Specified size: %dx%d, image size: %dx%d ratio: %.1f x %.1f, ratio: %.3f", Integer.valueOf(gVar.f421a), Integer.valueOf(gVar.f422b), Integer.valueOf(iD2), Integer.valueOf(iH), Float.valueOf(f3), Float.valueOf(f4), Float.valueOf(fB));
        return fB;
    }

    public static final int b(I0.h hVar, I0.g gVar, j jVar, int i3) {
        D2.h.f(hVar, "rotationOptions");
        D2.h.f(jVar, "encodedImage");
        if (!j.u0(jVar)) {
            return 1;
        }
        float fA = a(hVar, gVar, jVar);
        int iF = jVar.D() == D0.b.f135b ? f(fA) : e(fA);
        int iMax = Math.max(jVar.d(), jVar.h());
        float f3 = gVar != null ? gVar.f423c : i3;
        while (iMax / iF > f3) {
            iF = jVar.D() == D0.b.f135b ? iF * 2 : iF + 1;
        }
        return iF;
    }

    public static final int c(j jVar, int i3, int i4) {
        D2.h.f(jVar, "encodedImage");
        int iA0 = jVar.a0();
        while ((((jVar.h() * jVar.d()) * i3) / iA0) / iA0 > i4) {
            iA0 *= 2;
        }
        return iA0;
    }

    private final int d(I0.h hVar, j jVar) {
        if (!hVar.j()) {
            return 0;
        }
        int iN = jVar.N();
        if (iN == 0 || iN == 90 || iN == 180 || iN == 270) {
            return iN;
        }
        throw new IllegalStateException("Check failed.");
    }

    public static final int e(float f3) {
        if (f3 > 0.6666667f) {
            return 1;
        }
        int i3 = 2;
        while (true) {
            double d4 = i3;
            if ((1.0d / d4) + ((1.0d / (Math.pow(d4, 2.0d) - d4)) * ((double) 0.33333334f)) <= f3) {
                return i3 - 1;
            }
            i3++;
        }
    }

    public static final int f(float f3) {
        if (f3 > 0.6666667f) {
            return 1;
        }
        int i3 = 2;
        while (true) {
            int i4 = i3 * 2;
            double d4 = 1.0d / ((double) i4);
            if (d4 + (((double) 0.33333334f) * d4) <= f3) {
                return i3;
            }
            i3 = i4;
        }
    }
}

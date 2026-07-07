package W0;

import O0.j;
import android.graphics.Matrix;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final e f2682a = new e();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final X.f f2683b;

    static {
        X.f fVarC = X.f.c(2, 7, 4, 5);
        D2.h.e(fVarC, "of(...)");
        f2683b = fVarC;
    }

    private e() {
    }

    public static final int a(int i3) {
        return Math.max(1, 8 / i3);
    }

    public static final float b(I0.g gVar, int i3, int i4) {
        if (gVar == null) {
            return 1.0f;
        }
        float f3 = i3;
        float f4 = i4;
        float fMax = Math.max(gVar.f421a / f3, gVar.f422b / f4);
        float f5 = f3 * fMax;
        float f6 = gVar.f423c;
        if (f5 > f6) {
            fMax = f6 / f3;
        }
        return f4 * fMax > f6 ? f6 / f4 : fMax;
    }

    private final int c(j jVar) {
        int iN = jVar.N();
        if (iN == 90 || iN == 180 || iN == 270) {
            return jVar.N();
        }
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final int d(I0.h hVar, j jVar) {
        D2.h.f(hVar, "rotationOptions");
        D2.h.f(jVar, "encodedImage");
        int iS0 = jVar.s0();
        X.f fVar = f2683b;
        int iIndexOf = fVar.indexOf(Integer.valueOf(iS0));
        if (iIndexOf < 0) {
            throw new IllegalArgumentException("Only accepts inverted exif orientations");
        }
        E e4 = fVar.get((iIndexOf + ((!hVar.j() ? hVar.h() : 0) / 90)) % fVar.size());
        D2.h.e(e4, "get(...)");
        return ((Number) e4).intValue();
    }

    public static final int e(I0.h hVar, j jVar) {
        D2.h.f(hVar, "rotationOptions");
        D2.h.f(jVar, "encodedImage");
        if (!hVar.i()) {
            return 0;
        }
        int iC = f2682a.c(jVar);
        return hVar.j() ? iC : (iC + hVar.h()) % 360;
    }

    public static final int f(I0.h hVar, I0.g gVar, j jVar, boolean z3) {
        D2.h.f(hVar, "rotationOptions");
        D2.h.f(jVar, "encodedImage");
        if (!z3 || gVar == null) {
            return 8;
        }
        int iE = e(hVar, jVar);
        int iD = f2683b.contains(Integer.valueOf(jVar.s0())) ? d(hVar, jVar) : 0;
        boolean z4 = iE == 90 || iE == 270 || iD == 5 || iD == 7;
        int iK = k(b(gVar, z4 ? jVar.d() : jVar.h(), z4 ? jVar.h() : jVar.d()), gVar.f424d);
        if (iK > 8) {
            return 8;
        }
        if (iK < 1) {
            return 1;
        }
        return iK;
    }

    public static final Matrix g(j jVar, I0.h hVar) {
        D2.h.f(jVar, "encodedImage");
        D2.h.f(hVar, "rotationOptions");
        if (f2683b.contains(Integer.valueOf(jVar.s0()))) {
            return f2682a.h(d(hVar, jVar));
        }
        int iE = e(hVar, jVar);
        if (iE == 0) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(iE);
        return matrix;
    }

    private final Matrix h(int i3) {
        Matrix matrix = new Matrix();
        if (i3 == 2) {
            matrix.setScale(-1.0f, 1.0f);
        } else if (i3 == 7) {
            matrix.setRotate(-90.0f);
            matrix.postScale(-1.0f, 1.0f);
        } else if (i3 == 4) {
            matrix.setRotate(180.0f);
            matrix.postScale(-1.0f, 1.0f);
        } else {
            if (i3 != 5) {
                return null;
            }
            matrix.setRotate(90.0f);
            matrix.postScale(-1.0f, 1.0f);
        }
        return matrix;
    }

    public static final boolean i(int i3) {
        switch (i3) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return true;
            default:
                return false;
        }
    }

    public static final boolean j(int i3) {
        return i3 >= 0 && i3 <= 270 && i3 % 90 == 0;
    }

    public static final int k(float f3, float f4) {
        return (int) (f4 + (f3 * 8));
    }
}

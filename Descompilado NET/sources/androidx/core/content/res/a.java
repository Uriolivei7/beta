package androidx.core.content.res;

/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final float f4419a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final float f4420b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final float f4421c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final float f4422d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final float f4423e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final float f4424f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final float f4425g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final float f4426h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final float f4427i;

    a(float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        this.f4419a = f3;
        this.f4420b = f4;
        this.f4421c = f5;
        this.f4422d = f6;
        this.f4423e = f7;
        this.f4424f = f8;
        this.f4425g = f9;
        this.f4426h = f10;
        this.f4427i = f11;
    }

    private static a b(float f3, float f4, float f5) {
        float f6 = 100.0f;
        float f7 = 1000.0f;
        float f8 = 0.0f;
        a aVar = null;
        float f9 = 1000.0f;
        while (Math.abs(f8 - f6) > 0.01f) {
            float f10 = ((f6 - f8) / 2.0f) + f8;
            int iP = e(f10, f4, f3).p();
            float fB = b.b(iP);
            float fAbs = Math.abs(f5 - fB);
            if (fAbs < 0.2f) {
                a aVarC = c(iP);
                float fA = aVarC.a(e(aVarC.k(), aVarC.i(), f3));
                if (fA <= 1.0f) {
                    aVar = aVarC;
                    f7 = fAbs;
                    f9 = fA;
                }
            }
            if (f7 == 0.0f && f9 == 0.0f) {
                break;
            }
            if (fB < f5) {
                f8 = f10;
            } else {
                f6 = f10;
            }
        }
        return aVar;
    }

    static a c(int i3) {
        float[] fArr = new float[7];
        float[] fArr2 = new float[3];
        d(i3, i.f4459k, fArr, fArr2);
        return new a(fArr2[0], fArr2[1], fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5], fArr[6]);
    }

    static void d(int i3, i iVar, float[] fArr, float[] fArr2) {
        b.f(i3, fArr2);
        float[][] fArr3 = b.f4428a;
        float f3 = fArr2[0];
        float[] fArr4 = fArr3[0];
        float f4 = fArr4[0] * f3;
        float f5 = fArr2[1];
        float f6 = f4 + (fArr4[1] * f5);
        float f7 = fArr2[2];
        float f8 = f6 + (fArr4[2] * f7);
        float[] fArr5 = fArr3[1];
        float f9 = (fArr5[0] * f3) + (fArr5[1] * f5) + (fArr5[2] * f7);
        float[] fArr6 = fArr3[2];
        float f10 = (f3 * fArr6[0]) + (f5 * fArr6[1]) + (f7 * fArr6[2]);
        float f11 = iVar.i()[0] * f8;
        float f12 = iVar.i()[1] * f9;
        float f13 = iVar.i()[2] * f10;
        float fPow = (float) Math.pow(((double) (iVar.c() * Math.abs(f11))) / 100.0d, 0.42d);
        float fPow2 = (float) Math.pow(((double) (iVar.c() * Math.abs(f12))) / 100.0d, 0.42d);
        float fPow3 = (float) Math.pow(((double) (iVar.c() * Math.abs(f13))) / 100.0d, 0.42d);
        float fSignum = ((Math.signum(f11) * 400.0f) * fPow) / (fPow + 27.13f);
        float fSignum2 = ((Math.signum(f12) * 400.0f) * fPow2) / (fPow2 + 27.13f);
        float fSignum3 = ((Math.signum(f13) * 400.0f) * fPow3) / (fPow3 + 27.13f);
        double d4 = fSignum3;
        float f14 = ((float) (((((double) fSignum) * 11.0d) + (((double) fSignum2) * (-12.0d))) + d4)) / 11.0f;
        float f15 = ((float) (((double) (fSignum + fSignum2)) - (d4 * 2.0d))) / 9.0f;
        float f16 = fSignum2 * 20.0f;
        float f17 = (((fSignum * 20.0f) + f16) + (21.0f * fSignum3)) / 20.0f;
        float f18 = (((fSignum * 40.0f) + f16) + fSignum3) / 20.0f;
        float fAtan2 = (((float) Math.atan2(f15, f14)) * 180.0f) / 3.1415927f;
        if (fAtan2 < 0.0f) {
            fAtan2 += 360.0f;
        } else if (fAtan2 >= 360.0f) {
            fAtan2 -= 360.0f;
        }
        float f19 = (3.1415927f * fAtan2) / 180.0f;
        float fPow4 = ((float) Math.pow((f18 * iVar.f()) / iVar.a(), iVar.b() * iVar.j())) * 100.0f;
        float fB = (4.0f / iVar.b()) * ((float) Math.sqrt(fPow4 / 100.0f)) * (iVar.a() + 4.0f) * iVar.d();
        float fSqrt = ((float) Math.sqrt(((double) fPow4) / 100.0d)) * ((float) Math.pow(1.64d - Math.pow(0.29d, iVar.e()), 0.73d)) * ((float) Math.pow((((((((float) (Math.cos(((((double) (((double) fAtan2) < 20.14d ? 360.0f + fAtan2 : fAtan2)) * 3.141592653589793d) / 180.0d) + 2.0d) + 3.8d)) * 0.25f) * 3846.1538f) * iVar.g()) * iVar.h()) * ((float) Math.sqrt((f14 * f14) + (f15 * f15)))) / (f17 + 0.305f), 0.9d));
        float fD = iVar.d() * fSqrt;
        float fSqrt2 = ((float) Math.sqrt((r7 * iVar.b()) / (iVar.a() + 4.0f))) * 50.0f;
        float f20 = (1.7f * fPow4) / ((0.007f * fPow4) + 1.0f);
        float fLog = ((float) Math.log((0.0228f * fD) + 1.0f)) * 43.85965f;
        double d5 = f19;
        float fCos = ((float) Math.cos(d5)) * fLog;
        float fSin = fLog * ((float) Math.sin(d5));
        fArr2[0] = fAtan2;
        fArr2[1] = fSqrt;
        if (fArr != null) {
            fArr[0] = fPow4;
            fArr[1] = fB;
            fArr[2] = fD;
            fArr[3] = fSqrt2;
            fArr[4] = f20;
            fArr[5] = fCos;
            fArr[6] = fSin;
        }
    }

    private static a e(float f3, float f4, float f5) {
        return f(f3, f4, f5, i.f4459k);
    }

    private static a f(float f3, float f4, float f5, i iVar) {
        float fB = (4.0f / iVar.b()) * ((float) Math.sqrt(((double) f3) / 100.0d)) * (iVar.a() + 4.0f) * iVar.d();
        float fD = f4 * iVar.d();
        float fSqrt = ((float) Math.sqrt(((f4 / ((float) Math.sqrt(r4))) * iVar.b()) / (iVar.a() + 4.0f))) * 50.0f;
        float f6 = (1.7f * f3) / ((0.007f * f3) + 1.0f);
        float fLog = ((float) Math.log((((double) fD) * 0.0228d) + 1.0d)) * 43.85965f;
        double d4 = (3.1415927f * f5) / 180.0f;
        return new a(f5, f4, f3, fB, fD, fSqrt, f6, fLog * ((float) Math.cos(d4)), fLog * ((float) Math.sin(d4)));
    }

    public static int m(float f3, float f4, float f5) {
        return n(f3, f4, f5, i.f4459k);
    }

    static int n(float f3, float f4, float f5, i iVar) {
        if (f4 < 1.0d || Math.round(f5) <= 0.0d || Math.round(f5) >= 100.0d) {
            return b.a(f5);
        }
        float fMin = f3 < 0.0f ? 0.0f : Math.min(360.0f, f3);
        a aVar = null;
        boolean z3 = true;
        float f6 = 0.0f;
        float f7 = f4;
        while (Math.abs(f6 - f4) >= 0.4f) {
            a aVarB = b(fMin, f7, f5);
            if (!z3) {
                if (aVarB == null) {
                    f4 = f7;
                } else {
                    f6 = f7;
                    aVar = aVarB;
                }
                f7 = ((f4 - f6) / 2.0f) + f6;
            } else {
                if (aVarB != null) {
                    return aVarB.o(iVar);
                }
                f7 = ((f4 - f6) / 2.0f) + f6;
                z3 = false;
            }
        }
        return aVar == null ? b.a(f5) : aVar.o(iVar);
    }

    float a(a aVar) {
        float fL = l() - aVar.l();
        float fG = g() - aVar.g();
        float fH = h() - aVar.h();
        return (float) (Math.pow(Math.sqrt((fL * fL) + (fG * fG) + (fH * fH)), 0.63d) * 1.41d);
    }

    float g() {
        return this.f4426h;
    }

    float h() {
        return this.f4427i;
    }

    float i() {
        return this.f4420b;
    }

    float j() {
        return this.f4419a;
    }

    float k() {
        return this.f4421c;
    }

    float l() {
        return this.f4425g;
    }

    int o(i iVar) {
        float fPow = (float) Math.pow(((double) ((((double) i()) == 0.0d || ((double) k()) == 0.0d) ? 0.0f : i() / ((float) Math.sqrt(((double) k()) / 100.0d)))) / Math.pow(1.64d - Math.pow(0.29d, iVar.e()), 0.73d), 1.1111111111111112d);
        double dJ = (j() * 3.1415927f) / 180.0f;
        float fCos = ((float) (Math.cos(2.0d + dJ) + 3.8d)) * 0.25f;
        float fA = iVar.a() * ((float) Math.pow(((double) k()) / 100.0d, (1.0d / ((double) iVar.b())) / ((double) iVar.j())));
        float fG = fCos * 3846.1538f * iVar.g() * iVar.h();
        float f3 = fA / iVar.f();
        float fSin = (float) Math.sin(dJ);
        float fCos2 = (float) Math.cos(dJ);
        float f4 = (((0.305f + f3) * 23.0f) * fPow) / (((fG * 23.0f) + ((11.0f * fPow) * fCos2)) + ((fPow * 108.0f) * fSin));
        float f5 = fCos2 * f4;
        float f6 = f4 * fSin;
        float f7 = f3 * 460.0f;
        float f8 = (((451.0f * f5) + f7) + (288.0f * f6)) / 1403.0f;
        float f9 = ((f7 - (891.0f * f5)) - (261.0f * f6)) / 1403.0f;
        float fSignum = Math.signum(f8) * (100.0f / iVar.c()) * ((float) Math.pow((float) Math.max(0.0d, (((double) Math.abs(f8)) * 27.13d) / (400.0d - ((double) Math.abs(f8)))), 2.380952380952381d));
        float fSignum2 = Math.signum(f9) * (100.0f / iVar.c()) * ((float) Math.pow((float) Math.max(0.0d, (((double) Math.abs(f9)) * 27.13d) / (400.0d - ((double) Math.abs(f9)))), 2.380952380952381d));
        float fSignum3 = Math.signum(((f7 - (f5 * 220.0f)) - (f6 * 6300.0f)) / 1403.0f) * (100.0f / iVar.c()) * ((float) Math.pow((float) Math.max(0.0d, (((double) Math.abs(r8)) * 27.13d) / (400.0d - ((double) Math.abs(r8)))), 2.380952380952381d));
        float f10 = fSignum / iVar.i()[0];
        float f11 = fSignum2 / iVar.i()[1];
        float f12 = fSignum3 / iVar.i()[2];
        float[][] fArr = b.f4429b;
        float[] fArr2 = fArr[0];
        float f13 = (fArr2[0] * f10) + (fArr2[1] * f11) + (fArr2[2] * f12);
        float[] fArr3 = fArr[1];
        float f14 = (fArr3[0] * f10) + (fArr3[1] * f11) + (fArr3[2] * f12);
        float[] fArr4 = fArr[2];
        return androidx.core.graphics.a.a(f13, f14, (f10 * fArr4[0]) + (f11 * fArr4[1]) + (f12 * fArr4[2]));
    }

    int p() {
        return o(i.f4459k);
    }
}

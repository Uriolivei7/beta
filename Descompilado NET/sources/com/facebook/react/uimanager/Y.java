package com.facebook.react.uimanager;

import a1.C0210a;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class Y {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Y f7428a = new Y();

    public static class a {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private static final C0111a f7429f = new C0111a(null);

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public double[] f7430a = new double[4];

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public double[] f7431b = new double[3];

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public double[] f7432c = new double[3];

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public double[] f7433d = new double[3];

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public double[] f7434e = new double[3];

        /* JADX INFO: renamed from: com.facebook.react.uimanager.Y$a$a, reason: collision with other inner class name */
        private static final class C0111a {
            public /* synthetic */ C0111a(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public final void b(double[] dArr) {
                int length = dArr.length;
                for (int i3 = 0; i3 < length; i3++) {
                    dArr[i3] = 0.0d;
                }
            }

            private C0111a() {
            }
        }

        public final void a() {
            C0111a c0111a = f7429f;
            c0111a.b(this.f7430a);
            c0111a.b(this.f7431b);
            c0111a.b(this.f7432c);
            c0111a.b(this.f7433d);
            c0111a.b(this.f7434e);
        }
    }

    private Y() {
    }

    public static final void a(double[] dArr, double d4) {
        D2.h.f(dArr, "m");
        dArr[11] = ((double) (-1)) / d4;
    }

    public static final void b(double[] dArr, double d4) {
        D2.h.f(dArr, "m");
        dArr[5] = Math.cos(d4);
        dArr[6] = Math.sin(d4);
        dArr[9] = -Math.sin(d4);
        dArr[10] = Math.cos(d4);
    }

    public static final void c(double[] dArr, double d4) {
        D2.h.f(dArr, "m");
        dArr[0] = Math.cos(d4);
        dArr[2] = -Math.sin(d4);
        dArr[8] = Math.sin(d4);
        dArr[10] = Math.cos(d4);
    }

    public static final void d(double[] dArr, double d4) {
        D2.h.f(dArr, "m");
        dArr[0] = Math.cos(d4);
        dArr[1] = Math.sin(d4);
        dArr[4] = -Math.sin(d4);
        dArr[5] = Math.cos(d4);
    }

    public static final void e(double[] dArr, double d4) {
        D2.h.f(dArr, "m");
        dArr[0] = d4;
    }

    public static final void f(double[] dArr, double d4) {
        D2.h.f(dArr, "m");
        dArr[5] = d4;
    }

    public static final void g(double[] dArr, double d4) {
        D2.h.f(dArr, "m");
        dArr[4] = Math.tan(d4);
    }

    public static final void h(double[] dArr, double d4) {
        D2.h.f(dArr, "m");
        dArr[1] = Math.tan(d4);
    }

    public static final void i(double[] dArr, double d4, double d5) {
        D2.h.f(dArr, "m");
        dArr[12] = d4;
        dArr[13] = d5;
    }

    public static final void j(double[] dArr, double d4, double d5, double d6) {
        D2.h.f(dArr, "m");
        dArr[12] = d4;
        dArr[13] = d5;
        dArr[14] = d6;
    }

    public static final void k(double[] dArr, a aVar) {
        D2.h.f(dArr, "transformMatrix");
        D2.h.f(aVar, "ctx");
        C0210a.a(dArr.length == 16);
        double[] dArr2 = aVar.f7430a;
        double[] dArr3 = aVar.f7431b;
        double[] dArr4 = aVar.f7432c;
        double[] dArr5 = aVar.f7433d;
        double[] dArr6 = aVar.f7434e;
        if (f7428a.o(dArr[15])) {
            return;
        }
        double[][] dArr7 = new double[4][];
        for (int i3 = 0; i3 < 4; i3++) {
            dArr7[i3] = new double[4];
        }
        double[] dArr8 = new double[16];
        for (int i4 = 0; i4 < 4; i4++) {
            for (int i5 = 0; i5 < 4; i5++) {
                int i6 = (i4 * 4) + i5;
                double d4 = dArr[i6] / dArr[15];
                dArr7[i4][i5] = d4;
                if (i5 == 3) {
                    d4 = 0.0d;
                }
                dArr8[i6] = d4;
            }
        }
        dArr8[15] = 1.0d;
        Y y3 = f7428a;
        if (y3.o(m(dArr8))) {
            return;
        }
        if (y3.o(dArr7[0][3]) && y3.o(dArr7[1][3]) && y3.o(dArr7[2][3])) {
            dArr2[2] = 0.0d;
            dArr2[1] = 0.0d;
            dArr2[0] = 0.0d;
            dArr2[3] = 1.0d;
        } else {
            q(new double[]{dArr7[0][3], dArr7[1][3], dArr7[2][3], dArr7[3][3]}, t(n(dArr8)), dArr2);
        }
        for (int i7 = 0; i7 < 3; i7++) {
            dArr5[i7] = dArr7[3][i7];
        }
        double[][] dArr9 = new double[3][];
        for (int i8 = 0; i8 < 3; i8++) {
            dArr9[i8] = new double[3];
        }
        for (int i9 = 0; i9 < 3; i9++) {
            double[] dArr10 = dArr9[i9];
            double[] dArr11 = dArr7[i9];
            dArr10[0] = dArr11[0];
            dArr10[1] = dArr11[1];
            dArr10[2] = dArr11[2];
        }
        double dX = x(dArr9[0]);
        dArr3[0] = dX;
        double[] dArrY = y(dArr9[0], dX);
        dArr9[0] = dArrY;
        double dW = w(dArrY, dArr9[1]);
        dArr4[0] = dW;
        double[] dArrU = u(dArr9[1], dArr9[0], 1.0d, -dW);
        dArr9[1] = dArrU;
        double dX2 = x(dArrU);
        dArr3[1] = dX2;
        dArr9[1] = y(dArr9[1], dX2);
        dArr4[0] = dArr4[0] / dArr3[1];
        double dW2 = w(dArr9[0], dArr9[2]);
        dArr4[1] = dW2;
        double[] dArrU2 = u(dArr9[2], dArr9[0], 1.0d, -dW2);
        dArr9[2] = dArrU2;
        double dW3 = w(dArr9[1], dArrU2);
        dArr4[2] = dW3;
        double[] dArrU3 = u(dArr9[2], dArr9[1], 1.0d, -dW3);
        dArr9[2] = dArrU3;
        double dX3 = x(dArrU3);
        dArr3[2] = dX3;
        double[] dArrY2 = y(dArr9[2], dX3);
        dArr9[2] = dArrY2;
        double d5 = dArr4[1];
        double d6 = dArr3[2];
        dArr4[1] = d5 / d6;
        dArr4[2] = dArr4[2] / d6;
        if (w(dArr9[0], v(dArr9[1], dArrY2)) < 0.0d) {
            for (int i10 = 0; i10 < 3; i10++) {
                dArr3[i10] = dArr3[i10] * (-1.0d);
                double[] dArr12 = dArr9[i10];
                dArr12[0] = dArr12[0] * (-1.0d);
                dArr12[1] = dArr12[1] * (-1.0d);
                dArr12[2] = dArr12[2] * (-1.0d);
            }
        }
        double[] dArr13 = dArr9[2];
        dArr6[0] = s((-Math.atan2(dArr13[1], dArr13[2])) * 57.29577951308232d);
        double[] dArr14 = dArr9[2];
        double d7 = -dArr14[0];
        double d8 = dArr14[1];
        double d9 = dArr14[2];
        dArr6[1] = s((-Math.atan2(d7, Math.sqrt((d8 * d8) + (d9 * d9)))) * 57.29577951308232d);
        dArr6[2] = s((-Math.atan2(dArr9[1][0], dArr9[0][0])) * 57.29577951308232d);
    }

    public static final double l(double d4) {
        return (d4 * 3.141592653589793d) / ((double) 180);
    }

    public static final double m(double[] dArr) {
        D2.h.f(dArr, "matrix");
        double d4 = dArr[0];
        double d5 = dArr[1];
        double d6 = dArr[2];
        double d7 = dArr[3];
        double d8 = dArr[4];
        double d9 = dArr[5];
        double d10 = dArr[6];
        double d11 = dArr[7];
        double d12 = dArr[8];
        double d13 = dArr[9];
        double d14 = dArr[10];
        double d15 = dArr[11];
        double d16 = dArr[12];
        double d17 = dArr[13];
        double d18 = dArr[14];
        double d19 = dArr[15];
        double d20 = d7 * d10;
        double d21 = d6 * d11;
        double d22 = d7 * d9;
        double d23 = d5 * d11;
        double d24 = d6 * d9;
        double d25 = d5 * d10;
        double d26 = d7 * d8;
        double d27 = d11 * d4;
        double d28 = d6 * d8;
        double d29 = d10 * d4;
        double d30 = d5 * d8;
        double d31 = d4 * d9;
        return ((((((((((((((((((((((((d20 * d13) * d16) - ((d21 * d13) * d16)) - ((d22 * d14) * d16)) + ((d23 * d14) * d16)) + ((d24 * d15) * d16)) - ((d25 * d15) * d16)) - ((d20 * d12) * d17)) + ((d21 * d12) * d17)) + ((d26 * d14) * d17)) - ((d27 * d14) * d17)) - ((d28 * d15) * d17)) + ((d29 * d15) * d17)) + ((d22 * d12) * d18)) - ((d23 * d12) * d18)) - ((d26 * d13) * d18)) + ((d27 * d13) * d18)) + ((d30 * d15) * d18)) - ((d15 * d31) * d18)) - ((d24 * d12) * d19)) + ((d25 * d12) * d19)) + ((d28 * d13) * d19)) - ((d29 * d13) * d19)) - ((d30 * d14) * d19)) + (d31 * d14 * d19);
    }

    public static final double[] n(double[] dArr) {
        D2.h.f(dArr, "matrix");
        double dM = m(dArr);
        if (f7428a.o(dM)) {
            return dArr;
        }
        double d4 = dArr[0];
        double d5 = dArr[1];
        double d6 = dArr[2];
        double d7 = dArr[3];
        double d8 = dArr[4];
        double d9 = dArr[5];
        double d10 = dArr[6];
        double d11 = dArr[7];
        double d12 = dArr[8];
        double d13 = dArr[9];
        double d14 = dArr[10];
        double d15 = dArr[11];
        double d16 = dArr[12];
        double d17 = dArr[13];
        double d18 = dArr[14];
        double d19 = dArr[15];
        double d20 = d10 * d15;
        double d21 = d11 * d14;
        double d22 = d11 * d13;
        double d23 = d9 * d15;
        double d24 = d10 * d13;
        double d25 = d9 * d14;
        double d26 = d7 * d14;
        double d27 = d6 * d15;
        double d28 = d7 * d13;
        double d29 = d5 * d15;
        double d30 = d6 * d13;
        double d31 = d5 * d14;
        double d32 = d6 * d11;
        double d33 = d7 * d10;
        double d34 = d7 * d9;
        double d35 = d5 * d11;
        double d36 = d6 * d9;
        double d37 = d5 * d10;
        double d38 = (d21 * d16) - (d20 * d16);
        double d39 = d11 * d12;
        double d40 = d8 * d15;
        double d41 = d10 * d12;
        double d42 = d8 * d14;
        double d43 = (d27 * d16) - (d26 * d16);
        double d44 = d7 * d12;
        double d45 = d4 * d15;
        double d46 = d6 * d12;
        double d47 = d4 * d14;
        double d48 = d7 * d8;
        double d49 = d11 * d4;
        double d50 = d6 * d8;
        double d51 = d10 * d4;
        double d52 = (((d23 * d16) - (d22 * d16)) + (d39 * d17)) - (d40 * d17);
        double d53 = d9 * d12;
        double d54 = d8 * d13;
        double d55 = (((d28 * d16) - (d29 * d16)) - (d44 * d17)) + (d45 * d17);
        double d56 = d5 * d12;
        double d57 = d4 * d13;
        double d58 = d5 * d8;
        double d59 = d4 * d9;
        return new double[]{((((((d20 * d17) - (d21 * d17)) + (d22 * d18)) - (d23 * d18)) - (d24 * d19)) + (d25 * d19)) / dM, ((((((d26 * d17) - (d27 * d17)) - (d28 * d18)) + (d29 * d18)) + (d30 * d19)) - (d31 * d19)) / dM, ((((((d32 * d17) - (d33 * d17)) + (d34 * d18)) - (d35 * d18)) - (d36 * d19)) + (d37 * d19)) / dM, ((((((d33 * d13) - (d32 * d13)) - (d34 * d14)) + (d35 * d14)) + (d36 * d15)) - (d37 * d15)) / dM, ((((d38 - (d39 * d18)) + (d40 * d18)) + (d41 * d19)) - (d42 * d19)) / dM, ((((d43 + (d44 * d18)) - (d45 * d18)) - (d46 * d19)) + (d47 * d19)) / dM, ((((((d33 * d16) - (d32 * d16)) - (d48 * d18)) + (d49 * d18)) + (d50 * d19)) - (d51 * d19)) / dM, ((((((d32 * d12) - (d33 * d12)) + (d48 * d14)) - (d49 * d14)) - (d50 * d15)) + (d51 * d15)) / dM, ((d52 - (d53 * d19)) + (d54 * d19)) / dM, ((d55 + (d56 * d19)) - (d57 * d19)) / dM, ((((((d35 * d16) - (d34 * d16)) + (d48 * d17)) - (d49 * d17)) - (d58 * d19)) + (d19 * d59)) / dM, ((((((d34 * d12) - (d35 * d12)) - (d48 * d13)) + (d49 * d13)) + (d58 * d15)) - (d15 * d59)) / dM, ((((((d24 * d16) - (d25 * d16)) - (d41 * d17)) + (d42 * d17)) + (d53 * d18)) - (d54 * d18)) / dM, ((((((d31 * d16) - (d30 * d16)) + (d46 * d17)) - (d47 * d17)) - (d56 * d18)) + (d57 * d18)) / dM, ((((((d36 * d16) - (d16 * d37)) - (d50 * d17)) + (d17 * d51)) + (d58 * d18)) - (d18 * d59)) / dM, ((((((d37 * d12) - (d36 * d12)) + (d50 * d13)) - (d51 * d13)) - (d58 * d14)) + (d59 * d14)) / dM};
    }

    private final boolean o(double d4) {
        return !Double.isNaN(d4) && Math.abs(d4) < 1.0E-5d;
    }

    public static final void p(double[] dArr, double[] dArr2, double[] dArr3) {
        D2.h.f(dArr, "out");
        D2.h.f(dArr2, "a");
        D2.h.f(dArr3, "b");
        double d4 = dArr2[0];
        double d5 = dArr2[1];
        double d6 = dArr2[2];
        double d7 = dArr2[3];
        double d8 = dArr2[4];
        double d9 = dArr2[5];
        double d10 = dArr2[6];
        double d11 = dArr2[7];
        double d12 = dArr2[8];
        double d13 = dArr2[9];
        double d14 = dArr2[10];
        double d15 = dArr2[11];
        double d16 = dArr2[12];
        double d17 = dArr2[13];
        double d18 = dArr2[14];
        double d19 = dArr2[15];
        double d20 = dArr3[0];
        double d21 = dArr3[1];
        double d22 = dArr3[2];
        double d23 = dArr3[3];
        dArr[0] = (d20 * d4) + (d21 * d8) + (d22 * d12) + (d23 * d16);
        dArr[1] = (d20 * d5) + (d21 * d9) + (d22 * d13) + (d23 * d17);
        dArr[2] = (d20 * d6) + (d21 * d10) + (d22 * d14) + (d23 * d18);
        dArr[3] = (d20 * d7) + (d21 * d11) + (d22 * d15) + (d23 * d19);
        double d24 = dArr3[4];
        double d25 = dArr3[5];
        double d26 = dArr3[6];
        double d27 = dArr3[7];
        dArr[4] = (d24 * d4) + (d25 * d8) + (d26 * d12) + (d27 * d16);
        dArr[5] = (d24 * d5) + (d25 * d9) + (d26 * d13) + (d27 * d17);
        dArr[6] = (d24 * d6) + (d25 * d10) + (d26 * d14) + (d27 * d18);
        dArr[7] = (d24 * d7) + (d25 * d11) + (d26 * d15) + (d27 * d19);
        double d28 = dArr3[8];
        double d29 = dArr3[9];
        double d30 = dArr3[10];
        double d31 = dArr3[11];
        dArr[8] = (d28 * d4) + (d29 * d8) + (d30 * d12) + (d31 * d16);
        dArr[9] = (d28 * d5) + (d29 * d9) + (d30 * d13) + (d31 * d17);
        dArr[10] = (d28 * d6) + (d29 * d10) + (d30 * d14) + (d31 * d18);
        dArr[11] = (d28 * d7) + (d29 * d11) + (d30 * d15) + (d31 * d19);
        double d32 = dArr3[12];
        double d33 = dArr3[13];
        double d34 = dArr3[14];
        double d35 = dArr3[15];
        dArr[12] = (d4 * d32) + (d8 * d33) + (d12 * d34) + (d16 * d35);
        dArr[13] = (d5 * d32) + (d9 * d33) + (d13 * d34) + (d17 * d35);
        dArr[14] = (d6 * d32) + (d10 * d33) + (d14 * d34) + (d18 * d35);
        dArr[15] = (d32 * d7) + (d33 * d11) + (d34 * d15) + (d35 * d19);
    }

    public static final void q(double[] dArr, double[] dArr2, double[] dArr3) {
        D2.h.f(dArr, "v");
        D2.h.f(dArr2, "m");
        D2.h.f(dArr3, "result");
        double d4 = dArr[0];
        double d5 = dArr[1];
        double d6 = dArr[2];
        double d7 = dArr[3];
        dArr3[0] = (dArr2[0] * d4) + (dArr2[4] * d5) + (dArr2[8] * d6) + (dArr2[12] * d7);
        dArr3[1] = (dArr2[1] * d4) + (dArr2[5] * d5) + (dArr2[9] * d6) + (dArr2[13] * d7);
        dArr3[2] = (dArr2[2] * d4) + (dArr2[6] * d5) + (dArr2[10] * d6) + (dArr2[14] * d7);
        dArr3[3] = (d4 * dArr2[3]) + (d5 * dArr2[7]) + (d6 * dArr2[11]) + (d7 * dArr2[15]);
    }

    public static final void r(double[] dArr) {
        D2.h.f(dArr, "matrix");
        dArr[14] = 0.0d;
        dArr[13] = 0.0d;
        dArr[12] = 0.0d;
        dArr[11] = 0.0d;
        dArr[9] = 0.0d;
        dArr[8] = 0.0d;
        dArr[7] = 0.0d;
        dArr[6] = 0.0d;
        dArr[4] = 0.0d;
        dArr[3] = 0.0d;
        dArr[2] = 0.0d;
        dArr[1] = 0.0d;
        dArr[15] = 1.0d;
        dArr[10] = 1.0d;
        dArr[5] = 1.0d;
        dArr[0] = 1.0d;
    }

    public static final double s(double d4) {
        return Math.round(d4 * 1000.0d) * 0.001d;
    }

    public static final double[] t(double[] dArr) {
        D2.h.f(dArr, "m");
        return new double[]{dArr[0], dArr[4], dArr[8], dArr[12], dArr[1], dArr[5], dArr[9], dArr[13], dArr[2], dArr[6], dArr[10], dArr[14], dArr[3], dArr[7], dArr[11], dArr[15]};
    }

    public static final double[] u(double[] dArr, double[] dArr2, double d4, double d5) {
        D2.h.f(dArr, "a");
        D2.h.f(dArr2, "b");
        return new double[]{(dArr[0] * d4) + (dArr2[0] * d5), (dArr[1] * d4) + (dArr2[1] * d5), (d4 * dArr[2]) + (d5 * dArr2[2])};
    }

    public static final double[] v(double[] dArr, double[] dArr2) {
        D2.h.f(dArr, "a");
        D2.h.f(dArr2, "b");
        double d4 = dArr[1];
        double d5 = dArr2[2];
        double d6 = dArr[2];
        double d7 = dArr2[1];
        double d8 = dArr2[0];
        double d9 = dArr[0];
        return new double[]{(d4 * d5) - (d6 * d7), (d6 * d8) - (d5 * d9), (d9 * d7) - (d4 * d8)};
    }

    public static final double w(double[] dArr, double[] dArr2) {
        D2.h.f(dArr, "a");
        D2.h.f(dArr2, "b");
        return (dArr[0] * dArr2[0]) + (dArr[1] * dArr2[1]) + (dArr[2] * dArr2[2]);
    }

    public static final double x(double[] dArr) {
        D2.h.f(dArr, "a");
        double d4 = dArr[0];
        double d5 = dArr[1];
        double d6 = (d4 * d4) + (d5 * d5);
        double d7 = dArr[2];
        return Math.sqrt(d6 + (d7 * d7));
    }

    public static final double[] y(double[] dArr, double d4) {
        D2.h.f(dArr, "vector");
        double d5 = 1;
        if (f7428a.o(d4)) {
            d4 = x(dArr);
        }
        double d6 = d5 / d4;
        return new double[]{dArr[0] * d6, dArr[1] * d6, dArr[2] * d6};
    }
}

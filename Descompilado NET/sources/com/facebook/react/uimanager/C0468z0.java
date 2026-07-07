package com.facebook.react.uimanager;

import java.util.Arrays;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: com.facebook.react.uimanager.z0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0468z0 {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final a f7641e = new a(null);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final int[] f7642f = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final float f7643a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final float[] f7644b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f7645c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f7646d;

    /* JADX INFO: renamed from: com.facebook.react.uimanager.z0$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final float[] b() {
            return new float[]{Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN};
        }

        private a() {
        }
    }

    public C0468z0(float f3, float[] fArr) {
        D2.h.f(fArr, "spacing");
        this.f7643a = f3;
        this.f7644b = fArr;
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x000c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final float a(int r5) {
        /*
            r4 = this;
            r0 = 4
            if (r5 == r0) goto Lc
            r0 = 5
            if (r5 == r0) goto Lc
            switch(r5) {
                case 9: goto Lc;
                case 10: goto Lc;
                case 11: goto Lc;
                default: goto L9;
            }
        L9:
            float r0 = r4.f7643a
            goto Le
        Lc:
            r0 = 2143289344(0x7fc00000, float:NaN)
        Le:
            int r1 = r4.f7645c
            if (r1 != 0) goto L13
            return r0
        L13:
            int[] r2 = com.facebook.react.uimanager.C0468z0.f7642f
            r3 = r2[r5]
            r3 = r3 & r1
            if (r3 == 0) goto L1f
            float[] r0 = r4.f7644b
            r5 = r0[r5]
            return r5
        L1f:
            boolean r3 = r4.f7646d
            if (r3 == 0) goto L42
            r3 = 1
            if (r5 == r3) goto L2b
            r3 = 3
            if (r5 == r3) goto L2b
            r5 = 6
            goto L2c
        L2b:
            r5 = 7
        L2c:
            r3 = r2[r5]
            r3 = r3 & r1
            if (r3 == 0) goto L36
            float[] r0 = r4.f7644b
            r5 = r0[r5]
            return r5
        L36:
            r5 = 8
            r2 = r2[r5]
            r1 = r1 & r2
            if (r1 == 0) goto L42
            float[] r0 = r4.f7644b
            r5 = r0[r5]
            return r5
        L42:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.uimanager.C0468z0.a(int):float");
    }

    public final float b(int i3) {
        return this.f7644b[i3];
    }

    public final boolean c(int i3, float f3) {
        if (L.a(this.f7644b[i3], f3)) {
            return false;
        }
        this.f7644b[i3] = f3;
        int i4 = com.facebook.yoga.g.a(f3) ? (~f7642f[i3]) & this.f7645c : f7642f[i3] | this.f7645c;
        this.f7645c = i4;
        int[] iArr = f7642f;
        this.f7646d = ((iArr[8] & i4) == 0 && (iArr[7] & i4) == 0 && (iArr[6] & i4) == 0 && (i4 & iArr[9]) == 0) ? false : true;
        return true;
    }

    public C0468z0() {
        this(0.0f, f7641e.b());
    }

    public C0468z0(float f3) {
        this(f3, f7641e.b());
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public C0468z0(C0468z0 c0468z0) {
        D2.h.f(c0468z0, "original");
        float f3 = c0468z0.f7643a;
        float[] fArr = c0468z0.f7644b;
        float[] fArrCopyOf = Arrays.copyOf(fArr, fArr.length);
        D2.h.e(fArrCopyOf, "copyOf(...)");
        this(f3, fArrCopyOf);
        this.f7645c = c0468z0.f7645c;
        this.f7646d = c0468z0.f7646d;
    }
}

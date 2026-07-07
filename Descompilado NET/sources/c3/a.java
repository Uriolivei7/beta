package c3;

import D2.h;
import b3.A;
import b3.AbstractC0322e;
import b3.i;
import b3.w;
import java.io.EOFException;

/* JADX INFO: loaded from: classes.dex */
public abstract class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final byte[] f5700a = AbstractC0322e.a("0123456789abcdef");

    public static final byte[] a() {
        return f5700a;
    }

    public static final boolean b(A a4, int i3, byte[] bArr, int i4, int i5) {
        h.f(a4, "segment");
        h.f(bArr, "bytes");
        int i6 = a4.f5592c;
        byte[] bArr2 = a4.f5590a;
        while (i4 < i5) {
            if (i3 == i6) {
                a4 = a4.f5595f;
                h.c(a4);
                byte[] bArr3 = a4.f5590a;
                bArr2 = bArr3;
                i3 = a4.f5591b;
                i6 = a4.f5592c;
            }
            if (bArr2[i3] != bArr[i4]) {
                return false;
            }
            i3++;
            i4++;
        }
        return true;
    }

    public static final String c(i iVar, long j3) throws EOFException {
        h.f(iVar, "$this$readUtf8Line");
        if (j3 > 0) {
            long j4 = j3 - 1;
            if (iVar.a0(j4) == ((byte) 13)) {
                String strD0 = iVar.D0(j4);
                iVar.s(2L);
                return strD0;
            }
        }
        String strD02 = iVar.D0(j3);
        iVar.s(1L);
        return strD02;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x005d, code lost:
    
        if (r19 == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x005f, code lost:
    
        return -2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0060, code lost:
    
        return r10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final int d(b3.i r17, b3.w r18, boolean r19) {
        /*
            r0 = r17
            java.lang.String r1 = "$this$selectPrefix"
            D2.h.f(r0, r1)
            java.lang.String r1 = "options"
            r2 = r18
            D2.h.f(r2, r1)
            b3.A r0 = r0.f5627b
            r1 = -2
            r3 = -1
            if (r0 == 0) goto La9
            byte[] r4 = r0.f5590a
            int r5 = r0.f5591b
            int r6 = r0.f5592c
            int[] r2 = r18.f()
            r7 = 0
            r9 = r0
            r10 = r3
            r8 = r7
        L22:
            int r11 = r8 + 1
            r12 = r2[r8]
            int r8 = r8 + 2
            r11 = r2[r11]
            if (r11 == r3) goto L2d
            r10 = r11
        L2d:
            if (r9 != 0) goto L30
            goto L5d
        L30:
            r11 = 0
            if (r12 >= 0) goto L7a
            int r12 = r12 * (-1)
            int r13 = r8 + r12
        L37:
            int r12 = r5 + 1
            r5 = r4[r5]
            r5 = r5 & 255(0xff, float:3.57E-43)
            int r14 = r8 + 1
            r8 = r2[r8]
            if (r5 == r8) goto L44
            return r10
        L44:
            if (r14 != r13) goto L48
            r5 = 1
            goto L49
        L48:
            r5 = r7
        L49:
            if (r12 != r6) goto L6a
            D2.h.c(r9)
            b3.A r4 = r9.f5595f
            D2.h.c(r4)
            int r6 = r4.f5591b
            byte[] r8 = r4.f5590a
            int r9 = r4.f5592c
            if (r4 != r0) goto L64
            if (r5 != 0) goto L61
        L5d:
            if (r19 == 0) goto L60
            return r1
        L60:
            return r10
        L61:
            r4 = r8
            r8 = r11
            goto L6d
        L64:
            r16 = r8
            r8 = r4
            r4 = r16
            goto L6d
        L6a:
            r8 = r9
            r9 = r6
            r6 = r12
        L6d:
            if (r5 == 0) goto L75
            r5 = r2[r14]
            r13 = r6
            r6 = r9
            r9 = r8
            goto L9f
        L75:
            r5 = r6
            r6 = r9
            r9 = r8
            r8 = r14
            goto L37
        L7a:
            int r13 = r5 + 1
            r5 = r4[r5]
            r5 = r5 & 255(0xff, float:3.57E-43)
            int r14 = r8 + r12
        L82:
            if (r8 != r14) goto L85
            return r10
        L85:
            r15 = r2[r8]
            if (r5 != r15) goto La6
            int r8 = r8 + r12
            r5 = r2[r8]
            if (r13 != r6) goto L9f
            b3.A r9 = r9.f5595f
            D2.h.c(r9)
            int r4 = r9.f5591b
            byte[] r6 = r9.f5590a
            int r8 = r9.f5592c
            r13 = r4
            r4 = r6
            r6 = r8
            if (r9 != r0) goto L9f
            r9 = r11
        L9f:
            if (r5 < 0) goto La2
            return r5
        La2:
            int r8 = -r5
            r5 = r13
            goto L22
        La6:
            int r8 = r8 + 1
            goto L82
        La9:
            if (r19 == 0) goto Lac
            goto Lad
        Lac:
            r1 = r3
        Lad:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: c3.a.d(b3.i, b3.w, boolean):int");
    }

    public static /* synthetic */ int e(i iVar, w wVar, boolean z3, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            z3 = false;
        }
        return d(iVar, wVar, z3);
    }
}

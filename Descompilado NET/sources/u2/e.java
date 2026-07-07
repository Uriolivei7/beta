package U2;

import K2.o;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final String[] f2488d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final e f2489e = new e();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final b3.l f2485a = b3.l.f5639f.e("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String[] f2486b = {"DATA", "HEADERS", "PRIORITY", "RST_STREAM", "SETTINGS", "PUSH_PROMISE", "PING", "GOAWAY", "WINDOW_UPDATE", "CONTINUATION"};

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final String[] f2487c = new String[64];

    static {
        String[] strArr = new String[256];
        for (int i3 = 0; i3 < 256; i3++) {
            String binaryString = Integer.toBinaryString(i3);
            D2.h.e(binaryString, "Integer.toBinaryString(it)");
            strArr[i3] = o.u(N2.c.q("%8s", binaryString), ' ', '0', false, 4, null);
        }
        f2488d = strArr;
        String[] strArr2 = f2487c;
        strArr2[0] = "";
        strArr2[1] = "END_STREAM";
        int[] iArr = {1};
        strArr2[8] = "PADDED";
        int i4 = iArr[0];
        strArr2[i4 | 8] = D2.h.l(strArr2[i4], "|PADDED");
        strArr2[4] = "END_HEADERS";
        strArr2[32] = "PRIORITY";
        strArr2[36] = "END_HEADERS|PRIORITY";
        int[] iArr2 = {4, 32, 36};
        for (int i5 = 0; i5 < 3; i5++) {
            int i6 = iArr2[i5];
            int i7 = iArr[0];
            String[] strArr3 = f2487c;
            int i8 = i7 | i6;
            strArr3[i8] = strArr3[i7] + "|" + strArr3[i6];
            strArr3[i8 | 8] = strArr3[i7] + "|" + strArr3[i6] + "|PADDED";
        }
        int length = f2487c.length;
        for (int i9 = 0; i9 < length; i9++) {
            String[] strArr4 = f2487c;
            if (strArr4[i9] == null) {
                strArr4[i9] = f2488d[i9];
            }
        }
    }

    private e() {
    }

    public final String a(int i3, int i4) {
        String str;
        if (i4 == 0) {
            return "";
        }
        if (i3 != 2 && i3 != 3) {
            if (i3 == 4 || i3 == 6) {
                return i4 == 1 ? "ACK" : f2488d[i4];
            }
            if (i3 != 7 && i3 != 8) {
                String[] strArr = f2487c;
                if (i4 < strArr.length) {
                    str = strArr[i4];
                    D2.h.c(str);
                } else {
                    str = f2488d[i4];
                }
                String str2 = str;
                return (i3 != 5 || (i4 & 4) == 0) ? (i3 != 0 || (i4 & 32) == 0) ? str2 : o.v(str2, "PRIORITY", "COMPRESSED", false, 4, null) : o.v(str2, "HEADERS", "PUSH_PROMISE", false, 4, null);
            }
        }
        return f2488d[i4];
    }

    public final String b(int i3) {
        String[] strArr = f2486b;
        return i3 < strArr.length ? strArr[i3] : N2.c.q("0x%02x", Integer.valueOf(i3));
    }

    public final String c(boolean z3, int i3, int i4, int i5, int i6) {
        return N2.c.q("%s 0x%08x %5d %-13s %s", z3 ? "<<" : ">>", Integer.valueOf(i3), Integer.valueOf(i4), b(i5), a(i5, i6));
    }
}

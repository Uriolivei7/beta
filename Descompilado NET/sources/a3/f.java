package a3;

import b3.i;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final f f2937a = new f();

    private f() {
    }

    public final String a(int i3) {
        if (i3 < 1000 || i3 >= 5000) {
            return "Code must be in range [1000,5000): " + i3;
        }
        if ((1004 > i3 || 1006 < i3) && (1015 > i3 || 2999 < i3)) {
            return null;
        }
        return "Code " + i3 + " is reserved and may not be used.";
    }

    public final void b(i.a aVar, byte[] bArr) {
        D2.h.f(aVar, "cursor");
        D2.h.f(bArr, "key");
        int length = bArr.length;
        int i3 = 0;
        do {
            byte[] bArr2 = aVar.f5633f;
            int i4 = aVar.f5634g;
            int i5 = aVar.f5635h;
            if (bArr2 != null) {
                while (i4 < i5) {
                    int i6 = i3 % length;
                    bArr2[i4] = (byte) (bArr2[i4] ^ bArr[i6]);
                    i4++;
                    i3 = i6 + 1;
                }
            }
        } while (aVar.a() != -1);
    }

    public final void c(int i3) {
        String strA = a(i3);
        if (strA == null) {
            return;
        }
        D2.h.c(strA);
        throw new IllegalArgumentException(strA.toString());
    }
}

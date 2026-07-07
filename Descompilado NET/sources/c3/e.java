package c3;

import D2.h;
import b3.C;

/* JADX INFO: loaded from: classes.dex */
public abstract class e {
    public static final int a(int[] iArr, int i3, int i4, int i5) {
        h.f(iArr, "$this$binarySearch");
        int i6 = i5 - 1;
        while (i4 <= i6) {
            int i7 = (i4 + i6) >>> 1;
            int i8 = iArr[i7];
            if (i8 < i3) {
                i4 = i7 + 1;
            } else {
                if (i8 <= i3) {
                    return i7;
                }
                i6 = i7 - 1;
            }
        }
        return (-i4) - 1;
    }

    public static final int b(C c4, int i3) {
        h.f(c4, "$this$segment");
        int iA = a(c4.B(), i3 + 1, 0, c4.C().length);
        return iA >= 0 ? iA : ~iA;
    }
}

package b3;

/* JADX INFO: loaded from: classes.dex */
public abstract class H {
    public static final long a(String str, int i3, int i4) {
        int i5;
        D2.h.f(str, "$this$utf8Size");
        if (!(i3 >= 0)) {
            throw new IllegalArgumentException(("beginIndex < 0: " + i3).toString());
        }
        if (!(i4 >= i3)) {
            throw new IllegalArgumentException(("endIndex < beginIndex: " + i4 + " < " + i3).toString());
        }
        if (!(i4 <= str.length())) {
            throw new IllegalArgumentException(("endIndex > string.length: " + i4 + " > " + str.length()).toString());
        }
        long j3 = 0;
        while (i3 < i4) {
            char cCharAt = str.charAt(i3);
            if (cCharAt < 128) {
                j3++;
            } else {
                if (cCharAt < 2048) {
                    i5 = 2;
                } else if (cCharAt < 55296 || cCharAt > 57343) {
                    i5 = 3;
                } else {
                    int i6 = i3 + 1;
                    char cCharAt2 = i6 < i4 ? str.charAt(i6) : (char) 0;
                    if (cCharAt > 56319 || cCharAt2 < 56320 || cCharAt2 > 57343) {
                        j3++;
                        i3 = i6;
                    } else {
                        j3 += (long) 4;
                        i3 += 2;
                    }
                }
                j3 += (long) i5;
            }
            i3++;
        }
        return j3;
    }

    public static /* synthetic */ long b(String str, int i3, int i4, int i5, Object obj) {
        if ((i5 & 1) != 0) {
            i3 = 0;
        }
        if ((i5 & 2) != 0) {
            i4 = str.length();
        }
        return a(str, i3, i4);
    }
}

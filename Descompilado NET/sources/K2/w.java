package K2;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes.dex */
public class w extends v {
    public static Integer j(String str) {
        D2.h.f(str, "<this>");
        return k(str, 10);
    }

    public static final Integer k(String str, int i3) {
        boolean z3;
        int i4;
        int i5;
        D2.h.f(str, "<this>");
        AbstractC0189a.a(i3);
        int length = str.length();
        if (length == 0) {
            return null;
        }
        int i6 = 0;
        char cCharAt = str.charAt(0);
        int i7 = -2147483647;
        if (D2.h.g(cCharAt, 48) < 0) {
            i4 = 1;
            if (length == 1) {
                return null;
            }
            if (cCharAt == '+') {
                z3 = false;
            } else {
                if (cCharAt != '-') {
                    return null;
                }
                i7 = Integer.MIN_VALUE;
                z3 = true;
            }
        } else {
            z3 = false;
            i4 = 0;
        }
        int i8 = -59652323;
        while (i4 < length) {
            int iB = b.b(str.charAt(i4), i3);
            if (iB < 0) {
                return null;
            }
            if ((i6 < i8 && (i8 != -59652323 || i6 < (i8 = i7 / i3))) || (i5 = i6 * i3) < i7 + iB) {
                return null;
            }
            i6 = i5 - iB;
            i4++;
        }
        return z3 ? Integer.valueOf(i6) : Integer.valueOf(-i6);
    }
}

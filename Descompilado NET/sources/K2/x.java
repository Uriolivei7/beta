package K2;

import java.util.Comparator;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes.dex */
public class x extends w {
    public static final boolean l(String str, String str2, boolean z3) {
        D2.h.f(str, "<this>");
        D2.h.f(str2, "suffix");
        return !z3 ? str.endsWith(str2) : p(str, str.length() - str2.length(), str2, 0, str2.length(), true);
    }

    public static /* synthetic */ boolean m(String str, String str2, boolean z3, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            z3 = false;
        }
        return l(str, str2, z3);
    }

    public static boolean n(String str, String str2, boolean z3) {
        return str == null ? str2 == null : !z3 ? str.equals(str2) : str.equalsIgnoreCase(str2);
    }

    public static Comparator o(D2.u uVar) {
        D2.h.f(uVar, "<this>");
        Comparator comparator = String.CASE_INSENSITIVE_ORDER;
        D2.h.e(comparator, "CASE_INSENSITIVE_ORDER");
        return comparator;
    }

    public static final boolean p(String str, int i3, String str2, int i4, int i5, boolean z3) {
        D2.h.f(str, "<this>");
        D2.h.f(str2, "other");
        return !z3 ? str.regionMatches(i3, str2, i4, i5) : str.regionMatches(z3, i3, str2, i4, i5);
    }

    public static /* synthetic */ boolean q(String str, int i3, String str2, int i4, int i5, boolean z3, int i6, Object obj) {
        if ((i6 & 16) != 0) {
            z3 = false;
        }
        return p(str, i3, str2, i4, i5, z3);
    }

    public static String r(CharSequence charSequence, int i3) {
        D2.h.f(charSequence, "<this>");
        if (i3 < 0) {
            throw new IllegalArgumentException(("Count 'n' must be non-negative, but was " + i3 + '.').toString());
        }
        if (i3 == 0) {
            return "";
        }
        int i4 = 1;
        if (i3 == 1) {
            return charSequence.toString();
        }
        int length = charSequence.length();
        if (length == 0) {
            return "";
        }
        if (length == 1) {
            char cCharAt = charSequence.charAt(0);
            char[] cArr = new char[i3];
            for (int i5 = 0; i5 < i3; i5++) {
                cArr[i5] = cCharAt;
            }
            return new String(cArr);
        }
        StringBuilder sb = new StringBuilder(charSequence.length() * i3);
        if (1 <= i3) {
            while (true) {
                sb.append(charSequence);
                if (i4 == i3) {
                    break;
                }
                i4++;
            }
        }
        String string = sb.toString();
        D2.h.c(string);
        return string;
    }

    public static final String s(String str, char c4, char c5, boolean z3) {
        D2.h.f(str, "<this>");
        if (!z3) {
            String strReplace = str.replace(c4, c5);
            D2.h.e(strReplace, "replace(...)");
            return strReplace;
        }
        StringBuilder sb = new StringBuilder(str.length());
        for (int i3 = 0; i3 < str.length(); i3++) {
            char cCharAt = str.charAt(i3);
            if (c.d(cCharAt, c4, z3)) {
                cCharAt = c5;
            }
            sb.append(cCharAt);
        }
        String string = sb.toString();
        D2.h.e(string, "toString(...)");
        return string;
    }

    public static final String t(String str, String str2, String str3, boolean z3) {
        D2.h.f(str, "<this>");
        D2.h.f(str2, "oldValue");
        D2.h.f(str3, "newValue");
        int i3 = 0;
        int iK = y.K(str, str2, 0, z3);
        if (iK < 0) {
            return str;
        }
        int length = str2.length();
        int iC = H2.d.c(length, 1);
        int length2 = (str.length() - length) + str3.length();
        if (length2 < 0) {
            throw new OutOfMemoryError();
        }
        StringBuilder sb = new StringBuilder(length2);
        do {
            sb.append((CharSequence) str, i3, iK);
            sb.append(str3);
            i3 = iK + length;
            if (iK >= str.length()) {
                break;
            }
            iK = y.K(str, str2, iK + iC, z3);
        } while (iK > 0);
        sb.append((CharSequence) str, i3, str.length());
        String string = sb.toString();
        D2.h.e(string, "toString(...)");
        return string;
    }

    public static /* synthetic */ String u(String str, char c4, char c5, boolean z3, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            z3 = false;
        }
        return s(str, c4, c5, z3);
    }

    public static /* synthetic */ String v(String str, String str2, String str3, boolean z3, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            z3 = false;
        }
        return t(str, str2, str3, z3);
    }

    public static boolean w(String str, String str2, int i3, boolean z3) {
        D2.h.f(str, "<this>");
        D2.h.f(str2, "prefix");
        return !z3 ? str.startsWith(str2, i3) : p(str, i3, str2, 0, str2.length(), z3);
    }

    public static boolean x(String str, String str2, boolean z3) {
        D2.h.f(str, "<this>");
        D2.h.f(str2, "prefix");
        return !z3 ? str.startsWith(str2) : p(str, 0, str2, 0, str2.length(), z3);
    }

    public static /* synthetic */ boolean y(String str, String str2, int i3, boolean z3, int i4, Object obj) {
        if ((i4 & 4) != 0) {
            z3 = false;
        }
        return o.w(str, str2, i3, z3);
    }

    public static /* synthetic */ boolean z(String str, String str2, boolean z3, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            z3 = false;
        }
        return o.x(str, str2, z3);
    }
}

package K2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import r2.C0686i;
import s2.AbstractC0711h;
import s2.AbstractC0717n;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes.dex */
public class y extends x {

    static final class a extends D2.i implements C2.p {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ char[] f858c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ boolean f859d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(char[] cArr, boolean z3) {
            super(2);
            this.f858c = cArr;
            this.f859d = z3;
        }

        @Override // C2.p
        public /* bridge */ /* synthetic */ Object b(Object obj, Object obj2) {
            return e((CharSequence) obj, ((Number) obj2).intValue());
        }

        public final C0686i e(CharSequence charSequence, int i3) {
            D2.h.f(charSequence, "$this$$receiver");
            int iP = y.P(charSequence, this.f858c, i3, this.f859d);
            if (iP < 0) {
                return null;
            }
            return r2.n.a(Integer.valueOf(iP), 1);
        }
    }

    static final class b extends D2.i implements C2.p {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ List f860c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ boolean f861d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(List list, boolean z3) {
            super(2);
            this.f860c = list;
            this.f861d = z3;
        }

        @Override // C2.p
        public /* bridge */ /* synthetic */ Object b(Object obj, Object obj2) {
            return e((CharSequence) obj, ((Number) obj2).intValue());
        }

        public final C0686i e(CharSequence charSequence, int i3) {
            D2.h.f(charSequence, "$this$$receiver");
            C0686i c0686iH = y.H(charSequence, this.f860c, i3, this.f861d, false);
            if (c0686iH != null) {
                return r2.n.a(c0686iH.c(), Integer.valueOf(((String) c0686iH.d()).length()));
            }
            return null;
        }
    }

    static final class c extends D2.i implements C2.l {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ CharSequence f862c;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(CharSequence charSequence) {
            super(1);
            this.f862c = charSequence;
        }

        @Override // C2.l
        /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
        public final String d(H2.c cVar) {
            D2.h.f(cVar, "it");
            return y.p0(this.f862c, cVar);
        }
    }

    public static final boolean B(CharSequence charSequence, char c4, boolean z3) {
        D2.h.f(charSequence, "<this>");
        return o.N(charSequence, c4, 0, z3, 2, null) >= 0;
    }

    public static final boolean C(CharSequence charSequence, CharSequence charSequence2, boolean z3) {
        D2.h.f(charSequence, "<this>");
        D2.h.f(charSequence2, "other");
        if (charSequence2 instanceof String) {
            if (o.O(charSequence, (String) charSequence2, 0, z3, 2, null) < 0) {
                return false;
            }
        } else if (M(charSequence, charSequence2, 0, charSequence.length(), z3, false, 16, null) < 0) {
            return false;
        }
        return true;
    }

    public static /* synthetic */ boolean D(CharSequence charSequence, char c4, boolean z3, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            z3 = false;
        }
        return B(charSequence, c4, z3);
    }

    public static /* synthetic */ boolean E(CharSequence charSequence, CharSequence charSequence2, boolean z3, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            z3 = false;
        }
        return C(charSequence, charSequence2, z3);
    }

    public static final boolean F(CharSequence charSequence, CharSequence charSequence2, boolean z3) {
        D2.h.f(charSequence, "<this>");
        D2.h.f(charSequence2, "suffix");
        return (!z3 && (charSequence instanceof String) && (charSequence2 instanceof String)) ? o.m((String) charSequence, (String) charSequence2, false, 2, null) : c0(charSequence, charSequence.length() - charSequence2.length(), charSequence2, 0, charSequence2.length(), z3);
    }

    public static /* synthetic */ boolean G(CharSequence charSequence, CharSequence charSequence2, boolean z3, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            z3 = false;
        }
        return F(charSequence, charSequence2, z3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final C0686i H(CharSequence charSequence, Collection collection, int i3, boolean z3, boolean z4) {
        Object next;
        Object next2;
        if (!z3 && collection.size() == 1) {
            String str = (String) AbstractC0717n.Z(collection);
            int iO = !z4 ? o.O(charSequence, str, i3, false, 4, null) : U(charSequence, str, i3, false, 4, null);
            if (iO < 0) {
                return null;
            }
            return r2.n.a(Integer.valueOf(iO), str);
        }
        H2.a cVar = !z4 ? new H2.c(H2.d.c(i3, 0), charSequence.length()) : H2.d.g(H2.d.e(i3, I(charSequence)), 0);
        if (charSequence instanceof String) {
            int iA = cVar.a();
            int iB = cVar.b();
            int iC = cVar.c();
            if ((iC > 0 && iA <= iB) || (iC < 0 && iB <= iA)) {
                while (true) {
                    Iterator it = collection.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            next2 = null;
                            break;
                        }
                        next2 = it.next();
                        String str2 = (String) next2;
                        if (x.p(str2, 0, (String) charSequence, iA, str2.length(), z3)) {
                            break;
                        }
                    }
                    String str3 = (String) next2;
                    if (str3 == null) {
                        if (iA == iB) {
                            break;
                        }
                        iA += iC;
                    } else {
                        return r2.n.a(Integer.valueOf(iA), str3);
                    }
                }
            }
        } else {
            int iA2 = cVar.a();
            int iB2 = cVar.b();
            int iC2 = cVar.c();
            if ((iC2 > 0 && iA2 <= iB2) || (iC2 < 0 && iB2 <= iA2)) {
                while (true) {
                    Iterator it2 = collection.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            next = null;
                            break;
                        }
                        next = it2.next();
                        String str4 = (String) next;
                        if (c0(str4, 0, charSequence, iA2, str4.length(), z3)) {
                            break;
                        }
                    }
                    String str5 = (String) next;
                    if (str5 == null) {
                        if (iA2 == iB2) {
                            break;
                        }
                        iA2 += iC2;
                    } else {
                        return r2.n.a(Integer.valueOf(iA2), str5);
                    }
                }
            }
        }
        return null;
    }

    public static final int I(CharSequence charSequence) {
        D2.h.f(charSequence, "<this>");
        return charSequence.length() - 1;
    }

    public static final int J(CharSequence charSequence, char c4, int i3, boolean z3) {
        D2.h.f(charSequence, "<this>");
        return (z3 || !(charSequence instanceof String)) ? P(charSequence, new char[]{c4}, i3, z3) : ((String) charSequence).indexOf(c4, i3);
    }

    public static final int K(CharSequence charSequence, String str, int i3, boolean z3) {
        D2.h.f(charSequence, "<this>");
        D2.h.f(str, "string");
        return (z3 || !(charSequence instanceof String)) ? M(charSequence, str, i3, charSequence.length(), z3, false, 16, null) : ((String) charSequence).indexOf(str, i3);
    }

    private static final int L(CharSequence charSequence, CharSequence charSequence2, int i3, int i4, boolean z3, boolean z4) {
        H2.a cVar = !z4 ? new H2.c(H2.d.c(i3, 0), H2.d.e(i4, charSequence.length())) : H2.d.g(H2.d.e(i3, I(charSequence)), H2.d.c(i4, 0));
        if ((charSequence instanceof String) && (charSequence2 instanceof String)) {
            int iA = cVar.a();
            int iB = cVar.b();
            int iC = cVar.c();
            if ((iC <= 0 || iA > iB) && (iC >= 0 || iB > iA)) {
                return -1;
            }
            while (!x.p((String) charSequence2, 0, (String) charSequence, iA, charSequence2.length(), z3)) {
                if (iA == iB) {
                    return -1;
                }
                iA += iC;
            }
            return iA;
        }
        int iA2 = cVar.a();
        int iB2 = cVar.b();
        int iC2 = cVar.c();
        if ((iC2 <= 0 || iA2 > iB2) && (iC2 >= 0 || iB2 > iA2)) {
            return -1;
        }
        while (!c0(charSequence2, 0, charSequence, iA2, charSequence2.length(), z3)) {
            if (iA2 == iB2) {
                return -1;
            }
            iA2 += iC2;
        }
        return iA2;
    }

    static /* synthetic */ int M(CharSequence charSequence, CharSequence charSequence2, int i3, int i4, boolean z3, boolean z4, int i5, Object obj) {
        if ((i5 & 16) != 0) {
            z4 = false;
        }
        return L(charSequence, charSequence2, i3, i4, z3, z4);
    }

    public static /* synthetic */ int N(CharSequence charSequence, char c4, int i3, boolean z3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i3 = 0;
        }
        if ((i4 & 4) != 0) {
            z3 = false;
        }
        return J(charSequence, c4, i3, z3);
    }

    public static /* synthetic */ int O(CharSequence charSequence, String str, int i3, boolean z3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i3 = 0;
        }
        if ((i4 & 4) != 0) {
            z3 = false;
        }
        return K(charSequence, str, i3, z3);
    }

    public static final int P(CharSequence charSequence, char[] cArr, int i3, boolean z3) {
        D2.h.f(charSequence, "<this>");
        D2.h.f(cArr, "chars");
        if (!z3 && cArr.length == 1 && (charSequence instanceof String)) {
            return ((String) charSequence).indexOf(AbstractC0711h.z(cArr), i3);
        }
        int iC = H2.d.c(i3, 0);
        int I3 = I(charSequence);
        if (iC > I3) {
            return -1;
        }
        while (true) {
            char cCharAt = charSequence.charAt(iC);
            for (char c4 : cArr) {
                if (K2.c.d(c4, cCharAt, z3)) {
                    return iC;
                }
            }
            if (iC == I3) {
                return -1;
            }
            iC++;
        }
    }

    public static boolean Q(CharSequence charSequence) {
        D2.h.f(charSequence, "<this>");
        for (int i3 = 0; i3 < charSequence.length(); i3++) {
            if (!K2.b.c(charSequence.charAt(i3))) {
                return false;
            }
        }
        return true;
    }

    public static final int R(CharSequence charSequence, char c4, int i3, boolean z3) {
        D2.h.f(charSequence, "<this>");
        return (z3 || !(charSequence instanceof String)) ? V(charSequence, new char[]{c4}, i3, z3) : ((String) charSequence).lastIndexOf(c4, i3);
    }

    public static final int S(CharSequence charSequence, String str, int i3, boolean z3) {
        D2.h.f(charSequence, "<this>");
        D2.h.f(str, "string");
        return (z3 || !(charSequence instanceof String)) ? L(charSequence, str, i3, 0, z3, true) : ((String) charSequence).lastIndexOf(str, i3);
    }

    public static /* synthetic */ int T(CharSequence charSequence, char c4, int i3, boolean z3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i3 = I(charSequence);
        }
        if ((i4 & 4) != 0) {
            z3 = false;
        }
        return R(charSequence, c4, i3, z3);
    }

    public static /* synthetic */ int U(CharSequence charSequence, String str, int i3, boolean z3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i3 = I(charSequence);
        }
        if ((i4 & 4) != 0) {
            z3 = false;
        }
        return S(charSequence, str, i3, z3);
    }

    public static final int V(CharSequence charSequence, char[] cArr, int i3, boolean z3) {
        D2.h.f(charSequence, "<this>");
        D2.h.f(cArr, "chars");
        if (!z3 && cArr.length == 1 && (charSequence instanceof String)) {
            return ((String) charSequence).lastIndexOf(AbstractC0711h.z(cArr), i3);
        }
        for (int iE = H2.d.e(i3, I(charSequence)); -1 < iE; iE--) {
            char cCharAt = charSequence.charAt(iE);
            for (char c4 : cArr) {
                if (K2.c.d(c4, cCharAt, z3)) {
                    return iE;
                }
            }
        }
        return -1;
    }

    public static final J2.c W(CharSequence charSequence) {
        D2.h.f(charSequence, "<this>");
        return m0(charSequence, new String[]{"\r\n", "\n", "\r"}, false, 0, 6, null);
    }

    public static final List X(CharSequence charSequence) {
        D2.h.f(charSequence, "<this>");
        return J2.d.g(W(charSequence));
    }

    private static final J2.c Y(CharSequence charSequence, char[] cArr, int i3, boolean z3, int i4) {
        h0(i4);
        return new e(charSequence, i3, i4, new a(cArr, z3));
    }

    private static final J2.c Z(CharSequence charSequence, String[] strArr, int i3, boolean z3, int i4) {
        h0(i4);
        return new e(charSequence, i3, i4, new b(AbstractC0711h.d(strArr), z3));
    }

    static /* synthetic */ J2.c a0(CharSequence charSequence, char[] cArr, int i3, boolean z3, int i4, int i5, Object obj) {
        if ((i5 & 2) != 0) {
            i3 = 0;
        }
        if ((i5 & 4) != 0) {
            z3 = false;
        }
        if ((i5 & 8) != 0) {
            i4 = 0;
        }
        return Y(charSequence, cArr, i3, z3, i4);
    }

    static /* synthetic */ J2.c b0(CharSequence charSequence, String[] strArr, int i3, boolean z3, int i4, int i5, Object obj) {
        if ((i5 & 2) != 0) {
            i3 = 0;
        }
        if ((i5 & 4) != 0) {
            z3 = false;
        }
        if ((i5 & 8) != 0) {
            i4 = 0;
        }
        return Z(charSequence, strArr, i3, z3, i4);
    }

    public static final boolean c0(CharSequence charSequence, int i3, CharSequence charSequence2, int i4, int i5, boolean z3) {
        D2.h.f(charSequence, "<this>");
        D2.h.f(charSequence2, "other");
        if (i4 < 0 || i3 < 0 || i3 > charSequence.length() - i5 || i4 > charSequence2.length() - i5) {
            return false;
        }
        for (int i6 = 0; i6 < i5; i6++) {
            if (!K2.c.d(charSequence.charAt(i3 + i6), charSequence2.charAt(i4 + i6), z3)) {
                return false;
            }
        }
        return true;
    }

    public static String d0(String str, CharSequence charSequence) {
        D2.h.f(str, "<this>");
        D2.h.f(charSequence, "prefix");
        if (!o0(str, charSequence, false, 2, null)) {
            return str;
        }
        String strSubstring = str.substring(charSequence.length());
        D2.h.e(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static String e0(String str, CharSequence charSequence) {
        D2.h.f(str, "<this>");
        D2.h.f(charSequence, "suffix");
        if (!G(str, charSequence, false, 2, null)) {
            return str;
        }
        String strSubstring = str.substring(0, str.length() - charSequence.length());
        D2.h.e(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static String f0(String str, CharSequence charSequence) {
        D2.h.f(str, "<this>");
        D2.h.f(charSequence, "delimiter");
        return g0(str, charSequence, charSequence);
    }

    public static final String g0(String str, CharSequence charSequence, CharSequence charSequence2) {
        D2.h.f(str, "<this>");
        D2.h.f(charSequence, "prefix");
        D2.h.f(charSequence2, "suffix");
        if (str.length() < charSequence.length() + charSequence2.length() || !o0(str, charSequence, false, 2, null) || !G(str, charSequence2, false, 2, null)) {
            return str;
        }
        String strSubstring = str.substring(charSequence.length(), str.length() - charSequence2.length());
        D2.h.e(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static final void h0(int i3) {
        if (i3 >= 0) {
            return;
        }
        throw new IllegalArgumentException(("Limit must be non-negative, but was " + i3).toString());
    }

    public static final List i0(CharSequence charSequence, char[] cArr, boolean z3, int i3) {
        D2.h.f(charSequence, "<this>");
        D2.h.f(cArr, "delimiters");
        if (cArr.length == 1) {
            return j0(charSequence, String.valueOf(cArr[0]), z3, i3);
        }
        Iterable iterableA = J2.d.a(a0(charSequence, cArr, 0, z3, i3, 2, null));
        ArrayList arrayList = new ArrayList(AbstractC0717n.q(iterableA, 10));
        Iterator it = iterableA.iterator();
        while (it.hasNext()) {
            arrayList.add(p0(charSequence, (H2.c) it.next()));
        }
        return arrayList;
    }

    private static final List j0(CharSequence charSequence, String str, boolean z3, int i3) {
        h0(i3);
        int length = 0;
        int iK = K(charSequence, str, 0, z3);
        if (iK == -1 || i3 == 1) {
            return AbstractC0717n.b(charSequence.toString());
        }
        boolean z4 = i3 > 0;
        ArrayList arrayList = new ArrayList(z4 ? H2.d.e(i3, 10) : 10);
        do {
            arrayList.add(charSequence.subSequence(length, iK).toString());
            length = str.length() + iK;
            if (z4 && arrayList.size() == i3 - 1) {
                break;
            }
            iK = K(charSequence, str, length, z3);
        } while (iK != -1);
        arrayList.add(charSequence.subSequence(length, charSequence.length()).toString());
        return arrayList;
    }

    public static /* synthetic */ List k0(CharSequence charSequence, char[] cArr, boolean z3, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            z3 = false;
        }
        if ((i4 & 4) != 0) {
            i3 = 0;
        }
        return i0(charSequence, cArr, z3, i3);
    }

    public static final J2.c l0(CharSequence charSequence, String[] strArr, boolean z3, int i3) {
        D2.h.f(charSequence, "<this>");
        D2.h.f(strArr, "delimiters");
        return J2.d.f(b0(charSequence, strArr, 0, z3, i3, 2, null), new c(charSequence));
    }

    public static /* synthetic */ J2.c m0(CharSequence charSequence, String[] strArr, boolean z3, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            z3 = false;
        }
        if ((i4 & 4) != 0) {
            i3 = 0;
        }
        return l0(charSequence, strArr, z3, i3);
    }

    public static final boolean n0(CharSequence charSequence, CharSequence charSequence2, boolean z3) {
        D2.h.f(charSequence, "<this>");
        D2.h.f(charSequence2, "prefix");
        return (!z3 && (charSequence instanceof String) && (charSequence2 instanceof String)) ? o.z((String) charSequence, (String) charSequence2, false, 2, null) : c0(charSequence, 0, charSequence2, 0, charSequence2.length(), z3);
    }

    public static /* synthetic */ boolean o0(CharSequence charSequence, CharSequence charSequence2, boolean z3, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            z3 = false;
        }
        return n0(charSequence, charSequence2, z3);
    }

    public static final String p0(CharSequence charSequence, H2.c cVar) {
        D2.h.f(charSequence, "<this>");
        D2.h.f(cVar, "range");
        return charSequence.subSequence(cVar.i().intValue(), cVar.h().intValue() + 1).toString();
    }

    public static final String q0(String str, char c4, String str2) {
        D2.h.f(str, "<this>");
        D2.h.f(str2, "missingDelimiterValue");
        int iN = o.N(str, c4, 0, false, 6, null);
        if (iN == -1) {
            return str2;
        }
        String strSubstring = str.substring(iN + 1, str.length());
        D2.h.e(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static final String r0(String str, String str2, String str3) {
        D2.h.f(str, "<this>");
        D2.h.f(str2, "delimiter");
        D2.h.f(str3, "missingDelimiterValue");
        int iO = o.O(str, str2, 0, false, 6, null);
        if (iO == -1) {
            return str3;
        }
        String strSubstring = str.substring(iO + str2.length(), str.length());
        D2.h.e(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static /* synthetic */ String s0(String str, char c4, String str2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            str2 = str;
        }
        return q0(str, c4, str2);
    }

    public static /* synthetic */ String t0(String str, String str2, String str3, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            str3 = str;
        }
        return r0(str, str2, str3);
    }

    public static final String u0(String str, char c4, String str2) {
        D2.h.f(str, "<this>");
        D2.h.f(str2, "missingDelimiterValue");
        int iT = o.T(str, c4, 0, false, 6, null);
        if (iT == -1) {
            return str2;
        }
        String strSubstring = str.substring(iT + 1, str.length());
        D2.h.e(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static /* synthetic */ String v0(String str, char c4, String str2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            str2 = str;
        }
        return u0(str, c4, str2);
    }

    public static CharSequence w0(CharSequence charSequence) {
        D2.h.f(charSequence, "<this>");
        int length = charSequence.length() - 1;
        int i3 = 0;
        boolean z3 = false;
        while (i3 <= length) {
            boolean zC = K2.b.c(charSequence.charAt(!z3 ? i3 : length));
            if (z3) {
                if (!zC) {
                    break;
                }
                length--;
            } else if (zC) {
                i3++;
            } else {
                z3 = true;
            }
        }
        return charSequence.subSequence(i3, length + 1);
    }
}

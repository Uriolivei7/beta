package M2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.publicsuffix.PublicSuffixDatabase;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class m {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f1184a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f1185b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final long f1186c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final String f1187d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final String f1188e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final boolean f1189f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final boolean f1190g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final boolean f1191h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final boolean f1192i;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public static final b f1183n = new b(null);

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static final Pattern f1179j = Pattern.compile("(\\d{2,4})[^\\d]*");

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private static final Pattern f1180k = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static final Pattern f1181l = Pattern.compile("(\\d{1,2})[^\\d]*");

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private static final Pattern f1182m = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private String f1193a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String f1194b;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private String f1196d;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private boolean f1198f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private boolean f1199g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private boolean f1200h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private boolean f1201i;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private long f1195c = 253402300799999L;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private String f1197e = "/";

        private final a c(String str, boolean z3) {
            String strE = N2.a.e(str);
            if (strE != null) {
                this.f1196d = strE;
                this.f1201i = z3;
                return this;
            }
            throw new IllegalArgumentException("unexpected domain: " + str);
        }

        public final m a() {
            String str = this.f1193a;
            if (str == null) {
                throw new NullPointerException("builder.name == null");
            }
            String str2 = this.f1194b;
            if (str2 == null) {
                throw new NullPointerException("builder.value == null");
            }
            long j3 = this.f1195c;
            String str3 = this.f1196d;
            if (str3 != null) {
                return new m(str, str2, j3, str3, this.f1197e, this.f1198f, this.f1199g, this.f1200h, this.f1201i, null);
            }
            throw new NullPointerException("builder.domain == null");
        }

        public final a b(String str) {
            D2.h.f(str, "domain");
            return c(str, false);
        }

        public final a d(String str) {
            D2.h.f(str, "name");
            if (!D2.h.b(K2.o.w0(str).toString(), str)) {
                throw new IllegalArgumentException("name is not trimmed");
            }
            this.f1193a = str;
            return this;
        }

        public final a e(String str) {
            D2.h.f(str, "value");
            if (!D2.h.b(K2.o.w0(str).toString(), str)) {
                throw new IllegalArgumentException("value is not trimmed");
            }
            this.f1194b = str;
            return this;
        }
    }

    public static final class b {
        private b() {
        }

        private final int a(String str, int i3, int i4, boolean z3) {
            while (i3 < i4) {
                char cCharAt = str.charAt(i3);
                if (((cCharAt < ' ' && cCharAt != '\t') || cCharAt >= 127 || ('0' <= cCharAt && '9' >= cCharAt) || (('a' <= cCharAt && 'z' >= cCharAt) || (('A' <= cCharAt && 'Z' >= cCharAt) || cCharAt == ':'))) == (!z3)) {
                    return i3;
                }
                i3++;
            }
            return i4;
        }

        private final boolean b(String str, String str2) {
            if (D2.h.b(str, str2)) {
                return true;
            }
            return K2.o.m(str, str2, false, 2, null) && str.charAt((str.length() - str2.length()) - 1) == '.' && !N2.c.f(str);
        }

        private final String f(String str) {
            if (K2.o.m(str, ".", false, 2, null)) {
                throw new IllegalArgumentException("Failed requirement.");
            }
            String strE = N2.a.e(K2.o.d0(str, "."));
            if (strE != null) {
                return strE;
            }
            throw new IllegalArgumentException();
        }

        private final long g(String str, int i3, int i4) {
            int iA = a(str, i3, i4, false);
            Matcher matcher = m.f1182m.matcher(str);
            int i5 = -1;
            int i6 = -1;
            int i7 = -1;
            int iO = -1;
            int i8 = -1;
            int i9 = -1;
            while (iA < i4) {
                int iA2 = a(str, iA + 1, i4, true);
                matcher.region(iA, iA2);
                if (i6 == -1 && matcher.usePattern(m.f1182m).matches()) {
                    String strGroup = matcher.group(1);
                    D2.h.e(strGroup, "matcher.group(1)");
                    i6 = Integer.parseInt(strGroup);
                    String strGroup2 = matcher.group(2);
                    D2.h.e(strGroup2, "matcher.group(2)");
                    i8 = Integer.parseInt(strGroup2);
                    String strGroup3 = matcher.group(3);
                    D2.h.e(strGroup3, "matcher.group(3)");
                    i9 = Integer.parseInt(strGroup3);
                } else if (i7 == -1 && matcher.usePattern(m.f1181l).matches()) {
                    String strGroup4 = matcher.group(1);
                    D2.h.e(strGroup4, "matcher.group(1)");
                    i7 = Integer.parseInt(strGroup4);
                } else if (iO == -1 && matcher.usePattern(m.f1180k).matches()) {
                    String strGroup5 = matcher.group(1);
                    D2.h.e(strGroup5, "matcher.group(1)");
                    Locale locale = Locale.US;
                    D2.h.e(locale, "Locale.US");
                    if (strGroup5 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String lowerCase = strGroup5.toLowerCase(locale);
                    D2.h.e(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
                    String strPattern = m.f1180k.pattern();
                    D2.h.e(strPattern, "MONTH_PATTERN.pattern()");
                    iO = K2.o.O(strPattern, lowerCase, 0, false, 6, null) / 4;
                } else if (i5 == -1 && matcher.usePattern(m.f1179j).matches()) {
                    String strGroup6 = matcher.group(1);
                    D2.h.e(strGroup6, "matcher.group(1)");
                    i5 = Integer.parseInt(strGroup6);
                }
                iA = a(str, iA2 + 1, i4, false);
            }
            if (70 <= i5 && 99 >= i5) {
                i5 += 1900;
            }
            if (i5 >= 0 && 69 >= i5) {
                i5 += 2000;
            }
            if (!(i5 >= 1601)) {
                throw new IllegalArgumentException("Failed requirement.");
            }
            if (!(iO != -1)) {
                throw new IllegalArgumentException("Failed requirement.");
            }
            if (!(1 <= i7 && 31 >= i7)) {
                throw new IllegalArgumentException("Failed requirement.");
            }
            if (!(i6 >= 0 && 23 >= i6)) {
                throw new IllegalArgumentException("Failed requirement.");
            }
            if (!(i8 >= 0 && 59 >= i8)) {
                throw new IllegalArgumentException("Failed requirement.");
            }
            if (!(i9 >= 0 && 59 >= i9)) {
                throw new IllegalArgumentException("Failed requirement.");
            }
            GregorianCalendar gregorianCalendar = new GregorianCalendar(N2.c.f1407f);
            gregorianCalendar.setLenient(false);
            gregorianCalendar.set(1, i5);
            gregorianCalendar.set(2, iO - 1);
            gregorianCalendar.set(5, i7);
            gregorianCalendar.set(11, i6);
            gregorianCalendar.set(12, i8);
            gregorianCalendar.set(13, i9);
            gregorianCalendar.set(14, 0);
            return gregorianCalendar.getTimeInMillis();
        }

        private final long h(String str) {
            try {
                long j3 = Long.parseLong(str);
                if (j3 <= 0) {
                    return Long.MIN_VALUE;
                }
                return j3;
            } catch (NumberFormatException e4) {
                if (new K2.k("-?\\d+").b(str)) {
                    return K2.o.z(str, "-", false, 2, null) ? Long.MIN_VALUE : Long.MAX_VALUE;
                }
                throw e4;
            }
        }

        public final m c(u uVar, String str) {
            D2.h.f(uVar, "url");
            D2.h.f(str, "setCookie");
            return d(System.currentTimeMillis(), uVar, str);
        }

        public final m d(long j3, u uVar, String str) {
            long j4;
            m mVar;
            String str2;
            String str3;
            D2.h.f(uVar, "url");
            D2.h.f(str, "setCookie");
            int iO = N2.c.o(str, ';', 0, 0, 6, null);
            int iO2 = N2.c.o(str, '=', 0, iO, 2, null);
            if (iO2 == iO) {
                return null;
            }
            String strW = N2.c.W(str, 0, iO2, 1, null);
            if (strW.length() == 0 || N2.c.v(strW) != -1) {
                return null;
            }
            String strV = N2.c.V(str, iO2 + 1, iO);
            if (N2.c.v(strV) != -1) {
                return null;
            }
            int i3 = iO + 1;
            int length = str.length();
            String strF = null;
            String str4 = null;
            boolean z3 = false;
            boolean z4 = false;
            boolean z5 = false;
            boolean z6 = true;
            long jH = -1;
            long jG = 253402300799999L;
            while (i3 < length) {
                int iM = N2.c.m(str, ';', i3, length);
                int iM2 = N2.c.m(str, '=', i3, iM);
                String strV2 = N2.c.V(str, i3, iM2);
                String strV3 = iM2 < iM ? N2.c.V(str, iM2 + 1, iM) : "";
                if (K2.o.n(strV2, "expires", true)) {
                    try {
                        jG = g(strV3, 0, strV3.length());
                        z5 = true;
                    } catch (NumberFormatException | IllegalArgumentException unused) {
                    }
                } else if (K2.o.n(strV2, "max-age", true)) {
                    jH = h(strV3);
                    z5 = true;
                } else if (K2.o.n(strV2, "domain", true)) {
                    strF = f(strV3);
                    z6 = false;
                } else if (K2.o.n(strV2, "path", true)) {
                    str4 = strV3;
                } else if (K2.o.n(strV2, "secure", true)) {
                    z3 = true;
                } else if (K2.o.n(strV2, "httponly", true)) {
                    z4 = true;
                }
                i3 = iM + 1;
            }
            long j5 = Long.MIN_VALUE;
            if (jH == Long.MIN_VALUE) {
                j4 = j5;
            } else if (jH != -1) {
                long j6 = j3 + (jH <= 9223372036854775L ? jH * ((long) 1000) : Long.MAX_VALUE);
                if (j6 >= j3) {
                    j5 = 253402300799999L;
                    if (j6 <= 253402300799999L) {
                        j4 = j6;
                    }
                } else {
                    j5 = 253402300799999L;
                }
                j4 = j5;
            } else {
                j4 = jG;
            }
            String strH = uVar.h();
            if (strF == null) {
                str2 = strH;
                mVar = null;
            } else {
                if (!b(strH, strF)) {
                    return null;
                }
                mVar = null;
                str2 = strF;
            }
            if (strH.length() != str2.length() && PublicSuffixDatabase.f10204h.c().c(str2) == null) {
                return mVar;
            }
            String strSubstring = "/";
            String str5 = str4;
            if (str5 == null || !K2.o.z(str5, "/", false, 2, mVar)) {
                String strD = uVar.d();
                int iT = K2.o.T(strD, '/', 0, false, 6, null);
                if (iT != 0) {
                    if (strD == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    strSubstring = strD.substring(0, iT);
                    D2.h.e(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                }
                str3 = strSubstring;
            } else {
                str3 = str5;
            }
            return new m(strW, strV, j4, str2, str3, z3, z4, z5, z6, null);
        }

        public final List e(u uVar, t tVar) {
            D2.h.f(uVar, "url");
            D2.h.f(tVar, "headers");
            List listI = tVar.i("Set-Cookie");
            int size = listI.size();
            ArrayList arrayList = null;
            for (int i3 = 0; i3 < size; i3++) {
                m mVarC = c(uVar, (String) listI.get(i3));
                if (mVarC != null) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(mVarC);
                }
            }
            if (arrayList == null) {
                return AbstractC0717n.g();
            }
            List listUnmodifiableList = Collections.unmodifiableList(arrayList);
            D2.h.e(listUnmodifiableList, "Collections.unmodifiableList(cookies)");
            return listUnmodifiableList;
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    private m(String str, String str2, long j3, String str3, String str4, boolean z3, boolean z4, boolean z5, boolean z6) {
        this.f1184a = str;
        this.f1185b = str2;
        this.f1186c = j3;
        this.f1187d = str3;
        this.f1188e = str4;
        this.f1189f = z3;
        this.f1190g = z4;
        this.f1191h = z5;
        this.f1192i = z6;
    }

    public final String a() {
        return this.f1184a;
    }

    public final String b() {
        return this.f1185b;
    }

    public boolean equals(Object obj) {
        if (obj instanceof m) {
            m mVar = (m) obj;
            if (D2.h.b(mVar.f1184a, this.f1184a) && D2.h.b(mVar.f1185b, this.f1185b) && mVar.f1186c == this.f1186c && D2.h.b(mVar.f1187d, this.f1187d) && D2.h.b(mVar.f1188e, this.f1188e) && mVar.f1189f == this.f1189f && mVar.f1190g == this.f1190g && mVar.f1191h == this.f1191h && mVar.f1192i == this.f1192i) {
                return true;
            }
        }
        return false;
    }

    public final String g() {
        return this.f1184a;
    }

    public final String h(boolean z3) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.f1184a);
        sb.append('=');
        sb.append(this.f1185b);
        if (this.f1191h) {
            if (this.f1186c == Long.MIN_VALUE) {
                sb.append("; max-age=0");
            } else {
                sb.append("; expires=");
                sb.append(S2.c.b(new Date(this.f1186c)));
            }
        }
        if (!this.f1192i) {
            sb.append("; domain=");
            if (z3) {
                sb.append(".");
            }
            sb.append(this.f1187d);
        }
        sb.append("; path=");
        sb.append(this.f1188e);
        if (this.f1189f) {
            sb.append("; secure");
        }
        if (this.f1190g) {
            sb.append("; httponly");
        }
        String string = sb.toString();
        D2.h.e(string, "toString()");
        return string;
    }

    public int hashCode() {
        return ((((((((((((((((527 + this.f1184a.hashCode()) * 31) + this.f1185b.hashCode()) * 31) + Long.hashCode(this.f1186c)) * 31) + this.f1187d.hashCode()) * 31) + this.f1188e.hashCode()) * 31) + Boolean.hashCode(this.f1189f)) * 31) + Boolean.hashCode(this.f1190g)) * 31) + Boolean.hashCode(this.f1191h)) * 31) + Boolean.hashCode(this.f1192i);
    }

    public final String i() {
        return this.f1185b;
    }

    public String toString() {
        return h(false);
    }

    public /* synthetic */ m(String str, String str2, long j3, String str3, String str4, boolean z3, boolean z4, boolean z5, boolean z6, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, j3, str3, str4, z3, z4, z5, z6);
    }
}

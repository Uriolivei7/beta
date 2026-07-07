package M2;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0711h;

/* JADX INFO: loaded from: classes.dex */
public final class x {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f1252a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f1253b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f1254c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final String[] f1255d;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final a f1251g = new a(null);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final Pattern f1249e = Pattern.compile("([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final Pattern f1250f = Pattern.compile(";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");

    public static final class a {
        private a() {
        }

        public final x a(String str) {
            D2.h.f(str, "mediaType");
            return c(str);
        }

        public final x b(String str) {
            D2.h.f(str, "$this$toMediaType");
            Matcher matcher = x.f1249e.matcher(str);
            if (!matcher.lookingAt()) {
                throw new IllegalArgumentException(("No subtype found for: \"" + str + '\"').toString());
            }
            String strGroup = matcher.group(1);
            D2.h.e(strGroup, "typeSubtype.group(1)");
            Locale locale = Locale.US;
            D2.h.e(locale, "Locale.US");
            if (strGroup == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            String lowerCase = strGroup.toLowerCase(locale);
            D2.h.e(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
            String strGroup2 = matcher.group(2);
            D2.h.e(strGroup2, "typeSubtype.group(2)");
            D2.h.e(locale, "Locale.US");
            if (strGroup2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            String lowerCase2 = strGroup2.toLowerCase(locale);
            D2.h.e(lowerCase2, "(this as java.lang.String).toLowerCase(locale)");
            ArrayList arrayList = new ArrayList();
            Matcher matcher2 = x.f1250f.matcher(str);
            int iEnd = matcher.end();
            while (iEnd < str.length()) {
                matcher2.region(iEnd, str.length());
                if (!matcher2.lookingAt()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Parameter is not formatted correctly: \"");
                    String strSubstring = str.substring(iEnd);
                    D2.h.e(strSubstring, "(this as java.lang.String).substring(startIndex)");
                    sb.append(strSubstring);
                    sb.append("\" for: \"");
                    sb.append(str);
                    sb.append('\"');
                    throw new IllegalArgumentException(sb.toString().toString());
                }
                String strGroup3 = matcher2.group(1);
                if (strGroup3 == null) {
                    iEnd = matcher2.end();
                } else {
                    String strGroup4 = matcher2.group(2);
                    if (strGroup4 == null) {
                        strGroup4 = matcher2.group(3);
                    } else if (K2.o.z(strGroup4, "'", false, 2, null) && K2.o.m(strGroup4, "'", false, 2, null) && strGroup4.length() > 2) {
                        strGroup4 = strGroup4.substring(1, strGroup4.length() - 1);
                        D2.h.e(strGroup4, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    }
                    arrayList.add(strGroup3);
                    arrayList.add(strGroup4);
                    iEnd = matcher2.end();
                }
            }
            Object[] array = arrayList.toArray(new String[0]);
            if (array != null) {
                return new x(str, lowerCase, lowerCase2, (String[]) array, null);
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }

        public final x c(String str) {
            D2.h.f(str, "$this$toMediaTypeOrNull");
            try {
                return b(str);
            } catch (IllegalArgumentException unused) {
                return null;
            }
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    private x(String str, String str2, String str3, String[] strArr) {
        this.f1252a = str;
        this.f1253b = str2;
        this.f1254c = str3;
        this.f1255d = strArr;
    }

    public static /* synthetic */ Charset d(x xVar, Charset charset, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            charset = null;
        }
        return xVar.c(charset);
    }

    public static final x f(String str) {
        return f1251g.c(str);
    }

    public final Charset c(Charset charset) {
        String strE = e("charset");
        if (strE == null) {
            return charset;
        }
        try {
            return Charset.forName(strE);
        } catch (IllegalArgumentException unused) {
            return charset;
        }
    }

    public final String e(String str) {
        D2.h.f(str, "name");
        H2.a aVarH = H2.d.h(AbstractC0711h.p(this.f1255d), 2);
        int iA = aVarH.a();
        int iB = aVarH.b();
        int iC = aVarH.c();
        if (iC >= 0) {
            if (iA > iB) {
                return null;
            }
        } else if (iA < iB) {
            return null;
        }
        while (!K2.o.n(this.f1255d[iA], str, true)) {
            if (iA == iB) {
                return null;
            }
            iA += iC;
        }
        return this.f1255d[iA + 1];
    }

    public boolean equals(Object obj) {
        return (obj instanceof x) && D2.h.b(((x) obj).f1252a, this.f1252a);
    }

    public final String g() {
        return this.f1253b;
    }

    public int hashCode() {
        return this.f1252a.hashCode();
    }

    public String toString() {
        return this.f1252a;
    }

    public /* synthetic */ x(String str, String str2, String str3, String[] strArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, str3, strArr);
    }
}

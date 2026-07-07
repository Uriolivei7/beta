package Z;

import D2.h;
import K2.o;
import java.util.Locale;
import java.util.Map;
import r2.n;
import s2.AbstractC0696D;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f2796a = new a();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final Map f2797b = AbstractC0696D.h(n.a("mkv", "video/x-matroska"), n.a("glb", "model/gltf-binary"));

    private a() {
    }

    private final String a(String str) {
        int iT = o.T(str, '.', 0, false, 6, null);
        if (iT < 0 || iT == str.length() - 1) {
            return null;
        }
        String strSubstring = str.substring(iT + 1);
        h.e(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static final String b(String str) {
        h.f(str, "path");
        String strA = f2796a.a(str);
        if (strA == null) {
            return null;
        }
        Locale locale = Locale.US;
        h.e(locale, "US");
        String lowerCase = strA.toLowerCase(locale);
        h.e(lowerCase, "toLowerCase(...)");
        if (lowerCase == null) {
            return null;
        }
        String strA2 = b.a(lowerCase);
        return strA2 == null ? (String) f2797b.get(lowerCase) : strA2;
    }

    public static final boolean c(String str) {
        if (str != null) {
            return o.z(str, "video/", false, 2, null);
        }
        return false;
    }
}

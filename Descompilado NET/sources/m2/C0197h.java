package M2;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: renamed from: M2.h, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0197h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Map f1038a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f1039b;

    public C0197h(String str, Map<String, String> map) {
        String lowerCase;
        D2.h.f(str, "scheme");
        D2.h.f(map, "authParams");
        this.f1039b = str;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null) {
                Locale locale = Locale.US;
                D2.h.e(locale, "US");
                lowerCase = key.toLowerCase(locale);
                D2.h.e(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
            } else {
                lowerCase = null;
            }
            linkedHashMap.put(lowerCase, value);
        }
        Map mapUnmodifiableMap = Collections.unmodifiableMap(linkedHashMap);
        D2.h.e(mapUnmodifiableMap, "unmodifiableMap<String?, String>(newAuthParams)");
        this.f1038a = mapUnmodifiableMap;
    }

    public final Charset a() {
        String str = (String) this.f1038a.get("charset");
        if (str != null) {
            try {
                Charset charsetForName = Charset.forName(str);
                D2.h.e(charsetForName, "Charset.forName(charset)");
                return charsetForName;
            } catch (Exception unused) {
            }
        }
        Charset charset = StandardCharsets.ISO_8859_1;
        D2.h.e(charset, "ISO_8859_1");
        return charset;
    }

    public final String b() {
        return (String) this.f1038a.get("realm");
    }

    public final String c() {
        return this.f1039b;
    }

    public boolean equals(Object obj) {
        if (obj instanceof C0197h) {
            C0197h c0197h = (C0197h) obj;
            if (D2.h.b(c0197h.f1039b, this.f1039b) && D2.h.b(c0197h.f1038a, this.f1038a)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return ((899 + this.f1039b.hashCode()) * 31) + this.f1038a.hashCode();
    }

    public String toString() {
        return this.f1039b + " authParams=" + this.f1038a;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public C0197h(String str, String str2) {
        D2.h.f(str, "scheme");
        D2.h.f(str2, "realm");
        Map mapSingletonMap = Collections.singletonMap("realm", str2);
        D2.h.e(mapSingletonMap, "singletonMap(\"realm\", realm)");
        this(str, (Map<String, String>) mapSingletonMap);
    }
}

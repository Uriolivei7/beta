package K2;

import java.nio.charset.Charset;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final d f815a = new d();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final Charset f816b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final Charset f817c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final Charset f818d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final Charset f819e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final Charset f820f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final Charset f821g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static volatile Charset f822h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static volatile Charset f823i;

    static {
        Charset charsetForName = Charset.forName("UTF-8");
        D2.h.e(charsetForName, "forName(...)");
        f816b = charsetForName;
        Charset charsetForName2 = Charset.forName("UTF-16");
        D2.h.e(charsetForName2, "forName(...)");
        f817c = charsetForName2;
        Charset charsetForName3 = Charset.forName("UTF-16BE");
        D2.h.e(charsetForName3, "forName(...)");
        f818d = charsetForName3;
        Charset charsetForName4 = Charset.forName("UTF-16LE");
        D2.h.e(charsetForName4, "forName(...)");
        f819e = charsetForName4;
        Charset charsetForName5 = Charset.forName("US-ASCII");
        D2.h.e(charsetForName5, "forName(...)");
        f820f = charsetForName5;
        Charset charsetForName6 = Charset.forName("ISO-8859-1");
        D2.h.e(charsetForName6, "forName(...)");
        f821g = charsetForName6;
    }

    private d() {
    }

    public final Charset a() {
        Charset charset = f823i;
        if (charset != null) {
            return charset;
        }
        Charset charsetForName = Charset.forName("UTF-32BE");
        D2.h.e(charsetForName, "forName(...)");
        f823i = charsetForName;
        return charsetForName;
    }

    public final Charset b() {
        Charset charset = f822h;
        if (charset != null) {
            return charset;
        }
        Charset charsetForName = Charset.forName("UTF-32LE");
        D2.h.e(charsetForName, "forName(...)");
        f822h = charsetForName;
        return charsetForName;
    }
}

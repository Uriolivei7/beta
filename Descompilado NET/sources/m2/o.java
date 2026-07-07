package M2;

import java.nio.charset.Charset;

/* JADX INFO: loaded from: classes.dex */
public final class o {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final o f1204a = new o();

    private o() {
    }

    public static final String a(String str, String str2, Charset charset) {
        D2.h.f(str, "username");
        D2.h.f(str2, "password");
        D2.h.f(charset, "charset");
        return "Basic " + b3.l.f5639f.d(str + ':' + str2, charset).a();
    }
}

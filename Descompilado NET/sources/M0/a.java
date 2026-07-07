package M0;

import O0.j;

/* JADX INFO: loaded from: classes.dex */
public final class a extends RuntimeException {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final j f869b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(String str, j jVar) {
        super(str);
        D2.h.f(jVar, "encodedImage");
        this.f869b = jVar;
    }

    public final j a() {
        return this.f869b;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(String str, Throwable th, j jVar) {
        super(str, th);
        D2.h.f(jVar, "encodedImage");
        this.f869b = jVar;
    }
}

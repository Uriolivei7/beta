package M2;

import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public final class k {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final R2.h f1163a;

    public k(R2.h hVar) {
        D2.h.f(hVar, "delegate");
        this.f1163a = hVar;
    }

    public final R2.h a() {
        return this.f1163a;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public k(int i3, long j3, TimeUnit timeUnit) {
        this(new R2.h(Q2.e.f1870h, i3, j3, timeUnit));
        D2.h.f(timeUnit, "timeUnit");
    }

    public k() {
        this(5, 5L, TimeUnit.MINUTES);
    }
}

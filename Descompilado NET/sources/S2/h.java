package S2;

import M2.E;
import M2.x;

/* JADX INFO: loaded from: classes.dex */
public final class h extends E {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f2332c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final long f2333d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final b3.k f2334e;

    public h(String str, long j3, b3.k kVar) {
        D2.h.f(kVar, "source");
        this.f2332c = str;
        this.f2333d = j3;
        this.f2334e = kVar;
    }

    @Override // M2.E
    public long q() {
        return this.f2333d;
    }

    @Override // M2.E
    public x v() {
        String str = this.f2332c;
        if (str != null) {
            return x.f1251g.c(str);
        }
        return null;
    }

    @Override // M2.E
    public b3.k z() {
        return this.f2334e;
    }
}

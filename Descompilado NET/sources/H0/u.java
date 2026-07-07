package H0;

import b0.AbstractC0306a;

/* JADX INFO: loaded from: classes.dex */
public class u implements x {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final x f314a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final z f315b;

    public u(x xVar, z zVar) {
        this.f314a = xVar;
        this.f315b = zVar;
    }

    @Override // H0.x
    public AbstractC0306a b(Object obj, AbstractC0306a abstractC0306a) {
        this.f315b.a(obj);
        return this.f314a.b(obj, abstractC0306a);
    }

    @Override // H0.x
    public void c(Object obj) {
        this.f314a.c(obj);
    }

    @Override // H0.x
    public boolean d(X.l lVar) {
        return this.f314a.d(lVar);
    }

    @Override // H0.x
    public int e(X.l lVar) {
        return this.f314a.e(lVar);
    }

    @Override // H0.x
    public AbstractC0306a get(Object obj) {
        AbstractC0306a abstractC0306a = this.f314a.get(obj);
        if (abstractC0306a == null) {
            this.f315b.c(obj);
        } else {
            this.f315b.b(obj);
        }
        return abstractC0306a;
    }
}

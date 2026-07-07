package S2;

import M2.B;
import M2.D;
import M2.InterfaceC0194e;
import M2.v;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class g implements v.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f2323a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final R2.e f2324b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final List f2325c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f2326d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final R2.c f2327e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final B f2328f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final int f2329g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final int f2330h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final int f2331i;

    public g(R2.e eVar, List<? extends v> list, int i3, R2.c cVar, B b4, int i4, int i5, int i6) {
        D2.h.f(eVar, "call");
        D2.h.f(list, "interceptors");
        D2.h.f(b4, "request");
        this.f2324b = eVar;
        this.f2325c = list;
        this.f2326d = i3;
        this.f2327e = cVar;
        this.f2328f = b4;
        this.f2329g = i4;
        this.f2330h = i5;
        this.f2331i = i6;
    }

    public static /* synthetic */ g c(g gVar, int i3, R2.c cVar, B b4, int i4, int i5, int i6, int i7, Object obj) {
        if ((i7 & 1) != 0) {
            i3 = gVar.f2326d;
        }
        if ((i7 & 2) != 0) {
            cVar = gVar.f2327e;
        }
        R2.c cVar2 = cVar;
        if ((i7 & 4) != 0) {
            b4 = gVar.f2328f;
        }
        B b5 = b4;
        if ((i7 & 8) != 0) {
            i4 = gVar.f2329g;
        }
        int i8 = i4;
        if ((i7 & 16) != 0) {
            i5 = gVar.f2330h;
        }
        int i9 = i5;
        if ((i7 & 32) != 0) {
            i6 = gVar.f2331i;
        }
        return gVar.b(i3, cVar2, b5, i8, i9, i6);
    }

    @Override // M2.v.a
    public D a(B b4) {
        D2.h.f(b4, "request");
        if (!(this.f2326d < this.f2325c.size())) {
            throw new IllegalStateException("Check failed.");
        }
        this.f2323a++;
        R2.c cVar = this.f2327e;
        if (cVar != null) {
            if (!cVar.j().g(b4.l())) {
                throw new IllegalStateException(("network interceptor " + ((v) this.f2325c.get(this.f2326d - 1)) + " must retain the same host and port").toString());
            }
            if (!(this.f2323a == 1)) {
                throw new IllegalStateException(("network interceptor " + ((v) this.f2325c.get(this.f2326d - 1)) + " must call proceed() exactly once").toString());
            }
        }
        g gVarC = c(this, this.f2326d + 1, null, b4, 0, 0, 0, 58, null);
        v vVar = (v) this.f2325c.get(this.f2326d);
        D dA = vVar.a(gVarC);
        if (dA == null) {
            throw new NullPointerException("interceptor " + vVar + " returned null");
        }
        if (this.f2327e != null) {
            if (!(this.f2326d + 1 >= this.f2325c.size() || gVarC.f2323a == 1)) {
                throw new IllegalStateException(("network interceptor " + vVar + " must call proceed() exactly once").toString());
            }
        }
        if (dA.q() != null) {
            return dA;
        }
        throw new IllegalStateException(("interceptor " + vVar + " returned a response with no body").toString());
    }

    public final g b(int i3, R2.c cVar, B b4, int i4, int i5, int i6) {
        D2.h.f(b4, "request");
        return new g(this.f2324b, this.f2325c, i3, cVar, b4, i4, i5, i6);
    }

    @Override // M2.v.a
    public InterfaceC0194e call() {
        return this.f2324b;
    }

    public final R2.e d() {
        return this.f2324b;
    }

    public final int e() {
        return this.f2329g;
    }

    public final R2.c f() {
        return this.f2327e;
    }

    public final int g() {
        return this.f2330h;
    }

    public final B h() {
        return this.f2328f;
    }

    @Override // M2.v.a
    public B i() {
        return this.f2328f;
    }

    public final int j() {
        return this.f2331i;
    }

    public int k() {
        return this.f2330h;
    }
}

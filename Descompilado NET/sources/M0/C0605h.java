package m0;

import H0.x;
import X.n;
import android.content.res.Resources;
import java.util.concurrent.Executor;
import p0.AbstractC0635a;

/* JADX INFO: renamed from: m0.h, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0605h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Resources f9831a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private AbstractC0635a f9832b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private N0.a f9833c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private N0.a f9834d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Executor f9835e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private x f9836f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private X.f f9837g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private n f9838h;

    public void a(Resources resources, AbstractC0635a abstractC0635a, N0.a aVar, N0.a aVar2, Executor executor, x xVar, X.f fVar, n nVar) {
        this.f9831a = resources;
        this.f9832b = abstractC0635a;
        this.f9833c = aVar;
        this.f9834d = aVar2;
        this.f9835e = executor;
        this.f9836f = xVar;
        this.f9837g = fVar;
        this.f9838h = nVar;
    }

    protected C0602e b(Resources resources, AbstractC0635a abstractC0635a, N0.a aVar, N0.a aVar2, Executor executor, x xVar, X.f fVar) {
        return new C0602e(resources, abstractC0635a, aVar, aVar2, executor, xVar, fVar);
    }

    public C0602e c() {
        C0602e c0602eB = b(this.f9831a, this.f9832b, this.f9833c, this.f9834d, this.f9835e, this.f9836f, this.f9837g);
        n nVar = this.f9838h;
        if (nVar != null) {
            c0602eB.A0(((Boolean) nVar.get()).booleanValue());
        }
        return c0602eB;
    }
}

package m0;

import H0.x;
import O0.l;
import X.i;
import X.k;
import X.n;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import b0.AbstractC0306a;
import com.facebook.common.time.AwakeTimeSinceBootClock;
import h0.InterfaceC0548c;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import n0.C0617a;
import p0.AbstractC0635a;
import q0.AbstractC0645a;
import q0.AbstractC0646b;
import r0.C0668a;
import s0.C0690a;
import t0.C0721a;
import t0.InterfaceC0723c;
import t0.p;
import t0.r;
import w0.InterfaceC0760b;
import z0.InterfaceC0788g;

/* JADX INFO: renamed from: m0.e, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0602e extends AbstractC0645a {

    /* JADX INFO: renamed from: M, reason: collision with root package name */
    private static final Class f9808M = C0602e.class;

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private final N0.a f9809A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private final X.f f9810B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private final x f9811C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private R.d f9812D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private n f9813E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private boolean f9814F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private X.f f9815G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private C0617a f9816H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    private Set f9817I;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    private U0.b f9818J;

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    private U0.b[] f9819K;

    /* JADX INFO: renamed from: L, reason: collision with root package name */
    private U0.b f9820L;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private final Resources f9821z;

    public C0602e(Resources resources, AbstractC0635a abstractC0635a, N0.a aVar, N0.a aVar2, Executor executor, x xVar, X.f fVar) {
        super(abstractC0635a, executor, null, null);
        this.f9821z = resources;
        this.f9809A = new C0598a(resources, aVar, aVar2);
        this.f9810B = fVar;
        this.f9811C = xVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static p k0(Drawable drawable) {
        if (drawable == 0) {
            return null;
        }
        if (drawable instanceof p) {
            return (p) drawable;
        }
        if (drawable instanceof InterfaceC0723c) {
            return k0(((InterfaceC0723c) drawable).q());
        }
        if (drawable instanceof C0721a) {
            C0721a c0721a = (C0721a) drawable;
            int iD = c0721a.d();
            for (int i3 = 0; i3 < iD; i3++) {
                p pVarK0 = k0(c0721a.b(i3));
                if (pVarK0 != null) {
                    return pVarK0;
                }
            }
        }
        return null;
    }

    private void q0(n nVar) {
        this.f9813E = nVar;
        u0(null);
    }

    private Drawable t0(X.f fVar, O0.d dVar) {
        Drawable drawableB;
        if (fVar == null) {
            return null;
        }
        Iterator<E> it = fVar.iterator();
        while (it.hasNext()) {
            N0.a aVar = (N0.a) it.next();
            if (aVar.a(dVar) && (drawableB = aVar.b(dVar)) != null) {
                return drawableB;
            }
        }
        return null;
    }

    private void u0(O0.d dVar) {
        if (this.f9814F) {
            if (r() == null) {
                C0668a c0668a = new C0668a();
                j(new C0690a(c0668a));
                a0(c0668a);
            }
            if (r() instanceof C0668a) {
                B0(dVar, (C0668a) r());
            }
        }
    }

    public void A0(boolean z3) {
        this.f9814F = z3;
    }

    protected void B0(O0.d dVar, C0668a c0668a) {
        p pVarK0;
        c0668a.j(v());
        InterfaceC0760b interfaceC0760bB = b();
        r rVarA = null;
        if (interfaceC0760bB != null && (pVarK0 = k0(interfaceC0760bB.d())) != null) {
            rVarA = pVarK0.A();
        }
        c0668a.m(rVarA);
        String strM0 = m0();
        if (strM0 != null) {
            c0668a.b("cc", strM0);
        }
        if (dVar == null) {
            c0668a.i();
        } else {
            c0668a.k(dVar.h(), dVar.d());
            c0668a.l(dVar.b0());
        }
    }

    @Override // q0.AbstractC0645a, w0.InterfaceC0759a
    public void c(InterfaceC0760b interfaceC0760b) {
        super.c(interfaceC0760b);
        u0(null);
    }

    public synchronized void i0(Q0.e eVar) {
        try {
            if (this.f9817I == null) {
                this.f9817I = new HashSet();
            }
            this.f9817I.add(eVar);
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // q0.AbstractC0645a
    /* JADX INFO: renamed from: j0, reason: merged with bridge method [inline-methods] */
    public Drawable l(AbstractC0306a abstractC0306a) {
        try {
            if (V0.b.d()) {
                V0.b.a("PipelineDraweeController#createDrawable");
            }
            k.i(AbstractC0306a.c0(abstractC0306a));
            O0.d dVar = (O0.d) abstractC0306a.P();
            u0(dVar);
            Drawable drawableT0 = t0(this.f9815G, dVar);
            if (drawableT0 != null) {
                if (V0.b.d()) {
                    V0.b.b();
                }
                return drawableT0;
            }
            Drawable drawableT02 = t0(this.f9810B, dVar);
            if (drawableT02 != null) {
                if (V0.b.d()) {
                    V0.b.b();
                }
                return drawableT02;
            }
            Drawable drawableB = this.f9809A.b(dVar);
            if (drawableB != null) {
                if (V0.b.d()) {
                    V0.b.b();
                }
                return drawableB;
            }
            throw new UnsupportedOperationException("Unrecognized image class: " + dVar);
        } catch (Throwable th) {
            if (V0.b.d()) {
                V0.b.b();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // q0.AbstractC0645a
    /* JADX INFO: renamed from: l0, reason: merged with bridge method [inline-methods] */
    public AbstractC0306a n() {
        R.d dVar;
        if (V0.b.d()) {
            V0.b.a("PipelineDraweeController#getCachedImage");
        }
        try {
            x xVar = this.f9811C;
            if (xVar != null && (dVar = this.f9812D) != null) {
                AbstractC0306a abstractC0306a = xVar.get(dVar);
                if (abstractC0306a != null && !((O0.d) abstractC0306a.P()).l().a()) {
                    abstractC0306a.close();
                    return null;
                }
                if (V0.b.d()) {
                    V0.b.b();
                }
                return abstractC0306a;
            }
            if (V0.b.d()) {
                V0.b.b();
            }
            return null;
        } finally {
            if (V0.b.d()) {
                V0.b.b();
            }
        }
    }

    protected String m0() {
        Object objO = o();
        if (objO == null) {
            return null;
        }
        return objO.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // q0.AbstractC0645a
    /* JADX INFO: renamed from: n0, reason: merged with bridge method [inline-methods] */
    public int x(AbstractC0306a abstractC0306a) {
        if (abstractC0306a != null) {
            return abstractC0306a.X();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // q0.AbstractC0645a
    /* JADX INFO: renamed from: o0, reason: merged with bridge method [inline-methods] */
    public l y(AbstractC0306a abstractC0306a) {
        k.i(AbstractC0306a.c0(abstractC0306a));
        return ((O0.d) abstractC0306a.P()).r();
    }

    public synchronized Q0.e p0() {
        Set set = this.f9817I;
        if (set == null) {
            return null;
        }
        return new Q0.c((Set<Q0.e>) set);
    }

    public void r0(n nVar, String str, R.d dVar, Object obj, X.f fVar) {
        if (V0.b.d()) {
            V0.b.a("PipelineDraweeController#initialize");
        }
        super.D(str, obj);
        q0(nVar);
        this.f9812D = dVar;
        z0(fVar);
        u0(null);
        if (V0.b.d()) {
            V0.b.b();
        }
    }

    @Override // q0.AbstractC0645a
    protected InterfaceC0548c s() {
        if (V0.b.d()) {
            V0.b.a("PipelineDraweeController#getDataSource");
        }
        if (Y.a.w(2)) {
            Y.a.y(f9808M, "controller %x: getDataSource", Integer.valueOf(System.identityHashCode(this)));
        }
        InterfaceC0548c interfaceC0548c = (InterfaceC0548c) this.f9813E.get();
        if (V0.b.d()) {
            V0.b.b();
        }
        return interfaceC0548c;
    }

    protected synchronized void s0(InterfaceC0788g interfaceC0788g, AbstractC0646b abstractC0646b) {
        try {
            C0617a c0617a = this.f9816H;
            if (c0617a != null) {
                c0617a.f();
            }
            if (interfaceC0788g != null) {
                if (this.f9816H == null) {
                    this.f9816H = new C0617a(AwakeTimeSinceBootClock.get(), this);
                }
                this.f9816H.c(interfaceC0788g);
                this.f9816H.g(true);
            }
            this.f9818J = (U0.b) abstractC0646b.o();
            this.f9819K = (U0.b[]) abstractC0646b.n();
            this.f9820L = (U0.b) abstractC0646b.p();
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // q0.AbstractC0645a
    public String toString() {
        return i.b(this).b("super", super.toString()).b("dataSourceSupplier", this.f9813E).toString();
    }

    @Override // q0.AbstractC0645a
    /* JADX INFO: renamed from: v0, reason: merged with bridge method [inline-methods] */
    public Map K(l lVar) {
        if (lVar == null) {
            return null;
        }
        return lVar.a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // q0.AbstractC0645a
    /* JADX INFO: renamed from: w0, reason: merged with bridge method [inline-methods] */
    public void M(String str, AbstractC0306a abstractC0306a) {
        super.M(str, abstractC0306a);
        synchronized (this) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // q0.AbstractC0645a
    /* JADX INFO: renamed from: x0, reason: merged with bridge method [inline-methods] */
    public void R(AbstractC0306a abstractC0306a) {
        AbstractC0306a.D(abstractC0306a);
    }

    public synchronized void y0(Q0.e eVar) {
        Set set = this.f9817I;
        if (set == null) {
            return;
        }
        set.remove(eVar);
    }

    @Override // q0.AbstractC0645a
    protected Uri z() {
        return z0.l.a(this.f9818J, this.f9820L, this.f9819K, U0.b.f2380A);
    }

    public void z0(X.f fVar) {
        this.f9815G = fVar;
    }

    @Override // q0.AbstractC0645a
    protected void P(Drawable drawable) {
    }
}

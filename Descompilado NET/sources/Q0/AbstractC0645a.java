package q0;

import X.g;
import X.i;
import X.k;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.MotionEvent;
import h0.AbstractC0547b;
import h0.InterfaceC0548c;
import java.util.Map;
import java.util.concurrent.Executor;
import p0.AbstractC0635a;
import p0.c;
import u0.C0734a;
import v0.C0755a;
import w0.InterfaceC0759a;
import w0.InterfaceC0760b;
import w0.InterfaceC0761c;
import y0.C0777b;
import z0.C0785d;
import z0.InterfaceC0783b;

/* JADX INFO: renamed from: q0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0645a implements InterfaceC0759a, AbstractC0635a.InterfaceC0140a, C0755a.InterfaceC0149a {

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private static final Map f10334w = g.of("component_tag", "drawee");

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private static final Map f10335x = g.of("origin", "memory_bitmap", "origin_sub", "shortcut");

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private static final Class f10336y = AbstractC0645a.class;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final AbstractC0635a f10338b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Executor f10339c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private p0.d f10340d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private C0755a f10341e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    protected InterfaceC0648d f10342f;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private InterfaceC0761c f10344h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private Drawable f10345i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private String f10346j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private Object f10347k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f10348l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private boolean f10349m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private boolean f10350n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private boolean f10351o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private boolean f10352p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private String f10353q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private InterfaceC0548c f10354r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private Object f10355s;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    protected Drawable f10358v;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final p0.c f10337a = p0.c.a();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    protected C0785d f10343g = new C0785d();

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private boolean f10356t = true;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private boolean f10357u = false;

    /* JADX INFO: renamed from: q0.a$a, reason: collision with other inner class name */
    class C0141a extends AbstractC0547b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f10359a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ boolean f10360b;

        C0141a(String str, boolean z3) {
            this.f10359a = str;
            this.f10360b = z3;
        }

        @Override // h0.AbstractC0547b, h0.InterfaceC0550e
        public void b(InterfaceC0548c interfaceC0548c) {
            boolean zE = interfaceC0548c.e();
            AbstractC0645a.this.O(this.f10359a, interfaceC0548c, interfaceC0548c.g(), zE);
        }

        @Override // h0.AbstractC0547b
        public void e(InterfaceC0548c interfaceC0548c) {
            AbstractC0645a.this.L(this.f10359a, interfaceC0548c, interfaceC0548c.f(), true);
        }

        @Override // h0.AbstractC0547b
        public void f(InterfaceC0548c interfaceC0548c) {
            boolean zE = interfaceC0548c.e();
            boolean zC = interfaceC0548c.c();
            float fG = interfaceC0548c.g();
            Object objB = interfaceC0548c.b();
            if (objB != null) {
                AbstractC0645a.this.N(this.f10359a, interfaceC0548c, objB, fG, zE, this.f10360b, zC);
            } else if (zE) {
                AbstractC0645a.this.L(this.f10359a, interfaceC0548c, new NullPointerException(), true);
            }
        }
    }

    /* JADX INFO: renamed from: q0.a$b */
    private static class b extends f {
        private b() {
        }

        public static b f(InterfaceC0648d interfaceC0648d, InterfaceC0648d interfaceC0648d2) {
            if (V0.b.d()) {
                V0.b.a("AbstractDraweeController#createInternal");
            }
            b bVar = new b();
            bVar.c(interfaceC0648d);
            bVar.c(interfaceC0648d2);
            if (V0.b.d()) {
                V0.b.b();
            }
            return bVar;
        }
    }

    public AbstractC0645a(AbstractC0635a abstractC0635a, Executor executor, String str, Object obj) {
        this.f10338b = abstractC0635a;
        this.f10339c = executor;
        C(str, obj);
    }

    private InterfaceC0761c B() {
        InterfaceC0761c interfaceC0761c = this.f10344h;
        if (interfaceC0761c != null) {
            return interfaceC0761c;
        }
        throw new IllegalStateException("mSettableDraweeHierarchy is null; Caller context: " + this.f10347k);
    }

    private synchronized void C(String str, Object obj) {
        AbstractC0635a abstractC0635a;
        try {
            if (V0.b.d()) {
                V0.b.a("AbstractDraweeController#init");
            }
            this.f10337a.b(c.a.ON_INIT_CONTROLLER);
            if (!this.f10356t && (abstractC0635a = this.f10338b) != null) {
                abstractC0635a.a(this);
            }
            this.f10348l = false;
            this.f10350n = false;
            Q();
            this.f10352p = false;
            p0.d dVar = this.f10340d;
            if (dVar != null) {
                dVar.a();
            }
            C0755a c0755a = this.f10341e;
            if (c0755a != null) {
                c0755a.a();
                this.f10341e.f(this);
            }
            InterfaceC0648d interfaceC0648d = this.f10342f;
            if (interfaceC0648d instanceof b) {
                ((b) interfaceC0648d).d();
            } else {
                this.f10342f = null;
            }
            InterfaceC0761c interfaceC0761c = this.f10344h;
            if (interfaceC0761c != null) {
                interfaceC0761c.h();
                this.f10344h.c(null);
                this.f10344h = null;
            }
            this.f10345i = null;
            if (Y.a.w(2)) {
                Y.a.A(f10336y, "controller %x %s -> %s: initialize", Integer.valueOf(System.identityHashCode(this)), this.f10346j, str);
            }
            this.f10346j = str;
            this.f10347k = obj;
            if (V0.b.d()) {
                V0.b.b();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private boolean E(String str, InterfaceC0548c interfaceC0548c) {
        if (interfaceC0548c == null && this.f10354r == null) {
            return true;
        }
        return str.equals(this.f10346j) && interfaceC0548c == this.f10354r && this.f10349m;
    }

    private void G(String str, Throwable th) {
        if (Y.a.w(2)) {
            Y.a.B(f10336y, "controller %x %s: %s: failure: %s", Integer.valueOf(System.identityHashCode(this)), this.f10346j, str, th);
        }
    }

    private void H(String str, Object obj) {
        if (Y.a.w(2)) {
            Y.a.C(f10336y, "controller %x %s: %s: image: %s %x", Integer.valueOf(System.identityHashCode(this)), this.f10346j, str, w(obj), Integer.valueOf(x(obj)));
        }
    }

    private InterfaceC0783b.a I(InterfaceC0548c interfaceC0548c, Object obj, Uri uri) {
        return J(interfaceC0548c == null ? null : interfaceC0548c.a(), K(obj), uri);
    }

    private InterfaceC0783b.a J(Map map, Map map2, Uri uri) {
        String str;
        PointF pointFN;
        InterfaceC0761c interfaceC0761c = this.f10344h;
        if (interfaceC0761c instanceof C0734a) {
            C0734a c0734a = (C0734a) interfaceC0761c;
            String strValueOf = String.valueOf(c0734a.o());
            pointFN = c0734a.n();
            str = strValueOf;
        } else {
            str = null;
            pointFN = null;
        }
        return C0777b.a(f10334w, f10335x, map, null, t(), str, pointFN, map2, o(), F(), uri);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void L(String str, InterfaceC0548c interfaceC0548c, Throwable th, boolean z3) {
        Drawable drawable;
        if (V0.b.d()) {
            V0.b.a("AbstractDraweeController#onFailureInternal");
        }
        if (!E(str, interfaceC0548c)) {
            G("ignore_old_datasource @ onFailure", th);
            interfaceC0548c.close();
            if (V0.b.d()) {
                V0.b.b();
                return;
            }
            return;
        }
        this.f10337a.b(z3 ? c.a.ON_DATASOURCE_FAILURE : c.a.ON_DATASOURCE_FAILURE_INT);
        if (z3) {
            G("final_failed @ onFailure", th);
            this.f10354r = null;
            this.f10351o = true;
            InterfaceC0761c interfaceC0761c = this.f10344h;
            if (interfaceC0761c != null) {
                if (this.f10352p && (drawable = this.f10358v) != null) {
                    interfaceC0761c.e(drawable, 1.0f, true);
                } else if (g0()) {
                    interfaceC0761c.f(th);
                } else {
                    interfaceC0761c.g(th);
                }
            }
            T(th, interfaceC0548c);
        } else {
            G("intermediate_failed @ onFailure", th);
            U(th);
        }
        if (V0.b.d()) {
            V0.b.b();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void N(String str, InterfaceC0548c interfaceC0548c, Object obj, float f3, boolean z3, boolean z4, boolean z5) {
        try {
            if (V0.b.d()) {
                V0.b.a("AbstractDraweeController#onNewResultInternal");
            }
            if (!E(str, interfaceC0548c)) {
                H("ignore_old_datasource @ onNewResult", obj);
                R(obj);
                interfaceC0548c.close();
                if (V0.b.d()) {
                    V0.b.b();
                    return;
                }
                return;
            }
            this.f10337a.b(z3 ? c.a.ON_DATASOURCE_RESULT : c.a.ON_DATASOURCE_RESULT_INT);
            try {
                Drawable drawableL = l(obj);
                Object obj2 = this.f10355s;
                Drawable drawable = this.f10358v;
                this.f10355s = obj;
                this.f10358v = drawableL;
                try {
                    if (z3) {
                        H("set_final_result @ onNewResult", obj);
                        this.f10354r = null;
                        B().e(drawableL, 1.0f, z4);
                        Y(str, obj, interfaceC0548c);
                    } else if (z5) {
                        H("set_temporary_result @ onNewResult", obj);
                        B().e(drawableL, 1.0f, z4);
                        Y(str, obj, interfaceC0548c);
                    } else {
                        H("set_intermediate_result @ onNewResult", obj);
                        B().e(drawableL, f3, z4);
                        V(str, obj);
                    }
                    if (drawable != null && drawable != drawableL) {
                        P(drawable);
                    }
                    if (obj2 != null && obj2 != obj) {
                        H("release_previous_result @ onNewResult", obj2);
                        R(obj2);
                    }
                    if (V0.b.d()) {
                        V0.b.b();
                    }
                } catch (Throwable th) {
                    if (drawable != null && drawable != drawableL) {
                        P(drawable);
                    }
                    if (obj2 != null && obj2 != obj) {
                        H("release_previous_result @ onNewResult", obj2);
                        R(obj2);
                    }
                    throw th;
                }
            } catch (Exception e4) {
                H("drawable_failed @ onNewResult", obj);
                R(obj);
                L(str, interfaceC0548c, e4, z3);
                if (V0.b.d()) {
                    V0.b.b();
                }
            }
        } catch (Throwable th2) {
            if (V0.b.d()) {
                V0.b.b();
            }
            throw th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void O(String str, InterfaceC0548c interfaceC0548c, float f3, boolean z3) {
        if (!E(str, interfaceC0548c)) {
            G("ignore_old_datasource @ onProgress", null);
            interfaceC0548c.close();
        } else {
            if (z3) {
                return;
            }
            this.f10344h.a(f3, false);
        }
    }

    private void Q() {
        Map mapA;
        boolean z3 = this.f10349m;
        this.f10349m = false;
        this.f10351o = false;
        InterfaceC0548c interfaceC0548c = this.f10354r;
        Map map = null;
        if (interfaceC0548c != null) {
            mapA = interfaceC0548c.a();
            this.f10354r.close();
            this.f10354r = null;
        } else {
            mapA = null;
        }
        Drawable drawable = this.f10358v;
        if (drawable != null) {
            P(drawable);
        }
        if (this.f10353q != null) {
            this.f10353q = null;
        }
        this.f10358v = null;
        Object obj = this.f10355s;
        if (obj != null) {
            Map mapK = K(y(obj));
            H("release", this.f10355s);
            R(this.f10355s);
            this.f10355s = null;
            map = mapK;
        }
        if (z3) {
            W(mapA, map);
        }
    }

    private void T(Throwable th, InterfaceC0548c interfaceC0548c) {
        InterfaceC0783b.a aVarI = I(interfaceC0548c, null, null);
        p().r(this.f10346j, th);
        q().y(this.f10346j, th, aVarI);
    }

    private void U(Throwable th) {
        p().l(this.f10346j, th);
        q().q(this.f10346j);
    }

    private void V(String str, Object obj) {
        Object objY = y(obj);
        p().a(str, objY);
        q().a(str, objY);
    }

    private void W(Map map, Map map2) {
        p().b(this.f10346j);
        q().v(this.f10346j, J(map, map2, null));
    }

    private void Y(String str, Object obj, InterfaceC0548c interfaceC0548c) {
        Object objY = y(obj);
        p().k(str, objY, m());
        q().z(str, objY, I(interfaceC0548c, objY, null));
    }

    private boolean g0() {
        p0.d dVar;
        return this.f10351o && (dVar = this.f10340d) != null && dVar.e();
    }

    private Rect t() {
        InterfaceC0761c interfaceC0761c = this.f10344h;
        if (interfaceC0761c == null) {
            return null;
        }
        return interfaceC0761c.b();
    }

    protected p0.d A() {
        if (this.f10340d == null) {
            this.f10340d = new p0.d();
        }
        return this.f10340d;
    }

    protected void D(String str, Object obj) {
        C(str, obj);
        this.f10356t = false;
        this.f10357u = false;
    }

    protected boolean F() {
        return this.f10357u;
    }

    public abstract Map K(Object obj);

    protected abstract void P(Drawable drawable);

    protected abstract void R(Object obj);

    public void S(InterfaceC0783b interfaceC0783b) {
        this.f10343g.D(interfaceC0783b);
    }

    protected void X(InterfaceC0548c interfaceC0548c, Object obj) {
        p().j(this.f10346j, this.f10347k);
        q().o(this.f10346j, this.f10347k, I(interfaceC0548c, obj, z()));
    }

    public void Z(String str) {
        this.f10353q = str;
    }

    @Override // w0.InterfaceC0759a
    public void a() {
        if (V0.b.d()) {
            V0.b.a("AbstractDraweeController#onDetach");
        }
        if (Y.a.w(2)) {
            Y.a.z(f10336y, "controller %x %s: onDetach", Integer.valueOf(System.identityHashCode(this)), this.f10346j);
        }
        this.f10337a.b(c.a.ON_DETACH_CONTROLLER);
        this.f10348l = false;
        this.f10338b.d(this);
        if (V0.b.d()) {
            V0.b.b();
        }
    }

    protected void a0(Drawable drawable) {
        this.f10345i = drawable;
        InterfaceC0761c interfaceC0761c = this.f10344h;
        if (interfaceC0761c != null) {
            interfaceC0761c.c(drawable);
        }
    }

    @Override // w0.InterfaceC0759a
    public InterfaceC0760b b() {
        return this.f10344h;
    }

    @Override // w0.InterfaceC0759a
    public void c(InterfaceC0760b interfaceC0760b) {
        if (Y.a.w(2)) {
            Y.a.A(f10336y, "controller %x %s: setHierarchy: %s", Integer.valueOf(System.identityHashCode(this)), this.f10346j, interfaceC0760b);
        }
        this.f10337a.b(interfaceC0760b != null ? c.a.ON_SET_HIERARCHY : c.a.ON_CLEAR_HIERARCHY);
        if (this.f10349m) {
            this.f10338b.a(this);
            release();
        }
        InterfaceC0761c interfaceC0761c = this.f10344h;
        if (interfaceC0761c != null) {
            interfaceC0761c.c(null);
            this.f10344h = null;
        }
        if (interfaceC0760b != null) {
            k.b(Boolean.valueOf(interfaceC0760b instanceof InterfaceC0761c));
            InterfaceC0761c interfaceC0761c2 = (InterfaceC0761c) interfaceC0760b;
            this.f10344h = interfaceC0761c2;
            interfaceC0761c2.c(this.f10345i);
        }
    }

    protected void c0(C0755a c0755a) {
        this.f10341e = c0755a;
        if (c0755a != null) {
            c0755a.f(this);
        }
    }

    @Override // w0.InterfaceC0759a
    public boolean d(MotionEvent motionEvent) {
        if (Y.a.w(2)) {
            Y.a.A(f10336y, "controller %x %s: onTouchEvent %s", Integer.valueOf(System.identityHashCode(this)), this.f10346j, motionEvent);
        }
        C0755a c0755a = this.f10341e;
        if (c0755a == null) {
            return false;
        }
        if (!c0755a.b() && !f0()) {
            return false;
        }
        this.f10341e.d(motionEvent);
        return true;
    }

    protected void d0(boolean z3) {
        this.f10357u = z3;
    }

    @Override // w0.InterfaceC0759a
    public void e() {
        if (V0.b.d()) {
            V0.b.a("AbstractDraweeController#onAttach");
        }
        if (Y.a.w(2)) {
            Y.a.A(f10336y, "controller %x %s: onAttach: %s", Integer.valueOf(System.identityHashCode(this)), this.f10346j, this.f10349m ? "request already submitted" : "request needs submit");
        }
        this.f10337a.b(c.a.ON_ATTACH_CONTROLLER);
        k.g(this.f10344h);
        this.f10338b.a(this);
        this.f10348l = true;
        if (!this.f10349m) {
            h0();
        }
        if (V0.b.d()) {
            V0.b.b();
        }
    }

    protected void e0(boolean z3) {
        this.f10352p = z3;
    }

    @Override // v0.C0755a.InterfaceC0149a
    public boolean f() {
        if (Y.a.w(2)) {
            Y.a.z(f10336y, "controller %x %s: onClick", Integer.valueOf(System.identityHashCode(this)), this.f10346j);
        }
        if (!g0()) {
            return false;
        }
        this.f10340d.b();
        this.f10344h.h();
        h0();
        return true;
    }

    protected boolean f0() {
        return g0();
    }

    protected void h0() {
        if (V0.b.d()) {
            V0.b.a("AbstractDraweeController#submitRequest");
        }
        Object objN = n();
        if (objN != null) {
            if (V0.b.d()) {
                V0.b.a("AbstractDraweeController#submitRequest->cache");
            }
            this.f10354r = null;
            this.f10349m = true;
            this.f10351o = false;
            this.f10337a.b(c.a.ON_SUBMIT_CACHE_HIT);
            X(this.f10354r, y(objN));
            M(this.f10346j, objN);
            N(this.f10346j, this.f10354r, objN, 1.0f, true, true, true);
            if (V0.b.d()) {
                V0.b.b();
            }
            if (V0.b.d()) {
                V0.b.b();
                return;
            }
            return;
        }
        this.f10337a.b(c.a.ON_DATASOURCE_SUBMIT);
        this.f10344h.a(0.0f, true);
        this.f10349m = true;
        this.f10351o = false;
        InterfaceC0548c interfaceC0548cS = s();
        this.f10354r = interfaceC0548cS;
        X(interfaceC0548cS, null);
        if (Y.a.w(2)) {
            Y.a.A(f10336y, "controller %x %s: submitRequest: dataSource: %x", Integer.valueOf(System.identityHashCode(this)), this.f10346j, Integer.valueOf(System.identityHashCode(this.f10354r)));
        }
        this.f10354r.h(new C0141a(this.f10346j, this.f10354r.d()), this.f10339c);
        if (V0.b.d()) {
            V0.b.b();
        }
    }

    public void j(InterfaceC0648d interfaceC0648d) {
        k.g(interfaceC0648d);
        InterfaceC0648d interfaceC0648d2 = this.f10342f;
        if (interfaceC0648d2 instanceof b) {
            ((b) interfaceC0648d2).c(interfaceC0648d);
        } else if (interfaceC0648d2 != null) {
            this.f10342f = b.f(interfaceC0648d2, interfaceC0648d);
        } else {
            this.f10342f = interfaceC0648d;
        }
    }

    public void k(InterfaceC0783b interfaceC0783b) {
        this.f10343g.A(interfaceC0783b);
    }

    protected abstract Drawable l(Object obj);

    public Animatable m() {
        Object obj = this.f10358v;
        if (obj instanceof Animatable) {
            return (Animatable) obj;
        }
        return null;
    }

    protected Object n() {
        return null;
    }

    public Object o() {
        return this.f10347k;
    }

    protected InterfaceC0648d p() {
        InterfaceC0648d interfaceC0648d = this.f10342f;
        return interfaceC0648d == null ? C0647c.c() : interfaceC0648d;
    }

    protected InterfaceC0783b q() {
        return this.f10343g;
    }

    protected Drawable r() {
        return this.f10345i;
    }

    @Override // p0.AbstractC0635a.InterfaceC0140a
    public void release() {
        this.f10337a.b(c.a.ON_RELEASE_CONTROLLER);
        p0.d dVar = this.f10340d;
        if (dVar != null) {
            dVar.c();
        }
        C0755a c0755a = this.f10341e;
        if (c0755a != null) {
            c0755a.e();
        }
        InterfaceC0761c interfaceC0761c = this.f10344h;
        if (interfaceC0761c != null) {
            interfaceC0761c.h();
        }
        Q();
    }

    protected abstract InterfaceC0548c s();

    public String toString() {
        return i.b(this).c("isAttached", this.f10348l).c("isRequestSubmitted", this.f10349m).c("hasFetchFailed", this.f10351o).a("fetchedImage", x(this.f10355s)).b("events", this.f10337a.toString()).toString();
    }

    protected C0755a u() {
        return this.f10341e;
    }

    public String v() {
        return this.f10346j;
    }

    protected String w(Object obj) {
        return obj != null ? obj.getClass().getSimpleName() : "<null>";
    }

    protected int x(Object obj) {
        return System.identityHashCode(obj);
    }

    protected abstract Object y(Object obj);

    protected Uri z() {
        return null;
    }

    public void b0(InterfaceC0649e interfaceC0649e) {
    }

    protected void M(String str, Object obj) {
    }
}

package androidx.fragment.app;

import android.animation.Animator;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.core.view.AbstractC0277y;
import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.E;
import androidx.lifecycle.InterfaceC0298f;
import androidx.lifecycle.InterfaceC0302j;
import androidx.lifecycle.LiveData;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class Fragment implements ComponentCallbacks, View.OnCreateContextMenuListener, androidx.lifecycle.l, androidx.lifecycle.H, InterfaceC0298f, G.d {

    /* JADX INFO: renamed from: c0, reason: collision with root package name */
    static final Object f4910c0 = new Object();

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    String f4911A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    boolean f4912B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    boolean f4913C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    boolean f4914D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    boolean f4915E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    boolean f4916F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    boolean f4917G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private boolean f4918H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    ViewGroup f4919I;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    View f4920J;

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    boolean f4921K;

    /* JADX INFO: renamed from: L, reason: collision with root package name */
    boolean f4922L;

    /* JADX INFO: renamed from: M, reason: collision with root package name */
    f f4923M;

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    Runnable f4924N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    boolean f4925O;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    LayoutInflater f4926P;

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    boolean f4927Q;

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    public String f4928R;

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    AbstractC0299g.b f4929S;

    /* JADX INFO: renamed from: T, reason: collision with root package name */
    androidx.lifecycle.m f4930T;

    /* JADX INFO: renamed from: U, reason: collision with root package name */
    J f4931U;

    /* JADX INFO: renamed from: V, reason: collision with root package name */
    androidx.lifecycle.p f4932V;

    /* JADX INFO: renamed from: W, reason: collision with root package name */
    E.b f4933W;

    /* JADX INFO: renamed from: X, reason: collision with root package name */
    G.c f4934X;

    /* JADX INFO: renamed from: Y, reason: collision with root package name */
    private int f4935Y;

    /* JADX INFO: renamed from: Z, reason: collision with root package name */
    private final AtomicInteger f4936Z;

    /* JADX INFO: renamed from: a0, reason: collision with root package name */
    private final ArrayList f4937a0;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    int f4938b;

    /* JADX INFO: renamed from: b0, reason: collision with root package name */
    private final i f4939b0;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    Bundle f4940c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    SparseArray f4941d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    Bundle f4942e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    Boolean f4943f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    String f4944g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    Bundle f4945h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    Fragment f4946i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    String f4947j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    int f4948k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private Boolean f4949l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    boolean f4950m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    boolean f4951n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    boolean f4952o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    boolean f4953p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    boolean f4954q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    boolean f4955r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    boolean f4956s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    int f4957t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    x f4958u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    p f4959v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    x f4960w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    Fragment f4961x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    int f4962y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    int f4963z;

    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Fragment.this.y1();
        }
    }

    class b extends i {
        b() {
            super(null);
        }

        @Override // androidx.fragment.app.Fragment.i
        void a() {
            Fragment.this.f4934X.c();
            androidx.lifecycle.y.c(Fragment.this);
        }
    }

    class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Fragment.this.c(false);
        }
    }

    class d implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ L f4968b;

        d(L l3) {
            this.f4968b = l3;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f4968b.g();
        }
    }

    class e extends AbstractC0290l {
        e() {
        }

        @Override // androidx.fragment.app.AbstractC0290l
        public View f(int i3) {
            View view = Fragment.this.f4920J;
            if (view != null) {
                return view.findViewById(i3);
            }
            throw new IllegalStateException("Fragment " + Fragment.this + " does not have a view");
        }

        @Override // androidx.fragment.app.AbstractC0290l
        public boolean h() {
            return Fragment.this.f4920J != null;
        }
    }

    static class f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        View f4971a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        boolean f4972b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        int f4973c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        int f4974d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        int f4975e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        int f4976f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        int f4977g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        ArrayList f4978h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        ArrayList f4979i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        Object f4980j = null;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        Object f4981k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        Object f4982l;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        Object f4983m;

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        Object f4984n;

        /* JADX INFO: renamed from: o, reason: collision with root package name */
        Object f4985o;

        /* JADX INFO: renamed from: p, reason: collision with root package name */
        Boolean f4986p;

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        Boolean f4987q;

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        androidx.core.app.m f4988r;

        /* JADX INFO: renamed from: s, reason: collision with root package name */
        androidx.core.app.m f4989s;

        /* JADX INFO: renamed from: t, reason: collision with root package name */
        float f4990t;

        /* JADX INFO: renamed from: u, reason: collision with root package name */
        View f4991u;

        /* JADX INFO: renamed from: v, reason: collision with root package name */
        boolean f4992v;

        f() {
            Object obj = Fragment.f4910c0;
            this.f4981k = obj;
            this.f4982l = null;
            this.f4983m = obj;
            this.f4984n = null;
            this.f4985o = obj;
            this.f4988r = null;
            this.f4989s = null;
            this.f4990t = 1.0f;
            this.f4991u = null;
        }
    }

    static class g {
        static void a(View view) {
            view.cancelPendingInputEvents();
        }
    }

    public static class h extends RuntimeException {
        public h(String str, Exception exc) {
            super(str, exc);
        }
    }

    private static abstract class i {
        private i() {
        }

        abstract void a();

        /* synthetic */ i(a aVar) {
            this();
        }
    }

    public Fragment() {
        this.f4938b = -1;
        this.f4944g = UUID.randomUUID().toString();
        this.f4947j = null;
        this.f4949l = null;
        this.f4960w = new y();
        this.f4917G = true;
        this.f4922L = true;
        this.f4924N = new a();
        this.f4929S = AbstractC0299g.b.RESUMED;
        this.f4932V = new androidx.lifecycle.p();
        this.f4936Z = new AtomicInteger();
        this.f4937a0 = new ArrayList();
        this.f4939b0 = new b();
        T();
    }

    private int B() {
        AbstractC0299g.b bVar = this.f4929S;
        return (bVar == AbstractC0299g.b.INITIALIZED || this.f4961x == null) ? bVar.ordinal() : Math.min(bVar.ordinal(), this.f4961x.B());
    }

    private Fragment Q(boolean z3) {
        String str;
        if (z3) {
            C.c.h(this);
        }
        Fragment fragment = this.f4946i;
        if (fragment != null) {
            return fragment;
        }
        x xVar = this.f4958u;
        if (xVar == null || (str = this.f4947j) == null) {
            return null;
        }
        return xVar.e0(str);
    }

    private void T() {
        this.f4930T = new androidx.lifecycle.m(this);
        this.f4934X = G.c.a(this);
        this.f4933W = null;
        if (this.f4937a0.contains(this.f4939b0)) {
            return;
        }
        j1(this.f4939b0);
    }

    public static Fragment V(Context context, String str, Bundle bundle) {
        try {
            Fragment fragment = (Fragment) o.d(context.getClassLoader(), str).getConstructor(new Class[0]).newInstance(new Object[0]);
            if (bundle != null) {
                bundle.setClassLoader(fragment.getClass().getClassLoader());
                fragment.s1(bundle);
            }
            return fragment;
        } catch (IllegalAccessException e4) {
            throw new h("Unable to instantiate fragment " + str + ": make sure class name exists, is public, and has an empty constructor that is public", e4);
        } catch (InstantiationException e5) {
            throw new h("Unable to instantiate fragment " + str + ": make sure class name exists, is public, and has an empty constructor that is public", e5);
        } catch (NoSuchMethodException e6) {
            throw new h("Unable to instantiate fragment " + str + ": could not find Fragment constructor", e6);
        } catch (InvocationTargetException e7) {
            throw new h("Unable to instantiate fragment " + str + ": calling Fragment constructor caused an exception", e7);
        }
    }

    private f f() {
        if (this.f4923M == null) {
            this.f4923M = new f();
        }
        return this.f4923M;
    }

    private void j1(i iVar) {
        if (this.f4938b >= 0) {
            iVar.a();
        } else {
            this.f4937a0.add(iVar);
        }
    }

    private void p1() {
        if (x.G0(3)) {
            Log.d("FragmentManager", "moveto RESTORE_VIEW_STATE: " + this);
        }
        if (this.f4920J != null) {
            q1(this.f4940c);
        }
        this.f4940c = null;
    }

    public LayoutInflater A(Bundle bundle) {
        p pVar = this.f4959v;
        if (pVar == null) {
            throw new IllegalStateException("onGetLayoutInflater() cannot be executed until the Fragment is attached to the FragmentManager.");
        }
        LayoutInflater layoutInflaterY = pVar.y();
        AbstractC0277y.a(layoutInflaterY, this.f4960w.u0());
        return layoutInflaterY;
    }

    public void A0() {
        this.f4918H = true;
    }

    public void B0(boolean z3) {
    }

    int C() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return 0;
        }
        return fVar.f4977g;
    }

    public void C0(Menu menu) {
    }

    public final Fragment D() {
        return this.f4961x;
    }

    public void D0(boolean z3) {
    }

    public final x E() {
        x xVar = this.f4958u;
        if (xVar != null) {
            return xVar;
        }
        throw new IllegalStateException("Fragment " + this + " not associated with a fragment manager.");
    }

    public void E0(int i3, String[] strArr, int[] iArr) {
    }

    boolean F() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return false;
        }
        return fVar.f4972b;
    }

    public void F0() {
        this.f4918H = true;
    }

    int G() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return 0;
        }
        return fVar.f4975e;
    }

    public void G0(Bundle bundle) {
    }

    int H() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return 0;
        }
        return fVar.f4976f;
    }

    public void H0() {
        this.f4918H = true;
    }

    float I() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return 1.0f;
        }
        return fVar.f4990t;
    }

    public void I0() {
        this.f4918H = true;
    }

    public Object J() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return null;
        }
        Object obj = fVar.f4983m;
        return obj == f4910c0 ? w() : obj;
    }

    public void J0(View view, Bundle bundle) {
    }

    public final Resources K() {
        return m1().getResources();
    }

    public void K0(Bundle bundle) {
        this.f4918H = true;
    }

    public Object L() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return null;
        }
        Object obj = fVar.f4981k;
        return obj == f4910c0 ? r() : obj;
    }

    void L0(Bundle bundle) {
        this.f4960w.U0();
        this.f4938b = 3;
        this.f4918H = false;
        e0(bundle);
        if (this.f4918H) {
            p1();
            this.f4960w.x();
        } else {
            throw new N("Fragment " + this + " did not call through to super.onActivityCreated()");
        }
    }

    public Object M() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return null;
        }
        return fVar.f4984n;
    }

    void M0() {
        Iterator it = this.f4937a0.iterator();
        while (it.hasNext()) {
            ((i) it.next()).a();
        }
        this.f4937a0.clear();
        this.f4960w.m(this.f4959v, d(), this);
        this.f4938b = 0;
        this.f4918H = false;
        h0(this.f4959v.k());
        if (this.f4918H) {
            this.f4958u.H(this);
            this.f4960w.y();
        } else {
            throw new N("Fragment " + this + " did not call through to super.onAttach()");
        }
    }

    public Object N() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return null;
        }
        Object obj = fVar.f4985o;
        return obj == f4910c0 ? M() : obj;
    }

    void N0(Configuration configuration) {
        onConfigurationChanged(configuration);
    }

    ArrayList O() {
        ArrayList arrayList;
        f fVar = this.f4923M;
        return (fVar == null || (arrayList = fVar.f4978h) == null) ? new ArrayList() : arrayList;
    }

    boolean O0(MenuItem menuItem) {
        if (this.f4912B) {
            return false;
        }
        if (j0(menuItem)) {
            return true;
        }
        return this.f4960w.A(menuItem);
    }

    ArrayList P() {
        ArrayList arrayList;
        f fVar = this.f4923M;
        return (fVar == null || (arrayList = fVar.f4979i) == null) ? new ArrayList() : arrayList;
    }

    void P0(Bundle bundle) {
        this.f4960w.U0();
        this.f4938b = 1;
        this.f4918H = false;
        this.f4930T.a(new InterfaceC0302j() { // from class: androidx.fragment.app.Fragment.6
            @Override // androidx.lifecycle.InterfaceC0302j
            public void d(androidx.lifecycle.l lVar, AbstractC0299g.a aVar) {
                View view;
                if (aVar != AbstractC0299g.a.ON_STOP || (view = Fragment.this.f4920J) == null) {
                    return;
                }
                g.a(view);
            }
        });
        this.f4934X.d(bundle);
        k0(bundle);
        this.f4927Q = true;
        if (this.f4918H) {
            this.f4930T.h(AbstractC0299g.a.ON_CREATE);
            return;
        }
        throw new N("Fragment " + this + " did not call through to super.onCreate()");
    }

    boolean Q0(Menu menu, MenuInflater menuInflater) {
        boolean z3 = false;
        if (this.f4912B) {
            return false;
        }
        if (this.f4916F && this.f4917G) {
            n0(menu, menuInflater);
            z3 = true;
        }
        return z3 | this.f4960w.C(menu, menuInflater);
    }

    public View R() {
        return this.f4920J;
    }

    void R0(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.f4960w.U0();
        this.f4956s = true;
        this.f4931U = new J(this, s());
        View viewO0 = o0(layoutInflater, viewGroup, bundle);
        this.f4920J = viewO0;
        if (viewO0 == null) {
            if (this.f4931U.e()) {
                throw new IllegalStateException("Called getViewLifecycleOwner() but onCreateView() returned null");
            }
            this.f4931U = null;
        } else {
            this.f4931U.d();
            androidx.lifecycle.I.a(this.f4920J, this.f4931U);
            androidx.lifecycle.J.a(this.f4920J, this.f4931U);
            G.e.a(this.f4920J, this.f4931U);
            this.f4932V.i(this.f4931U);
        }
    }

    public LiveData S() {
        return this.f4932V;
    }

    void S0() {
        this.f4960w.D();
        this.f4930T.h(AbstractC0299g.a.ON_DESTROY);
        this.f4938b = 0;
        this.f4918H = false;
        this.f4927Q = false;
        p0();
        if (this.f4918H) {
            return;
        }
        throw new N("Fragment " + this + " did not call through to super.onDestroy()");
    }

    void T0() {
        this.f4960w.E();
        if (this.f4920J != null && this.f4931U.t().b().b(AbstractC0299g.b.CREATED)) {
            this.f4931U.c(AbstractC0299g.a.ON_DESTROY);
        }
        this.f4938b = 1;
        this.f4918H = false;
        r0();
        if (this.f4918H) {
            androidx.loader.app.a.b(this).c();
            this.f4956s = false;
        } else {
            throw new N("Fragment " + this + " did not call through to super.onDestroyView()");
        }
    }

    void U() {
        T();
        this.f4928R = this.f4944g;
        this.f4944g = UUID.randomUUID().toString();
        this.f4950m = false;
        this.f4951n = false;
        this.f4953p = false;
        this.f4954q = false;
        this.f4955r = false;
        this.f4957t = 0;
        this.f4958u = null;
        this.f4960w = new y();
        this.f4959v = null;
        this.f4962y = 0;
        this.f4963z = 0;
        this.f4911A = null;
        this.f4912B = false;
        this.f4913C = false;
    }

    void U0() {
        this.f4938b = -1;
        this.f4918H = false;
        s0();
        this.f4926P = null;
        if (this.f4918H) {
            if (this.f4960w.F0()) {
                return;
            }
            this.f4960w.D();
            this.f4960w = new y();
            return;
        }
        throw new N("Fragment " + this + " did not call through to super.onDetach()");
    }

    LayoutInflater V0(Bundle bundle) {
        LayoutInflater layoutInflaterT0 = t0(bundle);
        this.f4926P = layoutInflaterT0;
        return layoutInflaterT0;
    }

    public final boolean W() {
        return this.f4959v != null && this.f4950m;
    }

    void W0() {
        onLowMemory();
    }

    public final boolean X() {
        x xVar;
        return this.f4912B || ((xVar = this.f4958u) != null && xVar.J0(this.f4961x));
    }

    void X0(boolean z3) {
        x0(z3);
    }

    final boolean Y() {
        return this.f4957t > 0;
    }

    boolean Y0(MenuItem menuItem) {
        if (this.f4912B) {
            return false;
        }
        if (this.f4916F && this.f4917G && y0(menuItem)) {
            return true;
        }
        return this.f4960w.J(menuItem);
    }

    public final boolean Z() {
        x xVar;
        return this.f4917G && ((xVar = this.f4958u) == null || xVar.K0(this.f4961x));
    }

    void Z0(Menu menu) {
        if (this.f4912B) {
            return;
        }
        if (this.f4916F && this.f4917G) {
            z0(menu);
        }
        this.f4960w.K(menu);
    }

    boolean a0() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return false;
        }
        return fVar.f4992v;
    }

    void a1() {
        this.f4960w.M();
        if (this.f4920J != null) {
            this.f4931U.c(AbstractC0299g.a.ON_PAUSE);
        }
        this.f4930T.h(AbstractC0299g.a.ON_PAUSE);
        this.f4938b = 6;
        this.f4918H = false;
        A0();
        if (this.f4918H) {
            return;
        }
        throw new N("Fragment " + this + " did not call through to super.onPause()");
    }

    @Override // G.d
    public final androidx.savedstate.a b() {
        return this.f4934X.b();
    }

    public final boolean b0() {
        return this.f4938b >= 7;
    }

    void b1(boolean z3) {
        B0(z3);
    }

    void c(boolean z3) {
        ViewGroup viewGroup;
        x xVar;
        f fVar = this.f4923M;
        if (fVar != null) {
            fVar.f4992v = false;
        }
        if (this.f4920J == null || (viewGroup = this.f4919I) == null || (xVar = this.f4958u) == null) {
            return;
        }
        L lN = L.n(viewGroup, xVar);
        lN.p();
        if (z3) {
            this.f4959v.m().post(new d(lN));
        } else {
            lN.g();
        }
    }

    public final boolean c0() {
        x xVar = this.f4958u;
        if (xVar == null) {
            return false;
        }
        return xVar.N0();
    }

    boolean c1(Menu menu) {
        boolean z3 = false;
        if (this.f4912B) {
            return false;
        }
        if (this.f4916F && this.f4917G) {
            C0(menu);
            z3 = true;
        }
        return z3 | this.f4960w.O(menu);
    }

    AbstractC0290l d() {
        return new e();
    }

    void d0() {
        this.f4960w.U0();
    }

    void d1() {
        boolean zL0 = this.f4958u.L0(this);
        Boolean bool = this.f4949l;
        if (bool == null || bool.booleanValue() != zL0) {
            this.f4949l = Boolean.valueOf(zL0);
            D0(zL0);
            this.f4960w.P();
        }
    }

    public void e(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print(str);
        printWriter.print("mFragmentId=#");
        printWriter.print(Integer.toHexString(this.f4962y));
        printWriter.print(" mContainerId=#");
        printWriter.print(Integer.toHexString(this.f4963z));
        printWriter.print(" mTag=");
        printWriter.println(this.f4911A);
        printWriter.print(str);
        printWriter.print("mState=");
        printWriter.print(this.f4938b);
        printWriter.print(" mWho=");
        printWriter.print(this.f4944g);
        printWriter.print(" mBackStackNesting=");
        printWriter.println(this.f4957t);
        printWriter.print(str);
        printWriter.print("mAdded=");
        printWriter.print(this.f4950m);
        printWriter.print(" mRemoving=");
        printWriter.print(this.f4951n);
        printWriter.print(" mFromLayout=");
        printWriter.print(this.f4953p);
        printWriter.print(" mInLayout=");
        printWriter.println(this.f4954q);
        printWriter.print(str);
        printWriter.print("mHidden=");
        printWriter.print(this.f4912B);
        printWriter.print(" mDetached=");
        printWriter.print(this.f4913C);
        printWriter.print(" mMenuVisible=");
        printWriter.print(this.f4917G);
        printWriter.print(" mHasMenu=");
        printWriter.println(this.f4916F);
        printWriter.print(str);
        printWriter.print("mRetainInstance=");
        printWriter.print(this.f4914D);
        printWriter.print(" mUserVisibleHint=");
        printWriter.println(this.f4922L);
        if (this.f4958u != null) {
            printWriter.print(str);
            printWriter.print("mFragmentManager=");
            printWriter.println(this.f4958u);
        }
        if (this.f4959v != null) {
            printWriter.print(str);
            printWriter.print("mHost=");
            printWriter.println(this.f4959v);
        }
        if (this.f4961x != null) {
            printWriter.print(str);
            printWriter.print("mParentFragment=");
            printWriter.println(this.f4961x);
        }
        if (this.f4945h != null) {
            printWriter.print(str);
            printWriter.print("mArguments=");
            printWriter.println(this.f4945h);
        }
        if (this.f4940c != null) {
            printWriter.print(str);
            printWriter.print("mSavedFragmentState=");
            printWriter.println(this.f4940c);
        }
        if (this.f4941d != null) {
            printWriter.print(str);
            printWriter.print("mSavedViewState=");
            printWriter.println(this.f4941d);
        }
        if (this.f4942e != null) {
            printWriter.print(str);
            printWriter.print("mSavedViewRegistryState=");
            printWriter.println(this.f4942e);
        }
        Fragment fragmentQ = Q(false);
        if (fragmentQ != null) {
            printWriter.print(str);
            printWriter.print("mTarget=");
            printWriter.print(fragmentQ);
            printWriter.print(" mTargetRequestCode=");
            printWriter.println(this.f4948k);
        }
        printWriter.print(str);
        printWriter.print("mPopDirection=");
        printWriter.println(F());
        if (q() != 0) {
            printWriter.print(str);
            printWriter.print("getEnterAnim=");
            printWriter.println(q());
        }
        if (v() != 0) {
            printWriter.print(str);
            printWriter.print("getExitAnim=");
            printWriter.println(v());
        }
        if (G() != 0) {
            printWriter.print(str);
            printWriter.print("getPopEnterAnim=");
            printWriter.println(G());
        }
        if (H() != 0) {
            printWriter.print(str);
            printWriter.print("getPopExitAnim=");
            printWriter.println(H());
        }
        if (this.f4919I != null) {
            printWriter.print(str);
            printWriter.print("mContainer=");
            printWriter.println(this.f4919I);
        }
        if (this.f4920J != null) {
            printWriter.print(str);
            printWriter.print("mView=");
            printWriter.println(this.f4920J);
        }
        if (m() != null) {
            printWriter.print(str);
            printWriter.print("mAnimatingAway=");
            printWriter.println(m());
        }
        if (p() != null) {
            androidx.loader.app.a.b(this).a(str, fileDescriptor, printWriter, strArr);
        }
        printWriter.print(str);
        printWriter.println("Child " + this.f4960w + ":");
        this.f4960w.W(str + "  ", fileDescriptor, printWriter, strArr);
    }

    public void e0(Bundle bundle) {
        this.f4918H = true;
    }

    void e1() {
        this.f4960w.U0();
        this.f4960w.a0(true);
        this.f4938b = 7;
        this.f4918H = false;
        F0();
        if (!this.f4918H) {
            throw new N("Fragment " + this + " did not call through to super.onResume()");
        }
        androidx.lifecycle.m mVar = this.f4930T;
        AbstractC0299g.a aVar = AbstractC0299g.a.ON_RESUME;
        mVar.h(aVar);
        if (this.f4920J != null) {
            this.f4931U.c(aVar);
        }
        this.f4960w.Q();
    }

    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void f0(int i3, int i4, Intent intent) {
        if (x.G0(2)) {
            Log.v("FragmentManager", "Fragment " + this + " received the following in onActivityResult(): requestCode: " + i3 + " resultCode: " + i4 + " data: " + intent);
        }
    }

    void f1(Bundle bundle) {
        G0(bundle);
        this.f4934X.e(bundle);
        Bundle bundleH1 = this.f4960w.O0();
        if (bundleH1 != null) {
            bundle.putParcelable("android:support:fragments", bundleH1);
        }
    }

    Fragment g(String str) {
        return str.equals(this.f4944g) ? this : this.f4960w.i0(str);
    }

    public void g0(Activity activity) {
        this.f4918H = true;
    }

    void g1() {
        this.f4960w.U0();
        this.f4960w.a0(true);
        this.f4938b = 5;
        this.f4918H = false;
        H0();
        if (!this.f4918H) {
            throw new N("Fragment " + this + " did not call through to super.onStart()");
        }
        androidx.lifecycle.m mVar = this.f4930T;
        AbstractC0299g.a aVar = AbstractC0299g.a.ON_START;
        mVar.h(aVar);
        if (this.f4920J != null) {
            this.f4931U.c(aVar);
        }
        this.f4960w.R();
    }

    public final ActivityC0288j h() {
        p pVar = this.f4959v;
        if (pVar == null) {
            return null;
        }
        return (ActivityC0288j) pVar.j();
    }

    public void h0(Context context) {
        this.f4918H = true;
        p pVar = this.f4959v;
        Activity activityJ = pVar == null ? null : pVar.j();
        if (activityJ != null) {
            this.f4918H = false;
            g0(activityJ);
        }
    }

    void h1() {
        this.f4960w.T();
        if (this.f4920J != null) {
            this.f4931U.c(AbstractC0299g.a.ON_STOP);
        }
        this.f4930T.h(AbstractC0299g.a.ON_STOP);
        this.f4938b = 4;
        this.f4918H = false;
        I0();
        if (this.f4918H) {
            return;
        }
        throw new N("Fragment " + this + " did not call through to super.onStop()");
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public boolean i() {
        Boolean bool;
        f fVar = this.f4923M;
        if (fVar == null || (bool = fVar.f4987q) == null) {
            return true;
        }
        return bool.booleanValue();
    }

    public void i0(Fragment fragment) {
    }

    void i1() {
        J0(this.f4920J, this.f4940c);
        this.f4960w.U();
    }

    @Override // androidx.lifecycle.InterfaceC0298f
    public E.b j() {
        Application application;
        if (this.f4958u == null) {
            throw new IllegalStateException("Can't access ViewModels from detached fragment");
        }
        if (this.f4933W == null) {
            Context applicationContext = m1().getApplicationContext();
            while (true) {
                if (!(applicationContext instanceof ContextWrapper)) {
                    application = null;
                    break;
                }
                if (applicationContext instanceof Application) {
                    application = (Application) applicationContext;
                    break;
                }
                applicationContext = ((ContextWrapper) applicationContext).getBaseContext();
            }
            if (application == null && x.G0(3)) {
                Log.d("FragmentManager", "Could not find Application instance from Context " + m1().getApplicationContext() + ", you will need CreationExtras to use AndroidViewModel with the default ViewModelProvider.Factory");
            }
            this.f4933W = new androidx.lifecycle.B(application, this, n());
        }
        return this.f4933W;
    }

    public boolean j0(MenuItem menuItem) {
        return false;
    }

    @Override // androidx.lifecycle.InterfaceC0298f
    public F.a k() {
        Application application;
        Context applicationContext = m1().getApplicationContext();
        while (true) {
            if (!(applicationContext instanceof ContextWrapper)) {
                application = null;
                break;
            }
            if (applicationContext instanceof Application) {
                application = (Application) applicationContext;
                break;
            }
            applicationContext = ((ContextWrapper) applicationContext).getBaseContext();
        }
        if (application == null && x.G0(3)) {
            Log.d("FragmentManager", "Could not find Application instance from Context " + m1().getApplicationContext() + ", you will not be able to use AndroidViewModel with the default ViewModelProvider.Factory");
        }
        F.d dVar = new F.d();
        if (application != null) {
            dVar.c(E.a.f5272h, application);
        }
        dVar.c(androidx.lifecycle.y.f5368a, this);
        dVar.c(androidx.lifecycle.y.f5369b, this);
        if (n() != null) {
            dVar.c(androidx.lifecycle.y.f5370c, n());
        }
        return dVar;
    }

    public void k0(Bundle bundle) {
        this.f4918H = true;
        o1(bundle);
        if (this.f4960w.M0(1)) {
            return;
        }
        this.f4960w.B();
    }

    public final ActivityC0288j k1() {
        ActivityC0288j activityC0288jH = h();
        if (activityC0288jH != null) {
            return activityC0288jH;
        }
        throw new IllegalStateException("Fragment " + this + " not attached to an activity.");
    }

    public boolean l() {
        Boolean bool;
        f fVar = this.f4923M;
        if (fVar == null || (bool = fVar.f4986p) == null) {
            return true;
        }
        return bool.booleanValue();
    }

    public Animation l0(int i3, boolean z3, int i4) {
        return null;
    }

    public final Bundle l1() {
        Bundle bundleN = n();
        if (bundleN != null) {
            return bundleN;
        }
        throw new IllegalStateException("Fragment " + this + " does not have any arguments.");
    }

    View m() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return null;
        }
        return fVar.f4971a;
    }

    public Animator m0(int i3, boolean z3, int i4) {
        return null;
    }

    public final Context m1() {
        Context contextP = p();
        if (contextP != null) {
            return contextP;
        }
        throw new IllegalStateException("Fragment " + this + " not attached to a context.");
    }

    public final Bundle n() {
        return this.f4945h;
    }

    public void n0(Menu menu, MenuInflater menuInflater) {
    }

    public final View n1() {
        View viewR = R();
        if (viewR != null) {
            return viewR;
        }
        throw new IllegalStateException("Fragment " + this + " did not return a View from onCreateView() or this was called before onCreateView().");
    }

    public final x o() {
        if (this.f4959v != null) {
            return this.f4960w;
        }
        throw new IllegalStateException("Fragment " + this + " has not been attached yet.");
    }

    public View o0(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        int i3 = this.f4935Y;
        if (i3 != 0) {
            return layoutInflater.inflate(i3, viewGroup, false);
        }
        return null;
    }

    void o1(Bundle bundle) {
        Parcelable parcelable;
        if (bundle == null || (parcelable = bundle.getParcelable("android:support:fragments")) == null) {
            return;
        }
        this.f4960w.f1(parcelable);
        this.f4960w.B();
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        this.f4918H = true;
    }

    @Override // android.view.View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        k1().onCreateContextMenu(contextMenu, view, contextMenuInfo);
    }

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
        this.f4918H = true;
    }

    public Context p() {
        p pVar = this.f4959v;
        if (pVar == null) {
            return null;
        }
        return pVar.k();
    }

    public void p0() {
        this.f4918H = true;
    }

    int q() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return 0;
        }
        return fVar.f4973c;
    }

    public void q0() {
    }

    final void q1(Bundle bundle) {
        SparseArray<Parcelable> sparseArray = this.f4941d;
        if (sparseArray != null) {
            this.f4920J.restoreHierarchyState(sparseArray);
            this.f4941d = null;
        }
        if (this.f4920J != null) {
            this.f4931U.f(this.f4942e);
            this.f4942e = null;
        }
        this.f4918H = false;
        K0(bundle);
        if (this.f4918H) {
            if (this.f4920J != null) {
                this.f4931U.c(AbstractC0299g.a.ON_CREATE);
            }
        } else {
            throw new N("Fragment " + this + " did not call through to super.onViewStateRestored()");
        }
    }

    public Object r() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return null;
        }
        return fVar.f4980j;
    }

    public void r0() {
        this.f4918H = true;
    }

    void r1(int i3, int i4, int i5, int i6) {
        if (this.f4923M == null && i3 == 0 && i4 == 0 && i5 == 0 && i6 == 0) {
            return;
        }
        f().f4973c = i3;
        f().f4974d = i4;
        f().f4975e = i5;
        f().f4976f = i6;
    }

    @Override // androidx.lifecycle.H
    public androidx.lifecycle.G s() {
        if (this.f4958u == null) {
            throw new IllegalStateException("Can't access ViewModels from detached fragment");
        }
        if (B() != AbstractC0299g.b.INITIALIZED.ordinal()) {
            return this.f4958u.B0(this);
        }
        throw new IllegalStateException("Calling getViewModelStore() before a Fragment reaches onCreate() when using setMaxLifecycle(INITIALIZED) is not supported");
    }

    public void s0() {
        this.f4918H = true;
    }

    public void s1(Bundle bundle) {
        if (this.f4958u != null && c0()) {
            throw new IllegalStateException("Fragment already added and state has been saved");
        }
        this.f4945h = bundle;
    }

    @Override // androidx.lifecycle.l
    public AbstractC0299g t() {
        return this.f4930T;
    }

    public LayoutInflater t0(Bundle bundle) {
        return A(bundle);
    }

    void t1(View view) {
        f().f4991u = view;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append(getClass().getSimpleName());
        sb.append("{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append("}");
        sb.append(" (");
        sb.append(this.f4944g);
        if (this.f4962y != 0) {
            sb.append(" id=0x");
            sb.append(Integer.toHexString(this.f4962y));
        }
        if (this.f4911A != null) {
            sb.append(" tag=");
            sb.append(this.f4911A);
        }
        sb.append(")");
        return sb.toString();
    }

    androidx.core.app.m u() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return null;
        }
        return fVar.f4988r;
    }

    public void u0(boolean z3) {
    }

    void u1(int i3) {
        if (this.f4923M == null && i3 == 0) {
            return;
        }
        f();
        this.f4923M.f4977g = i3;
    }

    int v() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return 0;
        }
        return fVar.f4974d;
    }

    public void v0(Activity activity, AttributeSet attributeSet, Bundle bundle) {
        this.f4918H = true;
    }

    void v1(boolean z3) {
        if (this.f4923M == null) {
            return;
        }
        f().f4972b = z3;
    }

    public Object w() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return null;
        }
        return fVar.f4982l;
    }

    public void w0(Context context, AttributeSet attributeSet, Bundle bundle) {
        this.f4918H = true;
        p pVar = this.f4959v;
        Activity activityJ = pVar == null ? null : pVar.j();
        if (activityJ != null) {
            this.f4918H = false;
            v0(activityJ, attributeSet, bundle);
        }
    }

    void w1(float f3) {
        f().f4990t = f3;
    }

    androidx.core.app.m x() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return null;
        }
        return fVar.f4989s;
    }

    public void x0(boolean z3) {
    }

    void x1(ArrayList arrayList, ArrayList arrayList2) {
        f();
        f fVar = this.f4923M;
        fVar.f4978h = arrayList;
        fVar.f4979i = arrayList2;
    }

    View y() {
        f fVar = this.f4923M;
        if (fVar == null) {
            return null;
        }
        return fVar.f4991u;
    }

    public boolean y0(MenuItem menuItem) {
        return false;
    }

    public void y1() {
        if (this.f4923M == null || !f().f4992v) {
            return;
        }
        if (this.f4959v == null) {
            f().f4992v = false;
        } else if (Looper.myLooper() != this.f4959v.m().getLooper()) {
            this.f4959v.m().postAtFrontOfQueue(new c());
        } else {
            c(true);
        }
    }

    public final Object z() {
        p pVar = this.f4959v;
        if (pVar == null) {
            return null;
        }
        return pVar.w();
    }

    public void z0(Menu menu) {
    }

    public Fragment(int i3) {
        this();
        this.f4935Y = i3;
    }
}

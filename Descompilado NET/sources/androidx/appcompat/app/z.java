package androidx.appcompat.app;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.appcompat.view.b;
import androidx.appcompat.view.menu.e;
import androidx.appcompat.widget.ActionBarContainer;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.ActionBarOverlayLayout;
import androidx.appcompat.widget.J;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.a0;
import androidx.core.view.C0254i0;
import androidx.core.view.C0258k0;
import androidx.core.view.InterfaceC0256j0;
import androidx.core.view.InterfaceC0260l0;
import androidx.core.view.Z;
import d.AbstractC0487a;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class z extends androidx.appcompat.app.a implements ActionBarOverlayLayout.d {

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private static final Interpolator f3318E = new AccelerateInterpolator();

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private static final Interpolator f3319F = new DecelerateInterpolator();

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    boolean f3320A;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    Context f3324a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Context f3325b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Activity f3326c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    ActionBarOverlayLayout f3327d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    ActionBarContainer f3328e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    J f3329f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    ActionBarContextView f3330g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    View f3331h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    a0 f3332i;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f3335l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    d f3336m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    androidx.appcompat.view.b f3337n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    b.a f3338o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private boolean f3339p;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private boolean f3341r;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    boolean f3344u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    boolean f3345v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private boolean f3346w;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    androidx.appcompat.view.h f3348y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private boolean f3349z;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private ArrayList f3333j = new ArrayList();

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f3334k = -1;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private ArrayList f3340q = new ArrayList();

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private int f3342s = 0;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    boolean f3343t = true;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private boolean f3347x = true;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    final InterfaceC0256j0 f3321B = new a();

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    final InterfaceC0256j0 f3322C = new b();

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    final InterfaceC0260l0 f3323D = new c();

    class a extends C0258k0 {
        a() {
        }

        @Override // androidx.core.view.C0258k0, androidx.core.view.InterfaceC0256j0
        public void b(View view) {
            View view2;
            z zVar = z.this;
            if (zVar.f3343t && (view2 = zVar.f3331h) != null) {
                view2.setTranslationY(0.0f);
                z.this.f3328e.setTranslationY(0.0f);
            }
            z.this.f3328e.setVisibility(8);
            z.this.f3328e.setTransitioning(false);
            z zVar2 = z.this;
            zVar2.f3348y = null;
            zVar2.x();
            ActionBarOverlayLayout actionBarOverlayLayout = z.this.f3327d;
            if (actionBarOverlayLayout != null) {
                Z.U(actionBarOverlayLayout);
            }
        }
    }

    class b extends C0258k0 {
        b() {
        }

        @Override // androidx.core.view.C0258k0, androidx.core.view.InterfaceC0256j0
        public void b(View view) {
            z zVar = z.this;
            zVar.f3348y = null;
            zVar.f3328e.requestLayout();
        }
    }

    class c implements InterfaceC0260l0 {
        c() {
        }

        @Override // androidx.core.view.InterfaceC0260l0
        public void a(View view) {
            ((View) z.this.f3328e.getParent()).invalidate();
        }
    }

    public class d extends androidx.appcompat.view.b implements e.a {

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final Context f3353d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final androidx.appcompat.view.menu.e f3354e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private b.a f3355f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private WeakReference f3356g;

        public d(Context context, b.a aVar) {
            this.f3353d = context;
            this.f3355f = aVar;
            androidx.appcompat.view.menu.e eVarT = new androidx.appcompat.view.menu.e(context).T(1);
            this.f3354e = eVarT;
            eVarT.S(this);
        }

        @Override // androidx.appcompat.view.menu.e.a
        public boolean a(androidx.appcompat.view.menu.e eVar, MenuItem menuItem) {
            b.a aVar = this.f3355f;
            if (aVar != null) {
                return aVar.c(this, menuItem);
            }
            return false;
        }

        @Override // androidx.appcompat.view.menu.e.a
        public void b(androidx.appcompat.view.menu.e eVar) {
            if (this.f3355f == null) {
                return;
            }
            k();
            z.this.f3330g.l();
        }

        @Override // androidx.appcompat.view.b
        public void c() {
            z zVar = z.this;
            if (zVar.f3336m != this) {
                return;
            }
            if (z.w(zVar.f3344u, zVar.f3345v, false)) {
                this.f3355f.b(this);
            } else {
                z zVar2 = z.this;
                zVar2.f3337n = this;
                zVar2.f3338o = this.f3355f;
            }
            this.f3355f = null;
            z.this.v(false);
            z.this.f3330g.g();
            z zVar3 = z.this;
            zVar3.f3327d.setHideOnContentScrollEnabled(zVar3.f3320A);
            z.this.f3336m = null;
        }

        @Override // androidx.appcompat.view.b
        public View d() {
            WeakReference weakReference = this.f3356g;
            if (weakReference != null) {
                return (View) weakReference.get();
            }
            return null;
        }

        @Override // androidx.appcompat.view.b
        public Menu e() {
            return this.f3354e;
        }

        @Override // androidx.appcompat.view.b
        public MenuInflater f() {
            return new androidx.appcompat.view.g(this.f3353d);
        }

        @Override // androidx.appcompat.view.b
        public CharSequence g() {
            return z.this.f3330g.getSubtitle();
        }

        @Override // androidx.appcompat.view.b
        public CharSequence i() {
            return z.this.f3330g.getTitle();
        }

        @Override // androidx.appcompat.view.b
        public void k() {
            if (z.this.f3336m != this) {
                return;
            }
            this.f3354e.e0();
            try {
                this.f3355f.a(this, this.f3354e);
            } finally {
                this.f3354e.d0();
            }
        }

        @Override // androidx.appcompat.view.b
        public boolean l() {
            return z.this.f3330g.j();
        }

        @Override // androidx.appcompat.view.b
        public void m(View view) {
            z.this.f3330g.setCustomView(view);
            this.f3356g = new WeakReference(view);
        }

        @Override // androidx.appcompat.view.b
        public void n(int i3) {
            o(z.this.f3324a.getResources().getString(i3));
        }

        @Override // androidx.appcompat.view.b
        public void o(CharSequence charSequence) {
            z.this.f3330g.setSubtitle(charSequence);
        }

        @Override // androidx.appcompat.view.b
        public void q(int i3) {
            r(z.this.f3324a.getResources().getString(i3));
        }

        @Override // androidx.appcompat.view.b
        public void r(CharSequence charSequence) {
            z.this.f3330g.setTitle(charSequence);
        }

        @Override // androidx.appcompat.view.b
        public void s(boolean z3) {
            super.s(z3);
            z.this.f3330g.setTitleOptional(z3);
        }

        public boolean t() {
            this.f3354e.e0();
            try {
                return this.f3355f.d(this, this.f3354e);
            } finally {
                this.f3354e.d0();
            }
        }
    }

    public z(Activity activity, boolean z3) {
        this.f3326c = activity;
        View decorView = activity.getWindow().getDecorView();
        D(decorView);
        if (z3) {
            return;
        }
        this.f3331h = decorView.findViewById(R.id.content);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private J A(View view) {
        if (view instanceof J) {
            return (J) view;
        }
        if (view instanceof Toolbar) {
            return ((Toolbar) view).getWrapper();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Can't make a decor toolbar out of ");
        sb.append(view != 0 ? view.getClass().getSimpleName() : "null");
        throw new IllegalStateException(sb.toString());
    }

    private void C() {
        if (this.f3346w) {
            this.f3346w = false;
            ActionBarOverlayLayout actionBarOverlayLayout = this.f3327d;
            if (actionBarOverlayLayout != null) {
                actionBarOverlayLayout.setShowingForActionMode(false);
            }
            M(false);
        }
    }

    private void D(View view) {
        ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) view.findViewById(d.f.f8799p);
        this.f3327d = actionBarOverlayLayout;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setActionBarVisibilityCallback(this);
        }
        this.f3329f = A(view.findViewById(d.f.f8784a));
        this.f3330g = (ActionBarContextView) view.findViewById(d.f.f8789f);
        ActionBarContainer actionBarContainer = (ActionBarContainer) view.findViewById(d.f.f8786c);
        this.f3328e = actionBarContainer;
        J j3 = this.f3329f;
        if (j3 == null || this.f3330g == null || actionBarContainer == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used with a compatible window decor layout");
        }
        this.f3324a = j3.c();
        boolean z3 = (this.f3329f.o() & 4) != 0;
        if (z3) {
            this.f3335l = true;
        }
        androidx.appcompat.view.a aVarB = androidx.appcompat.view.a.b(this.f3324a);
        J(aVarB.a() || z3);
        H(aVarB.g());
        TypedArray typedArrayObtainStyledAttributes = this.f3324a.obtainStyledAttributes(null, d.j.f8952a, AbstractC0487a.f8675c, 0);
        if (typedArrayObtainStyledAttributes.getBoolean(d.j.f8997k, false)) {
            I(true);
        }
        int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(d.j.f8989i, 0);
        if (dimensionPixelSize != 0) {
            G(dimensionPixelSize);
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    private void H(boolean z3) {
        this.f3341r = z3;
        if (z3) {
            this.f3328e.setTabContainer(null);
            this.f3329f.k(this.f3332i);
        } else {
            this.f3329f.k(null);
            this.f3328e.setTabContainer(this.f3332i);
        }
        boolean z4 = B() == 2;
        a0 a0Var = this.f3332i;
        if (a0Var != null) {
            if (z4) {
                a0Var.setVisibility(0);
                ActionBarOverlayLayout actionBarOverlayLayout = this.f3327d;
                if (actionBarOverlayLayout != null) {
                    Z.U(actionBarOverlayLayout);
                }
            } else {
                a0Var.setVisibility(8);
            }
        }
        this.f3329f.u(!this.f3341r && z4);
        this.f3327d.setHasNonEmbeddedTabs(!this.f3341r && z4);
    }

    private boolean K() {
        return this.f3328e.isLaidOut();
    }

    private void L() {
        if (this.f3346w) {
            return;
        }
        this.f3346w = true;
        ActionBarOverlayLayout actionBarOverlayLayout = this.f3327d;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setShowingForActionMode(true);
        }
        M(false);
    }

    private void M(boolean z3) {
        if (w(this.f3344u, this.f3345v, this.f3346w)) {
            if (this.f3347x) {
                return;
            }
            this.f3347x = true;
            z(z3);
            return;
        }
        if (this.f3347x) {
            this.f3347x = false;
            y(z3);
        }
    }

    static boolean w(boolean z3, boolean z4, boolean z5) {
        if (z5) {
            return true;
        }
        return (z3 || z4) ? false : true;
    }

    public int B() {
        return this.f3329f.q();
    }

    public void E(boolean z3) {
        F(z3 ? 4 : 0, 4);
    }

    public void F(int i3, int i4) {
        int iO = this.f3329f.o();
        if ((i4 & 4) != 0) {
            this.f3335l = true;
        }
        this.f3329f.n((i3 & i4) | ((~i4) & iO));
    }

    public void G(float f3) {
        Z.e0(this.f3328e, f3);
    }

    public void I(boolean z3) {
        if (z3 && !this.f3327d.x()) {
            throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to enable hide on content scroll");
        }
        this.f3320A = z3;
        this.f3327d.setHideOnContentScrollEnabled(z3);
    }

    public void J(boolean z3) {
        this.f3329f.l(z3);
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void a() {
        if (this.f3345v) {
            this.f3345v = false;
            M(true);
        }
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void b() {
        androidx.appcompat.view.h hVar = this.f3348y;
        if (hVar != null) {
            hVar.a();
            this.f3348y = null;
        }
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void c(int i3) {
        this.f3342s = i3;
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void d() {
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void e(boolean z3) {
        this.f3343t = z3;
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void f() {
        if (this.f3345v) {
            return;
        }
        this.f3345v = true;
        M(true);
    }

    @Override // androidx.appcompat.app.a
    public boolean h() {
        J j3 = this.f3329f;
        if (j3 == null || !j3.m()) {
            return false;
        }
        this.f3329f.collapseActionView();
        return true;
    }

    @Override // androidx.appcompat.app.a
    public void i(boolean z3) {
        if (z3 == this.f3339p) {
            return;
        }
        this.f3339p = z3;
        if (this.f3340q.size() <= 0) {
            return;
        }
        androidx.activity.result.d.a(this.f3340q.get(0));
        throw null;
    }

    @Override // androidx.appcompat.app.a
    public int j() {
        return this.f3329f.o();
    }

    @Override // androidx.appcompat.app.a
    public Context k() {
        if (this.f3325b == null) {
            TypedValue typedValue = new TypedValue();
            this.f3324a.getTheme().resolveAttribute(AbstractC0487a.f8680h, typedValue, true);
            int i3 = typedValue.resourceId;
            if (i3 != 0) {
                this.f3325b = new ContextThemeWrapper(this.f3324a, i3);
            } else {
                this.f3325b = this.f3324a;
            }
        }
        return this.f3325b;
    }

    @Override // androidx.appcompat.app.a
    public void m(Configuration configuration) {
        H(androidx.appcompat.view.a.b(this.f3324a).g());
    }

    @Override // androidx.appcompat.app.a
    public boolean o(int i3, KeyEvent keyEvent) {
        Menu menuE;
        d dVar = this.f3336m;
        if (dVar == null || (menuE = dVar.e()) == null) {
            return false;
        }
        menuE.setQwertyMode(KeyCharacterMap.load(keyEvent != null ? keyEvent.getDeviceId() : -1).getKeyboardType() != 1);
        return menuE.performShortcut(i3, keyEvent, 0);
    }

    @Override // androidx.appcompat.app.a
    public void r(boolean z3) {
        if (this.f3335l) {
            return;
        }
        E(z3);
    }

    @Override // androidx.appcompat.app.a
    public void s(boolean z3) {
        androidx.appcompat.view.h hVar;
        this.f3349z = z3;
        if (z3 || (hVar = this.f3348y) == null) {
            return;
        }
        hVar.a();
    }

    @Override // androidx.appcompat.app.a
    public void t(CharSequence charSequence) {
        this.f3329f.setWindowTitle(charSequence);
    }

    @Override // androidx.appcompat.app.a
    public androidx.appcompat.view.b u(b.a aVar) {
        d dVar = this.f3336m;
        if (dVar != null) {
            dVar.c();
        }
        this.f3327d.setHideOnContentScrollEnabled(false);
        this.f3330g.k();
        d dVar2 = new d(this.f3330g.getContext(), aVar);
        if (!dVar2.t()) {
            return null;
        }
        this.f3336m = dVar2;
        dVar2.k();
        this.f3330g.h(dVar2);
        v(true);
        return dVar2;
    }

    public void v(boolean z3) {
        C0254i0 c0254i0R;
        C0254i0 c0254i0F;
        if (z3) {
            L();
        } else {
            C();
        }
        if (!K()) {
            if (z3) {
                this.f3329f.j(4);
                this.f3330g.setVisibility(0);
                return;
            } else {
                this.f3329f.j(0);
                this.f3330g.setVisibility(8);
                return;
            }
        }
        if (z3) {
            c0254i0F = this.f3329f.r(4, 100L);
            c0254i0R = this.f3330g.f(0, 200L);
        } else {
            c0254i0R = this.f3329f.r(0, 200L);
            c0254i0F = this.f3330g.f(8, 100L);
        }
        androidx.appcompat.view.h hVar = new androidx.appcompat.view.h();
        hVar.d(c0254i0F, c0254i0R);
        hVar.h();
    }

    void x() {
        b.a aVar = this.f3338o;
        if (aVar != null) {
            aVar.b(this.f3337n);
            this.f3337n = null;
            this.f3338o = null;
        }
    }

    public void y(boolean z3) {
        View view;
        androidx.appcompat.view.h hVar = this.f3348y;
        if (hVar != null) {
            hVar.a();
        }
        if (this.f3342s != 0 || (!this.f3349z && !z3)) {
            this.f3321B.b(null);
            return;
        }
        this.f3328e.setAlpha(1.0f);
        this.f3328e.setTransitioning(true);
        androidx.appcompat.view.h hVar2 = new androidx.appcompat.view.h();
        float f3 = -this.f3328e.getHeight();
        if (z3) {
            this.f3328e.getLocationInWindow(new int[]{0, 0});
            f3 -= r5[1];
        }
        C0254i0 c0254i0M = Z.c(this.f3328e).m(f3);
        c0254i0M.k(this.f3323D);
        hVar2.c(c0254i0M);
        if (this.f3343t && (view = this.f3331h) != null) {
            hVar2.c(Z.c(view).m(f3));
        }
        hVar2.f(f3318E);
        hVar2.e(250L);
        hVar2.g(this.f3321B);
        this.f3348y = hVar2;
        hVar2.h();
    }

    public void z(boolean z3) {
        View view;
        View view2;
        androidx.appcompat.view.h hVar = this.f3348y;
        if (hVar != null) {
            hVar.a();
        }
        this.f3328e.setVisibility(0);
        if (this.f3342s == 0 && (this.f3349z || z3)) {
            this.f3328e.setTranslationY(0.0f);
            float f3 = -this.f3328e.getHeight();
            if (z3) {
                this.f3328e.getLocationInWindow(new int[]{0, 0});
                f3 -= r5[1];
            }
            this.f3328e.setTranslationY(f3);
            androidx.appcompat.view.h hVar2 = new androidx.appcompat.view.h();
            C0254i0 c0254i0M = Z.c(this.f3328e).m(0.0f);
            c0254i0M.k(this.f3323D);
            hVar2.c(c0254i0M);
            if (this.f3343t && (view2 = this.f3331h) != null) {
                view2.setTranslationY(f3);
                hVar2.c(Z.c(this.f3331h).m(0.0f));
            }
            hVar2.f(f3319F);
            hVar2.e(250L);
            hVar2.g(this.f3322C);
            this.f3348y = hVar2;
            hVar2.h();
        } else {
            this.f3328e.setAlpha(1.0f);
            this.f3328e.setTranslationY(0.0f);
            if (this.f3343t && (view = this.f3331h) != null) {
                view.setTranslationY(0.0f);
            }
            this.f3322C.b(null);
        }
        ActionBarOverlayLayout actionBarOverlayLayout = this.f3327d;
        if (actionBarOverlayLayout != null) {
            Z.U(actionBarOverlayLayout);
        }
    }

    public z(Dialog dialog) {
        D(dialog.getWindow().getDecorView());
    }

    public z(View view) {
        D(view);
    }
}

package androidx.appcompat.widget;

import android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import androidx.appcompat.view.menu.j;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.C0254i0;
import androidx.core.view.C0258k0;
import d.AbstractC0487a;
import e.AbstractC0521a;
import i.C0558a;

/* JADX INFO: loaded from: classes.dex */
public class l0 implements J {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    Toolbar f4261a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f4262b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private View f4263c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private View f4264d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Drawable f4265e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Drawable f4266f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private Drawable f4267g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f4268h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    CharSequence f4269i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private CharSequence f4270j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private CharSequence f4271k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    Window.Callback f4272l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    boolean f4273m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private C0214c f4274n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private int f4275o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private int f4276p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private Drawable f4277q;

    class a implements View.OnClickListener {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final C0558a f4278b;

        a() {
            this.f4278b = new C0558a(l0.this.f4261a.getContext(), 0, R.id.home, 0, 0, l0.this.f4269i);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            l0 l0Var = l0.this;
            Window.Callback callback = l0Var.f4272l;
            if (callback == null || !l0Var.f4273m) {
                return;
            }
            callback.onMenuItemSelected(0, this.f4278b);
        }
    }

    class b extends C0258k0 {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private boolean f4280a = false;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f4281b;

        b(int i3) {
            this.f4281b = i3;
        }

        @Override // androidx.core.view.C0258k0, androidx.core.view.InterfaceC0256j0
        public void a(View view) {
            this.f4280a = true;
        }

        @Override // androidx.core.view.C0258k0, androidx.core.view.InterfaceC0256j0
        public void b(View view) {
            if (this.f4280a) {
                return;
            }
            l0.this.f4261a.setVisibility(this.f4281b);
        }

        @Override // androidx.core.view.C0258k0, androidx.core.view.InterfaceC0256j0
        public void c(View view) {
            l0.this.f4261a.setVisibility(0);
        }
    }

    public l0(Toolbar toolbar, boolean z3) {
        this(toolbar, z3, d.h.f8830a, d.e.f8755n);
    }

    private void E(CharSequence charSequence) {
        this.f4269i = charSequence;
        if ((this.f4262b & 8) != 0) {
            this.f4261a.setTitle(charSequence);
            if (this.f4268h) {
                androidx.core.view.Z.a0(this.f4261a.getRootView(), charSequence);
            }
        }
    }

    private void F() {
        if ((this.f4262b & 4) != 0) {
            if (TextUtils.isEmpty(this.f4271k)) {
                this.f4261a.setNavigationContentDescription(this.f4276p);
            } else {
                this.f4261a.setNavigationContentDescription(this.f4271k);
            }
        }
    }

    private void G() {
        if ((this.f4262b & 4) == 0) {
            this.f4261a.setNavigationIcon((Drawable) null);
            return;
        }
        Toolbar toolbar = this.f4261a;
        Drawable drawable = this.f4267g;
        if (drawable == null) {
            drawable = this.f4277q;
        }
        toolbar.setNavigationIcon(drawable);
    }

    private void H() {
        Drawable drawable;
        int i3 = this.f4262b;
        if ((i3 & 2) == 0) {
            drawable = null;
        } else if ((i3 & 1) == 0 || (drawable = this.f4266f) == null) {
            drawable = this.f4265e;
        }
        this.f4261a.setLogo(drawable);
    }

    private int v() {
        if (this.f4261a.getNavigationIcon() == null) {
            return 11;
        }
        this.f4277q = this.f4261a.getNavigationIcon();
        return 15;
    }

    public void A(CharSequence charSequence) {
        this.f4271k = charSequence;
        F();
    }

    public void B(Drawable drawable) {
        this.f4267g = drawable;
        G();
    }

    public void C(CharSequence charSequence) {
        this.f4270j = charSequence;
        if ((this.f4262b & 8) != 0) {
            this.f4261a.setSubtitle(charSequence);
        }
    }

    public void D(CharSequence charSequence) {
        this.f4268h = true;
        E(charSequence);
    }

    @Override // androidx.appcompat.widget.J
    public void a(Menu menu, j.a aVar) {
        if (this.f4274n == null) {
            C0214c c0214c = new C0214c(this.f4261a.getContext());
            this.f4274n = c0214c;
            c0214c.p(d.f.f8790g);
        }
        this.f4274n.k(aVar);
        this.f4261a.M((androidx.appcompat.view.menu.e) menu, this.f4274n);
    }

    @Override // androidx.appcompat.widget.J
    public boolean b() {
        return this.f4261a.D();
    }

    @Override // androidx.appcompat.widget.J
    public Context c() {
        return this.f4261a.getContext();
    }

    @Override // androidx.appcompat.widget.J
    public void collapseActionView() {
        this.f4261a.f();
    }

    @Override // androidx.appcompat.widget.J
    public void d() {
        this.f4273m = true;
    }

    @Override // androidx.appcompat.widget.J
    public boolean e() {
        return this.f4261a.C();
    }

    @Override // androidx.appcompat.widget.J
    public boolean f() {
        return this.f4261a.y();
    }

    @Override // androidx.appcompat.widget.J
    public boolean g() {
        return this.f4261a.R();
    }

    @Override // androidx.appcompat.widget.J
    public CharSequence getTitle() {
        return this.f4261a.getTitle();
    }

    @Override // androidx.appcompat.widget.J
    public boolean h() {
        return this.f4261a.e();
    }

    @Override // androidx.appcompat.widget.J
    public void i() {
        this.f4261a.g();
    }

    @Override // androidx.appcompat.widget.J
    public void j(int i3) {
        this.f4261a.setVisibility(i3);
    }

    @Override // androidx.appcompat.widget.J
    public void k(a0 a0Var) {
        View view = this.f4263c;
        if (view != null) {
            ViewParent parent = view.getParent();
            Toolbar toolbar = this.f4261a;
            if (parent == toolbar) {
                toolbar.removeView(this.f4263c);
            }
        }
        this.f4263c = a0Var;
        if (a0Var == null || this.f4275o != 2) {
            return;
        }
        this.f4261a.addView(a0Var, 0);
        Toolbar.g gVar = (Toolbar.g) this.f4263c.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) gVar).width = -2;
        ((ViewGroup.MarginLayoutParams) gVar).height = -2;
        gVar.f3161a = 8388691;
        a0Var.setAllowCollapse(true);
    }

    @Override // androidx.appcompat.widget.J
    public void l(boolean z3) {
    }

    @Override // androidx.appcompat.widget.J
    public boolean m() {
        return this.f4261a.x();
    }

    @Override // androidx.appcompat.widget.J
    public void n(int i3) {
        View view;
        int i4 = this.f4262b ^ i3;
        this.f4262b = i3;
        if (i4 != 0) {
            if ((i4 & 4) != 0) {
                if ((i3 & 4) != 0) {
                    F();
                }
                G();
            }
            if ((i4 & 3) != 0) {
                H();
            }
            if ((i4 & 8) != 0) {
                if ((i3 & 8) != 0) {
                    this.f4261a.setTitle(this.f4269i);
                    this.f4261a.setSubtitle(this.f4270j);
                } else {
                    this.f4261a.setTitle((CharSequence) null);
                    this.f4261a.setSubtitle((CharSequence) null);
                }
            }
            if ((i4 & 16) == 0 || (view = this.f4264d) == null) {
                return;
            }
            if ((i3 & 16) != 0) {
                this.f4261a.addView(view);
            } else {
                this.f4261a.removeView(view);
            }
        }
    }

    @Override // androidx.appcompat.widget.J
    public int o() {
        return this.f4262b;
    }

    @Override // androidx.appcompat.widget.J
    public void p(int i3) {
        y(i3 != 0 ? AbstractC0521a.b(c(), i3) : null);
    }

    @Override // androidx.appcompat.widget.J
    public int q() {
        return this.f4275o;
    }

    @Override // androidx.appcompat.widget.J
    public C0254i0 r(int i3, long j3) {
        return androidx.core.view.Z.c(this.f4261a).b(i3 == 0 ? 1.0f : 0.0f).f(j3).h(new b(i3));
    }

    @Override // androidx.appcompat.widget.J
    public void s() {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }

    @Override // androidx.appcompat.widget.J
    public void setIcon(int i3) {
        setIcon(i3 != 0 ? AbstractC0521a.b(c(), i3) : null);
    }

    @Override // androidx.appcompat.widget.J
    public void setWindowCallback(Window.Callback callback) {
        this.f4272l = callback;
    }

    @Override // androidx.appcompat.widget.J
    public void setWindowTitle(CharSequence charSequence) {
        if (this.f4268h) {
            return;
        }
        E(charSequence);
    }

    @Override // androidx.appcompat.widget.J
    public void t() {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }

    @Override // androidx.appcompat.widget.J
    public void u(boolean z3) {
        this.f4261a.setCollapsible(z3);
    }

    public void w(View view) {
        View view2 = this.f4264d;
        if (view2 != null && (this.f4262b & 16) != 0) {
            this.f4261a.removeView(view2);
        }
        this.f4264d = view;
        if (view == null || (this.f4262b & 16) == 0) {
            return;
        }
        this.f4261a.addView(view);
    }

    public void x(int i3) {
        if (i3 == this.f4276p) {
            return;
        }
        this.f4276p = i3;
        if (TextUtils.isEmpty(this.f4261a.getNavigationContentDescription())) {
            z(this.f4276p);
        }
    }

    public void y(Drawable drawable) {
        this.f4266f = drawable;
        H();
    }

    public void z(int i3) {
        A(i3 == 0 ? null : c().getString(i3));
    }

    public l0(Toolbar toolbar, boolean z3, int i3, int i4) {
        Drawable drawable;
        this.f4275o = 0;
        this.f4276p = 0;
        this.f4261a = toolbar;
        this.f4269i = toolbar.getTitle();
        this.f4270j = toolbar.getSubtitle();
        this.f4268h = this.f4269i != null;
        this.f4267g = toolbar.getNavigationIcon();
        h0 h0VarU = h0.u(toolbar.getContext(), null, d.j.f8952a, AbstractC0487a.f8675c, 0);
        this.f4277q = h0VarU.f(d.j.f9001l);
        if (z3) {
            CharSequence charSequenceO = h0VarU.o(d.j.f9025r);
            if (!TextUtils.isEmpty(charSequenceO)) {
                D(charSequenceO);
            }
            CharSequence charSequenceO2 = h0VarU.o(d.j.f9017p);
            if (!TextUtils.isEmpty(charSequenceO2)) {
                C(charSequenceO2);
            }
            Drawable drawableF = h0VarU.f(d.j.f9009n);
            if (drawableF != null) {
                y(drawableF);
            }
            Drawable drawableF2 = h0VarU.f(d.j.f9005m);
            if (drawableF2 != null) {
                setIcon(drawableF2);
            }
            if (this.f4267g == null && (drawable = this.f4277q) != null) {
                B(drawable);
            }
            n(h0VarU.j(d.j.f8985h, 0));
            int iM = h0VarU.m(d.j.f8981g, 0);
            if (iM != 0) {
                w(LayoutInflater.from(this.f4261a.getContext()).inflate(iM, (ViewGroup) this.f4261a, false));
                n(this.f4262b | 16);
            }
            int iL = h0VarU.l(d.j.f8993j, 0);
            if (iL > 0) {
                ViewGroup.LayoutParams layoutParams = this.f4261a.getLayoutParams();
                layoutParams.height = iL;
                this.f4261a.setLayoutParams(layoutParams);
            }
            int iD = h0VarU.d(d.j.f8977f, -1);
            int iD2 = h0VarU.d(d.j.f8972e, -1);
            if (iD >= 0 || iD2 >= 0) {
                this.f4261a.L(Math.max(iD, 0), Math.max(iD2, 0));
            }
            int iM2 = h0VarU.m(d.j.f9029s, 0);
            if (iM2 != 0) {
                Toolbar toolbar2 = this.f4261a;
                toolbar2.O(toolbar2.getContext(), iM2);
            }
            int iM3 = h0VarU.m(d.j.f9021q, 0);
            if (iM3 != 0) {
                Toolbar toolbar3 = this.f4261a;
                toolbar3.N(toolbar3.getContext(), iM3);
            }
            int iM4 = h0VarU.m(d.j.f9013o, 0);
            if (iM4 != 0) {
                this.f4261a.setPopupTheme(iM4);
            }
        } else {
            this.f4262b = v();
        }
        h0VarU.w();
        x(i3);
        this.f4271k = this.f4261a.getNavigationContentDescription();
        this.f4261a.setNavigationOnClickListener(new a());
    }

    @Override // androidx.appcompat.widget.J
    public void setIcon(Drawable drawable) {
        this.f4265e = drawable;
        H();
    }
}

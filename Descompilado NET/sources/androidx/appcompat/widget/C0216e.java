package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/* JADX INFO: renamed from: androidx.appcompat.widget.e, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0216e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final View f4201a;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private f0 f4204d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private f0 f4205e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private f0 f4206f;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f4203c = -1;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0222k f4202b = C0222k.b();

    C0216e(View view) {
        this.f4201a = view;
    }

    private boolean a(Drawable drawable) {
        if (this.f4206f == null) {
            this.f4206f = new f0();
        }
        f0 f0Var = this.f4206f;
        f0Var.a();
        ColorStateList colorStateListM = androidx.core.view.Z.m(this.f4201a);
        if (colorStateListM != null) {
            f0Var.f4217d = true;
            f0Var.f4214a = colorStateListM;
        }
        PorterDuff.Mode modeN = androidx.core.view.Z.n(this.f4201a);
        if (modeN != null) {
            f0Var.f4216c = true;
            f0Var.f4215b = modeN;
        }
        if (!f0Var.f4217d && !f0Var.f4216c) {
            return false;
        }
        C0222k.i(drawable, f0Var, this.f4201a.getDrawableState());
        return true;
    }

    private boolean k() {
        return this.f4204d != null;
    }

    void b() {
        Drawable background = this.f4201a.getBackground();
        if (background != null) {
            if (k() && a(background)) {
                return;
            }
            f0 f0Var = this.f4205e;
            if (f0Var != null) {
                C0222k.i(background, f0Var, this.f4201a.getDrawableState());
                return;
            }
            f0 f0Var2 = this.f4204d;
            if (f0Var2 != null) {
                C0222k.i(background, f0Var2, this.f4201a.getDrawableState());
            }
        }
    }

    ColorStateList c() {
        f0 f0Var = this.f4205e;
        if (f0Var != null) {
            return f0Var.f4214a;
        }
        return null;
    }

    PorterDuff.Mode d() {
        f0 f0Var = this.f4205e;
        if (f0Var != null) {
            return f0Var.f4215b;
        }
        return null;
    }

    void e(AttributeSet attributeSet, int i3) {
        h0 h0VarU = h0.u(this.f4201a.getContext(), attributeSet, d.j.K3, i3, 0);
        View view = this.f4201a;
        androidx.core.view.Z.V(view, view.getContext(), d.j.K3, attributeSet, h0VarU.q(), i3, 0);
        try {
            if (h0VarU.r(d.j.L3)) {
                this.f4203c = h0VarU.m(d.j.L3, -1);
                ColorStateList colorStateListF = this.f4202b.f(this.f4201a.getContext(), this.f4203c);
                if (colorStateListF != null) {
                    h(colorStateListF);
                }
            }
            if (h0VarU.r(d.j.M3)) {
                androidx.core.view.Z.c0(this.f4201a, h0VarU.c(d.j.M3));
            }
            if (h0VarU.r(d.j.N3)) {
                androidx.core.view.Z.d0(this.f4201a, O.d(h0VarU.j(d.j.N3, -1), null));
            }
            h0VarU.w();
        } catch (Throwable th) {
            h0VarU.w();
            throw th;
        }
    }

    void f(Drawable drawable) {
        this.f4203c = -1;
        h(null);
        b();
    }

    void g(int i3) {
        this.f4203c = i3;
        C0222k c0222k = this.f4202b;
        h(c0222k != null ? c0222k.f(this.f4201a.getContext(), i3) : null);
        b();
    }

    void h(ColorStateList colorStateList) {
        if (colorStateList != null) {
            if (this.f4204d == null) {
                this.f4204d = new f0();
            }
            f0 f0Var = this.f4204d;
            f0Var.f4214a = colorStateList;
            f0Var.f4217d = true;
        } else {
            this.f4204d = null;
        }
        b();
    }

    void i(ColorStateList colorStateList) {
        if (this.f4205e == null) {
            this.f4205e = new f0();
        }
        f0 f0Var = this.f4205e;
        f0Var.f4214a = colorStateList;
        f0Var.f4217d = true;
        b();
    }

    void j(PorterDuff.Mode mode) {
        if (this.f4205e == null) {
            this.f4205e = new f0();
        }
        f0 f0Var = this.f4205e;
        f0Var.f4215b = mode;
        f0Var.f4216c = true;
        b();
    }
}

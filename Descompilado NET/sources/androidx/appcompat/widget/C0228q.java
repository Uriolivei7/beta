package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import e.AbstractC0521a;

/* JADX INFO: renamed from: androidx.appcompat.widget.q, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0228q {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ImageView f4304a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private f0 f4305b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private f0 f4306c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private f0 f4307d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f4308e = 0;

    public C0228q(ImageView imageView) {
        this.f4304a = imageView;
    }

    private boolean a(Drawable drawable) {
        if (this.f4307d == null) {
            this.f4307d = new f0();
        }
        f0 f0Var = this.f4307d;
        f0Var.a();
        ColorStateList colorStateListA = androidx.core.widget.e.a(this.f4304a);
        if (colorStateListA != null) {
            f0Var.f4217d = true;
            f0Var.f4214a = colorStateListA;
        }
        PorterDuff.Mode modeB = androidx.core.widget.e.b(this.f4304a);
        if (modeB != null) {
            f0Var.f4216c = true;
            f0Var.f4215b = modeB;
        }
        if (!f0Var.f4217d && !f0Var.f4216c) {
            return false;
        }
        C0222k.i(drawable, f0Var, this.f4304a.getDrawableState());
        return true;
    }

    private boolean l() {
        return this.f4305b != null;
    }

    void b() {
        if (this.f4304a.getDrawable() != null) {
            this.f4304a.getDrawable().setLevel(this.f4308e);
        }
    }

    void c() {
        Drawable drawable = this.f4304a.getDrawable();
        if (drawable != null) {
            O.a(drawable);
        }
        if (drawable != null) {
            if (l() && a(drawable)) {
                return;
            }
            f0 f0Var = this.f4306c;
            if (f0Var != null) {
                C0222k.i(drawable, f0Var, this.f4304a.getDrawableState());
                return;
            }
            f0 f0Var2 = this.f4305b;
            if (f0Var2 != null) {
                C0222k.i(drawable, f0Var2, this.f4304a.getDrawableState());
            }
        }
    }

    ColorStateList d() {
        f0 f0Var = this.f4306c;
        if (f0Var != null) {
            return f0Var.f4214a;
        }
        return null;
    }

    PorterDuff.Mode e() {
        f0 f0Var = this.f4306c;
        if (f0Var != null) {
            return f0Var.f4215b;
        }
        return null;
    }

    boolean f() {
        return !(this.f4304a.getBackground() instanceof RippleDrawable);
    }

    public void g(AttributeSet attributeSet, int i3) {
        int iM;
        h0 h0VarU = h0.u(this.f4304a.getContext(), attributeSet, d.j.f8908P, i3, 0);
        ImageView imageView = this.f4304a;
        androidx.core.view.Z.V(imageView, imageView.getContext(), d.j.f8908P, attributeSet, h0VarU.q(), i3, 0);
        try {
            Drawable drawable = this.f4304a.getDrawable();
            if (drawable == null && (iM = h0VarU.m(d.j.f8912Q, -1)) != -1 && (drawable = AbstractC0521a.b(this.f4304a.getContext(), iM)) != null) {
                this.f4304a.setImageDrawable(drawable);
            }
            if (drawable != null) {
                O.a(drawable);
            }
            if (h0VarU.r(d.j.f8916R)) {
                androidx.core.widget.e.c(this.f4304a, h0VarU.c(d.j.f8916R));
            }
            if (h0VarU.r(d.j.f8920S)) {
                androidx.core.widget.e.d(this.f4304a, O.d(h0VarU.j(d.j.f8920S, -1), null));
            }
            h0VarU.w();
        } catch (Throwable th) {
            h0VarU.w();
            throw th;
        }
    }

    void h(Drawable drawable) {
        this.f4308e = drawable.getLevel();
    }

    public void i(int i3) {
        if (i3 != 0) {
            Drawable drawableB = AbstractC0521a.b(this.f4304a.getContext(), i3);
            if (drawableB != null) {
                O.a(drawableB);
            }
            this.f4304a.setImageDrawable(drawableB);
        } else {
            this.f4304a.setImageDrawable(null);
        }
        c();
    }

    void j(ColorStateList colorStateList) {
        if (this.f4306c == null) {
            this.f4306c = new f0();
        }
        f0 f0Var = this.f4306c;
        f0Var.f4214a = colorStateList;
        f0Var.f4217d = true;
        c();
    }

    void k(PorterDuff.Mode mode) {
        if (this.f4306c == null) {
            this.f4306c = new f0();
        }
        f0 f0Var = this.f4306c;
        f0Var.f4215b = mode;
        f0Var.f4216c = true;
        c();
    }
}

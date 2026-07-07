package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

/* JADX INFO: renamed from: androidx.appcompat.widget.z, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0236z extends C0231u {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final SeekBar f4339d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Drawable f4340e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private ColorStateList f4341f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private PorterDuff.Mode f4342g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f4343h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f4344i;

    C0236z(SeekBar seekBar) {
        super(seekBar);
        this.f4341f = null;
        this.f4342g = null;
        this.f4343h = false;
        this.f4344i = false;
        this.f4339d = seekBar;
    }

    private void f() {
        Drawable drawable = this.f4340e;
        if (drawable != null) {
            if (this.f4343h || this.f4344i) {
                Drawable drawableJ = androidx.core.graphics.drawable.a.j(drawable.mutate());
                this.f4340e = drawableJ;
                if (this.f4343h) {
                    androidx.core.graphics.drawable.a.g(drawableJ, this.f4341f);
                }
                if (this.f4344i) {
                    androidx.core.graphics.drawable.a.h(this.f4340e, this.f4342g);
                }
                if (this.f4340e.isStateful()) {
                    this.f4340e.setState(this.f4339d.getDrawableState());
                }
            }
        }
    }

    @Override // androidx.appcompat.widget.C0231u
    void c(AttributeSet attributeSet, int i3) {
        super.c(attributeSet, i3);
        h0 h0VarU = h0.u(this.f4339d.getContext(), attributeSet, d.j.f8924T, i3, 0);
        SeekBar seekBar = this.f4339d;
        androidx.core.view.Z.V(seekBar, seekBar.getContext(), d.j.f8924T, attributeSet, h0VarU.q(), i3, 0);
        Drawable drawableG = h0VarU.g(d.j.f8928U);
        if (drawableG != null) {
            this.f4339d.setThumb(drawableG);
        }
        j(h0VarU.f(d.j.f8932V));
        if (h0VarU.r(d.j.f8940X)) {
            this.f4342g = O.d(h0VarU.j(d.j.f8940X, -1), this.f4342g);
            this.f4344i = true;
        }
        if (h0VarU.r(d.j.f8936W)) {
            this.f4341f = h0VarU.c(d.j.f8936W);
            this.f4343h = true;
        }
        h0VarU.w();
        f();
    }

    void g(Canvas canvas) {
        if (this.f4340e != null) {
            int max = this.f4339d.getMax();
            if (max > 1) {
                int intrinsicWidth = this.f4340e.getIntrinsicWidth();
                int intrinsicHeight = this.f4340e.getIntrinsicHeight();
                int i3 = intrinsicWidth >= 0 ? intrinsicWidth / 2 : 1;
                int i4 = intrinsicHeight >= 0 ? intrinsicHeight / 2 : 1;
                this.f4340e.setBounds(-i3, -i4, i3, i4);
                float width = ((this.f4339d.getWidth() - this.f4339d.getPaddingLeft()) - this.f4339d.getPaddingRight()) / max;
                int iSave = canvas.save();
                canvas.translate(this.f4339d.getPaddingLeft(), this.f4339d.getHeight() / 2);
                for (int i5 = 0; i5 <= max; i5++) {
                    this.f4340e.draw(canvas);
                    canvas.translate(width, 0.0f);
                }
                canvas.restoreToCount(iSave);
            }
        }
    }

    void h() {
        Drawable drawable = this.f4340e;
        if (drawable != null && drawable.isStateful() && drawable.setState(this.f4339d.getDrawableState())) {
            this.f4339d.invalidateDrawable(drawable);
        }
    }

    void i() {
        Drawable drawable = this.f4340e;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    void j(Drawable drawable) {
        Drawable drawable2 = this.f4340e;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.f4340e = drawable;
        if (drawable != null) {
            drawable.setCallback(this.f4339d);
            androidx.core.graphics.drawable.a.e(drawable, this.f4339d.getLayoutDirection());
            if (drawable.isStateful()) {
                drawable.setState(this.f4339d.getDrawableState());
            }
            f();
        }
        this.f4339d.invalidate();
    }
}

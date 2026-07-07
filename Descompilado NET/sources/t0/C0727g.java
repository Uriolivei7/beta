package t0;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/* JADX INFO: renamed from: t0.g, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0727g extends Drawable implements Drawable.Callback, E, D, InterfaceC0723c {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final Matrix f10658e = new Matrix();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Drawable f10659b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C0724d f10660c = new C0724d();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected E f10661d;

    public C0727g(Drawable drawable) {
        this.f10659b = drawable;
        C0725e.d(drawable, this, this);
    }

    @Override // t0.InterfaceC0723c
    public Drawable d(Drawable drawable) {
        return v(drawable);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Drawable drawable = this.f10659b;
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    @Override // t0.E
    public void g(RectF rectF) {
        E e4 = this.f10661d;
        if (e4 != null) {
            e4.g(rectF);
        } else {
            rectF.set(getBounds());
        }
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        Drawable drawable = this.f10659b;
        return drawable == null ? super.getConstantState() : drawable.getConstantState();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable getCurrent() {
        return this.f10659b;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        Drawable drawable = this.f10659b;
        return drawable == null ? super.getIntrinsicHeight() : drawable.getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        Drawable drawable = this.f10659b;
        return drawable == null ? super.getIntrinsicWidth() : drawable.getIntrinsicWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        Drawable drawable = this.f10659b;
        if (drawable == null) {
            return 0;
        }
        return drawable.getOpacity();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        Drawable drawable = this.f10659b;
        return drawable == null ? super.getPadding(rect) : drawable.getPadding(rect);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        Drawable drawable = this.f10659b;
        if (drawable == null) {
            return false;
        }
        return drawable.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        Drawable drawable = this.f10659b;
        if (drawable != null) {
            drawable.mutate();
        }
        return this;
    }

    @Override // t0.E
    public void n(Matrix matrix) {
        u(matrix);
    }

    @Override // t0.D
    public void o(E e4) {
        this.f10661d = e4;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        Drawable drawable = this.f10659b;
        if (drawable != null) {
            drawable.setBounds(rect);
        }
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int i3) {
        Drawable drawable = this.f10659b;
        return drawable == null ? super.onLevelChange(i3) : drawable.setLevel(i3);
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        Drawable drawable = this.f10659b;
        return drawable == null ? super.onStateChange(iArr) : drawable.setState(iArr);
    }

    @Override // t0.InterfaceC0723c
    public Drawable q() {
        return getCurrent();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j3) {
        scheduleSelf(runnable, j3);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        this.f10660c.b(i3);
        Drawable drawable = this.f10659b;
        if (drawable != null) {
            drawable.setAlpha(i3);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f10660c.c(colorFilter);
        Drawable drawable = this.f10659b;
        if (drawable != null) {
            drawable.setColorFilter(colorFilter);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean z3) {
        this.f10660c.d(z3);
        Drawable drawable = this.f10659b;
        if (drawable != null) {
            drawable.setDither(z3);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean z3) {
        this.f10660c.e(z3);
        Drawable drawable = this.f10659b;
        if (drawable != null) {
            drawable.setFilterBitmap(z3);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspot(float f3, float f4) {
        Drawable drawable = this.f10659b;
        if (drawable != null) {
            drawable.setHotspot(f3, f4);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z3, boolean z4) {
        boolean visible = super.setVisible(z3, z4);
        Drawable drawable = this.f10659b;
        return drawable == null ? visible : drawable.setVisible(z3, z4);
    }

    protected void u(Matrix matrix) {
        E e4 = this.f10661d;
        if (e4 != null) {
            e4.n(matrix);
        } else {
            matrix.reset();
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        unscheduleSelf(runnable);
    }

    public Drawable v(Drawable drawable) {
        Drawable drawableW = w(drawable);
        invalidateSelf();
        return drawableW;
    }

    protected Drawable w(Drawable drawable) {
        Drawable drawable2 = this.f10659b;
        C0725e.d(drawable2, null, null);
        C0725e.d(drawable, null, null);
        C0725e.e(drawable, this.f10660c);
        C0725e.a(drawable, this);
        C0725e.d(drawable, this, this);
        this.f10659b = drawable;
        return drawable2;
    }
}

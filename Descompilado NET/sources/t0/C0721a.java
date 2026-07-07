package t0;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/* JADX INFO: renamed from: t0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0721a extends Drawable implements Drawable.Callback, E, D {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private E f10621b;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Drawable[] f10623d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final InterfaceC0723c[] f10624e;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C0724d f10622c = new C0724d();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Rect f10625f = new Rect();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f10626g = false;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f10627h = false;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f10628i = false;

    /* JADX INFO: renamed from: t0.a$a, reason: collision with other inner class name */
    class C0146a implements InterfaceC0723c {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f10629b;

        C0146a(int i3) {
            this.f10629b = i3;
        }

        @Override // t0.InterfaceC0723c
        public Drawable d(Drawable drawable) {
            return C0721a.this.e(this.f10629b, drawable);
        }

        @Override // t0.InterfaceC0723c
        public Drawable q() {
            return C0721a.this.b(this.f10629b);
        }
    }

    public C0721a(Drawable[] drawableArr) {
        int i3 = 0;
        X.k.g(drawableArr);
        this.f10623d = drawableArr;
        while (true) {
            Drawable[] drawableArr2 = this.f10623d;
            if (i3 >= drawableArr2.length) {
                this.f10624e = new InterfaceC0723c[drawableArr2.length];
                return;
            } else {
                C0725e.d(drawableArr2[i3], this, this);
                i3++;
            }
        }
    }

    private InterfaceC0723c a(int i3) {
        return new C0146a(i3);
    }

    public Drawable b(int i3) {
        X.k.b(Boolean.valueOf(i3 >= 0));
        X.k.b(Boolean.valueOf(i3 < this.f10623d.length));
        return this.f10623d[i3];
    }

    public InterfaceC0723c c(int i3) {
        X.k.b(Boolean.valueOf(i3 >= 0));
        X.k.b(Boolean.valueOf(i3 < this.f10624e.length));
        InterfaceC0723c[] interfaceC0723cArr = this.f10624e;
        if (interfaceC0723cArr[i3] == null) {
            interfaceC0723cArr[i3] = a(i3);
        }
        return this.f10624e[i3];
    }

    public int d() {
        return this.f10623d.length;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        int i3 = 0;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i3 >= drawableArr.length) {
                return;
            }
            Drawable drawable = drawableArr[i3];
            if (drawable != null) {
                drawable.draw(canvas);
            }
            i3++;
        }
    }

    public Drawable e(int i3, Drawable drawable) {
        X.k.b(Boolean.valueOf(i3 >= 0));
        X.k.b(Boolean.valueOf(i3 < this.f10623d.length));
        Drawable drawable2 = this.f10623d[i3];
        if (drawable != drawable2) {
            if (drawable != null && this.f10628i) {
                drawable.mutate();
            }
            C0725e.d(this.f10623d[i3], null, null);
            C0725e.d(drawable, null, null);
            C0725e.e(drawable, this.f10622c);
            C0725e.a(drawable, this);
            C0725e.d(drawable, this, this);
            this.f10627h = false;
            this.f10623d[i3] = drawable;
            invalidateSelf();
        }
        return drawable2;
    }

    @Override // t0.E
    public void g(RectF rectF) {
        E e4 = this.f10621b;
        if (e4 != null) {
            e4.g(rectF);
        } else {
            rectF.set(getBounds());
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        int i3 = 0;
        int iMax = -1;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i3 >= drawableArr.length) {
                break;
            }
            Drawable drawable = drawableArr[i3];
            if (drawable != null) {
                iMax = Math.max(iMax, drawable.getIntrinsicHeight());
            }
            i3++;
        }
        if (iMax > 0) {
            return iMax;
        }
        return -1;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        int i3 = 0;
        int iMax = -1;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i3 >= drawableArr.length) {
                break;
            }
            Drawable drawable = drawableArr[i3];
            if (drawable != null) {
                iMax = Math.max(iMax, drawable.getIntrinsicWidth());
            }
            i3++;
        }
        if (iMax > 0) {
            return iMax;
        }
        return -1;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        if (this.f10623d.length == 0) {
            return -2;
        }
        int i3 = 1;
        int iResolveOpacity = -1;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i3 >= drawableArr.length) {
                return iResolveOpacity;
            }
            Drawable drawable = drawableArr[i3];
            if (drawable != null) {
                iResolveOpacity = Drawable.resolveOpacity(iResolveOpacity, drawable.getOpacity());
            }
            i3++;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        int i3 = 0;
        rect.left = 0;
        rect.top = 0;
        rect.right = 0;
        rect.bottom = 0;
        Rect rect2 = this.f10625f;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i3 >= drawableArr.length) {
                return true;
            }
            Drawable drawable = drawableArr[i3];
            if (drawable != null) {
                drawable.getPadding(rect2);
                rect.left = Math.max(rect.left, rect2.left);
                rect.top = Math.max(rect.top, rect2.top);
                rect.right = Math.max(rect.right, rect2.right);
                rect.bottom = Math.max(rect.bottom, rect2.bottom);
            }
            i3++;
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        if (!this.f10627h) {
            this.f10626g = false;
            int i3 = 0;
            while (true) {
                Drawable[] drawableArr = this.f10623d;
                boolean z3 = true;
                if (i3 >= drawableArr.length) {
                    break;
                }
                Drawable drawable = drawableArr[i3];
                boolean z4 = this.f10626g;
                if (drawable == null || !drawable.isStateful()) {
                    z3 = false;
                }
                this.f10626g = z4 | z3;
                i3++;
            }
            this.f10627h = true;
        }
        return this.f10626g;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        int i3 = 0;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i3 >= drawableArr.length) {
                this.f10628i = true;
                return this;
            }
            Drawable drawable = drawableArr[i3];
            if (drawable != null) {
                drawable.mutate();
            }
            i3++;
        }
    }

    @Override // t0.E
    public void n(Matrix matrix) {
        E e4 = this.f10621b;
        if (e4 != null) {
            e4.n(matrix);
        } else {
            matrix.reset();
        }
    }

    @Override // t0.D
    public void o(E e4) {
        this.f10621b = e4;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        int i3 = 0;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i3 >= drawableArr.length) {
                return;
            }
            Drawable drawable = drawableArr[i3];
            if (drawable != null) {
                drawable.setBounds(rect);
            }
            i3++;
        }
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int i3) {
        int i4 = 0;
        boolean z3 = false;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i4 >= drawableArr.length) {
                return z3;
            }
            Drawable drawable = drawableArr[i4];
            if (drawable != null && drawable.setLevel(i3)) {
                z3 = true;
            }
            i4++;
        }
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        int i3 = 0;
        boolean z3 = false;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i3 >= drawableArr.length) {
                return z3;
            }
            Drawable drawable = drawableArr[i3];
            if (drawable != null && drawable.setState(iArr)) {
                z3 = true;
            }
            i3++;
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j3) {
        scheduleSelf(runnable, j3);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        this.f10622c.b(i3);
        int i4 = 0;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i4 >= drawableArr.length) {
                return;
            }
            Drawable drawable = drawableArr[i4];
            if (drawable != null) {
                drawable.setAlpha(i3);
            }
            i4++;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f10622c.c(colorFilter);
        int i3 = 0;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i3 >= drawableArr.length) {
                return;
            }
            Drawable drawable = drawableArr[i3];
            if (drawable != null) {
                drawable.setColorFilter(colorFilter);
            }
            i3++;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean z3) {
        this.f10622c.d(z3);
        int i3 = 0;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i3 >= drawableArr.length) {
                return;
            }
            Drawable drawable = drawableArr[i3];
            if (drawable != null) {
                drawable.setDither(z3);
            }
            i3++;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean z3) {
        this.f10622c.e(z3);
        int i3 = 0;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i3 >= drawableArr.length) {
                return;
            }
            Drawable drawable = drawableArr[i3];
            if (drawable != null) {
                drawable.setFilterBitmap(z3);
            }
            i3++;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspot(float f3, float f4) {
        int i3 = 0;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i3 >= drawableArr.length) {
                return;
            }
            Drawable drawable = drawableArr[i3];
            if (drawable != null) {
                drawable.setHotspot(f3, f4);
            }
            i3++;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z3, boolean z4) {
        boolean visible = super.setVisible(z3, z4);
        int i3 = 0;
        while (true) {
            Drawable[] drawableArr = this.f10623d;
            if (i3 >= drawableArr.length) {
                return visible;
            }
            Drawable drawable = drawableArr[i3];
            if (drawable != null) {
                drawable.setVisible(z3, z4);
            }
            i3++;
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        unscheduleSelf(runnable);
    }
}

package t0;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public abstract class n extends Drawable implements j, D {

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private E f10717D;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Drawable f10718b;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    float[] f10728l;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    RectF f10733q;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    Matrix f10739w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    Matrix f10740x;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected boolean f10719c = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected boolean f10720d = false;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    protected float f10721e = 0.0f;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    protected final Path f10722f = new Path();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    protected boolean f10723g = true;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    protected int f10724h = 0;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    protected final Path f10725i = new Path();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final float[] f10726j = new float[8];

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    final float[] f10727k = new float[8];

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    final RectF f10729m = new RectF();

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    final RectF f10730n = new RectF();

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    final RectF f10731o = new RectF();

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    final RectF f10732p = new RectF();

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    final Matrix f10734r = new Matrix();

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    final Matrix f10735s = new Matrix();

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    final Matrix f10736t = new Matrix();

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    final Matrix f10737u = new Matrix();

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    final Matrix f10738v = new Matrix();

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    final Matrix f10741y = new Matrix();

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private float f10742z = 0.0f;

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private boolean f10714A = false;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private boolean f10715B = false;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private boolean f10716C = true;

    n(Drawable drawable) {
        this.f10718b = drawable;
    }

    private static Matrix a(Matrix matrix) {
        if (matrix == null) {
            return null;
        }
        return new Matrix(matrix);
    }

    private static boolean d(Matrix matrix, Matrix matrix2) {
        if (matrix == null && matrix2 == null) {
            return true;
        }
        if (matrix == null || matrix2 == null) {
            return false;
        }
        return matrix.equals(matrix2);
    }

    public boolean b() {
        return this.f10715B;
    }

    @Override // t0.j
    public void c(int i3, float f3) {
        if (this.f10724h == i3 && this.f10721e == f3) {
            return;
        }
        this.f10724h = i3;
        this.f10721e = f3;
        this.f10716C = true;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void clearColorFilter() {
        this.f10718b.clearColorFilter();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (V0.b.d()) {
            V0.b.a("RoundedDrawable#draw");
        }
        this.f10718b.draw(canvas);
        if (V0.b.d()) {
            V0.b.b();
        }
    }

    boolean e() {
        return this.f10719c || this.f10720d || this.f10721e > 0.0f;
    }

    protected void g() {
        float[] fArr;
        if (this.f10716C) {
            this.f10725i.reset();
            RectF rectF = this.f10729m;
            float f3 = this.f10721e;
            rectF.inset(f3 / 2.0f, f3 / 2.0f);
            if (this.f10719c) {
                this.f10725i.addCircle(this.f10729m.centerX(), this.f10729m.centerY(), Math.min(this.f10729m.width(), this.f10729m.height()) / 2.0f, Path.Direction.CW);
            } else {
                int i3 = 0;
                while (true) {
                    fArr = this.f10727k;
                    if (i3 >= fArr.length) {
                        break;
                    }
                    fArr[i3] = (this.f10726j[i3] + this.f10742z) - (this.f10721e / 2.0f);
                    i3++;
                }
                this.f10725i.addRoundRect(this.f10729m, fArr, Path.Direction.CW);
            }
            RectF rectF2 = this.f10729m;
            float f4 = this.f10721e;
            rectF2.inset((-f4) / 2.0f, (-f4) / 2.0f);
            this.f10722f.reset();
            float f5 = this.f10742z + (this.f10714A ? this.f10721e : 0.0f);
            this.f10729m.inset(f5, f5);
            if (this.f10719c) {
                this.f10722f.addCircle(this.f10729m.centerX(), this.f10729m.centerY(), Math.min(this.f10729m.width(), this.f10729m.height()) / 2.0f, Path.Direction.CW);
            } else if (this.f10714A) {
                if (this.f10728l == null) {
                    this.f10728l = new float[8];
                }
                for (int i4 = 0; i4 < this.f10727k.length; i4++) {
                    this.f10728l[i4] = this.f10726j[i4] - this.f10721e;
                }
                this.f10722f.addRoundRect(this.f10729m, this.f10728l, Path.Direction.CW);
            } else {
                this.f10722f.addRoundRect(this.f10729m, this.f10726j, Path.Direction.CW);
            }
            float f6 = -f5;
            this.f10729m.inset(f6, f6);
            this.f10722f.setFillType(Path.FillType.WINDING);
            this.f10716C = false;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.f10718b.getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        return this.f10718b.getColorFilter();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.f10718b.getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.f10718b.getIntrinsicWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return this.f10718b.getOpacity();
    }

    @Override // t0.j
    public void h(boolean z3) {
        this.f10719c = z3;
        this.f10716C = true;
        invalidateSelf();
    }

    @Override // t0.j
    public void i(float f3) {
        if (this.f10742z != f3) {
            this.f10742z = f3;
            this.f10716C = true;
            invalidateSelf();
        }
    }

    protected void j() {
        Matrix matrix;
        Matrix matrix2;
        E e4 = this.f10717D;
        if (e4 != null) {
            e4.n(this.f10736t);
            this.f10717D.g(this.f10729m);
        } else {
            this.f10736t.reset();
            this.f10729m.set(getBounds());
        }
        this.f10731o.set(0.0f, 0.0f, getIntrinsicWidth(), getIntrinsicHeight());
        this.f10732p.set(this.f10718b.getBounds());
        Matrix matrix3 = this.f10734r;
        RectF rectF = this.f10731o;
        RectF rectF2 = this.f10732p;
        Matrix.ScaleToFit scaleToFit = Matrix.ScaleToFit.FILL;
        matrix3.setRectToRect(rectF, rectF2, scaleToFit);
        if (this.f10714A) {
            RectF rectF3 = this.f10733q;
            if (rectF3 == null) {
                this.f10733q = new RectF(this.f10729m);
            } else {
                rectF3.set(this.f10729m);
            }
            RectF rectF4 = this.f10733q;
            float f3 = this.f10721e;
            rectF4.inset(f3, f3);
            if (this.f10739w == null) {
                this.f10739w = new Matrix();
            }
            this.f10739w.setRectToRect(this.f10729m, this.f10733q, scaleToFit);
        } else {
            Matrix matrix4 = this.f10739w;
            if (matrix4 != null) {
                matrix4.reset();
            }
        }
        if (!this.f10736t.equals(this.f10737u) || !this.f10734r.equals(this.f10735s) || ((matrix2 = this.f10739w) != null && !d(matrix2, this.f10740x))) {
            this.f10723g = true;
            this.f10736t.invert(this.f10738v);
            this.f10741y.set(this.f10736t);
            if (this.f10714A && (matrix = this.f10739w) != null) {
                this.f10741y.postConcat(matrix);
            }
            this.f10741y.preConcat(this.f10734r);
            this.f10737u.set(this.f10736t);
            this.f10735s.set(this.f10734r);
            if (this.f10714A) {
                Matrix matrix5 = this.f10740x;
                if (matrix5 == null) {
                    this.f10740x = a(this.f10739w);
                } else {
                    matrix5.set(this.f10739w);
                }
            } else {
                Matrix matrix6 = this.f10740x;
                if (matrix6 != null) {
                    matrix6.reset();
                }
            }
        }
        if (this.f10729m.equals(this.f10730n)) {
            return;
        }
        this.f10716C = true;
        this.f10730n.set(this.f10729m);
    }

    @Override // t0.j
    public void m(float f3) {
        X.k.i(f3 >= 0.0f);
        Arrays.fill(this.f10726j, f3);
        this.f10720d = f3 != 0.0f;
        this.f10716C = true;
        invalidateSelf();
    }

    @Override // t0.D
    public void o(E e4) {
        this.f10717D = e4;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        this.f10718b.setBounds(rect);
    }

    @Override // t0.j
    public void p(boolean z3) {
        if (this.f10715B != z3) {
            this.f10715B = z3;
            invalidateSelf();
        }
    }

    @Override // t0.j
    public void s(boolean z3) {
        if (this.f10714A != z3) {
            this.f10714A = z3;
            this.f10716C = true;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        this.f10718b.setAlpha(i3);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(int i3, PorterDuff.Mode mode) {
        this.f10718b.setColorFilter(i3, mode);
    }

    @Override // t0.j
    public void t(float[] fArr) {
        if (fArr == null) {
            Arrays.fill(this.f10726j, 0.0f);
            this.f10720d = false;
        } else {
            X.k.c(fArr.length == 8, "radii should have exactly 8 values");
            System.arraycopy(fArr, 0, this.f10726j, 0, 8);
            this.f10720d = false;
            for (int i3 = 0; i3 < 8; i3++) {
                this.f10720d |= fArr[i3] > 0.0f;
            }
        }
        this.f10716C = true;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f10718b.setColorFilter(colorFilter);
    }

    public void f(boolean z3) {
    }
}

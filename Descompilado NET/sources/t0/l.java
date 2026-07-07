package t0;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class l extends Drawable implements j {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final float[] f10678b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final float[] f10679c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    float[] f10680d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    final Paint f10681e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f10682f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private float f10683g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private float f10684h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f10685i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f10686j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f10687k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    final Path f10688l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    final Path f10689m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private int f10690n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private final RectF f10691o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private int f10692p;

    public l(int i3) {
        this.f10678b = new float[8];
        this.f10679c = new float[8];
        this.f10681e = new Paint(1);
        this.f10682f = false;
        this.f10683g = 0.0f;
        this.f10684h = 0.0f;
        this.f10685i = 0;
        this.f10686j = false;
        this.f10687k = false;
        this.f10688l = new Path();
        this.f10689m = new Path();
        this.f10690n = 0;
        this.f10691o = new RectF();
        this.f10692p = 255;
        d(i3);
    }

    public static l a(ColorDrawable colorDrawable) {
        return new l(colorDrawable.getColor());
    }

    private void e() {
        float[] fArr;
        float[] fArr2;
        this.f10688l.reset();
        this.f10689m.reset();
        this.f10691o.set(getBounds());
        RectF rectF = this.f10691o;
        float f3 = this.f10683g;
        rectF.inset(f3 / 2.0f, f3 / 2.0f);
        int i3 = 0;
        if (this.f10682f) {
            this.f10689m.addCircle(this.f10691o.centerX(), this.f10691o.centerY(), Math.min(this.f10691o.width(), this.f10691o.height()) / 2.0f, Path.Direction.CW);
        } else {
            int i4 = 0;
            while (true) {
                fArr = this.f10679c;
                if (i4 >= fArr.length) {
                    break;
                }
                fArr[i4] = (this.f10678b[i4] + this.f10684h) - (this.f10683g / 2.0f);
                i4++;
            }
            this.f10689m.addRoundRect(this.f10691o, fArr, Path.Direction.CW);
        }
        RectF rectF2 = this.f10691o;
        float f4 = this.f10683g;
        rectF2.inset((-f4) / 2.0f, (-f4) / 2.0f);
        float f5 = this.f10684h + (this.f10686j ? this.f10683g : 0.0f);
        this.f10691o.inset(f5, f5);
        if (this.f10682f) {
            this.f10688l.addCircle(this.f10691o.centerX(), this.f10691o.centerY(), Math.min(this.f10691o.width(), this.f10691o.height()) / 2.0f, Path.Direction.CW);
        } else if (this.f10686j) {
            if (this.f10680d == null) {
                this.f10680d = new float[8];
            }
            while (true) {
                fArr2 = this.f10680d;
                if (i3 >= fArr2.length) {
                    break;
                }
                fArr2[i3] = this.f10678b[i3] - this.f10683g;
                i3++;
            }
            this.f10688l.addRoundRect(this.f10691o, fArr2, Path.Direction.CW);
        } else {
            this.f10688l.addRoundRect(this.f10691o, this.f10678b, Path.Direction.CW);
        }
        float f6 = -f5;
        this.f10691o.inset(f6, f6);
    }

    public boolean b() {
        return this.f10687k;
    }

    @Override // t0.j
    public void c(int i3, float f3) {
        if (this.f10685i != i3) {
            this.f10685i = i3;
            invalidateSelf();
        }
        if (this.f10683g != f3) {
            this.f10683g = f3;
            e();
            invalidateSelf();
        }
    }

    public void d(int i3) {
        if (this.f10690n != i3) {
            this.f10690n = i3;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.f10681e.setColor(C0725e.c(this.f10690n, this.f10692p));
        this.f10681e.setStyle(Paint.Style.FILL);
        this.f10681e.setFilterBitmap(b());
        canvas.drawPath(this.f10688l, this.f10681e);
        if (this.f10683g != 0.0f) {
            this.f10681e.setColor(C0725e.c(this.f10685i, this.f10692p));
            this.f10681e.setStyle(Paint.Style.STROKE);
            this.f10681e.setStrokeWidth(this.f10683g);
            canvas.drawPath(this.f10689m, this.f10681e);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.f10692p;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return C0725e.b(C0725e.c(this.f10690n, this.f10692p));
    }

    @Override // t0.j
    public void h(boolean z3) {
        this.f10682f = z3;
        e();
        invalidateSelf();
    }

    @Override // t0.j
    public void i(float f3) {
        if (this.f10684h != f3) {
            this.f10684h = f3;
            e();
            invalidateSelf();
        }
    }

    @Override // t0.j
    public void m(float f3) {
        X.k.c(f3 >= 0.0f, "radius should be non negative");
        Arrays.fill(this.f10678b, f3);
        e();
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        e();
    }

    @Override // t0.j
    public void p(boolean z3) {
        if (this.f10687k != z3) {
            this.f10687k = z3;
            invalidateSelf();
        }
    }

    @Override // t0.j
    public void s(boolean z3) {
        if (this.f10686j != z3) {
            this.f10686j = z3;
            e();
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i3) {
        if (i3 != this.f10692p) {
            this.f10692p = i3;
            invalidateSelf();
        }
    }

    @Override // t0.j
    public void t(float[] fArr) {
        if (fArr == null) {
            Arrays.fill(this.f10678b, 0.0f);
        } else {
            X.k.c(fArr.length == 8, "radii should have exactly 8 values");
            System.arraycopy(fArr, 0, this.f10678b, 0, 8);
        }
        e();
        invalidateSelf();
    }

    public l(float[] fArr, int i3) {
        this(i3);
        t(fArr);
    }

    public l(float f3, int i3) {
        this(i3);
        m(f3);
    }

    @Override // t0.j
    public void f(boolean z3) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }
}

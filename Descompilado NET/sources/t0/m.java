package t0;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class m extends C0727g implements j {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    b f10693f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final RectF f10694g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private RectF f10695h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private Matrix f10696i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final float[] f10697j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    final float[] f10698k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    final Paint f10699l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private boolean f10700m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private float f10701n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private int f10702o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private int f10703p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private float f10704q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private boolean f10705r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private boolean f10706s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final Path f10707t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private final Path f10708u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private final RectF f10709v;

    static /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f10710a;

        static {
            int[] iArr = new int[b.values().length];
            f10710a = iArr;
            try {
                iArr[b.CLIPPING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f10710a[b.OVERLAY_COLOR.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public enum b {
        OVERLAY_COLOR,
        CLIPPING
    }

    public m(Drawable drawable) {
        super((Drawable) X.k.g(drawable));
        this.f10693f = b.OVERLAY_COLOR;
        this.f10694g = new RectF();
        this.f10697j = new float[8];
        this.f10698k = new float[8];
        this.f10699l = new Paint(1);
        this.f10700m = false;
        this.f10701n = 0.0f;
        this.f10702o = 0;
        this.f10703p = 0;
        this.f10704q = 0.0f;
        this.f10705r = false;
        this.f10706s = false;
        this.f10707t = new Path();
        this.f10708u = new Path();
        this.f10709v = new RectF();
    }

    private void z() {
        float[] fArr;
        this.f10707t.reset();
        this.f10708u.reset();
        this.f10709v.set(getBounds());
        RectF rectF = this.f10709v;
        float f3 = this.f10704q;
        rectF.inset(f3, f3);
        if (this.f10693f == b.OVERLAY_COLOR) {
            this.f10707t.addRect(this.f10709v, Path.Direction.CW);
        }
        if (this.f10700m) {
            this.f10707t.addCircle(this.f10709v.centerX(), this.f10709v.centerY(), Math.min(this.f10709v.width(), this.f10709v.height()) / 2.0f, Path.Direction.CW);
        } else {
            this.f10707t.addRoundRect(this.f10709v, this.f10697j, Path.Direction.CW);
        }
        RectF rectF2 = this.f10709v;
        float f4 = this.f10704q;
        rectF2.inset(-f4, -f4);
        RectF rectF3 = this.f10709v;
        float f5 = this.f10701n;
        rectF3.inset(f5 / 2.0f, f5 / 2.0f);
        if (this.f10700m) {
            this.f10708u.addCircle(this.f10709v.centerX(), this.f10709v.centerY(), Math.min(this.f10709v.width(), this.f10709v.height()) / 2.0f, Path.Direction.CW);
        } else {
            int i3 = 0;
            while (true) {
                fArr = this.f10698k;
                if (i3 >= fArr.length) {
                    break;
                }
                fArr[i3] = (this.f10697j[i3] + this.f10704q) - (this.f10701n / 2.0f);
                i3++;
            }
            this.f10708u.addRoundRect(this.f10709v, fArr, Path.Direction.CW);
        }
        RectF rectF4 = this.f10709v;
        float f6 = this.f10701n;
        rectF4.inset((-f6) / 2.0f, (-f6) / 2.0f);
    }

    @Override // t0.j
    public void c(int i3, float f3) {
        this.f10702o = i3;
        this.f10701n = f3;
        z();
        invalidateSelf();
    }

    @Override // t0.C0727g, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.f10694g.set(getBounds());
        int i3 = a.f10710a[this.f10693f.ordinal()];
        if (i3 == 1) {
            int iSave = canvas.save();
            canvas.clipPath(this.f10707t);
            super.draw(canvas);
            canvas.restoreToCount(iSave);
        } else if (i3 == 2) {
            if (this.f10705r) {
                RectF rectF = this.f10695h;
                if (rectF == null) {
                    this.f10695h = new RectF(this.f10694g);
                    this.f10696i = new Matrix();
                } else {
                    rectF.set(this.f10694g);
                }
                RectF rectF2 = this.f10695h;
                float f3 = this.f10701n;
                rectF2.inset(f3, f3);
                Matrix matrix = this.f10696i;
                if (matrix != null) {
                    matrix.setRectToRect(this.f10694g, this.f10695h, Matrix.ScaleToFit.FILL);
                }
                int iSave2 = canvas.save();
                canvas.clipRect(this.f10694g);
                canvas.concat(this.f10696i);
                super.draw(canvas);
                canvas.restoreToCount(iSave2);
            } else {
                super.draw(canvas);
            }
            this.f10699l.setStyle(Paint.Style.FILL);
            this.f10699l.setColor(this.f10703p);
            this.f10699l.setStrokeWidth(0.0f);
            this.f10699l.setFilterBitmap(x());
            this.f10707t.setFillType(Path.FillType.EVEN_ODD);
            canvas.drawPath(this.f10707t, this.f10699l);
            if (this.f10700m) {
                float fWidth = ((this.f10694g.width() - this.f10694g.height()) + this.f10701n) / 2.0f;
                float fHeight = ((this.f10694g.height() - this.f10694g.width()) + this.f10701n) / 2.0f;
                if (fWidth > 0.0f) {
                    RectF rectF3 = this.f10694g;
                    float f4 = rectF3.left;
                    canvas.drawRect(f4, rectF3.top, f4 + fWidth, rectF3.bottom, this.f10699l);
                    RectF rectF4 = this.f10694g;
                    float f5 = rectF4.right;
                    canvas.drawRect(f5 - fWidth, rectF4.top, f5, rectF4.bottom, this.f10699l);
                }
                if (fHeight > 0.0f) {
                    RectF rectF5 = this.f10694g;
                    float f6 = rectF5.left;
                    float f7 = rectF5.top;
                    canvas.drawRect(f6, f7, rectF5.right, f7 + fHeight, this.f10699l);
                    RectF rectF6 = this.f10694g;
                    float f8 = rectF6.left;
                    float f9 = rectF6.bottom;
                    canvas.drawRect(f8, f9 - fHeight, rectF6.right, f9, this.f10699l);
                }
            }
        }
        if (this.f10702o != 0) {
            this.f10699l.setStyle(Paint.Style.STROKE);
            this.f10699l.setColor(this.f10702o);
            this.f10699l.setStrokeWidth(this.f10701n);
            this.f10707t.setFillType(Path.FillType.EVEN_ODD);
            canvas.drawPath(this.f10708u, this.f10699l);
        }
    }

    @Override // t0.j
    public void h(boolean z3) {
        this.f10700m = z3;
        z();
        invalidateSelf();
    }

    @Override // t0.j
    public void i(float f3) {
        this.f10704q = f3;
        z();
        invalidateSelf();
    }

    @Override // t0.j
    public void m(float f3) {
        Arrays.fill(this.f10697j, f3);
        z();
        invalidateSelf();
    }

    @Override // t0.C0727g, android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        z();
    }

    @Override // t0.j
    public void p(boolean z3) {
        if (this.f10706s != z3) {
            this.f10706s = z3;
            invalidateSelf();
        }
    }

    @Override // t0.j
    public void s(boolean z3) {
        this.f10705r = z3;
        z();
        invalidateSelf();
    }

    @Override // t0.j
    public void t(float[] fArr) {
        if (fArr == null) {
            Arrays.fill(this.f10697j, 0.0f);
        } else {
            X.k.c(fArr.length == 8, "radii should have exactly 8 values");
            System.arraycopy(fArr, 0, this.f10697j, 0, 8);
        }
        z();
        invalidateSelf();
    }

    public boolean x() {
        return this.f10706s;
    }

    public void y(int i3) {
        this.f10703p = i3;
        invalidateSelf();
    }

    @Override // t0.j
    public void f(boolean z3) {
    }
}

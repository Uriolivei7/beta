package t0;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes.dex */
public final class p extends C0727g {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private r f10743f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public Object f10744g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public PointF f10745h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public int f10746i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public int f10747j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public Matrix f10748k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final Matrix f10749l;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public p(Drawable drawable, r rVar) {
        super(drawable);
        D2.h.f(rVar, "scaleType");
        this.f10749l = new Matrix();
        this.f10743f = rVar;
    }

    private final void y() {
        Drawable current = getCurrent();
        if (current == null) {
            return;
        }
        if (this.f10746i == current.getIntrinsicWidth() && this.f10747j == current.getIntrinsicHeight()) {
            return;
        }
        x();
    }

    public final r A() {
        return this.f10743f;
    }

    public final void B(PointF pointF) {
        if (X.i.a(this.f10745h, pointF)) {
            return;
        }
        if (pointF == null) {
            this.f10745h = null;
        } else {
            if (this.f10745h == null) {
                this.f10745h = new PointF();
            }
            PointF pointF2 = this.f10745h;
            D2.h.c(pointF2);
            pointF2.set(pointF);
        }
        x();
        invalidateSelf();
    }

    public final void C(r rVar) {
        D2.h.f(rVar, "scaleType");
        if (X.i.a(this.f10743f, rVar)) {
            return;
        }
        this.f10743f = rVar;
        this.f10744g = null;
        x();
        invalidateSelf();
    }

    @Override // t0.C0727g, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        D2.h.f(canvas, "canvas");
        y();
        if (this.f10748k == null) {
            super.draw(canvas);
            return;
        }
        int iSave = canvas.save();
        canvas.clipRect(getBounds());
        canvas.concat(this.f10748k);
        super.draw(canvas);
        canvas.restoreToCount(iSave);
    }

    @Override // t0.C0727g, t0.E
    public void n(Matrix matrix) {
        D2.h.f(matrix, "transform");
        u(matrix);
        y();
        Matrix matrix2 = this.f10748k;
        if (matrix2 != null) {
            matrix.preConcat(matrix2);
        }
    }

    @Override // t0.C0727g, android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        D2.h.f(rect, "bounds");
        x();
    }

    @Override // t0.C0727g
    public Drawable v(Drawable drawable) {
        Drawable drawableV = super.v(drawable);
        x();
        return drawableV;
    }

    public final void x() {
        float f3;
        float f4;
        Drawable current = getCurrent();
        if (current == null) {
            this.f10747j = 0;
            this.f10746i = 0;
            this.f10748k = null;
            return;
        }
        Rect bounds = getBounds();
        D2.h.e(bounds, "getBounds(...)");
        int iWidth = bounds.width();
        int iHeight = bounds.height();
        int intrinsicWidth = current.getIntrinsicWidth();
        this.f10746i = intrinsicWidth;
        int intrinsicHeight = current.getIntrinsicHeight();
        this.f10747j = intrinsicHeight;
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            current.setBounds(bounds);
            this.f10748k = null;
            return;
        }
        if (intrinsicWidth == iWidth && intrinsicHeight == iHeight) {
            current.setBounds(bounds);
            this.f10748k = null;
            return;
        }
        if (this.f10743f == r.f10750a) {
            current.setBounds(bounds);
            this.f10748k = null;
            return;
        }
        current.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        this.f10749l.reset();
        r rVar = this.f10743f;
        Matrix matrix = this.f10749l;
        PointF pointF = this.f10745h;
        if (pointF != null) {
            D2.h.c(pointF);
            f3 = pointF.x;
        } else {
            f3 = 0.5f;
        }
        PointF pointF2 = this.f10745h;
        if (pointF2 != null) {
            D2.h.c(pointF2);
            f4 = pointF2.y;
        } else {
            f4 = 0.5f;
        }
        rVar.a(matrix, bounds, intrinsicWidth, intrinsicHeight, f3, f4);
        this.f10748k = this.f10749l;
    }

    public final PointF z() {
        return this.f10745h;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public p(Drawable drawable, r rVar, PointF pointF) {
        super(drawable);
        D2.h.f(rVar, "scaleType");
        this.f10749l = new Matrix();
        this.f10743f = rVar;
        this.f10745h = pointF;
    }
}

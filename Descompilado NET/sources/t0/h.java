package t0;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes.dex */
public class h extends C0727g {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Matrix f10662f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private Matrix f10663g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f10664h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f10665i;

    public h(Drawable drawable, Matrix matrix) {
        super((Drawable) X.k.g(drawable));
        this.f10664h = 0;
        this.f10665i = 0;
        this.f10662f = matrix;
    }

    private void x() {
        Drawable current = getCurrent();
        if (current == null) {
            return;
        }
        Rect bounds = getBounds();
        int intrinsicWidth = current.getIntrinsicWidth();
        this.f10664h = intrinsicWidth;
        int intrinsicHeight = current.getIntrinsicHeight();
        this.f10665i = intrinsicHeight;
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            current.setBounds(bounds);
            this.f10663g = null;
        } else {
            current.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
            this.f10663g = this.f10662f;
        }
    }

    private void y() {
        Drawable current = getCurrent();
        if (current == null) {
            return;
        }
        if (this.f10664h == current.getIntrinsicWidth() && this.f10665i == current.getIntrinsicHeight()) {
            return;
        }
        x();
    }

    @Override // t0.C0727g, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        y();
        if (this.f10663g == null) {
            super.draw(canvas);
            return;
        }
        int iSave = canvas.save();
        canvas.clipRect(getBounds());
        canvas.concat(this.f10663g);
        super.draw(canvas);
        canvas.restoreToCount(iSave);
    }

    @Override // t0.C0727g, t0.E
    public void n(Matrix matrix) {
        super.n(matrix);
        Matrix matrix2 = this.f10663g;
        if (matrix2 != null) {
            matrix.preConcat(matrix2);
        }
    }

    @Override // t0.C0727g, android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        x();
    }

    @Override // t0.C0727g
    public Drawable v(Drawable drawable) {
        Drawable drawableV = super.v(drawable);
        x();
        return drawableV;
    }
}

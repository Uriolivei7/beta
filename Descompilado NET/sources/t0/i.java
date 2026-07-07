package t0;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class i extends C0727g {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final Matrix f10666f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final int f10667g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final int f10668h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final Matrix f10669i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final RectF f10670j;

    public i(Drawable drawable, int i3) {
        this(drawable, i3, 0, 4, null);
    }

    @Override // t0.C0727g, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        int i3;
        D2.h.f(canvas, "canvas");
        if (this.f10667g <= 0 && ((i3 = this.f10668h) == 0 || i3 == 1)) {
            super.draw(canvas);
            return;
        }
        int iSave = canvas.save();
        canvas.concat(this.f10666f);
        super.draw(canvas);
        canvas.restoreToCount(iSave);
    }

    @Override // t0.C0727g, android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        int i3 = this.f10668h;
        return (i3 == 5 || i3 == 7 || this.f10667g % 180 != 0) ? super.getIntrinsicWidth() : super.getIntrinsicHeight();
    }

    @Override // t0.C0727g, android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        int i3 = this.f10668h;
        return (i3 == 5 || i3 == 7 || this.f10667g % 180 != 0) ? super.getIntrinsicHeight() : super.getIntrinsicWidth();
    }

    @Override // t0.C0727g, t0.E
    public void n(Matrix matrix) {
        D2.h.f(matrix, "transform");
        u(matrix);
        if (this.f10666f.isIdentity()) {
            return;
        }
        matrix.preConcat(this.f10666f);
    }

    @Override // t0.C0727g, android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        int i3;
        D2.h.f(rect, "bounds");
        Drawable current = getCurrent();
        if (current == null) {
            return;
        }
        int i4 = this.f10667g;
        if (i4 <= 0 && ((i3 = this.f10668h) == 0 || i3 == 1)) {
            current.setBounds(rect);
            return;
        }
        int i5 = this.f10668h;
        if (i5 == 2) {
            this.f10666f.setScale(-1.0f, 1.0f);
        } else if (i5 == 7) {
            this.f10666f.setRotate(270.0f, rect.centerX(), rect.centerY());
            this.f10666f.postScale(-1.0f, 1.0f);
        } else if (i5 == 4) {
            this.f10666f.setScale(1.0f, -1.0f);
        } else if (i5 != 5) {
            this.f10666f.setRotate(i4, rect.centerX(), rect.centerY());
        } else {
            this.f10666f.setRotate(270.0f, rect.centerX(), rect.centerY());
            this.f10666f.postScale(1.0f, -1.0f);
        }
        this.f10669i.reset();
        this.f10666f.invert(this.f10669i);
        this.f10670j.set(rect);
        this.f10669i.mapRect(this.f10670j);
        RectF rectF = this.f10670j;
        current.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
    }

    public /* synthetic */ i(Drawable drawable, int i3, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this(drawable, i3, (i5 & 4) != 0 ? 0 : i4);
    }

    public i(Drawable drawable, int i3, int i4) {
        super(drawable);
        this.f10666f = new Matrix();
        this.f10667g = i3 - (i3 % 90);
        this.f10668h = (i4 < 0 || i4 > 8) ? 0 : i4;
        this.f10669i = new Matrix();
        this.f10670j = new RectF();
    }
}

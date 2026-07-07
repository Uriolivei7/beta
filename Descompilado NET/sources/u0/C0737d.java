package u0;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import t0.C0727g;
import t0.F;
import t0.G;

/* JADX INFO: renamed from: u0.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0737d extends C0727g implements F {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    Drawable f10831f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private G f10832g;

    public C0737d(Drawable drawable) {
        super(drawable);
        this.f10831f = null;
    }

    @Override // t0.C0727g, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (isVisible()) {
            G g3 = this.f10832g;
            if (g3 != null) {
                g3.onDraw();
            }
            super.draw(canvas);
            Drawable drawable = this.f10831f;
            if (drawable != null) {
                drawable.setBounds(getBounds());
                this.f10831f.draw(canvas);
            }
        }
    }

    @Override // t0.F
    public void e(G g3) {
        this.f10832g = g3;
    }

    @Override // t0.C0727g, android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return -1;
    }

    @Override // t0.C0727g, android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return -1;
    }

    @Override // t0.C0727g, android.graphics.drawable.Drawable
    public boolean setVisible(boolean z3, boolean z4) {
        G g3 = this.f10832g;
        if (g3 != null) {
            g3.i(z3);
        }
        return super.setVisible(z3, z4);
    }

    public void x(Drawable drawable) {
        this.f10831f = drawable;
        invalidateSelf();
    }
}

package t0;

import android.graphics.Canvas;
import android.graphics.drawable.NinePatchDrawable;

/* JADX INFO: loaded from: classes.dex */
public final class o extends n {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public o(NinePatchDrawable ninePatchDrawable) {
        super(ninePatchDrawable);
        D2.h.f(ninePatchDrawable, "ninePatchDrawable");
    }

    @Override // t0.n, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        D2.h.f(canvas, "canvas");
        if (V0.b.d()) {
            V0.b.a("RoundedNinePatchDrawable#draw");
        }
        if (!e()) {
            super.draw(canvas);
            if (V0.b.d()) {
                V0.b.b();
                return;
            }
            return;
        }
        j();
        g();
        canvas.clipPath(this.f10722f);
        super.draw(canvas);
        if (V0.b.d()) {
            V0.b.b();
        }
    }
}

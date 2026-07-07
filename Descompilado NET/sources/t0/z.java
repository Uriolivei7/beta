package t0;

import android.graphics.Matrix;
import android.graphics.Rect;

/* JADX INFO: loaded from: classes.dex */
class z extends q {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final r f10768l = new z();

    private z() {
    }

    @Override // t0.q
    public void b(Matrix matrix, Rect rect, int i3, int i4, float f3, float f4, float f5, float f6) {
        float f7 = rect.left;
        float fHeight = rect.top + ((rect.height() - (i4 * f5)) * 0.5f);
        matrix.setScale(f5, f5);
        matrix.postTranslate((int) (f7 + 0.5f), (int) (fHeight + 0.5f));
    }

    public String toString() {
        return "fit_x";
    }
}

package t0;

import android.graphics.Matrix;
import android.graphics.Rect;

/* JADX INFO: loaded from: classes.dex */
class v extends q {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final r f10764l = new v();

    private v() {
    }

    @Override // t0.q
    public void b(Matrix matrix, Rect rect, int i3, int i4, float f3, float f4, float f5, float f6) {
        float fMin = Math.min(f5, f6);
        float f7 = rect.left;
        float fHeight = rect.top + (rect.height() - (i4 * fMin));
        matrix.setScale(fMin, fMin);
        matrix.postTranslate((int) (f7 + 0.5f), (int) (fHeight + 0.5f));
    }

    public String toString() {
        return "fit_bottom_start";
    }
}

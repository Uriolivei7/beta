package t0;

import android.graphics.Matrix;
import android.graphics.Rect;

/* JADX INFO: loaded from: classes.dex */
class B extends q {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final r f10619l = new B();

    private B() {
    }

    @Override // t0.q
    public void b(Matrix matrix, Rect rect, int i3, int i4, float f3, float f4, float f5, float f6) {
        float fWidth = rect.left + ((rect.width() - (i3 * f6)) * 0.5f);
        float f7 = rect.top;
        matrix.setScale(f6, f6);
        matrix.postTranslate((int) (fWidth + 0.5f), (int) (f7 + 0.5f));
    }

    public String toString() {
        return "fit_y";
    }
}

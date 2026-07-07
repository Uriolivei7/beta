package t0;

import android.graphics.Matrix;
import android.graphics.Rect;

/* JADX INFO: loaded from: classes.dex */
class t extends q {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final r f10762l = new t();

    private t() {
    }

    @Override // t0.q
    public void b(Matrix matrix, Rect rect, int i3, int i4, float f3, float f4, float f5, float f6) {
        float fHeight;
        float fWidth;
        if (f6 > f5) {
            fWidth = rect.left + ((rect.width() - (i3 * f6)) * 0.5f);
            fHeight = rect.top;
            f5 = f6;
        } else {
            float f7 = rect.left;
            fHeight = ((rect.height() - (i4 * f5)) * 0.5f) + rect.top;
            fWidth = f7;
        }
        matrix.setScale(f5, f5);
        matrix.postTranslate((int) (fWidth + 0.5f), (int) (fHeight + 0.5f));
    }

    public String toString() {
        return "center_crop";
    }
}

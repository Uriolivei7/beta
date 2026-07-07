package t0;

import android.graphics.Matrix;
import android.graphics.Rect;

/* JADX INFO: loaded from: classes.dex */
class C extends q {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final r f10620l = new C();

    private C() {
    }

    @Override // t0.q
    public void b(Matrix matrix, Rect rect, int i3, int i4, float f3, float f4, float f5, float f6) {
        float fMax;
        float fMax2;
        if (f6 > f5) {
            float f7 = i3 * f6;
            fMax = rect.left + Math.max(Math.min((rect.width() * 0.5f) - (f3 * f7), 0.0f), rect.width() - f7);
            fMax2 = rect.top;
            f5 = f6;
        } else {
            fMax = rect.left;
            float f8 = i4 * f5;
            fMax2 = Math.max(Math.min((rect.height() * 0.5f) - (f4 * f8), 0.0f), rect.height() - f8) + rect.top;
        }
        matrix.setScale(f5, f5);
        matrix.postTranslate((int) (fMax + 0.5f), (int) (fMax2 + 0.5f));
    }

    public String toString() {
        return "focus_crop";
    }
}

package t0;

import android.graphics.Matrix;
import android.graphics.Rect;

/* JADX INFO: renamed from: t0.A, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0720A extends q {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final r f10618l = new C0720A();

    private C0720A() {
    }

    @Override // t0.q
    public void b(Matrix matrix, Rect rect, int i3, int i4, float f3, float f4, float f5, float f6) {
        float f7 = rect.left;
        float f8 = rect.top;
        matrix.setScale(f5, f6);
        matrix.postTranslate((int) (f7 + 0.5f), (int) (f8 + 0.5f));
    }

    public String toString() {
        return "fit_xy";
    }
}

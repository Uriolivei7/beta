package t0;

import android.graphics.Matrix;
import android.graphics.Rect;

/* JADX INFO: loaded from: classes.dex */
class s extends q {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final r f10761l = new s();

    private s() {
    }

    @Override // t0.q
    public void b(Matrix matrix, Rect rect, int i3, int i4, float f3, float f4, float f5, float f6) {
        matrix.setTranslate((int) (rect.left + ((rect.width() - i3) * 0.5f) + 0.5f), (int) (rect.top + ((rect.height() - i4) * 0.5f) + 0.5f));
    }

    public String toString() {
        return "center";
    }
}

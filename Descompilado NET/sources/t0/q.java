package t0;

import android.graphics.Matrix;
import android.graphics.Rect;

/* JADX INFO: loaded from: classes.dex */
public abstract class q implements r {
    @Override // t0.r
    public Matrix a(Matrix matrix, Rect rect, int i3, int i4, float f3, float f4) {
        b(matrix, rect, i3, i4, f3, f4, rect.width() / i3, rect.height() / i4);
        return matrix;
    }

    public abstract void b(Matrix matrix, Rect rect, int i3, int i4, float f3, float f4, float f5, float f6);
}

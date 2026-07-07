package G0;

import android.graphics.Bitmap;
import b0.AbstractC0306a;

/* JADX INFO: loaded from: classes.dex */
public abstract class b {
    public AbstractC0306a a(int i3, int i4) {
        return b(i3, i4, Bitmap.Config.ARGB_8888);
    }

    public AbstractC0306a b(int i3, int i4, Bitmap.Config config) {
        return c(i3, i4, config, null);
    }

    public AbstractC0306a c(int i3, int i4, Bitmap.Config config, Object obj) {
        return d(i3, i4, config);
    }

    public abstract AbstractC0306a d(int i3, int i4, Bitmap.Config config);
}

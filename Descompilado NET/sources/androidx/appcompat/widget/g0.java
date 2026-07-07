package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
class g0 extends Y {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final WeakReference f4222b;

    public g0(Context context, Resources resources) {
        super(resources);
        this.f4222b = new WeakReference(context);
    }

    @Override // androidx.appcompat.widget.Y, android.content.res.Resources
    public Drawable getDrawable(int i3) {
        Drawable drawableA = a(i3);
        Context context = (Context) this.f4222b.get();
        if (drawableA != null && context != null) {
            X.g().w(context, i3, drawableA);
        }
        return drawableA;
    }
}

package x0;

import android.content.Context;
import android.util.AttributeSet;
import u0.C0734a;
import u0.C0735b;
import u0.C0736c;

/* JADX INFO: loaded from: classes.dex */
public class d extends c {
    public d(Context context, C0734a c0734a) {
        super(context);
        setHierarchy(c0734a);
    }

    protected void g(Context context, AttributeSet attributeSet) throws Throwable {
        if (V0.b.d()) {
            V0.b.a("GenericDraweeView#inflateHierarchy");
        }
        C0735b c0735bD = C0736c.d(context, attributeSet);
        setAspectRatio(c0735bD.f());
        setHierarchy(c0735bD.a());
        if (V0.b.d()) {
            V0.b.b();
        }
    }

    public d(Context context) throws Throwable {
        super(context);
        g(context, null);
    }

    public d(Context context, AttributeSet attributeSet) throws Throwable {
        super(context, attributeSet);
        g(context, attributeSet);
    }

    public d(Context context, AttributeSet attributeSet, int i3) throws Throwable {
        super(context, attributeSet, i3);
        g(context, attributeSet);
    }

    public d(Context context, AttributeSet attributeSet, int i3, int i4) throws Throwable {
        super(context, attributeSet, i3, i4);
        g(context, attributeSet);
    }
}

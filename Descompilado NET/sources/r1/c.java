package R1;

import android.content.Context;
import android.graphics.RectF;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Float[] f1998a = new Float[n.values().length];

    public final RectF a(int i3, Context context) {
        RectF rectF;
        D2.h.f(context, "context");
        if (i3 == 0) {
            Float f3 = this.f1998a[n.f2080h.ordinal()];
            float fFloatValue = (f3 == null && (f3 = this.f1998a[n.f2076d.ordinal()]) == null && (f3 = this.f1998a[n.f2082j.ordinal()]) == null && (f3 = this.f1998a[n.f2075c.ordinal()]) == null) ? 0.0f : f3.floatValue();
            Float f4 = this.f1998a[n.f2084l.ordinal()];
            float fFloatValue2 = (f4 == null && (f4 = this.f1998a[n.f2078f.ordinal()]) == null && (f4 = this.f1998a[n.f2086n.ordinal()]) == null && (f4 = this.f1998a[n.f2083k.ordinal()]) == null && (f4 = this.f1998a[n.f2075c.ordinal()]) == null) ? 0.0f : f4.floatValue();
            Float f5 = this.f1998a[n.f2081i.ordinal()];
            float fFloatValue3 = (f5 == null && (f5 = this.f1998a[n.f2077e.ordinal()]) == null && (f5 = this.f1998a[n.f2082j.ordinal()]) == null && (f5 = this.f1998a[n.f2075c.ordinal()]) == null) ? 0.0f : f5.floatValue();
            Float f6 = this.f1998a[n.f2085m.ordinal()];
            rectF = new RectF(fFloatValue, fFloatValue2, fFloatValue3, (f6 == null && (f6 = this.f1998a[n.f2079g.ordinal()]) == null && (f6 = this.f1998a[n.f2086n.ordinal()]) == null && (f6 = this.f1998a[n.f2083k.ordinal()]) == null && (f6 = this.f1998a[n.f2075c.ordinal()]) == null) ? 0.0f : f6.floatValue());
        } else {
            if (i3 != 1) {
                throw new IllegalArgumentException("Expected resolved layout direction");
            }
            if (com.facebook.react.modules.i18nmanager.a.f6977a.a().d(context)) {
                Float f7 = this.f1998a[n.f2081i.ordinal()];
                float fFloatValue4 = (f7 == null && (f7 = this.f1998a[n.f2077e.ordinal()]) == null && (f7 = this.f1998a[n.f2082j.ordinal()]) == null && (f7 = this.f1998a[n.f2075c.ordinal()]) == null) ? 0.0f : f7.floatValue();
                Float f8 = this.f1998a[n.f2084l.ordinal()];
                float fFloatValue5 = (f8 == null && (f8 = this.f1998a[n.f2078f.ordinal()]) == null && (f8 = this.f1998a[n.f2086n.ordinal()]) == null && (f8 = this.f1998a[n.f2083k.ordinal()]) == null && (f8 = this.f1998a[n.f2075c.ordinal()]) == null) ? 0.0f : f8.floatValue();
                Float f9 = this.f1998a[n.f2080h.ordinal()];
                float fFloatValue6 = (f9 == null && (f9 = this.f1998a[n.f2076d.ordinal()]) == null && (f9 = this.f1998a[n.f2082j.ordinal()]) == null && (f9 = this.f1998a[n.f2075c.ordinal()]) == null) ? 0.0f : f9.floatValue();
                Float f10 = this.f1998a[n.f2085m.ordinal()];
                rectF = new RectF(fFloatValue4, fFloatValue5, fFloatValue6, (f10 == null && (f10 = this.f1998a[n.f2079g.ordinal()]) == null && (f10 = this.f1998a[n.f2086n.ordinal()]) == null && (f10 = this.f1998a[n.f2083k.ordinal()]) == null && (f10 = this.f1998a[n.f2075c.ordinal()]) == null) ? 0.0f : f10.floatValue());
            } else {
                Float f11 = this.f1998a[n.f2081i.ordinal()];
                float fFloatValue7 = (f11 == null && (f11 = this.f1998a[n.f2076d.ordinal()]) == null && (f11 = this.f1998a[n.f2082j.ordinal()]) == null && (f11 = this.f1998a[n.f2075c.ordinal()]) == null) ? 0.0f : f11.floatValue();
                Float f12 = this.f1998a[n.f2084l.ordinal()];
                float fFloatValue8 = (f12 == null && (f12 = this.f1998a[n.f2078f.ordinal()]) == null && (f12 = this.f1998a[n.f2086n.ordinal()]) == null && (f12 = this.f1998a[n.f2083k.ordinal()]) == null && (f12 = this.f1998a[n.f2075c.ordinal()]) == null) ? 0.0f : f12.floatValue();
                Float f13 = this.f1998a[n.f2080h.ordinal()];
                float fFloatValue9 = (f13 == null && (f13 = this.f1998a[n.f2077e.ordinal()]) == null && (f13 = this.f1998a[n.f2082j.ordinal()]) == null && (f13 = this.f1998a[n.f2075c.ordinal()]) == null) ? 0.0f : f13.floatValue();
                Float f14 = this.f1998a[n.f2085m.ordinal()];
                rectF = new RectF(fFloatValue7, fFloatValue8, fFloatValue9, (f14 == null && (f14 = this.f1998a[n.f2079g.ordinal()]) == null && (f14 = this.f1998a[n.f2086n.ordinal()]) == null && (f14 = this.f1998a[n.f2083k.ordinal()]) == null && (f14 = this.f1998a[n.f2075c.ordinal()]) == null) ? 0.0f : f14.floatValue());
            }
        }
        return rectF;
    }

    public final void b(n nVar, Float f3) {
        D2.h.f(nVar, "edge");
        this.f1998a[nVar.ordinal()] = f3;
    }
}

package u0;

import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import t0.C0727g;
import t0.InterfaceC0723c;
import t0.j;
import t0.k;
import t0.l;
import t0.m;
import t0.o;
import t0.p;
import t0.r;
import u0.e;

/* JADX INFO: loaded from: classes.dex */
public class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final Drawable f10846a = new ColorDrawable(0);

    private static Drawable a(Drawable drawable, e eVar, Resources resources) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            k kVar = new k(resources, bitmapDrawable.getBitmap(), bitmapDrawable.getPaint(), eVar.i());
            b(kVar, eVar);
            return kVar;
        }
        if (drawable instanceof NinePatchDrawable) {
            o oVar = new o((NinePatchDrawable) drawable);
            b(oVar, eVar);
            return oVar;
        }
        if (!(drawable instanceof ColorDrawable)) {
            Y.a.K("WrappingUtils", "Don't know how to round that drawable: %s", drawable);
            return drawable;
        }
        l lVarA = l.a((ColorDrawable) drawable);
        b(lVarA, eVar);
        return lVarA;
    }

    static void b(j jVar, e eVar) {
        jVar.h(eVar.j());
        jVar.t(eVar.d());
        jVar.c(eVar.b(), eVar.c());
        jVar.i(eVar.g());
        jVar.s(eVar.l());
        jVar.p(eVar.h());
        jVar.f(eVar.i());
    }

    static InterfaceC0723c c(InterfaceC0723c interfaceC0723c) {
        while (true) {
            Object objQ = interfaceC0723c.q();
            if (objQ == interfaceC0723c || !(objQ instanceof InterfaceC0723c)) {
                break;
            }
            interfaceC0723c = (InterfaceC0723c) objQ;
        }
        return interfaceC0723c;
    }

    static Drawable d(Drawable drawable, e eVar, Resources resources) {
        try {
            if (V0.b.d()) {
                V0.b.a("WrappingUtils#maybeApplyLeafRounding");
            }
            if (drawable != null && eVar != null && eVar.k() == e.a.BITMAP_ONLY) {
                if (!(drawable instanceof C0727g)) {
                    Drawable drawableA = a(drawable, eVar, resources);
                    if (V0.b.d()) {
                        V0.b.b();
                    }
                    return drawableA;
                }
                InterfaceC0723c interfaceC0723cC = c((C0727g) drawable);
                interfaceC0723cC.d(a(interfaceC0723cC.d(f10846a), eVar, resources));
                if (V0.b.d()) {
                    V0.b.b();
                }
                return drawable;
            }
            return drawable;
        } finally {
            if (V0.b.d()) {
                V0.b.b();
            }
        }
    }

    static Drawable e(Drawable drawable, e eVar) {
        try {
            if (V0.b.d()) {
                V0.b.a("WrappingUtils#maybeWrapWithRoundedOverlayColor");
            }
            if (drawable != null && eVar != null && eVar.k() == e.a.OVERLAY_COLOR) {
                m mVar = new m(drawable);
                b(mVar, eVar);
                mVar.y(eVar.f());
                if (V0.b.d()) {
                    V0.b.b();
                }
                return mVar;
            }
            return drawable;
        } finally {
            if (V0.b.d()) {
                V0.b.b();
            }
        }
    }

    static Drawable f(Drawable drawable, r rVar) {
        return g(drawable, rVar, null);
    }

    static Drawable g(Drawable drawable, r rVar, PointF pointF) {
        if (V0.b.d()) {
            V0.b.a("WrappingUtils#maybeWrapWithScaleType");
        }
        if (drawable == null || rVar == null) {
            if (V0.b.d()) {
                V0.b.b();
            }
            return drawable;
        }
        p pVar = new p(drawable, rVar);
        if (pointF != null) {
            pVar.B(pointF);
        }
        if (V0.b.d()) {
            V0.b.b();
        }
        return pVar;
    }

    static void h(j jVar) {
        jVar.h(false);
        jVar.m(0.0f);
        jVar.c(0, 0.0f);
        jVar.i(0.0f);
        jVar.s(false);
        jVar.p(false);
        jVar.f(k.k());
    }

    /* JADX WARN: Multi-variable type inference failed */
    static void i(InterfaceC0723c interfaceC0723c, e eVar, Resources resources) {
        InterfaceC0723c interfaceC0723cC = c(interfaceC0723c);
        Drawable drawableQ = interfaceC0723cC.q();
        if (eVar == null || eVar.k() != e.a.BITMAP_ONLY) {
            if (drawableQ instanceof j) {
                h((j) drawableQ);
            }
        } else if (drawableQ instanceof j) {
            b((j) drawableQ, eVar);
        } else if (drawableQ != 0) {
            interfaceC0723cC.d(f10846a);
            interfaceC0723cC.d(a(drawableQ, eVar, resources));
        }
    }

    static void j(InterfaceC0723c interfaceC0723c, e eVar) {
        Drawable drawableQ = interfaceC0723c.q();
        if (eVar == null || eVar.k() != e.a.OVERLAY_COLOR) {
            if (drawableQ instanceof m) {
                Drawable drawable = f10846a;
                interfaceC0723c.d(((m) drawableQ).v(drawable));
                drawable.setCallback(null);
                return;
            }
            return;
        }
        if (!(drawableQ instanceof m)) {
            interfaceC0723c.d(e(interfaceC0723c.d(f10846a), eVar));
            return;
        }
        m mVar = (m) drawableQ;
        b(mVar, eVar);
        mVar.y(eVar.f());
    }

    static p k(InterfaceC0723c interfaceC0723c, r rVar) {
        Drawable drawableF = f(interfaceC0723c.d(f10846a), rVar);
        interfaceC0723c.d(drawableF);
        X.k.h(drawableF, "Parent has no child drawable!");
        return (p) drawableF;
    }
}

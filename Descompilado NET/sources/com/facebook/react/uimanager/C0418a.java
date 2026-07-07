package com.facebook.react.uimanager;

import R1.g;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import r1.C0670b;
import s2.AbstractC0717n;

/* JADX INFO: renamed from: com.facebook.react.uimanager.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0418a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0418a f7440a = new C0418a();

    private C0418a() {
    }

    public static final void a(View view, Canvas canvas) {
        RectF rectFA;
        float fB;
        float fB2;
        float fB3;
        D2.h.f(view, "view");
        D2.h.f(canvas, "canvas");
        if (!C0670b.h()) {
            Rect rect = new Rect();
            view.getDrawingRect(rect);
            O1.e eVarJ = f7440a.j(view);
            if (eVarJ == null) {
                canvas.clipRect(rect);
                return;
            }
            Path pathP = eVarJ.p();
            if (pathP != null) {
                pathP.offset(rect.left, rect.top);
                canvas.clipPath(pathP);
                return;
            } else {
                RectF rectFQ = eVarJ.q();
                D2.h.e(rectFQ, "getPaddingBoxRect(...)");
                rectFQ.offset(rect.left, rect.top);
                canvas.clipRect(rectFQ);
                return;
            }
        }
        view.getDrawingRect(new Rect());
        C0418a c0418a = f7440a;
        O1.g gVarF = c0418a.f(view);
        RectF rectF = new RectF();
        R1.c cVarC = gVarF.c();
        if (cVarC != null) {
            int layoutDirection = gVarF.getLayoutDirection();
            Context context = view.getContext();
            D2.h.e(context, "getContext(...)");
            rectFA = cVarC.a(layoutDirection, context);
        } else {
            rectFA = null;
        }
        float f3 = gVarF.getBounds().left;
        float fB4 = 0.0f;
        if (rectFA != null) {
            fB = C0429f0.f7476a.b(rectFA.left);
        } else {
            fB = 0.0f;
        }
        rectF.left = f3 + fB;
        float f4 = gVarF.getBounds().top;
        if (rectFA != null) {
            fB2 = C0429f0.f7476a.b(rectFA.top);
        } else {
            fB2 = 0.0f;
        }
        rectF.top = f4 + fB2;
        float f5 = gVarF.getBounds().right;
        if (rectFA != null) {
            fB3 = C0429f0.f7476a.b(rectFA.right);
        } else {
            fB3 = 0.0f;
        }
        rectF.right = f5 - fB3;
        float f6 = gVarF.getBounds().bottom;
        if (rectFA != null) {
            fB4 = C0429f0.f7476a.b(rectFA.bottom);
        }
        rectF.bottom = f6 - fB4;
        R1.e eVarD = gVarF.d();
        if (eVarD == null || !eVarD.c()) {
            rectF.offset(r0.left, r0.top);
            canvas.clipRect(rectF);
        } else {
            Path pathB = c0418a.b(view, gVarF, rectF, rectFA);
            pathB.offset(r0.left, r0.top);
            canvas.clipPath(pathB);
        }
    }

    private final Path b(View view, O1.g gVar, RectF rectF, RectF rectF2) {
        R1.j jVarD;
        R1.k kVarA;
        R1.k kVarA2;
        R1.k kVarB;
        R1.k kVarB2;
        R1.k kVarD;
        R1.k kVarD2;
        R1.k kVarC;
        R1.k kVarC2;
        R1.e eVarD = gVar.d();
        if (eVarD != null) {
            int layoutDirection = gVar.getLayoutDirection();
            Context context = view.getContext();
            D2.h.e(context, "getContext(...)");
            jVarD = eVarD.d(layoutDirection, context, C0429f0.f(gVar.getBounds().width()), C0429f0.f(gVar.getBounds().height()));
        } else {
            jVarD = null;
        }
        Path path = new Path();
        path.addRoundRect(rectF, new float[]{l((jVarD == null || (kVarC2 = jVarD.c()) == null) ? null : Float.valueOf(C0429f0.f7476a.b(kVarC2.a())), rectF2 != null ? Float.valueOf(C0429f0.f7476a.b(rectF2.left)) : null), l((jVarD == null || (kVarC = jVarD.c()) == null) ? null : Float.valueOf(C0429f0.f7476a.b(kVarC.b())), rectF2 != null ? Float.valueOf(C0429f0.f7476a.b(rectF2.top)) : null), l((jVarD == null || (kVarD2 = jVarD.d()) == null) ? null : Float.valueOf(C0429f0.f7476a.b(kVarD2.a())), rectF2 != null ? Float.valueOf(C0429f0.f7476a.b(rectF2.right)) : null), l((jVarD == null || (kVarD = jVarD.d()) == null) ? null : Float.valueOf(C0429f0.f7476a.b(kVarD.b())), rectF2 != null ? Float.valueOf(C0429f0.f7476a.b(rectF2.top)) : null), l((jVarD == null || (kVarB2 = jVarD.b()) == null) ? null : Float.valueOf(C0429f0.f7476a.b(kVarB2.a())), rectF2 != null ? Float.valueOf(C0429f0.f7476a.b(rectF2.right)) : null), l((jVarD == null || (kVarB = jVarD.b()) == null) ? null : Float.valueOf(C0429f0.f7476a.b(kVarB.b())), rectF2 != null ? Float.valueOf(C0429f0.f7476a.b(rectF2.bottom)) : null), l((jVarD == null || (kVarA2 = jVarD.a()) == null) ? null : Float.valueOf(C0429f0.f7476a.b(kVarA2.a())), rectF2 != null ? Float.valueOf(C0429f0.f7476a.b(rectF2.left)) : null), l((jVarD == null || (kVarA = jVarD.a()) == null) ? null : Float.valueOf(C0429f0.f7476a.b(kVarA.b())), rectF2 != null ? Float.valueOf(C0429f0.f7476a.b(rectF2.bottom)) : null)}, Path.Direction.CW);
        return path;
    }

    private final O1.a c(View view) {
        O1.g gVarF = f(view);
        O1.a aVarA = gVarF.a();
        if (aVarA != null) {
            return aVarA;
        }
        Context context = view.getContext();
        D2.h.e(context, "getContext(...)");
        O1.a aVar = new O1.a(context, gVarF.d(), gVarF.c());
        view.setBackground(gVarF.l(aVar));
        return aVar;
    }

    private final O1.c d(View view) {
        O1.g gVarF = f(view);
        O1.c cVarB = gVarF.b();
        if (cVarB != null) {
            return cVarB;
        }
        Context context = view.getContext();
        D2.h.e(context, "getContext(...)");
        R1.e eVarD = gVarF.d();
        O1.c cVar = new O1.c(context, new C0468z0(0.0f), eVarD, gVarF.c(), R1.f.f2029c);
        view.setBackground(gVarF.m(cVar));
        return cVar;
    }

    private final O1.e e(View view) {
        O1.g gVarF = f(view);
        O1.e eVarE = gVarF.e();
        if (eVarE != null) {
            return eVarE;
        }
        O1.e eVar = new O1.e(view.getContext());
        view.setBackground(gVarF.n(eVar));
        return eVar;
    }

    private final O1.g f(View view) {
        if (view.getBackground() instanceof O1.g) {
            Drawable background = view.getBackground();
            D2.h.d(background, "null cannot be cast to non-null type com.facebook.react.uimanager.drawable.CompositeBackgroundDrawable");
            return (O1.g) background;
        }
        Context context = view.getContext();
        D2.h.e(context, "getContext(...)");
        O1.g gVar = new O1.g(context, view.getBackground(), null, null, null, null, null, null, null, null, null, 2044, null);
        view.setBackground(gVar);
        return gVar;
    }

    private final O1.k g(View view) {
        O1.g gVarF = f(view);
        O1.k kVarI = gVarF.i();
        if (kVarI != null) {
            return kVarI;
        }
        R1.e eVarD = C0670b.h() ? gVarF.d() : e(view).h();
        Context context = view.getContext();
        D2.h.e(context, "getContext(...)");
        O1.k kVar = new O1.k(context, eVarD, -16777216, 0.0f, R1.o.f2090c, 0.0f);
        view.setBackground(gVarF.p(kVar));
        return kVar;
    }

    private final O1.a h(View view) {
        O1.g gVarK = k(view);
        if (gVarK != null) {
            return gVarK.a();
        }
        return null;
    }

    public static final Integer i(View view) {
        D2.h.f(view, "view");
        if (C0670b.h()) {
            O1.a aVarH = f7440a.h(view);
            if (aVarH != null) {
                return Integer.valueOf(aVarH.b());
            }
            return null;
        }
        O1.e eVarJ = f7440a.j(view);
        if (eVarJ != null) {
            return Integer.valueOf(eVarJ.k());
        }
        return null;
    }

    private final O1.e j(View view) {
        O1.g gVarK = k(view);
        if (gVarK != null) {
            return gVarK.e();
        }
        return null;
    }

    private final O1.g k(View view) {
        Drawable background = view.getBackground();
        if (background instanceof O1.g) {
            return (O1.g) background;
        }
        return null;
    }

    private final float l(Float f3, Float f4) {
        return H2.d.b((f3 != null ? f3.floatValue() : 0.0f) - (f4 != null ? f4.floatValue() : 0.0f), 0.0f);
    }

    public static final void m(View view) {
        D2.h.f(view, "view");
        if (view.getBackground() instanceof O1.g) {
            Drawable background = view.getBackground();
            D2.h.d(background, "null cannot be cast to non-null type com.facebook.react.uimanager.drawable.CompositeBackgroundDrawable");
            view.setBackground(((O1.g) background).g());
        }
    }

    public static final void n(View view, Integer num) {
        D2.h.f(view, "view");
        if ((num == null || num.intValue() == 0) && !(view.getBackground() instanceof O1.g)) {
            return;
        }
        if (C0670b.h()) {
            f7440a.c(view).d(num != null ? num.intValue() : 0);
        } else {
            f7440a.e(view).C(num != null ? num.intValue() : 0);
        }
    }

    public static final void o(View view, List list) {
        D2.h.f(view, "view");
        if (C0670b.h()) {
            f7440a.c(view).e(list);
        } else {
            f7440a.e(view).v(list);
        }
    }

    public static final void p(View view, R1.n nVar, Integer num) {
        D2.h.f(view, "view");
        D2.h.f(nVar, "edge");
        if (C0670b.h()) {
            f7440a.d(view).o(nVar, num);
        } else {
            f7440a.e(view).x(nVar.b(), num);
        }
    }

    public static final void q(View view, R1.d dVar, W w3) {
        D2.h.f(view, "view");
        D2.h.f(dVar, "corner");
        C0418a c0418a = f7440a;
        O1.g gVarF = c0418a.f(view);
        R1.e eVarD = gVarF.d();
        if (eVarD == null) {
            eVarD = new R1.e(null, null, null, null, null, null, null, null, null, null, null, null, null, 8191, null);
        }
        gVarF.k(eVarD);
        R1.e eVarD2 = gVarF.d();
        if (eVarD2 != null) {
            eVarD2.e(dVar, w3);
        }
        if (C0670b.h()) {
            if (view instanceof ImageView) {
                c0418a.c(view);
            }
            O1.a aVarA = gVarF.a();
            if (aVarA != null) {
                aVarA.g(gVarF.d());
            }
            O1.c cVarB = gVarF.b();
            if (cVarB != null) {
                cVarB.q(gVarF.d());
            }
            O1.a aVarA2 = gVarF.a();
            if (aVarA2 != null) {
                aVarA2.invalidateSelf();
            }
            O1.c cVarB2 = gVarF.b();
            if (cVarB2 != null) {
                cVarB2.invalidateSelf();
            }
        } else {
            c0418a.e(view).z(dVar, w3);
        }
        if (Build.VERSION.SDK_INT >= 28) {
            List listH = gVarF.h();
            ArrayList arrayList = new ArrayList();
            for (Object obj : listH) {
                if (obj instanceof O1.m) {
                    arrayList.add(obj);
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((O1.m) it.next()).c(gVarF.d());
            }
        }
        if (Build.VERSION.SDK_INT >= 29) {
            List listF = gVarF.f();
            ArrayList arrayList2 = new ArrayList();
            for (Object obj2 : listF) {
                if (obj2 instanceof O1.i) {
                    arrayList2.add(obj2);
                }
            }
            Iterator it2 = arrayList2.iterator();
            while (it2.hasNext()) {
                ((O1.i) it2.next()).e(gVarF.d());
            }
        }
        O1.k kVarI = gVarF.i();
        if (kVarI != null) {
            kVarI.e(gVarF.d());
        }
        gVarF.invalidateSelf();
    }

    public static final void r(View view, R1.f fVar) {
        D2.h.f(view, "view");
        if (C0670b.h()) {
            f7440a.d(view).r(fVar);
        } else {
            f7440a.e(view).A(fVar);
        }
    }

    public static final void s(View view, R1.n nVar, Float f3) {
        D2.h.f(view, "view");
        D2.h.f(nVar, "edge");
        C0418a c0418a = f7440a;
        O1.g gVarF = c0418a.f(view);
        R1.c cVarC = gVarF.c();
        if (cVarC == null) {
            cVarC = new R1.c();
        }
        gVarF.j(cVarC);
        R1.c cVarC2 = gVarF.c();
        if (cVarC2 != null) {
            cVarC2.b(nVar, f3);
        }
        if (C0670b.h()) {
            c0418a.d(view).s(nVar.b(), f3 != null ? C0429f0.f7476a.b(f3.floatValue()) : Float.NaN);
            O1.a aVarA = gVarF.a();
            if (aVarA != null) {
                aVarA.f(gVarF.c());
            }
            O1.c cVarB = gVarF.b();
            if (cVarB != null) {
                cVarB.p(gVarF.c());
            }
            O1.a aVarA2 = gVarF.a();
            if (aVarA2 != null) {
                aVarA2.invalidateSelf();
            }
            O1.c cVarB2 = gVarF.b();
            if (cVarB2 != null) {
                cVarB2.invalidateSelf();
            }
        } else {
            c0418a.e(view).B(nVar.b(), f3 != null ? C0429f0.f7476a.b(f3.floatValue()) : Float.NaN);
        }
        R1.c cVarC3 = gVarF.c();
        if (cVarC3 == null) {
            cVarC3 = new R1.c();
        }
        gVarF.j(cVarC3);
        R1.c cVarC4 = gVarF.c();
        if (cVarC4 != null) {
            cVarC4.b(nVar, f3);
        }
        if (Build.VERSION.SDK_INT >= 29) {
            List listF = gVarF.f();
            ArrayList arrayList = new ArrayList();
            for (Object obj : listF) {
                if (obj instanceof O1.i) {
                    arrayList.add(obj);
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((O1.i) it.next()).d(gVarF.c());
            }
        }
    }

    public static final void t(View view, ReadableArray readableArray) {
        D2.h.f(view, "view");
        if (readableArray == null) {
            u(view, AbstractC0717n.g());
            return;
        }
        ArrayList arrayList = new ArrayList();
        int size = readableArray.size();
        for (int i3 = 0; i3 < size; i3++) {
            g.a aVar = R1.g.f2034g;
            ReadableMap map = readableArray.getMap(i3);
            Context context = view.getContext();
            D2.h.e(context, "getContext(...)");
            R1.g gVarA = aVar.a(map, context);
            if (gVarA == null) {
                throw new IllegalStateException("Required value was null.");
            }
            arrayList.add(gVarA);
        }
        u(view, arrayList);
    }

    public static final void u(View view, List list) {
        D2.h.f(view, "view");
        D2.h.f(list, "shadows");
        if (M1.a.c(view) != 2) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        O1.g gVarF = f7440a.f(view);
        R1.c cVarC = gVarF.c();
        R1.e eVarD = gVarF.d();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            R1.g gVar = (R1.g) it.next();
            float fD = gVar.d();
            float fE = gVar.e();
            Integer numB = gVar.b();
            int iIntValue = numB != null ? numB.intValue() : -16777216;
            Float fA = gVar.a();
            float fFloatValue = fA != null ? fA.floatValue() : 0.0f;
            Float f3 = gVar.f();
            float fFloatValue2 = f3 != null ? f3.floatValue() : 0.0f;
            Boolean boolC = gVar.c();
            boolean zBooleanValue = boolC != null ? boolC.booleanValue() : false;
            if (zBooleanValue && Build.VERSION.SDK_INT >= 29) {
                Context context = view.getContext();
                D2.h.e(context, "getContext(...)");
                arrayList.add(new O1.i(context, iIntValue, fD, fE, fFloatValue, fFloatValue2, cVarC, eVarD));
            } else if (!zBooleanValue && Build.VERSION.SDK_INT >= 28) {
                Context context2 = view.getContext();
                D2.h.e(context2, "getContext(...)");
                arrayList2.add(new O1.m(context2, iIntValue, fD, fE, fFloatValue, fFloatValue2, eVarD));
            }
        }
        view.setBackground(f7440a.f(view).q(arrayList2, arrayList));
    }

    public static final void v(View view, Drawable drawable) {
        D2.h.f(view, "view");
        if (C0670b.h()) {
            f7440a.f(view).o(drawable);
        } else {
            view.setBackground(f7440a.f(view).o(drawable));
        }
    }

    public static final void w(View view, Integer num) {
        D2.h.f(view, "view");
        if (M1.a.c(view) != 2) {
            return;
        }
        O1.k kVarG = f7440a.g(view);
        if (num != null) {
            kVarG.f(num.intValue());
        }
    }

    public static final void x(View view, float f3) {
        D2.h.f(view, "view");
        if (M1.a.c(view) != 2) {
            return;
        }
        f7440a.g(view).g(C0429f0.f7476a.b(f3));
    }

    public static final void y(View view, R1.o oVar) {
        D2.h.f(view, "view");
        if (M1.a.c(view) != 2) {
            return;
        }
        O1.k kVarG = f7440a.g(view);
        if (oVar != null) {
            kVarG.h(oVar);
        }
    }

    public static final void z(View view, float f3) {
        D2.h.f(view, "view");
        if (M1.a.c(view) != 2) {
            return;
        }
        f7440a.g(view).i(C0429f0.f7476a.b(f3));
    }
}

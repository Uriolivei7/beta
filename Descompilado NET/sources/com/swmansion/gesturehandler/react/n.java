package com.swmansion.gesturehandler.react;

import android.view.View;
import android.view.ViewGroup;
import com.facebook.react.uimanager.EnumC0431g0;
import com.facebook.react.uimanager.InterfaceC0445n0;
import n2.D;
import n2.v;
import r2.C0685h;

/* JADX INFO: loaded from: classes.dex */
public final class n implements D {

    public /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f8652a;

        static {
            int[] iArr = new int[EnumC0431g0.values().length];
            try {
                iArr[EnumC0431g0.f7481e.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[EnumC0431g0.f7480d.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[EnumC0431g0.f7479c.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[EnumC0431g0.f7482f.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            f8652a = iArr;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // n2.D
    public v a(View view) {
        D2.h.f(view, "view");
        EnumC0431g0 pointerEvents = view instanceof InterfaceC0445n0 ? ((InterfaceC0445n0) view).getPointerEvents() : EnumC0431g0.f7482f;
        if (!view.isEnabled()) {
            if (pointerEvents == EnumC0431g0.f7482f) {
                return v.f10100c;
            }
            if (pointerEvents == EnumC0431g0.f7481e) {
                return v.f10099b;
            }
        }
        int i3 = a.f8652a[pointerEvents.ordinal()];
        if (i3 == 1) {
            return v.f10101d;
        }
        if (i3 == 2) {
            return v.f10100c;
        }
        if (i3 == 3) {
            return v.f10099b;
        }
        if (i3 == 4) {
            return v.f10102e;
        }
        throw new C0685h();
    }

    @Override // n2.D
    public View b(ViewGroup viewGroup, int i3) {
        D2.h.f(viewGroup, "parent");
        if (viewGroup instanceof com.facebook.react.views.view.g) {
            View childAt = viewGroup.getChildAt(((com.facebook.react.views.view.g) viewGroup).a(i3));
            D2.h.c(childAt);
            return childAt;
        }
        View childAt2 = viewGroup.getChildAt(i3);
        D2.h.e(childAt2, "getChildAt(...)");
        return childAt2;
    }

    @Override // n2.D
    public boolean c(ViewGroup viewGroup) {
        D2.h.f(viewGroup, "view");
        if (viewGroup.getClipChildren()) {
            return true;
        }
        if (viewGroup instanceof com.facebook.react.views.scroll.g) {
            if (!D2.h.b(((com.facebook.react.views.scroll.g) viewGroup).getOverflow(), "visible")) {
                return true;
            }
        } else if (viewGroup instanceof com.facebook.react.views.scroll.f) {
            if (!D2.h.b(((com.facebook.react.views.scroll.f) viewGroup).getOverflow(), "visible")) {
                return true;
            }
        } else if (viewGroup instanceof com.facebook.react.views.view.g) {
            return D2.h.b(((com.facebook.react.views.view.g) viewGroup).getOverflow(), "hidden");
        }
        return false;
    }
}

package com.facebook.react.uimanager;

import a1.C0210a;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/* JADX INFO: renamed from: com.facebook.react.uimanager.x0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0464x0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0464x0 f7637a = new C0464x0();

    private C0464x0() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final InterfaceC0462w0 a(View view) {
        D2.h.f(view, "reactView");
        View view2 = view;
        while (!(view2 instanceof InterfaceC0462w0)) {
            Object parent = view2.getParent();
            if (parent == null) {
                return null;
            }
            C0210a.a(parent instanceof View);
            view2 = (View) parent;
        }
        return (InterfaceC0462w0) view2;
    }

    public static final Point b(View view) {
        D2.h.f(view, "v");
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        iArr[0] = iArr[0] - rect.left;
        iArr[1] = iArr[1] - rect.top;
        return new Point(iArr[0], iArr[1]);
    }
}

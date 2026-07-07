package com.facebook.react.views.view;

import android.view.View;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.views.view.g;

/* JADX INFO: loaded from: classes.dex */
public abstract class ReactClippingViewManager<T extends g> extends ViewGroupManager<T> {
    @L1.a(name = "removeClippedSubviews")
    public void setRemoveClippedSubviews(T t3, boolean z3) {
        D2.h.f(t3, "view");
        UiThreadUtil.assertOnUiThread();
        t3.setRemoveClippedSubviews(z3);
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.N
    public void removeAllViews(T t3) {
        D2.h.f(t3, "parent");
        UiThreadUtil.assertOnUiThread();
        if (t3.getRemoveClippedSubviews()) {
            t3.t();
        } else {
            t3.removeAllViews();
        }
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager
    public void addView(T t3, View view, int i3) {
        D2.h.f(t3, "parent");
        D2.h.f(view, "child");
        UiThreadUtil.assertOnUiThread();
        if (t3.getRemoveClippedSubviews()) {
            t3.j(view, i3);
        } else {
            t3.addView(view, i3);
        }
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager
    public View getChildAt(T t3, int i3) {
        D2.h.f(t3, "parent");
        if (t3.getRemoveClippedSubviews()) {
            return t3.n(i3);
        }
        return t3.getChildAt(i3);
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager
    public int getChildCount(T t3) {
        D2.h.f(t3, "parent");
        if (t3.getRemoveClippedSubviews()) {
            return t3.getAllChildrenCount();
        }
        return t3.getChildCount();
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager
    public void removeViewAt(T t3, int i3) {
        D2.h.f(t3, "parent");
        UiThreadUtil.assertOnUiThread();
        if (t3.getRemoveClippedSubviews()) {
            View childAt = getChildAt((g) t3, i3);
            if (childAt != null) {
                t3.v(childAt);
                return;
            }
            return;
        }
        t3.removeViewAt(i3);
    }
}

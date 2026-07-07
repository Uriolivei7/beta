package com.facebook.react.uimanager;

import android.view.View;
import com.facebook.react.bridge.UiThreadUtil;

/* JADX INFO: loaded from: classes.dex */
public interface N extends O {
    void addView(View view, View view2, int i3);

    View getChildAt(View view, int i3);

    int getChildCount(View view);

    default void removeAllViews(View view) {
        D2.h.f(view, "parent");
        UiThreadUtil.assertOnUiThread();
        int childCount = getChildCount(view);
        while (true) {
            childCount--;
            if (-1 >= childCount) {
                return;
            } else {
                removeViewAt(view, childCount);
            }
        }
    }

    void removeViewAt(View view, int i3);
}

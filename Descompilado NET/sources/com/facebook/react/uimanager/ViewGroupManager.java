package com.facebook.react.uimanager;

import android.view.View;
import android.view.ViewGroup;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.UiThreadUtil;
import java.util.List;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes.dex */
public abstract class ViewGroupManager<T extends ViewGroup> extends BaseViewManager<T, U> implements N {
    private static WeakHashMap<View, Integer> mZIndexHash = new WeakHashMap<>();

    public ViewGroupManager() {
        super(null);
    }

    public static Integer getViewZIndex(View view) {
        return mZIndexHash.get(view);
    }

    public static void setViewZIndex(View view, int i3) {
        mZIndexHash.put(view, Integer.valueOf(i3));
    }

    public void addViews(T t3, List<View> list) {
        UiThreadUtil.assertOnUiThread();
        int size = list.size();
        for (int i3 = 0; i3 < size; i3++) {
            addView((ViewGroup) t3, list.get(i3), i3);
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Class<? extends U> getShadowNodeClass() {
        return U.class;
    }

    @Override // com.facebook.react.uimanager.O
    public boolean needsCustomLayoutForChildren() {
        return false;
    }

    public /* bridge */ /* synthetic */ void removeAllViews(View view) {
        super.removeAllViews(view);
    }

    public void removeView(T t3, View view) {
        UiThreadUtil.assertOnUiThread();
        for (int i3 = 0; i3 < getChildCount((ViewGroup) t3); i3++) {
            if (getChildAt((ViewGroup) t3, i3) == view) {
                removeViewAt((ViewGroup) t3, i3);
                return;
            }
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void updateExtraData(T t3, Object obj) {
    }

    public ViewGroupManager(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @Override // com.facebook.react.uimanager.N
    public void addView(T t3, View view, int i3) {
        t3.addView(view, i3);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public U createShadowNodeInstance() {
        return new U();
    }

    @Override // com.facebook.react.uimanager.N
    public View getChildAt(T t3, int i3) {
        return t3.getChildAt(i3);
    }

    @Override // com.facebook.react.uimanager.N
    public int getChildCount(T t3) {
        return t3.getChildCount();
    }

    @Override // com.facebook.react.uimanager.N
    public void removeViewAt(T t3, int i3) {
        UiThreadUtil.assertOnUiThread();
        t3.removeViewAt(i3);
    }
}

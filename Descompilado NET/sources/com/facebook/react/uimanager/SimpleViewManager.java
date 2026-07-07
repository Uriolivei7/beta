package com.facebook.react.uimanager;

import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public abstract class SimpleViewManager<T extends View> extends BaseViewManager<T, U> {
    @Override // com.facebook.react.uimanager.ViewManager
    public Class<U> getShadowNodeClass() {
        return U.class;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void updateExtraData(T t3, Object obj) {
        D2.h.f(t3, "root");
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public U createShadowNodeInstance() {
        return new U();
    }
}

package com.facebook.react.views.text;

import android.view.View;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.BaseViewManager;
import kotlin.jvm.internal.DefaultConstructorMarker;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = ReactVirtualTextViewManager.REACT_CLASS)
public final class ReactVirtualTextViewManager extends BaseViewManager<View, q> {
    public static final a Companion = new a(null);
    public static final String REACT_CLASS = "RCTVirtualText";

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    protected View createViewInstance(B0 b02) {
        D2.h.f(b02, "context");
        throw new IllegalStateException("Attempt to create a native view for RCTVirtualText");
    }

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Class<q> getShadowNodeClass() {
        return q.class;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void updateExtraData(View view, Object obj) {
        D2.h.f(view, "view");
        D2.h.f(obj, "extraData");
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public q createShadowNodeInstance() {
        return new q();
    }
}

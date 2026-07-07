package com.facebook.react.views.text;

import android.view.View;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.ViewManager;
import kotlin.jvm.internal.DefaultConstructorMarker;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = ReactRawTextManager.REACT_CLASS)
public final class ReactRawTextManager extends ViewManager<View, e> {
    public static final a Companion = new a(null);
    public static final String REACT_CLASS = "RCTRawText";

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Class<e> getShadowNodeClass() {
        return e.class;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    protected View prepareToRecycleView(B0 b02, View view) {
        D2.h.f(b02, "reactContext");
        D2.h.f(view, "view");
        throw new IllegalStateException("Attempt to recycle a native view for RCTRawText");
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void updateExtraData(View view, Object obj) {
        D2.h.f(view, "view");
        D2.h.f(obj, "extraData");
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public e createShadowNodeInstance() {
        return new e();
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public m createViewInstance(B0 b02) {
        D2.h.f(b02, "context");
        throw new IllegalStateException("Attempt to create a native view for RCTRawText");
    }
}

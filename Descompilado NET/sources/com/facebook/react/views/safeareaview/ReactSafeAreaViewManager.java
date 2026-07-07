package com.facebook.react.views.safeareaview;

import D2.h;
import U1.t;
import android.view.View;
import com.facebook.react.uimanager.A0;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.C0454s0;
import com.facebook.react.uimanager.Q0;
import com.facebook.react.uimanager.U;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.W0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = ReactSafeAreaViewManager.REACT_CLASS)
public final class ReactSafeAreaViewManager extends ViewGroupManager<b> implements W0 {
    public static final a Companion = new a(null);
    public static final String REACT_CLASS = "RCTSafeAreaView";
    private final Q0 delegate = new t(this);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    protected Q0 getDelegate() {
        return this.delegate;
    }

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.ViewManager
    public Class<? extends U> getShadowNodeClass() {
        return c.class;
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.N
    public /* bridge */ /* synthetic */ void removeAllViews(View view) {
        super.removeAllViews(view);
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.ViewManager
    public U createShadowNodeInstance() {
        return new c();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public b createViewInstance(B0 b02) {
        h.f(b02, "context");
        return new b(b02);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Object updateState(b bVar, C0454s0 c0454s0, A0 a02) {
        h.f(bVar, "view");
        h.f(c0454s0, "props");
        h.f(a02, "stateWrapper");
        bVar.setStateWrapper$ReactAndroid_release(a02);
        return null;
    }
}

package com.facebook.react.views.scroll;

import android.view.View;
import com.facebook.react.uimanager.A0;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.C0454s0;
import com.facebook.react.views.view.ReactViewManager;
import kotlin.jvm.internal.DefaultConstructorMarker;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = ReactHorizontalScrollContainerViewManager.REACT_CLASS)
public final class ReactHorizontalScrollContainerViewManager extends ReactViewManager {
    public static final a Companion = new a(null);
    public static final String REACT_CLASS = "AndroidHorizontalScrollContentView";
    private static Integer uiManagerType;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    @Override // com.facebook.react.views.view.ReactViewManager, com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public com.facebook.react.views.view.g createViewInstance(int i3, B0 b02, C0454s0 c0454s0, A0 a02) {
        D2.h.f(b02, "context");
        if (uiManagerType == null) {
            uiManagerType = Integer.valueOf(M1.a.a(i3));
            View viewCreateViewInstance = super.createViewInstance(i3, b02, c0454s0, a02);
            D2.h.e(viewCreateViewInstance, "createViewInstance(...)");
            com.facebook.react.views.view.g gVar = (com.facebook.react.views.view.g) viewCreateViewInstance;
            uiManagerType = null;
            return gVar;
        }
        throw new IllegalStateException("Check failed.");
    }

    @Override // com.facebook.react.views.view.ReactViewManager, com.facebook.react.uimanager.ViewManager
    public com.facebook.react.views.view.g createViewInstance(B0 b02) {
        D2.h.f(b02, "context");
        Integer num = uiManagerType;
        if (num == null) {
            throw new IllegalStateException("Required value was null.");
        }
        if (num.intValue() == 2) {
            return new com.facebook.react.views.view.g(b02);
        }
        return new e(b02);
    }
}

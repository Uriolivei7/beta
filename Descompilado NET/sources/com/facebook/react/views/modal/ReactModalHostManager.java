package com.facebook.react.views.modal;

import D2.h;
import U1.m;
import U1.n;
import android.content.DialogInterface;
import android.view.View;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.A0;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.C0454s0;
import com.facebook.react.uimanager.H0;
import com.facebook.react.uimanager.Q0;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.views.modal.c;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0696D;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = ReactModalHostManager.REACT_CLASS)
public final class ReactModalHostManager extends ViewGroupManager<c> implements n {
    public static final a Companion = new a(null);
    public static final String REACT_CLASS = "RCTModalHostView";
    private final Q0 delegate = new m(this);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void addEventEmitters$lambda$0(EventDispatcher eventDispatcher, B0 b02, c cVar, DialogInterface dialogInterface) {
        eventDispatcher.b(new d(H0.e(b02), cVar.getId()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void addEventEmitters$lambda$1(EventDispatcher eventDispatcher, B0 b02, c cVar, DialogInterface dialogInterface) {
        eventDispatcher.b(new e(H0.e(b02), cVar.getId()));
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Q0 getDelegate() {
        return this.delegate;
    }

    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        Map<String, Object> exportedCustomDirectEventTypeConstants = super.getExportedCustomDirectEventTypeConstants();
        if (exportedCustomDirectEventTypeConstants == null) {
            exportedCustomDirectEventTypeConstants = new LinkedHashMap<>();
        }
        exportedCustomDirectEventTypeConstants.put("topRequestClose", AbstractC0696D.d(r2.n.a("registrationName", "onRequestClose")));
        exportedCustomDirectEventTypeConstants.put("topShow", AbstractC0696D.d(r2.n.a("registrationName", "onShow")));
        exportedCustomDirectEventTypeConstants.put("topDismiss", AbstractC0696D.d(r2.n.a("registrationName", "onDismiss")));
        exportedCustomDirectEventTypeConstants.put("topOrientationChange", AbstractC0696D.d(r2.n.a("registrationName", "onOrientationChange")));
        return exportedCustomDirectEventTypeConstants;
    }

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.N
    public /* bridge */ /* synthetic */ void removeAllViews(View view) {
        super.removeAllViews(view);
    }

    @Override // U1.n
    @L1.a(name = "animated")
    public void setAnimated(c cVar, boolean z3) {
        h.f(cVar, "view");
    }

    @Override // U1.n
    @L1.a(name = "identifier")
    public void setIdentifier(c cVar, int i3) {
        h.f(cVar, "view");
    }

    @Override // U1.n
    @L1.a(name = "presentationStyle")
    public void setPresentationStyle(c cVar, String str) {
        h.f(cVar, "view");
    }

    @Override // U1.n
    @L1.a(name = "supportedOrientations")
    public void setSupportedOrientations(c cVar, ReadableArray readableArray) {
        h.f(cVar, "view");
    }

    @Override // U1.n
    @L1.a(name = "visible")
    public void setVisible(c cVar, boolean z3) {
        h.f(cVar, "view");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public void addEventEmitters(final B0 b02, final c cVar) {
        h.f(b02, "reactContext");
        h.f(cVar, "view");
        final EventDispatcher eventDispatcherC = H0.c(b02, cVar.getId());
        if (eventDispatcherC != null) {
            cVar.setOnRequestCloseListener(new c.InterfaceC0113c() { // from class: com.facebook.react.views.modal.a
                @Override // com.facebook.react.views.modal.c.InterfaceC0113c
                public final void a(DialogInterface dialogInterface) {
                    ReactModalHostManager.addEventEmitters$lambda$0(eventDispatcherC, b02, cVar, dialogInterface);
                }
            });
            cVar.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.facebook.react.views.modal.b
                @Override // android.content.DialogInterface.OnShowListener
                public final void onShow(DialogInterface dialogInterface) {
                    ReactModalHostManager.addEventEmitters$lambda$1(eventDispatcherC, b02, cVar, dialogInterface);
                }
            });
            cVar.setEventDispatcher(eventDispatcherC);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public c createViewInstance(B0 b02) {
        h.f(b02, "reactContext");
        return new c(b02);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public void onAfterUpdateTransaction(c cVar) {
        h.f(cVar, "view");
        super.onAfterUpdateTransaction(cVar);
        cVar.d();
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void onDropViewInstance(c cVar) {
        h.f(cVar, "view");
        super.onDropViewInstance(cVar);
        cVar.c();
    }

    @Override // U1.n
    @L1.a(name = "animationType")
    public void setAnimationType(c cVar, String str) {
        h.f(cVar, "view");
        if (str != null) {
            cVar.setAnimationType(str);
        }
    }

    @Override // U1.n
    @L1.a(name = "hardwareAccelerated")
    public void setHardwareAccelerated(c cVar, boolean z3) {
        h.f(cVar, "view");
        cVar.setHardwareAccelerated(z3);
    }

    @Override // U1.n
    @L1.a(name = "navigationBarTranslucent")
    public void setNavigationBarTranslucent(c cVar, boolean z3) {
        h.f(cVar, "view");
        cVar.setNavigationBarTranslucent(z3);
    }

    @Override // U1.n
    @L1.a(name = "statusBarTranslucent")
    public void setStatusBarTranslucent(c cVar, boolean z3) {
        h.f(cVar, "view");
        cVar.setStatusBarTranslucent(z3);
    }

    @Override // com.facebook.react.uimanager.BaseViewManager
    public void setTestId(c cVar, String str) {
        h.f(cVar, "view");
        super.setTestId(cVar, str);
        cVar.setDialogRootViewGroupTestId(str);
    }

    @Override // U1.n
    @L1.a(name = "transparent")
    public void setTransparent(c cVar, boolean z3) {
        h.f(cVar, "view");
        cVar.setTransparent(z3);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Object updateState(c cVar, C0454s0 c0454s0, A0 a02) {
        h.f(cVar, "view");
        h.f(c0454s0, "props");
        h.f(a02, "stateWrapper");
        cVar.setStateWrapper(a02);
        return null;
    }
}

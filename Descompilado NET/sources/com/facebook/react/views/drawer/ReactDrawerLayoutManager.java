package com.facebook.react.views.drawer;

import D2.h;
import U1.c;
import U1.d;
import android.view.View;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.H0;
import com.facebook.react.uimanager.Q0;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.events.EventDispatcher;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.n;
import s2.AbstractC0696D;
import v1.InterfaceC0756a;
import y.C0775a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = ReactDrawerLayoutManager.REACT_CLASS)
public final class ReactDrawerLayoutManager extends ViewGroupManager<com.facebook.react.views.drawer.a> implements d {
    public static final int CLOSE_DRAWER = 2;
    public static final String COMMAND_CLOSE_DRAWER = "closeDrawer";
    public static final String COMMAND_OPEN_DRAWER = "openDrawer";
    public static final a Companion = new a(null);
    private static final String DRAWER_POSITION = "DrawerPosition";
    private static final String DRAWER_POSITION_LEFT = "Left";
    private static final String DRAWER_POSITION_RIGHT = "Right";
    public static final int OPEN_DRAWER = 1;
    public static final String REACT_CLASS = "AndroidDrawerLayout";
    private final Q0 delegate = new c(this);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public static final class b implements C0775a.d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final C0775a f7657a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final EventDispatcher f7658b;

        public b(C0775a c0775a, EventDispatcher eventDispatcher) {
            h.f(c0775a, "drawerLayout");
            h.f(eventDispatcher, "eventDispatcher");
            this.f7657a = c0775a;
            this.f7658b = eventDispatcher;
        }

        @Override // y.C0775a.d
        public void a(int i3) {
            this.f7658b.b(new W1.d(H0.f(this.f7657a), this.f7657a.getId(), i3));
        }

        @Override // y.C0775a.d
        public void b(View view, float f3) {
            h.f(view, "view");
            this.f7658b.b(new W1.c(H0.f(this.f7657a), this.f7657a.getId(), f3));
        }

        @Override // y.C0775a.d
        public void c(View view) {
            h.f(view, "view");
            this.f7658b.b(new W1.b(H0.f(this.f7657a), this.f7657a.getId()));
        }

        @Override // y.C0775a.d
        public void d(View view) {
            h.f(view, "view");
            this.f7658b.b(new W1.a(H0.f(this.f7657a), this.f7657a.getId()));
        }
    }

    private final void setDrawerPositionInternal(com.facebook.react.views.drawer.a aVar, String str) {
        if (h.b(str, "left")) {
            aVar.setDrawerPosition$ReactAndroid_release(8388611);
            return;
        }
        if (h.b(str, "right")) {
            aVar.setDrawerPosition$ReactAndroid_release(8388613);
            return;
        }
        Y.a.I("ReactNative", "drawerPosition must be 'left' or 'right', received" + str);
        aVar.setDrawerPosition$ReactAndroid_release(8388611);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Map<String, Integer> getCommandsMap() {
        return AbstractC0696D.h(n.a(COMMAND_OPEN_DRAWER, 1), n.a(COMMAND_CLOSE_DRAWER, 2));
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
        exportedCustomDirectEventTypeConstants.put("topDrawerSlide", AbstractC0696D.d(n.a("registrationName", "onDrawerSlide")));
        exportedCustomDirectEventTypeConstants.put("topDrawerOpen", AbstractC0696D.d(n.a("registrationName", "onDrawerOpen")));
        exportedCustomDirectEventTypeConstants.put("topDrawerClose", AbstractC0696D.d(n.a("registrationName", "onDrawerClose")));
        exportedCustomDirectEventTypeConstants.put("topDrawerStateChanged", AbstractC0696D.d(n.a("registrationName", "onDrawerStateChanged")));
        return exportedCustomDirectEventTypeConstants;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Map<String, Object> getExportedViewConstants() {
        return AbstractC0696D.d(n.a(DRAWER_POSITION, AbstractC0696D.h(n.a(DRAWER_POSITION_LEFT, 8388611), n.a(DRAWER_POSITION_RIGHT, 8388613))));
    }

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.O
    public boolean needsCustomLayoutForChildren() {
        return true;
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.N
    public /* bridge */ /* synthetic */ void removeAllViews(View view) {
        super.removeAllViews(view);
    }

    @Override // U1.d
    @L1.a(customType = "Color", name = "drawerBackgroundColor")
    public void setDrawerBackgroundColor(com.facebook.react.views.drawer.a aVar, Integer num) {
        h.f(aVar, "view");
    }

    @Override // U1.d
    @L1.a(name = "keyboardDismissMode")
    public void setKeyboardDismissMode(com.facebook.react.views.drawer.a aVar, String str) {
        h.f(aVar, "view");
    }

    @Override // U1.d
    @L1.a(customType = "Color", name = "statusBarBackgroundColor")
    public void setStatusBarBackgroundColor(com.facebook.react.views.drawer.a aVar, Integer num) {
        h.f(aVar, "view");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public void addEventEmitters(B0 b02, com.facebook.react.views.drawer.a aVar) {
        h.f(b02, "reactContext");
        h.f(aVar, "view");
        EventDispatcher eventDispatcherC = H0.c(b02, aVar.getId());
        if (eventDispatcherC == null) {
            return;
        }
        aVar.a(new b(aVar, eventDispatcherC));
    }

    @Override // U1.d
    public void closeDrawer(com.facebook.react.views.drawer.a aVar) {
        h.f(aVar, "view");
        aVar.V();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public com.facebook.react.views.drawer.a createViewInstance(B0 b02) {
        h.f(b02, "context");
        return new com.facebook.react.views.drawer.a(b02);
    }

    @Override // U1.d
    public void openDrawer(com.facebook.react.views.drawer.a aVar) {
        h.f(aVar, "view");
        aVar.W();
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0030, code lost:
    
        if (r5.equals("unlocked") != false) goto L22;
     */
    @Override // U1.d
    @L1.a(name = "drawerLockMode")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setDrawerLockMode(com.facebook.react.views.drawer.a r4, java.lang.String r5) {
        /*
            r3 = this;
            java.lang.String r0 = "view"
            D2.h.f(r4, r0)
            r0 = 0
            if (r5 == 0) goto L5a
            int r1 = r5.hashCode()
            r2 = -1292600945(0xffffffffb2f4798f, float:-2.8460617E-8)
            if (r1 == r2) goto L33
            r2 = -210949405(0xfffffffff36d2ae3, float:-1.8790347E31)
            if (r1 == r2) goto L2a
            r2 = 168848173(0xa106b2d, float:6.953505E-33)
            if (r1 == r2) goto L1c
            goto L3b
        L1c:
            java.lang.String r1 = "locked-open"
            boolean r1 = r5.equals(r1)
            if (r1 != 0) goto L25
            goto L3b
        L25:
            r5 = 2
            r4.setDrawerLockMode(r5)
            goto L5d
        L2a:
            java.lang.String r1 = "unlocked"
            boolean r1 = r5.equals(r1)
            if (r1 == 0) goto L3b
            goto L5a
        L33:
            java.lang.String r1 = "locked-closed"
            boolean r1 = r5.equals(r1)
            if (r1 != 0) goto L55
        L3b:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unknown drawerLockMode "
            r1.append(r2)
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            java.lang.String r1 = "ReactNative"
            Y.a.I(r1, r5)
            r4.setDrawerLockMode(r0)
            goto L5d
        L55:
            r5 = 1
            r4.setDrawerLockMode(r5)
            goto L5d
        L5a:
            r4.setDrawerLockMode(r0)
        L5d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.drawer.ReactDrawerLayoutManager.setDrawerLockMode(com.facebook.react.views.drawer.a, java.lang.String):void");
    }

    @Override // U1.d
    public void setDrawerPosition(com.facebook.react.views.drawer.a aVar, String str) {
        h.f(aVar, "view");
        if (str == null) {
            aVar.setDrawerPosition$ReactAndroid_release(8388611);
        } else {
            setDrawerPositionInternal(aVar, str);
        }
    }

    @L1.a(defaultFloat = Float.NaN, name = "drawerWidth")
    public final void setDrawerWidth(com.facebook.react.views.drawer.a aVar, float f3) {
        h.f(aVar, "view");
        aVar.setDrawerWidth$ReactAndroid_release(Float.isNaN(f3) ? -1 : Math.round(C0429f0.f7476a.b(f3)));
    }

    @Override // com.facebook.react.uimanager.BaseViewManager
    public void setElevation(com.facebook.react.views.drawer.a aVar, float f3) {
        h.f(aVar, "view");
        aVar.setDrawerElevation(C0429f0.f7476a.b(f3));
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager
    public void addView(com.facebook.react.views.drawer.a aVar, View view, int i3) {
        h.f(aVar, "parent");
        h.f(view, "child");
        if (getChildCount(aVar) >= 2) {
            throw new JSApplicationIllegalArgumentException("The Drawer cannot have more than two children");
        }
        if (i3 != 0 && i3 != 1) {
            throw new JSApplicationIllegalArgumentException("The only valid indices for drawer's child are 0 or 1. Got " + i3 + " instead.");
        }
        aVar.addView(view, i3);
        aVar.X();
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(com.facebook.react.views.drawer.a aVar, int i3, ReadableArray readableArray) {
        h.f(aVar, "root");
        if (i3 == 1) {
            aVar.W();
        } else {
            if (i3 != 2) {
                return;
            }
            aVar.V();
        }
    }

    @L1.a(name = "drawerPosition")
    public final void setDrawerPosition(com.facebook.react.views.drawer.a aVar, Dynamic dynamic) {
        h.f(aVar, "view");
        h.f(dynamic, "drawerPosition");
        if (dynamic.isNull()) {
            aVar.setDrawerPosition$ReactAndroid_release(8388611);
            return;
        }
        if (dynamic.getType() == ReadableType.Number) {
            int iAsInt = dynamic.asInt();
            if (8388611 != iAsInt && 8388613 != iAsInt) {
                Y.a.I("ReactNative", "Unknown drawerPosition " + iAsInt);
                aVar.setDrawerPosition$ReactAndroid_release(8388611);
                return;
            }
            aVar.setDrawerPosition$ReactAndroid_release(iAsInt);
            return;
        }
        if (dynamic.getType() == ReadableType.String) {
            setDrawerPositionInternal(aVar, dynamic.asString());
        } else {
            Y.a.I("ReactNative", "drawerPosition must be a string or int");
            aVar.setDrawerPosition$ReactAndroid_release(8388611);
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(com.facebook.react.views.drawer.a aVar, String str, ReadableArray readableArray) {
        h.f(aVar, "root");
        h.f(str, "commandId");
        if (h.b(str, COMMAND_OPEN_DRAWER)) {
            aVar.W();
        } else if (h.b(str, COMMAND_CLOSE_DRAWER)) {
            aVar.V();
        }
    }

    @Override // U1.d
    public void setDrawerWidth(com.facebook.react.views.drawer.a aVar, Float f3) {
        int iRound;
        h.f(aVar, "view");
        if (f3 != null) {
            iRound = Math.round(C0429f0.f7476a.b(f3.floatValue()));
        } else {
            iRound = -1;
        }
        aVar.setDrawerWidth$ReactAndroid_release(iRound);
    }
}

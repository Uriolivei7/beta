package com.facebook.react.modules.devloading;

import D2.h;
import com.facebook.fbreact.specs.NativeDevLoadingViewSpec;
import com.facebook.react.bridge.JSExceptionHandler;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.devsupport.E;
import com.facebook.react.modules.devloading.DevLoadingModule;
import k1.InterfaceC0585c;
import kotlin.jvm.internal.DefaultConstructorMarker;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "DevLoadingView")
public final class DevLoadingModule extends NativeDevLoadingViewSpec {
    public static final a Companion = new a(null);
    public static final String NAME = "DevLoadingView";
    private InterfaceC0585c devLoadingViewManager;
    private final JSExceptionHandler jsExceptionHandler;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DevLoadingModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        h.f(reactApplicationContext, "reactContext");
        JSExceptionHandler jSExceptionHandler = reactApplicationContext.getJSExceptionHandler();
        this.jsExceptionHandler = jSExceptionHandler;
        if (jSExceptionHandler == null || !(jSExceptionHandler instanceof E)) {
            return;
        }
        this.devLoadingViewManager = ((E) jSExceptionHandler).e0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void hide$lambda$1(DevLoadingModule devLoadingModule) {
        InterfaceC0585c interfaceC0585c = devLoadingModule.devLoadingViewManager;
        if (interfaceC0585c != null) {
            interfaceC0585c.c();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void showMessage$lambda$0(DevLoadingModule devLoadingModule, String str) {
        InterfaceC0585c interfaceC0585c = devLoadingModule.devLoadingViewManager;
        if (interfaceC0585c != null) {
            interfaceC0585c.a(str);
        }
    }

    @Override // com.facebook.fbreact.specs.NativeDevLoadingViewSpec
    public void hide() {
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: D1.a
            @Override // java.lang.Runnable
            public final void run() {
                DevLoadingModule.hide$lambda$1(this.f161b);
            }
        });
    }

    @Override // com.facebook.fbreact.specs.NativeDevLoadingViewSpec
    public void showMessage(final String str, Double d4, Double d5) {
        h.f(str, "message");
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: D1.b
            @Override // java.lang.Runnable
            public final void run() {
                DevLoadingModule.showMessage$lambda$0(this.f162b, str);
            }
        });
    }
}

package com.reactnativecommunity.blurview;

import android.view.View;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.Q0;
import com.facebook.react.uimanager.ViewGroupManager;
import q2.C0655c;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "AndroidBlurView")
class BlurViewManager extends ViewGroupManager<C0655c> implements U1.b {
    private final Q0 mDelegate = new U1.a(this);

    public BlurViewManager(ReactApplicationContext reactApplicationContext) {
    }

    @Override // com.facebook.react.uimanager.ViewManager
    protected Q0 getDelegate() {
        return this.mDelegate;
    }

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return "AndroidBlurView";
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.N
    public /* bridge */ /* synthetic */ void removeAllViews(View view) {
        super.removeAllViews(view);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public C0655c createViewInstance(B0 b02) {
        return a.a(b02);
    }

    @Override // U1.b
    @L1.a(defaultBoolean = true, name = "autoUpdate")
    public void setAutoUpdate(C0655c c0655c, boolean z3) {
        a.b(c0655c, z3);
    }

    @Override // U1.b
    public void setBlurAmount(C0655c c0655c, int i3) {
    }

    @Override // U1.b
    @L1.a(defaultInt = 10, name = "blurRadius")
    public void setBlurRadius(C0655c c0655c, int i3) {
        a.e(c0655c, i3);
    }

    @Override // U1.b
    public void setBlurType(C0655c c0655c, String str) {
    }

    @Override // U1.b
    @L1.a(defaultInt = 10, name = "downsampleFactor")
    public void setDownsampleFactor(C0655c c0655c, int i3) {
    }

    @Override // U1.b
    @L1.a(defaultBoolean = true, name = "enabled")
    public void setEnabled(C0655c c0655c, boolean z3) {
        a.c(c0655c, z3);
    }

    @Override // U1.b
    @L1.a(customType = "Color", name = "overlayColor")
    public void setOverlayColor(C0655c c0655c, Integer num) {
        a.d(c0655c, num.intValue());
    }
}

package com.facebook.react.devsupport;

import android.content.Context;
import com.facebook.react.bridge.UiThreadUtil;
import java.util.Map;
import k1.InterfaceC0584b;
import k1.InterfaceC0585c;

/* JADX INFO: renamed from: com.facebook.react.devsupport.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0368a extends E {
    public C0368a(Context context, c0 c0Var, String str) {
        this(context.getApplicationContext(), c0Var, str, true, null, null, 2, null, null, null, null);
    }

    @Override // com.facebook.react.devsupport.E
    protected String k0() {
        return "Bridgeless";
    }

    @Override // k1.e
    public void s() {
        UiThreadUtil.assertOnUiThread();
        q();
        this.f6603f.j("BridgelessDevSupportManager.handleReloadJS()");
    }

    public C0368a(Context context, c0 c0Var, String str, boolean z3, k1.i iVar, InterfaceC0584b interfaceC0584b, int i3, Map<String, H1.f> map, e1.k kVar, InterfaceC0585c interfaceC0585c, k1.h hVar) {
        super(context, c0Var, str, z3, iVar, interfaceC0584b, i3, map, kVar, interfaceC0585c, hVar);
    }
}

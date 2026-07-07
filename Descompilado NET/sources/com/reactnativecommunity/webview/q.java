package com.reactnativecommunity.webview;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import d1.b0;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import w1.InterfaceC0763a;

/* JADX INFO: loaded from: classes.dex */
public class q extends b0 {
    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Map l() {
        HashMap map = new HashMap();
        map.put(NativeRNCWebViewModuleSpec.NAME, new ReactModuleInfo(NativeRNCWebViewModuleSpec.NAME, NativeRNCWebViewModuleSpec.NAME, false, false, true, false, true));
        return map;
    }

    @Override // d1.AbstractC0493a, d1.O
    public List f(ReactApplicationContext reactApplicationContext) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new RNCWebViewManager());
        return arrayList;
    }

    @Override // d1.AbstractC0493a
    public NativeModule g(String str, ReactApplicationContext reactApplicationContext) {
        if (str.equals(NativeRNCWebViewModuleSpec.NAME)) {
            return new RNCWebViewModule(reactApplicationContext);
        }
        return null;
    }

    @Override // d1.AbstractC0493a
    public InterfaceC0763a i() {
        return new InterfaceC0763a() { // from class: com.reactnativecommunity.webview.p
            @Override // w1.InterfaceC0763a
            public final Map a() {
                return q.l();
            }
        };
    }
}

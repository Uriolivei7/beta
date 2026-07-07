package com.oblador.vectoricons;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import d1.b0;
import java.util.HashMap;
import java.util.Map;
import w1.InterfaceC0763a;

/* JADX INFO: loaded from: classes.dex */
public class c extends b0 {
    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Map l() {
        HashMap map = new HashMap();
        map.put(NativeRNVectorIconsSpec.NAME, new ReactModuleInfo(NativeRNVectorIconsSpec.NAME, NativeRNVectorIconsSpec.NAME, false, false, false, false, true));
        return map;
    }

    @Override // d1.AbstractC0493a
    public NativeModule g(String str, ReactApplicationContext reactApplicationContext) {
        if (str.equals(NativeRNVectorIconsSpec.NAME)) {
            return new VectorIconsModule(reactApplicationContext);
        }
        return null;
    }

    @Override // d1.AbstractC0493a
    public InterfaceC0763a i() {
        return new InterfaceC0763a() { // from class: com.oblador.vectoricons.b
            @Override // w1.InterfaceC0763a
            public final Map a() {
                return c.l();
            }
        };
    }
}

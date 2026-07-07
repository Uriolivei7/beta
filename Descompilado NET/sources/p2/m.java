package p2;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import com.ting.TingModule;
import d1.b0;
import java.util.HashMap;
import java.util.Map;
import w1.InterfaceC0763a;

/* JADX INFO: loaded from: classes.dex */
public final class m extends b0 {
    /* JADX INFO: Access modifiers changed from: private */
    public static final Map l() {
        HashMap map = new HashMap();
        map.put("Ting", new ReactModuleInfo("Ting", "Ting", false, false, true, false, true));
        return map;
    }

    @Override // d1.AbstractC0493a
    public NativeModule g(String str, ReactApplicationContext reactApplicationContext) {
        D2.h.f(str, "name");
        D2.h.f(reactApplicationContext, "reactContext");
        if (D2.h.b(str, "Ting")) {
            return new TingModule(reactApplicationContext);
        }
        return null;
    }

    @Override // d1.AbstractC0493a
    public InterfaceC0763a i() {
        return new InterfaceC0763a() { // from class: p2.l
            @Override // w1.InterfaceC0763a
            public final Map a() {
                return m.l();
            }
        };
    }
}

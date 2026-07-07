package com.facebook.react.bridge.interop;

import D2.h;
import com.facebook.react.bridge.JavaScriptModule;
import java.util.LinkedHashMap;
import java.util.Map;
import r1.C0670b;

/* JADX INFO: loaded from: classes.dex */
public final class InteropModuleRegistry {
    private final Map<Class<?>, Object> supportedModules = new LinkedHashMap();

    private final boolean checkReactFeatureFlagsConditions() {
        return C0670b.f() && C0670b.p();
    }

    public final <T extends JavaScriptModule> T getInteropModule(Class<T> cls) {
        h.f(cls, "requestedModule");
        if (!checkReactFeatureFlagsConditions()) {
            return null;
        }
        Object obj = this.supportedModules.get(cls);
        if (obj instanceof JavaScriptModule) {
            return (T) obj;
        }
        return null;
    }

    public final <T extends JavaScriptModule> void registerInteropModule(Class<T> cls, Object obj) {
        h.f(cls, "interopModuleInterface");
        h.f(obj, "interopModule");
        if (checkReactFeatureFlagsConditions()) {
            this.supportedModules.put(cls, obj);
        }
    }

    public final <T extends JavaScriptModule> boolean shouldReturnInteropModule(Class<T> cls) {
        h.f(cls, "requestedModule");
        return checkReactFeatureFlagsConditions() && this.supportedModules.containsKey(cls);
    }
}

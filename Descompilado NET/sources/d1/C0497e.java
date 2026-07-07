package d1;

import com.facebook.react.bridge.ModuleSpec;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.views.debuggingoverlay.DebuggingOverlayManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Provider;
import w1.InterfaceC0763a;

/* JADX INFO: renamed from: d1.e, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0497e extends AbstractC0493a implements c0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Map f9203a;

    /* JADX INFO: renamed from: d1.e$a */
    class a implements InterfaceC0763a {
        a() {
        }

        @Override // w1.InterfaceC0763a
        public Map a() {
            return Collections.emptyMap();
        }
    }

    private Map k() {
        if (this.f9203a == null) {
            HashMap map = new HashMap();
            map.put(DebuggingOverlayManager.REACT_CLASS, ModuleSpec.viewManagerSpec(new Provider() { // from class: d1.d
                @Override // javax.inject.Provider
                public final Object get() {
                    return new DebuggingOverlayManager();
                }
            }));
            this.f9203a = map;
        }
        return this.f9203a;
    }

    @Override // d1.c0
    public ViewManager a(ReactApplicationContext reactApplicationContext, String str) {
        ModuleSpec moduleSpec = (ModuleSpec) k().get(str);
        if (moduleSpec != null) {
            return (ViewManager) moduleSpec.getProvider().get();
        }
        return null;
    }

    @Override // d1.c0
    public Collection d(ReactApplicationContext reactApplicationContext) {
        return k().keySet();
    }

    @Override // d1.AbstractC0493a
    public NativeModule g(String str, ReactApplicationContext reactApplicationContext) {
        return null;
    }

    @Override // d1.AbstractC0493a
    public InterfaceC0763a i() {
        return new a();
    }

    @Override // d1.AbstractC0493a
    public List j(ReactApplicationContext reactApplicationContext) {
        return new ArrayList(k().values());
    }
}

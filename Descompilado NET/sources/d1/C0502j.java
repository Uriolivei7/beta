package d1;

import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.NativeModuleRegistry;
import com.facebook.react.bridge.ReactApplicationContext;
import java.util.HashMap;

/* JADX INFO: renamed from: d1.j, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0502j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ReactApplicationContext f9217a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final HashMap f9218b;

    public C0502j(ReactApplicationContext reactApplicationContext) {
        D2.h.f(reactApplicationContext, "reactApplicationContext");
        this.f9217a = reactApplicationContext;
        this.f9218b = new HashMap();
    }

    public final NativeModuleRegistry a() {
        return new NativeModuleRegistry(this.f9217a, this.f9218b);
    }

    public final void b(O o3) {
        D2.h.f(o3, "reactPackage");
        for (ModuleHolder moduleHolder : o3 instanceof AbstractC0500h ? ((AbstractC0500h) o3).b(this.f9217a) : o3 instanceof AbstractC0493a ? ((AbstractC0493a) o3).h(this.f9217a) : P.f9152a.a(o3, this.f9217a)) {
            String name = moduleHolder.getName();
            ModuleHolder moduleHolder2 = (ModuleHolder) this.f9218b.get(name);
            if (moduleHolder2 != null && !moduleHolder.getCanOverrideExistingModule()) {
                throw new IllegalStateException(("\nNative module " + name + " tried to override " + moduleHolder2.getClassName() + ".\n\nCheck the getPackages() method in MainApplication.java, it might be that module is being created twice. \nIf this was your intention, set canOverrideExistingModule=true. This error may also be present if the \npackage is present only once in getPackages() but is also automatically added later during build time \nby autolinking. Try removing the existing entry and rebuild.\n").toString());
            }
            this.f9218b.put(name, moduleHolder);
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public C0502j(ReactApplicationContext reactApplicationContext, J j3) {
        this(reactApplicationContext);
        D2.h.f(reactApplicationContext, "reactApplicationContext");
        D2.h.f(j3, "reactInstanceManager");
    }
}

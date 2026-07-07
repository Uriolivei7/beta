package d1;

import a1.C0210a;
import com.facebook.jni.HybridData;
import com.facebook.react.bridge.CxxModuleWrapper;
import com.facebook.react.bridge.ModuleSpec;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.internal.turbomodule.core.TurboModuleManagerDelegate;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.turbomodule.core.interfaces.TurboModule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Provider;
import r1.C0670b;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
public abstract class V extends TurboModuleManagerDelegate {
    private final List<b> mModuleProviders;
    private final Map<b, Map<String, ReactModuleInfo>> mPackageModuleInfos;
    private List<O> mPackages;
    private ReactApplicationContext mReactContext;
    private final boolean mShouldEnableLegacyModuleInterop;

    public static abstract class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private List f9160a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private ReactApplicationContext f9161b;

        public V a() {
            C0210a.d(this.f9161b, "The ReactApplicationContext must be provided to create ReactPackageTurboModuleManagerDelegate");
            C0210a.d(this.f9160a, "A set of ReactPackages must be provided to create ReactPackageTurboModuleManagerDelegate");
            return b(this.f9161b, this.f9160a);
        }

        protected abstract V b(ReactApplicationContext reactApplicationContext, List list);

        public a c(List list) {
            this.f9160a = new ArrayList(list);
            return this;
        }

        public a d(ReactApplicationContext reactApplicationContext) {
            this.f9161b = reactApplicationContext;
            return this;
        }
    }

    interface b {
        NativeModule getModule(String str);
    }

    protected V(ReactApplicationContext reactApplicationContext, List list, HybridData hybridData) {
        super(hybridData);
        this.mModuleProviders = new ArrayList();
        this.mPackageModuleInfos = new HashMap();
        this.mShouldEnableLegacyModuleInterop = C0670b.c() && C0670b.s();
        c(reactApplicationContext, list);
    }

    private void c(final ReactApplicationContext reactApplicationContext, List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            O o3 = (O) it.next();
            if (o3 instanceof AbstractC0493a) {
                final AbstractC0493a abstractC0493a = (AbstractC0493a) o3;
                b bVar = new b() { // from class: d1.S
                    @Override // d1.V.b
                    public final NativeModule getModule(String str) {
                        return V.d(abstractC0493a, reactApplicationContext, str);
                    }
                };
                this.mModuleProviders.add(bVar);
                this.mPackageModuleInfos.put(bVar, abstractC0493a.i().a());
            } else if (f() && (o3 instanceof AbstractC0500h)) {
                AbstractC0500h abstractC0500h = (AbstractC0500h) o3;
                List<ModuleSpec> listC = abstractC0500h.c(reactApplicationContext);
                final HashMap map = new HashMap();
                for (ModuleSpec moduleSpec : listC) {
                    map.put(moduleSpec.getName(), moduleSpec.getProvider());
                }
                b bVar2 = new b() { // from class: d1.T
                    @Override // d1.V.b
                    public final NativeModule getModule(String str) {
                        return V.e(map, str);
                    }
                };
                this.mModuleProviders.add(bVar2);
                this.mPackageModuleInfos.put(bVar2, abstractC0500h.d().a());
            } else if (f()) {
                List<NativeModule> listE = o3.e(reactApplicationContext);
                final HashMap map2 = new HashMap();
                HashMap map3 = new HashMap();
                for (NativeModule nativeModule : listE) {
                    Class<?> cls = nativeModule.getClass();
                    InterfaceC0756a interfaceC0756a = (InterfaceC0756a) cls.getAnnotation(InterfaceC0756a.class);
                    String strName = interfaceC0756a != null ? interfaceC0756a.name() : nativeModule.getName();
                    map3.put(strName, interfaceC0756a != null ? new ReactModuleInfo(strName, cls.getName(), interfaceC0756a.canOverrideExistingModule(), true, interfaceC0756a.isCxxModule(), ReactModuleInfo.b(cls)) : new ReactModuleInfo(strName, cls.getName(), nativeModule.canOverrideExistingModule(), true, CxxModuleWrapper.class.isAssignableFrom(cls), ReactModuleInfo.b(cls)));
                    map2.put(strName, nativeModule);
                }
                b bVar3 = new b() { // from class: d1.U
                    @Override // d1.V.b
                    public final NativeModule getModule(String str) {
                        return (NativeModule) map2.get(str);
                    }
                };
                this.mModuleProviders.add(bVar3);
                this.mPackageModuleInfos.put(bVar3, map3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ NativeModule d(AbstractC0493a abstractC0493a, ReactApplicationContext reactApplicationContext, String str) {
        return abstractC0493a.g(str, reactApplicationContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ NativeModule e(Map map, String str) {
        Provider provider = (Provider) map.get(str);
        if (provider != null) {
            return (NativeModule) provider.get();
        }
        return null;
    }

    private boolean f() {
        return unstable_shouldEnableLegacyModuleInterop();
    }

    @Override // com.facebook.react.internal.turbomodule.core.TurboModuleManagerDelegate
    public List<String> getEagerInitModuleNames() {
        ArrayList arrayList = new ArrayList();
        Iterator<b> it = this.mModuleProviders.iterator();
        while (it.hasNext()) {
            for (ReactModuleInfo reactModuleInfo : this.mPackageModuleInfos.get(it.next()).values()) {
                if (reactModuleInfo.e() && reactModuleInfo.g()) {
                    arrayList.add(reactModuleInfo.f());
                }
            }
        }
        return arrayList;
    }

    @Override // com.facebook.react.internal.turbomodule.core.TurboModuleManagerDelegate
    public NativeModule getLegacyModule(String str) {
        if (!unstable_shouldEnableLegacyModuleInterop()) {
            return null;
        }
        NativeModule nativeModule = null;
        for (b bVar : this.mModuleProviders) {
            ReactModuleInfo reactModuleInfo = this.mPackageModuleInfos.get(bVar).get(str);
            if (reactModuleInfo != null && !reactModuleInfo.e() && (nativeModule == null || reactModuleInfo.a())) {
                NativeModule module = bVar.getModule(str);
                if (module != null) {
                    nativeModule = module;
                }
            }
        }
        if (nativeModule instanceof TurboModule) {
            return null;
        }
        return nativeModule;
    }

    @Override // com.facebook.react.internal.turbomodule.core.TurboModuleManagerDelegate
    public TurboModule getModule(String str) {
        NativeModule nativeModule = null;
        for (b bVar : this.mModuleProviders) {
            ReactModuleInfo reactModuleInfo = this.mPackageModuleInfos.get(bVar).get(str);
            if (reactModuleInfo != null && reactModuleInfo.e() && (nativeModule == null || reactModuleInfo.a())) {
                NativeModule module = bVar.getModule(str);
                if (module != null) {
                    nativeModule = module;
                }
            }
        }
        if (nativeModule instanceof TurboModule) {
            return (TurboModule) nativeModule;
        }
        return null;
    }

    @Override // com.facebook.react.internal.turbomodule.core.TurboModuleManagerDelegate
    public boolean unstable_isLegacyModuleRegistered(String str) {
        Iterator<b> it = this.mModuleProviders.iterator();
        while (it.hasNext()) {
            ReactModuleInfo reactModuleInfo = this.mPackageModuleInfos.get(it.next()).get(str);
            if (reactModuleInfo != null && !reactModuleInfo.e()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.facebook.react.internal.turbomodule.core.TurboModuleManagerDelegate
    public boolean unstable_isModuleRegistered(String str) {
        Iterator<b> it = this.mModuleProviders.iterator();
        while (it.hasNext()) {
            ReactModuleInfo reactModuleInfo = this.mPackageModuleInfos.get(it.next()).get(str);
            if (reactModuleInfo != null && reactModuleInfo.e()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.facebook.react.internal.turbomodule.core.TurboModuleManagerDelegate
    public boolean unstable_shouldEnableLegacyModuleInterop() {
        return this.mShouldEnableLegacyModuleInterop;
    }
}

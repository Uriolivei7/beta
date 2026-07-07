package com.facebook.react.runtime;

import a1.C0210a;
import android.content.res.AssetManager;
import android.view.ViewGroup;
import com.facebook.fbreact.specs.NativeExceptionsManagerSpec;
import com.facebook.jni.HybridData;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.JSBundleLoader;
import com.facebook.react.bridge.JSBundleLoaderDelegate;
import com.facebook.react.bridge.JavaScriptContextHolder;
import com.facebook.react.bridge.NativeArray;
import com.facebook.react.bridge.NativeMap;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactNoCrashSoftException;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.RuntimeExecutor;
import com.facebook.react.bridge.RuntimeScheduler;
import com.facebook.react.bridge.queue.MessageQueueThread;
import com.facebook.react.bridge.queue.MessageQueueThreadSpec;
import com.facebook.react.bridge.queue.QueueThreadExceptionHandler;
import com.facebook.react.bridge.queue.ReactQueueConfiguration;
import com.facebook.react.bridge.queue.ReactQueueConfigurationImpl;
import com.facebook.react.bridge.queue.ReactQueueConfigurationSpec;
import com.facebook.react.devsupport.l0;
import com.facebook.react.fabric.ComponentFactory;
import com.facebook.react.fabric.FabricUIManager;
import com.facebook.react.fabric.FabricUIManagerBinding;
import com.facebook.react.fabric.events.EventBeatManager;
import com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler;
import com.facebook.react.internal.turbomodule.core.TurboModuleManager;
import com.facebook.react.modules.core.JavaTimerManager;
import com.facebook.react.turbomodule.core.CallInvokerHolderImpl;
import com.facebook.react.turbomodule.core.NativeMethodCallInvokerHolderImpl;
import com.facebook.react.uimanager.C0463x;
import com.facebook.react.uimanager.ComponentNameResolver;
import com.facebook.react.uimanager.ComponentNameResolverBinding;
import com.facebook.react.uimanager.K0;
import com.facebook.react.uimanager.U0;
import com.facebook.react.uimanager.UIConstantsProviderBinding;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.V0;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.soloader.SoLoader;
import d1.C0497e;
import d2.C0518a;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import q1.C0650a;
import r1.C0670b;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
final class ReactInstance {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final String f7122h = "ReactInstance";

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static volatile boolean f7123i;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final C0394b f7124a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ReactQueueConfiguration f7125b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final TurboModuleManager f7126c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final FabricUIManager f7127d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final JavaTimerManager f7128e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final b f7129f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final JavaScriptContextHolder f7130g;
    private final HybridData mHybridData;

    class a implements JSBundleLoaderDelegate {
        a() {
        }

        @Override // com.facebook.react.bridge.JSBundleLoaderDelegate
        public void loadScriptFromAssets(AssetManager assetManager, String str, boolean z3) {
            ReactInstance.this.f7124a.d(str);
            ReactInstance.this.loadJSBundleFromAssets(assetManager, str);
        }

        @Override // com.facebook.react.bridge.JSBundleLoaderDelegate
        public void loadScriptFromFile(String str, String str2, boolean z3) {
            ReactInstance.this.f7124a.d(str2);
            ReactInstance.this.loadJSBundleFromFile(str, str2);
        }

        @Override // com.facebook.react.bridge.JSBundleLoaderDelegate
        public void loadSplitBundleFromFile(String str, String str2) {
            ReactInstance.this.loadJSBundleFromFile(str, str2);
        }

        @Override // com.facebook.react.bridge.JSBundleLoaderDelegate
        public void setSourceURLs(String str, String str2) {
            ReactInstance.this.f7124a.d(str);
        }
    }

    private static class b implements V0 {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final List f7132a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final C0394b f7133b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final Map f7134c = new HashMap();

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private Map f7135d = null;

        public b(List<d1.O> list, C0394b c0394b) {
            this.f7132a = list;
            this.f7133b = c0394b;
        }

        private ViewManager d(String str) {
            ViewManager viewManagerA;
            if (this.f7134c.containsKey(str)) {
                return (ViewManager) this.f7134c.get(str);
            }
            for (d1.O o3 : this.f7132a) {
                if ((o3 instanceof d1.c0) && (viewManagerA = ((d1.c0) o3).a(this.f7133b, str)) != null) {
                    this.f7134c.put(str, viewManagerA);
                    return viewManagerA;
                }
            }
            return null;
        }

        @Override // com.facebook.react.uimanager.V0
        public synchronized ViewManager a(String str) {
            ViewManager viewManagerD = d(str);
            if (viewManagerD != null) {
                return viewManagerD;
            }
            return (ViewManager) c().get(str);
        }

        @Override // com.facebook.react.uimanager.V0
        public synchronized Collection b() {
            HashSet hashSet;
            hashSet = new HashSet();
            hashSet.addAll(e());
            hashSet.addAll(c().keySet());
            return hashSet;
        }

        public synchronized Map c() {
            try {
                Map map = this.f7135d;
                if (map != null) {
                    return map;
                }
                HashMap map2 = new HashMap();
                for (d1.O o3 : this.f7132a) {
                    if (!(o3 instanceof d1.c0)) {
                        for (ViewManager viewManager : o3.f(this.f7133b)) {
                            map2.put(viewManager.getName(), viewManager);
                        }
                    }
                }
                this.f7135d = map2;
                return map2;
            } catch (Throwable th) {
                throw th;
            }
        }

        public synchronized Collection e() {
            HashSet hashSet;
            Collection collectionD;
            hashSet = new HashSet();
            for (d1.O o3 : this.f7132a) {
                if ((o3 instanceof d1.c0) && (collectionD = ((d1.c0) o3).d(this.f7133b)) != null) {
                    hashSet.addAll(collectionD);
                }
            }
            return hashSet;
        }
    }

    private class c implements ReactJsExceptionHandler {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final QueueThreadExceptionHandler f7136a;

        c(QueueThreadExceptionHandler queueThreadExceptionHandler) {
            this.f7136a = queueThreadExceptionHandler;
        }

        @Override // com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler
        public void reportJsException(ReactJsExceptionHandler.ProcessedError processedError) {
            try {
                ((NativeExceptionsManagerSpec) C0210a.c(ReactInstance.this.f7126c.getModule(NativeExceptionsManagerSpec.NAME))).reportException(l0.b(processedError));
            } catch (Exception e4) {
                this.f7136a.handleException(e4);
            }
        }
    }

    static {
        A();
    }

    ReactInstance(C0394b c0394b, InterfaceC0398f interfaceC0398f, ComponentFactory componentFactory, k1.e eVar, QueueThreadExceptionHandler queueThreadExceptionHandler, boolean z3, ReactHostInspectorTarget reactHostInspectorTarget) {
        this.f7124a = c0394b;
        C0518a.c(0L, "ReactInstance.initialize");
        ReactQueueConfigurationImpl reactQueueConfigurationImplCreate = ReactQueueConfigurationImpl.create(new ReactQueueConfigurationSpec(MessageQueueThreadSpec.newBackgroundThreadSpec("v_native"), MessageQueueThreadSpec.newBackgroundThreadSpec("v_js")), queueThreadExceptionHandler);
        this.f7125b = reactQueueConfigurationImplCreate;
        Y.a.b(f7122h, "Calling initializeMessageQueueThreads()");
        c0394b.initializeMessageQueueThreads(reactQueueConfigurationImplCreate);
        MessageQueueThread jSQueueThread = reactQueueConfigurationImplCreate.getJSQueueThread();
        MessageQueueThread nativeModulesQueueThread = reactQueueConfigurationImplCreate.getNativeModulesQueueThread();
        com.facebook.react.modules.core.b.i(C0650a.b());
        eVar.u();
        JSTimerExecutor jSTimerExecutorCreateJSTimerExecutor = createJSTimerExecutor();
        JavaTimerManager javaTimerManager = new JavaTimerManager(c0394b, jSTimerExecutorCreateJSTimerExecutor, com.facebook.react.modules.core.b.h(), eVar);
        this.f7128e = javaTimerManager;
        this.mHybridData = initHybrid(interfaceC0398f.d(), jSQueueThread, nativeModulesQueueThread, javaTimerManager, jSTimerExecutorCreateJSTimerExecutor, new c(queueThreadExceptionHandler), interfaceC0398f.getBindingsInstaller(), C0518a.j(0L), reactHostInspectorTarget);
        this.f7130g = new JavaScriptContextHolder(getJavaScriptContext());
        C0518a.c(0L, "ReactInstance.initialize#initTurboModules");
        ArrayList arrayList = new ArrayList();
        arrayList.add(new C0397e(c0394b.c(), c0394b.b()));
        if (z3) {
            arrayList.add(new C0497e());
        }
        arrayList.addAll(interfaceC0398f.f());
        d1.V vA = interfaceC0398f.c().c(arrayList).d(c0394b).a();
        RuntimeExecutor unbufferedRuntimeExecutor = getUnbufferedRuntimeExecutor();
        this.f7126c = new TurboModuleManager(unbufferedRuntimeExecutor, vA, getJSCallInvokerHolder(), getNativeMethodCallInvokerHolder());
        C0518a.i(0L);
        C0518a.c(0L, "ReactInstance.initialize#initFabric");
        b bVar = new b(arrayList, c0394b);
        this.f7129f = bVar;
        ComponentNameResolverBinding.install(unbufferedRuntimeExecutor, new ComponentNameResolver() { // from class: com.facebook.react.runtime.Y
            @Override // com.facebook.react.uimanager.ComponentNameResolver
            public final String[] getComponentNames() {
                return this.f7148a.v();
            }
        });
        if (C0670b.q()) {
            final HashMap map = new HashMap();
            UIConstantsProviderBinding.install(unbufferedRuntimeExecutor, new UIConstantsProviderBinding.DefaultEventTypesProvider() { // from class: com.facebook.react.runtime.Z
                @Override // com.facebook.react.uimanager.UIConstantsProviderBinding.DefaultEventTypesProvider
                public final NativeMap getDefaultEventTypes() {
                    return ReactInstance.w();
                }
            }, new UIConstantsProviderBinding.ConstantsForViewManagerProvider() { // from class: com.facebook.react.runtime.a0
                @Override // com.facebook.react.uimanager.UIConstantsProviderBinding.ConstantsForViewManagerProvider
                public final NativeMap getConstantsForViewManager(String str) {
                    return this.f7158a.x(map, str);
                }
            }, new UIConstantsProviderBinding.ConstantsProvider() { // from class: com.facebook.react.runtime.b0
                @Override // com.facebook.react.uimanager.UIConstantsProviderBinding.ConstantsProvider
                public final NativeMap getConstants() {
                    return this.f7165a.y(map);
                }
            });
        }
        EventBeatManager eventBeatManager = new EventBeatManager();
        FabricUIManager fabricUIManager = new FabricUIManager(c0394b, new U0(bVar), eventBeatManager);
        this.f7127d = fabricUIManager;
        C0463x.f(c0394b);
        new FabricUIManagerBinding().i(getBufferedRuntimeExecutor(), getRuntimeScheduler(), fabricUIManager, eventBeatManager, componentFactory);
        fabricUIManager.initialize();
        C0518a.i(0L);
        C0518a.i(0L);
    }

    private static synchronized void A() {
        if (!f7123i) {
            SoLoader.t("rninstance");
            f7123i = true;
        }
    }

    private static native JSTimerExecutor createJSTimerExecutor();

    private native long getJavaScriptContext();

    private native NativeMethodCallInvokerHolderImpl getNativeMethodCallInvokerHolder();

    private native RuntimeScheduler getRuntimeScheduler();

    private native RuntimeExecutor getUnbufferedRuntimeExecutor();

    private native void handleMemoryPressureJs(int i3);

    private native HybridData initHybrid(JSRuntimeFactory jSRuntimeFactory, MessageQueueThread messageQueueThread, MessageQueueThread messageQueueThread2, JavaTimerManager javaTimerManager, JSTimerExecutor jSTimerExecutor, ReactJsExceptionHandler reactJsExceptionHandler, BindingsInstaller bindingsInstaller, boolean z3, ReactHostInspectorTarget reactHostInspectorTarget);

    private native void installGlobals(boolean z3);

    /* JADX INFO: Access modifiers changed from: private */
    public native void loadJSBundleFromAssets(AssetManager assetManager, String str);

    /* JADX INFO: Access modifiers changed from: private */
    public native void loadJSBundleFromFile(String str, String str2);

    private native void registerSegmentNative(int i3, String str);

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void u() {
        C0518a.c(0L, "initializeEagerTurboModules");
        Iterator<String> it = this.f7126c.getEagerInitModuleNames().iterator();
        while (it.hasNext()) {
            this.f7126c.getModule(it.next());
        }
        C0518a.i(0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String[] v() {
        Collection collectionB = this.f7129f.b();
        if (collectionB.size() >= 1) {
            return (String[]) collectionB.toArray(new String[0]);
        }
        Y.a.m(f7122h, "No ViewManager names found");
        return new String[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ NativeMap w() {
        return Arguments.makeNativeMap((Map<String, Object>) K0.d());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ NativeMap x(Map map, String str) {
        ViewManager viewManagerA = this.f7129f.a(str);
        if (viewManagerA == null) {
            return null;
        }
        return (NativeMap) UIManagerModule.getConstantsForViewManager(viewManagerA, map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ NativeMap y(Map map) {
        Map<String, Object> mapCreateConstants = UIManagerModule.createConstants(new ArrayList(this.f7129f.c().values()), null, map);
        Collection collectionE = this.f7129f.e();
        if (collectionE.size() > 0) {
            mapCreateConstants.put("ViewManagerNames", new ArrayList(collectionE));
            mapCreateConstants.put("LazyViewManagersEnabled", Boolean.TRUE);
        }
        return Arguments.makeNativeMap(mapCreateConstants);
    }

    public void B(int i3, String str) {
        registerSegmentNative(i3, str);
    }

    void C(e0 e0Var) {
        String str = f7122h;
        Y.a.b(str, "startSurface() is called with surface: " + e0Var.n());
        C0518a.c(0L, "ReactInstance.startSurface");
        ViewGroup viewGroupA = e0Var.a();
        if (viewGroupA == null) {
            throw new IllegalStateException("Starting surface without a view is not supported, use prerenderSurface instead.");
        }
        if (viewGroupA.getId() != -1) {
            ReactSoftExceptionLogger.logSoftException(str, new com.facebook.react.uimanager.P("surfaceView's is NOT equal to View.NO_ID before calling startSurface."));
            viewGroupA.setId(-1);
        }
        if (e0Var.q()) {
            this.f7127d.attachRootView(e0Var.m(), viewGroupA);
        } else {
            this.f7127d.startSurface(e0Var.m(), e0Var.h(), viewGroupA);
        }
        C0518a.i(0L);
    }

    void D(e0 e0Var) {
        Y.a.b(f7122h, "stopSurface() is called with surface: " + e0Var.n());
        this.f7127d.stopSurface(e0Var.m());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void callFunctionOnModule(String str, String str2, NativeArray nativeArray);

    native RuntimeExecutor getBufferedRuntimeExecutor();

    native CallInvokerHolderImpl getJSCallInvokerHolder();

    void j() {
        Y.a.b(f7122h, "ReactInstance.destroy() is called.");
        this.f7125b.destroy();
        this.f7126c.invalidate();
        this.f7127d.invalidate();
        this.f7128e.x();
        this.mHybridData.resetNative();
        this.f7130g.clear();
    }

    EventDispatcher k() {
        return this.f7127d.getEventDispatcher();
    }

    JavaScriptContextHolder l() {
        return this.f7130g;
    }

    public NativeModule m(Class cls) {
        InterfaceC0756a interfaceC0756a = (InterfaceC0756a) cls.getAnnotation(InterfaceC0756a.class);
        if (interfaceC0756a != null) {
            return n(interfaceC0756a.name());
        }
        return null;
    }

    public NativeModule n(String str) {
        NativeModule module;
        synchronized (this.f7126c) {
            module = this.f7126c.getModule(str);
        }
        return module;
    }

    public Collection o() {
        return new ArrayList(this.f7126c.getModules());
    }

    public ReactQueueConfiguration p() {
        return this.f7125b;
    }

    FabricUIManager q() {
        return this.f7127d;
    }

    public void r(int i3) {
        try {
            handleMemoryPressureJs(i3);
        } catch (NullPointerException unused) {
            ReactSoftExceptionLogger.logSoftException(f7122h, new ReactNoCrashSoftException("Native method handleMemoryPressureJs is called earlier than librninstance.so got ready."));
        }
    }

    public boolean s(Class cls) {
        InterfaceC0756a interfaceC0756a = (InterfaceC0756a) cls.getAnnotation(InterfaceC0756a.class);
        if (interfaceC0756a != null) {
            return this.f7126c.hasModule(interfaceC0756a.name());
        }
        return false;
    }

    void t() {
        this.f7125b.getNativeModulesQueueThread().runOnQueue(new Runnable() { // from class: com.facebook.react.runtime.X
            @Override // java.lang.Runnable
            public final void run() {
                this.f7147b.u();
            }
        });
    }

    native void unregisterFromInspector();

    public void z(JSBundleLoader jSBundleLoader) {
        C0518a.c(0L, "ReactInstance.loadJSBundle");
        jSBundleLoader.loadScript(new a());
        C0518a.i(0L);
    }
}

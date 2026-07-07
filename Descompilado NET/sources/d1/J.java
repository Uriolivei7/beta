package d1;

import a1.C0210a;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BridgeReactContext;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.CatalystInstanceImpl;
import com.facebook.react.bridge.JSBundleLoader;
import com.facebook.react.bridge.JSExceptionHandler;
import com.facebook.react.bridge.JavaScriptExecutor;
import com.facebook.react.bridge.JavaScriptExecutorFactory;
import com.facebook.react.bridge.NativeModuleRegistry;
import com.facebook.react.bridge.NotThreadSafeBridgeIdleDebugListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactCxxErrorHandler;
import com.facebook.react.bridge.ReactInstanceManagerInspectorTarget;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.react.bridge.ReactNoCrashSoftException;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.bridge.UIManagerProvider;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.queue.ReactQueueConfigurationSpec;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.devsupport.InspectorFlags;
import com.facebook.react.devsupport.inspector.InspectorNetworkRequestListener;
import com.facebook.react.internal.turbomodule.core.TurboModuleManager;
import com.facebook.react.modules.appearance.AppearanceModule;
import com.facebook.react.modules.appregistry.AppRegistry;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.C0463x;
import com.facebook.react.uimanager.H0;
import com.facebook.react.uimanager.InterfaceC0447o0;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.soloader.SoLoader;
import d1.V;
import d2.C0518a;
import d2.C0519b;
import j0.C0569c;
import j1.C0570a;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import k0.C0582a;
import k1.InterfaceC0584b;
import k1.InterfaceC0585c;
import k1.e;
import q1.C0650a;
import q1.InterfaceC0651b;
import r1.C0670b;

/* JADX INFO: loaded from: classes.dex */
public class J {

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private static final String f9076E = "J";

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private final V.a f9077A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private List f9078B;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private volatile LifecycleState f9082b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private f f9083c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private volatile Thread f9084d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final JavaScriptExecutorFactory f9085e;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final JSBundleLoader f9087g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final String f9088h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final List f9089i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final k1.e f9090j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final boolean f9091k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final boolean f9092l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final boolean f9093m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final NotThreadSafeBridgeIdleDebugListener f9094n;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private volatile ReactContext f9096p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private final Context f9097q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private B1.a f9098r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private Activity f9099s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private ReactInstanceManagerInspectorTarget f9100t;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private final ComponentCallbacks2C0501i f9104x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private final JSExceptionHandler f9105y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private final UIManagerProvider f9106z;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Set f9081a = Collections.synchronizedSet(new HashSet());

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Collection f9086f = null;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private final Object f9095o = new Object();

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private final Collection f9101u = Collections.synchronizedList(new ArrayList());

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private volatile boolean f9102v = false;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private volatile Boolean f9103w = Boolean.FALSE;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private boolean f9079C = true;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private volatile boolean f9080D = false;

    class a implements B1.a {
        a() {
        }

        @Override // B1.a
        public void c() {
            J.this.K();
        }
    }

    class c implements k1.g {
        c() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void c(boolean z3) {
            if (J.this.f9080D) {
                return;
            }
            if (z3) {
                J.this.f9090j.s();
            } else if (!J.this.f9090j.v() || J.this.f9079C) {
                J.this.m0();
            } else {
                J.this.f0();
            }
        }

        @Override // k1.g
        public void a(final boolean z3) {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: d1.K
                @Override // java.lang.Runnable
                public final void run() {
                    this.f9118b.c(z3);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class e implements ReactInstanceManagerInspectorTarget.TargetDelegate {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private WeakReference f9112a;

        class a implements e.a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ J f9113a;

            a(J j3) {
                this.f9113a = j3;
            }

            @Override // k1.e.a
            public void a() {
                UiThreadUtil.assertOnUiThread();
                if (this.f9113a.f9100t != null) {
                    this.f9113a.f9100t.sendDebuggerResumeCommand();
                }
            }
        }

        public e(J j3) {
            this.f9112a = new WeakReference(j3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void b() {
            J j3 = (J) this.f9112a.get();
            if (j3 != null) {
                j3.f9090j.s();
            }
        }

        @Override // com.facebook.react.bridge.ReactInstanceManagerInspectorTarget.TargetDelegate
        public Map getMetadata() {
            J j3 = (J) this.f9112a.get();
            return com.facebook.react.modules.systeminfo.a.e(j3 != null ? j3.f9097q : null);
        }

        @Override // com.facebook.react.bridge.ReactInstanceManagerInspectorTarget.TargetDelegate
        public void loadNetworkResource(String str, InspectorNetworkRequestListener inspectorNetworkRequestListener) {
            C0570a.a(str, inspectorNetworkRequestListener);
        }

        @Override // com.facebook.react.bridge.ReactInstanceManagerInspectorTarget.TargetDelegate
        public void onReload() {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: d1.L
                @Override // java.lang.Runnable
                public final void run() {
                    this.f9120b.b();
                }
            });
        }

        @Override // com.facebook.react.bridge.ReactInstanceManagerInspectorTarget.TargetDelegate
        public void onSetPausedInDebuggerMessage(String str) {
            J j3 = (J) this.f9112a.get();
            if (j3 == null) {
                return;
            }
            if (str == null) {
                j3.f9090j.e();
            } else {
                j3.f9090j.d(str, new a(j3));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final JavaScriptExecutorFactory f9115a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final JSBundleLoader f9116b;

        public f(JavaScriptExecutorFactory javaScriptExecutorFactory, JSBundleLoader jSBundleLoader) {
            this.f9115a = (JavaScriptExecutorFactory) C0210a.c(javaScriptExecutorFactory);
            this.f9116b = (JSBundleLoader) C0210a.c(jSBundleLoader);
        }

        public JSBundleLoader a() {
            return this.f9116b;
        }

        public JavaScriptExecutorFactory b() {
            return this.f9115a;
        }
    }

    J(Context context, Activity activity, B1.a aVar, JavaScriptExecutorFactory javaScriptExecutorFactory, JSBundleLoader jSBundleLoader, String str, List list, boolean z3, com.facebook.react.devsupport.H h3, boolean z4, boolean z5, NotThreadSafeBridgeIdleDebugListener notThreadSafeBridgeIdleDebugListener, LifecycleState lifecycleState, JSExceptionHandler jSExceptionHandler, k1.i iVar, boolean z6, InterfaceC0584b interfaceC0584b, int i3, int i4, UIManagerProvider uIManagerProvider, Map map, V.a aVar2, e1.k kVar, InterfaceC0585c interfaceC0585c, InterfaceC0651b interfaceC0651b, k1.h hVar) {
        Y.a.b(f9076E, "ReactInstanceManager.ctor()");
        J(context);
        C0463x.f(context);
        this.f9097q = context;
        this.f9099s = activity;
        this.f9098r = aVar;
        this.f9085e = javaScriptExecutorFactory;
        this.f9087g = jSBundleLoader;
        this.f9088h = str;
        ArrayList arrayList = new ArrayList();
        this.f9089i = arrayList;
        this.f9091k = z3;
        this.f9092l = z4;
        this.f9093m = z5;
        C0518a.c(0L, "ReactInstanceManager.initDevSupportManager");
        k1.e eVarB = h3.b(context, w(), str, z3, iVar, interfaceC0584b, i3, map, kVar, interfaceC0585c, hVar);
        this.f9090j = eVarB;
        C0518a.i(0L);
        this.f9094n = notThreadSafeBridgeIdleDebugListener;
        this.f9082b = lifecycleState;
        this.f9104x = new ComponentCallbacks2C0501i(context);
        this.f9105y = jSExceptionHandler;
        this.f9077A = aVar2;
        synchronized (arrayList) {
            try {
                C0569c.a().c(C0582a.f9581d, "RNCore: Use Split Packages");
                arrayList.add(new C0495c(this, new a(), z6, i4));
                if (z3) {
                    arrayList.add(new C0497e());
                }
                arrayList.addAll(list);
            } catch (Throwable th) {
                throw th;
            }
        }
        this.f9106z = uIManagerProvider;
        com.facebook.react.modules.core.b.i(interfaceC0651b != null ? interfaceC0651b : C0650a.b());
        if (z3) {
            eVarB.u();
        }
        o0();
    }

    private void B(InterfaceC0447o0 interfaceC0447o0, ReactContext reactContext) {
        Y.a.b("ReactNative", "ReactInstanceManager.detachRootViewFromInstance()");
        UiThreadUtil.assertOnUiThread();
        if (interfaceC0447o0.getState().compareAndSet(1, 0)) {
            int uIManagerType = interfaceC0447o0.getUIManagerType();
            if (uIManagerType != 2) {
                ((AppRegistry) reactContext.getCatalystInstance().getJSModule(AppRegistry.class)).unmountApplicationComponentAtRootTag(interfaceC0447o0.getRootViewTag());
                return;
            }
            int rootViewTag = interfaceC0447o0.getRootViewTag();
            if (rootViewTag != -1) {
                UIManager uIManagerG = H0.g(reactContext, uIManagerType);
                if (uIManagerG != null) {
                    uIManagerG.stopSurface(rootViewTag);
                } else {
                    Y.a.I("ReactNative", "Failed to stop surface, UIManager has already gone away");
                }
            } else {
                ReactSoftExceptionLogger.logSoftException(f9076E, new RuntimeException("detachRootViewFromInstance called with ReactRootView with invalid id"));
            }
            v(interfaceC0447o0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JavaScriptExecutorFactory E() {
        return this.f9085e;
    }

    private ReactInstanceManagerInspectorTarget F() {
        if (this.f9100t == null && InspectorFlags.getFuseboxEnabled()) {
            this.f9100t = new ReactInstanceManagerInspectorTarget(new e(this));
        }
        return this.f9100t;
    }

    static void J(Context context) {
        SoLoader.m(context, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void K() {
        UiThreadUtil.assertOnUiThread();
        B1.a aVar = this.f9098r;
        if (aVar != null) {
            aVar.c();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void L(int i3, InterfaceC0447o0 interfaceC0447o0) {
        C0518a.g(0L, "pre_rootView.onAttachedToReactInstance", i3);
        interfaceC0447o0.a(101);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void M() {
        f fVar = this.f9083c;
        if (fVar != null) {
            p0(fVar);
            this.f9083c = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void N(ReactApplicationContext reactApplicationContext) {
        try {
            q0(reactApplicationContext);
        } catch (Exception e4) {
            this.f9090j.handleException(e4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void O(f fVar) {
        ReactMarker.logMarker(ReactMarkerConstants.REACT_CONTEXT_THREAD_END);
        synchronized (this.f9103w) {
            while (this.f9103w.booleanValue()) {
                try {
                    this.f9103w.wait();
                } catch (InterruptedException unused) {
                }
            }
        }
        this.f9102v = true;
        try {
            Process.setThreadPriority(-4);
            ReactMarker.logMarker(ReactMarkerConstants.VM_INIT);
            final ReactApplicationContext reactApplicationContextX = x(fVar.b().create(), fVar.a());
            try {
                this.f9084d = null;
                ReactMarker.logMarker(ReactMarkerConstants.PRE_SETUP_REACT_CONTEXT_START);
                Runnable runnable = new Runnable() { // from class: d1.E
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f9070b.M();
                    }
                };
                reactApplicationContextX.runOnNativeModulesQueueThread(new Runnable() { // from class: d1.F
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f9071b.N(reactApplicationContextX);
                    }
                });
                UiThreadUtil.runOnUiThread(runnable);
            } catch (Exception e4) {
                this.f9090j.handleException(e4);
            }
        } catch (Exception e5) {
            this.f9102v = false;
            this.f9084d = null;
            this.f9090j.handleException(e5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void P(InterfaceC0492B[] interfaceC0492BArr, ReactApplicationContext reactApplicationContext) {
        S();
        for (InterfaceC0492B interfaceC0492B : interfaceC0492BArr) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void Q() {
        Process.setThreadPriority(0);
        ReactMarker.logMarker(ReactMarkerConstants.CHANGE_THREAD_PRIORITY, "js_default");
    }

    private synchronized void S() {
        if (this.f9082b == LifecycleState.f6518d) {
            V(true);
        }
    }

    private synchronized void T() {
        try {
            ReactContext reactContextC = C();
            if (reactContextC != null) {
                if (this.f9082b == LifecycleState.f6518d) {
                    reactContextC.onHostPause();
                    this.f9082b = LifecycleState.f6517c;
                }
                if (this.f9082b == LifecycleState.f6517c) {
                    reactContextC.onHostDestroy(this.f9093m);
                }
            }
            this.f9082b = LifecycleState.f6516b;
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void U() {
        try {
            ReactContext reactContextC = C();
            if (reactContextC != null) {
                if (this.f9082b == LifecycleState.f6516b) {
                    reactContextC.onHostResume(this.f9099s);
                    reactContextC.onHostPause();
                } else if (this.f9082b == LifecycleState.f6518d) {
                    reactContextC.onHostPause();
                }
            }
            this.f9082b = LifecycleState.f6517c;
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void V(boolean z3) {
        try {
            ReactContext reactContextC = C();
            if (reactContextC != null && (z3 || this.f9082b == LifecycleState.f6517c || this.f9082b == LifecycleState.f6516b)) {
                reactContextC.onHostResume(this.f9099s);
            }
            this.f9082b = LifecycleState.f6518d;
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f0() {
        Y.a.b("ReactNative", "ReactInstanceManager.onJSBundleLoadedFromServer()");
        l0(this.f9085e, JSBundleLoader.createCachedBundleFromNetworkLoader(this.f9090j.E(), this.f9090j.j()));
    }

    private void j0(O o3, C0502j c0502j) {
        C0519b.a(0L, "processPackage").b("className", o3.getClass().getSimpleName()).c();
        boolean z3 = o3 instanceof Q;
        if (z3) {
            ((Q) o3).b();
        }
        c0502j.b(o3);
        if (z3) {
            ((Q) o3).c();
        }
        C0519b.b(0L).c();
    }

    private NativeModuleRegistry k0(ReactApplicationContext reactApplicationContext, List list) {
        C0502j c0502j = new C0502j(reactApplicationContext);
        ReactMarker.logMarker(ReactMarkerConstants.PROCESS_PACKAGES_START);
        synchronized (this.f9089i) {
            try {
                Iterator it = list.iterator();
                while (true) {
                    if (it.hasNext()) {
                        O o3 = (O) it.next();
                        C0518a.c(0L, "createAndProcessCustomReactPackage");
                        try {
                            j0(o3, c0502j);
                            C0518a.i(0L);
                        } catch (Throwable th) {
                            C0518a.i(0L);
                            throw th;
                        }
                    }
                }
            } catch (Throwable th2) {
                throw th2;
            }
        }
        ReactMarker.logMarker(ReactMarkerConstants.PROCESS_PACKAGES_END);
        ReactMarker.logMarker(ReactMarkerConstants.BUILD_NATIVE_MODULE_REGISTRY_START);
        C0518a.c(0L, "buildNativeModuleRegistry");
        try {
            return c0502j.a();
        } finally {
            C0518a.i(0L);
            ReactMarker.logMarker(ReactMarkerConstants.BUILD_NATIVE_MODULE_REGISTRY_END);
        }
    }

    private void l0(JavaScriptExecutorFactory javaScriptExecutorFactory, JSBundleLoader jSBundleLoader) {
        Y.a.b("ReactNative", "ReactInstanceManager.recreateReactContextInBackground()");
        UiThreadUtil.assertOnUiThread();
        f fVar = new f(javaScriptExecutorFactory, jSBundleLoader);
        if (this.f9084d == null) {
            p0(fVar);
        } else {
            this.f9083c = fVar;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m0() {
        Y.a.b(f9076E, "ReactInstanceManager.recreateReactContextInBackgroundFromBundleLoader()");
        C0569c.a().c(C0582a.f9581d, "RNCore: load from BundleLoader");
        l0(this.f9085e, this.f9087g);
    }

    private void n0() {
        Y.a.b(f9076E, "ReactInstanceManager.recreateReactContextInBackgroundInner()");
        C0569c.a().c(C0582a.f9581d, "RNCore: recreateReactContextInBackground");
        UiThreadUtil.assertOnUiThread();
        if (this.f9091k && this.f9088h != null) {
            this.f9090j.o();
            if (!C0518a.j(0L)) {
                if (this.f9087g == null) {
                    this.f9090j.s();
                    return;
                } else {
                    this.f9090j.k(new c());
                    return;
                }
            }
        }
        m0();
    }

    private void o0() {
        Method method;
        try {
            method = J.class.getMethod("I", Exception.class);
        } catch (NoSuchMethodException e4) {
            Y.a.n("ReactInstanceHolder", "Failed to set cxx error handler function", e4);
            method = null;
        }
        ReactCxxErrorHandler.setHandleErrorFunc(this, method);
    }

    private void p0(final f fVar) {
        Y.a.b("ReactNative", "ReactInstanceManager.runCreateReactContextOnNewThread()");
        UiThreadUtil.assertOnUiThread();
        C0210a.b(!this.f9080D, "Cannot create a new React context on an invalidated ReactInstanceManager");
        ReactMarker.logMarker(ReactMarkerConstants.REACT_BRIDGE_LOADING_START);
        synchronized (this.f9081a) {
            synchronized (this.f9095o) {
                try {
                    if (this.f9096p != null) {
                        s0(this.f9096p);
                        this.f9096p = null;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        this.f9084d = new Thread(null, new Runnable() { // from class: d1.D
            @Override // java.lang.Runnable
            public final void run() {
                this.f9068b.O(fVar);
            }
        }, "create_react_context");
        ReactMarker.logMarker(ReactMarkerConstants.REACT_CONTEXT_THREAD_START);
        this.f9084d.start();
    }

    private void q0(final ReactApplicationContext reactApplicationContext) {
        Y.a.b("ReactNative", "ReactInstanceManager.setupReactContext()");
        ReactMarker.logMarker(ReactMarkerConstants.PRE_SETUP_REACT_CONTEXT_END);
        ReactMarker.logMarker(ReactMarkerConstants.SETUP_REACT_CONTEXT_START);
        C0518a.c(0L, "setupReactContext");
        synchronized (this.f9081a) {
            try {
                synchronized (this.f9095o) {
                    this.f9096p = (ReactContext) C0210a.c(reactApplicationContext);
                }
                CatalystInstance catalystInstance = (CatalystInstance) C0210a.c(reactApplicationContext.getCatalystInstance());
                catalystInstance.initialize();
                this.f9090j.r(reactApplicationContext);
                this.f9104x.a(catalystInstance);
                ReactMarker.logMarker(ReactMarkerConstants.ATTACH_MEASURED_ROOT_VIEWS_START);
                Iterator it = this.f9081a.iterator();
                while (it.hasNext()) {
                    t((InterfaceC0447o0) it.next());
                }
                ReactMarker.logMarker(ReactMarkerConstants.ATTACH_MEASURED_ROOT_VIEWS_END);
            } catch (Throwable th) {
                throw th;
            }
        }
        final InterfaceC0492B[] interfaceC0492BArr = (InterfaceC0492B[]) this.f9101u.toArray(new InterfaceC0492B[this.f9101u.size()]);
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: d1.G
            @Override // java.lang.Runnable
            public final void run() {
                this.f9073b.P(interfaceC0492BArr, reactApplicationContext);
            }
        });
        reactApplicationContext.runOnJSQueueThread(new Runnable() { // from class: d1.H
            @Override // java.lang.Runnable
            public final void run() {
                J.Q();
            }
        });
        reactApplicationContext.runOnNativeModulesQueueThread(new Runnable() { // from class: d1.I
            @Override // java.lang.Runnable
            public final void run() {
                Process.setThreadPriority(0);
            }
        });
        C0518a.i(0L);
        ReactMarker.logMarker(ReactMarkerConstants.SETUP_REACT_CONTEXT_END);
        ReactMarker.logMarker(ReactMarkerConstants.REACT_BRIDGE_LOADING_END);
    }

    private void s0(ReactContext reactContext) {
        Y.a.b("ReactNative", "ReactInstanceManager.tearDownReactContext()");
        UiThreadUtil.assertOnUiThread();
        if (this.f9082b == LifecycleState.f6518d) {
            reactContext.onHostPause();
        }
        synchronized (this.f9081a) {
            try {
                Iterator it = this.f9081a.iterator();
                while (it.hasNext()) {
                    B((InterfaceC0447o0) it.next(), reactContext);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        this.f9104x.d(reactContext.getCatalystInstance());
        reactContext.destroy();
        this.f9090j.z(reactContext);
    }

    private void t(final InterfaceC0447o0 interfaceC0447o0) {
        final int iAddRootView;
        Y.a.b("ReactNative", "ReactInstanceManager.attachRootViewToInstance()");
        if (interfaceC0447o0.getState().compareAndSet(0, 1)) {
            C0518a.c(0L, "attachRootViewToInstance");
            UIManager uIManagerG = H0.g(this.f9096p, interfaceC0447o0.getUIManagerType());
            if (uIManagerG == null) {
                throw new IllegalStateException("Unable to attach a rootView to ReactInstance when UIManager is not properly initialized.");
            }
            Bundle appProperties = interfaceC0447o0.getAppProperties();
            if (interfaceC0447o0.getUIManagerType() == 2) {
                iAddRootView = uIManagerG.startSurface(interfaceC0447o0.getRootViewGroup(), interfaceC0447o0.getJSModuleName(), appProperties == null ? new WritableNativeMap() : Arguments.fromBundle(appProperties), interfaceC0447o0.getWidthMeasureSpec(), interfaceC0447o0.getHeightMeasureSpec());
                interfaceC0447o0.setShouldLogContentAppeared(true);
            } else {
                iAddRootView = uIManagerG.addRootView(interfaceC0447o0.getRootViewGroup(), appProperties == null ? new WritableNativeMap() : Arguments.fromBundle(appProperties));
                interfaceC0447o0.setRootViewTag(iAddRootView);
                interfaceC0447o0.d();
            }
            C0518a.a(0L, "pre_rootView.onAttachedToReactInstance", iAddRootView);
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: d1.C
                @Override // java.lang.Runnable
                public final void run() {
                    J.L(iAddRootView, interfaceC0447o0);
                }
            });
            C0518a.i(0L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void t0() {
        ReactContext reactContextC = C();
        if (reactContextC == null || !reactContextC.hasActiveReactInstance()) {
            ReactSoftExceptionLogger.logSoftException(f9076E, new ReactNoCrashSoftException("Cannot toggleElementInspector, CatalystInstance not available"));
        } else {
            reactContextC.emitDeviceEvent("toggleElementInspector");
        }
    }

    public static M u() {
        return new M();
    }

    private void v(InterfaceC0447o0 interfaceC0447o0) {
        UiThreadUtil.assertOnUiThread();
        interfaceC0447o0.getState().compareAndSet(1, 0);
        ViewGroup rootViewGroup = interfaceC0447o0.getRootViewGroup();
        rootViewGroup.removeAllViews();
        rootViewGroup.setId(-1);
    }

    private com.facebook.react.devsupport.c0 w() {
        return new b();
    }

    private ReactApplicationContext x(JavaScriptExecutor javaScriptExecutor, JSBundleLoader jSBundleLoader) {
        UIManager uIManagerCreateUIManager;
        V.a aVar;
        Y.a.b("ReactNative", "ReactInstanceManager.createReactContext()");
        ReactMarker.logMarker(ReactMarkerConstants.CREATE_REACT_CONTEXT_START, javaScriptExecutor.getName());
        BridgeReactContext bridgeReactContext = new BridgeReactContext(this.f9097q);
        JSExceptionHandler jSExceptionHandler = this.f9105y;
        if (jSExceptionHandler == null) {
            jSExceptionHandler = this.f9090j;
        }
        bridgeReactContext.setJSExceptionHandler(jSExceptionHandler);
        CatalystInstanceImpl.Builder inspectorTarget = new CatalystInstanceImpl.Builder().setReactQueueConfigurationSpec(ReactQueueConfigurationSpec.createDefault()).setJSExecutor(javaScriptExecutor).setRegistry(k0(bridgeReactContext, this.f9089i)).setJSBundleLoader(jSBundleLoader).setJSExceptionHandler(jSExceptionHandler).setInspectorTarget(F());
        ReactMarker.logMarker(ReactMarkerConstants.CREATE_CATALYST_INSTANCE_START);
        C0518a.c(0L, "createCatalystInstance");
        try {
            CatalystInstanceImpl catalystInstanceImplBuild = inspectorTarget.build();
            C0518a.i(0L);
            ReactMarker.logMarker(ReactMarkerConstants.CREATE_CATALYST_INSTANCE_END);
            bridgeReactContext.initializeWithInstance(catalystInstanceImplBuild);
            catalystInstanceImplBuild.getRuntimeScheduler();
            if (C0670b.t() && (aVar = this.f9077A) != null) {
                TurboModuleManager turboModuleManager = new TurboModuleManager(catalystInstanceImplBuild.getRuntimeExecutor(), aVar.c(this.f9089i).d(bridgeReactContext).a(), catalystInstanceImplBuild.getJSCallInvokerHolder(), catalystInstanceImplBuild.getNativeMethodCallInvokerHolder());
                catalystInstanceImplBuild.setTurboModuleRegistry(turboModuleManager);
                Iterator<String> it = turboModuleManager.getEagerInitModuleNames().iterator();
                while (it.hasNext()) {
                    turboModuleManager.getModule(it.next());
                }
            }
            UIManagerProvider uIManagerProvider = this.f9106z;
            if (uIManagerProvider != null && (uIManagerCreateUIManager = uIManagerProvider.createUIManager(bridgeReactContext)) != null) {
                catalystInstanceImplBuild.setFabricUIManager(uIManagerCreateUIManager);
                uIManagerCreateUIManager.initialize();
                catalystInstanceImplBuild.setFabricUIManager(uIManagerCreateUIManager);
            }
            NotThreadSafeBridgeIdleDebugListener notThreadSafeBridgeIdleDebugListener = this.f9094n;
            if (notThreadSafeBridgeIdleDebugListener != null) {
                catalystInstanceImplBuild.addBridgeIdleDebugListener(notThreadSafeBridgeIdleDebugListener);
            }
            if (C0518a.j(0L)) {
                catalystInstanceImplBuild.setGlobalVariable("__RCTProfileIsProfiling", "true");
            }
            ReactMarker.logMarker(ReactMarkerConstants.PRE_RUN_JS_BUNDLE_START);
            C0518a.c(0L, "runJSBundle");
            catalystInstanceImplBuild.runJSBundle();
            C0518a.i(0L);
            return bridgeReactContext;
        } catch (Throwable th) {
            C0518a.i(0L);
            ReactMarker.logMarker(ReactMarkerConstants.CREATE_CATALYST_INSTANCE_END);
            throw th;
        }
    }

    public void A(InterfaceC0447o0 interfaceC0447o0) {
        ReactContext reactContext;
        UiThreadUtil.assertOnUiThread();
        if (this.f9081a.remove(interfaceC0447o0) && (reactContext = this.f9096p) != null && reactContext.hasActiveReactInstance()) {
            B(interfaceC0447o0, reactContext);
        }
    }

    public ReactContext C() {
        ReactContext reactContext;
        synchronized (this.f9095o) {
            reactContext = this.f9096p;
        }
        return reactContext;
    }

    public k1.e D() {
        return this.f9090j;
    }

    public List G(ReactApplicationContext reactApplicationContext) {
        ReactMarker.logMarker(ReactMarkerConstants.CREATE_VIEW_MANAGERS_START);
        C0518a.c(0L, "createAllViewManagers");
        try {
            if (this.f9078B == null) {
                synchronized (this.f9089i) {
                    try {
                        if (this.f9078B == null) {
                            ArrayList arrayList = new ArrayList();
                            Iterator it = this.f9089i.iterator();
                            while (it.hasNext()) {
                                arrayList.addAll(((O) it.next()).f(reactApplicationContext));
                            }
                            this.f9078B = arrayList;
                            C0518a.i(0L);
                            ReactMarker.logMarker(ReactMarkerConstants.CREATE_VIEW_MANAGERS_END);
                            return arrayList;
                        }
                    } finally {
                    }
                }
            }
            List list = this.f9078B;
            C0518a.i(0L);
            ReactMarker.logMarker(ReactMarkerConstants.CREATE_VIEW_MANAGERS_END);
            return list;
        } catch (Throwable th) {
            C0518a.i(0L);
            ReactMarker.logMarker(ReactMarkerConstants.CREATE_VIEW_MANAGERS_END);
            throw th;
        }
    }

    public Collection H() {
        Collection collection;
        C0518a.c(0L, "ReactInstanceManager.getViewManagerNames");
        try {
            Collection collection2 = this.f9086f;
            if (collection2 != null) {
                return collection2;
            }
            synchronized (this.f9095o) {
                ReactApplicationContext reactApplicationContext = (ReactApplicationContext) C();
                if (reactApplicationContext != null && reactApplicationContext.hasActiveReactInstance()) {
                    synchronized (this.f9089i) {
                        try {
                            if (this.f9086f == null) {
                                HashSet hashSet = new HashSet();
                                for (O o3 : this.f9089i) {
                                    C0519b.a(0L, "ReactInstanceManager.getViewManagerName").b("Package", o3.getClass().getSimpleName()).c();
                                    if (o3 instanceof c0) {
                                        Collection collectionD = ((c0) o3).d(reactApplicationContext);
                                        if (collectionD != null) {
                                            hashSet.addAll(collectionD);
                                        }
                                    } else {
                                        Y.a.K("ReactNative", "Package %s is not a ViewManagerOnDemandReactPackage, view managers will not be loaded", o3.getClass().getSimpleName());
                                    }
                                    C0518a.i(0L);
                                }
                                this.f9086f = hashSet;
                            }
                            collection = this.f9086f;
                        } finally {
                        }
                    }
                    return collection;
                }
                Y.a.I("ReactNative", "Calling getViewManagerNames without active context");
                return Collections.emptyList();
            }
        } finally {
            C0518a.i(0L);
        }
    }

    public void I(Exception exc) {
        this.f9090j.handleException(exc);
    }

    public void W(Activity activity, int i3, int i4, Intent intent) {
        ReactContext reactContextC = C();
        if (reactContextC != null) {
            reactContextC.onActivityResult(activity, i3, i4, intent);
        }
    }

    public void X() {
        UiThreadUtil.assertOnUiThread();
        ReactContext reactContext = this.f9096p;
        if (reactContext == null) {
            Y.a.I(f9076E, "Instance detached from instance manager");
            K();
        } else {
            DeviceEventManagerModule deviceEventManagerModule = (DeviceEventManagerModule) reactContext.getNativeModule(DeviceEventManagerModule.class);
            if (deviceEventManagerModule != null) {
                deviceEventManagerModule.emitHardwareBackPressed();
            }
        }
    }

    public void Y(Context context, Configuration configuration) {
        AppearanceModule appearanceModule;
        UiThreadUtil.assertOnUiThread();
        ReactContext reactContextC = C();
        if (reactContextC == null || (appearanceModule = (AppearanceModule) reactContextC.getNativeModule(AppearanceModule.class)) == null) {
            return;
        }
        appearanceModule.onConfigurationChanged(context);
    }

    public void Z() {
        UiThreadUtil.assertOnUiThread();
        if (this.f9091k) {
            this.f9090j.A(false);
        }
        T();
        if (this.f9093m) {
            return;
        }
        this.f9099s = null;
    }

    public void a0(Activity activity) {
        if (activity == this.f9099s) {
            Z();
        }
    }

    public void b0() {
        UiThreadUtil.assertOnUiThread();
        this.f9098r = null;
        if (this.f9091k) {
            this.f9090j.A(false);
        }
        U();
    }

    public void c0(Activity activity) {
        if (this.f9092l) {
            if (this.f9099s == null) {
                Y.a.m(f9076E, "ReactInstanceManager.onHostPause called with null activity, expected:" + this.f9099s.getClass().getSimpleName());
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                int length = stackTrace.length;
                for (int i3 = 0; i3 < length; i3++) {
                    Y.a.m(f9076E, stackTrace[i3].toString());
                }
            }
            C0210a.a(this.f9099s != null);
        }
        Activity activity2 = this.f9099s;
        if (activity2 != null) {
            C0210a.b(activity == activity2, "Pausing an activity that is not the current activity, this is incorrect! Current activity: " + this.f9099s.getClass().getSimpleName() + " Paused activity: " + activity.getClass().getSimpleName());
        }
        b0();
    }

    public void d0(Activity activity) {
        UiThreadUtil.assertOnUiThread();
        this.f9099s = activity;
        if (this.f9091k) {
            if (activity != null) {
                View decorView = activity.getWindow().getDecorView();
                if (androidx.core.view.Z.E(decorView)) {
                    this.f9090j.A(true);
                } else {
                    decorView.addOnAttachStateChangeListener(new d(decorView));
                }
            } else if (!this.f9092l) {
                this.f9090j.A(true);
            }
        }
        V(false);
    }

    public void e0(Activity activity, B1.a aVar) {
        UiThreadUtil.assertOnUiThread();
        this.f9098r = aVar;
        d0(activity);
    }

    public void g0(Intent intent) {
        DeviceEventManagerModule deviceEventManagerModule;
        UiThreadUtil.assertOnUiThread();
        ReactContext reactContextC = C();
        if (reactContextC == null) {
            Y.a.I(f9076E, "Instance detached from instance manager");
            return;
        }
        String action = intent.getAction();
        Uri data = intent.getData();
        if (data != null && (("android.intent.action.VIEW".equals(action) || "android.nfc.action.NDEF_DISCOVERED".equals(action)) && (deviceEventManagerModule = (DeviceEventManagerModule) reactContextC.getNativeModule(DeviceEventManagerModule.class)) != null)) {
            deviceEventManagerModule.emitNewIntentReceived(data);
        }
        reactContextC.onNewIntent(this.f9099s, intent);
    }

    public void h0(Activity activity) {
        Activity activity2 = this.f9099s;
        if (activity2 == null || activity != activity2) {
            return;
        }
        UiThreadUtil.assertOnUiThread();
        ReactContext reactContextC = C();
        if (reactContextC != null) {
            reactContextC.onUserLeaveHint(activity);
        }
    }

    public void i0(boolean z3) {
        UiThreadUtil.assertOnUiThread();
        ReactContext reactContextC = C();
        if (reactContextC != null) {
            reactContextC.onWindowFocusChange(z3);
        }
    }

    public void r0() {
        UiThreadUtil.assertOnUiThread();
        this.f9090j.x();
    }

    public void s(InterfaceC0447o0 interfaceC0447o0) {
        UiThreadUtil.assertOnUiThread();
        synchronized (this.f9081a) {
            try {
                if (this.f9081a.add(interfaceC0447o0)) {
                    v(interfaceC0447o0);
                } else {
                    Y.a.m("ReactNative", "ReactRoot was attached multiple times");
                }
                ReactContext reactContextC = C();
                if (this.f9084d == null && reactContextC != null) {
                    t(interfaceC0447o0);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void y() {
        Y.a.b(f9076E, "ReactInstanceManager.createReactContextInBackground()");
        UiThreadUtil.assertOnUiThread();
        if (this.f9102v) {
            return;
        }
        this.f9102v = true;
        n0();
    }

    public ViewManager z(String str) {
        ViewManager viewManagerA;
        synchronized (this.f9095o) {
            ReactApplicationContext reactApplicationContext = (ReactApplicationContext) C();
            if (reactApplicationContext != null && reactApplicationContext.hasActiveReactInstance()) {
                synchronized (this.f9089i) {
                    try {
                        for (O o3 : this.f9089i) {
                            if ((o3 instanceof c0) && (viewManagerA = ((c0) o3).a(reactApplicationContext, str)) != null) {
                                return viewManagerA;
                            }
                        }
                        return null;
                    } finally {
                    }
                }
            }
            return null;
        }
    }

    class b implements com.facebook.react.devsupport.c0 {
        b() {
        }

        @Override // com.facebook.react.devsupport.c0
        public View a(String str) {
            Activity activityI = i();
            if (activityI == null) {
                return null;
            }
            a0 a0Var = new a0(activityI);
            a0Var.setIsFabric(C0670b.f());
            a0Var.u(J.this, str, new Bundle());
            return a0Var;
        }

        @Override // com.facebook.react.devsupport.c0
        public void b(View view) {
            if (view instanceof a0) {
                ((a0) view).v();
            }
        }

        @Override // com.facebook.react.devsupport.c0
        public void h() {
            J.this.t0();
        }

        @Override // com.facebook.react.devsupport.c0
        public Activity i() {
            return J.this.f9099s;
        }

        @Override // com.facebook.react.devsupport.c0
        public JavaScriptExecutorFactory k() {
            return J.this.E();
        }

        @Override // com.facebook.react.devsupport.c0
        public void j(String str) {
        }
    }

    class d implements View.OnAttachStateChangeListener {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f9110b;

        d(View view) {
            this.f9110b = view;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            this.f9110b.removeOnAttachStateChangeListener(this);
            J.this.f9090j.A(true);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
        }
    }
}

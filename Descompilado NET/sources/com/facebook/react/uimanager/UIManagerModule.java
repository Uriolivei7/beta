package com.facebook.react.uimanager;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.view.View;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.GuardedRunnable;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.OnBatchCompleteListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.bridge.UIManagerListener;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import d2.C0518a;
import d2.C0519b;
import e1.C0527d;
import j0.C0569c;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import k0.C0582a;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = UIManagerModule.NAME)
public class UIManagerModule extends ReactContextBaseJavaModule implements OnBatchCompleteListener, LifecycleEventListener, UIManager {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final boolean DEBUG = C0569c.a().a(C0582a.f9584g);
    public static final String NAME = "UIManager";
    public static final String TAG = "UIManagerModule";
    private int mBatchId;
    private final Map<String, Object> mCustomDirectEvents;
    private final EventDispatcher mEventDispatcher;
    private final List<L0> mListeners;
    private final e mMemoryTrimCallback;
    private final Map<String, Object> mModuleConstants;
    private final G0 mUIImplementation;
    private final CopyOnWriteArrayList<UIManagerListener> mUIManagerListeners;
    private final U0 mViewManagerRegistry;

    class a implements d {
        a() {
        }
    }

    class b extends GuardedRunnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f7397b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Object f7398c;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(ReactContext reactContext, int i3, Object obj) {
            super(reactContext);
            this.f7397b = i3;
            this.f7398c = obj;
        }

        @Override // com.facebook.react.bridge.GuardedRunnable
        public void runGuarded() {
            UIManagerModule.this.mUIImplementation.U(this.f7397b, this.f7398c);
        }
    }

    class c extends GuardedRunnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f7400b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f7401c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f7402d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(ReactContext reactContext, int i3, int i4, int i5) {
            super(reactContext);
            this.f7400b = i3;
            this.f7401c = i4;
            this.f7402d = i5;
        }

        @Override // com.facebook.react.bridge.GuardedRunnable
        public void runGuarded() {
            UIManagerModule.this.mUIImplementation.Y(this.f7400b, this.f7401c, this.f7402d);
            UIManagerModule.this.mUIImplementation.m(-1);
        }
    }

    public interface d {
    }

    private static class e implements ComponentCallbacks2 {
        @Override // android.content.ComponentCallbacks
        public void onConfigurationChanged(Configuration configuration) {
        }

        @Override // android.content.ComponentCallbacks
        public void onLowMemory() {
        }

        @Override // android.content.ComponentCallbacks2
        public void onTrimMemory(int i3) {
            if (i3 >= 60) {
                b1.b().c();
            }
        }

        private e() {
        }
    }

    public UIManagerModule(ReactApplicationContext reactApplicationContext, V0 v02, int i3) {
        super(reactApplicationContext);
        this.mMemoryTrimCallback = new e();
        this.mListeners = new ArrayList();
        this.mUIManagerListeners = new CopyOnWriteArrayList<>();
        this.mBatchId = 0;
        C0463x.f(reactApplicationContext);
        P1.e eVar = new P1.e(reactApplicationContext);
        this.mEventDispatcher = eVar;
        this.mModuleConstants = createConstants(v02);
        this.mCustomDirectEvents = J0.c();
        U0 u02 = new U0(v02);
        this.mViewManagerRegistry = u02;
        this.mUIImplementation = new G0(reactApplicationContext, u02, eVar, i3);
        reactApplicationContext.addLifecycleEventListener(this);
    }

    private static Map<String, Object> createConstants(V0 v02) {
        ReactMarker.logMarker(ReactMarkerConstants.CREATE_UI_MANAGER_MODULE_CONSTANTS_START);
        C0519b.a(0L, "CreateUIManagerConstants").b("Lazy", Boolean.TRUE).c();
        try {
            return K0.a(v02);
        } finally {
            C0518a.i(0L);
            ReactMarker.logMarker(ReactMarkerConstants.CREATE_UI_MANAGER_MODULE_CONSTANTS_END);
        }
    }

    public <T extends View> int addRootView(T t3) {
        return addRootView(t3, null);
    }

    public void addUIBlock(F0 f02) {
        this.mUIImplementation.a(f02);
    }

    @Override // com.facebook.react.bridge.UIManager
    public void addUIManagerEventListener(UIManagerListener uIManagerListener) {
        this.mUIManagerListeners.add(uIManagerListener);
    }

    @Deprecated
    public void addUIManagerListener(L0 l02) {
        this.mListeners.add(l02);
    }

    @ReactMethod
    public void clearJSResponder() {
        this.mUIImplementation.f();
    }

    @ReactMethod
    public void configureNextLayoutAnimation(ReadableMap readableMap, Callback callback, Callback callback2) {
        this.mUIImplementation.g(readableMap, callback);
    }

    @ReactMethod
    public void createView(int i3, String str, int i4, ReadableMap readableMap) {
        if (DEBUG) {
            String str2 = "(UIManager.createView) tag: " + i3 + ", class: " + str + ", props: " + readableMap;
            Y.a.b("ReactNative", str2);
            C0569c.a().c(C0582a.f9584g, str2);
        }
        this.mUIImplementation.j(i3, str, i4, readableMap);
    }

    @Override // com.facebook.react.bridge.UIManager
    @Deprecated
    public void dispatchCommand(int i3, int i4, ReadableArray readableArray) {
        this.mUIImplementation.k(i3, i4, readableArray);
    }

    @ReactMethod
    public void dispatchViewManagerCommand(int i3, Dynamic dynamic, ReadableArray readableArray) {
        UIManager uIManagerG = H0.g(getReactApplicationContext(), M1.a.a(i3));
        if (uIManagerG == null) {
            return;
        }
        if (dynamic.getType() == ReadableType.Number) {
            uIManagerG.dispatchCommand(i3, dynamic.asInt(), readableArray);
        } else if (dynamic.getType() == ReadableType.String) {
            uIManagerG.dispatchCommand(i3, dynamic.asString(), readableArray);
        }
    }

    @ReactMethod
    public void findSubviewIn(int i3, ReadableArray readableArray, Callback callback) {
        this.mUIImplementation.o(i3, Math.round(C0429f0.g(readableArray.getDouble(0))), Math.round(C0429f0.g(readableArray.getDouble(1))), callback);
    }

    @Override // com.facebook.react.bridge.BaseJavaModule
    public Map<String, Object> getConstants() {
        return this.mModuleConstants;
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    public WritableMap getConstantsForViewManager(String str) {
        ViewManager viewManagerO = this.mUIImplementation.O(str);
        if (viewManagerO == null) {
            return null;
        }
        return getConstantsForViewManager(viewManagerO, this.mCustomDirectEvents);
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    public WritableMap getDefaultEventTypes() {
        return Arguments.makeNativeMap((Map<String, Object>) K0.d());
    }

    @Deprecated
    public d getDirectEventNamesResolver() {
        return new a();
    }

    @Override // com.facebook.react.bridge.UIManager
    public EventDispatcher getEventDispatcher() {
        return this.mEventDispatcher;
    }

    @Override // com.facebook.react.bridge.NativeModule
    public String getName() {
        return NAME;
    }

    @Override // com.facebook.react.bridge.PerformanceCounter
    public Map<String, Long> getPerformanceCounters() {
        return this.mUIImplementation.p();
    }

    @Deprecated
    public G0 getUIImplementation() {
        return this.mUIImplementation;
    }

    @Deprecated
    public U0 getViewManagerRegistry_DO_NOT_USE() {
        return this.mViewManagerRegistry;
    }

    @Override // com.facebook.react.bridge.BaseJavaModule, com.facebook.react.bridge.NativeModule, com.facebook.react.turbomodule.core.interfaces.TurboModule
    public void initialize() {
        getReactApplicationContext().registerComponentCallbacks(this.mMemoryTrimCallback);
        getReactApplicationContext().registerComponentCallbacks(this.mViewManagerRegistry);
        this.mEventDispatcher.a(1, (RCTEventEmitter) getReactApplicationContext().getJSModule(RCTEventEmitter.class));
    }

    @Override // com.facebook.react.bridge.BaseJavaModule, com.facebook.react.bridge.NativeModule, com.facebook.react.turbomodule.core.interfaces.TurboModule
    public void invalidate() {
        super.invalidate();
        this.mEventDispatcher.c();
        this.mUIImplementation.B();
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        reactApplicationContext.unregisterComponentCallbacks(this.mMemoryTrimCallback);
        reactApplicationContext.unregisterComponentCallbacks(this.mViewManagerRegistry);
        b1.b().c();
        R0.b();
    }

    public void invalidateNodeLayout(int i3) {
        InterfaceC0451q0 interfaceC0451q0N = this.mUIImplementation.N(i3);
        if (interfaceC0451q0N != null) {
            interfaceC0451q0N.i();
            this.mUIImplementation.m(-1);
        } else {
            Y.a.I("ReactNative", "Warning : attempted to dirty a non-existent react shadow node. reactTag=" + i3);
        }
    }

    @ReactMethod
    public void manageChildren(int i3, ReadableArray readableArray, ReadableArray readableArray2, ReadableArray readableArray3, ReadableArray readableArray4, ReadableArray readableArray5) {
        if (DEBUG) {
            String str = "(UIManager.manageChildren) tag: " + i3 + ", moveFrom: " + readableArray + ", moveTo: " + readableArray2 + ", addTags: " + readableArray3 + ", atIndices: " + readableArray4 + ", removeFrom: " + readableArray5;
            Y.a.b("ReactNative", str);
            C0569c.a().c(C0582a.f9584g, str);
        }
        this.mUIImplementation.u(i3, readableArray, readableArray2, readableArray3, readableArray4, readableArray5);
    }

    @Override // com.facebook.react.bridge.UIManager
    public void markActiveTouchForTag(int i3, int i4) {
    }

    @ReactMethod
    public void measure(int i3, Callback callback) {
        this.mUIImplementation.v(i3, callback);
    }

    @ReactMethod
    public void measureInWindow(int i3, Callback callback) {
        this.mUIImplementation.w(i3, callback);
    }

    @ReactMethod
    public void measureLayout(int i3, int i4, Callback callback, Callback callback2) {
        this.mUIImplementation.x(i3, i4, callback, callback2);
    }

    @Override // com.facebook.react.bridge.OnBatchCompleteListener
    public void onBatchComplete() {
        int i3 = this.mBatchId;
        this.mBatchId = i3 + 1;
        C0519b.a(0L, "onBatchCompleteUI").a("BatchId", i3).c();
        Iterator<L0> it = this.mListeners.iterator();
        if (it.hasNext()) {
            androidx.activity.result.d.a(it.next());
            throw null;
        }
        Iterator<UIManagerListener> it2 = this.mUIManagerListeners.iterator();
        while (it2.hasNext()) {
            it2.next().willDispatchViewUpdates(this);
        }
        try {
            if (this.mUIImplementation.q() > 0) {
                this.mUIImplementation.m(i3);
            }
        } finally {
            C0518a.i(0L);
        }
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostDestroy() {
        this.mUIImplementation.C();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostPause() {
        this.mUIImplementation.D();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostResume() {
        this.mUIImplementation.E();
    }

    public void prependUIBlock(F0 f02) {
        this.mUIImplementation.F(f02);
    }

    @Override // com.facebook.react.bridge.PerformanceCounter
    public void profileNextBatch() {
        this.mUIImplementation.G();
    }

    @Override // com.facebook.react.bridge.UIManager
    public void receiveEvent(int i3, String str, WritableMap writableMap) {
        receiveEvent(-1, i3, str, writableMap);
    }

    @ReactMethod
    public void removeRootView(int i3) {
        this.mUIImplementation.J(i3);
    }

    @Override // com.facebook.react.bridge.UIManager
    public void removeUIManagerEventListener(UIManagerListener uIManagerListener) {
        this.mUIManagerListeners.remove(uIManagerListener);
    }

    @Deprecated
    public void removeUIManagerListener(L0 l02) {
        this.mListeners.remove(l02);
    }

    @Override // com.facebook.react.bridge.UIManager
    @Deprecated
    public String resolveCustomDirectEventName(String str) {
        Map map;
        return (str == null || (map = (Map) this.mCustomDirectEvents.get(str)) == null) ? str : (String) map.get("registrationName");
    }

    @Deprecated
    public int resolveRootTagFromReactTag(int i3) {
        return M1.a.d(i3) ? i3 : this.mUIImplementation.M(i3);
    }

    @Override // com.facebook.react.bridge.UIManager
    public View resolveView(int i3) {
        UiThreadUtil.assertOnUiThread();
        return this.mUIImplementation.r().S().v(i3);
    }

    @Override // com.facebook.react.bridge.UIManager
    @ReactMethod
    public void sendAccessibilityEvent(int i3, int i4) {
        int iA = M1.a.a(i3);
        if (iA != 2) {
            this.mUIImplementation.P(i3, i4);
            return;
        }
        UIManager uIManagerG = H0.g(getReactApplicationContext(), iA);
        if (uIManagerG != null) {
            uIManagerG.sendAccessibilityEvent(i3, i4);
        }
    }

    @ReactMethod
    public void setChildren(int i3, ReadableArray readableArray) {
        if (DEBUG) {
            String str = "(UIManager.setChildren) tag: " + i3 + ", children: " + readableArray;
            Y.a.b("ReactNative", str);
            C0569c.a().c(C0582a.f9584g, str);
        }
        this.mUIImplementation.Q(i3, readableArray);
    }

    @ReactMethod
    public void setJSResponder(int i3, boolean z3) {
        this.mUIImplementation.R(i3, z3);
    }

    @ReactMethod
    public void setLayoutAnimationEnabledExperimental(boolean z3) {
        this.mUIImplementation.S(z3);
    }

    public void setViewHierarchyUpdateDebugListener(N1.a aVar) {
        this.mUIImplementation.T(aVar);
    }

    public void setViewLocalData(int i3, Object obj) {
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        reactApplicationContext.assertOnUiQueueThread();
        reactApplicationContext.runOnNativeModulesQueueThread(new b(reactApplicationContext, i3, obj));
    }

    @Override // com.facebook.react.bridge.UIManager
    public <T extends View> int startSurface(T t3, String str, WritableMap writableMap, int i3, int i4) {
        throw new UnsupportedOperationException();
    }

    @Override // com.facebook.react.bridge.UIManager
    public void stopSurface(int i3) {
        throw new UnsupportedOperationException();
    }

    @Override // com.facebook.react.bridge.UIManager
    public void sweepActiveTouchForTag(int i3, int i4) {
    }

    @Override // com.facebook.react.bridge.UIManager
    public void synchronouslyUpdateViewOnUIThread(int i3, ReadableMap readableMap) {
        this.mUIImplementation.V(i3, new C0454s0(readableMap));
    }

    public void updateInsetsPadding(int i3, int i4, int i5, int i6, int i7) {
        getReactApplicationContext().assertOnNativeModulesQueueThread();
        this.mUIImplementation.W(i3, i4, i5, i6, i7);
    }

    public void updateNodeSize(int i3, int i4, int i5) {
        getReactApplicationContext().assertOnNativeModulesQueueThread();
        this.mUIImplementation.X(i3, i4, i5);
    }

    @Override // com.facebook.react.bridge.UIManager
    public void updateRootLayoutSpecs(int i3, int i4, int i5, int i6, int i7) {
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        reactApplicationContext.runOnNativeModulesQueueThread(new c(reactApplicationContext, i3, i4, i5));
    }

    @ReactMethod
    public void updateView(int i3, String str, ReadableMap readableMap) {
        if (DEBUG) {
            String str2 = "(UIManager.updateView) tag: " + i3 + ", class: " + str + ", props: " + readableMap;
            Y.a.b("ReactNative", str2);
            C0569c.a().c(C0582a.f9584g, str2);
        }
        this.mUIImplementation.a0(i3, str, readableMap);
    }

    @ReactMethod
    @Deprecated
    public void viewIsDescendantOf(int i3, int i4, Callback callback) {
        this.mUIImplementation.c0(i3, i4, callback);
    }

    @Override // com.facebook.react.bridge.UIManager
    public <T extends View> int addRootView(T t3, WritableMap writableMap) {
        C0518a.c(0L, "UIManagerModule.addRootView");
        int iA = C0449p0.a();
        this.mUIImplementation.H(t3, iA, new B0(getReactApplicationContext(), t3.getContext(), ((InterfaceC0447o0) t3).getSurfaceID(), -1));
        C0518a.i(0L);
        return iA;
    }

    @Override // com.facebook.react.bridge.UIManager
    public void dispatchCommand(int i3, String str, ReadableArray readableArray) {
        this.mUIImplementation.l(i3, str, readableArray);
    }

    @Override // com.facebook.react.bridge.UIManager
    public void receiveEvent(int i3, int i4, String str, WritableMap writableMap) {
        ((RCTEventEmitter) getReactApplicationContext().getJSModule(RCTEventEmitter.class)).receiveEvent(i4, str, writableMap);
    }

    public static WritableMap getConstantsForViewManager(ViewManager viewManager, Map<String, Object> map) {
        C0519b.a(0L, "UIManagerModule.getConstantsForViewManager").b("ViewManager", viewManager.getName()).b("Lazy", Boolean.TRUE).c();
        try {
            Map mapC = K0.c(viewManager, null, null, null, map);
            if (mapC != null) {
                return Arguments.makeNativeMap((Map<String, Object>) mapC);
            }
            return null;
        } finally {
            C0519b.b(0L).c();
        }
    }

    public static Map<String, Object> createConstants(List<ViewManager> list, Map<String, Object> map, Map<String, Object> map2) {
        ReactMarker.logMarker(ReactMarkerConstants.CREATE_UI_MANAGER_MODULE_CONSTANTS_START);
        C0519b.a(0L, "CreateUIManagerConstants").b("Lazy", Boolean.FALSE).c();
        try {
            return K0.b(list, map, map2);
        } finally {
            C0518a.i(0L);
            ReactMarker.logMarker(ReactMarkerConstants.CREATE_UI_MANAGER_MODULE_CONSTANTS_END);
        }
    }

    public UIManagerModule(ReactApplicationContext reactApplicationContext, List<ViewManager> list, int i3) {
        super(reactApplicationContext);
        this.mMemoryTrimCallback = new e();
        this.mListeners = new ArrayList();
        this.mUIManagerListeners = new CopyOnWriteArrayList<>();
        this.mBatchId = 0;
        C0463x.f(reactApplicationContext);
        P1.e eVar = new P1.e(reactApplicationContext);
        this.mEventDispatcher = eVar;
        HashMap mapB = C0527d.b();
        this.mCustomDirectEvents = mapB;
        this.mModuleConstants = createConstants(list, null, mapB);
        U0 u02 = new U0(list);
        this.mViewManagerRegistry = u02;
        this.mUIImplementation = new G0(reactApplicationContext, u02, eVar, i3);
        reactApplicationContext.addLifecycleEventListener(this);
    }
}

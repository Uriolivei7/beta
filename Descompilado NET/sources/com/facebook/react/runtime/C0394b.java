package com.facebook.react.runtime;

import android.content.Context;
import android.util.Log;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.JavaScriptContextHolder;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.JavaScriptModuleRegistry;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.interop.InteropModuleRegistry;
import com.facebook.react.turbomodule.core.interfaces.CallInvokerHolder;
import com.facebook.react.uimanager.events.EventDispatcher;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import r1.C0670b;

/* JADX INFO: renamed from: com.facebook.react.runtime.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0394b extends ReactApplicationContext implements P1.h {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ReactHostImpl f7160b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final AtomicReference f7161c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final String f7162d;

    /* JADX INFO: renamed from: com.facebook.react.runtime.b$a */
    private static class a implements InvocationHandler {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final ReactHostImpl f7163a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Class f7164b;

        public a(ReactHostImpl reactHostImpl, Class<? extends JavaScriptModule> cls) {
            this.f7163a = reactHostImpl;
            this.f7164b = cls;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] objArr) {
            this.f7163a.Y(JavaScriptModuleRegistry.getJSModuleName(this.f7164b), method.getName(), objArr != null ? Arguments.fromJavaArgs(objArr) : new WritableNativeArray());
            return null;
        }
    }

    C0394b(Context context, ReactHostImpl reactHostImpl) {
        super(context);
        this.f7161c = new AtomicReference();
        this.f7162d = getClass().getSimpleName();
        this.f7160b = reactHostImpl;
        if (C0670b.p()) {
            initializeInteropModules();
        }
    }

    B1.a b() {
        return this.f7160b.g0();
    }

    k1.e c() {
        return this.f7160b.b();
    }

    public void d(String str) {
        this.f7161c.set(str);
    }

    @Override // com.facebook.react.bridge.ReactContext
    public void destroy() {
    }

    @Override // com.facebook.react.bridge.ReactContext
    public void emitDeviceEvent(String str, Object obj) {
        this.f7160b.Y("RCTDeviceEventEmitter", "emit", Arguments.fromJavaArgs(new Object[]{str, obj}));
    }

    @Override // com.facebook.react.bridge.ReactContext
    public CatalystInstance getCatalystInstance() {
        Log.w(this.f7162d, "[WARNING] Bridgeless doesn't support CatalystInstance. Accessing an API that's not part of the new architecture is not encouraged usage.");
        return new BridgelessCatalystInstance(this.f7160b);
    }

    @Override // P1.h
    public EventDispatcher getEventDispatcher() {
        return this.f7160b.h0();
    }

    @Override // com.facebook.react.bridge.ReactContext
    public UIManager getFabricUIManager() {
        return this.f7160b.x0();
    }

    @Override // com.facebook.react.bridge.ReactContext
    public CallInvokerHolder getJSCallInvokerHolder() {
        return this.f7160b.i0();
    }

    @Override // com.facebook.react.bridge.ReactContext
    public JavaScriptModule getJSModule(Class cls) {
        InteropModuleRegistry interopModuleRegistry = this.mInteropModuleRegistry;
        return (interopModuleRegistry == null || !interopModuleRegistry.shouldReturnInteropModule(cls)) ? (JavaScriptModule) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new a(this.f7160b, cls)) : this.mInteropModuleRegistry.getInteropModule(cls);
    }

    @Override // com.facebook.react.bridge.ReactContext
    public JavaScriptContextHolder getJavaScriptContextHolder() {
        return this.f7160b.j0();
    }

    @Override // com.facebook.react.bridge.ReactContext
    public NativeModule getNativeModule(Class cls) {
        return this.f7160b.m0(cls);
    }

    @Override // com.facebook.react.bridge.ReactContext
    public Collection getNativeModules() {
        return this.f7160b.o0();
    }

    @Override // com.facebook.react.bridge.ReactContext
    public String getSourceURL() {
        return (String) this.f7161c.get();
    }

    @Override // com.facebook.react.bridge.ReactContext
    public void handleException(Exception exc) {
        this.f7160b.y0(exc);
    }

    @Override // com.facebook.react.bridge.ReactContext
    public boolean hasActiveCatalystInstance() {
        return hasActiveReactInstance();
    }

    @Override // com.facebook.react.bridge.ReactContext
    public boolean hasActiveReactInstance() {
        return this.f7160b.A0();
    }

    @Override // com.facebook.react.bridge.ReactContext
    public boolean hasCatalystInstance() {
        return false;
    }

    @Override // com.facebook.react.bridge.ReactContext
    public boolean hasNativeModule(Class cls) {
        return this.f7160b.z0(cls);
    }

    @Override // com.facebook.react.bridge.ReactContext
    public boolean hasReactInstance() {
        return this.f7160b.A0();
    }

    @Override // com.facebook.react.bridge.ReactContext
    public boolean isBridgeless() {
        return true;
    }

    @Override // com.facebook.react.bridge.ReactContext
    public void registerSegment(int i3, String str, Callback callback) {
        this.f7160b.w1(i3, str, callback);
    }

    @Override // com.facebook.react.bridge.ReactContext
    public NativeModule getNativeModule(String str) {
        return this.f7160b.n0(str);
    }
}

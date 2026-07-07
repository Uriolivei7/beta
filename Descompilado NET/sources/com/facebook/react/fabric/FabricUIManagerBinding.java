package com.facebook.react.fabric;

import com.facebook.jni.HybridClassBase;
import com.facebook.react.bridge.NativeMap;
import com.facebook.react.bridge.RuntimeExecutor;
import com.facebook.react.bridge.RuntimeScheduler;
import com.facebook.react.fabric.events.EventBeatManager;
import com.facebook.react.uimanager.C0429f0;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class FabricUIManagerBinding extends HybridClassBase {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final a f6812b = new a(null);

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    static {
        c.a();
    }

    public FabricUIManagerBinding() {
        initHybrid();
    }

    private final native void initHybrid();

    private final native void installFabricUIManager(RuntimeExecutor runtimeExecutor, RuntimeScheduler runtimeScheduler, FabricUIManager fabricUIManager, EventBeatManager eventBeatManager, ComponentFactory componentFactory);

    private final native void uninstallFabricUIManager();

    public final native void drainPreallocateViewsQueue();

    public final native void driveCxxAnimations();

    public final void i(RuntimeExecutor runtimeExecutor, RuntimeScheduler runtimeScheduler, FabricUIManager fabricUIManager, EventBeatManager eventBeatManager, ComponentFactory componentFactory) {
        D2.h.f(runtimeExecutor, "runtimeExecutor");
        D2.h.f(runtimeScheduler, "runtimeScheduler");
        D2.h.f(fabricUIManager, "fabricUIManager");
        D2.h.f(eventBeatManager, "eventBeatManager");
        D2.h.f(componentFactory, "componentFactory");
        fabricUIManager.setBinding(this);
        installFabricUIManager(runtimeExecutor, runtimeScheduler, fabricUIManager, eventBeatManager, componentFactory);
        setPixelDensity(C0429f0.c());
    }

    public final void j() {
        uninstallFabricUIManager();
    }

    public final native void reportMount(int i3);

    public final native void setConstraints(int i3, float f3, float f4, float f5, float f6, float f7, float f8, boolean z3, boolean z4);

    public final native void setPixelDensity(float f3);

    public final native void startSurface(int i3, String str, NativeMap nativeMap);

    public final native void startSurfaceWithConstraints(int i3, String str, NativeMap nativeMap, float f3, float f4, float f5, float f6, float f7, float f8, boolean z3, boolean z4);

    public final native void startSurfaceWithSurfaceHandler(int i3, SurfaceHandlerBinding surfaceHandlerBinding, boolean z3);

    public final native void stopSurface(int i3);

    public final native void stopSurfaceWithSurfaceHandler(SurfaceHandlerBinding surfaceHandlerBinding);
}

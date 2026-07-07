package com.facebook.react.fabric;

import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.RuntimeExecutor;
import com.facebook.react.bridge.RuntimeScheduler;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.bridge.UIManagerProvider;
import com.facebook.react.fabric.events.EventBeatManager;
import com.facebook.react.uimanager.U0;
import d2.C0518a;

/* JADX INFO: loaded from: classes.dex */
public final class f implements UIManagerProvider {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ComponentFactory f6828a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final U0 f6829b;

    public f(ComponentFactory componentFactory, U0 u02) {
        D2.h.f(componentFactory, "componentFactory");
        D2.h.f(u02, "viewManagerRegistry");
        this.f6828a = componentFactory;
        this.f6829b = u02;
    }

    @Override // com.facebook.react.bridge.UIManagerProvider
    public UIManager createUIManager(ReactApplicationContext reactApplicationContext) {
        D2.h.f(reactApplicationContext, "context");
        C0518a.c(0L, "FabricUIManagerProviderImpl.create");
        EventBeatManager eventBeatManager = new EventBeatManager();
        C0518a.c(0L, "FabricUIManagerProviderImpl.createUIManager");
        FabricUIManager fabricUIManager = new FabricUIManager(reactApplicationContext, this.f6829b, eventBeatManager);
        C0518a.i(0L);
        C0518a.c(0L, "FabricUIManagerProviderImpl.registerBinding");
        FabricUIManagerBinding fabricUIManagerBinding = new FabricUIManagerBinding();
        CatalystInstance catalystInstance = reactApplicationContext.getCatalystInstance();
        RuntimeExecutor runtimeExecutor = catalystInstance != null ? catalystInstance.getRuntimeExecutor() : null;
        RuntimeScheduler runtimeScheduler = catalystInstance != null ? catalystInstance.getRuntimeScheduler() : null;
        if (runtimeExecutor == null || runtimeScheduler == null) {
            throw new IllegalStateException("Unable to register FabricUIManager with CatalystInstance, runtimeExecutor and runtimeScheduler must not be null");
        }
        fabricUIManagerBinding.i(runtimeExecutor, runtimeScheduler, fabricUIManager, eventBeatManager, this.f6828a);
        C0518a.i(0L);
        C0518a.i(0L);
        return fabricUIManager;
    }
}

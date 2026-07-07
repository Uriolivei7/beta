package r1;

import com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider;

/* JADX INFO: renamed from: r1.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0670b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0670b f10514a = new C0670b();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static C2.a f10515b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static InterfaceC0671c f10516c;

    static {
        C2.a aVar = new C2.a() { // from class: r1.a
            @Override // C2.a
            public final Object a() {
                return C0670b.b();
            }
        };
        f10515b = aVar;
        f10516c = (InterfaceC0671c) aVar.a();
    }

    private C0670b() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final C0672d b() {
        return new C0672d();
    }

    public static final boolean c() {
        return f10516c.enableBridgelessArchitecture();
    }

    public static final boolean d() {
        return f10516c.enableEagerRootViewAttachment();
    }

    public static final boolean e() {
        return f10516c.enableFabricLogs();
    }

    public static final boolean f() {
        return f10516c.enableFabricRenderer();
    }

    public static final boolean g() {
        return f10516c.enableImagePrefetchingAndroid();
    }

    public static final boolean h() {
        return f10516c.enableNewBackgroundAndBorderDrawables();
    }

    public static final boolean i() {
        return f10516c.enablePreciseSchedulingForPremountItemsOnAndroid();
    }

    public static final boolean j() {
        return f10516c.enableViewRecycling();
    }

    public static final boolean k() {
        return f10516c.enableViewRecyclingForText();
    }

    public static final boolean l() {
        return f10516c.enableViewRecyclingForView();
    }

    public static final boolean m() {
        return f10516c.lazyAnimationCallbacks();
    }

    public static final void n(ReactNativeFeatureFlagsProvider reactNativeFeatureFlagsProvider) {
        D2.h.f(reactNativeFeatureFlagsProvider, "provider");
        f10516c.a(reactNativeFeatureFlagsProvider);
    }

    public static final boolean o() {
        return f10516c.useEditTextStockAndroidFocusBehavior();
    }

    public static final boolean p() {
        return f10516c.useFabricInterop();
    }

    public static final boolean q() {
        return f10516c.useNativeViewConfigsInBridgelessMode();
    }

    public static final boolean r() {
        return f10516c.useOptimizedEventBatchingOnAndroid();
    }

    public static final boolean s() {
        return f10516c.useTurboModuleInterop();
    }

    public static final boolean t() {
        return f10516c.useTurboModules();
    }
}

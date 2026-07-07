package r1;

import com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsCxxInterop;
import com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider;

/* JADX INFO: renamed from: r1.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0672d implements InterfaceC0671c {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private Boolean f10517A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private Boolean f10518B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private Boolean f10519C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private Boolean f10520D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private Boolean f10521E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private Boolean f10522F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private Boolean f10523G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private Boolean f10524H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    private Boolean f10525I;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    private Boolean f10526J;

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    private Boolean f10527K;

    /* JADX INFO: renamed from: L, reason: collision with root package name */
    private Boolean f10528L;

    /* JADX INFO: renamed from: M, reason: collision with root package name */
    private Boolean f10529M;

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private Boolean f10530N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    private Boolean f10531O;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    private Boolean f10532P;

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    private Boolean f10533Q;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Boolean f10534a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Boolean f10535b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Boolean f10536c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Boolean f10537d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Boolean f10538e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Boolean f10539f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private Boolean f10540g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private Boolean f10541h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private Boolean f10542i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private Boolean f10543j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private Boolean f10544k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private Boolean f10545l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private Boolean f10546m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private Boolean f10547n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private Boolean f10548o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private Boolean f10549p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private Boolean f10550q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private Boolean f10551r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private Boolean f10552s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private Boolean f10553t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private Boolean f10554u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private Boolean f10555v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private Boolean f10556w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private Boolean f10557x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private Boolean f10558y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private Boolean f10559z;

    @Override // r1.InterfaceC0671c
    public void a(ReactNativeFeatureFlagsProvider reactNativeFeatureFlagsProvider) {
        D2.h.f(reactNativeFeatureFlagsProvider, "provider");
        ReactNativeFeatureFlagsCxxInterop.override(reactNativeFeatureFlagsProvider);
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean commonTestFlag() {
        Boolean boolValueOf = this.f10534a;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.commonTestFlag());
            this.f10534a = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean disableMountItemReorderingAndroid() {
        Boolean boolValueOf = this.f10535b;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.disableMountItemReorderingAndroid());
            this.f10535b = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableAccumulatedUpdatesInRawPropsAndroid() {
        Boolean boolValueOf = this.f10536c;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableAccumulatedUpdatesInRawPropsAndroid());
            this.f10536c = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableBridgelessArchitecture() {
        Boolean boolValueOf = this.f10537d;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableBridgelessArchitecture());
            this.f10537d = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableCppPropsIteratorSetter() {
        Boolean boolValueOf = this.f10538e;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableCppPropsIteratorSetter());
            this.f10538e = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableEagerRootViewAttachment() {
        Boolean boolValueOf = this.f10539f;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableEagerRootViewAttachment());
            this.f10539f = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableFabricLogs() {
        Boolean boolValueOf = this.f10540g;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableFabricLogs());
            this.f10540g = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableFabricRenderer() {
        Boolean boolValueOf = this.f10541h;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableFabricRenderer());
            this.f10541h = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableIOSViewClipToPaddingBox() {
        Boolean boolValueOf = this.f10542i;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableIOSViewClipToPaddingBox());
            this.f10542i = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableImagePrefetchingAndroid() {
        Boolean boolValueOf = this.f10543j;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableImagePrefetchingAndroid());
            this.f10543j = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableJSRuntimeGCOnMemoryPressureOnIOS() {
        Boolean boolValueOf = this.f10544k;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableJSRuntimeGCOnMemoryPressureOnIOS());
            this.f10544k = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableLayoutAnimationsOnAndroid() {
        Boolean boolValueOf = this.f10545l;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableLayoutAnimationsOnAndroid());
            this.f10545l = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableLayoutAnimationsOnIOS() {
        Boolean boolValueOf = this.f10546m;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableLayoutAnimationsOnIOS());
            this.f10546m = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableLongTaskAPI() {
        Boolean boolValueOf = this.f10547n;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableLongTaskAPI());
            this.f10547n = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableNativeCSSParsing() {
        Boolean boolValueOf = this.f10548o;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableNativeCSSParsing());
            this.f10548o = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableNewBackgroundAndBorderDrawables() {
        Boolean boolValueOf = this.f10549p;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableNewBackgroundAndBorderDrawables());
            this.f10549p = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enablePreciseSchedulingForPremountItemsOnAndroid() {
        Boolean boolValueOf = this.f10550q;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enablePreciseSchedulingForPremountItemsOnAndroid());
            this.f10550q = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enablePropsUpdateReconciliationAndroid() {
        Boolean boolValueOf = this.f10551r;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enablePropsUpdateReconciliationAndroid());
            this.f10551r = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableReportEventPaintTime() {
        Boolean boolValueOf = this.f10552s;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableReportEventPaintTime());
            this.f10552s = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableSynchronousStateUpdates() {
        Boolean boolValueOf = this.f10553t;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableSynchronousStateUpdates());
            this.f10553t = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableUIConsistency() {
        Boolean boolValueOf = this.f10554u;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableUIConsistency());
            this.f10554u = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableViewCulling() {
        Boolean boolValueOf = this.f10555v;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableViewCulling());
            this.f10555v = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableViewRecycling() {
        Boolean boolValueOf = this.f10556w;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableViewRecycling());
            this.f10556w = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableViewRecyclingForText() {
        Boolean boolValueOf = this.f10557x;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableViewRecyclingForText());
            this.f10557x = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableViewRecyclingForView() {
        Boolean boolValueOf = this.f10558y;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.enableViewRecyclingForView());
            this.f10558y = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean excludeYogaFromRawProps() {
        Boolean boolValueOf = this.f10559z;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.excludeYogaFromRawProps());
            this.f10559z = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean fixDifferentiatorEmittingUpdatesWithWrongParentTag() {
        Boolean boolValueOf = this.f10517A;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.fixDifferentiatorEmittingUpdatesWithWrongParentTag());
            this.f10517A = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean fixMappingOfEventPrioritiesBetweenFabricAndReact() {
        Boolean boolValueOf = this.f10518B;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.fixMappingOfEventPrioritiesBetweenFabricAndReact());
            this.f10518B = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean fixMountingCoordinatorReportedPendingTransactionsOnAndroid() {
        Boolean boolValueOf = this.f10519C;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.fixMountingCoordinatorReportedPendingTransactionsOnAndroid());
            this.f10519C = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean fuseboxEnabledRelease() {
        Boolean boolValueOf = this.f10520D;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.fuseboxEnabledRelease());
            this.f10520D = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean fuseboxNetworkInspectionEnabled() {
        Boolean boolValueOf = this.f10521E;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.fuseboxNetworkInspectionEnabled());
            this.f10521E = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean lazyAnimationCallbacks() {
        Boolean boolValueOf = this.f10522F;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.lazyAnimationCallbacks());
            this.f10522F = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean removeTurboModuleManagerDelegateMutex() {
        Boolean boolValueOf = this.f10523G;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.removeTurboModuleManagerDelegateMutex());
            this.f10523G = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean throwExceptionInsteadOfDeadlockOnTurboModuleSetupDuringSyncRenderIOS() {
        Boolean boolValueOf = this.f10524H;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.throwExceptionInsteadOfDeadlockOnTurboModuleSetupDuringSyncRenderIOS());
            this.f10524H = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean traceTurboModulePromiseRejectionsOnAndroid() {
        Boolean boolValueOf = this.f10525I;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.traceTurboModulePromiseRejectionsOnAndroid());
            this.f10525I = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean useAlwaysAvailableJSErrorHandling() {
        Boolean boolValueOf = this.f10526J;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.useAlwaysAvailableJSErrorHandling());
            this.f10526J = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean useEditTextStockAndroidFocusBehavior() {
        Boolean boolValueOf = this.f10527K;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.useEditTextStockAndroidFocusBehavior());
            this.f10527K = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean useFabricInterop() {
        Boolean boolValueOf = this.f10528L;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.useFabricInterop());
            this.f10528L = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean useNativeViewConfigsInBridgelessMode() {
        Boolean boolValueOf = this.f10529M;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.useNativeViewConfigsInBridgelessMode());
            this.f10529M = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean useOptimizedEventBatchingOnAndroid() {
        Boolean boolValueOf = this.f10530N;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.useOptimizedEventBatchingOnAndroid());
            this.f10530N = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean useRawPropsJsiValue() {
        Boolean boolValueOf = this.f10531O;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.useRawPropsJsiValue());
            this.f10531O = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean useTurboModuleInterop() {
        Boolean boolValueOf = this.f10532P;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.useTurboModuleInterop());
            this.f10532P = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }

    @Override // r1.InterfaceC0671c, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean useTurboModules() {
        Boolean boolValueOf = this.f10533Q;
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(ReactNativeFeatureFlagsCxxInterop.useTurboModules());
            this.f10533Q = boolValueOf;
        }
        return boolValueOf.booleanValue();
    }
}

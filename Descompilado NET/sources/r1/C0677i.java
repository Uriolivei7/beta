package r1;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: r1.i, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0677i extends C0673e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final boolean f10563a;

    public C0677i() {
        this(false, 1, null);
    }

    @Override // r1.C0673e, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableBridgelessArchitecture() {
        return this.f10563a;
    }

    @Override // r1.C0673e, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableFabricRenderer() {
        return this.f10563a;
    }

    @Override // r1.C0673e, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean useFabricInterop() {
        return this.f10563a;
    }

    @Override // r1.C0673e, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean useNativeViewConfigsInBridgelessMode() {
        return this.f10563a || super.useNativeViewConfigsInBridgelessMode();
    }

    @Override // r1.C0673e, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean useTurboModuleInterop() {
        return this.f10563a || super.useTurboModuleInterop();
    }

    @Override // r1.C0673e, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean useTurboModules() {
        return this.f10563a;
    }

    public /* synthetic */ C0677i(boolean z3, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? true : z3);
    }

    public C0677i(boolean z3) {
        this.f10563a = z3;
    }
}

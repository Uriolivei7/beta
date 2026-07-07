package r1;

/* JADX INFO: renamed from: r1.h, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0676h extends C0677i {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final boolean f10560b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final boolean f10561c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final boolean f10562d;

    public C0676h(boolean z3, boolean z4, boolean z5) {
        super(z4);
        this.f10560b = z3;
        this.f10561c = z4;
        this.f10562d = z5;
    }

    @Override // r1.C0677i, r1.C0673e, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean enableFabricRenderer() {
        return this.f10561c || this.f10560b;
    }

    @Override // r1.C0677i, r1.C0673e, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean useFabricInterop() {
        return this.f10561c || this.f10560b;
    }

    @Override // r1.C0677i, r1.C0673e, com.facebook.react.internal.featureflags.ReactNativeFeatureFlagsProvider
    public boolean useTurboModules() {
        return this.f10561c || this.f10562d;
    }
}

package com.facebook.react.modules.fresco;

import D2.h;
import E1.c;
import E1.d;
import J0.C0185t;
import J0.C0186u;
import J0.EnumC0180n;
import M2.w;
import M2.z;
import android.content.Context;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.modules.network.g;
import com.facebook.react.turbomodule.core.interfaces.TurboModule;
import java.util.HashSet;
import kotlin.jvm.internal.DefaultConstructorMarker;
import m0.AbstractC0601d;
import m0.C0599b;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = FrescoModule.NAME, needsEagerInit = true)
public class FrescoModule extends ReactContextBaseJavaModule implements LifecycleEventListener, TurboModule {
    public static final a Companion = new a(null);
    public static final String NAME = "FrescoModule";
    private static boolean hasBeenInitialized;
    private final boolean clearOnDestroy;
    private C0186u config;
    private C0185t pipeline;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final C0186u b(ReactContext reactContext) {
            return c(reactContext).a();
        }

        public final C0186u.a c(ReactContext reactContext) {
            h.f(reactContext, "context");
            HashSet hashSet = new HashSet();
            hashSet.add(new d());
            z zVarA = g.a();
            com.facebook.react.modules.network.h.a(zVarA).d(new w(new com.facebook.react.modules.network.d()));
            Context applicationContext = reactContext.getApplicationContext();
            h.e(applicationContext, "getApplicationContext(...)");
            C0186u.a aVarT = F0.a.a(applicationContext, zVarA).S(new c(zVarA)).R(EnumC0180n.f599c).T(hashSet);
            aVarT.b().d(true);
            return aVarT;
        }

        public final boolean d() {
            return FrescoModule.hasBeenInitialized;
        }

        private a() {
        }
    }

    public FrescoModule(ReactApplicationContext reactApplicationContext) {
        this(reactApplicationContext, false, null, 6, null);
    }

    public static final C0186u.a getDefaultConfigBuilder(ReactContext reactContext) {
        return Companion.c(reactContext);
    }

    private final C0185t getImagePipeline() {
        if (this.pipeline == null) {
            this.pipeline = AbstractC0601d.a();
        }
        return this.pipeline;
    }

    public static final boolean hasBeenInitialized() {
        return Companion.d();
    }

    public void clearSensitiveData() {
        C0185t imagePipeline = getImagePipeline();
        if (imagePipeline != null) {
            imagePipeline.c();
        }
    }

    @Override // com.facebook.react.bridge.NativeModule
    public String getName() {
        return NAME;
    }

    @Override // com.facebook.react.bridge.BaseJavaModule, com.facebook.react.bridge.NativeModule, com.facebook.react.turbomodule.core.interfaces.TurboModule
    public void initialize() {
        super.initialize();
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        reactApplicationContext.addLifecycleEventListener(this);
        a aVar = Companion;
        if (!aVar.d()) {
            C0186u c0186uB = this.config;
            if (c0186uB == null) {
                h.c(reactApplicationContext);
                c0186uB = aVar.b(reactApplicationContext);
            }
            C0599b.a aVarE = C0599b.e();
            h.e(aVarE, "newBuilder(...)");
            AbstractC0601d.c(reactApplicationContext.getApplicationContext(), c0186uB, aVarE.e());
            hasBeenInitialized = true;
        } else if (this.config != null) {
            Y.a.I("ReactNative", "Fresco has already been initialized with a different config. The new Fresco configuration will be ignored!");
        }
        this.config = null;
    }

    @Override // com.facebook.react.bridge.BaseJavaModule, com.facebook.react.bridge.NativeModule, com.facebook.react.turbomodule.core.interfaces.TurboModule
    public void invalidate() {
        getReactApplicationContext().removeLifecycleEventListener(this);
        super.invalidate();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostDestroy() {
        C0185t imagePipeline;
        if (Companion.d() && this.clearOnDestroy && (imagePipeline = getImagePipeline()) != null) {
            imagePipeline.e();
        }
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostPause() {
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostResume() {
    }

    public FrescoModule(ReactApplicationContext reactApplicationContext, C0185t c0185t) {
        this(reactApplicationContext, c0185t, false, false, 12, null);
    }

    public FrescoModule(ReactApplicationContext reactApplicationContext, C0185t c0185t, boolean z3) {
        this(reactApplicationContext, c0185t, z3, false, 8, null);
    }

    public FrescoModule(ReactApplicationContext reactApplicationContext, boolean z3) {
        this(reactApplicationContext, z3, null, 4, null);
    }

    public /* synthetic */ FrescoModule(ReactApplicationContext reactApplicationContext, boolean z3, C0186u c0186u, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(reactApplicationContext, (i3 & 2) != 0 ? true : z3, (i3 & 4) != 0 ? null : c0186u);
    }

    public FrescoModule(ReactApplicationContext reactApplicationContext, boolean z3, C0186u c0186u) {
        super(reactApplicationContext);
        this.clearOnDestroy = z3;
        this.config = c0186u;
    }

    public /* synthetic */ FrescoModule(ReactApplicationContext reactApplicationContext, C0185t c0185t, boolean z3, boolean z4, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(reactApplicationContext, c0185t, (i3 & 4) != 0 ? true : z3, (i3 & 8) != 0 ? false : z4);
    }

    public FrescoModule(ReactApplicationContext reactApplicationContext, C0185t c0185t, boolean z3, boolean z4) {
        this(reactApplicationContext, z3, null, 4, null);
        this.pipeline = c0185t;
        if (z4) {
            hasBeenInitialized = true;
        }
    }
}

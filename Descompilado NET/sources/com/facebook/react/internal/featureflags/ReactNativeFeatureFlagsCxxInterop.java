package com.facebook.react.internal.featureflags;

import com.facebook.soloader.SoLoader;

/* JADX INFO: loaded from: classes.dex */
public final class ReactNativeFeatureFlagsCxxInterop {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final ReactNativeFeatureFlagsCxxInterop f6869a = new ReactNativeFeatureFlagsCxxInterop();

    static {
        SoLoader.t("react_featureflagsjni");
    }

    private ReactNativeFeatureFlagsCxxInterop() {
    }

    public static final native boolean commonTestFlag();

    public static final native String dangerouslyForceOverride(Object obj);

    public static final native void dangerouslyReset();

    public static final native boolean disableMountItemReorderingAndroid();

    public static final native boolean enableAccumulatedUpdatesInRawPropsAndroid();

    public static final native boolean enableBridgelessArchitecture();

    public static final native boolean enableCppPropsIteratorSetter();

    public static final native boolean enableEagerRootViewAttachment();

    public static final native boolean enableFabricLogs();

    public static final native boolean enableFabricRenderer();

    public static final native boolean enableIOSViewClipToPaddingBox();

    public static final native boolean enableImagePrefetchingAndroid();

    public static final native boolean enableJSRuntimeGCOnMemoryPressureOnIOS();

    public static final native boolean enableLayoutAnimationsOnAndroid();

    public static final native boolean enableLayoutAnimationsOnIOS();

    public static final native boolean enableLongTaskAPI();

    public static final native boolean enableNativeCSSParsing();

    public static final native boolean enableNewBackgroundAndBorderDrawables();

    public static final native boolean enablePreciseSchedulingForPremountItemsOnAndroid();

    public static final native boolean enablePropsUpdateReconciliationAndroid();

    public static final native boolean enableReportEventPaintTime();

    public static final native boolean enableSynchronousStateUpdates();

    public static final native boolean enableUIConsistency();

    public static final native boolean enableViewCulling();

    public static final native boolean enableViewRecycling();

    public static final native boolean enableViewRecyclingForText();

    public static final native boolean enableViewRecyclingForView();

    public static final native boolean excludeYogaFromRawProps();

    public static final native boolean fixDifferentiatorEmittingUpdatesWithWrongParentTag();

    public static final native boolean fixMappingOfEventPrioritiesBetweenFabricAndReact();

    public static final native boolean fixMountingCoordinatorReportedPendingTransactionsOnAndroid();

    public static final native boolean fuseboxEnabledRelease();

    public static final native boolean fuseboxNetworkInspectionEnabled();

    public static final native boolean lazyAnimationCallbacks();

    public static final native void override(Object obj);

    public static final native boolean removeTurboModuleManagerDelegateMutex();

    public static final native boolean throwExceptionInsteadOfDeadlockOnTurboModuleSetupDuringSyncRenderIOS();

    public static final native boolean traceTurboModulePromiseRejectionsOnAndroid();

    public static final native boolean useAlwaysAvailableJSErrorHandling();

    public static final native boolean useEditTextStockAndroidFocusBehavior();

    public static final native boolean useFabricInterop();

    public static final native boolean useNativeViewConfigsInBridgelessMode();

    public static final native boolean useOptimizedEventBatchingOnAndroid();

    public static final native boolean useRawPropsJsiValue();

    public static final native boolean useTurboModuleInterop();

    public static final native boolean useTurboModules();
}

package com.facebook.react.internal.featureflags;

/* JADX INFO: loaded from: classes.dex */
public interface ReactNativeFeatureFlagsProvider {
    boolean commonTestFlag();

    boolean disableMountItemReorderingAndroid();

    boolean enableAccumulatedUpdatesInRawPropsAndroid();

    boolean enableBridgelessArchitecture();

    boolean enableCppPropsIteratorSetter();

    boolean enableEagerRootViewAttachment();

    boolean enableFabricLogs();

    boolean enableFabricRenderer();

    boolean enableIOSViewClipToPaddingBox();

    boolean enableImagePrefetchingAndroid();

    boolean enableJSRuntimeGCOnMemoryPressureOnIOS();

    boolean enableLayoutAnimationsOnAndroid();

    boolean enableLayoutAnimationsOnIOS();

    boolean enableLongTaskAPI();

    boolean enableNativeCSSParsing();

    boolean enableNewBackgroundAndBorderDrawables();

    boolean enablePreciseSchedulingForPremountItemsOnAndroid();

    boolean enablePropsUpdateReconciliationAndroid();

    boolean enableReportEventPaintTime();

    boolean enableSynchronousStateUpdates();

    boolean enableUIConsistency();

    boolean enableViewCulling();

    boolean enableViewRecycling();

    boolean enableViewRecyclingForText();

    boolean enableViewRecyclingForView();

    boolean excludeYogaFromRawProps();

    boolean fixDifferentiatorEmittingUpdatesWithWrongParentTag();

    boolean fixMappingOfEventPrioritiesBetweenFabricAndReact();

    boolean fixMountingCoordinatorReportedPendingTransactionsOnAndroid();

    boolean fuseboxEnabledRelease();

    boolean fuseboxNetworkInspectionEnabled();

    boolean lazyAnimationCallbacks();

    boolean removeTurboModuleManagerDelegateMutex();

    boolean throwExceptionInsteadOfDeadlockOnTurboModuleSetupDuringSyncRenderIOS();

    boolean traceTurboModulePromiseRejectionsOnAndroid();

    boolean useAlwaysAvailableJSErrorHandling();

    boolean useEditTextStockAndroidFocusBehavior();

    boolean useFabricInterop();

    boolean useNativeViewConfigsInBridgelessMode();

    boolean useOptimizedEventBatchingOnAndroid();

    boolean useRawPropsJsiValue();

    boolean useTurboModuleInterop();

    boolean useTurboModules();
}

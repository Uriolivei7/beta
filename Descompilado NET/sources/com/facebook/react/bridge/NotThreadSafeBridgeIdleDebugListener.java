package com.facebook.react.bridge;

/* JADX INFO: loaded from: classes.dex */
public interface NotThreadSafeBridgeIdleDebugListener {
    void onBridgeDestroyed();

    void onTransitionToBridgeBusy();

    void onTransitionToBridgeIdle();
}

package com.facebook.react.bridge;

/* JADX INFO: loaded from: classes.dex */
public interface UIManagerListener {
    void didDispatchMountItems(UIManager uIManager);

    void didMountItems(UIManager uIManager);

    void didScheduleMountItems(UIManager uIManager);

    void willDispatchViewUpdates(UIManager uIManager);

    void willMountItems(UIManager uIManager);
}

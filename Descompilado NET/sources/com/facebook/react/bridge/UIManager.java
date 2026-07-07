package com.facebook.react.bridge;

import android.view.View;
import com.facebook.react.uimanager.events.EventDispatcher;

/* JADX INFO: loaded from: classes.dex */
public interface UIManager extends PerformanceCounter {
    <T extends View> int addRootView(T t3, WritableMap writableMap);

    void addUIManagerEventListener(UIManagerListener uIManagerListener);

    void dispatchCommand(int i3, int i4, ReadableArray readableArray);

    void dispatchCommand(int i3, String str, ReadableArray readableArray);

    EventDispatcher getEventDispatcher();

    void initialize();

    void invalidate();

    void markActiveTouchForTag(int i3, int i4);

    void receiveEvent(int i3, int i4, String str, WritableMap writableMap);

    void receiveEvent(int i3, String str, WritableMap writableMap);

    void removeUIManagerEventListener(UIManagerListener uIManagerListener);

    String resolveCustomDirectEventName(String str);

    View resolveView(int i3);

    void sendAccessibilityEvent(int i3, int i4);

    <T extends View> int startSurface(T t3, String str, WritableMap writableMap, int i3, int i4);

    void stopSurface(int i3);

    void sweepActiveTouchForTag(int i3, int i4);

    void synchronouslyUpdateViewOnUIThread(int i3, ReadableMap readableMap);

    void updateRootLayoutSpecs(int i3, int i4, int i5, int i6, int i7);
}

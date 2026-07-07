package com.facebook.react.fabric;

import com.facebook.jni.HybridClassBase;
import com.facebook.react.bridge.NativeMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.mapbuffer.ReadableMapBuffer;
import com.facebook.react.uimanager.A0;

/* JADX INFO: loaded from: classes.dex */
public class StateWrapperImpl extends HybridClassBase implements A0 {
    static {
        c.a();
    }

    private StateWrapperImpl() {
        initHybrid();
    }

    private native ReadableNativeMap getStateDataImpl();

    private native ReadableMapBuffer getStateMapBufferDataImpl();

    private native void initHybrid();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.facebook.react.uimanager.A0
    public void b(WritableMap writableMap) {
        if (isValid()) {
            updateStateImpl((NativeMap) writableMap);
        } else {
            Y.a.m("StateWrapperImpl", "Race between StateWrapperImpl destruction and updateState");
        }
    }

    @Override // com.facebook.react.uimanager.A0
    public ReadableMapBuffer e() {
        if (isValid()) {
            return getStateMapBufferDataImpl();
        }
        Y.a.m("StateWrapperImpl", "Race between StateWrapperImpl destruction and getState");
        return null;
    }

    @Override // com.facebook.react.uimanager.A0
    public void f() {
        if (isValid()) {
            resetNative();
        }
    }

    public String toString() {
        if (!isValid()) {
            return "<destroyed>";
        }
        ReadableMapBuffer stateMapBufferDataImpl = getStateMapBufferDataImpl();
        if (stateMapBufferDataImpl != null) {
            return stateMapBufferDataImpl.toString();
        }
        ReadableNativeMap stateDataImpl = getStateDataImpl();
        return stateDataImpl == null ? "<unexpected null>" : stateDataImpl.toString();
    }

    public native void updateStateImpl(NativeMap nativeMap);
}

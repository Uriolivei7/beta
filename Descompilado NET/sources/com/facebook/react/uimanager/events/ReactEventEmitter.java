package com.facebook.react.uimanager.events;

import D2.h;
import P1.q;
import P1.t;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactNoCrashSoftException;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class ReactEventEmitter implements RCTModernEventEmitter {
    public static final a Companion = new a(null);
    private static final String TAG = "ReactEventEmitter";
    private RCTEventEmitter defaultEventEmitter;
    private RCTModernEventEmitter fabricEventEmitter;
    private final ReactApplicationContext reactContext;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public ReactEventEmitter(ReactApplicationContext reactApplicationContext) {
        h.f(reactApplicationContext, "reactContext");
        this.reactContext = reactApplicationContext;
    }

    private final RCTEventEmitter ensureDefaultEventEmitter() {
        if (this.defaultEventEmitter == null) {
            if (this.reactContext.hasActiveReactInstance()) {
                this.defaultEventEmitter = (RCTEventEmitter) this.reactContext.getJSModule(RCTEventEmitter.class);
            } else {
                ReactSoftExceptionLogger.logSoftException(TAG, new ReactNoCrashSoftException("Cannot get RCTEventEmitter from Context, no active Catalyst instance!"));
            }
        }
        return this.defaultEventEmitter;
    }

    @Override // com.facebook.react.uimanager.events.RCTEventEmitter
    public void receiveEvent(int i3, String str, WritableMap writableMap) {
        h.f(str, "eventName");
        receiveEvent(-1, i3, str, writableMap);
    }

    @Override // com.facebook.react.uimanager.events.RCTEventEmitter
    public void receiveTouches(String str, WritableArray writableArray, WritableArray writableArray2) {
        RCTEventEmitter rCTEventEmitterEnsureDefaultEventEmitter;
        h.f(str, "eventName");
        h.f(writableArray, "touches");
        h.f(writableArray2, "changedIndices");
        if (writableArray.size() <= 0) {
            throw new IllegalStateException("Check failed.");
        }
        ReadableMap map = writableArray.getMap(0);
        if (M1.a.a(map != null ? map.getInt(t.f1710b) : 0) != 1 || (rCTEventEmitterEnsureDefaultEventEmitter = ensureDefaultEventEmitter()) == null) {
            return;
        }
        rCTEventEmitterEnsureDefaultEventEmitter.receiveTouches(str, writableArray, writableArray2);
    }

    public final void register(int i3, RCTModernEventEmitter rCTModernEventEmitter) {
        if (i3 != 2) {
            throw new IllegalStateException("Check failed.");
        }
        this.fabricEventEmitter = rCTModernEventEmitter;
    }

    public final void unregister(int i3) {
        if (i3 == 1) {
            this.defaultEventEmitter = null;
        } else {
            this.fabricEventEmitter = null;
        }
    }

    @Override // com.facebook.react.uimanager.events.RCTModernEventEmitter
    public void receiveEvent(int i3, int i4, String str, WritableMap writableMap) {
        h.f(str, "eventName");
        receiveEvent(i3, i4, str, false, 0, writableMap, 2);
    }

    @Override // com.facebook.react.uimanager.events.RCTModernEventEmitter
    public void receiveEvent(int i3, int i4, String str, boolean z3, int i5, WritableMap writableMap, int i6) {
        RCTModernEventEmitter rCTModernEventEmitter;
        h.f(str, "eventName");
        int iB = M1.a.b(i4, i3);
        if (iB != 1) {
            if (iB == 2 && (rCTModernEventEmitter = this.fabricEventEmitter) != null) {
                rCTModernEventEmitter.receiveEvent(i3, i4, str, z3, i5, writableMap, i6);
                return;
            }
            return;
        }
        RCTEventEmitter rCTEventEmitterEnsureDefaultEventEmitter = ensureDefaultEventEmitter();
        if (rCTEventEmitterEnsureDefaultEventEmitter != null) {
            rCTEventEmitterEnsureDefaultEventEmitter.receiveEvent(i4, str, writableMap);
        }
    }

    public final void register(int i3, RCTEventEmitter rCTEventEmitter) {
        if (i3 == 1) {
            this.defaultEventEmitter = rCTEventEmitter;
            return;
        }
        throw new IllegalStateException("Check failed.");
    }

    @Override // com.facebook.react.uimanager.events.RCTModernEventEmitter
    public void receiveTouches(q qVar) {
        RCTModernEventEmitter rCTModernEventEmitter;
        h.f(qVar, "event");
        int iB = M1.a.b(qVar.o(), qVar.l());
        if (iB != 1) {
            if (iB == 2 && (rCTModernEventEmitter = this.fabricEventEmitter) != null) {
                t.c(rCTModernEventEmitter, qVar);
                return;
            }
            return;
        }
        RCTEventEmitter rCTEventEmitterEnsureDefaultEventEmitter = ensureDefaultEventEmitter();
        if (rCTEventEmitterEnsureDefaultEventEmitter != null) {
            t.d(rCTEventEmitterEnsureDefaultEventEmitter, qVar);
        }
    }
}

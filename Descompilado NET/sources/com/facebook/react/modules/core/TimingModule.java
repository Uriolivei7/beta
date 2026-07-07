package com.facebook.react.modules.core;

import B1.c;
import D2.h;
import com.facebook.fbreact.specs.NativeTimingSpec;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableArray;
import k1.e;
import kotlin.jvm.internal.DefaultConstructorMarker;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "Timing")
public final class TimingModule extends NativeTimingSpec implements c {
    public static final a Companion = new a(null);
    public static final String NAME = "Timing";
    private final JavaTimerManager javaTimerManager;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TimingModule(ReactApplicationContext reactApplicationContext, e eVar) {
        super(reactApplicationContext);
        h.f(reactApplicationContext, "reactContext");
        h.f(eVar, "devSupportManager");
        this.javaTimerManager = new JavaTimerManager(reactApplicationContext, this, b.f6916f.a(), eVar);
    }

    @Override // B1.c
    public void callIdleCallbacks(double d4) {
        JSTimers jSTimers;
        ReactApplicationContext reactApplicationContextIfActiveOrWarn = getReactApplicationContextIfActiveOrWarn();
        if (reactApplicationContextIfActiveOrWarn == null || (jSTimers = (JSTimers) reactApplicationContextIfActiveOrWarn.getJSModule(JSTimers.class)) == null) {
            return;
        }
        jSTimers.callIdleCallbacks(d4);
    }

    @Override // B1.c
    public void callTimers(WritableArray writableArray) {
        JSTimers jSTimers;
        h.f(writableArray, "timerIDs");
        ReactApplicationContext reactApplicationContextIfActiveOrWarn = getReactApplicationContextIfActiveOrWarn();
        if (reactApplicationContextIfActiveOrWarn == null || (jSTimers = (JSTimers) reactApplicationContextIfActiveOrWarn.getJSModule(JSTimers.class)) == null) {
            return;
        }
        jSTimers.callTimers(writableArray);
    }

    @Override // com.facebook.fbreact.specs.NativeTimingSpec
    public void createTimer(double d4, double d5, double d6, boolean z3) {
        this.javaTimerManager.t((int) d4, (int) d5, d6, z3);
    }

    @Override // com.facebook.fbreact.specs.NativeTimingSpec
    public void deleteTimer(double d4) {
        this.javaTimerManager.deleteTimer((int) d4);
    }

    @Override // B1.c
    public void emitTimeDriftWarning(String str) {
        JSTimers jSTimers;
        h.f(str, "warningMessage");
        ReactApplicationContext reactApplicationContextIfActiveOrWarn = getReactApplicationContextIfActiveOrWarn();
        if (reactApplicationContextIfActiveOrWarn == null || (jSTimers = (JSTimers) reactApplicationContextIfActiveOrWarn.getJSModule(JSTimers.class)) == null) {
            return;
        }
        jSTimers.emitTimeDriftWarning(str);
    }

    public final boolean hasActiveTimersInRange(long j3) {
        return this.javaTimerManager.u(j3);
    }

    @Override // com.facebook.react.bridge.BaseJavaModule, com.facebook.react.bridge.NativeModule, com.facebook.react.turbomodule.core.interfaces.TurboModule
    public void invalidate() {
        this.javaTimerManager.x();
    }

    @Override // com.facebook.fbreact.specs.NativeTimingSpec
    public void setSendIdleEvents(boolean z3) {
        this.javaTimerManager.setSendIdleEvents(z3);
    }
}

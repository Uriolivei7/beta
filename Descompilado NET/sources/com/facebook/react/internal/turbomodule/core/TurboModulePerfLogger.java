package com.facebook.react.internal.turbomodule.core;

import com.facebook.react.reactperflogger.NativeModulePerfLogger;

/* JADX INFO: loaded from: classes.dex */
public final class TurboModulePerfLogger {
    public static final TurboModulePerfLogger INSTANCE = new TurboModulePerfLogger();
    private static NativeModulePerfLogger nativeModulePerfLogger;

    static {
        NativeModuleSoLoader.Companion.maybeLoadSoLibrary();
    }

    private TurboModulePerfLogger() {
    }

    private final native void jniEnableCppLogging(NativeModulePerfLogger nativeModulePerfLogger2);

    public static final void moduleCreateCacheHit(String str, int i3) {
        NativeModulePerfLogger nativeModulePerfLogger2 = nativeModulePerfLogger;
        if (nativeModulePerfLogger2 != null) {
            if (str == null) {
                throw new IllegalStateException("Required value was null.");
            }
            nativeModulePerfLogger2.a(str, i3);
        }
    }

    public static final void moduleCreateConstructEnd(String str, int i3) {
        NativeModulePerfLogger nativeModulePerfLogger2 = nativeModulePerfLogger;
        if (nativeModulePerfLogger2 != null) {
            if (str == null) {
                throw new IllegalStateException("Required value was null.");
            }
            nativeModulePerfLogger2.b(str, i3);
        }
    }

    public static final void moduleCreateConstructStart(String str, int i3) {
        NativeModulePerfLogger nativeModulePerfLogger2 = nativeModulePerfLogger;
        if (nativeModulePerfLogger2 != null) {
            if (str == null) {
                throw new IllegalStateException("Required value was null.");
            }
            nativeModulePerfLogger2.c(str, i3);
        }
    }

    public static final void moduleCreateEnd(String str, int i3) {
        NativeModulePerfLogger nativeModulePerfLogger2 = nativeModulePerfLogger;
        if (nativeModulePerfLogger2 != null) {
            if (str == null) {
                throw new IllegalStateException("Required value was null.");
            }
            nativeModulePerfLogger2.d(str, i3);
        }
    }

    public static final void moduleCreateFail(String str, int i3) {
        NativeModulePerfLogger nativeModulePerfLogger2 = nativeModulePerfLogger;
        if (nativeModulePerfLogger2 != null) {
            if (str == null) {
                throw new IllegalStateException("Required value was null.");
            }
            nativeModulePerfLogger2.e(str, i3);
        }
    }

    public static final void moduleCreateSetUpEnd(String str, int i3) {
        NativeModulePerfLogger nativeModulePerfLogger2 = nativeModulePerfLogger;
        if (nativeModulePerfLogger2 != null) {
            if (str == null) {
                throw new IllegalStateException("Required value was null.");
            }
            nativeModulePerfLogger2.f(str, i3);
        }
    }

    public static final void moduleCreateSetUpStart(String str, int i3) {
        NativeModulePerfLogger nativeModulePerfLogger2 = nativeModulePerfLogger;
        if (nativeModulePerfLogger2 != null) {
            if (str == null) {
                throw new IllegalStateException("Required value was null.");
            }
            nativeModulePerfLogger2.g(str, i3);
        }
    }

    public static final void moduleCreateStart(String str, int i3) {
        NativeModulePerfLogger nativeModulePerfLogger2 = nativeModulePerfLogger;
        if (nativeModulePerfLogger2 != null) {
            if (str == null) {
                throw new IllegalStateException("Required value was null.");
            }
            nativeModulePerfLogger2.h(str, i3);
        }
    }

    public final void enableLogging(NativeModulePerfLogger nativeModulePerfLogger2) {
        if (nativeModulePerfLogger2 != null) {
            nativeModulePerfLogger = nativeModulePerfLogger2;
            jniEnableCppLogging(nativeModulePerfLogger2);
        }
    }
}

package com.facebook.react.runtime;

import com.facebook.jni.HybridData;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.soloader.SoLoader;
import java.io.Closeable;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class ReactHostInspectorTarget implements Closeable {
    private static final a Companion = new a(null);
    private final HybridData mHybridData;
    private final ReactHostImpl reactHostImpl;

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    private static final class b implements Executor {
        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            D2.h.f(runnable, "command");
            if (UiThreadUtil.isOnUiThread()) {
                runnable.run();
            } else {
                UiThreadUtil.runOnUiThread(runnable);
            }
        }
    }

    static {
        SoLoader.t("rninstance");
    }

    public ReactHostInspectorTarget(ReactHostImpl reactHostImpl) {
        D2.h.f(reactHostImpl, "reactHostImpl");
        this.reactHostImpl = reactHostImpl;
        this.mHybridData = initHybrid(reactHostImpl, new b());
    }

    private static /* synthetic */ void getMHybridData$annotations() {
    }

    private final native HybridData initHybrid(ReactHostImpl reactHostImpl, Executor executor);

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.mHybridData.resetNative();
    }

    public final boolean isValid() {
        return this.mHybridData.isValid();
    }

    public final native void sendDebuggerResumeCommand();
}

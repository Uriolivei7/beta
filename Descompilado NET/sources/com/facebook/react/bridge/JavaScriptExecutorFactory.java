package com.facebook.react.bridge;

/* JADX INFO: loaded from: classes.dex */
public interface JavaScriptExecutorFactory {
    JavaScriptExecutor create();

    void startSamplingProfiler();

    void stopSamplingProfiler(String str);
}

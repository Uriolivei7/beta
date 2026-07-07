package com.facebook.react.bridge;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface PerformanceCounter {
    Map<String, Long> getPerformanceCounters();

    void profileNextBatch();
}

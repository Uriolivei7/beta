package com.facebook.hermes.instrumentation;

import com.facebook.soloader.SoLoader;

/* JADX INFO: loaded from: classes.dex */
public final class HermesSamplingProfiler {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final HermesSamplingProfiler f5705a = new HermesSamplingProfiler();

    static {
        SoLoader.t("jsijniprofiler");
    }

    private HermesSamplingProfiler() {
    }

    public static final native void disable();

    public static final native void dumpSampledTraceToFile(String str);

    public static final native void enable();
}

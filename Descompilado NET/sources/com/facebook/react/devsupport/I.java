package com.facebook.react.devsupport;

import com.facebook.soloader.SoLoader;

/* JADX INFO: loaded from: classes.dex */
public final class I {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final I f6637a = new I();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static volatile boolean f6638b;

    private I() {
    }

    public static final synchronized void a() {
        if (f6638b) {
            return;
        }
        SoLoader.t("react_devsupportjni");
        f6638b = true;
    }
}

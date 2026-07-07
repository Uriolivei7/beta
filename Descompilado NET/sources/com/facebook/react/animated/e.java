package com.facebook.react.animated;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JSApplicationCausedNativeException;
import com.facebook.react.bridge.ReadableMap;

/* JADX INFO: loaded from: classes.dex */
public abstract class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public boolean f6382a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public w f6383b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Callback f6384c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public int f6385d;

    public void a(ReadableMap readableMap) {
        D2.h.f(readableMap, "config");
        throw new JSApplicationCausedNativeException("Animation config for " + getClass().getSimpleName() + " cannot be reset");
    }

    public abstract void b(long j3);
}

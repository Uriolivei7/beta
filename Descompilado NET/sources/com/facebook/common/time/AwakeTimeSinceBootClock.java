package com.facebook.common.time;

import e0.c;

/* JADX INFO: loaded from: classes.dex */
public class AwakeTimeSinceBootClock implements c {
    private static final AwakeTimeSinceBootClock INSTANCE = new AwakeTimeSinceBootClock();

    private AwakeTimeSinceBootClock() {
    }

    public static AwakeTimeSinceBootClock get() {
        return INSTANCE;
    }

    @Override // e0.c, e0.InterfaceC0523b
    public /* bridge */ /* synthetic */ long now() {
        return super.now();
    }

    @Override // e0.c, e0.InterfaceC0523b
    public long nowNanos() {
        return System.nanoTime();
    }
}

package com.facebook.react.modules.debug;

import com.facebook.react.bridge.NotThreadSafeBridgeIdleDebugListener;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class d implements NotThreadSafeBridgeIdleDebugListener, N1.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ArrayList f6935a = new ArrayList(20);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ArrayList f6936b = new ArrayList(20);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final ArrayList f6937c = new ArrayList(20);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final ArrayList f6938d = new ArrayList(20);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private volatile boolean f6939e = true;

    private final boolean c(long j3, long j4) {
        long jE = e.e(this.f6935a, j3, j4);
        long jE2 = e.e(this.f6936b, j3, j4);
        return (jE == -1 && jE2 == -1) ? this.f6939e : jE > jE2;
    }

    @Override // N1.a
    public synchronized void a() {
        this.f6937c.add(Long.valueOf(System.nanoTime()));
    }

    @Override // N1.a
    public synchronized void b() {
        this.f6938d.add(Long.valueOf(System.nanoTime()));
    }

    public final synchronized boolean d(long j3, long j4) {
        boolean z3;
        try {
            boolean zF = e.f(this.f6938d, j3, j4);
            boolean zC = c(j3, j4);
            z3 = true;
            if (!zF && (!zC || e.f(this.f6937c, j3, j4))) {
                z3 = false;
            }
            e.d(this.f6935a, j4);
            e.d(this.f6936b, j4);
            e.d(this.f6937c, j4);
            e.d(this.f6938d, j4);
            this.f6939e = zC;
        } catch (Throwable th) {
            throw th;
        }
        return z3;
    }

    @Override // com.facebook.react.bridge.NotThreadSafeBridgeIdleDebugListener
    public synchronized void onBridgeDestroyed() {
    }

    @Override // com.facebook.react.bridge.NotThreadSafeBridgeIdleDebugListener
    public synchronized void onTransitionToBridgeBusy() {
        this.f6936b.add(Long.valueOf(System.nanoTime()));
    }

    @Override // com.facebook.react.bridge.NotThreadSafeBridgeIdleDebugListener
    public synchronized void onTransitionToBridgeIdle() {
        this.f6935a.add(Long.valueOf(System.nanoTime()));
    }
}

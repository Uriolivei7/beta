package com.facebook.imagepipeline.producers;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public final class r0 implements q0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Executor f6238a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f6239b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Deque f6240c;

    public r0(Executor executor) {
        D2.h.f(executor, "executor");
        this.f6238a = executor;
        this.f6240c = new ArrayDeque();
    }

    @Override // com.facebook.imagepipeline.producers.q0
    public synchronized void a(Runnable runnable) {
        try {
            D2.h.f(runnable, "runnable");
            if (this.f6239b) {
                this.f6240c.add(runnable);
            } else {
                this.f6238a.execute(runnable);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // com.facebook.imagepipeline.producers.q0
    public synchronized void b(Runnable runnable) {
        D2.h.f(runnable, "runnable");
        this.f6240c.remove(runnable);
    }
}

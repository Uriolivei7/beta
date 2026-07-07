package com.facebook.imagepipeline.producers;

import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public final class C implements q0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Executor f5974a;

    public C(Executor executor) {
        if (executor == null) {
            throw new IllegalStateException("Required value was null.");
        }
        this.f5974a = executor;
    }

    @Override // com.facebook.imagepipeline.producers.q0
    public void a(Runnable runnable) {
        D2.h.f(runnable, "runnable");
        this.f5974a.execute(runnable);
    }

    @Override // com.facebook.imagepipeline.producers.q0
    public void b(Runnable runnable) {
        D2.h.f(runnable, "runnable");
    }
}

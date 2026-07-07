package com.facebook.imagepipeline.producers;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.t, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0359t implements e0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final e0 f6252a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ScheduledExecutorService f6253b;

    public C0359t(e0 e0Var, ScheduledExecutorService scheduledExecutorService) {
        D2.h.f(e0Var, "inputProducer");
        this.f6252a = e0Var;
        this.f6253b = scheduledExecutorService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void d(C0359t c0359t, InterfaceC0354n interfaceC0354n, f0 f0Var) {
        D2.h.f(c0359t, "this$0");
        D2.h.f(interfaceC0354n, "$consumer");
        D2.h.f(f0Var, "$context");
        c0359t.f6252a.b(interfaceC0354n, f0Var);
    }

    @Override // com.facebook.imagepipeline.producers.e0
    public void b(final InterfaceC0354n interfaceC0354n, final f0 f0Var) {
        D2.h.f(interfaceC0354n, "consumer");
        D2.h.f(f0Var, "context");
        U0.b bVarX = f0Var.X();
        ScheduledExecutorService scheduledExecutorService = this.f6253b;
        if (scheduledExecutorService != null) {
            scheduledExecutorService.schedule(new Runnable() { // from class: com.facebook.imagepipeline.producers.s
                @Override // java.lang.Runnable
                public final void run() {
                    C0359t.d(this.f6241b, interfaceC0354n, f0Var);
                }
            }, bVarX.e(), TimeUnit.MILLISECONDS);
        } else {
            this.f6252a.b(interfaceC0354n, f0Var);
        }
    }
}

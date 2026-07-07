package com.reactnativecommunity.asyncstorage;

import java.util.ArrayDeque;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class l implements Executor {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ArrayDeque f8441b = new ArrayDeque();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Runnable f8442c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Executor f8443d;

    class a implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ Runnable f8444b;

        a(Runnable runnable) {
            this.f8444b = runnable;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.f8444b.run();
            } finally {
                l.this.b();
            }
        }
    }

    public l(Executor executor) {
        this.f8443d = executor;
    }

    synchronized void b() {
        Runnable runnable = (Runnable) this.f8441b.poll();
        this.f8442c = runnable;
        if (runnable != null) {
            this.f8443d.execute(runnable);
        }
    }

    @Override // java.util.concurrent.Executor
    public synchronized void execute(Runnable runnable) {
        this.f8441b.offer(new a(runnable));
        if (this.f8442c == null) {
            b();
        }
    }
}

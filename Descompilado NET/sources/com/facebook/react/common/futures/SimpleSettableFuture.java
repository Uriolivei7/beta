package com.facebook.react.common.futures;

import D2.h;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
public final class SimpleSettableFuture implements Future {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final CountDownLatch f6521b = new CountDownLatch(1);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Object f6522c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Exception f6523d;

    private final void a() {
        if (this.f6521b.getCount() == 0) {
            throw new RuntimeException("Result has already been set!");
        }
    }

    public final Object b() {
        try {
            return get();
        } catch (InterruptedException e4) {
            throw new RuntimeException(e4);
        } catch (ExecutionException e5) {
            throw new RuntimeException(e5);
        }
    }

    public final void c(Object obj) {
        a();
        this.f6522c = obj;
        this.f6521b.countDown();
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z3) {
        throw new UnsupportedOperationException();
    }

    public final void d(Exception exc) {
        h.f(exc, "exception");
        a();
        this.f6523d = exc;
        this.f6521b.countDown();
    }

    @Override // java.util.concurrent.Future
    public Object get() throws ExecutionException, InterruptedException {
        this.f6521b.await();
        if (this.f6523d == null) {
            return this.f6522c;
        }
        throw new ExecutionException(this.f6523d);
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.f6521b.getCount() == 0;
    }

    @Override // java.util.concurrent.Future
    public Object get(long j3, TimeUnit timeUnit) throws ExecutionException, TimeoutException {
        h.f(timeUnit, "unit");
        if (this.f6521b.await(j3, timeUnit)) {
            if (this.f6523d == null) {
                return this.f6522c;
            }
            throw new ExecutionException(this.f6523d);
        }
        throw new TimeoutException("Timed out waiting for result");
    }
}

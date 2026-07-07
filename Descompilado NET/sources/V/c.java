package V;

import android.os.Handler;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class c implements RunnableFuture, ScheduledFuture {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Handler f2670b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final FutureTask f2671c;

    public c(Handler handler, Callable<Object> callable) {
        this.f2670b = handler;
        this.f2671c = new FutureTask(callable);
    }

    @Override // java.lang.Comparable
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compareTo(Delayed delayed) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z3) {
        return this.f2671c.cancel(z3);
    }

    @Override // java.util.concurrent.Future
    public Object get() {
        return this.f2671c.get();
    }

    @Override // java.util.concurrent.Delayed
    public long getDelay(TimeUnit timeUnit) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.f2671c.isCancelled();
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.f2671c.isDone();
    }

    @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
    public void run() {
        this.f2671c.run();
    }

    @Override // java.util.concurrent.Future
    public Object get(long j3, TimeUnit timeUnit) {
        return this.f2671c.get(j3, timeUnit);
    }

    public c(Handler handler, Runnable runnable, Object obj) {
        this.f2670b = handler;
        this.f2671c = new FutureTask(runnable, obj);
    }
}

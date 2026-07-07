package com.facebook.imagepipeline.producers;

import android.os.SystemClock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class H {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Executor f5998a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final d f5999b;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final int f6002e;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Runnable f6000c = new a();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Runnable f6001d = new b();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    O0.j f6003f = null;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    int f6004g = 0;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    f f6005h = f.IDLE;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    long f6006i = 0;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    long f6007j = 0;

    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            H.this.d();
        }
    }

    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            H.this.j();
        }
    }

    static /* synthetic */ class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f6010a;

        static {
            int[] iArr = new int[f.values().length];
            f6010a = iArr;
            try {
                iArr[f.IDLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f6010a[f.QUEUED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f6010a[f.RUNNING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f6010a[f.RUNNING_AND_PENDING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public interface d {
        void a(O0.j jVar, int i3);
    }

    static class e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static ScheduledExecutorService f6011a;

        static ScheduledExecutorService a() {
            if (f6011a == null) {
                f6011a = Executors.newSingleThreadScheduledExecutor();
            }
            return f6011a;
        }
    }

    enum f {
        IDLE,
        QUEUED,
        RUNNING,
        RUNNING_AND_PENDING
    }

    public H(Executor executor, d dVar, int i3) {
        this.f5998a = executor;
        this.f5999b = dVar;
        this.f6002e = i3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        O0.j jVar;
        int i3;
        long jUptimeMillis = SystemClock.uptimeMillis();
        synchronized (this) {
            jVar = this.f6003f;
            i3 = this.f6004g;
            this.f6003f = null;
            this.f6004g = 0;
            this.f6005h = f.RUNNING;
            this.f6007j = jUptimeMillis;
        }
        try {
            if (i(jVar, i3)) {
                this.f5999b.a(jVar, i3);
            }
        } finally {
            O0.j.o(jVar);
            g();
        }
    }

    private void e(long j3) {
        Runnable runnableA = P0.a.a(this.f6001d, "JobScheduler_enqueueJob");
        if (j3 > 0) {
            e.a().schedule(runnableA, j3, TimeUnit.MILLISECONDS);
        } else {
            runnableA.run();
        }
    }

    private void g() {
        long jMax;
        boolean z3;
        long jUptimeMillis = SystemClock.uptimeMillis();
        synchronized (this) {
            try {
                if (this.f6005h == f.RUNNING_AND_PENDING) {
                    jMax = Math.max(this.f6007j + ((long) this.f6002e), jUptimeMillis);
                    this.f6006i = jUptimeMillis;
                    this.f6005h = f.QUEUED;
                    z3 = true;
                } else {
                    this.f6005h = f.IDLE;
                    jMax = 0;
                    z3 = false;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (z3) {
            e(jMax - jUptimeMillis);
        }
    }

    private static boolean i(O0.j jVar, int i3) {
        return AbstractC0343c.e(i3) || AbstractC0343c.n(i3, 4) || O0.j.w0(jVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        this.f5998a.execute(P0.a.a(this.f6000c, "JobScheduler_submitJob"));
    }

    public void c() {
        O0.j jVar;
        synchronized (this) {
            jVar = this.f6003f;
            this.f6003f = null;
            this.f6004g = 0;
        }
        O0.j.o(jVar);
    }

    public synchronized long f() {
        return this.f6007j - this.f6006i;
    }

    public boolean h() {
        long jMax;
        long jUptimeMillis = SystemClock.uptimeMillis();
        synchronized (this) {
            try {
                boolean z3 = false;
                if (!i(this.f6003f, this.f6004g)) {
                    return false;
                }
                int i3 = c.f6010a[this.f6005h.ordinal()];
                if (i3 != 1) {
                    if (i3 == 3) {
                        this.f6005h = f.RUNNING_AND_PENDING;
                    }
                    jMax = 0;
                } else {
                    jMax = Math.max(this.f6007j + ((long) this.f6002e), jUptimeMillis);
                    this.f6006i = jUptimeMillis;
                    this.f6005h = f.QUEUED;
                    z3 = true;
                }
                if (z3) {
                    e(jMax - jUptimeMillis);
                }
                return true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean k(O0.j jVar, int i3) {
        O0.j jVar2;
        if (!i(jVar, i3)) {
            return false;
        }
        synchronized (this) {
            jVar2 = this.f6003f;
            this.f6003f = O0.j.i(jVar);
            this.f6004g = i3;
        }
        O0.j.o(jVar2);
        return true;
    }
}

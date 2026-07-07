package c0;

import X.p;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: renamed from: c0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0327a {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static C0327a f5674h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final long f5675i = TimeUnit.MINUTES.toMillis(2);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private volatile File f5677b;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private volatile File f5679d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private long f5680e;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private volatile StatFs f5676a = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile StatFs f5678c = null;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private volatile boolean f5682g = false;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Lock f5681f = new ReentrantLock();

    /* JADX INFO: renamed from: c0.a$a, reason: collision with other inner class name */
    public enum EnumC0088a {
        INTERNAL,
        EXTERNAL
    }

    protected C0327a() {
    }

    protected static StatFs a(String str) {
        return new StatFs(str);
    }

    private void b() {
        if (this.f5682g) {
            return;
        }
        this.f5681f.lock();
        try {
            if (!this.f5682g) {
                this.f5677b = Environment.getDataDirectory();
                this.f5679d = Environment.getExternalStorageDirectory();
                g();
                this.f5682g = true;
            }
        } finally {
            this.f5681f.unlock();
        }
    }

    public static synchronized C0327a d() {
        try {
            if (f5674h == null) {
                f5674h = new C0327a();
            }
        } catch (Throwable th) {
            throw th;
        }
        return f5674h;
    }

    private void e() {
        if (this.f5681f.tryLock()) {
            try {
                if (SystemClock.uptimeMillis() - this.f5680e > f5675i) {
                    g();
                }
            } finally {
                this.f5681f.unlock();
            }
        }
    }

    private void g() {
        this.f5676a = h(this.f5676a, this.f5677b);
        this.f5678c = h(this.f5678c, this.f5679d);
        this.f5680e = SystemClock.uptimeMillis();
    }

    private StatFs h(StatFs statFs, File file) {
        StatFs statFs2 = null;
        if (file == null || !file.exists()) {
            return null;
        }
        try {
            if (statFs == null) {
                statFs = a(file.getAbsolutePath());
            } else {
                statFs.restat(file.getAbsolutePath());
            }
            statFs2 = statFs;
            return statFs2;
        } catch (IllegalArgumentException unused) {
            return statFs2;
        } catch (Throwable th) {
            throw p.a(th);
        }
    }

    public long c(EnumC0088a enumC0088a) {
        b();
        e();
        StatFs statFs = enumC0088a == EnumC0088a.INTERNAL ? this.f5676a : this.f5678c;
        if (statFs != null) {
            return statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
        }
        return 0L;
    }

    public boolean f(EnumC0088a enumC0088a, long j3) {
        b();
        long jC = c(enumC0088a);
        return jC <= 0 || jC < j3;
    }
}

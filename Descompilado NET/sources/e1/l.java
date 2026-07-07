package e1;

import android.os.SystemClock;

/* JADX INFO: loaded from: classes.dex */
public final class l {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final l f9371a = new l();

    private l() {
    }

    public static final long a() {
        return System.currentTimeMillis();
    }

    public static final long b() {
        return System.nanoTime();
    }

    public static final long c() {
        return SystemClock.uptimeMillis();
    }
}

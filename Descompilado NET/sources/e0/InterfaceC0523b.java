package e0;

import java.util.concurrent.TimeUnit;

/* JADX INFO: renamed from: e0.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public interface InterfaceC0523b {
    default long now() {
        return TimeUnit.NANOSECONDS.toMillis(nowNanos());
    }

    long nowNanos();
}

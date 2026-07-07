package H0;

import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class q implements X.n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final long f312a = TimeUnit.MINUTES.toMillis(5);

    private int b() {
        int iMin = (int) Math.min(Runtime.getRuntime().maxMemory(), 2147483647L);
        if (iMin < 16777216) {
            return 1048576;
        }
        return iMin < 33554432 ? 2097152 : 4194304;
    }

    @Override // X.n
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public y get() {
        int iB = b();
        return new y(iB, Integer.MAX_VALUE, iB, Integer.MAX_VALUE, iB / 8, f312a);
    }
}

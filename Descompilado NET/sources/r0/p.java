package R0;

import android.util.SparseIntArray;

/* JADX INFO: loaded from: classes.dex */
public final class p {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final p f1980a = new p();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final int f1981b = Runtime.getRuntime().availableProcessors();

    private p() {
    }

    public static final SparseIntArray a(int i3, int i4, int i5) {
        SparseIntArray sparseIntArray = new SparseIntArray();
        while (i3 <= i4) {
            sparseIntArray.put(i3, i5);
            i3 *= 2;
        }
        return sparseIntArray;
    }

    public static final E b() {
        int i3 = f1981b;
        return new E(4194304, i3 * 4194304, a(131072, 4194304, i3), 131072, 4194304, i3);
    }
}

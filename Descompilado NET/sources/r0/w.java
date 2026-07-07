package R0;

/* JADX INFO: loaded from: classes.dex */
public final class w {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final w f1991a = new w();

    private w() {
    }

    public static final int a(int i3, int i4, int i5) {
        return Math.min(Math.max(0, i5 - i3), i4);
    }

    public static final void b(int i3, int i4, int i5, int i6, int i7) {
        X.k.d(i6 >= 0, "count (%d) ! >= 0", Integer.valueOf(i6));
        X.k.d(i3 >= 0, "offset (%d) ! >= 0", Integer.valueOf(i3));
        X.k.d(i5 >= 0, "otherOffset (%d) ! >= 0", Integer.valueOf(i5));
        X.k.d(i3 + i6 <= i7, "offset (%d) + count (%d) ! <= %d", Integer.valueOf(i3), Integer.valueOf(i6), Integer.valueOf(i7));
        X.k.d(i5 + i6 <= i4, "otherOffset (%d) + count (%d) ! <= %d", Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i4));
    }
}

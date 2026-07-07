package b1;

/* JADX INFO: renamed from: b1.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0315a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0315a f5587a = new C0315a();

    private C0315a() {
    }

    public static final int a(int i3, Object obj) {
        return (i3 * 31) + (obj != null ? obj.hashCode() : 0);
    }
}

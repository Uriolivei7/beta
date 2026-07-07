package a0;

/* JADX INFO: loaded from: classes.dex */
public class e implements d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static e f2853a;

    public static synchronized e b() {
        try {
            if (f2853a == null) {
                f2853a = new e();
            }
        } catch (Throwable th) {
            throw th;
        }
        return f2853a;
    }

    @Override // a0.d
    public void a(InterfaceC0209c interfaceC0209c) {
    }
}

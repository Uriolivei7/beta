package e0;

/* JADX INFO: loaded from: classes.dex */
public class d implements InterfaceC0522a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final d f9345a = new d();

    private d() {
    }

    public static d a() {
        return f9345a;
    }

    @Override // e0.InterfaceC0522a
    public long now() {
        return System.currentTimeMillis();
    }
}

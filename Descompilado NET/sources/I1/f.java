package I1;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private d f471a;

    public f(d dVar) {
        this.f471a = dVar;
    }

    public final void a() {
        this.f471a = null;
    }

    protected final void finalize() {
        if (this.f471a != null) {
            d.p();
        }
    }
}

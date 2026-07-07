package O;

/* JADX INFO: loaded from: classes.dex */
class h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private f f1449a;

    public h(f fVar) {
        this.f1449a = fVar;
    }

    public void a() {
        this.f1449a = null;
    }

    protected void finalize() throws Throwable {
        try {
            if (this.f1449a != null) {
                f.k();
            }
        } finally {
            super.finalize();
        }
    }
}

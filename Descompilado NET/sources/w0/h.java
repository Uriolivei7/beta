package W0;

/* JADX INFO: loaded from: classes.dex */
public class h implements d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f2693a;

    public h(int i3) {
        this.f2693a = i3;
    }

    @Override // W0.d
    public c createImageTranscoder(D0.c cVar, boolean z3) {
        return new g(z3, this.f2693a);
    }
}

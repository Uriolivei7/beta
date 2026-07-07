package p0;

/* JADX INFO: loaded from: classes.dex */
public class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f10300a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f10301b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f10302c;

    public d() {
        a();
    }

    public void a() {
        this.f10300a = false;
        this.f10301b = 4;
        c();
    }

    public void b() {
        this.f10302c++;
    }

    public void c() {
        this.f10302c = 0;
    }

    public void d(boolean z3) {
        this.f10300a = z3;
    }

    public boolean e() {
        return this.f10300a && this.f10302c < this.f10301b;
    }
}

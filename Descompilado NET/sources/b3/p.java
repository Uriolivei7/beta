package b3;

import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class p extends G {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private G f5648f;

    public p(G g3) {
        D2.h.f(g3, "delegate");
        this.f5648f = g3;
    }

    @Override // b3.G
    public G a() {
        return this.f5648f.a();
    }

    @Override // b3.G
    public G b() {
        return this.f5648f.b();
    }

    @Override // b3.G
    public long c() {
        return this.f5648f.c();
    }

    @Override // b3.G
    public G d(long j3) {
        return this.f5648f.d(j3);
    }

    @Override // b3.G
    public boolean e() {
        return this.f5648f.e();
    }

    @Override // b3.G
    public void f() throws InterruptedIOException {
        this.f5648f.f();
    }

    @Override // b3.G
    public G g(long j3, TimeUnit timeUnit) {
        D2.h.f(timeUnit, "unit");
        return this.f5648f.g(j3, timeUnit);
    }

    @Override // b3.G
    public long h() {
        return this.f5648f.h();
    }

    public final G i() {
        return this.f5648f;
    }

    public final p j(G g3) {
        D2.h.f(g3, "delegate");
        this.f5648f = g3;
        return this;
    }
}

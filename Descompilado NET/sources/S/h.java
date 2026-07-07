package S;

import R.a;
import S.f;
import W.c;
import X.n;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/* JADX INFO: loaded from: classes.dex */
public class h implements f {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final Class f2281f = h.class;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f2282a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final n f2283b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f2284c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final R.a f2285d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    volatile a f2286e = new a(null, null);

    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final f f2287a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final File f2288b;

        a(File file, f fVar) {
            this.f2287a = fVar;
            this.f2288b = file;
        }
    }

    public h(int i3, n nVar, String str, R.a aVar) {
        this.f2282a = i3;
        this.f2285d = aVar;
        this.f2283b = nVar;
        this.f2284c = str;
    }

    private void l() throws c.a {
        File file = new File((File) this.f2283b.get(), this.f2284c);
        k(file);
        this.f2286e = new a(file, new S.a(file, this.f2282a, this.f2285d));
    }

    private boolean o() {
        File file;
        a aVar = this.f2286e;
        return aVar.f2287a == null || (file = aVar.f2288b) == null || !file.exists();
    }

    @Override // S.f
    public void a() {
        n().a();
    }

    @Override // S.f
    public Collection b() {
        return n().b();
    }

    @Override // S.f
    public boolean c() {
        try {
            return n().c();
        } catch (IOException unused) {
            return false;
        }
    }

    @Override // S.f
    public void d() {
        try {
            n().d();
        } catch (IOException e4) {
            Y.a.j(f2281f, "purgeUnexpectedResources", e4);
        }
    }

    @Override // S.f
    public long e(f.a aVar) {
        return n().e(aVar);
    }

    @Override // S.f
    public f.b f(String str, Object obj) {
        return n().f(str, obj);
    }

    @Override // S.f
    public boolean g(String str, Object obj) {
        return n().g(str, obj);
    }

    @Override // S.f
    public long h(String str) {
        return n().h(str);
    }

    @Override // S.f
    public boolean i(String str, Object obj) {
        return n().i(str, obj);
    }

    @Override // S.f
    public Q.a j(String str, Object obj) {
        return n().j(str, obj);
    }

    void k(File file) throws c.a {
        try {
            W.c.a(file);
            Y.a.a(f2281f, "Created cache directory %s", file.getAbsolutePath());
        } catch (c.a e4) {
            this.f2285d.a(a.EnumC0028a.WRITE_CREATE_DIR, f2281f, "createRootDirectoryIfNecessary", e4);
            throw e4;
        }
    }

    void m() {
        if (this.f2286e.f2287a == null || this.f2286e.f2288b == null) {
            return;
        }
        W.a.b(this.f2286e.f2288b);
    }

    synchronized f n() {
        try {
            if (o()) {
                m();
                l();
            }
        } catch (Throwable th) {
            throw th;
        }
        return (f) X.k.g(this.f2286e.f2287a);
    }
}

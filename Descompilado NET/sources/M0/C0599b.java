package m0;

import X.n;
import X.o;
import java.util.List;
import z0.InterfaceC0788g;

/* JADX INFO: renamed from: m0.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0599b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final X.f f9799a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0605h f9800b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final n f9801c;

    /* JADX INFO: renamed from: m0.b$a */
    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private List f9802a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private n f9803b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private C0605h f9804c;

        static /* bridge */ /* synthetic */ InterfaceC0788g c(a aVar) {
            aVar.getClass();
            return null;
        }

        public C0599b e() {
            return new C0599b(this);
        }
    }

    public static a e() {
        return new a();
    }

    public X.f a() {
        return this.f9799a;
    }

    public n b() {
        return this.f9801c;
    }

    public InterfaceC0788g c() {
        return null;
    }

    public C0605h d() {
        return this.f9800b;
    }

    private C0599b(a aVar) {
        this.f9799a = aVar.f9802a != null ? X.f.b(aVar.f9802a) : null;
        this.f9801c = aVar.f9803b != null ? aVar.f9803b : o.a(Boolean.FALSE);
        this.f9800b = aVar.f9804c;
        a.c(aVar);
    }
}

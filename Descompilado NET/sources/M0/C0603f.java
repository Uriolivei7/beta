package m0;

import H0.k;
import J0.C0185t;
import U0.b;
import android.content.Context;
import android.net.Uri;
import h0.InterfaceC0548c;
import java.util.Set;
import q0.AbstractC0646b;
import q0.InterfaceC0648d;
import w0.InterfaceC0759a;
import z0.InterfaceC0783b;
import z0.InterfaceC0788g;

/* JADX INFO: renamed from: m0.f, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0603f extends AbstractC0646b {

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final C0185t f9822t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private final C0605h f9823u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private X.f f9824v;

    /* JADX INFO: renamed from: m0.f$a */
    static /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f9825a;

        static {
            int[] iArr = new int[AbstractC0646b.c.values().length];
            f9825a = iArr;
            try {
                iArr[AbstractC0646b.c.FULL_FETCH.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f9825a[AbstractC0646b.c.DISK_CACHE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f9825a[AbstractC0646b.c.BITMAP_MEMORY_CACHE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public C0603f(Context context, C0605h c0605h, C0185t c0185t, Set<InterfaceC0648d> set, Set<InterfaceC0783b> set2) {
        super(context, set, set2);
        this.f9822t = c0185t;
        this.f9823u = c0605h;
    }

    public static b.c I(AbstractC0646b.c cVar) {
        int i3 = a.f9825a[cVar.ordinal()];
        if (i3 == 1) {
            return b.c.FULL_FETCH;
        }
        if (i3 == 2) {
            return b.c.DISK_CACHE;
        }
        if (i3 == 3) {
            return b.c.BITMAP_MEMORY_CACHE;
        }
        throw new RuntimeException("Cache level" + cVar + "is not supported. ");
    }

    private R.d J() {
        U0.b bVar = (U0.b) o();
        k kVarP = this.f9822t.p();
        if (kVarP == null || bVar == null) {
            return null;
        }
        return bVar.l() != null ? kVarP.a(bVar, g()) : kVarP.b(bVar, g());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // q0.AbstractC0646b
    /* JADX INFO: renamed from: K, reason: merged with bridge method [inline-methods] */
    public InterfaceC0548c j(InterfaceC0759a interfaceC0759a, String str, U0.b bVar, Object obj, AbstractC0646b.c cVar) {
        return this.f9822t.l(bVar, obj, I(cVar), L(interfaceC0759a), str);
    }

    protected Q0.e L(InterfaceC0759a interfaceC0759a) {
        if (interfaceC0759a instanceof C0602e) {
            return ((C0602e) interfaceC0759a).p0();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // q0.AbstractC0646b
    /* JADX INFO: renamed from: M, reason: merged with bridge method [inline-methods] */
    public C0602e y() {
        if (V0.b.d()) {
            V0.b.a("PipelineDraweeControllerBuilder#obtainController");
        }
        try {
            InterfaceC0759a interfaceC0759aQ = q();
            String strF = AbstractC0646b.f();
            C0602e c0602eC = interfaceC0759aQ instanceof C0602e ? (C0602e) interfaceC0759aQ : this.f9823u.c();
            c0602eC.r0(z(c0602eC, strF), strF, J(), g(), this.f9824v);
            c0602eC.s0(null, this);
            if (V0.b.d()) {
                V0.b.b();
            }
            return c0602eC;
        } catch (Throwable th) {
            if (V0.b.d()) {
                V0.b.b();
            }
            throw th;
        }
    }

    public C0603f N(InterfaceC0788g interfaceC0788g) {
        return (C0603f) s();
    }

    @Override // w0.InterfaceC0762d
    /* JADX INFO: renamed from: O, reason: merged with bridge method [inline-methods] */
    public C0603f c(Uri uri) {
        return uri == null ? (C0603f) super.E(null) : (C0603f) super.E(U0.c.x(uri).P(I0.h.e()).a());
    }
}

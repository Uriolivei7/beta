package n0;

import Q0.c;
import android.graphics.Rect;
import androidx.activity.result.d;
import e0.InterfaceC0523b;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import m0.C0602e;
import o0.C0627a;
import o0.C0628b;
import w0.InterfaceC0760b;
import z0.C0791j;
import z0.EnumC0786e;
import z0.EnumC0792k;
import z0.InterfaceC0788g;
import z0.InterfaceC0790i;
import z0.n;

/* JADX INFO: renamed from: n0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0617a implements InterfaceC0790i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final C0602e f9841a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final InterfaceC0523b f9842b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C0791j f9843c = new C0791j(EnumC0792k.f11146d);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private C0627a f9844d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private C0628b f9845e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private c f9846f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private List f9847g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f9848h;

    public C0617a(InterfaceC0523b interfaceC0523b, C0602e c0602e) {
        this.f9842b = interfaceC0523b;
        this.f9841a = c0602e;
    }

    private void h() {
        if (this.f9845e == null) {
            this.f9845e = new C0628b(this.f9842b, this.f9843c, this);
        }
        if (this.f9844d == null) {
            this.f9844d = new C0627a(this.f9842b, this.f9843c);
        }
        if (this.f9846f == null) {
            this.f9846f = new c(this.f9844d);
        }
    }

    @Override // z0.InterfaceC0790i
    public void a(C0791j c0791j, EnumC0786e enumC0786e) {
        List list;
        c0791j.H(enumC0786e);
        if (!this.f9848h || (list = this.f9847g) == null || list.isEmpty()) {
            return;
        }
        if (enumC0786e == EnumC0786e.f11057h) {
            d();
        }
        c0791j.S();
        Iterator it = this.f9847g.iterator();
        if (it.hasNext()) {
            d.a(it.next());
            throw null;
        }
    }

    @Override // z0.InterfaceC0790i
    public void b(C0791j c0791j, n nVar) {
        List list;
        if (!this.f9848h || (list = this.f9847g) == null || list.isEmpty()) {
            return;
        }
        c0791j.S();
        Iterator it = this.f9847g.iterator();
        if (it.hasNext()) {
            d.a(it.next());
            throw null;
        }
    }

    public void c(InterfaceC0788g interfaceC0788g) {
        if (interfaceC0788g == null) {
            return;
        }
        if (this.f9847g == null) {
            this.f9847g = new CopyOnWriteArrayList();
        }
        this.f9847g.add(interfaceC0788g);
    }

    public void d() {
        InterfaceC0760b interfaceC0760bB = this.f9841a.b();
        if (interfaceC0760bB == null || interfaceC0760bB.d() == null) {
            return;
        }
        Rect bounds = interfaceC0760bB.d().getBounds();
        this.f9843c.N(bounds.width());
        this.f9843c.M(bounds.height());
    }

    public void e() {
        List list = this.f9847g;
        if (list != null) {
            list.clear();
        }
    }

    public void f() {
        e();
        g(false);
        this.f9843c.w();
    }

    public void g(boolean z3) {
        this.f9848h = z3;
        if (!z3) {
            C0628b c0628b = this.f9845e;
            if (c0628b != null) {
                this.f9841a.S(c0628b);
            }
            c cVar = this.f9846f;
            if (cVar != null) {
                this.f9841a.y0(cVar);
                return;
            }
            return;
        }
        h();
        C0628b c0628b2 = this.f9845e;
        if (c0628b2 != null) {
            this.f9841a.k(c0628b2);
        }
        c cVar2 = this.f9846f;
        if (cVar2 != null) {
            this.f9841a.i0(cVar2);
        }
    }
}

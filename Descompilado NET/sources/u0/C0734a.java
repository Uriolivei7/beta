package u0;

import X.k;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import java.util.Iterator;
import t0.C0726f;
import t0.C0727g;
import t0.InterfaceC0723c;
import t0.h;
import t0.p;
import t0.r;
import w0.InterfaceC0761c;

/* JADX INFO: renamed from: u0.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0734a implements InterfaceC0761c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Drawable f10804a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Resources f10805b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private e f10806c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final C0737d f10807d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final C0726f f10808e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final C0727g f10809f;

    C0734a(C0735b c0735b) {
        ColorDrawable colorDrawable = new ColorDrawable(0);
        this.f10804a = colorDrawable;
        if (V0.b.d()) {
            V0.b.a("GenericDraweeHierarchy()");
        }
        this.f10805b = c0735b.p();
        this.f10806c = c0735b.s();
        C0727g c0727g = new C0727g(colorDrawable);
        this.f10809f = c0727g;
        int i3 = 1;
        int size = c0735b.j() != null ? c0735b.j().size() : 1;
        int i4 = (size == 0 ? 1 : size) + (c0735b.m() != null ? 1 : 0);
        Drawable[] drawableArr = new Drawable[i4 + 6];
        drawableArr[0] = j(c0735b.e(), null);
        drawableArr[1] = j(c0735b.k(), c0735b.l());
        drawableArr[2] = i(c0727g, c0735b.d(), c0735b.c(), c0735b.b());
        drawableArr[3] = j(c0735b.n(), c0735b.o());
        drawableArr[4] = j(c0735b.q(), c0735b.r());
        drawableArr[5] = j(c0735b.h(), c0735b.i());
        if (i4 > 0) {
            if (c0735b.j() != null) {
                Iterator it = c0735b.j().iterator();
                i3 = 0;
                while (it.hasNext()) {
                    drawableArr[i3 + 6] = j((Drawable) it.next(), null);
                    i3++;
                }
            }
            if (c0735b.m() != null) {
                drawableArr[i3 + 6] = j(c0735b.m(), null);
            }
        }
        C0726f c0726f = new C0726f(drawableArr, false, 2);
        this.f10808e = c0726f;
        c0726f.u(c0735b.g());
        C0737d c0737d = new C0737d(f.e(c0726f, this.f10806c));
        this.f10807d = c0737d;
        c0737d.mutate();
        u();
        if (V0.b.d()) {
            V0.b.b();
        }
    }

    private Drawable i(Drawable drawable, r rVar, PointF pointF, ColorFilter colorFilter) {
        drawable.setColorFilter(colorFilter);
        return f.g(drawable, rVar, pointF);
    }

    private Drawable j(Drawable drawable, r rVar) {
        return f.f(f.d(drawable, this.f10806c, this.f10805b), rVar);
    }

    private void k(int i3) {
        if (i3 >= 0) {
            this.f10808e.k(i3);
        }
    }

    private void l() {
        m(1);
        m(2);
        m(3);
        m(4);
        m(5);
    }

    private void m(int i3) {
        if (i3 >= 0) {
            this.f10808e.l(i3);
        }
    }

    private InterfaceC0723c p(int i3) {
        InterfaceC0723c interfaceC0723cC = this.f10808e.c(i3);
        if (interfaceC0723cC.q() instanceof h) {
            interfaceC0723cC = (h) interfaceC0723cC.q();
        }
        return interfaceC0723cC.q() instanceof p ? (p) interfaceC0723cC.q() : interfaceC0723cC;
    }

    private p r(int i3) {
        InterfaceC0723c interfaceC0723cP = p(i3);
        return interfaceC0723cP instanceof p ? (p) interfaceC0723cP : f.k(interfaceC0723cP, r.f10750a);
    }

    private boolean s(int i3) {
        return p(i3) instanceof p;
    }

    private void t() {
        this.f10809f.d(this.f10804a);
    }

    private void u() {
        C0726f c0726f = this.f10808e;
        if (c0726f != null) {
            c0726f.f();
            this.f10808e.j();
            l();
            k(1);
            this.f10808e.m();
            this.f10808e.i();
        }
    }

    private void w(int i3, Drawable drawable) {
        if (drawable == null) {
            this.f10808e.e(i3, null);
        } else {
            p(i3).d(f.d(drawable, this.f10806c, this.f10805b));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void z(float f3) {
        Drawable drawableB = this.f10808e.b(3);
        if (drawableB == 0) {
            return;
        }
        if (f3 >= 0.999f) {
            if (drawableB instanceof Animatable) {
                ((Animatable) drawableB).stop();
            }
            m(3);
        } else {
            if (drawableB instanceof Animatable) {
                ((Animatable) drawableB).start();
            }
            k(3);
        }
        drawableB.setLevel(Math.round(f3 * 10000.0f));
    }

    public void A(Drawable drawable) {
        w(3, drawable);
    }

    public void B(e eVar) {
        this.f10806c = eVar;
        f.j(this.f10807d, eVar);
        for (int i3 = 0; i3 < this.f10808e.d(); i3++) {
            f.i(p(i3), this.f10806c, this.f10805b);
        }
    }

    @Override // w0.InterfaceC0761c
    public void a(float f3, boolean z3) {
        if (this.f10808e.b(3) == null) {
            return;
        }
        this.f10808e.f();
        z(f3);
        if (z3) {
            this.f10808e.m();
        }
        this.f10808e.i();
    }

    @Override // w0.InterfaceC0760b
    public Rect b() {
        return this.f10807d.getBounds();
    }

    @Override // w0.InterfaceC0761c
    public void c(Drawable drawable) {
        this.f10807d.x(drawable);
    }

    @Override // w0.InterfaceC0760b
    public Drawable d() {
        return this.f10807d;
    }

    @Override // w0.InterfaceC0761c
    public void e(Drawable drawable, float f3, boolean z3) {
        Drawable drawableD = f.d(drawable, this.f10806c, this.f10805b);
        drawableD.mutate();
        this.f10809f.d(drawableD);
        this.f10808e.f();
        l();
        k(2);
        z(f3);
        if (z3) {
            this.f10808e.m();
        }
        this.f10808e.i();
    }

    @Override // w0.InterfaceC0761c
    public void f(Throwable th) {
        this.f10808e.f();
        l();
        if (this.f10808e.b(4) != null) {
            k(4);
        } else {
            k(1);
        }
        this.f10808e.i();
    }

    @Override // w0.InterfaceC0761c
    public void g(Throwable th) {
        this.f10808e.f();
        l();
        if (this.f10808e.b(5) != null) {
            k(5);
        } else {
            k(1);
        }
        this.f10808e.i();
    }

    @Override // w0.InterfaceC0761c
    public void h() {
        t();
        u();
    }

    public PointF n() {
        if (s(2)) {
            return r(2).z();
        }
        return null;
    }

    public r o() {
        if (s(2)) {
            return r(2).A();
        }
        return null;
    }

    public e q() {
        return this.f10806c;
    }

    public void v(r rVar) {
        k.g(rVar);
        r(2).C(rVar);
    }

    public void x(int i3) {
        this.f10808e.u(i3);
    }

    public void y(Drawable drawable, r rVar) {
        w(1, drawable);
        r(1).C(rVar);
    }
}

package x0;

import X.i;
import X.k;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import p0.c;
import t0.F;
import t0.G;
import w0.InterfaceC0759a;
import w0.InterfaceC0760b;

/* JADX INFO: renamed from: x0.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0770b implements G {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private InterfaceC0760b f10943e;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f10940b = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f10941c = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f10942d = true;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private InterfaceC0759a f10944f = null;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final p0.c f10945g = p0.c.a();

    public C0770b(InterfaceC0760b interfaceC0760b) {
        if (interfaceC0760b != null) {
            p(interfaceC0760b);
        }
    }

    private void a() {
        if (this.f10940b) {
            return;
        }
        this.f10945g.b(c.a.ON_ATTACH_CONTROLLER);
        this.f10940b = true;
        InterfaceC0759a interfaceC0759a = this.f10944f;
        if (interfaceC0759a == null || interfaceC0759a.b() == null) {
            return;
        }
        this.f10944f.e();
    }

    private void b() {
        if (this.f10941c && this.f10942d) {
            a();
        } else {
            d();
        }
    }

    public static C0770b c(InterfaceC0760b interfaceC0760b, Context context) {
        C0770b c0770b = new C0770b(interfaceC0760b);
        c0770b.m(context);
        return c0770b;
    }

    private void d() {
        if (this.f10940b) {
            this.f10945g.b(c.a.ON_DETACH_CONTROLLER);
            this.f10940b = false;
            if (h()) {
                this.f10944f.a();
            }
        }
    }

    private void q(G g3) {
        Object objG = g();
        if (objG instanceof F) {
            ((F) objG).e(g3);
        }
    }

    public InterfaceC0759a e() {
        return this.f10944f;
    }

    public InterfaceC0760b f() {
        return (InterfaceC0760b) k.g(this.f10943e);
    }

    public Drawable g() {
        InterfaceC0760b interfaceC0760b = this.f10943e;
        if (interfaceC0760b == null) {
            return null;
        }
        return interfaceC0760b.d();
    }

    public boolean h() {
        InterfaceC0759a interfaceC0759a = this.f10944f;
        return interfaceC0759a != null && interfaceC0759a.b() == this.f10943e;
    }

    @Override // t0.G
    public void i(boolean z3) {
        if (this.f10942d == z3) {
            return;
        }
        this.f10945g.b(z3 ? c.a.ON_DRAWABLE_SHOW : c.a.ON_DRAWABLE_HIDE);
        this.f10942d = z3;
        b();
    }

    public void j() {
        this.f10945g.b(c.a.ON_HOLDER_ATTACH);
        this.f10941c = true;
        b();
    }

    public void k() {
        this.f10945g.b(c.a.ON_HOLDER_DETACH);
        this.f10941c = false;
        b();
    }

    public boolean l(MotionEvent motionEvent) {
        if (h()) {
            return this.f10944f.d(motionEvent);
        }
        return false;
    }

    public void n() {
        o(null);
    }

    public void o(InterfaceC0759a interfaceC0759a) {
        boolean z3 = this.f10940b;
        if (z3) {
            d();
        }
        if (h()) {
            this.f10945g.b(c.a.ON_CLEAR_OLD_CONTROLLER);
            this.f10944f.c(null);
        }
        this.f10944f = interfaceC0759a;
        if (interfaceC0759a != null) {
            this.f10945g.b(c.a.ON_SET_CONTROLLER);
            this.f10944f.c(this.f10943e);
        } else {
            this.f10945g.b(c.a.ON_CLEAR_CONTROLLER);
        }
        if (z3) {
            a();
        }
    }

    @Override // t0.G
    public void onDraw() {
        if (this.f10940b) {
            return;
        }
        Y.a.G(p0.c.class, "%x: Draw requested for a non-attached controller %x. %s", Integer.valueOf(System.identityHashCode(this)), Integer.valueOf(System.identityHashCode(this.f10944f)), toString());
        this.f10941c = true;
        this.f10942d = true;
        b();
    }

    public void p(InterfaceC0760b interfaceC0760b) {
        this.f10945g.b(c.a.ON_SET_HIERARCHY);
        boolean zH = h();
        q(null);
        InterfaceC0760b interfaceC0760b2 = (InterfaceC0760b) k.g(interfaceC0760b);
        this.f10943e = interfaceC0760b2;
        Drawable drawableD = interfaceC0760b2.d();
        i(drawableD == null || drawableD.isVisible());
        q(this);
        if (zH) {
            this.f10944f.c(interfaceC0760b);
        }
    }

    public String toString() {
        return i.b(this).c("controllerAttached", this.f10940b).c("holderAttached", this.f10941c).c("drawableVisible", this.f10942d).b("events", this.f10945g.toString()).toString();
    }

    public void m(Context context) {
    }
}

package U0;

import I0.f;
import I0.g;
import I0.h;
import J0.C0186u;
import J0.EnumC0180n;
import Q0.e;
import U0.b;
import X.k;
import android.net.Uri;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class c {

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private static final Set f2417t = new HashSet();

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private e f2431n;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private int f2435r;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Uri f2418a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private b.c f2419b = b.c.FULL_FETCH;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f2420c = 0;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private g f2421d = null;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private h f2422e = null;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private I0.d f2423f = I0.d.a();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private b.EnumC0034b f2424g = b.EnumC0034b.DEFAULT;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f2425h = C0186u.b().a();

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f2426i = false;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f2427j = false;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private f f2428k = f.f417e;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private d f2429l = null;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private Boolean f2430m = null;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private I0.b f2432o = null;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private Boolean f2433p = null;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private EnumC0180n f2434q = null;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private String f2436s = null;

    public static class a extends RuntimeException {
        public a(String str) {
            super("Invalid request builder: " + str);
        }
    }

    private c() {
    }

    private c B(int i3) {
        this.f2420c = i3;
        if (this.f2424g != b.EnumC0034b.DYNAMIC) {
            this.f2436s = null;
        }
        return this;
    }

    public static c b(b bVar) {
        return x(bVar.v()).F(bVar.h()).z(bVar.b()).A(bVar.c()).H(bVar.j()).G(bVar.i()).I(bVar.k()).B(bVar.d()).J(bVar.l()).K(bVar.p()).M(bVar.o()).N(bVar.r()).L(bVar.q()).P(bVar.t()).Q(bVar.z()).C(bVar.e()).D(bVar.f()).E(bVar.g()).O(bVar.s());
    }

    public static boolean s(Uri uri) {
        Set set = f2417t;
        if (set != null && uri != null) {
            Iterator it = set.iterator();
            while (it.hasNext()) {
                if (((String) it.next()).equals(uri.getScheme())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static c x(Uri uri) {
        return new c().R(uri);
    }

    public c A(b.EnumC0034b enumC0034b) {
        this.f2424g = enumC0034b;
        return this;
    }

    public c C(int i3) {
        this.f2435r = i3;
        return this;
    }

    public c D(String str) {
        this.f2436s = str;
        return this;
    }

    public c E(EnumC0180n enumC0180n) {
        this.f2434q = enumC0180n;
        return this;
    }

    public c F(I0.d dVar) {
        this.f2423f = dVar;
        return this;
    }

    public c G(boolean z3) {
        this.f2427j = z3;
        return this;
    }

    public c H(boolean z3) {
        this.f2426i = z3;
        return this;
    }

    public c I(b.c cVar) {
        this.f2419b = cVar;
        return this;
    }

    public c J(d dVar) {
        this.f2429l = dVar;
        return this;
    }

    public c K(boolean z3) {
        this.f2425h = z3;
        return this;
    }

    public c L(e eVar) {
        this.f2431n = eVar;
        return this;
    }

    public c M(f fVar) {
        this.f2428k = fVar;
        return this;
    }

    public c N(g gVar) {
        this.f2421d = gVar;
        return this;
    }

    public c O(Boolean bool) {
        this.f2433p = bool;
        return this;
    }

    public c P(h hVar) {
        this.f2422e = hVar;
        return this;
    }

    public c Q(Boolean bool) {
        this.f2430m = bool;
        return this;
    }

    public c R(Uri uri) {
        k.g(uri);
        this.f2418a = uri;
        return this;
    }

    public Boolean S() {
        return this.f2430m;
    }

    protected void T() {
        Uri uri = this.f2418a;
        if (uri == null) {
            throw new a("Source must be set!");
        }
        if (f0.f.n(uri)) {
            if (!this.f2418a.isAbsolute()) {
                throw new a("Resource URI path must be absolute.");
            }
            if (this.f2418a.getPath().isEmpty()) {
                throw new a("Resource URI must not be empty");
            }
            try {
                Integer.parseInt(this.f2418a.getPath().substring(1));
            } catch (NumberFormatException unused) {
                throw new a("Resource URI path must be a resource id.");
            }
        }
        if (f0.f.i(this.f2418a) && !this.f2418a.isAbsolute()) {
            throw new a("Asset URI path must be absolute.");
        }
    }

    public b a() {
        T();
        return new b(this);
    }

    public I0.b c() {
        return this.f2432o;
    }

    public b.EnumC0034b d() {
        return this.f2424g;
    }

    public int e() {
        return this.f2420c;
    }

    public int f() {
        return this.f2435r;
    }

    public String g() {
        return this.f2436s;
    }

    public EnumC0180n h() {
        return this.f2434q;
    }

    public I0.d i() {
        return this.f2423f;
    }

    public boolean j() {
        return this.f2427j;
    }

    public b.c k() {
        return this.f2419b;
    }

    public d l() {
        return this.f2429l;
    }

    public e m() {
        return this.f2431n;
    }

    public f n() {
        return this.f2428k;
    }

    public g o() {
        return this.f2421d;
    }

    public Boolean p() {
        return this.f2433p;
    }

    public h q() {
        return this.f2422e;
    }

    public Uri r() {
        return this.f2418a;
    }

    public boolean t() {
        return (this.f2420c & 48) == 0 && (f0.f.o(this.f2418a) || s(this.f2418a));
    }

    public boolean u() {
        return this.f2426i;
    }

    public boolean v() {
        return (this.f2420c & 15) == 0;
    }

    public boolean w() {
        return this.f2425h;
    }

    public c y(boolean z3) {
        return z3 ? P(h.d()) : P(h.g());
    }

    public c z(I0.b bVar) {
        this.f2432o = bVar;
        return this;
    }
}

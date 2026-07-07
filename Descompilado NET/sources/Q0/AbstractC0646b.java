package q0;

import X.i;
import X.k;
import X.n;
import android.content.Context;
import android.graphics.drawable.Animatable;
import h0.AbstractC0549d;
import h0.C0551f;
import h0.C0553h;
import h0.InterfaceC0548c;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import v0.C0755a;
import w0.InterfaceC0759a;
import w0.InterfaceC0762d;
import z0.InterfaceC0783b;

/* JADX INFO: renamed from: q0.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0646b implements InterfaceC0762d {

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private static final InterfaceC0648d f10362q = new a();

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private static final NullPointerException f10363r = new NullPointerException("No image request was specified!");

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private static final AtomicLong f10364s = new AtomicLong();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f10365a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Set f10366b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Set f10367c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Object f10368d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Object f10369e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Object f10370f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private Object[] f10371g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f10372h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private n f10373i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private InterfaceC0648d f10374j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f10375k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f10376l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private boolean f10377m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private boolean f10378n = false;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private String f10379o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private InterfaceC0759a f10380p;

    /* JADX INFO: renamed from: q0.b$a */
    class a extends C0647c {
        a() {
        }

        @Override // q0.C0647c, q0.InterfaceC0648d
        public void k(String str, Object obj, Animatable animatable) {
            if (animatable != null) {
                animatable.start();
            }
        }
    }

    /* JADX INFO: renamed from: q0.b$b, reason: collision with other inner class name */
    class C0142b implements n {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ InterfaceC0759a f10381a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ String f10382b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Object f10383c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ Object f10384d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ c f10385e;

        C0142b(InterfaceC0759a interfaceC0759a, String str, Object obj, Object obj2, c cVar) {
            this.f10381a = interfaceC0759a;
            this.f10382b = str;
            this.f10383c = obj;
            this.f10384d = obj2;
            this.f10385e = cVar;
        }

        @Override // X.n
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public InterfaceC0548c get() {
            return AbstractC0646b.this.j(this.f10381a, this.f10382b, this.f10383c, this.f10384d, this.f10385e);
        }

        public String toString() {
            return i.b(this).b("request", this.f10383c.toString()).toString();
        }
    }

    /* JADX INFO: renamed from: q0.b$c */
    public enum c {
        FULL_FETCH,
        DISK_CACHE,
        BITMAP_MEMORY_CACHE
    }

    protected AbstractC0646b(Context context, Set set, Set set2) {
        this.f10365a = context;
        this.f10366b = set;
        this.f10367c = set2;
        t();
    }

    protected static String f() {
        return String.valueOf(f10364s.getAndIncrement());
    }

    private void t() {
        this.f10368d = null;
        this.f10369e = null;
        this.f10370f = null;
        this.f10371g = null;
        this.f10372h = true;
        this.f10374j = null;
        this.f10375k = false;
        this.f10376l = false;
        this.f10378n = false;
        this.f10380p = null;
        this.f10379o = null;
    }

    public AbstractC0646b A() {
        t();
        return s();
    }

    public AbstractC0646b B(boolean z3) {
        this.f10376l = z3;
        return s();
    }

    public AbstractC0646b C(Object obj) {
        this.f10368d = obj;
        return s();
    }

    public AbstractC0646b D(InterfaceC0648d interfaceC0648d) {
        this.f10374j = interfaceC0648d;
        return s();
    }

    public AbstractC0646b E(Object obj) {
        this.f10369e = obj;
        return s();
    }

    public AbstractC0646b F(Object obj) {
        this.f10370f = obj;
        return s();
    }

    @Override // w0.InterfaceC0762d
    /* JADX INFO: renamed from: G, reason: merged with bridge method [inline-methods] */
    public AbstractC0646b b(InterfaceC0759a interfaceC0759a) {
        this.f10380p = interfaceC0759a;
        return s();
    }

    protected void H() {
        boolean z3 = true;
        k.j(this.f10371g == null || this.f10369e == null, "Cannot specify both ImageRequest and FirstAvailableImageRequests!");
        if (this.f10373i != null && (this.f10371g != null || this.f10369e != null || this.f10370f != null)) {
            z3 = false;
        }
        k.j(z3, "Cannot specify DataSourceSupplier with other ImageRequests! Use one or the other.");
    }

    @Override // w0.InterfaceC0762d
    /* JADX INFO: renamed from: d, reason: merged with bridge method [inline-methods] */
    public AbstractC0645a a() {
        Object obj;
        H();
        if (this.f10369e == null && this.f10371g == null && (obj = this.f10370f) != null) {
            this.f10369e = obj;
            this.f10370f = null;
        }
        return e();
    }

    protected AbstractC0645a e() {
        if (V0.b.d()) {
            V0.b.a("AbstractDraweeControllerBuilder#buildController");
        }
        AbstractC0645a abstractC0645aY = y();
        abstractC0645aY.d0(u());
        abstractC0645aY.e0(r());
        abstractC0645aY.Z(h());
        i();
        abstractC0645aY.b0(null);
        x(abstractC0645aY);
        v(abstractC0645aY);
        if (V0.b.d()) {
            V0.b.b();
        }
        return abstractC0645aY;
    }

    public Object g() {
        return this.f10368d;
    }

    public String h() {
        return this.f10379o;
    }

    public InterfaceC0649e i() {
        return null;
    }

    protected abstract InterfaceC0548c j(InterfaceC0759a interfaceC0759a, String str, Object obj, Object obj2, c cVar);

    protected n k(InterfaceC0759a interfaceC0759a, String str, Object obj) {
        return l(interfaceC0759a, str, obj, c.FULL_FETCH);
    }

    protected n l(InterfaceC0759a interfaceC0759a, String str, Object obj, c cVar) {
        return new C0142b(interfaceC0759a, str, obj, g(), cVar);
    }

    protected n m(InterfaceC0759a interfaceC0759a, String str, Object[] objArr, boolean z3) {
        ArrayList arrayList = new ArrayList(objArr.length * 2);
        if (z3) {
            for (Object obj : objArr) {
                arrayList.add(l(interfaceC0759a, str, obj, c.BITMAP_MEMORY_CACHE));
            }
        }
        for (Object obj2 : objArr) {
            arrayList.add(k(interfaceC0759a, str, obj2));
        }
        return C0551f.b(arrayList);
    }

    public Object[] n() {
        return this.f10371g;
    }

    public Object o() {
        return this.f10369e;
    }

    public Object p() {
        return this.f10370f;
    }

    public InterfaceC0759a q() {
        return this.f10380p;
    }

    public boolean r() {
        return this.f10377m;
    }

    public boolean u() {
        return this.f10378n;
    }

    protected void v(AbstractC0645a abstractC0645a) {
        Set set = this.f10366b;
        if (set != null) {
            Iterator it = set.iterator();
            while (it.hasNext()) {
                abstractC0645a.j((InterfaceC0648d) it.next());
            }
        }
        Set set2 = this.f10367c;
        if (set2 != null) {
            Iterator it2 = set2.iterator();
            while (it2.hasNext()) {
                abstractC0645a.k((InterfaceC0783b) it2.next());
            }
        }
        InterfaceC0648d interfaceC0648d = this.f10374j;
        if (interfaceC0648d != null) {
            abstractC0645a.j(interfaceC0648d);
        }
        if (this.f10376l) {
            abstractC0645a.j(f10362q);
        }
    }

    protected void w(AbstractC0645a abstractC0645a) {
        if (abstractC0645a.u() == null) {
            abstractC0645a.c0(C0755a.c(this.f10365a));
        }
    }

    protected void x(AbstractC0645a abstractC0645a) {
        if (this.f10375k) {
            abstractC0645a.A().d(this.f10375k);
            w(abstractC0645a);
        }
    }

    protected abstract AbstractC0645a y();

    protected n z(InterfaceC0759a interfaceC0759a, String str) {
        n nVarM;
        n nVar = this.f10373i;
        if (nVar != null) {
            return nVar;
        }
        Object obj = this.f10369e;
        if (obj != null) {
            nVarM = k(interfaceC0759a, str, obj);
        } else {
            Object[] objArr = this.f10371g;
            nVarM = objArr != null ? m(interfaceC0759a, str, objArr, this.f10372h) : null;
        }
        if (nVarM != null && this.f10370f != null) {
            ArrayList arrayList = new ArrayList(2);
            arrayList.add(nVarM);
            arrayList.add(k(interfaceC0759a, str, this.f10370f));
            nVarM = C0553h.c(arrayList, false);
        }
        return nVarM == null ? AbstractC0549d.a(f10363r) : nVarM;
    }

    protected final AbstractC0646b s() {
        return this;
    }
}

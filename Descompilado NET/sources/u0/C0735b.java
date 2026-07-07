package u0;

import X.k;
import android.R;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import t0.r;

/* JADX INFO: renamed from: u0.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0735b {

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    public static final r f10810t = r.f10757h;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    public static final r f10811u = r.f10758i;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Resources f10812a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f10813b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private float f10814c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Drawable f10815d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private r f10816e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Drawable f10817f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private r f10818g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private Drawable f10819h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private r f10820i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private Drawable f10821j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private r f10822k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private r f10823l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private Matrix f10824m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private PointF f10825n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private ColorFilter f10826o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private Drawable f10827p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private List f10828q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private Drawable f10829r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private e f10830s;

    public C0735b(Resources resources) {
        this.f10812a = resources;
        t();
    }

    private void K() {
        List list = this.f10828q;
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                k.g((Drawable) it.next());
            }
        }
    }

    private void t() {
        this.f10813b = 300;
        this.f10814c = 0.0f;
        this.f10815d = null;
        r rVar = f10810t;
        this.f10816e = rVar;
        this.f10817f = null;
        this.f10818g = rVar;
        this.f10819h = null;
        this.f10820i = rVar;
        this.f10821j = null;
        this.f10822k = rVar;
        this.f10823l = f10811u;
        this.f10824m = null;
        this.f10825n = null;
        this.f10826o = null;
        this.f10827p = null;
        this.f10828q = null;
        this.f10829r = null;
        this.f10830s = null;
    }

    public static C0735b u(Resources resources) {
        return new C0735b(resources);
    }

    public C0735b A(r rVar) {
        this.f10820i = rVar;
        return this;
    }

    public C0735b B(Drawable drawable) {
        if (drawable == null) {
            this.f10828q = null;
        } else {
            this.f10828q = Arrays.asList(drawable);
        }
        return this;
    }

    public C0735b C(Drawable drawable) {
        this.f10815d = drawable;
        return this;
    }

    public C0735b D(r rVar) {
        this.f10816e = rVar;
        return this;
    }

    public C0735b E(Drawable drawable) {
        if (drawable == null) {
            this.f10829r = null;
        } else {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{R.attr.state_pressed}, drawable);
            this.f10829r = stateListDrawable;
        }
        return this;
    }

    public C0735b F(Drawable drawable) {
        this.f10821j = drawable;
        return this;
    }

    public C0735b G(r rVar) {
        this.f10822k = rVar;
        return this;
    }

    public C0735b H(Drawable drawable) {
        this.f10817f = drawable;
        return this;
    }

    public C0735b I(r rVar) {
        this.f10818g = rVar;
        return this;
    }

    public C0735b J(e eVar) {
        this.f10830s = eVar;
        return this;
    }

    public C0734a a() {
        K();
        return new C0734a(this);
    }

    public ColorFilter b() {
        return this.f10826o;
    }

    public PointF c() {
        return this.f10825n;
    }

    public r d() {
        return this.f10823l;
    }

    public Drawable e() {
        return this.f10827p;
    }

    public float f() {
        return this.f10814c;
    }

    public int g() {
        return this.f10813b;
    }

    public Drawable h() {
        return this.f10819h;
    }

    public r i() {
        return this.f10820i;
    }

    public List j() {
        return this.f10828q;
    }

    public Drawable k() {
        return this.f10815d;
    }

    public r l() {
        return this.f10816e;
    }

    public Drawable m() {
        return this.f10829r;
    }

    public Drawable n() {
        return this.f10821j;
    }

    public r o() {
        return this.f10822k;
    }

    public Resources p() {
        return this.f10812a;
    }

    public Drawable q() {
        return this.f10817f;
    }

    public r r() {
        return this.f10818g;
    }

    public e s() {
        return this.f10830s;
    }

    public C0735b v(r rVar) {
        this.f10823l = rVar;
        this.f10824m = null;
        return this;
    }

    public C0735b w(Drawable drawable) {
        this.f10827p = drawable;
        return this;
    }

    public C0735b x(float f3) {
        this.f10814c = f3;
        return this;
    }

    public C0735b y(int i3) {
        this.f10813b = i3;
        return this;
    }

    public C0735b z(Drawable drawable) {
        this.f10819h = drawable;
        return this;
    }
}

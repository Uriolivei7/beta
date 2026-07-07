package I0;

import android.graphics.Bitmap;
import android.graphics.ColorSpace;

/* JADX INFO: loaded from: classes.dex */
public class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f402a = 100;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f403b = Integer.MAX_VALUE;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f404c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f405d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f406e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f407f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f408g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private Bitmap.Config f409h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private Bitmap.Config f410i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private M0.c f411j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private ColorSpace f412k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f413l;

    public e() {
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        this.f409h = config;
        this.f410i = config;
    }

    public d a() {
        return new d(this);
    }

    public Bitmap.Config b() {
        return this.f410i;
    }

    public Bitmap.Config c() {
        return this.f409h;
    }

    public X0.a d() {
        return null;
    }

    public ColorSpace e() {
        return this.f412k;
    }

    public M0.c f() {
        return this.f411j;
    }

    public boolean g() {
        return this.f407f;
    }

    public boolean h() {
        return this.f404c;
    }

    public boolean i() {
        return this.f413l;
    }

    public boolean j() {
        return this.f408g;
    }

    public int k() {
        return this.f403b;
    }

    public int l() {
        return this.f402a;
    }

    public boolean m() {
        return this.f406e;
    }

    public boolean n() {
        return this.f405d;
    }
}

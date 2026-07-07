package O0;

import android.graphics.Bitmap;
import b0.AbstractC0306a;
import b0.InterfaceC0313h;

/* JADX INFO: loaded from: classes.dex */
public class b extends a implements e {

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static boolean f1453j = false;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private AbstractC0306a f1454e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private volatile Bitmap f1455f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final o f1456g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final int f1457h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final int f1458i;

    protected b(Bitmap bitmap, InterfaceC0313h interfaceC0313h, o oVar, int i3, int i4) {
        this.f1455f = (Bitmap) X.k.g(bitmap);
        this.f1454e = AbstractC0306a.n0(this.f1455f, (InterfaceC0313h) X.k.g(interfaceC0313h));
        this.f1456g = oVar;
        this.f1457h = i3;
        this.f1458i = i4;
    }

    private synchronized AbstractC0306a t0() {
        AbstractC0306a abstractC0306a;
        abstractC0306a = this.f1454e;
        this.f1454e = null;
        this.f1455f = null;
        return abstractC0306a;
    }

    private static int u0(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        return bitmap.getHeight();
    }

    private static int v0(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        return bitmap.getWidth();
    }

    public static boolean w0() {
        return f1453j;
    }

    @Override // O0.c
    public Bitmap C() {
        return this.f1455f;
    }

    @Override // O0.e
    public int N() {
        return this.f1457h;
    }

    @Override // O0.d
    public synchronized boolean b() {
        return this.f1454e == null;
    }

    @Override // O0.d
    public int b0() {
        return Z0.e.j(this.f1455f);
    }

    @Override // O0.d, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        AbstractC0306a abstractC0306aT0 = t0();
        if (abstractC0306aT0 != null) {
            abstractC0306aT0.close();
        }
    }

    @Override // O0.d, O0.l
    public int d() {
        int i3;
        return (this.f1457h % 180 != 0 || (i3 = this.f1458i) == 5 || i3 == 7) ? v0(this.f1455f) : u0(this.f1455f);
    }

    @Override // O0.d, O0.l
    public int h() {
        int i3;
        return (this.f1457h % 180 != 0 || (i3 = this.f1458i) == 5 || i3 == 7) ? u0(this.f1455f) : v0(this.f1455f);
    }

    @Override // O0.a, O0.d
    public o l() {
        return this.f1456g;
    }

    @Override // O0.e
    public int s0() {
        return this.f1458i;
    }

    protected b(AbstractC0306a abstractC0306a, o oVar, int i3, int i4) {
        AbstractC0306a abstractC0306a2 = (AbstractC0306a) X.k.g(abstractC0306a.z());
        this.f1454e = abstractC0306a2;
        this.f1455f = (Bitmap) abstractC0306a2.P();
        this.f1456g = oVar;
        this.f1457h = i3;
        this.f1458i = i4;
    }
}

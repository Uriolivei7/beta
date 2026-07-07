package R0;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class t implements i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected final A f1984a = new j();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f1985b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f1986c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final F f1987d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f1988e;

    public t(int i3, int i4, F f3, a0.d dVar) {
        this.f1985b = i3;
        this.f1986c = i4;
        this.f1987d = f3;
        if (dVar != null) {
            dVar.a(this);
        }
    }

    private Bitmap f(int i3) {
        this.f1987d.a(i3);
        return Bitmap.createBitmap(1, i3, Bitmap.Config.ALPHA_8);
    }

    private synchronized void i(int i3) {
        Bitmap bitmap;
        while (this.f1988e > i3 && (bitmap = (Bitmap) this.f1984a.b()) != null) {
            int iA = this.f1984a.a(bitmap);
            this.f1988e -= iA;
            this.f1987d.c(iA);
        }
    }

    @Override // a0.f
    /* JADX INFO: renamed from: g, reason: merged with bridge method [inline-methods] */
    public synchronized Bitmap get(int i3) {
        try {
            int i4 = this.f1988e;
            int i5 = this.f1985b;
            if (i4 > i5) {
                i(i5);
            }
            Bitmap bitmap = (Bitmap) this.f1984a.get(i3);
            if (bitmap == null) {
                return f(i3);
            }
            int iA = this.f1984a.a(bitmap);
            this.f1988e -= iA;
            this.f1987d.b(iA);
            return bitmap;
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // a0.f, b0.InterfaceC0313h
    /* JADX INFO: renamed from: h, reason: merged with bridge method [inline-methods] */
    public void a(Bitmap bitmap) {
        int iA = this.f1984a.a(bitmap);
        if (iA <= this.f1986c) {
            this.f1987d.e(iA);
            this.f1984a.c(bitmap);
            synchronized (this) {
                this.f1988e += iA;
            }
        }
    }
}

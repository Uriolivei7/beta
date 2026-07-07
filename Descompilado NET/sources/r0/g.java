package R0;

import android.graphics.Bitmap;
import b0.InterfaceC0313h;

/* JADX INFO: loaded from: classes.dex */
public class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f1956a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private long f1957b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f1958c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f1959d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final InterfaceC0313h f1960e;

    class a implements InterfaceC0313h {
        a() {
        }

        @Override // b0.InterfaceC0313h
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(Bitmap bitmap) {
            try {
                g.this.a(bitmap);
            } finally {
                bitmap.recycle();
            }
        }
    }

    public g(int i3, int i4) {
        X.k.b(Boolean.valueOf(i3 > 0));
        X.k.b(Boolean.valueOf(i4 > 0));
        this.f1958c = i3;
        this.f1959d = i4;
        this.f1960e = new a();
    }

    public synchronized void a(Bitmap bitmap) {
        int iJ = Z0.e.j(bitmap);
        X.k.c(this.f1956a > 0, "No bitmaps registered.");
        long j3 = iJ;
        X.k.d(j3 <= this.f1957b, "Bitmap size bigger than the total registered size: %d, %d", Integer.valueOf(iJ), Long.valueOf(this.f1957b));
        this.f1957b -= j3;
        this.f1956a--;
    }

    public synchronized int b() {
        return this.f1956a;
    }

    public synchronized int c() {
        return this.f1958c;
    }

    public synchronized int d() {
        return this.f1959d;
    }

    public InterfaceC0313h e() {
        return this.f1960e;
    }

    public synchronized long f() {
        return this.f1957b;
    }

    public synchronized boolean g(Bitmap bitmap) {
        int iJ = Z0.e.j(bitmap);
        int i3 = this.f1956a;
        if (i3 < this.f1958c) {
            long j3 = this.f1957b;
            long j4 = iJ;
            if (j3 + j4 <= this.f1959d) {
                this.f1956a = i3 + 1;
                this.f1957b = j3 + j4;
                return true;
            }
        }
        return false;
    }
}

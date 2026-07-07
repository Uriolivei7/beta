package androidx.emoji2.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import z.C0780a;

/* JADX INFO: loaded from: classes.dex */
public class q {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final ThreadLocal f4841d = new ThreadLocal();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f4842a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final o f4843b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile int f4844c = 0;

    q(o oVar, int i3) {
        this.f4843b = oVar;
        this.f4842a = i3;
    }

    private C0780a g() {
        ThreadLocal threadLocal = f4841d;
        C0780a c0780a = (C0780a) threadLocal.get();
        if (c0780a == null) {
            c0780a = new C0780a();
            threadLocal.set(c0780a);
        }
        this.f4843b.d().j(c0780a, this.f4842a);
        return c0780a;
    }

    public void a(Canvas canvas, float f3, float f4, Paint paint) {
        Typeface typefaceG = this.f4843b.g();
        Typeface typeface = paint.getTypeface();
        paint.setTypeface(typefaceG);
        canvas.drawText(this.f4843b.c(), this.f4842a * 2, 2, f3, f4, paint);
        paint.setTypeface(typeface);
    }

    public int b(int i3) {
        return g().h(i3);
    }

    public int c() {
        return g().i();
    }

    public int d() {
        return this.f4844c & 3;
    }

    public int e() {
        return g().k();
    }

    public int f() {
        return g().l();
    }

    public short h() {
        return g().m();
    }

    public int i() {
        return g().n();
    }

    public boolean j() {
        return g().j();
    }

    public boolean k() {
        return (this.f4844c & 4) > 0;
    }

    public void l(boolean z3) {
        int iD = d();
        if (z3) {
            this.f4844c = iD | 4;
        } else {
            this.f4844c = iD;
        }
    }

    public void m(boolean z3) {
        int i3 = this.f4844c & 4;
        this.f4844c = z3 ? i3 | 2 : i3 | 1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(", id:");
        sb.append(Integer.toHexString(f()));
        sb.append(", codepoints:");
        int iC = c();
        for (int i3 = 0; i3 < iC; i3++) {
            sb.append(Integer.toHexString(b(i3)));
            sb.append(" ");
        }
        return sb.toString();
    }
}

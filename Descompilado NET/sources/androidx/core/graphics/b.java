package androidx.core.graphics;

import android.graphics.Insets;
import android.graphics.Rect;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final b f4471e = new b(0, 0, 0, 0);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f4472a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f4473b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final int f4474c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final int f4475d;

    static class a {
        static Insets a(int i3, int i4, int i5, int i6) {
            return Insets.of(i3, i4, i5, i6);
        }
    }

    private b(int i3, int i4, int i5, int i6) {
        this.f4472a = i3;
        this.f4473b = i4;
        this.f4474c = i5;
        this.f4475d = i6;
    }

    public static b a(b bVar, b bVar2) {
        return b(Math.max(bVar.f4472a, bVar2.f4472a), Math.max(bVar.f4473b, bVar2.f4473b), Math.max(bVar.f4474c, bVar2.f4474c), Math.max(bVar.f4475d, bVar2.f4475d));
    }

    public static b b(int i3, int i4, int i5, int i6) {
        return (i3 == 0 && i4 == 0 && i5 == 0 && i6 == 0) ? f4471e : new b(i3, i4, i5, i6);
    }

    public static b c(Rect rect) {
        return b(rect.left, rect.top, rect.right, rect.bottom);
    }

    public static b d(Insets insets) {
        return b(insets.left, insets.top, insets.right, insets.bottom);
    }

    public Insets e() {
        return a.a(this.f4472a, this.f4473b, this.f4474c, this.f4475d);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || b.class != obj.getClass()) {
            return false;
        }
        b bVar = (b) obj;
        return this.f4475d == bVar.f4475d && this.f4472a == bVar.f4472a && this.f4474c == bVar.f4474c && this.f4473b == bVar.f4473b;
    }

    public int hashCode() {
        return (((((this.f4472a * 31) + this.f4473b) * 31) + this.f4474c) * 31) + this.f4475d;
    }

    public String toString() {
        return "Insets{left=" + this.f4472a + ", top=" + this.f4473b + ", right=" + this.f4474c + ", bottom=" + this.f4475d + '}';
    }
}

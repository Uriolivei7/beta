package b3;

import java.util.Arrays;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0711h;

/* JADX INFO: loaded from: classes.dex */
public final class A {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final a f5589h = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final byte[] f5590a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f5591b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f5592c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public boolean f5593d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public boolean f5594e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public A f5595f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public A f5596g;

    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public A() {
        this.f5590a = new byte[8192];
        this.f5594e = true;
        this.f5593d = false;
    }

    public final void a() {
        A a4 = this.f5596g;
        int i3 = 0;
        if (!(a4 != this)) {
            throw new IllegalStateException("cannot compact");
        }
        D2.h.c(a4);
        if (a4.f5594e) {
            int i4 = this.f5592c - this.f5591b;
            A a5 = this.f5596g;
            D2.h.c(a5);
            int i5 = 8192 - a5.f5592c;
            A a6 = this.f5596g;
            D2.h.c(a6);
            if (!a6.f5593d) {
                A a7 = this.f5596g;
                D2.h.c(a7);
                i3 = a7.f5591b;
            }
            if (i4 > i5 + i3) {
                return;
            }
            A a8 = this.f5596g;
            D2.h.c(a8);
            g(a8, i4);
            b();
            B.b(this);
        }
    }

    public final A b() {
        A a4 = this.f5595f;
        if (a4 == this) {
            a4 = null;
        }
        A a5 = this.f5596g;
        D2.h.c(a5);
        a5.f5595f = this.f5595f;
        A a6 = this.f5595f;
        D2.h.c(a6);
        a6.f5596g = this.f5596g;
        this.f5595f = null;
        this.f5596g = null;
        return a4;
    }

    public final A c(A a4) {
        D2.h.f(a4, "segment");
        a4.f5596g = this;
        a4.f5595f = this.f5595f;
        A a5 = this.f5595f;
        D2.h.c(a5);
        a5.f5596g = a4;
        this.f5595f = a4;
        return a4;
    }

    public final A d() {
        this.f5593d = true;
        return new A(this.f5590a, this.f5591b, this.f5592c, true, false);
    }

    public final A e(int i3) {
        A aC;
        if (!(i3 > 0 && i3 <= this.f5592c - this.f5591b)) {
            throw new IllegalArgumentException("byteCount out of range");
        }
        if (i3 >= 1024) {
            aC = d();
        } else {
            aC = B.c();
            byte[] bArr = this.f5590a;
            byte[] bArr2 = aC.f5590a;
            int i4 = this.f5591b;
            AbstractC0711h.g(bArr, bArr2, 0, i4, i4 + i3, 2, null);
        }
        aC.f5592c = aC.f5591b + i3;
        this.f5591b += i3;
        A a4 = this.f5596g;
        D2.h.c(a4);
        a4.c(aC);
        return aC;
    }

    public final A f() {
        byte[] bArr = this.f5590a;
        byte[] bArrCopyOf = Arrays.copyOf(bArr, bArr.length);
        D2.h.e(bArrCopyOf, "java.util.Arrays.copyOf(this, size)");
        return new A(bArrCopyOf, this.f5591b, this.f5592c, false, true);
    }

    public final void g(A a4, int i3) {
        D2.h.f(a4, "sink");
        if (!a4.f5594e) {
            throw new IllegalStateException("only owner can write");
        }
        int i4 = a4.f5592c;
        if (i4 + i3 > 8192) {
            if (a4.f5593d) {
                throw new IllegalArgumentException();
            }
            int i5 = a4.f5591b;
            if ((i4 + i3) - i5 > 8192) {
                throw new IllegalArgumentException();
            }
            byte[] bArr = a4.f5590a;
            AbstractC0711h.g(bArr, bArr, 0, i5, i4, 2, null);
            a4.f5592c -= a4.f5591b;
            a4.f5591b = 0;
        }
        byte[] bArr2 = this.f5590a;
        byte[] bArr3 = a4.f5590a;
        int i6 = a4.f5592c;
        int i7 = this.f5591b;
        AbstractC0711h.e(bArr2, bArr3, i6, i7, i7 + i3);
        a4.f5592c += i3;
        this.f5591b += i3;
    }

    public A(byte[] bArr, int i3, int i4, boolean z3, boolean z4) {
        D2.h.f(bArr, "data");
        this.f5590a = bArr;
        this.f5591b = i3;
        this.f5592c = i4;
        this.f5593d = z3;
        this.f5594e = z4;
    }
}

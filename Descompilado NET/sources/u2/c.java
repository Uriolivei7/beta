package U2;

import b3.l;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final b3.l f2454d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final b3.l f2455e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final b3.l f2456f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final b3.l f2457g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final b3.l f2458h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final b3.l f2459i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final a f2460j = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f2461a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final b3.l f2462b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final b3.l f2463c;

    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static {
        l.a aVar = b3.l.f5639f;
        f2454d = aVar.e(":");
        f2455e = aVar.e(":status");
        f2456f = aVar.e(":method");
        f2457g = aVar.e(":path");
        f2458h = aVar.e(":scheme");
        f2459i = aVar.e(":authority");
    }

    public c(b3.l lVar, b3.l lVar2) {
        D2.h.f(lVar, "name");
        D2.h.f(lVar2, "value");
        this.f2462b = lVar;
        this.f2463c = lVar2;
        this.f2461a = lVar.v() + 32 + lVar2.v();
    }

    public final b3.l a() {
        return this.f2462b;
    }

    public final b3.l b() {
        return this.f2463c;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof c)) {
            return false;
        }
        c cVar = (c) obj;
        return D2.h.b(this.f2462b, cVar.f2462b) && D2.h.b(this.f2463c, cVar.f2463c);
    }

    public int hashCode() {
        b3.l lVar = this.f2462b;
        int iHashCode = (lVar != null ? lVar.hashCode() : 0) * 31;
        b3.l lVar2 = this.f2463c;
        return iHashCode + (lVar2 != null ? lVar2.hashCode() : 0);
    }

    public String toString() {
        return this.f2462b.z() + ": " + this.f2463c.z();
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public c(String str, String str2) {
        D2.h.f(str, "name");
        D2.h.f(str2, "value");
        l.a aVar = b3.l.f5639f;
        this(aVar.e(str), aVar.e(str2));
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public c(b3.l lVar, String str) {
        this(lVar, b3.l.f5639f.e(str));
        D2.h.f(lVar, "name");
        D2.h.f(str, "value");
    }
}

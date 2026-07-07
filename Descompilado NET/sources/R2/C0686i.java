package r2;

import java.io.Serializable;

/* JADX INFO: renamed from: r2.i, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0686i implements Serializable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Object f10570b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Object f10571c;

    public C0686i(Object obj, Object obj2) {
        this.f10570b = obj;
        this.f10571c = obj2;
    }

    public final Object a() {
        return this.f10570b;
    }

    public final Object b() {
        return this.f10571c;
    }

    public final Object c() {
        return this.f10570b;
    }

    public final Object d() {
        return this.f10571c;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof C0686i)) {
            return false;
        }
        C0686i c0686i = (C0686i) obj;
        return D2.h.b(this.f10570b, c0686i.f10570b) && D2.h.b(this.f10571c, c0686i.f10571c);
    }

    public int hashCode() {
        Object obj = this.f10570b;
        int iHashCode = (obj == null ? 0 : obj.hashCode()) * 31;
        Object obj2 = this.f10571c;
        return iHashCode + (obj2 != null ? obj2.hashCode() : 0);
    }

    public String toString() {
        return '(' + this.f10570b + ", " + this.f10571c + ')';
    }
}

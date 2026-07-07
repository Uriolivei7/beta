package R1;

/* JADX INFO: loaded from: classes.dex */
public final class j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final k f2048a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final k f2049b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final k f2050c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final k f2051d;

    public j(k kVar, k kVar2, k kVar3, k kVar4) {
        D2.h.f(kVar, "topLeft");
        D2.h.f(kVar2, "topRight");
        D2.h.f(kVar3, "bottomLeft");
        D2.h.f(kVar4, "bottomRight");
        this.f2048a = kVar;
        this.f2049b = kVar2;
        this.f2050c = kVar3;
        this.f2051d = kVar4;
    }

    public final k a() {
        return this.f2050c;
    }

    public final k b() {
        return this.f2051d;
    }

    public final k c() {
        return this.f2048a;
    }

    public final k d() {
        return this.f2049b;
    }

    public final boolean e() {
        return this.f2048a.a() > 0.0f || this.f2048a.b() > 0.0f || this.f2049b.a() > 0.0f || this.f2049b.b() > 0.0f || this.f2050c.a() > 0.0f || this.f2050c.b() > 0.0f || this.f2051d.a() > 0.0f;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof j)) {
            return false;
        }
        j jVar = (j) obj;
        return D2.h.b(this.f2048a, jVar.f2048a) && D2.h.b(this.f2049b, jVar.f2049b) && D2.h.b(this.f2050c, jVar.f2050c) && D2.h.b(this.f2051d, jVar.f2051d);
    }

    public final boolean f() {
        return D2.h.b(this.f2048a, this.f2049b) && D2.h.b(this.f2048a, this.f2050c) && D2.h.b(this.f2048a, this.f2051d);
    }

    public int hashCode() {
        return (((((this.f2048a.hashCode() * 31) + this.f2049b.hashCode()) * 31) + this.f2050c.hashCode()) * 31) + this.f2051d.hashCode();
    }

    public String toString() {
        return "ComputedBorderRadius(topLeft=" + this.f2048a + ", topRight=" + this.f2049b + ", bottomLeft=" + this.f2050c + ", bottomRight=" + this.f2051d + ")";
    }

    public j() {
        this(new k(0.0f, 0.0f), new k(0.0f, 0.0f), new k(0.0f, 0.0f), new k(0.0f, 0.0f));
    }
}

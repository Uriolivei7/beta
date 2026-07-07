package K2;

/* JADX INFO: loaded from: classes.dex */
public final class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f834a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final H2.c f835b;

    public g(String str, H2.c cVar) {
        D2.h.f(str, "value");
        D2.h.f(cVar, "range");
        this.f834a = str;
        this.f835b = cVar;
    }

    public final String a() {
        return this.f834a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof g)) {
            return false;
        }
        g gVar = (g) obj;
        return D2.h.b(this.f834a, gVar.f834a) && D2.h.b(this.f835b, gVar.f835b);
    }

    public int hashCode() {
        return (this.f834a.hashCode() * 31) + this.f835b.hashCode();
    }

    public String toString() {
        return "MatchGroup(value=" + this.f834a + ", range=" + this.f835b + ')';
    }
}

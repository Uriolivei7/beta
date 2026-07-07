package q;

/* JADX INFO: loaded from: classes.dex */
public class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Object f10331a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Object f10332b;

    public d(Object obj, Object obj2) {
        this.f10331a = obj;
        this.f10332b = obj2;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof d)) {
            return false;
        }
        d dVar = (d) obj;
        return c.a(dVar.f10331a, this.f10331a) && c.a(dVar.f10332b, this.f10332b);
    }

    public int hashCode() {
        Object obj = this.f10331a;
        int iHashCode = obj == null ? 0 : obj.hashCode();
        Object obj2 = this.f10332b;
        return iHashCode ^ (obj2 != null ? obj2.hashCode() : 0);
    }

    public String toString() {
        return "Pair{" + this.f10331a + " " + this.f10332b + "}";
    }
}

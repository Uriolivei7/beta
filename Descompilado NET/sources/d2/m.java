package D2;

/* JADX INFO: loaded from: classes.dex */
public final class m implements d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Class f183a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f184b;

    public m(Class<?> cls, String str) {
        h.f(cls, "jClass");
        h.f(str, "moduleName");
        this.f183a = cls;
        this.f184b = str;
    }

    @Override // D2.d
    public Class b() {
        return this.f183a;
    }

    public boolean equals(Object obj) {
        return (obj instanceof m) && h.b(b(), ((m) obj).b());
    }

    public int hashCode() {
        return b().hashCode();
    }

    public String toString() {
        return b().toString() + " (Kotlin reflection is not available)";
    }
}

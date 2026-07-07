package D2;

/* JADX INFO: loaded from: classes.dex */
public abstract class n extends c implements I2.f {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final boolean f185i;

    public n() {
        this.f185i = false;
    }

    @Override // D2.c
    public I2.a a() {
        return this.f185i ? this : super.a();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof n) {
            n nVar = (n) obj;
            return g().equals(nVar.g()) && f().equals(nVar.f()) && i().equals(nVar.i()) && h.b(e(), nVar.e());
        }
        if (obj instanceof I2.f) {
            return obj.equals(a());
        }
        return false;
    }

    public int hashCode() {
        return (((g().hashCode() * 31) + f().hashCode()) * 31) + i().hashCode();
    }

    protected I2.f j() {
        if (this.f185i) {
            throw new UnsupportedOperationException("Kotlin reflection is not yet supported for synthetic Java properties. Please follow/upvote https://youtrack.jetbrains.com/issue/KT-55980");
        }
        return (I2.f) super.h();
    }

    public String toString() {
        I2.a aVarA = a();
        if (aVarA != this) {
            return aVarA.toString();
        }
        return "property " + f() + " (Kotlin reflection is not available)";
    }

    public n(Object obj) {
        super(obj);
        this.f185i = false;
    }

    public n(Object obj, Class cls, String str, String str2, int i3) {
        super(obj, cls, str, str2, (i3 & 1) == 1);
        this.f185i = (i3 & 2) == 2;
    }
}

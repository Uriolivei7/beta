package O0;

/* JADX INFO: loaded from: classes.dex */
public class n implements o {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final o f1480d = d(Integer.MAX_VALUE, true, true);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    int f1481a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    boolean f1482b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    boolean f1483c;

    private n(int i3, boolean z3, boolean z4) {
        this.f1481a = i3;
        this.f1482b = z3;
        this.f1483c = z4;
    }

    public static o d(int i3, boolean z3, boolean z4) {
        return new n(i3, z3, z4);
    }

    @Override // O0.o
    public boolean a() {
        return this.f1483c;
    }

    @Override // O0.o
    public boolean b() {
        return this.f1482b;
    }

    @Override // O0.o
    public int c() {
        return this.f1481a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof n)) {
            return false;
        }
        n nVar = (n) obj;
        return this.f1481a == nVar.f1481a && this.f1482b == nVar.f1482b && this.f1483c == nVar.f1483c;
    }

    public int hashCode() {
        return (this.f1481a ^ (this.f1482b ? 4194304 : 0)) ^ (this.f1483c ? 8388608 : 0);
    }
}

package H2;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class c extends H2.a {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final a f376f = new a(null);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final c f377g = new c(1, 0);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final c a() {
            return c.f377g;
        }

        private a() {
        }
    }

    public c(int i3, int i4) {
        super(i3, i4, 1);
    }

    @Override // H2.a
    public boolean equals(Object obj) {
        if (obj instanceof c) {
            if (!isEmpty() || !((c) obj).isEmpty()) {
                c cVar = (c) obj;
                if (a() != cVar.a() || b() != cVar.b()) {
                }
            }
            return true;
        }
        return false;
    }

    public Integer h() {
        return Integer.valueOf(b());
    }

    @Override // H2.a
    public int hashCode() {
        if (isEmpty()) {
            return -1;
        }
        return (a() * 31) + b();
    }

    public Integer i() {
        return Integer.valueOf(a());
    }

    @Override // H2.a
    public boolean isEmpty() {
        return a() > b();
    }

    @Override // H2.a
    public String toString() {
        return a() + ".." + b();
    }
}

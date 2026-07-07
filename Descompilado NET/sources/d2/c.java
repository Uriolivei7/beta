package D2;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public abstract class c implements I2.a, Serializable {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final Object f166h = a.f173b;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private transient I2.a f167b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected final Object f168c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Class f169d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final String f170e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final String f171f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final boolean f172g;

    private static class a implements Serializable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private static final a f173b = new a();

        private a() {
        }
    }

    public c() {
        this(f166h);
    }

    public I2.a a() {
        I2.a aVar = this.f167b;
        if (aVar != null) {
            return aVar;
        }
        I2.a aVarB = b();
        this.f167b = aVarB;
        return aVarB;
    }

    protected abstract I2.a b();

    public Object e() {
        return this.f168c;
    }

    public String f() {
        return this.f170e;
    }

    public I2.c g() {
        Class cls = this.f169d;
        if (cls == null) {
            return null;
        }
        return this.f172g ? s.b(cls) : s.a(cls);
    }

    protected I2.a h() {
        I2.a aVarA = a();
        if (aVarA != this) {
            return aVarA;
        }
        throw new B2.b();
    }

    public String i() {
        return this.f171f;
    }

    protected c(Object obj) {
        this(obj, null, null, null, false);
    }

    protected c(Object obj, Class cls, String str, String str2, boolean z3) {
        this.f168c = obj;
        this.f169d = cls;
        this.f170e = str;
        this.f171f = str2;
        this.f172g = z3;
    }
}

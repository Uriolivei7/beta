package T2;

import D2.h;
import M2.t;
import b3.k;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final C0032a f2349c = new C0032a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private long f2350a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final k f2351b;

    /* JADX INFO: renamed from: T2.a$a, reason: collision with other inner class name */
    public static final class C0032a {
        private C0032a() {
        }

        public /* synthetic */ C0032a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public a(k kVar) {
        h.f(kVar, "source");
        this.f2351b = kVar;
        this.f2350a = 262144;
    }

    public final t a() {
        t.a aVar = new t.a();
        while (true) {
            String strB = b();
            if (strB.length() == 0) {
                return aVar.e();
            }
            aVar.b(strB);
        }
    }

    public final String b() {
        String strW = this.f2351b.W(this.f2350a);
        this.f2350a -= (long) strW.length();
        return strW;
    }
}

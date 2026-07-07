package F0;

import D2.h;
import M2.D;
import M2.t;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class d extends Exception {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f231d = new a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Integer f232b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final t f233c;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final d a(D d4) {
            h.f(d4, "response");
            D dT0 = d4.t0();
            Integer numValueOf = dT0 != null ? Integer.valueOf(dT0.A()) : null;
            D dT02 = d4.t0();
            return new d(numValueOf, dT02 != null ? dT02.d0() : null);
        }

        private a() {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public d() {
        this(null, 0 == true ? 1 : 0, 3, 0 == true ? 1 : 0);
    }

    public /* synthetic */ d(Integer num, t tVar, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? null : num, (i3 & 2) != 0 ? null : tVar);
    }

    public d(Integer num, t tVar) {
        this.f232b = num;
        this.f233c = tVar;
    }
}

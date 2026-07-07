package E1;

import D2.h;
import com.facebook.react.bridge.ReadableMap;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class b extends U0.b {

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    public static final a f202D = new a(null);

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private final ReadableMap f203B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private final E1.a f204C;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ b c(a aVar, U0.c cVar, ReadableMap readableMap, E1.a aVar2, int i3, Object obj) {
            if ((i3 & 4) != 0) {
                aVar2 = E1.a.f196b;
            }
            return aVar.b(cVar, readableMap, aVar2);
        }

        public final b a(U0.c cVar, ReadableMap readableMap) {
            h.f(cVar, "builder");
            return c(this, cVar, readableMap, null, 4, null);
        }

        public final b b(U0.c cVar, ReadableMap readableMap, E1.a aVar) {
            h.f(cVar, "builder");
            h.f(aVar, "cacheControl");
            return new b(cVar, readableMap, aVar, null);
        }

        private a() {
        }
    }

    public /* synthetic */ b(U0.c cVar, ReadableMap readableMap, E1.a aVar, DefaultConstructorMarker defaultConstructorMarker) {
        this(cVar, readableMap, aVar);
    }

    public static final b A(U0.c cVar, ReadableMap readableMap) {
        return f202D.a(cVar, readableMap);
    }

    public final E1.a B() {
        return this.f204C;
    }

    public final ReadableMap C() {
        return this.f203B;
    }

    private b(U0.c cVar, ReadableMap readableMap, E1.a aVar) {
        super(cVar);
        this.f203B = readableMap;
        this.f204C = aVar;
    }
}

package F;

import D2.h;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Map f210a = new LinkedHashMap();

    /* JADX INFO: renamed from: F.a$a, reason: collision with other inner class name */
    public static final class C0003a extends a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public static final C0003a f211b = new C0003a();

        private C0003a() {
        }

        @Override // F.a
        public Object a(b bVar) {
            h.f(bVar, "key");
            return null;
        }
    }

    public interface b {
    }

    public abstract Object a(b bVar);

    public final Map b() {
        return this.f210a;
    }
}

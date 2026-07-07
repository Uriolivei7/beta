package Z2;

import D2.h;
import W2.j;
import java.util.List;
import javax.net.ssl.X509TrustManager;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public abstract class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f2846a = new a(null);

    public static final class a {
        private a() {
        }

        public final c a(X509TrustManager x509TrustManager) {
            h.f(x509TrustManager, "trustManager");
            return j.f2732c.g().c(x509TrustManager);
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public abstract List a(List list, String str);
}

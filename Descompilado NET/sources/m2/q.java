package M2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0711h;

/* JADX INFO: loaded from: classes.dex */
public interface q {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f1213b = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final q f1212a = new a.C0020a();

    public static final class a {

        /* JADX INFO: renamed from: M2.q$a$a, reason: collision with other inner class name */
        private static final class C0020a implements q {
            @Override // M2.q
            public List a(String str) throws UnknownHostException {
                D2.h.f(str, "hostname");
                try {
                    InetAddress[] allByName = InetAddress.getAllByName(str);
                    D2.h.e(allByName, "InetAddress.getAllByName(hostname)");
                    return AbstractC0711h.B(allByName);
                } catch (NullPointerException e4) {
                    UnknownHostException unknownHostException = new UnknownHostException("Broken system behaviour for dns lookup of " + str);
                    unknownHostException.initCause(e4);
                    throw unknownHostException;
                }
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    List a(String str);
}

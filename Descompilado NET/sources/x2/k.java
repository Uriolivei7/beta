package X2;

import X2.l;
import java.util.List;
import javax.net.ssl.SSLSocket;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.conscrypt.Conscrypt;

/* JADX INFO: loaded from: classes.dex */
public final class k implements m {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final b f2782b = new b(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final l.a f2781a = new a();

    public static final class a implements l.a {
        a() {
        }

        @Override // X2.l.a
        public boolean a(SSLSocket sSLSocket) {
            D2.h.f(sSLSocket, "sslSocket");
            return W2.d.f2713f.c() && Conscrypt.isConscrypt(sSLSocket);
        }

        @Override // X2.l.a
        public m b(SSLSocket sSLSocket) {
            D2.h.f(sSLSocket, "sslSocket");
            return new k();
        }
    }

    public static final class b {
        private b() {
        }

        public final l.a a() {
            return k.f2781a;
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Override // X2.m
    public boolean a(SSLSocket sSLSocket) {
        D2.h.f(sSLSocket, "sslSocket");
        return Conscrypt.isConscrypt(sSLSocket);
    }

    @Override // X2.m
    public boolean b() {
        return W2.d.f2713f.c();
    }

    @Override // X2.m
    public String c(SSLSocket sSLSocket) {
        D2.h.f(sSLSocket, "sslSocket");
        if (a(sSLSocket)) {
            return Conscrypt.getApplicationProtocol(sSLSocket);
        }
        return null;
    }

    @Override // X2.m
    public void d(SSLSocket sSLSocket, String str, List list) {
        D2.h.f(sSLSocket, "sslSocket");
        D2.h.f(list, "protocols");
        if (a(sSLSocket)) {
            Conscrypt.setUseSessionTickets(sSLSocket, true);
            Object[] array = W2.j.f2732c.b(list).toArray(new String[0]);
            if (array == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            Conscrypt.setApplicationProtocols(sSLSocket, (String[]) array);
        }
    }
}

package O2;

import D2.h;
import K2.o;
import M2.B;
import M2.C0190a;
import M2.C0197h;
import M2.D;
import M2.F;
import M2.InterfaceC0191b;
import M2.q;
import M2.u;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class b implements InterfaceC0191b {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final q f1597d;

    /* JADX WARN: Multi-variable type inference failed */
    public b() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    private final InetAddress b(Proxy proxy, u uVar, q qVar) {
        Proxy.Type type = proxy.type();
        if (type != null && a.f1596a[type.ordinal()] == 1) {
            return (InetAddress) AbstractC0717n.N(qVar.a(uVar.h()));
        }
        SocketAddress socketAddressAddress = proxy.address();
        if (socketAddressAddress == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.net.InetSocketAddress");
        }
        InetAddress address = ((InetSocketAddress) socketAddressAddress).getAddress();
        h.e(address, "(address() as InetSocketAddress).address");
        return address;
    }

    @Override // M2.InterfaceC0191b
    public B a(F f3, D d4) {
        Proxy proxyB;
        q qVarC;
        PasswordAuthentication passwordAuthenticationRequestPasswordAuthentication;
        C0190a c0190aA;
        h.f(d4, "response");
        List<C0197h> listZ = d4.z();
        B bY0 = d4.y0();
        u uVarL = bY0.l();
        boolean z3 = d4.A() == 407;
        if (f3 == null || (proxyB = f3.b()) == null) {
            proxyB = Proxy.NO_PROXY;
        }
        for (C0197h c0197h : listZ) {
            if (o.n("Basic", c0197h.c(), true)) {
                if (f3 == null || (c0190aA = f3.a()) == null || (qVarC = c0190aA.c()) == null) {
                    qVarC = this.f1597d;
                }
                if (z3) {
                    SocketAddress socketAddressAddress = proxyB.address();
                    if (socketAddressAddress == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.net.InetSocketAddress");
                    }
                    InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddressAddress;
                    String hostName = inetSocketAddress.getHostName();
                    h.e(proxyB, "proxy");
                    passwordAuthenticationRequestPasswordAuthentication = Authenticator.requestPasswordAuthentication(hostName, b(proxyB, uVarL, qVarC), inetSocketAddress.getPort(), uVarL.p(), c0197h.b(), c0197h.c(), uVarL.r(), Authenticator.RequestorType.PROXY);
                } else {
                    String strH = uVarL.h();
                    h.e(proxyB, "proxy");
                    passwordAuthenticationRequestPasswordAuthentication = Authenticator.requestPasswordAuthentication(strH, b(proxyB, uVarL, qVarC), uVarL.l(), uVarL.p(), c0197h.b(), c0197h.c(), uVarL.r(), Authenticator.RequestorType.SERVER);
                }
                if (passwordAuthenticationRequestPasswordAuthentication != null) {
                    String str = z3 ? "Proxy-Authorization" : "Authorization";
                    String userName = passwordAuthenticationRequestPasswordAuthentication.getUserName();
                    h.e(userName, "auth.userName");
                    char[] password = passwordAuthenticationRequestPasswordAuthentication.getPassword();
                    h.e(password, "auth.password");
                    return bY0.i().e(str, M2.o.a(userName, new String(password), c0197h.a())).b();
                }
            }
        }
        return null;
    }

    public b(q qVar) {
        h.f(qVar, "defaultDns");
        this.f1597d = qVar;
    }

    public /* synthetic */ b(q qVar, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? q.f1212a : qVar);
    }
}

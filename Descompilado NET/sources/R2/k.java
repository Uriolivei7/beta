package R2;

import M2.C0190a;
import M2.F;
import M2.InterfaceC0194e;
import M2.r;
import M2.u;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class k {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final a f2194i = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private List f2195a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f2196b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private List f2197c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final List f2198d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final C0190a f2199e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final i f2200f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final InterfaceC0194e f2201g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final r f2202h;

    public static final class a {
        private a() {
        }

        public final String a(InetSocketAddress inetSocketAddress) {
            D2.h.f(inetSocketAddress, "$this$socketHost");
            InetAddress address = inetSocketAddress.getAddress();
            if (address != null) {
                String hostAddress = address.getHostAddress();
                D2.h.e(hostAddress, "address.hostAddress");
                return hostAddress;
            }
            String hostName = inetSocketAddress.getHostName();
            D2.h.e(hostName, "hostName");
            return hostName;
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public static final class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private int f2203a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final List f2204b;

        public b(List<F> list) {
            D2.h.f(list, "routes");
            this.f2204b = list;
        }

        public final List a() {
            return this.f2204b;
        }

        public final boolean b() {
            return this.f2203a < this.f2204b.size();
        }

        public final F c() {
            if (!b()) {
                throw new NoSuchElementException();
            }
            List list = this.f2204b;
            int i3 = this.f2203a;
            this.f2203a = i3 + 1;
            return (F) list.get(i3);
        }
    }

    static final class c extends D2.i implements C2.a {

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ Proxy f2206d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ u f2207e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(Proxy proxy, u uVar) {
            super(0);
            this.f2206d = proxy;
            this.f2207e = uVar;
        }

        @Override // C2.a
        /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
        public final List a() {
            Proxy proxy = this.f2206d;
            if (proxy != null) {
                return AbstractC0717n.b(proxy);
            }
            URI uriQ = this.f2207e.q();
            if (uriQ.getHost() == null) {
                return N2.c.t(Proxy.NO_PROXY);
            }
            List<Proxy> listSelect = k.this.f2199e.i().select(uriQ);
            return (listSelect == null || listSelect.isEmpty()) ? N2.c.t(Proxy.NO_PROXY) : N2.c.R(listSelect);
        }
    }

    public k(C0190a c0190a, i iVar, InterfaceC0194e interfaceC0194e, r rVar) {
        D2.h.f(c0190a, "address");
        D2.h.f(iVar, "routeDatabase");
        D2.h.f(interfaceC0194e, "call");
        D2.h.f(rVar, "eventListener");
        this.f2199e = c0190a;
        this.f2200f = iVar;
        this.f2201g = interfaceC0194e;
        this.f2202h = rVar;
        this.f2195a = AbstractC0717n.g();
        this.f2197c = AbstractC0717n.g();
        this.f2198d = new ArrayList();
        g(c0190a.l(), c0190a.g());
    }

    private final boolean c() {
        return this.f2196b < this.f2195a.size();
    }

    private final Proxy e() throws SocketException, UnknownHostException {
        if (c()) {
            List list = this.f2195a;
            int i3 = this.f2196b;
            this.f2196b = i3 + 1;
            Proxy proxy = (Proxy) list.get(i3);
            f(proxy);
            return proxy;
        }
        throw new SocketException("No route to " + this.f2199e.l().h() + "; exhausted proxy configurations: " + this.f2195a);
    }

    private final void f(Proxy proxy) throws SocketException, UnknownHostException {
        String strH;
        int iL;
        ArrayList arrayList = new ArrayList();
        this.f2197c = arrayList;
        if (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.SOCKS) {
            strH = this.f2199e.l().h();
            iL = this.f2199e.l().l();
        } else {
            SocketAddress socketAddressAddress = proxy.address();
            if (!(socketAddressAddress instanceof InetSocketAddress)) {
                throw new IllegalArgumentException(("Proxy.address() is not an InetSocketAddress: " + socketAddressAddress.getClass()).toString());
            }
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddressAddress;
            strH = f2194i.a(inetSocketAddress);
            iL = inetSocketAddress.getPort();
        }
        if (1 > iL || 65535 < iL) {
            throw new SocketException("No route to " + strH + ':' + iL + "; port is out of range");
        }
        if (proxy.type() == Proxy.Type.SOCKS) {
            arrayList.add(InetSocketAddress.createUnresolved(strH, iL));
            return;
        }
        this.f2202h.n(this.f2201g, strH);
        List listA = this.f2199e.c().a(strH);
        if (listA.isEmpty()) {
            throw new UnknownHostException(this.f2199e.c() + " returned no addresses for " + strH);
        }
        this.f2202h.m(this.f2201g, strH, listA);
        Iterator it = listA.iterator();
        while (it.hasNext()) {
            arrayList.add(new InetSocketAddress((InetAddress) it.next(), iL));
        }
    }

    private final void g(u uVar, Proxy proxy) {
        c cVar = new c(proxy, uVar);
        this.f2202h.p(this.f2201g, uVar);
        List listA = cVar.a();
        this.f2195a = listA;
        this.f2196b = 0;
        this.f2202h.o(this.f2201g, uVar, listA);
    }

    public final boolean b() {
        return c() || !this.f2198d.isEmpty();
    }

    public final b d() {
        if (!b()) {
            throw new NoSuchElementException();
        }
        ArrayList arrayList = new ArrayList();
        while (c()) {
            Proxy proxyE = e();
            Iterator it = this.f2197c.iterator();
            while (it.hasNext()) {
                F f3 = new F(this.f2199e, proxyE, (InetSocketAddress) it.next());
                if (this.f2200f.c(f3)) {
                    this.f2198d.add(f3);
                } else {
                    arrayList.add(f3);
                }
            }
            if (!arrayList.isEmpty()) {
                break;
            }
        }
        if (arrayList.isEmpty()) {
            AbstractC0717n.t(arrayList, this.f2198d);
            this.f2198d.clear();
        }
        return new b(arrayList);
    }
}

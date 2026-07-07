package M2;

import M2.u;
import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;
import java.util.Objects;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

/* JADX INFO: renamed from: M2.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0190a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final u f957a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final List f958b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final List f959c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final q f960d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final SocketFactory f961e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final SSLSocketFactory f962f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final HostnameVerifier f963g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final C0196g f964h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final InterfaceC0191b f965i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final Proxy f966j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final ProxySelector f967k;

    public C0190a(String str, int i3, q qVar, SocketFactory socketFactory, SSLSocketFactory sSLSocketFactory, HostnameVerifier hostnameVerifier, C0196g c0196g, InterfaceC0191b interfaceC0191b, Proxy proxy, List<? extends A> list, List<l> list2, ProxySelector proxySelector) {
        D2.h.f(str, "uriHost");
        D2.h.f(qVar, "dns");
        D2.h.f(socketFactory, "socketFactory");
        D2.h.f(interfaceC0191b, "proxyAuthenticator");
        D2.h.f(list, "protocols");
        D2.h.f(list2, "connectionSpecs");
        D2.h.f(proxySelector, "proxySelector");
        this.f960d = qVar;
        this.f961e = socketFactory;
        this.f962f = sSLSocketFactory;
        this.f963g = hostnameVerifier;
        this.f964h = c0196g;
        this.f965i = interfaceC0191b;
        this.f966j = proxy;
        this.f967k = proxySelector;
        this.f957a = new u.a().o(sSLSocketFactory != null ? "https" : "http").e(str).k(i3).a();
        this.f958b = N2.c.R(list);
        this.f959c = N2.c.R(list2);
    }

    public final C0196g a() {
        return this.f964h;
    }

    public final List b() {
        return this.f959c;
    }

    public final q c() {
        return this.f960d;
    }

    public final boolean d(C0190a c0190a) {
        D2.h.f(c0190a, "that");
        return D2.h.b(this.f960d, c0190a.f960d) && D2.h.b(this.f965i, c0190a.f965i) && D2.h.b(this.f958b, c0190a.f958b) && D2.h.b(this.f959c, c0190a.f959c) && D2.h.b(this.f967k, c0190a.f967k) && D2.h.b(this.f966j, c0190a.f966j) && D2.h.b(this.f962f, c0190a.f962f) && D2.h.b(this.f963g, c0190a.f963g) && D2.h.b(this.f964h, c0190a.f964h) && this.f957a.l() == c0190a.f957a.l();
    }

    public final HostnameVerifier e() {
        return this.f963g;
    }

    public boolean equals(Object obj) {
        if (obj instanceof C0190a) {
            C0190a c0190a = (C0190a) obj;
            if (D2.h.b(this.f957a, c0190a.f957a) && d(c0190a)) {
                return true;
            }
        }
        return false;
    }

    public final List f() {
        return this.f958b;
    }

    public final Proxy g() {
        return this.f966j;
    }

    public final InterfaceC0191b h() {
        return this.f965i;
    }

    public int hashCode() {
        return ((((((((((((((((((527 + this.f957a.hashCode()) * 31) + this.f960d.hashCode()) * 31) + this.f965i.hashCode()) * 31) + this.f958b.hashCode()) * 31) + this.f959c.hashCode()) * 31) + this.f967k.hashCode()) * 31) + Objects.hashCode(this.f966j)) * 31) + Objects.hashCode(this.f962f)) * 31) + Objects.hashCode(this.f963g)) * 31) + Objects.hashCode(this.f964h);
    }

    public final ProxySelector i() {
        return this.f967k;
    }

    public final SocketFactory j() {
        return this.f961e;
    }

    public final SSLSocketFactory k() {
        return this.f962f;
    }

    public final u l() {
        return this.f957a;
    }

    public String toString() {
        StringBuilder sb;
        Object obj;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Address{");
        sb2.append(this.f957a.h());
        sb2.append(':');
        sb2.append(this.f957a.l());
        sb2.append(", ");
        if (this.f966j != null) {
            sb = new StringBuilder();
            sb.append("proxy=");
            obj = this.f966j;
        } else {
            sb = new StringBuilder();
            sb.append("proxySelector=");
            obj = this.f967k;
        }
        sb.append(obj);
        sb2.append(sb.toString());
        sb2.append("}");
        return sb2.toString();
    }
}

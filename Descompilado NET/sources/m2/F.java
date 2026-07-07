package M2;

import java.net.InetSocketAddress;
import java.net.Proxy;

/* JADX INFO: loaded from: classes.dex */
public final class F {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final C0190a f946a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Proxy f947b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final InetSocketAddress f948c;

    public F(C0190a c0190a, Proxy proxy, InetSocketAddress inetSocketAddress) {
        D2.h.f(c0190a, "address");
        D2.h.f(proxy, "proxy");
        D2.h.f(inetSocketAddress, "socketAddress");
        this.f946a = c0190a;
        this.f947b = proxy;
        this.f948c = inetSocketAddress;
    }

    public final C0190a a() {
        return this.f946a;
    }

    public final Proxy b() {
        return this.f947b;
    }

    public final boolean c() {
        return this.f946a.k() != null && this.f947b.type() == Proxy.Type.HTTP;
    }

    public final InetSocketAddress d() {
        return this.f948c;
    }

    public boolean equals(Object obj) {
        if (obj instanceof F) {
            F f3 = (F) obj;
            if (D2.h.b(f3.f946a, this.f946a) && D2.h.b(f3.f947b, this.f947b) && D2.h.b(f3.f948c, this.f948c)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return ((((527 + this.f946a.hashCode()) * 31) + this.f947b.hashCode()) * 31) + this.f948c.hashCode();
    }

    public String toString() {
        return "Route{" + this.f948c + '}';
    }
}

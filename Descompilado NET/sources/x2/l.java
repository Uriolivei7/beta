package X2;

import java.util.List;
import javax.net.ssl.SSLSocket;

/* JADX INFO: loaded from: classes.dex */
public final class l implements m {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private m f2783a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final a f2784b;

    public interface a {
        boolean a(SSLSocket sSLSocket);

        m b(SSLSocket sSLSocket);
    }

    public l(a aVar) {
        D2.h.f(aVar, "socketAdapterFactory");
        this.f2784b = aVar;
    }

    private final synchronized m e(SSLSocket sSLSocket) {
        try {
            if (this.f2783a == null && this.f2784b.a(sSLSocket)) {
                this.f2783a = this.f2784b.b(sSLSocket);
            }
        } catch (Throwable th) {
            throw th;
        }
        return this.f2783a;
    }

    @Override // X2.m
    public boolean a(SSLSocket sSLSocket) {
        D2.h.f(sSLSocket, "sslSocket");
        return this.f2784b.a(sSLSocket);
    }

    @Override // X2.m
    public boolean b() {
        return true;
    }

    @Override // X2.m
    public String c(SSLSocket sSLSocket) {
        D2.h.f(sSLSocket, "sslSocket");
        m mVarE = e(sSLSocket);
        if (mVarE != null) {
            return mVarE.c(sSLSocket);
        }
        return null;
    }

    @Override // X2.m
    public void d(SSLSocket sSLSocket, String str, List list) {
        D2.h.f(sSLSocket, "sslSocket");
        D2.h.f(list, "protocols");
        m mVarE = e(sSLSocket);
        if (mVarE != null) {
            mVarE.d(sSLSocket, str, list);
        }
    }
}

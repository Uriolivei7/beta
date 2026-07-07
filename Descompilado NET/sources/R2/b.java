package R2;

import M2.l;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.UnknownServiceException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f2104a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f2105b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f2106c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final List f2107d;

    public b(List<l> list) {
        D2.h.f(list, "connectionSpecs");
        this.f2107d = list;
    }

    private final boolean c(SSLSocket sSLSocket) {
        int size = this.f2107d.size();
        for (int i3 = this.f2104a; i3 < size; i3++) {
            if (((l) this.f2107d.get(i3)).e(sSLSocket)) {
                return true;
            }
        }
        return false;
    }

    public final l a(SSLSocket sSLSocket) throws UnknownServiceException, CloneNotSupportedException {
        l lVar;
        D2.h.f(sSLSocket, "sslSocket");
        int i3 = this.f2104a;
        int size = this.f2107d.size();
        while (true) {
            if (i3 >= size) {
                lVar = null;
                break;
            }
            lVar = (l) this.f2107d.get(i3);
            if (lVar.e(sSLSocket)) {
                this.f2104a = i3 + 1;
                break;
            }
            i3++;
        }
        if (lVar != null) {
            this.f2105b = c(sSLSocket);
            lVar.c(sSLSocket, this.f2106c);
            return lVar;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to find acceptable protocols. isFallback=");
        sb.append(this.f2106c);
        sb.append(',');
        sb.append(" modes=");
        sb.append(this.f2107d);
        sb.append(',');
        sb.append(" supported protocols=");
        String[] enabledProtocols = sSLSocket.getEnabledProtocols();
        D2.h.c(enabledProtocols);
        String string = Arrays.toString(enabledProtocols);
        D2.h.e(string, "java.util.Arrays.toString(this)");
        sb.append(string);
        throw new UnknownServiceException(sb.toString());
    }

    public final boolean b(IOException iOException) {
        D2.h.f(iOException, "e");
        this.f2106c = true;
        return (!this.f2105b || (iOException instanceof ProtocolException) || (iOException instanceof InterruptedIOException) || ((iOException instanceof SSLHandshakeException) && (iOException.getCause() instanceof CertificateException)) || (iOException instanceof SSLPeerUnverifiedException) || !(iOException instanceof SSLException)) ? false : true;
    }
}

package S2;

import M2.B;
import M2.u;
import java.net.Proxy;

/* JADX INFO: loaded from: classes.dex */
public final class i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final i f2335a = new i();

    private i() {
    }

    private final boolean b(B b4, Proxy.Type type) {
        return !b4.g() && type == Proxy.Type.HTTP;
    }

    public final String a(B b4, Proxy.Type type) {
        D2.h.f(b4, "request");
        D2.h.f(type, "proxyType");
        StringBuilder sb = new StringBuilder();
        sb.append(b4.h());
        sb.append(' ');
        i iVar = f2335a;
        if (iVar.b(b4, type)) {
            sb.append(b4.l());
        } else {
            sb.append(iVar.c(b4.l()));
        }
        sb.append(" HTTP/1.1");
        String string = sb.toString();
        D2.h.e(string, "StringBuilder().apply(builderAction).toString()");
        return string;
    }

    public final String c(u uVar) {
        D2.h.f(uVar, "url");
        String strD = uVar.d();
        String strF = uVar.f();
        if (strF == null) {
            return strD;
        }
        return strD + '?' + strF;
    }
}

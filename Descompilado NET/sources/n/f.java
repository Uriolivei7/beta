package N;

import android.webkit.WebMessagePort;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import org.chromium.support_lib_boundary.WebMessagePortBoundaryInterface;

/* JADX INFO: loaded from: classes.dex */
public class f extends M.c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private WebMessagePort f1344a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private WebMessagePortBoundaryInterface f1345b;

    public f(WebMessagePort webMessagePort) {
        this.f1344a = webMessagePort;
    }

    private WebMessagePortBoundaryInterface b() {
        if (this.f1345b == null) {
            this.f1345b = (WebMessagePortBoundaryInterface) e3.a.a(WebMessagePortBoundaryInterface.class, j.c().b(this.f1344a));
        }
        return this.f1345b;
    }

    @Override // M.c
    public InvocationHandler a() {
        return Proxy.getInvocationHandler(b());
    }

    public f(InvocationHandler invocationHandler) {
        this.f1345b = (WebMessagePortBoundaryInterface) e3.a.a(WebMessagePortBoundaryInterface.class, invocationHandler);
    }
}

package N;

import java.lang.reflect.InvocationHandler;
import java.util.concurrent.Callable;
import org.chromium.support_lib_boundary.JsReplyProxyBoundaryInterface;

/* JADX INFO: loaded from: classes.dex */
public class c extends M.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private JsReplyProxyBoundaryInterface f1340a;

    class a implements Callable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ JsReplyProxyBoundaryInterface f1341a;

        a(JsReplyProxyBoundaryInterface jsReplyProxyBoundaryInterface) {
            this.f1341a = jsReplyProxyBoundaryInterface;
        }

        @Override // java.util.concurrent.Callable
        public Object call() {
            return new c(this.f1341a);
        }
    }

    public c(JsReplyProxyBoundaryInterface jsReplyProxyBoundaryInterface) {
        this.f1340a = jsReplyProxyBoundaryInterface;
    }

    public static c a(InvocationHandler invocationHandler) {
        JsReplyProxyBoundaryInterface jsReplyProxyBoundaryInterface = (JsReplyProxyBoundaryInterface) e3.a.a(JsReplyProxyBoundaryInterface.class, invocationHandler);
        return (c) jsReplyProxyBoundaryInterface.getOrCreatePeer(new a(jsReplyProxyBoundaryInterface));
    }
}

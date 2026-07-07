package N;

import java.lang.reflect.InvocationHandler;
import org.chromium.support_lib_boundary.WebMessageBoundaryInterface;

/* JADX INFO: loaded from: classes.dex */
public class d implements WebMessageBoundaryInterface {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private M.b f1342a;

    public d(M.b bVar) {
        this.f1342a = bVar;
    }

    private static M.c[] a(InvocationHandler[] invocationHandlerArr) {
        M.c[] cVarArr = new M.c[invocationHandlerArr.length];
        for (int i3 = 0; i3 < invocationHandlerArr.length; i3++) {
            cVarArr[i3] = new f(invocationHandlerArr[i3]);
        }
        return cVarArr;
    }

    public static M.b b(WebMessageBoundaryInterface webMessageBoundaryInterface) {
        return new M.b(webMessageBoundaryInterface.getData(), a(webMessageBoundaryInterface.getPorts()));
    }

    @Override // org.chromium.support_lib_boundary.WebMessageBoundaryInterface
    public String getData() {
        return this.f1342a.a();
    }

    @Override // org.chromium.support_lib_boundary.WebMessageBoundaryInterface
    public InvocationHandler[] getPorts() {
        M.c[] cVarArrB = this.f1342a.b();
        if (cVarArrB == null) {
            return null;
        }
        InvocationHandler[] invocationHandlerArr = new InvocationHandler[cVarArrB.length];
        for (int i3 = 0; i3 < cVarArrB.length; i3++) {
            invocationHandlerArr[i3] = cVarArrB[i3].a();
        }
        return invocationHandlerArr;
    }

    @Override // org.chromium.support_lib_boundary.FeatureFlagHolderBoundaryInterface
    public String[] getSupportedFeatures() {
        return new String[0];
    }
}

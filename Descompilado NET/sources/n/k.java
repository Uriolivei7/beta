package N;

import M.f;
import org.chromium.support_lib_boundary.WebViewProviderBoundaryInterface;

/* JADX INFO: loaded from: classes.dex */
public class k {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    WebViewProviderBoundaryInterface f1399a;

    public k(WebViewProviderBoundaryInterface webViewProviderBoundaryInterface) {
        this.f1399a = webViewProviderBoundaryInterface;
    }

    public void a(String str, String[] strArr, f.a aVar) {
        this.f1399a.addWebMessageListener(str, strArr, e3.a.c(new e(aVar)));
    }
}

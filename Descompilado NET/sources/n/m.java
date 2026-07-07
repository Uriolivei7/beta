package N;

import android.webkit.WebView;
import org.chromium.support_lib_boundary.WebViewProviderBoundaryInterface;
import org.chromium.support_lib_boundary.WebViewProviderFactoryBoundaryInterface;
import org.chromium.support_lib_boundary.WebkitToCompatConverterBoundaryInterface;

/* JADX INFO: loaded from: classes.dex */
public class m implements l {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    WebViewProviderFactoryBoundaryInterface f1400a;

    public m(WebViewProviderFactoryBoundaryInterface webViewProviderFactoryBoundaryInterface) {
        this.f1400a = webViewProviderFactoryBoundaryInterface;
    }

    @Override // N.l
    public String[] a() {
        return this.f1400a.getSupportedFeatures();
    }

    @Override // N.l
    public WebViewProviderBoundaryInterface createWebView(WebView webView) {
        return (WebViewProviderBoundaryInterface) e3.a.a(WebViewProviderBoundaryInterface.class, this.f1400a.createWebView(webView));
    }

    @Override // N.l
    public WebkitToCompatConverterBoundaryInterface getWebkitToCompatConverter() {
        return (WebkitToCompatConverterBoundaryInterface) e3.a.a(WebkitToCompatConverterBoundaryInterface.class, this.f1400a.getWebkitToCompatConverter());
    }
}

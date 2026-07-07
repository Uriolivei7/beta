package N;

import android.webkit.WebView;
import org.chromium.support_lib_boundary.WebViewProviderBoundaryInterface;
import org.chromium.support_lib_boundary.WebkitToCompatConverterBoundaryInterface;

/* JADX INFO: loaded from: classes.dex */
public interface l {
    String[] a();

    WebViewProviderBoundaryInterface createWebView(WebView webView);

    WebkitToCompatConverterBoundaryInterface getWebkitToCompatConverter();
}

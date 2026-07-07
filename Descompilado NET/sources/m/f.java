package M;

import N.h;
import N.j;
import N.k;
import N.l;
import android.net.Uri;
import android.webkit.WebView;
import java.util.Set;
import org.chromium.support_lib_boundary.WebViewProviderBoundaryInterface;

/* JADX INFO: loaded from: classes.dex */
public abstract class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final Uri f867a = Uri.parse("*");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Uri f868b = Uri.parse("");

    public interface a {
        void a(WebView webView, b bVar, Uri uri, boolean z3, M.a aVar);
    }

    public static void a(WebView webView, String str, Set set, a aVar) {
        if (!h.WEB_MESSAGE_LISTENER.g()) {
            throw h.a();
        }
        d(webView).a(str, (String[]) set.toArray(new String[0]), aVar);
    }

    private static WebViewProviderBoundaryInterface b(WebView webView) {
        return c().createWebView(webView);
    }

    private static l c() {
        return j.d();
    }

    private static k d(WebView webView) {
        return new k(b(webView));
    }
}

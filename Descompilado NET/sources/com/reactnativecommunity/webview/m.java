package com.reactnativecommunity.webview;

import android.app.Activity;
import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.uimanager.B0;
import e1.C0527d;
import g1.C0542a;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class m {

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    public static final a f8517C = new a(null);

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private final int f8518A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private final int f8519B;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final boolean f8520a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f8521b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private j f8522c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f8523d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f8524e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private String f8525f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private String f8526g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f8527h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private ReadableMap f8528i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private String f8529j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private String f8530k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final String f8531l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final String f8532m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final String f8533n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private final String f8534o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private final String f8535p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private final String f8536q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private final int f8537r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private final int f8538s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final int f8539t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private final int f8540u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private final int f8541v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private final int f8542w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private final int f8543x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private final int f8544y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private final int f8545z;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public static final class b extends com.reactnativecommunity.webview.c {
        b(f fVar) {
            super(fVar);
        }

        @Override // android.webkit.WebChromeClient
        public Bitmap getDefaultVideoPoster() {
            return Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        }
    }

    public static final class c extends com.reactnativecommunity.webview.c {

        /* JADX INFO: renamed from: p, reason: collision with root package name */
        final /* synthetic */ Activity f8546p;

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ int f8547q;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(f fVar, Activity activity, int i3) {
            super(fVar);
            this.f8546p = activity;
            this.f8547q = i3;
        }

        @Override // android.webkit.WebChromeClient
        public Bitmap getDefaultVideoPoster() {
            return Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        }

        @Override // android.webkit.WebChromeClient
        public void onHideCustomView() {
            if (this.f8461c == null) {
                return;
            }
            ViewGroup viewGroupC = c();
            if (viewGroupC.getRootView() != this.f8460b.getRootView()) {
                this.f8460b.getRootView().setVisibility(0);
            } else {
                this.f8460b.setVisibility(0);
            }
            this.f8546p.getWindow().clearFlags(512);
            viewGroupC.removeView(this.f8461c);
            this.f8462d.onCustomViewHidden();
            this.f8461c = null;
            this.f8462d = null;
            this.f8546p.setRequestedOrientation(this.f8547q);
            this.f8460b.getThemedReactContext().removeLifecycleEventListener(this);
        }

        @Override // android.webkit.WebChromeClient
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
            D2.h.f(view, "view");
            D2.h.f(customViewCallback, "callback");
            if (this.f8461c != null) {
                customViewCallback.onCustomViewHidden();
                return;
            }
            this.f8461c = view;
            this.f8462d = customViewCallback;
            this.f8546p.setRequestedOrientation(-1);
            this.f8461c.setSystemUiVisibility(7942);
            this.f8546p.getWindow().setFlags(512, 512);
            this.f8461c.setBackgroundColor(-16777216);
            ViewGroup viewGroupC = c();
            viewGroupC.addView(this.f8461c, com.reactnativecommunity.webview.c.f8459o);
            if (viewGroupC.getRootView() != this.f8460b.getRootView()) {
                this.f8460b.getRootView().setVisibility(8);
            } else {
                this.f8460b.setVisibility(8);
            }
            this.f8460b.getThemedReactContext().addLifecycleEventListener(this);
        }
    }

    public m() {
        this(false, 1, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void f(f fVar, m mVar, String str, String str2, String str3, String str4, long j3) {
        fVar.setIgnoreErrFailedForThisURL(str);
        RNCWebViewModule rNCWebViewModule = (RNCWebViewModule) fVar.getReactApplicationContext().getNativeModule(RNCWebViewModule.class);
        if (rNCWebViewModule == null) {
            return;
        }
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
            String strA = s.a(str, str3, str4);
            D2.h.c(strA);
            String strC = n.a().c(strA, "_");
            String str5 = "Downloading " + strC;
            try {
                URL url = new URL(str);
                request.addRequestHeader("Cookie", CookieManager.getInstance().getCookie(url.getProtocol() + "://" + url.getHost()));
            } catch (MalformedURLException e4) {
                Log.w(mVar.f8521b, "Error getting cookie for DownloadManager", e4);
            }
            request.addRequestHeader("User-Agent", str2);
            request.setTitle(strC);
            request.setDescription(str5);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(1);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, strC);
            rNCWebViewModule.setDownloadRequest(request);
            if (rNCWebViewModule.grantFileDownloaderPermissions(mVar.h(), mVar.i())) {
                rNCWebViewModule.downloadFile(mVar.h());
            }
        } catch (IllegalArgumentException e5) {
            Log.w(mVar.f8521b, "Unsupported URI, aborting download", e5);
        }
    }

    private final void f0(r rVar) {
        f webView = rVar.getWebView();
        if (this.f8529j != null) {
            webView.getSettings().setUserAgentString(this.f8529j);
        } else if (this.f8530k != null) {
            webView.getSettings().setUserAgentString(this.f8530k);
        } else {
            webView.getSettings().setUserAgentString(WebSettings.getDefaultUserAgent(webView.getContext()));
        }
    }

    private final String h() {
        String str = this.f8525f;
        return str == null ? this.f8535p : str;
    }

    private final void h0(f fVar) {
        Activity currentActivity = fVar.getThemedReactContext().getCurrentActivity();
        if (this.f8523d && currentActivity != null) {
            c cVar = new c(fVar, currentActivity, currentActivity.getRequestedOrientation());
            cVar.f(this.f8524e);
            cVar.g(this.f8527h);
            fVar.setWebChromeClient(cVar);
            return;
        }
        com.reactnativecommunity.webview.c cVar2 = (com.reactnativecommunity.webview.c) fVar.getWebChromeClient();
        if (cVar2 != null) {
            cVar2.onHideCustomView();
        }
        b bVar = new b(fVar);
        bVar.f(this.f8524e);
        bVar.g(this.f8527h);
        fVar.setWebChromeClient(bVar);
    }

    private final String i() {
        String str = this.f8526g;
        return str == null ? this.f8536q : str;
    }

    private final void j(r rVar, ReadableMap readableMap) {
        byte[] bytes;
        f webView = rVar.getWebView();
        if (readableMap != null) {
            if (readableMap.hasKey("html")) {
                String string = readableMap.getString("html");
                String string2 = readableMap.hasKey("baseUrl") ? readableMap.getString("baseUrl") : "";
                D2.h.c(string);
                webView.loadDataWithBaseURL(string2, string, this.f8532m, this.f8531l, null);
                return;
            }
            if (readableMap.hasKey("uri")) {
                String string3 = readableMap.getString("uri");
                String url = webView.getUrl();
                if (url == null || !D2.h.b(url, string3)) {
                    if (readableMap.hasKey("method") && K2.o.n(readableMap.getString("method"), this.f8533n, true)) {
                        if (readableMap.hasKey("body")) {
                            String string4 = readableMap.getString("body");
                            try {
                                D2.h.c(string4);
                                Charset charsetForName = Charset.forName("UTF-8");
                                D2.h.e(charsetForName, "forName(...)");
                                bytes = string4.getBytes(charsetForName);
                                D2.h.e(bytes, "getBytes(...)");
                            } catch (UnsupportedEncodingException unused) {
                                D2.h.c(string4);
                                bytes = string4.getBytes(K2.d.f816b);
                                D2.h.e(bytes, "getBytes(...)");
                            }
                        } else {
                            bytes = null;
                        }
                        if (bytes == null) {
                            bytes = new byte[0];
                        }
                        D2.h.c(string3);
                        webView.postUrl(string3, bytes);
                        return;
                    }
                    HashMap map = new HashMap();
                    if (readableMap.hasKey("headers")) {
                        if (this.f8520a) {
                            ReadableArray array = readableMap.getArray("headers");
                            D2.h.c(array);
                            Iterator<Object> it = array.toArrayList().iterator();
                            D2.h.e(it, "iterator(...)");
                            while (it.hasNext()) {
                                Object next = it.next();
                                D2.h.d(next, "null cannot be cast to non-null type java.util.HashMap<kotlin.String, kotlin.String>");
                                HashMap map2 = (HashMap) next;
                                String str = (String) map2.get("name");
                                if (str == null) {
                                    str = "";
                                }
                                String str2 = (String) map2.get("value");
                                if (str2 == null) {
                                    str2 = "";
                                }
                                Locale locale = Locale.ENGLISH;
                                D2.h.e(locale, "ENGLISH");
                                String lowerCase = str.toLowerCase(locale);
                                D2.h.e(lowerCase, "toLowerCase(...)");
                                if (D2.h.b("user-agent", lowerCase)) {
                                    webView.getSettings().setUserAgentString(str2);
                                } else {
                                    map.put(str, str2);
                                }
                            }
                        } else {
                            ReadableMap map3 = readableMap.getMap("headers");
                            D2.h.c(map3);
                            ReadableMapKeySetIterator readableMapKeySetIteratorKeySetIterator = map3.keySetIterator();
                            while (readableMapKeySetIteratorKeySetIterator.hasNextKey()) {
                                String strNextKey = readableMapKeySetIteratorKeySetIterator.nextKey();
                                Locale locale2 = Locale.ENGLISH;
                                D2.h.e(locale2, "ENGLISH");
                                String lowerCase2 = strNextKey.toLowerCase(locale2);
                                D2.h.e(lowerCase2, "toLowerCase(...)");
                                if (D2.h.b("user-agent", lowerCase2)) {
                                    webView.getSettings().setUserAgentString(map3.getString(strNextKey));
                                } else {
                                    map.put(strNextKey, map3.getString(strNextKey));
                                }
                            }
                        }
                    }
                    D2.h.c(string3);
                    webView.loadUrl(string3, map);
                    return;
                }
                return;
            }
        }
        webView.loadUrl(this.f8534o);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void k(WebView webView) {
    }

    public final void A(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setGeolocationEnabled(z3);
    }

    public final void B(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        f webView = rVar.getWebView();
        this.f8527h = z3;
        h0(webView);
    }

    public final void C(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().setHasScrollEvent(z3);
    }

    public final void D(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        f webView = rVar.getWebView();
        if (z3) {
            CookieManager.getInstance().removeAllCookies(null);
            webView.getSettings().setCacheMode(2);
            webView.clearHistory();
            webView.clearCache(true);
            webView.clearFormData();
            webView.getSettings().setSavePassword(false);
            webView.getSettings().setSaveFormData(false);
        }
    }

    public final void E(r rVar, String str) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().f8475b = str;
    }

    public final void F(r rVar, String str) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().f8476c = str;
    }

    public final void G(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().f8480g = z3;
    }

    public final void H(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().f8479f = z3;
    }

    public final void I(r rVar, String str) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().setInjectedJavaScriptObject(str);
    }

    public final void J(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setJavaScriptCanOpenWindowsAutomatically(z3);
    }

    public final void K(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setJavaScriptEnabled(z3);
    }

    public final void L(String str) {
        this.f8526g = str;
    }

    public final void M(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setMediaPlaybackRequiresUserGesture(z3);
    }

    public final void N(r rVar, ReadableArray readableArray) {
        D2.h.f(rVar, "viewWrapper");
        f webView = rVar.getWebView();
        if (readableArray == null) {
            webView.setMenuCustomItems(null);
            return;
        }
        ArrayList<Object> arrayList = readableArray.toArrayList();
        D2.h.d(arrayList, "null cannot be cast to non-null type kotlin.collections.List<kotlin.collections.Map<kotlin.String, kotlin.String>>");
        webView.setMenuCustomItems(arrayList);
    }

    public final void O(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().setMessagingEnabled(z3);
    }

    public final void P(r rVar, String str) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().f8482i = str;
    }

    public final void Q(r rVar, int i3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setMinimumFontSize(i3);
    }

    public final void R(r rVar, String str) {
        D2.h.f(rVar, "viewWrapper");
        f webView = rVar.getWebView();
        if (str == null || D2.h.b("never", str)) {
            webView.getSettings().setMixedContentMode(1);
        } else if (D2.h.b("always", str)) {
            webView.getSettings().setMixedContentMode(0);
        } else if (D2.h.b("compatibility", str)) {
            webView.getSettings().setMixedContentMode(2);
        }
    }

    public final void S(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().f8488o = z3;
    }

    public final void T(r rVar, String str) {
        D2.h.f(rVar, "viewWrapper");
        f webView = rVar.getWebView();
        int i3 = 0;
        if (str != null) {
            int iHashCode = str.hashCode();
            if (iHashCode == -1414557169) {
                str.equals("always");
            } else if (iHashCode != 104712844) {
                if (iHashCode == 951530617 && str.equals("content")) {
                    i3 = 1;
                }
            } else if (str.equals("never")) {
                i3 = 2;
            }
        }
        webView.setOverScrollMode(i3);
    }

    public final void U(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setSaveFormData(!z3);
    }

    public final void V(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        f webView = rVar.getWebView();
        webView.getSettings().setLoadWithOverviewMode(z3);
        webView.getSettings().setUseWideViewPort(z3);
    }

    public final void W(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setBuiltInZoomControls(z3);
    }

    public final void X(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setDisplayZoomControls(z3);
    }

    public final void Y(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setSupportMultipleWindows(z3);
    }

    public final void Z(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().setHorizontalScrollBarEnabled(z3);
    }

    public final void a0(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().setVerticalScrollBarEnabled(z3);
    }

    public final void b0(r rVar, ReadableMap readableMap) {
        D2.h.f(rVar, "viewWrapper");
        this.f8528i = readableMap;
    }

    public final f c(B0 b02) {
        D2.h.f(b02, "context");
        return new f(b02);
    }

    public final void c0(r rVar, int i3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setTextZoom(i3);
    }

    public final r d(B0 b02) {
        D2.h.f(b02, "context");
        return e(b02, c(b02));
    }

    public final void d0(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        CookieManager.getInstance().setAcceptThirdPartyCookies(rVar.getWebView(), z3);
    }

    public final r e(B0 b02, final f fVar) {
        D2.h.f(b02, "context");
        D2.h.f(fVar, "webView");
        h0(fVar);
        b02.addLifecycleEventListener(fVar);
        this.f8522c.a(fVar);
        WebSettings settings = fVar.getSettings();
        D2.h.e(settings, "getSettings(...)");
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setDomStorageEnabled(true);
        settings.setSupportMultipleWindows(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setMixedContentMode(1);
        fVar.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        if (C0542a.f9423b) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        fVar.setDownloadListener(new DownloadListener() { // from class: com.reactnativecommunity.webview.l
            @Override // android.webkit.DownloadListener
            public final void onDownloadStart(String str, String str2, String str3, String str4, long j3) {
                m.f(fVar, this, str, str2, str3, str4, j3);
            }
        });
        return new r(b02, fVar);
    }

    public final void e0(r rVar, String str) {
        D2.h.f(rVar, "viewWrapper");
        this.f8529j = str;
        f0(rVar);
    }

    public final Map g() {
        return C0527d.a().b("goBack", Integer.valueOf(this.f8537r)).b("goForward", Integer.valueOf(this.f8538s)).b("reload", Integer.valueOf(this.f8539t)).b("stopLoading", Integer.valueOf(this.f8540u)).b("postMessage", Integer.valueOf(this.f8541v)).b("injectJavaScript", Integer.valueOf(this.f8542w)).b("loadUrl", Integer.valueOf(this.f8543x)).b("requestFocus", Integer.valueOf(this.f8544y)).b("clearFormData", Integer.valueOf(this.f8545z)).b("clearCache", Integer.valueOf(this.f8518A)).b("clearHistory", Integer.valueOf(this.f8519B)).a();
    }

    public final void g0(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        WebView.setWebContentsDebuggingEnabled(z3);
    }

    public final void l(r rVar) {
        D2.h.f(rVar, "viewWrapper");
        ReadableMap readableMap = this.f8528i;
        if (readableMap != null) {
            j(rVar, readableMap);
        }
        this.f8528i = null;
    }

    public final void m(r rVar) {
        D2.h.f(rVar, "viewWrapper");
        f webView = rVar.getWebView();
        webView.getThemedReactContext().removeLifecycleEventListener(webView);
        webView.c();
        webView.f8491r = null;
    }

    public final void n(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setAllowFileAccess(z3);
    }

    public final void o(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setAllowFileAccessFromFileURLs(z3);
    }

    public final void p(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setAllowUniversalAccessFromFileURLs(z3);
    }

    public final void q(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        f webView = rVar.getWebView();
        this.f8523d = z3;
        h0(webView);
    }

    public final void r(r rVar, boolean z3) {
        WebChromeClient webChromeClient;
        D2.h.f(rVar, "viewWrapper");
        f webView = rVar.getWebView();
        this.f8524e = z3;
        if (Build.VERSION.SDK_INT < 26 || (webChromeClient = webView.getWebChromeClient()) == null || !(webChromeClient instanceof com.reactnativecommunity.webview.c)) {
            return;
        }
        ((com.reactnativecommunity.webview.c) webChromeClient).f(z3);
    }

    public final void s(r rVar, String str) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().setLayerType(D2.h.b(str, "hardware") ? 2 : D2.h.b(str, "software") ? 1 : 0, null);
    }

    public final void t(r rVar, String str) {
        D2.h.f(rVar, "viewWrapper");
        if (str != null) {
            this.f8530k = WebSettings.getDefaultUserAgent(rVar.getWebView().getContext()) + " " + str;
        } else {
            this.f8530k = null;
        }
        f0(rVar);
    }

    public final void u(r rVar, ReadableMap readableMap) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().setBasicAuthCredential((readableMap != null && readableMap.hasKey("username") && readableMap.hasKey("password")) ? new com.reactnativecommunity.webview.a(readableMap.getString("username"), readableMap.getString("password")) : null);
    }

    public final void v(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setCacheMode(z3 ? -1 : 2);
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public final void w(r rVar, String str) {
        D2.h.f(rVar, "viewWrapper");
        WebSettings settings = rVar.getWebView().getSettings();
        int i3 = -1;
        if (str != null) {
            switch (str.hashCode()) {
                case -2059164003:
                    if (str.equals("LOAD_NO_CACHE")) {
                        i3 = 2;
                    }
                    break;
                case -1215135800:
                    str.equals("LOAD_DEFAULT");
                    break;
                case -873877826:
                    if (str.equals("LOAD_CACHE_ELSE_NETWORK")) {
                        i3 = 1;
                    }
                    break;
                case 1548620642:
                    if (str.equals("LOAD_CACHE_ONLY")) {
                        i3 = 3;
                    }
                    break;
            }
        }
        settings.setCacheMode(i3);
    }

    public final void x(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        rVar.getWebView().getSettings().setDomStorageEnabled(z3);
    }

    public final void y(String str) {
        this.f8525f = str;
    }

    public final void z(r rVar, boolean z3) {
        D2.h.f(rVar, "viewWrapper");
        f webView = rVar.getWebView();
        if (Build.VERSION.SDK_INT > 28) {
            if (M.g.a("FORCE_DARK")) {
                M.e.b(webView.getSettings(), z3 ? 2 : 0);
            }
            if (z3 && M.g.a("FORCE_DARK_STRATEGY")) {
                M.e.c(webView.getSettings(), 2);
            }
        }
    }

    public m(boolean z3) {
        this.f8520a = z3;
        this.f8521b = "RNCWebViewManagerImpl";
        this.f8522c = new j() { // from class: com.reactnativecommunity.webview.k
            @Override // com.reactnativecommunity.webview.j
            public final void a(WebView webView) {
                m.k(webView);
            }
        };
        this.f8531l = "UTF-8";
        this.f8532m = "text/html";
        this.f8533n = "POST";
        this.f8534o = "about:blank";
        this.f8535p = "Downloading";
        this.f8536q = "Cannot download files as permission was denied. Please provide permission to write to storage, in order to download files.";
        this.f8537r = 1;
        this.f8538s = 2;
        this.f8539t = 3;
        this.f8540u = 4;
        this.f8541v = 5;
        this.f8542w = 6;
        this.f8543x = 7;
        this.f8544y = 8;
        this.f8545z = 1000;
        this.f8518A = 1001;
        this.f8519B = 1002;
    }

    public /* synthetic */ m(boolean z3, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? false : z3);
    }
}

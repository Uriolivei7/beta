package com.reactnativecommunity.webview;

import M.f;
import android.graphics.Rect;
import android.net.Uri;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.H0;
import java.util.List;
import java.util.Map;
import l2.C0592a;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class f extends WebView implements LifecycleEventListener {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected String f8475b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected String f8476c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected e f8477d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    protected f.a f8478e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    protected boolean f8479f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    protected boolean f8480g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    protected boolean f8481h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    protected String f8482i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    protected RNCWebViewMessagingModule f8483j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    protected i f8484k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    protected boolean f8485l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private com.facebook.react.views.scroll.c f8486m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    protected boolean f8487n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    protected boolean f8488o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    protected d f8489p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    protected List f8490q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    WebChromeClient f8491r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    protected String f8492s;

    class a extends ActionMode.Callback2 {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ActionMode.Callback f8493a;

        /* JADX INFO: renamed from: com.reactnativecommunity.webview.f$a$a, reason: collision with other inner class name */
        class C0121a implements ValueCallback {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ MenuItem f8495a;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            final /* synthetic */ WritableMap f8496b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            final /* synthetic */ ActionMode f8497c;

            C0121a(MenuItem menuItem, WritableMap writableMap, ActionMode actionMode) {
                this.f8495a = menuItem;
                this.f8496b = writableMap;
                this.f8497c = actionMode;
            }

            /* JADX WARN: Type inference fix 'apply assigned field type' failed
            java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
            	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
            	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
            	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
             */
            @Override // android.webkit.ValueCallback
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onReceiveValue(String str) {
                String string;
                Map map = (Map) f.this.f8490q.get(this.f8495a.getItemId());
                this.f8496b.putString("label", (String) map.get("label"));
                this.f8496b.putString("key", (String) map.get("key"));
                try {
                    string = new JSONObject(str).getString("selection");
                } catch (JSONException unused) {
                    string = "";
                }
                this.f8496b.putString("selectedText", string);
                f fVar = f.this;
                fVar.g(fVar, new C0592a(r.a(f.this), this.f8496b));
                this.f8497c.finish();
            }
        }

        a(ActionMode.Callback callback) {
            this.f8493a = callback;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            f.this.evaluateJavascript("(function(){return {selection: window.getSelection().toString()} })()", new C0121a(menuItem, Arguments.createMap(), actionMode));
            return true;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            for (int i3 = 0; i3 < f.this.f8490q.size(); i3++) {
                menu.add(0, i3, i3, (CharSequence) ((Map) f.this.f8490q.get(i3)).get("label"));
            }
            return true;
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
        }

        @Override // android.view.ActionMode.Callback2
        public void onGetContentRect(ActionMode actionMode, View view, Rect rect) {
            ActionMode.Callback callback = this.f8493a;
            if (callback instanceof ActionMode.Callback2) {
                ((ActionMode.Callback2) callback).onGetContentRect(actionMode, view, rect);
            } else {
                super.onGetContentRect(actionMode, view, rect);
            }
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }
    }

    class b implements f.a {
        b() {
        }

        @Override // M.f.a
        public void a(WebView webView, M.b bVar, Uri uri, boolean z3, M.a aVar) {
            f.this.j(bVar.a(), uri.toString());
        }
    }

    class c implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ WebView f8500b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ String f8501c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ String f8502d;

        c(WebView webView, String str, String str2) {
            this.f8500b = webView;
            this.f8501c = str;
            this.f8502d = str2;
        }

        @Override // java.lang.Runnable
        public void run() {
            i iVar = f.this.f8484k;
            if (iVar == null) {
                return;
            }
            WritableMap writableMapA = iVar.a(this.f8500b, this.f8501c);
            writableMapA.putString("data", this.f8502d);
            f fVar = f.this;
            if (fVar.f8483j != null) {
                fVar.e(writableMapA);
            } else {
                fVar.g(this.f8500b, new l2.g(r.a(this.f8500b), writableMapA));
            }
        }
    }

    protected static class d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private boolean f8504a = false;

        protected d() {
        }

        public boolean a() {
            return this.f8504a;
        }

        public void b(boolean z3) {
            this.f8504a = z3;
        }
    }

    protected class e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private String f8505a = "RNCWebViewBridge";

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        f f8506b;

        e(f fVar) {
            this.f8506b = fVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void b(String str) {
            f fVar = this.f8506b;
            fVar.j(str, fVar.getUrl());
        }

        @JavascriptInterface
        public void postMessage(final String str) {
            if (this.f8506b.getMessagingEnabled()) {
                this.f8506b.post(new Runnable() { // from class: com.reactnativecommunity.webview.g
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f8508b.b(str);
                    }
                });
            } else {
                Y.a.I(this.f8505a, "ReactNativeWebView.postMessage method was called but messaging is disabled. Pass an onMessage handler to the WebView.");
            }
        }
    }

    public f(B0 b02) {
        super(b02);
        this.f8478e = null;
        this.f8479f = true;
        this.f8480g = true;
        this.f8481h = false;
        this.f8485l = false;
        this.f8487n = false;
        this.f8488o = false;
        this.f8492s = null;
        this.f8483j = (RNCWebViewMessagingModule) ((B0) getContext()).b().getJSModule(RNCWebViewMessagingModule.class);
        this.f8489p = new d();
    }

    private void i() {
        String str;
        if (getSettings().getJavaScriptEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("(function(){\n    window.ReactNativeWebView = window.ReactNativeWebView || {};\n    window.ReactNativeWebView.injectedObjectJson = function () { return ");
            if (this.f8492s == null) {
                str = null;
            } else {
                str = "`" + this.f8492s + "`";
            }
            sb.append(str);
            sb.append("; };\n})();");
            h(sb.toString());
        }
    }

    public void a() {
        String str;
        if (!getSettings().getJavaScriptEnabled() || (str = this.f8475b) == null || TextUtils.isEmpty(str)) {
            return;
        }
        h("(function() {\n" + this.f8475b + ";\n})();");
        i();
    }

    public void b() {
        String str;
        if (!getSettings().getJavaScriptEnabled() || (str = this.f8476c) == null || TextUtils.isEmpty(str)) {
            return;
        }
        h("(function() {\n" + this.f8476c + ";\n})();");
        i();
    }

    protected void c() {
        setWebViewClient(null);
        destroy();
    }

    protected void d(f fVar) {
        if (M.g.a("WEB_MESSAGE_LISTENER")) {
            if (this.f8478e == null) {
                this.f8478e = new b();
                M.f.a(fVar, "ReactNativeWebView", com.reactnativecommunity.webview.e.a(new Object[]{"*"}), this.f8478e);
            }
        } else if (this.f8477d == null) {
            e eVar = new e(fVar);
            this.f8477d = eVar;
            addJavascriptInterface(eVar, "ReactNativeWebView");
        }
        i();
    }

    @Override // android.webkit.WebView
    public void destroy() {
        WebChromeClient webChromeClient = this.f8491r;
        if (webChromeClient != null) {
            webChromeClient.onHideCustomView();
        }
        super.destroy();
    }

    protected void e(WritableMap writableMap) {
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        writableNativeMap.putMap("nativeEvent", writableMap);
        writableNativeMap.putString("messagingModuleName", this.f8482i);
        this.f8483j.onMessage(writableNativeMap);
    }

    protected boolean f(WritableMap writableMap) {
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        writableNativeMap.putMap("nativeEvent", writableMap);
        writableNativeMap.putString("messagingModuleName", this.f8482i);
        this.f8483j.onShouldStartLoadWithRequest(writableNativeMap);
        return true;
    }

    protected void g(WebView webView, P1.d dVar) {
        H0.c(getThemedReactContext(), r.a(webView)).b(dVar);
    }

    public boolean getMessagingEnabled() {
        return this.f8481h;
    }

    public i getRNCWebViewClient() {
        return this.f8484k;
    }

    public ReactApplicationContext getReactApplicationContext() {
        return getThemedReactContext().b();
    }

    public B0 getThemedReactContext() {
        return (B0) getContext();
    }

    @Override // android.webkit.WebView
    public WebChromeClient getWebChromeClient() {
        return this.f8491r;
    }

    protected void h(String str) {
        evaluateJavascript(str, null);
    }

    public void j(String str, String str2) {
        getThemedReactContext();
        if (this.f8484k != null) {
            post(new c(this, str2, str));
            return;
        }
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putString("data", str);
        if (this.f8483j != null) {
            e(writableMapCreateMap);
        } else {
            g(this, new l2.g(r.a(this), writableMapCreateMap));
        }
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostDestroy() {
        c();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostPause() {
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostResume() {
    }

    @Override // android.webkit.WebView, android.view.View
    protected void onScrollChanged(int i3, int i4, int i5, int i6) {
        super.onScrollChanged(i3, i4, i5, i6);
        if (this.f8487n) {
            if (this.f8486m == null) {
                this.f8486m = new com.facebook.react.views.scroll.c();
            }
            if (this.f8486m.c(i3, i4)) {
                g(this, com.facebook.react.views.scroll.k.y(r.a(this), com.facebook.react.views.scroll.l.f7890e, i3, i4, this.f8486m.a(), this.f8486m.b(), computeHorizontalScrollRange(), computeVerticalScrollRange(), getWidth(), getHeight()));
            }
        }
    }

    @Override // android.webkit.WebView, android.view.View
    protected void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        if (this.f8485l) {
            g(this, new P1.c(r.a(this), i3, i4));
        }
    }

    @Override // android.webkit.WebView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.f8488o) {
            requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setBasicAuthCredential(com.reactnativecommunity.webview.a aVar) {
        this.f8484k.c(aVar);
    }

    public void setHasScrollEvent(boolean z3) {
        this.f8487n = z3;
    }

    public void setIgnoreErrFailedForThisURL(String str) {
        this.f8484k.d(str);
    }

    public void setInjectedJavaScriptObject(String str) {
        this.f8492s = str;
        i();
    }

    public void setMenuCustomItems(List<Map<String, String>> list) {
        this.f8490q = list;
    }

    public void setMessagingEnabled(boolean z3) {
        if (this.f8481h == z3) {
            return;
        }
        this.f8481h = z3;
        if (z3) {
            d(this);
        }
    }

    public void setNestedScrollEnabled(boolean z3) {
        this.f8488o = z3;
    }

    public void setSendContentSizeChangeEvents(boolean z3) {
        this.f8485l = z3;
    }

    @Override // android.webkit.WebView
    public void setWebChromeClient(WebChromeClient webChromeClient) {
        this.f8491r = webChromeClient;
        super.setWebChromeClient(webChromeClient);
        if (webChromeClient instanceof com.reactnativecommunity.webview.c) {
            ((com.reactnativecommunity.webview.c) webChromeClient).h(this.f8489p);
        }
    }

    @Override // android.webkit.WebView
    public void setWebViewClient(WebViewClient webViewClient) {
        super.setWebViewClient(webViewClient);
        if (webViewClient instanceof i) {
            i iVar = (i) webViewClient;
            this.f8484k = iVar;
            iVar.e(this.f8489p);
        }
    }

    @Override // android.view.View
    public ActionMode startActionMode(ActionMode.Callback callback, int i3) {
        return this.f8490q == null ? super.startActionMode(callback, i3) : super.startActionMode(new a(callback), i3);
    }
}

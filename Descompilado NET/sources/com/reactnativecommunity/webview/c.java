package com.reactnativecommunity.webview;

import android.R;
import android.content.ComponentCallbacks2;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.H0;
import com.facebook.react.views.progressbar.ReactProgressBarViewManager;
import com.reactnativecommunity.webview.f;
import g1.C0542a;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class c extends WebChromeClient implements LifecycleEventListener {

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    protected static final FrameLayout.LayoutParams f8459o = new FrameLayout.LayoutParams(-1, -1, 17);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected f f8460b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected View f8461c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected WebChromeClient.CustomViewCallback f8462d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    protected PermissionRequest f8463e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    protected List f8464f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    protected GeolocationPermissions.Callback f8465g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    protected String f8466h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    protected boolean f8467i = false;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    protected List f8468j = new ArrayList();

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    protected f.d f8469k = null;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    protected boolean f8470l = false;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    protected boolean f8471m = false;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private B1.g f8472n = new B1.g() { // from class: com.reactnativecommunity.webview.b
        @Override // B1.g
        public final boolean onRequestPermissionsResult(int i3, String[] strArr, int[] iArr) {
            return this.f8458b.d(i3, strArr, iArr);
        }
    };

    class a extends WebViewClient {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ WebView f8473a;

        a(WebView webView) {
            this.f8473a = webView;
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            WritableMap writableMapCreateMap = Arguments.createMap();
            writableMapCreateMap.putString("targetUrl", str);
            WebView webView2 = this.f8473a;
            ((f) webView2).g(webView2, new l2.h(r.a(this.f8473a), writableMapCreateMap));
            return true;
        }
    }

    public c(f fVar) {
        this.f8460b = fVar;
    }

    private B1.f b() {
        ComponentCallbacks2 currentActivity = this.f8460b.getThemedReactContext().getCurrentActivity();
        if (currentActivity == null) {
            throw new IllegalStateException("Tried to use permissions API while not attached to an Activity.");
        }
        if (currentActivity instanceof B1.f) {
            return (B1.f) currentActivity;
        }
        throw new IllegalStateException("Tried to use permissions API but the host Activity doesn't implement PermissionAwareActivity.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean d(int i3, String[] strArr, int[] iArr) {
        PermissionRequest permissionRequest;
        List list;
        List list2;
        List list3;
        List list4;
        GeolocationPermissions.Callback callback;
        String str;
        this.f8467i = false;
        boolean z3 = false;
        for (int i4 = 0; i4 < strArr.length; i4++) {
            String str2 = strArr[i4];
            boolean z4 = iArr[i4] == 0;
            if (str2.equals("android.permission.ACCESS_FINE_LOCATION") && (callback = this.f8465g) != null && (str = this.f8466h) != null) {
                if (z4) {
                    callback.invoke(str, true, false);
                } else {
                    callback.invoke(str, false, false);
                }
                this.f8465g = null;
                this.f8466h = null;
            }
            if (str2.equals("android.permission.RECORD_AUDIO")) {
                if (z4 && (list4 = this.f8464f) != null) {
                    list4.add("android.webkit.resource.AUDIO_CAPTURE");
                }
                z3 = true;
            }
            if (str2.equals("android.permission.CAMERA")) {
                if (z4 && (list3 = this.f8464f) != null) {
                    list3.add("android.webkit.resource.VIDEO_CAPTURE");
                }
                z3 = true;
            }
            if (str2.equals("android.webkit.resource.PROTECTED_MEDIA_ID")) {
                if (z4 && (list2 = this.f8464f) != null) {
                    list2.add("android.webkit.resource.PROTECTED_MEDIA_ID");
                }
                z3 = true;
            }
        }
        if (z3 && (permissionRequest = this.f8463e) != null && (list = this.f8464f) != null) {
            permissionRequest.grant((String[]) list.toArray(new String[0]));
            this.f8463e = null;
            this.f8464f = null;
        }
        if (this.f8468j.isEmpty()) {
            return true;
        }
        e(this.f8468j);
        return false;
    }

    private synchronized void e(List list) {
        if (this.f8467i) {
            this.f8468j.addAll(list);
            return;
        }
        B1.f fVarB = b();
        this.f8467i = true;
        fVarB.m((String[]) list.toArray(new String[0]), 3, this.f8472n);
        this.f8468j.clear();
    }

    protected ViewGroup c() {
        return (ViewGroup) this.f8460b.getThemedReactContext().getCurrentActivity().findViewById(R.id.content);
    }

    public void f(boolean z3) {
        this.f8470l = z3;
    }

    public void g(boolean z3) {
        this.f8471m = z3;
    }

    public void h(f.d dVar) {
        this.f8469k = dVar;
    }

    @Override // android.webkit.WebChromeClient
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (C0542a.f9423b) {
            return super.onConsoleMessage(consoleMessage);
        }
        return true;
    }

    @Override // android.webkit.WebChromeClient
    public boolean onCreateWindow(WebView webView, boolean z3, boolean z4, Message message) {
        WebView webView2 = new WebView(webView.getContext());
        if (this.f8471m) {
            webView2.setWebViewClient(new a(webView));
        }
        ((WebView.WebViewTransport) message.obj).setWebView(webView2);
        message.sendToTarget();
        return true;
    }

    @Override // android.webkit.WebChromeClient
    public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
        if (androidx.core.content.a.a(this.f8460b.getThemedReactContext(), "android.permission.ACCESS_FINE_LOCATION") == 0) {
            callback.invoke(str, true, false);
            return;
        }
        this.f8465g = callback;
        this.f8466h = str;
        e(Collections.singletonList("android.permission.ACCESS_FINE_LOCATION"));
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostDestroy() {
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostPause() {
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostResume() {
        View view = this.f8461c;
        if (view == null || view.getSystemUiVisibility() == 7942) {
            return;
        }
        this.f8461c.setSystemUiVisibility(7942);
    }

    @Override // android.webkit.WebChromeClient
    public void onPermissionRequest(PermissionRequest permissionRequest) {
        this.f8464f = new ArrayList();
        ArrayList arrayList = new ArrayList();
        String[] resources = permissionRequest.getResources();
        int length = resources.length;
        int i3 = 0;
        while (true) {
            String str = null;
            if (i3 >= length) {
                break;
            }
            String str2 = resources[i3];
            if (str2.equals("android.webkit.resource.AUDIO_CAPTURE")) {
                str = "android.permission.RECORD_AUDIO";
            } else if (str2.equals("android.webkit.resource.VIDEO_CAPTURE")) {
                str = "android.permission.CAMERA";
            } else if (str2.equals("android.webkit.resource.PROTECTED_MEDIA_ID")) {
                if (this.f8470l) {
                    this.f8464f.add(str2);
                } else {
                    str = "android.webkit.resource.PROTECTED_MEDIA_ID";
                }
            }
            if (str != null) {
                if (androidx.core.content.a.a(this.f8460b.getThemedReactContext(), str) == 0) {
                    this.f8464f.add(str2);
                } else {
                    arrayList.add(str);
                }
            }
            i3++;
        }
        if (arrayList.isEmpty()) {
            permissionRequest.grant((String[]) this.f8464f.toArray(new String[0]));
            this.f8464f = null;
        } else {
            this.f8463e = permissionRequest;
            e(arrayList);
        }
    }

    @Override // android.webkit.WebChromeClient
    public void onProgressChanged(WebView webView, int i3) {
        super.onProgressChanged(webView, i3);
        String url = webView.getUrl();
        if (this.f8469k.a()) {
            return;
        }
        int iA = r.a(webView);
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putDouble("target", iA);
        writableMapCreateMap.putString("title", webView.getTitle());
        writableMapCreateMap.putString("url", url);
        writableMapCreateMap.putBoolean("canGoBack", webView.canGoBack());
        writableMapCreateMap.putBoolean("canGoForward", webView.canGoForward());
        writableMapCreateMap.putDouble(ReactProgressBarViewManager.PROP_PROGRESS, i3 / 100.0f);
        H0.c(this.f8460b.getThemedReactContext(), iA).b(new l2.e(iA, writableMapCreateMap));
    }

    @Override // android.webkit.WebChromeClient
    public boolean onShowFileChooser(WebView webView, ValueCallback valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return ((RNCWebViewModule) this.f8460b.getThemedReactContext().getNativeModule(RNCWebViewModule.class)).startPhotoPickerIntent(valueCallback, fileChooserParams.getAcceptTypes(), fileChooserParams.getMode() == 1, fileChooserParams.isCaptureEnabled());
    }
}

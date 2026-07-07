package com.reactnativecommunity.webview;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class r extends FrameLayout {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f8571c = new a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final f f8572b;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int a(WebView webView) {
            D2.h.f(webView, "webView");
            Object parent = webView.getParent();
            View view = parent instanceof View ? (View) parent : null;
            if (view != null) {
                return view.getId();
            }
            return -1;
        }

        private a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public r(Context context, f fVar) {
        super(context);
        D2.h.f(context, "context");
        D2.h.f(fVar, "webView");
        fVar.setBackgroundColor(0);
        addView(fVar);
        View childAt = getChildAt(0);
        D2.h.d(childAt, "null cannot be cast to non-null type com.reactnativecommunity.webview.RNCWebView");
        this.f8572b = (f) childAt;
    }

    public static final int a(WebView webView) {
        return f8571c.a(webView);
    }

    public final f getWebView() {
        return this.f8572b;
    }
}

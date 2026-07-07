package com.facebook.react.modules.network;

import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactContext;
import java.net.CookieHandler;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0696D;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class d extends CookieHandler {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final a f7004b = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private CookieManager f7005a;

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean b(String str) {
            return K2.o.n(str, "Set-cookie", true) || K2.o.n(str, "Set-cookie2", true);
        }

        private a() {
        }
    }

    public d() {
    }

    private final void b(String str, String str2) {
        CookieManager cookieManagerG = g();
        if (cookieManagerG != null) {
            cookieManagerG.setCookie(str, str2, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void e(Callback callback, Boolean bool) {
        callback.invoke(bool);
    }

    private final CookieManager g() {
        if (this.f7005a == null) {
            try {
                this.f7005a = CookieManager.getInstance();
            } catch (IllegalArgumentException | Exception unused) {
                return null;
            }
        }
        return this.f7005a;
    }

    public final void c(String str, List list) {
        D2.h.f(str, "url");
        D2.h.f(list, "cookies");
        Iterator it = list.iterator();
        while (it.hasNext()) {
            b(str, (String) it.next());
        }
        CookieManager cookieManagerG = g();
        if (cookieManagerG != null) {
            cookieManagerG.flush();
        }
    }

    public final void d(final Callback callback) {
        D2.h.f(callback, "callback");
        CookieManager cookieManagerG = g();
        if (cookieManagerG != null) {
            cookieManagerG.removeAllCookies(new ValueCallback() { // from class: com.facebook.react.modules.network.c
                @Override // android.webkit.ValueCallback
                public final void onReceiveValue(Object obj) {
                    d.e(callback, (Boolean) obj);
                }
            });
        }
    }

    public final void f() {
    }

    @Override // java.net.CookieHandler
    public Map get(URI uri, Map map) {
        D2.h.f(uri, "uri");
        D2.h.f(map, "headers");
        CookieManager cookieManagerG = g();
        String cookie = cookieManagerG != null ? cookieManagerG.getCookie(uri.toString()) : null;
        return (cookie == null || cookie.length() == 0) ? AbstractC0696D.f() : AbstractC0696D.d(r2.n.a("Cookie", AbstractC0717n.b(cookie)));
    }

    @Override // java.net.CookieHandler
    public void put(URI uri, Map map) {
        D2.h.f(uri, "uri");
        D2.h.f(map, "headers");
        String string = uri.toString();
        D2.h.e(string, "toString(...)");
        for (Map.Entry entry : map.entrySet()) {
            String str = (String) entry.getKey();
            List list = (List) entry.getValue();
            if (f7004b.b(str)) {
                c(string, list);
            }
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public d(ReactContext reactContext) {
        this();
        D2.h.f(reactContext, "reactContext");
    }
}

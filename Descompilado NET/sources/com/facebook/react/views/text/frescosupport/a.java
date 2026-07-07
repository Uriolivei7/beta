package com.facebook.react.views.text.frescosupport;

import Z1.p;
import android.content.Context;
import android.net.Uri;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import java.util.Locale;
import q.g;
import q0.AbstractC0646b;

/* JADX INFO: loaded from: classes.dex */
class a extends Y1.a {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private Uri f7950A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private ReadableMap f7951B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private final AbstractC0646b f7952C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private final Object f7953D;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private String f7955F;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private float f7954E = Float.NaN;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private float f7956G = Float.NaN;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private int f7957H = 0;

    public a(AbstractC0646b abstractC0646b, Object obj) {
        this.f7952C = abstractC0646b;
        this.f7953D = obj;
    }

    private static Uri A1(Context context, String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return new Uri.Builder().scheme("res").path(String.valueOf(context.getResources().getIdentifier(str.toLowerCase(Locale.getDefault()).replace("-", "_"), "drawable", context.getPackageName()))).build();
    }

    public Uri B1() {
        return this.f7950A;
    }

    @Override // com.facebook.react.uimanager.C0452r0, com.facebook.react.uimanager.InterfaceC0451q0
    public boolean R() {
        return true;
    }

    @L1.a(name = "headers")
    public void setHeaders(ReadableMap readableMap) {
        this.f7951B = readableMap;
    }

    @Override // com.facebook.react.uimanager.U
    public void setHeight(Dynamic dynamic) {
        if (dynamic.getType() == ReadableType.Number) {
            this.f7956G = (float) dynamic.asDouble();
        } else {
            Y.a.I("ReactNative", "Inline images must not have percentage based height");
            this.f7956G = Float.NaN;
        }
    }

    @L1.a(name = "resizeMode")
    public void setResizeMode(String str) {
        this.f7955F = str;
    }

    @L1.a(name = "src")
    public void setSource(ReadableArray readableArray) {
        Uri uriA1 = null;
        String string = (readableArray == null || readableArray.size() == 0 || readableArray.getType(0) != ReadableType.Map) ? null : ((ReadableMap) g.g(readableArray.getMap(0))).getString("uri");
        if (string != null) {
            try {
                Uri uri = Uri.parse(string);
                if (uri.getScheme() != null) {
                    uriA1 = uri;
                }
            } catch (Exception unused) {
            }
            if (uriA1 == null) {
                uriA1 = A1(l(), string);
            }
        }
        if (uriA1 != this.f7950A) {
            y0();
        }
        this.f7950A = uriA1;
    }

    @L1.a(customType = "Color", name = "tintColor")
    public void setTintColor(int i3) {
        this.f7957H = i3;
    }

    @Override // com.facebook.react.uimanager.U
    public void setWidth(Dynamic dynamic) {
        if (dynamic.getType() == ReadableType.Number) {
            this.f7954E = (float) dynamic.asDouble();
        } else {
            Y.a.I("ReactNative", "Inline images must not have percentage based width");
            this.f7954E = Float.NaN;
        }
    }

    @Override // Y1.a
    public p w1() {
        return new b(l().getResources(), (int) Math.ceil(this.f7956G), (int) Math.ceil(this.f7954E), this.f7957H, B1(), z1(), y1(), x1(), this.f7955F);
    }

    public Object x1() {
        return this.f7953D;
    }

    public AbstractC0646b y1() {
        return this.f7952C;
    }

    public ReadableMap z1() {
        return this.f7951B;
    }
}

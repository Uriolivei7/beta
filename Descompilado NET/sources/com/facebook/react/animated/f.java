package com.facebook.react.animated;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import com.facebook.react.bridge.ColorPropConverter;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class f extends b implements d {

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public static final a f6386n = new a(null);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final o f6387f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final ReactApplicationContext f6388g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f6389h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f6390i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f6391j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f6392k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private ReadableMap f6393l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private boolean f6394m;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Context b(b bVar) {
            List list = bVar.f6378a;
            if (list == null) {
                return null;
            }
            Iterator it = list.iterator();
            if (!it.hasNext()) {
                return null;
            }
            b bVar2 = (b) it.next();
            if (!(bVar2 instanceof q)) {
                return f.f6386n.b(bVar2);
            }
            View viewK = ((q) bVar2).k();
            if (viewK != null) {
                return viewK.getContext();
            }
            return null;
        }

        private a() {
        }
    }

    public f(ReadableMap readableMap, o oVar, ReactApplicationContext reactApplicationContext) {
        D2.h.f(readableMap, "config");
        D2.h.f(oVar, "nativeAnimatedNodesManager");
        D2.h.f(reactApplicationContext, "reactApplicationContext");
        this.f6387f = oVar;
        this.f6388g = reactApplicationContext;
        a(readableMap);
    }

    private final Context j() {
        Activity currentActivity = this.f6388g.getCurrentActivity();
        return currentActivity != null ? currentActivity : f6386n.b(this);
    }

    private final void k() {
        Context contextJ;
        if (this.f6393l == null || this.f6394m || (contextJ = j()) == null) {
            return;
        }
        Integer color = ColorPropConverter.getColor(this.f6393l, contextJ);
        w wVar = (w) this.f6387f.l(this.f6389h);
        w wVar2 = (w) this.f6387f.l(this.f6390i);
        w wVar3 = (w) this.f6387f.l(this.f6391j);
        w wVar4 = (w) this.f6387f.l(this.f6392k);
        if (wVar != null) {
            D2.h.c(color);
            wVar.f6495f = Color.red(color.intValue());
        }
        if (wVar2 != null) {
            D2.h.c(color);
            wVar2.f6495f = Color.green(color.intValue());
        }
        if (wVar3 != null) {
            D2.h.c(color);
            wVar3.f6495f = Color.blue(color.intValue());
        }
        if (wVar4 != null) {
            D2.h.c(color);
            wVar4.f6495f = ((double) Color.alpha(color.intValue())) / 255.0d;
        }
        this.f6394m = true;
    }

    @Override // com.facebook.react.animated.d
    public void a(ReadableMap readableMap) {
        if (readableMap == null) {
            this.f6389h = 0;
            this.f6390i = 0;
            this.f6391j = 0;
            this.f6392k = 0;
            this.f6393l = null;
            this.f6394m = false;
            return;
        }
        this.f6389h = readableMap.getInt("r");
        this.f6390i = readableMap.getInt("g");
        this.f6391j = readableMap.getInt("b");
        this.f6392k = readableMap.getInt("a");
        this.f6393l = readableMap.getMap("nativeColor");
        this.f6394m = false;
        k();
    }

    @Override // com.facebook.react.animated.b
    public String e() {
        return "ColorAnimatedNode[" + this.f6381d + "]: r: " + this.f6389h + "  g: " + this.f6390i + " b: " + this.f6391j + " a: " + this.f6392k;
    }

    public final int i() {
        k();
        w wVar = (w) this.f6387f.l(this.f6389h);
        w wVar2 = (w) this.f6387f.l(this.f6390i);
        w wVar3 = (w) this.f6387f.l(this.f6391j);
        w wVar4 = (w) this.f6387f.l(this.f6392k);
        return com.facebook.react.views.view.d.b(wVar != null ? wVar.f6495f : 0.0d, wVar2 != null ? wVar2.f6495f : 0.0d, wVar3 != null ? wVar3.f6495f : 0.0d, wVar4 != null ? wVar4.f6495f : 0.0d);
    }
}

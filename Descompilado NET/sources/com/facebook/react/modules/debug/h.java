package com.facebook.react.modules.debug;

import a1.C0210a;
import android.view.Choreographer;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.uimanager.UIManagerModule;
import java.util.TreeMap;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class h implements Choreographer.FrameCallback {

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private static final a f6942n = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ReactContext f6943a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Choreographer f6944b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final UIManagerModule f6945c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final d f6946d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private long f6947e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private long f6948f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private int f6949g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f6950h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f6951i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f6952j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f6953k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private double f6954l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private TreeMap f6955m;

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public static final class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final int f6956a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int f6957b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f6958c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final int f6959d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final double f6960e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final double f6961f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private final int f6962g;

        public b(int i3, int i4, int i5, int i6, double d4, double d5, int i7) {
            this.f6956a = i3;
            this.f6957b = i4;
            this.f6958c = i5;
            this.f6959d = i6;
            this.f6960e = d4;
            this.f6961f = d5;
            this.f6962g = i7;
        }
    }

    public h(ReactContext reactContext) {
        D2.h.f(reactContext, "reactContext");
        this.f6943a = reactContext;
        this.f6945c = (UIManagerModule) reactContext.getNativeModule(UIManagerModule.class);
        this.f6946d = new d();
        this.f6947e = -1L;
        this.f6948f = -1L;
        this.f6954l = 60.0d;
    }

    public static /* synthetic */ void l(h hVar, double d4, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            d4 = hVar.f6954l;
        }
        hVar.k(d4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void m(h hVar) {
        Choreographer choreographer = Choreographer.getInstance();
        hVar.f6944b = choreographer;
        if (choreographer != null) {
            choreographer.postFrameCallback(hVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void o(h hVar) {
        Choreographer choreographer = Choreographer.getInstance();
        hVar.f6944b = choreographer;
        if (choreographer != null) {
            choreographer.removeFrameCallback(hVar);
        }
    }

    public final int c() {
        return this.f6951i;
    }

    public final int d() {
        return (int) (((this.f6954l * ((double) i())) / ((double) 1000)) + ((double) 1));
    }

    @Override // android.view.Choreographer.FrameCallback
    public void doFrame(long j3) {
        if (this.f6947e == -1) {
            this.f6947e = j3;
        }
        long j4 = this.f6948f;
        this.f6948f = j3;
        if (this.f6946d.d(j4, j3)) {
            this.f6952j++;
        }
        this.f6949g++;
        int iD = d();
        if ((iD - this.f6950h) - 1 >= 4) {
            this.f6951i++;
        }
        if (this.f6953k) {
            C0210a.c(this.f6955m);
            b bVar = new b(g(), h(), iD, this.f6951i, e(), f(), i());
            TreeMap treeMap = this.f6955m;
            if (treeMap != null) {
            }
        }
        this.f6950h = iD;
        Choreographer choreographer = this.f6944b;
        if (choreographer != null) {
            choreographer.postFrameCallback(this);
        }
    }

    public final double e() {
        if (this.f6948f == this.f6947e) {
            return 0.0d;
        }
        return (((double) g()) * 1.0E9d) / (this.f6948f - this.f6947e);
    }

    public final double f() {
        if (this.f6948f == this.f6947e) {
            return 0.0d;
        }
        return (((double) h()) * 1.0E9d) / (this.f6948f - this.f6947e);
    }

    public final int g() {
        return this.f6949g - 1;
    }

    public final int h() {
        return this.f6952j - 1;
    }

    public final int i() {
        return (int) ((this.f6948f - this.f6947e) / 1000000.0d);
    }

    public final void j() {
        this.f6947e = -1L;
        this.f6948f = -1L;
        this.f6949g = 0;
        this.f6951i = 0;
        this.f6952j = 0;
        this.f6953k = false;
        this.f6955m = null;
    }

    public final void k(double d4) {
        if (!this.f6943a.isBridgeless()) {
            this.f6943a.getCatalystInstance().addBridgeIdleDebugListener(this.f6946d);
        }
        UIManagerModule uIManagerModule = this.f6945c;
        if (uIManagerModule != null) {
            uIManagerModule.setViewHierarchyUpdateDebugListener(this.f6946d);
        }
        this.f6954l = d4;
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.modules.debug.f
            @Override // java.lang.Runnable
            public final void run() {
                h.m(this.f6940b);
            }
        });
    }

    public final void n() {
        if (!this.f6943a.isBridgeless()) {
            this.f6943a.getCatalystInstance().removeBridgeIdleDebugListener(this.f6946d);
        }
        UIManagerModule uIManagerModule = this.f6945c;
        if (uIManagerModule != null) {
            uIManagerModule.setViewHierarchyUpdateDebugListener(null);
        }
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.modules.debug.g
            @Override // java.lang.Runnable
            public final void run() {
                h.o(this.f6941b);
            }
        });
    }
}

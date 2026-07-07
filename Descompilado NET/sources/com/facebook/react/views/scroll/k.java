package com.facebook.react.views.scroll;

import a1.C0210a;
import android.os.SystemClock;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.views.scroll.l;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class k extends P1.d {

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public static final a f7874r = new a(null);

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private static final String f7875s = k.class.getSimpleName();

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private static final q.f f7876t = new q.f(3);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private float f7877h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private float f7878i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private float f7879j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private float f7880k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private int f7881l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private int f7882m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private int f7883n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private int f7884o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private l f7885p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private long f7886q;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final k a(int i3, int i4, l lVar, float f3, float f4, float f5, float f6, int i5, int i6, int i7, int i8) {
            k kVar = (k) k.f7876t.b();
            if (kVar == null) {
                kVar = new k(null);
            }
            kVar.w(i3, i4, lVar, f3, f4, f5, f6, i5, i6, i7, i8);
            return kVar;
        }

        public final k b(int i3, l lVar, float f3, float f4, float f5, float f6, int i4, int i5, int i6, int i7) {
            return a(-1, i3, lVar, f3, f4, f5, f6, i4, i5, i6, i7);
        }

        private a() {
        }
    }

    public /* synthetic */ k(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void w(int i3, int i4, l lVar, float f3, float f4, float f5, float f6, int i5, int i6, int i7, int i8) {
        super.q(i3, i4);
        this.f7885p = lVar;
        this.f7877h = f3;
        this.f7878i = f4;
        this.f7879j = f5;
        this.f7880k = f6;
        this.f7881l = i5;
        this.f7882m = i6;
        this.f7883n = i7;
        this.f7884o = i8;
        this.f7886q = SystemClock.uptimeMillis();
    }

    public static final k x(int i3, int i4, l lVar, float f3, float f4, float f5, float f6, int i5, int i6, int i7, int i8) {
        return f7874r.a(i3, i4, lVar, f3, f4, f5, f6, i5, i6, i7, i8);
    }

    public static final k y(int i3, l lVar, float f3, float f4, float f5, float f6, int i4, int i5, int i6, int i7) {
        return f7874r.b(i3, lVar, f3, f4, f5, f6, i4, i5, i6, i7);
    }

    @Override // P1.d
    public boolean a() {
        return this.f7885p == l.f7890e;
    }

    @Override // P1.d
    protected WritableMap j() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putDouble("top", 0.0d);
        writableMapCreateMap.putDouble("bottom", 0.0d);
        writableMapCreateMap.putDouble("left", 0.0d);
        writableMapCreateMap.putDouble("right", 0.0d);
        WritableMap writableMapCreateMap2 = Arguments.createMap();
        writableMapCreateMap2.putDouble("x", C0429f0.f(this.f7877h));
        writableMapCreateMap2.putDouble("y", C0429f0.f(this.f7878i));
        WritableMap writableMapCreateMap3 = Arguments.createMap();
        writableMapCreateMap3.putDouble("width", C0429f0.f(this.f7881l));
        writableMapCreateMap3.putDouble("height", C0429f0.f(this.f7882m));
        WritableMap writableMapCreateMap4 = Arguments.createMap();
        writableMapCreateMap4.putDouble("width", C0429f0.f(this.f7883n));
        writableMapCreateMap4.putDouble("height", C0429f0.f(this.f7884o));
        WritableMap writableMapCreateMap5 = Arguments.createMap();
        writableMapCreateMap5.putDouble("x", this.f7879j);
        writableMapCreateMap5.putDouble("y", this.f7880k);
        WritableMap writableMapCreateMap6 = Arguments.createMap();
        writableMapCreateMap6.putMap("contentInset", writableMapCreateMap);
        writableMapCreateMap6.putMap("contentOffset", writableMapCreateMap2);
        writableMapCreateMap6.putMap("contentSize", writableMapCreateMap3);
        writableMapCreateMap6.putMap("layoutMeasurement", writableMapCreateMap4);
        writableMapCreateMap6.putMap("velocity", writableMapCreateMap5);
        writableMapCreateMap6.putInt("target", o());
        writableMapCreateMap6.putDouble("timestamp", this.f7886q);
        writableMapCreateMap6.putBoolean("responderIgnoreScroll", true);
        D2.h.c(writableMapCreateMap6);
        return writableMapCreateMap6;
    }

    @Override // P1.d
    public String k() {
        l.a aVar = l.f7887b;
        Object objC = C0210a.c(this.f7885p);
        D2.h.e(objC, "assertNotNull(...)");
        return aVar.a((l) objC);
    }

    @Override // P1.d
    public void t() {
        try {
            f7876t.a(this);
        } catch (IllegalStateException e4) {
            String str = f7875s;
            D2.h.e(str, "TAG");
            ReactSoftExceptionLogger.logSoftException(str, e4);
        }
    }

    private k() {
    }
}

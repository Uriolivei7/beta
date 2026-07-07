package com.facebook.react.views.image;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.views.progressbar.ReactProgressBarViewManager;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class b extends P1.d {

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public static final a f7663o = new a(null);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final int f7664h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final String f7665i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final String f7666j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final int f7667k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final int f7668l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final int f7669m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final int f7670n;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final b a(int i3, int i4, Throwable th) {
            D2.h.f(th, "throwable");
            return new b(i3, i4, 1, th.getMessage(), null, 0, 0, 0, 0, null);
        }

        public final b b(int i3, int i4) {
            return new b(i3, i4, 3, null, null, 0, 0, 0, 0, 504, null);
        }

        public final b c(int i3, int i4, String str, int i5, int i6) {
            return new b(i3, i4, 2, null, str, i5, i6, 0, 0, null);
        }

        public final b d(int i3, int i4) {
            return new b(i3, i4, 4, null, null, 0, 0, 0, 0, 504, null);
        }

        public final b e(int i3, int i4, String str, int i5, int i6) {
            return new b(i3, i4, 5, null, str, 0, 0, i5, i6, null);
        }

        public final String f(int i3) {
            if (i3 == 1) {
                return "topError";
            }
            if (i3 == 2) {
                return "topLoad";
            }
            if (i3 == 3) {
                return "topLoadEnd";
            }
            if (i3 == 4) {
                return "topLoadStart";
            }
            if (i3 == 5) {
                return "topProgress";
            }
            throw new IllegalStateException(("Invalid image event: " + i3).toString());
        }

        private a() {
        }
    }

    public /* synthetic */ b(int i3, int i4, int i5, String str, String str2, int i6, int i7, int i8, int i9, DefaultConstructorMarker defaultConstructorMarker) {
        this(i3, i4, i5, str, str2, i6, i7, i8, i9);
    }

    private final WritableMap u() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putString("uri", this.f7666j);
        writableMapCreateMap.putDouble("width", this.f7667k);
        writableMapCreateMap.putDouble("height", this.f7668l);
        D2.h.e(writableMapCreateMap, "apply(...)");
        return writableMapCreateMap;
    }

    @Override // P1.d
    public short g() {
        return (short) this.f7664h;
    }

    @Override // P1.d
    protected WritableMap j() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        int i3 = this.f7664h;
        if (i3 == 1) {
            writableMapCreateMap.putString("error", this.f7665i);
        } else if (i3 == 2) {
            writableMapCreateMap.putMap("source", u());
        } else if (i3 == 5) {
            writableMapCreateMap.putInt("loaded", this.f7669m);
            writableMapCreateMap.putInt("total", this.f7670n);
            writableMapCreateMap.putDouble(ReactProgressBarViewManager.PROP_PROGRESS, ((double) this.f7669m) / ((double) this.f7670n));
        }
        return writableMapCreateMap;
    }

    @Override // P1.d
    public String k() {
        return f7663o.f(this.f7664h);
    }

    /* synthetic */ b(int i3, int i4, int i5, String str, String str2, int i6, int i7, int i8, int i9, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(i3, i4, i5, (i10 & 8) != 0 ? null : str, (i10 & 16) != 0 ? null : str2, (i10 & 32) != 0 ? 0 : i6, (i10 & 64) != 0 ? 0 : i7, (i10 & 128) != 0 ? 0 : i8, (i10 & 256) != 0 ? 0 : i9);
    }

    private b(int i3, int i4, int i5, String str, String str2, int i6, int i7, int i8, int i9) {
        super(i3, i4);
        this.f7664h = i5;
        this.f7665i = str;
        this.f7666j = str2;
        this.f7667k = i6;
        this.f7668l = i7;
        this.f7669m = i8;
        this.f7670n = i9;
    }
}

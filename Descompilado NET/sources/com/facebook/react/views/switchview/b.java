package com.facebook.react.views.switchview;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class b extends P1.d {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final a f7912i = new a(null);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final boolean f7913h;

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public b(int i3, int i4, boolean z3) {
        super(i3, i4);
        this.f7913h = z3;
    }

    @Override // P1.d
    public WritableMap j() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putInt("target", o());
        writableMapCreateMap.putBoolean("value", this.f7913h);
        return writableMapCreateMap;
    }

    @Override // P1.d
    public String k() {
        return "topChange";
    }

    public b(int i3, boolean z3) {
        this(-1, i3, z3);
    }
}

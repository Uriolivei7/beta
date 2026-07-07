package com.facebook.react.views.modal;

import D2.h;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class d extends P1.d {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final a f7735h = new a(null);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public d(int i3, int i4) {
        super(i3, i4);
    }

    @Override // P1.d
    protected WritableMap j() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        h.e(writableMapCreateMap, "createMap(...)");
        return writableMapCreateMap;
    }

    @Override // P1.d
    public String k() {
        return "topRequestClose";
    }

    public d(int i3) {
        this(-1, i3);
    }
}

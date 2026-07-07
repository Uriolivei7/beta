package com.facebook.react.views.view;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class j extends P1.d {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final a f8190h = new a(null);

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public j(int i3, int i4) {
        super(i3, i4);
    }

    @Override // P1.d
    public boolean a() {
        return false;
    }

    @Override // P1.d
    protected WritableMap j() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        D2.h.e(writableMapCreateMap, "createMap(...)");
        return writableMapCreateMap;
    }

    @Override // P1.d
    public String k() {
        return "topClick";
    }

    public j(int i3) {
        this(-1, i3);
    }
}

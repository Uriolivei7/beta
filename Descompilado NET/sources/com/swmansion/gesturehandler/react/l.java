package com.swmansion.gesturehandler.react;

import android.view.View;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.H0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import n2.C0625d;
import o2.AbstractC0631b;

/* JADX INFO: loaded from: classes.dex */
public final class l extends P1.d {

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public static final a f8643k = new a(null);

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static final q.f f8644l = new q.f(7);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private AbstractC0631b f8645h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f8646i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f8647j;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final WritableMap a(AbstractC0631b abstractC0631b, int i3, int i4) {
            D2.h.f(abstractC0631b, "dataBuilder");
            WritableMap writableMapCreateMap = Arguments.createMap();
            D2.h.c(writableMapCreateMap);
            abstractC0631b.a(writableMapCreateMap);
            writableMapCreateMap.putInt("state", i3);
            writableMapCreateMap.putInt("oldState", i4);
            D2.h.e(writableMapCreateMap, "apply(...)");
            return writableMapCreateMap;
        }

        public final l b(C0625d c0625d, int i3, int i4, AbstractC0631b abstractC0631b) {
            D2.h.f(c0625d, "handler");
            D2.h.f(abstractC0631b, "dataBuilder");
            l lVar = (l) l.f8644l.b();
            if (lVar == null) {
                lVar = new l(null);
            }
            lVar.w(c0625d, i3, i4, abstractC0631b);
            return lVar;
        }

        private a() {
        }
    }

    public /* synthetic */ l(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void w(C0625d c0625d, int i3, int i4, AbstractC0631b abstractC0631b) {
        View viewU = c0625d.U();
        D2.h.c(viewU);
        super.q(H0.f(viewU), viewU.getId());
        this.f8645h = abstractC0631b;
        this.f8646i = i3;
        this.f8647j = i4;
    }

    @Override // P1.d
    public boolean a() {
        return false;
    }

    @Override // P1.d
    public short g() {
        return (short) 0;
    }

    @Override // P1.d
    protected WritableMap j() {
        a aVar = f8643k;
        AbstractC0631b abstractC0631b = this.f8645h;
        D2.h.c(abstractC0631b);
        return aVar.a(abstractC0631b, this.f8646i, this.f8647j);
    }

    @Override // P1.d
    public String k() {
        return "onGestureHandlerStateChange";
    }

    @Override // P1.d
    public void t() {
        this.f8645h = null;
        this.f8646i = 0;
        this.f8647j = 0;
        f8644l.a(this);
    }

    private l() {
    }
}

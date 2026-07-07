package com.swmansion.gesturehandler.react;

import android.view.View;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.H0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import n2.C0625d;
import o2.AbstractC0631b;

/* JADX INFO: loaded from: classes.dex */
public final class d extends P1.d {

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public static final a f8617k = new a(null);

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static final q.f f8618l = new q.f(7);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private AbstractC0631b f8619h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private short f8620i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f8621j;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ d c(a aVar, C0625d c0625d, AbstractC0631b abstractC0631b, boolean z3, int i3, Object obj) {
            if ((i3 & 4) != 0) {
                z3 = false;
            }
            return aVar.b(c0625d, abstractC0631b, z3);
        }

        public final WritableMap a(AbstractC0631b abstractC0631b) {
            D2.h.f(abstractC0631b, "dataBuilder");
            WritableMap writableMapCreateMap = Arguments.createMap();
            D2.h.c(writableMapCreateMap);
            abstractC0631b.a(writableMapCreateMap);
            D2.h.e(writableMapCreateMap, "apply(...)");
            return writableMapCreateMap;
        }

        public final d b(C0625d c0625d, AbstractC0631b abstractC0631b, boolean z3) {
            D2.h.f(c0625d, "handler");
            D2.h.f(abstractC0631b, "dataBuilder");
            d dVar = (d) d.f8618l.b();
            if (dVar == null) {
                dVar = new d(null);
            }
            dVar.w(c0625d, abstractC0631b, z3);
            return dVar;
        }

        private a() {
        }
    }

    public /* synthetic */ d(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void w(C0625d c0625d, AbstractC0631b abstractC0631b, boolean z3) {
        View viewU = c0625d.U();
        D2.h.c(viewU);
        super.q(H0.f(viewU), viewU.getId());
        this.f8619h = abstractC0631b;
        this.f8621j = z3;
        this.f8620i = c0625d.G();
    }

    @Override // P1.d
    public boolean a() {
        return true;
    }

    @Override // P1.d
    public short g() {
        return this.f8620i;
    }

    @Override // P1.d
    protected WritableMap j() {
        a aVar = f8617k;
        AbstractC0631b abstractC0631b = this.f8619h;
        D2.h.c(abstractC0631b);
        return aVar.a(abstractC0631b);
    }

    @Override // P1.d
    public String k() {
        return this.f8621j ? "topGestureHandlerEvent" : "onGestureHandlerEvent";
    }

    @Override // P1.d
    public void t() {
        this.f8619h = null;
        f8618l.a(this);
    }

    private d() {
    }
}

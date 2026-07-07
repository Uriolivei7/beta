package com.swmansion.gesturehandler.react;

import android.view.View;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.H0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import n2.C0625d;

/* JADX INFO: loaded from: classes.dex */
public final class m extends P1.d {

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final a f8648j = new a(null);

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private static final q.f f8649k = new q.f(7);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private WritableMap f8650h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private short f8651i;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final WritableMap a(C0625d c0625d) {
            D2.h.f(c0625d, "handler");
            WritableMap writableMapCreateMap = Arguments.createMap();
            writableMapCreateMap.putInt("handlerTag", c0625d.R());
            writableMapCreateMap.putInt("state", c0625d.Q());
            writableMapCreateMap.putInt("numberOfTouches", c0625d.T());
            writableMapCreateMap.putInt("eventType", c0625d.S());
            writableMapCreateMap.putInt("pointerType", c0625d.O());
            WritableArray writableArrayR = c0625d.r();
            if (writableArrayR != null) {
                writableMapCreateMap.putArray("changedTouches", writableArrayR);
            }
            WritableArray writableArrayQ = c0625d.q();
            if (writableArrayQ != null) {
                writableMapCreateMap.putArray("allTouches", writableArrayQ);
            }
            if (c0625d.Y() && c0625d.Q() == 4) {
                writableMapCreateMap.putInt("state", 2);
            }
            D2.h.e(writableMapCreateMap, "apply(...)");
            return writableMapCreateMap;
        }

        public final m b(C0625d c0625d) {
            D2.h.f(c0625d, "handler");
            m mVar = (m) m.f8649k.b();
            if (mVar == null) {
                mVar = new m(null);
            }
            mVar.w(c0625d);
            return mVar;
        }

        private a() {
        }
    }

    public /* synthetic */ m(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void w(C0625d c0625d) {
        View viewU = c0625d.U();
        D2.h.c(viewU);
        super.q(H0.f(viewU), viewU.getId());
        this.f8650h = f8648j.a(c0625d);
        this.f8651i = c0625d.G();
    }

    @Override // P1.d
    public boolean a() {
        return true;
    }

    @Override // P1.d
    public short g() {
        return this.f8651i;
    }

    @Override // P1.d
    protected WritableMap j() {
        return this.f8650h;
    }

    @Override // P1.d
    public String k() {
        return "onGestureHandlerEvent";
    }

    @Override // P1.d
    public void t() {
        this.f8650h = null;
        f8649k.a(this);
    }

    private m() {
    }
}

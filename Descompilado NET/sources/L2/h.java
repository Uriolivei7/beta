package l2;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class h extends P1.d {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final a f9714i = new a(null);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final WritableMap f9715h;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public h(int i3, WritableMap writableMap) {
        super(i3);
        D2.h.f(writableMap, "mEventData");
        this.f9715h = writableMap;
    }

    @Override // P1.d
    public boolean a() {
        return false;
    }

    @Override // P1.d
    public void c(RCTEventEmitter rCTEventEmitter) {
        D2.h.f(rCTEventEmitter, "rctEventEmitter");
        rCTEventEmitter.receiveEvent(o(), k(), this.f9715h);
    }

    @Override // P1.d
    public short g() {
        return (short) 0;
    }

    @Override // P1.d
    public String k() {
        return "topOpenWindow";
    }
}

package l2;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: l2.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0592a extends P1.d {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final C0136a f9700i = new C0136a(null);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final WritableMap f9701h;

    /* JADX INFO: renamed from: l2.a$a, reason: collision with other inner class name */
    public static final class C0136a {
        public /* synthetic */ C0136a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private C0136a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0592a(int i3, WritableMap writableMap) {
        super(i3);
        D2.h.f(writableMap, "mEventData");
        this.f9701h = writableMap;
    }

    @Override // P1.d
    public boolean a() {
        return false;
    }

    @Override // P1.d
    public void c(RCTEventEmitter rCTEventEmitter) {
        D2.h.f(rCTEventEmitter, "rctEventEmitter");
        rCTEventEmitter.receiveEvent(o(), k(), this.f9701h);
    }

    @Override // P1.d
    public short g() {
        return (short) 0;
    }

    @Override // P1.d
    public String k() {
        return "topCustomMenuSelection";
    }
}

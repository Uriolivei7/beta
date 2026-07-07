package P1;

import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.uimanager.events.RCTModernEventEmitter;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class b implements EventDispatcher {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f1600b = new a(null);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final EventDispatcher f1601c = new b();

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final EventDispatcher a() {
            return b.f1601c;
        }

        private a() {
        }
    }

    private b() {
    }

    public static final EventDispatcher k() {
        return f1600b.a();
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void a(int i3, RCTEventEmitter rCTEventEmitter) {
        D2.h.f(rCTEventEmitter, "eventEmitter");
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void b(d dVar) {
        D2.h.f(dVar, "event");
        Y.a.b("BlackHoleEventDispatcher", "Trying to emit event to JS, but the React instance isn't ready. Event: " + dVar.k());
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void d(P1.a aVar) {
        D2.h.f(aVar, "listener");
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void e(P1.a aVar) {
        D2.h.f(aVar, "listener");
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void f(int i3, RCTModernEventEmitter rCTModernEventEmitter) {
        D2.h.f(rCTModernEventEmitter, "eventEmitter");
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void i(g gVar) {
        D2.h.f(gVar, "listener");
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void c() {
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void h() {
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void g(int i3) {
    }
}

package P1;

import P1.k;
import android.os.Handler;
import android.view.Choreographer;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.modules.core.b;
import com.facebook.react.uimanager.H0;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.uimanager.events.RCTModernEventEmitter;
import com.facebook.react.uimanager.events.ReactEventEmitter;
import d2.C0518a;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r1.C0670b;

/* JADX INFO: loaded from: classes.dex */
public class k implements EventDispatcher, LifecycleEventListener {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final a f1637i = new a(null);

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static final Handler f1638j;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ReactEventEmitter f1639b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final ReactApplicationContext f1640c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final CopyOnWriteArrayList f1641d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final CopyOnWriteArrayList f1642e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final b f1643f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f1644g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final Runnable f1645h;

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class b implements Choreographer.FrameCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private volatile boolean f1646a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private boolean f1647b;

        public b() {
        }

        private final void b() {
            com.facebook.react.modules.core.b.f6916f.a().k(b.a.f6926f, k.this.f1643f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void e(b bVar) {
            bVar.c();
        }

        public final void c() {
            if (this.f1646a) {
                return;
            }
            this.f1646a = true;
            b();
        }

        public final void d() {
            if (this.f1646a) {
                return;
            }
            if (k.this.f1640c.isOnUiQueueThread()) {
                c();
            } else {
                k.this.f1640c.runOnUiQueueThread(new Runnable() { // from class: P1.l
                    @Override // java.lang.Runnable
                    public final void run() {
                        k.b.e(this.f1649b);
                    }
                });
            }
        }

        @Override // android.view.Choreographer.FrameCallback
        public void doFrame(long j3) {
            UiThreadUtil.assertOnUiThread();
            if (this.f1647b) {
                this.f1646a = false;
            } else {
                b();
            }
            C0518a.c(0L, "BatchEventDispatchedListeners");
            try {
                Iterator it = k.this.f1642e.iterator();
                D2.h.e(it, "iterator(...)");
                while (it.hasNext()) {
                    ((P1.a) it.next()).a();
                }
            } finally {
                C0518a.i(0L);
            }
        }

        public final void f() {
            this.f1647b = false;
        }

        public final void g() {
            this.f1647b = true;
        }
    }

    static {
        Handler uiThreadHandler = UiThreadUtil.getUiThreadHandler();
        D2.h.e(uiThreadHandler, "getUiThreadHandler(...)");
        f1638j = uiThreadHandler;
    }

    public k(ReactApplicationContext reactApplicationContext) {
        D2.h.f(reactApplicationContext, "reactContext");
        this.f1640c = reactApplicationContext;
        this.f1641d = new CopyOnWriteArrayList();
        this.f1642e = new CopyOnWriteArrayList();
        this.f1643f = new b();
        this.f1645h = new Runnable() { // from class: P1.j
            @Override // java.lang.Runnable
            public final void run() {
                k.p(this.f1636b);
            }
        };
        reactApplicationContext.addLifecycleEventListener(this);
        this.f1639b = new ReactEventEmitter(reactApplicationContext);
    }

    private final void o() {
        UiThreadUtil.assertOnUiThread();
        if (!C0670b.r()) {
            this.f1643f.g();
        } else {
            this.f1644g = false;
            f1638j.removeCallbacks(this.f1645h);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void p(k kVar) {
        kVar.f1644g = false;
        C0518a.c(0L, "BatchEventDispatchedListeners");
        try {
            Iterator it = kVar.f1642e.iterator();
            D2.h.e(it, "iterator(...)");
            while (it.hasNext()) {
                ((P1.a) it.next()).a();
            }
        } finally {
            C0518a.i(0L);
        }
    }

    private final void q(d dVar) {
        C0518a.c(0L, "FabricEventDispatcher.dispatchSynchronous('" + dVar.k() + "')");
        try {
            UIManager uIManagerG = H0.g(this.f1640c, 2);
            if (uIManagerG instanceof p) {
                int iL = dVar.l();
                int iO = dVar.o();
                String strK = dVar.k();
                D2.h.e(strK, "getEventName(...)");
                ((p) uIManagerG).receiveEvent(iL, iO, strK, dVar.a(), dVar.j(), dVar.i(), true);
            } else {
                ReactSoftExceptionLogger.logSoftException("FabricEventDispatcher", new IllegalStateException("Fabric UIManager expected to implement SynchronousEventReceiver."));
            }
            C0518a.i(0L);
        } catch (Throwable th) {
            C0518a.i(0L);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void r(k kVar) {
        kVar.o();
    }

    private final void s() {
        if (!C0670b.r()) {
            this.f1643f.d();
        } else {
            if (this.f1644g) {
                return;
            }
            this.f1644g = true;
            f1638j.postAtFrontOfQueue(this.f1645h);
        }
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void a(int i3, RCTEventEmitter rCTEventEmitter) {
        D2.h.f(rCTEventEmitter, "eventEmitter");
        this.f1639b.register(i3, rCTEventEmitter);
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void b(d dVar) {
        D2.h.f(dVar, "event");
        Iterator it = this.f1641d.iterator();
        D2.h.e(it, "iterator(...)");
        while (it.hasNext()) {
            ((g) it.next()).a(dVar);
        }
        if (dVar.f()) {
            q(dVar);
        } else {
            dVar.d(this.f1639b);
        }
        dVar.e();
        s();
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void c() {
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: P1.i
            @Override // java.lang.Runnable
            public final void run() {
                k.r(this.f1635b);
            }
        });
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void d(P1.a aVar) {
        D2.h.f(aVar, "listener");
        this.f1642e.add(aVar);
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void e(P1.a aVar) {
        D2.h.f(aVar, "listener");
        this.f1642e.remove(aVar);
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void f(int i3, RCTModernEventEmitter rCTModernEventEmitter) {
        D2.h.f(rCTModernEventEmitter, "eventEmitter");
        this.f1639b.register(i3, rCTModernEventEmitter);
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void g(int i3) {
        this.f1639b.unregister(i3);
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void h() {
        s();
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void i(g gVar) {
        D2.h.f(gVar, "listener");
        this.f1641d.add(gVar);
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostDestroy() {
        o();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostPause() {
        o();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostResume() {
        s();
        if (C0670b.r()) {
            return;
        }
        this.f1643f.f();
    }
}

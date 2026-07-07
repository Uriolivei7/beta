package u1;

import a1.C0210a;
import android.util.SparseArray;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.appregistry.AppRegistry;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: u1.e, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0742e {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final a f10859g = new a(null);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final WeakHashMap f10860h = new WeakHashMap();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final WeakReference f10861a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Set f10862b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final AtomicInteger f10863c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Set f10864d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Map f10865e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final SparseArray f10866f;

    /* JADX INFO: renamed from: u1.e$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final C0742e a(ReactContext reactContext) {
            D2.h.f(reactContext, "context");
            WeakHashMap weakHashMap = C0742e.f10860h;
            Object c0742e = weakHashMap.get(reactContext);
            if (c0742e == null) {
                c0742e = new C0742e(reactContext, null);
                weakHashMap.put(reactContext, c0742e);
            }
            return (C0742e) c0742e;
        }

        private a() {
        }
    }

    public /* synthetic */ C0742e(ReactContext reactContext, DefaultConstructorMarker defaultConstructorMarker) {
        this(reactContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void g(C0742e c0742e, int i3) {
        Iterator it = c0742e.f10862b.iterator();
        while (it.hasNext()) {
            ((InterfaceC0743f) it.next()).b(i3);
        }
    }

    private final void k(int i3) {
        Runnable runnable = (Runnable) this.f10866f.get(i3);
        if (runnable != null) {
            UiThreadUtil.removeOnUiThread(runnable);
            this.f10866f.remove(i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void m(C0742e c0742e, C0738a c0738a, int i3) {
        c0742e.p(c0738a, i3);
    }

    private final void n(final int i3, long j3) {
        Runnable runnable = new Runnable() { // from class: u1.d
            @Override // java.lang.Runnable
            public final void run() {
                C0742e.o(this.f10857b, i3);
            }
        };
        this.f10866f.append(i3, runnable);
        UiThreadUtil.runOnUiThread(runnable, j3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void o(C0742e c0742e, int i3) {
        c0742e.f(i3);
    }

    private final synchronized void p(C0738a c0738a, int i3) {
        try {
            UiThreadUtil.assertOnUiThread();
            ReactContext reactContext = (ReactContext) C0210a.d(this.f10861a.get(), "Tried to start a task on a react context that has already been destroyed");
            if (reactContext.getLifecycleState() == LifecycleState.f6518d && !c0738a.e()) {
                throw new IllegalStateException(("Tried to start task " + c0738a.c() + " while in foreground, but this is not allowed.").toString());
            }
            this.f10864d.add(Integer.valueOf(i3));
            this.f10865e.put(Integer.valueOf(i3), new C0738a(c0738a));
            if (reactContext.hasActiveReactInstance()) {
                ((AppRegistry) reactContext.getJSModule(AppRegistry.class)).startHeadlessTask(i3, c0738a.c(), c0738a.a());
            } else {
                ReactSoftExceptionLogger.logSoftException("HeadlessJsTaskContext", new RuntimeException("Cannot start headless task, CatalystInstance not available"));
            }
            if (c0738a.d() > 0) {
                n(i3, c0738a.d());
            }
            Iterator it = this.f10862b.iterator();
            while (it.hasNext()) {
                ((InterfaceC0743f) it.next()).a(i3);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public final synchronized void e(InterfaceC0743f interfaceC0743f) {
        D2.h.f(interfaceC0743f, "listener");
        this.f10862b.add(interfaceC0743f);
        Iterator it = this.f10864d.iterator();
        while (it.hasNext()) {
            interfaceC0743f.a(((Number) it.next()).intValue());
        }
    }

    public final synchronized void f(final int i3) {
        boolean zRemove = this.f10864d.remove(Integer.valueOf(i3));
        this.f10865e.remove(Integer.valueOf(i3));
        k(i3);
        if (zRemove) {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: u1.c
                @Override // java.lang.Runnable
                public final void run() {
                    C0742e.g(this.f10855b, i3);
                }
            });
        }
    }

    public final boolean h() {
        return !this.f10864d.isEmpty();
    }

    public final synchronized boolean i(int i3) {
        return this.f10864d.contains(Integer.valueOf(i3));
    }

    public final void j(InterfaceC0743f interfaceC0743f) {
        D2.h.f(interfaceC0743f, "listener");
        this.f10862b.remove(interfaceC0743f);
    }

    public final synchronized boolean l(final int i3) {
        C0738a c0738a = (C0738a) this.f10865e.get(Integer.valueOf(i3));
        if (c0738a == null) {
            throw new IllegalStateException(("Tried to retrieve non-existent task config with id " + i3 + ".").toString());
        }
        InterfaceC0744g interfaceC0744gB = c0738a.b();
        if (interfaceC0744gB != null && interfaceC0744gB.a()) {
            k(i3);
            final C0738a c0738a2 = new C0738a(c0738a.c(), c0738a.a(), c0738a.d(), c0738a.e(), interfaceC0744gB.c());
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: u1.b
                @Override // java.lang.Runnable
                public final void run() {
                    C0742e.m(this.f10852b, c0738a2, i3);
                }
            }, interfaceC0744gB.b());
            return true;
        }
        return false;
    }

    private C0742e(ReactContext reactContext) {
        this.f10861a = new WeakReference(reactContext);
        this.f10862b = new CopyOnWriteArraySet();
        this.f10863c = new AtomicInteger(0);
        this.f10864d = new CopyOnWriteArraySet();
        this.f10865e = new ConcurrentHashMap();
        this.f10866f = new SparseArray();
    }
}

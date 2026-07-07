package com.facebook.react.modules.core;

import C2.p;
import D2.h;
import android.util.SparseArray;
import android.view.Choreographer;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.modules.core.JavaTimerManager;
import com.facebook.react.modules.core.b;
import e1.l;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.r;
import u1.C0742e;
import u1.InterfaceC0743f;

/* JADX INFO: loaded from: classes.dex */
public class JavaTimerManager implements LifecycleEventListener, InterfaceC0743f {

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private static final a f6889r = new a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ReactApplicationContext f6890b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final B1.c f6891c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final com.facebook.react.modules.core.b f6892d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final k1.e f6893e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Object f6894f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final Object f6895g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final SparseArray f6896h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final AtomicBoolean f6897i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final AtomicBoolean f6898j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final e f6899k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final c f6900l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private b f6901m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private boolean f6902n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private boolean f6903o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private boolean f6904p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private final PriorityQueue f6905q;

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean b(d dVar, long j3) {
            return !dVar.b() && ((long) dVar.a()) < j3;
        }

        private a() {
        }
    }

    private final class b implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final long f6906b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private volatile boolean f6907c;

        public b(long j3) {
            this.f6906b = j3;
        }

        public final void a() {
            this.f6907c = true;
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean z3;
            if (this.f6907c) {
                return;
            }
            long jC = l.c() - (this.f6906b / ((long) 1000000));
            long jA = l.a() - jC;
            if (16.666666f - jC < 1.0f) {
                return;
            }
            Object obj = JavaTimerManager.this.f6895g;
            JavaTimerManager javaTimerManager = JavaTimerManager.this;
            synchronized (obj) {
                z3 = javaTimerManager.f6904p;
                r rVar = r.f10584a;
            }
            if (z3) {
                JavaTimerManager.this.f6891c.callIdleCallbacks(jA);
            }
            JavaTimerManager.this.f6901m = null;
        }
    }

    private final class c implements Choreographer.FrameCallback {
        public c() {
        }

        @Override // android.view.Choreographer.FrameCallback
        public void doFrame(long j3) {
            if (!JavaTimerManager.this.f6897i.get() || JavaTimerManager.this.f6898j.get()) {
                b bVar = JavaTimerManager.this.f6901m;
                if (bVar != null) {
                    bVar.a();
                }
                JavaTimerManager javaTimerManager = JavaTimerManager.this;
                javaTimerManager.f6901m = javaTimerManager.new b(j3);
                JavaTimerManager.this.f6890b.runOnJSQueueThread(JavaTimerManager.this.f6901m);
                JavaTimerManager.this.f6892d.k(b.a.f6927g, this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final int f6910a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private long f6911b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f6912c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final boolean f6913d;

        public d(int i3, long j3, int i4, boolean z3) {
            this.f6910a = i3;
            this.f6911b = j3;
            this.f6912c = i4;
            this.f6913d = z3;
        }

        public final int a() {
            return this.f6912c;
        }

        public final boolean b() {
            return this.f6913d;
        }

        public final long c() {
            return this.f6911b;
        }

        public final int d() {
            return this.f6910a;
        }

        public final void e(long j3) {
            this.f6911b = j3;
        }
    }

    private final class e implements Choreographer.FrameCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private WritableArray f6914a;

        public e() {
        }

        @Override // android.view.Choreographer.FrameCallback
        public void doFrame(long j3) {
            d dVar;
            if (!JavaTimerManager.this.f6897i.get() || JavaTimerManager.this.f6898j.get()) {
                long j4 = j3 / ((long) 1000000);
                Object obj = JavaTimerManager.this.f6894f;
                JavaTimerManager javaTimerManager = JavaTimerManager.this;
                synchronized (obj) {
                    while (!javaTimerManager.f6905q.isEmpty()) {
                        try {
                            Object objPeek = javaTimerManager.f6905q.peek();
                            h.c(objPeek);
                            if (((d) objPeek).c() >= j4 || (dVar = (d) javaTimerManager.f6905q.poll()) == null) {
                                break;
                            }
                            if (this.f6914a == null) {
                                this.f6914a = Arguments.createArray();
                            }
                            WritableArray writableArray = this.f6914a;
                            if (writableArray != null) {
                                writableArray.pushInt(dVar.d());
                            }
                            if (dVar.b()) {
                                dVar.e(((long) dVar.a()) + j4);
                                javaTimerManager.f6905q.add(dVar);
                            } else {
                                javaTimerManager.f6896h.remove(dVar.d());
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                    r rVar = r.f10584a;
                }
                WritableArray writableArray2 = this.f6914a;
                if (writableArray2 != null) {
                    JavaTimerManager.this.f6891c.callTimers(writableArray2);
                    this.f6914a = null;
                }
                JavaTimerManager.this.f6892d.k(b.a.f6926f, this);
            }
        }
    }

    public JavaTimerManager(ReactApplicationContext reactApplicationContext, B1.c cVar, com.facebook.react.modules.core.b bVar, k1.e eVar) {
        h.f(reactApplicationContext, "reactApplicationContext");
        h.f(cVar, "javaScriptTimerExecutor");
        h.f(bVar, "reactChoreographer");
        h.f(eVar, "devSupportManager");
        this.f6890b = reactApplicationContext;
        this.f6891c = cVar;
        this.f6892d = bVar;
        this.f6893e = eVar;
        this.f6894f = new Object();
        this.f6895g = new Object();
        this.f6896h = new SparseArray();
        this.f6897i = new AtomicBoolean(true);
        this.f6898j = new AtomicBoolean(false);
        this.f6899k = new e();
        this.f6900l = new c();
        final p pVar = new p() { // from class: com.facebook.react.modules.core.a
            @Override // C2.p
            public final Object b(Object obj, Object obj2) {
                return Integer.valueOf(JavaTimerManager.B((JavaTimerManager.d) obj, (JavaTimerManager.d) obj2));
            }
        };
        this.f6905q = new PriorityQueue(11, new Comparator() { // from class: B1.d
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return JavaTimerManager.C(pVar, obj, obj2);
            }
        });
        reactApplicationContext.addLifecycleEventListener(this);
        C0742e.f10859g.a(reactApplicationContext).e(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void A(JavaTimerManager javaTimerManager, boolean z3) {
        synchronized (javaTimerManager.f6895g) {
            try {
                if (z3) {
                    javaTimerManager.z();
                } else {
                    javaTimerManager.r();
                }
                r rVar = r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int B(d dVar, d dVar2) {
        return F2.a.a(dVar.c() - dVar2.c());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int C(p pVar, Object obj, Object obj2) {
        return ((Number) pVar.b(obj, obj2)).intValue();
    }

    private final void r() {
        if (this.f6903o) {
            this.f6892d.n(b.a.f6927g, this.f6900l);
            this.f6903o = false;
        }
    }

    private final void s() {
        C0742e c0742eA = C0742e.f10859g.a(this.f6890b);
        if (this.f6902n && this.f6897i.get() && !c0742eA.h()) {
            this.f6892d.n(b.a.f6926f, this.f6899k);
            this.f6902n = false;
        }
    }

    private final void v() {
        if (!this.f6897i.get() || this.f6898j.get()) {
            return;
        }
        s();
    }

    private final void w() {
        synchronized (this.f6895g) {
            try {
                if (this.f6904p) {
                    z();
                }
                r rVar = r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private final void y() {
        if (this.f6902n) {
            return;
        }
        this.f6892d.k(b.a.f6926f, this.f6899k);
        this.f6902n = true;
    }

    private final void z() {
        if (this.f6903o) {
            return;
        }
        this.f6892d.k(b.a.f6927g, this.f6900l);
        this.f6903o = true;
    }

    @Override // u1.InterfaceC0743f
    public void a(int i3) {
        if (this.f6898j.getAndSet(true)) {
            return;
        }
        y();
        w();
    }

    @Override // u1.InterfaceC0743f
    public void b(int i3) {
        if (C0742e.f10859g.a(this.f6890b).h()) {
            return;
        }
        this.f6898j.set(false);
        s();
        v();
    }

    public void createTimer(int i3, long j3, boolean z3) {
        d dVar = new d(i3, (l.b() / ((long) 1000000)) + j3, (int) j3, z3);
        synchronized (this.f6894f) {
            this.f6905q.add(dVar);
            this.f6896h.put(i3, dVar);
            r rVar = r.f10584a;
        }
    }

    public void deleteTimer(int i3) {
        synchronized (this.f6894f) {
            d dVar = (d) this.f6896h.get(i3);
            if (dVar == null) {
                return;
            }
            this.f6896h.remove(i3);
            this.f6905q.remove(dVar);
        }
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostDestroy() {
        s();
        v();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostPause() {
        this.f6897i.set(true);
        s();
        v();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostResume() {
        this.f6897i.set(false);
        y();
        w();
    }

    public void setSendIdleEvents(final boolean z3) {
        synchronized (this.f6895g) {
            this.f6904p = z3;
            r rVar = r.f10584a;
        }
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: B1.e
            @Override // java.lang.Runnable
            public final void run() {
                JavaTimerManager.A(this.f82b, z3);
            }
        });
    }

    public void t(int i3, int i4, double d4, boolean z3) {
        long jA = l.a();
        long j3 = (long) d4;
        if (this.f6893e.n() && Math.abs(j3 - jA) > 60000) {
            this.f6891c.emitTimeDriftWarning("Debugger and device times have drifted by more than 60s. Please correct this by running adb shell \"date `date +%m%d%H%M%Y.%S`\" on your debugger machine.");
        }
        long jMax = Math.max(0L, (j3 - jA) + ((long) i4));
        if (i4 != 0 || z3) {
            createTimer(i3, jMax, z3);
            return;
        }
        WritableArray writableArrayCreateArray = Arguments.createArray();
        writableArrayCreateArray.pushInt(i3);
        B1.c cVar = this.f6891c;
        h.c(writableArrayCreateArray);
        cVar.callTimers(writableArrayCreateArray);
    }

    public final boolean u(long j3) {
        synchronized (this.f6894f) {
            d dVar = (d) this.f6905q.peek();
            if (dVar == null) {
                return false;
            }
            if (f6889r.b(dVar, j3)) {
                return true;
            }
            Iterator it = this.f6905q.iterator();
            h.e(it, "iterator(...)");
            while (it.hasNext()) {
                d dVar2 = (d) it.next();
                a aVar = f6889r;
                h.c(dVar2);
                if (aVar.b(dVar2, j3)) {
                    return true;
                }
            }
            r rVar = r.f10584a;
            return false;
        }
    }

    public void x() {
        C0742e.f10859g.a(this.f6890b).j(this);
        this.f6890b.removeLifecycleEventListener(this);
        s();
        r();
    }
}

package P1;

import a1.C0210a;
import android.util.LongSparseArray;
import android.view.Choreographer;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.modules.core.b;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.uimanager.events.RCTModernEventEmitter;
import com.facebook.react.uimanager.events.ReactEventEmitter;
import d2.C0518a;
import e1.C0527d;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class e implements EventDispatcher, LifecycleEventListener {

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private static final Comparator f1612r = new a();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final ReactApplicationContext f1615d;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final c f1618g;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final d f1622k;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private volatile ReactEventEmitter f1626o;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Object f1613b = new Object();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Object f1614c = new Object();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final LongSparseArray f1616e = new LongSparseArray();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Map f1617f = C0527d.b();

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final ArrayList f1619h = new ArrayList();

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final CopyOnWriteArrayList f1620i = new CopyOnWriteArrayList();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final CopyOnWriteArrayList f1621j = new CopyOnWriteArrayList();

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final AtomicInteger f1623l = new AtomicInteger();

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private P1.d[] f1624m = new P1.d[16];

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private int f1625n = 0;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private short f1627p = 0;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private volatile boolean f1628q = false;

    class a implements Comparator {
        a() {
        }

        @Override // java.util.Comparator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(P1.d dVar, P1.d dVar2) {
            if (dVar == null && dVar2 == null) {
                return 0;
            }
            if (dVar == null) {
                return -1;
            }
            if (dVar2 == null) {
                return 1;
            }
            long jM = dVar.m() - dVar2.m();
            if (jM == 0) {
                return 0;
            }
            return jM < 0 ? -1 : 1;
        }
    }

    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            e.this.F();
        }
    }

    private class c implements Runnable {
        @Override // java.lang.Runnable
        public void run() {
            C0518a.c(0L, "DispatchEventsRunnable");
            try {
                C0518a.f(0L, "ScheduleDispatchFrameCallback", e.this.f1623l.getAndIncrement());
                e.this.f1628q = false;
                C0210a.c(e.this.f1626o);
                synchronized (e.this.f1614c) {
                    try {
                        if (e.this.f1625n > 0) {
                            if (e.this.f1625n > 1) {
                                Arrays.sort(e.this.f1624m, 0, e.this.f1625n, e.f1612r);
                            }
                            for (int i3 = 0; i3 < e.this.f1625n; i3++) {
                                P1.d dVar = e.this.f1624m[i3];
                                if (dVar != null) {
                                    C0518a.f(0L, dVar.k(), dVar.n());
                                    dVar.d(e.this.f1626o);
                                    dVar.e();
                                }
                            }
                            e.this.A();
                            e.this.f1616e.clear();
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                Iterator it = e.this.f1621j.iterator();
                while (it.hasNext()) {
                    ((P1.a) it.next()).a();
                }
            } finally {
                C0518a.i(0L);
            }
        }

        private c() {
        }
    }

    private class d implements Choreographer.FrameCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private volatile boolean f1631a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private boolean f1632b;

        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                d.this.a();
            }
        }

        private void c() {
            com.facebook.react.modules.core.b.h().k(b.a.f6926f, e.this.f1622k);
        }

        public void a() {
            if (this.f1631a) {
                return;
            }
            this.f1631a = true;
            c();
        }

        public void b() {
            if (this.f1631a) {
                return;
            }
            if (e.this.f1615d.isOnUiQueueThread()) {
                a();
            } else {
                e.this.f1615d.runOnUiQueueThread(new a());
            }
        }

        public void d() {
            this.f1632b = true;
        }

        @Override // android.view.Choreographer.FrameCallback
        public void doFrame(long j3) {
            UiThreadUtil.assertOnUiThread();
            if (this.f1632b) {
                this.f1631a = false;
            } else {
                c();
            }
            C0518a.c(0L, "ScheduleDispatchFrameCallback");
            try {
                e.this.E();
                if (!e.this.f1628q) {
                    e.this.f1628q = true;
                    C0518a.l(0L, "ScheduleDispatchFrameCallback", e.this.f1623l.get());
                    e.this.f1615d.runOnJSQueueThread(e.this.f1618g);
                }
            } finally {
                C0518a.i(0L);
            }
        }

        private d() {
            this.f1631a = false;
            this.f1632b = false;
        }
    }

    public e(ReactApplicationContext reactApplicationContext) {
        this.f1618g = new c();
        this.f1622k = new d();
        this.f1615d = reactApplicationContext;
        reactApplicationContext.addLifecycleEventListener(this);
        this.f1626o = new ReactEventEmitter(reactApplicationContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void A() {
        Arrays.fill(this.f1624m, 0, this.f1625n, (Object) null);
        this.f1625n = 0;
    }

    private long B(int i3, String str, short s3) {
        short sShortValue;
        Short sh = (Short) this.f1617f.get(str);
        if (sh != null) {
            sShortValue = sh.shortValue();
        } else {
            short s4 = this.f1627p;
            this.f1627p = (short) (s4 + 1);
            this.f1617f.put(str, Short.valueOf(s4));
            sShortValue = s4;
        }
        return C(i3, sShortValue, s3);
    }

    private static long C(int i3, short s3, short s4) {
        return ((((long) s3) & 65535) << 32) | ((long) i3) | ((((long) s4) & 65535) << 48);
    }

    private void D() {
        if (this.f1626o != null) {
            this.f1622k.b();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void E() {
        synchronized (this.f1613b) {
            synchronized (this.f1614c) {
                for (int i3 = 0; i3 < this.f1619h.size(); i3++) {
                    try {
                        P1.d dVar = (P1.d) this.f1619h.get(i3);
                        if (dVar.a()) {
                            long jB = B(dVar.o(), dVar.k(), dVar.g());
                            Integer num = (Integer) this.f1616e.get(jB);
                            P1.d dVar2 = null;
                            if (num == null) {
                                this.f1616e.put(jB, Integer.valueOf(this.f1625n));
                            } else {
                                P1.d dVar3 = this.f1624m[num.intValue()];
                                P1.d dVarB = dVar.b(dVar3);
                                if (dVarB != dVar3) {
                                    this.f1616e.put(jB, Integer.valueOf(this.f1625n));
                                    this.f1624m[num.intValue()] = null;
                                    dVar2 = dVar3;
                                    dVar = dVarB;
                                } else {
                                    dVar2 = dVar;
                                    dVar = null;
                                }
                            }
                            if (dVar != null) {
                                z(dVar);
                            }
                            if (dVar2 != null) {
                                dVar2.e();
                            }
                        } else {
                            z(dVar);
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            this.f1619h.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F() {
        UiThreadUtil.assertOnUiThread();
        this.f1622k.d();
    }

    private void z(P1.d dVar) {
        int i3 = this.f1625n;
        P1.d[] dVarArr = this.f1624m;
        if (i3 == dVarArr.length) {
            this.f1624m = (P1.d[]) Arrays.copyOf(dVarArr, dVarArr.length * 2);
        }
        P1.d[] dVarArr2 = this.f1624m;
        int i4 = this.f1625n;
        this.f1625n = i4 + 1;
        dVarArr2[i4] = dVar;
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void a(int i3, RCTEventEmitter rCTEventEmitter) {
        this.f1626o.register(i3, rCTEventEmitter);
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void b(P1.d dVar) {
        C0210a.b(dVar.s(), "Dispatched event hasn't been initialized");
        Iterator it = this.f1620i.iterator();
        while (it.hasNext()) {
            ((g) it.next()).a(dVar);
        }
        synchronized (this.f1613b) {
            this.f1619h.add(dVar);
            C0518a.l(0L, dVar.k(), dVar.n());
        }
        D();
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void c() {
        UiThreadUtil.runOnUiThread(new b());
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void d(P1.a aVar) {
        this.f1621j.add(aVar);
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void e(P1.a aVar) {
        this.f1621j.remove(aVar);
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void f(int i3, RCTModernEventEmitter rCTModernEventEmitter) {
        this.f1626o.register(i3, rCTModernEventEmitter);
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void g(int i3) {
        this.f1626o.unregister(i3);
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void h() {
        D();
    }

    @Override // com.facebook.react.uimanager.events.EventDispatcher
    public void i(g gVar) {
        this.f1620i.add(gVar);
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostDestroy() {
        F();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostPause() {
        F();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostResume() {
        D();
    }
}

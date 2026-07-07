package com.facebook.react.uimanager;

import android.os.SystemClock;
import android.view.View;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.GuardedRunnable;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactNoCrashSoftException;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.RetryableMountingLayerException;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.modules.core.b;
import d2.C0518a;
import d2.C0519b;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class M0 {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private static final String f7258A = "M0";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0421b0 f7260b;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final i f7263e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final ReactApplicationContext f7264f;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private N1.a f7269k;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private long f7273o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private long f7274p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private long f7275q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private long f7276r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private long f7277s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private long f7278t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private long f7279u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private long f7280v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private long f7281w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private long f7282x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private long f7283y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private long f7284z;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int[] f7259a = new int[4];

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Object f7261c = new Object();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Object f7262d = new Object();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private ArrayList f7265g = new ArrayList();

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private ArrayList f7266h = new ArrayList();

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private ArrayList f7267i = new ArrayList();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private ArrayDeque f7268j = new ArrayDeque();

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f7270l = false;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private boolean f7271m = false;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private boolean f7272n = false;

    class a implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f7285b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ ArrayList f7286c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ ArrayDeque f7287d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ ArrayList f7288e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        final /* synthetic */ long f7289f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        final /* synthetic */ long f7290g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        final /* synthetic */ long f7291h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        final /* synthetic */ long f7292i;

        a(int i3, ArrayList arrayList, ArrayDeque arrayDeque, ArrayList arrayList2, long j3, long j4, long j5, long j6) {
            this.f7285b = i3;
            this.f7286c = arrayList;
            this.f7287d = arrayDeque;
            this.f7288e = arrayList2;
            this.f7289f = j3;
            this.f7290g = j4;
            this.f7291h = j5;
            this.f7292i = j6;
        }

        @Override // java.lang.Runnable
        public void run() {
            C0519b.a(0L, "DispatchUI").a("BatchId", this.f7285b).c();
            try {
                try {
                    long jUptimeMillis = SystemClock.uptimeMillis();
                    ArrayList<g> arrayList = this.f7286c;
                    if (arrayList != null) {
                        for (g gVar : arrayList) {
                            try {
                                gVar.c();
                            } catch (RetryableMountingLayerException e4) {
                                if (gVar.b() == 0) {
                                    gVar.d();
                                    M0.this.f7265g.add(gVar);
                                } else {
                                    ReactSoftExceptionLogger.logSoftException(M0.f7258A, new ReactNoCrashSoftException(e4));
                                }
                            } catch (Throwable th) {
                                ReactSoftExceptionLogger.logSoftException(M0.f7258A, th);
                            }
                        }
                    }
                    ArrayDeque arrayDeque = this.f7287d;
                    if (arrayDeque != null) {
                        Iterator it = arrayDeque.iterator();
                        while (it.hasNext()) {
                            ((r) it.next()).a();
                        }
                    }
                    ArrayList arrayList2 = this.f7288e;
                    if (arrayList2 != null) {
                        Iterator it2 = arrayList2.iterator();
                        while (it2.hasNext()) {
                            ((r) it2.next()).a();
                        }
                    }
                    if (M0.this.f7272n && M0.this.f7274p == 0) {
                        M0.this.f7274p = this.f7289f;
                        M0.this.f7275q = SystemClock.uptimeMillis();
                        M0.this.f7276r = this.f7290g;
                        M0.this.f7277s = this.f7291h;
                        M0.this.f7278t = jUptimeMillis;
                        M0 m02 = M0.this;
                        m02.f7279u = m02.f7275q;
                        M0.this.f7282x = this.f7292i;
                        C0518a.b(0L, "delayBeforeDispatchViewUpdates", 0, M0.this.f7274p * 1000000);
                        C0518a.h(0L, "delayBeforeDispatchViewUpdates", 0, M0.this.f7277s * 1000000);
                        C0518a.b(0L, "delayBeforeBatchRunStart", 0, M0.this.f7277s * 1000000);
                        C0518a.h(0L, "delayBeforeBatchRunStart", 0, M0.this.f7278t * 1000000);
                    }
                    M0.this.f7260b.f();
                    if (M0.this.f7269k != null) {
                        M0.this.f7269k.b();
                    }
                    C0518a.i(0L);
                } catch (Exception e5) {
                    M0.this.f7271m = true;
                    throw e5;
                }
            } catch (Throwable th2) {
                C0518a.i(0L);
                throw th2;
            }
        }
    }

    class b extends GuardedRunnable {
        b(ReactContext reactContext) {
            super(reactContext);
        }

        @Override // com.facebook.react.bridge.GuardedRunnable
        public void runGuarded() {
            M0.this.R();
        }
    }

    private final class c extends v {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f7295c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final boolean f7296d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final boolean f7297e;

        public c(int i3, int i4, boolean z3, boolean z4) {
            super(i3);
            this.f7295c = i4;
            this.f7297e = z3;
            this.f7296d = z4;
        }

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            if (this.f7297e) {
                M0.this.f7260b.e();
            } else {
                M0.this.f7260b.y(this.f7349a, this.f7295c, this.f7296d);
            }
        }
    }

    private class d implements r {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final ReadableMap f7299a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Callback f7300b;

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            M0.this.f7260b.h(this.f7299a, this.f7300b);
        }

        private d(ReadableMap readableMap, Callback callback) {
            this.f7299a = readableMap;
            this.f7300b = callback;
        }
    }

    private final class e extends v {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final B0 f7302c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final String f7303d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final C0454s0 f7304e;

        public e(B0 b02, int i3, String str, C0454s0 c0454s0) {
            super(i3);
            this.f7302c = b02;
            this.f7303d = str;
            this.f7304e = c0454s0;
            C0518a.l(0L, "createView", this.f7349a);
        }

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            C0518a.f(0L, "createView", this.f7349a);
            M0.this.f7260b.j(this.f7302c, this.f7349a, this.f7303d, this.f7304e);
        }
    }

    private final class f extends v implements g {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f7306c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final ReadableArray f7307d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int f7308e;

        public f(int i3, int i4, ReadableArray readableArray) {
            super(i3);
            this.f7308e = 0;
            this.f7306c = i4;
            this.f7307d = readableArray;
        }

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            try {
                M0.this.f7260b.k(this.f7349a, this.f7306c, this.f7307d);
            } catch (Throwable th) {
                ReactSoftExceptionLogger.logSoftException(M0.f7258A, new RuntimeException("Error dispatching View Command", th));
            }
        }

        @Override // com.facebook.react.uimanager.M0.g
        public int b() {
            return this.f7308e;
        }

        @Override // com.facebook.react.uimanager.M0.g
        public void c() {
            M0.this.f7260b.k(this.f7349a, this.f7306c, this.f7307d);
        }

        @Override // com.facebook.react.uimanager.M0.g
        public void d() {
            this.f7308e++;
        }
    }

    private interface g {
        int b();

        void c();

        void d();
    }

    private final class h extends v implements g {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final String f7310c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final ReadableArray f7311d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int f7312e;

        public h(int i3, String str, ReadableArray readableArray) {
            super(i3);
            this.f7312e = 0;
            this.f7310c = str;
            this.f7311d = readableArray;
        }

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            try {
                M0.this.f7260b.l(this.f7349a, this.f7310c, this.f7311d);
            } catch (Throwable th) {
                ReactSoftExceptionLogger.logSoftException(M0.f7258A, new RuntimeException("Error dispatching View Command", th));
            }
        }

        @Override // com.facebook.react.uimanager.M0.g
        public int b() {
            return this.f7312e;
        }

        @Override // com.facebook.react.uimanager.M0.g
        public void c() {
            M0.this.f7260b.l(this.f7349a, this.f7310c, this.f7311d);
        }

        @Override // com.facebook.react.uimanager.M0.g
        public void d() {
            this.f7312e++;
        }
    }

    private class i extends M {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int f7314b;

        private void b(long j3) throws Exception {
            r rVar;
            while (16 - ((System.nanoTime() - j3) / 1000000) >= this.f7314b) {
                synchronized (M0.this.f7262d) {
                    try {
                        if (M0.this.f7268j.isEmpty()) {
                            return;
                        } else {
                            rVar = (r) M0.this.f7268j.pollFirst();
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                try {
                    long jUptimeMillis = SystemClock.uptimeMillis();
                    rVar.a();
                    M0.this.f7273o += SystemClock.uptimeMillis() - jUptimeMillis;
                } catch (Exception e4) {
                    M0.this.f7271m = true;
                    throw e4;
                }
            }
        }

        @Override // com.facebook.react.uimanager.M
        public void a(long j3) {
            if (M0.this.f7271m) {
                Y.a.I("ReactNative", "Not flushing pending UI operations because of previously thrown Exception");
                return;
            }
            C0518a.c(0L, "dispatchNonBatchedUIOperations");
            try {
                b(j3);
                C0518a.i(0L);
                M0.this.R();
                com.facebook.react.modules.core.b.h().k(b.a.f6924d, this);
            } catch (Throwable th) {
                C0518a.i(0L);
                throw th;
            }
        }

        private i(ReactContext reactContext, int i3) {
            super(reactContext);
            this.f7314b = i3;
        }
    }

    private final class j implements r {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final int f7316a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final float f7317b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final float f7318c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final Callback f7319d;

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            try {
                M0.this.f7260b.s(this.f7316a, M0.this.f7259a);
                float f3 = M0.this.f7259a[0];
                float f4 = M0.this.f7259a[1];
                int iN = M0.this.f7260b.n(this.f7316a, this.f7317b, this.f7318c);
                try {
                    M0.this.f7260b.s(iN, M0.this.f7259a);
                    this.f7319d.invoke(Integer.valueOf(iN), Float.valueOf(C0429f0.f(M0.this.f7259a[0] - f3)), Float.valueOf(C0429f0.f(M0.this.f7259a[1] - f4)), Float.valueOf(C0429f0.f(M0.this.f7259a[2])), Float.valueOf(C0429f0.f(M0.this.f7259a[3])));
                } catch (P unused) {
                    this.f7319d.invoke(new Object[0]);
                }
            } catch (P unused2) {
                this.f7319d.invoke(new Object[0]);
            }
        }

        private j(int i3, float f3, float f4, Callback callback) {
            this.f7316a = i3;
            this.f7317b = f3;
            this.f7318c = f4;
            this.f7319d = callback;
        }
    }

    private final class k extends v {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int[] f7321c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final O0[] f7322d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final int[] f7323e;

        public k(int i3, int[] iArr, O0[] o0Arr, int[] iArr2) {
            super(i3);
            this.f7321c = iArr;
            this.f7322d = o0Arr;
            this.f7323e = iArr2;
        }

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            M0.this.f7260b.q(this.f7349a, this.f7321c, this.f7322d, this.f7323e);
        }
    }

    private final class l implements r {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final int f7325a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Callback f7326b;

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            try {
                M0.this.f7260b.t(this.f7325a, M0.this.f7259a);
                this.f7326b.invoke(Float.valueOf(C0429f0.f(M0.this.f7259a[0])), Float.valueOf(C0429f0.f(M0.this.f7259a[1])), Float.valueOf(C0429f0.f(M0.this.f7259a[2])), Float.valueOf(C0429f0.f(M0.this.f7259a[3])));
            } catch (C0425d0 unused) {
                this.f7326b.invoke(new Object[0]);
            }
        }

        private l(int i3, Callback callback) {
            this.f7325a = i3;
            this.f7326b = callback;
        }
    }

    private final class m implements r {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final int f7328a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Callback f7329b;

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            try {
                M0.this.f7260b.s(this.f7328a, M0.this.f7259a);
                this.f7329b.invoke(0, 0, Float.valueOf(C0429f0.f(M0.this.f7259a[2])), Float.valueOf(C0429f0.f(M0.this.f7259a[3])), Float.valueOf(C0429f0.f(M0.this.f7259a[0])), Float.valueOf(C0429f0.f(M0.this.f7259a[1])));
            } catch (C0425d0 unused) {
                this.f7329b.invoke(new Object[0]);
            }
        }

        private m(int i3, Callback callback) {
            this.f7328a = i3;
            this.f7329b = callback;
        }
    }

    private final class n extends v {
        public n(int i3) {
            super(i3);
        }

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            M0.this.f7260b.u(this.f7349a);
        }
    }

    private final class o extends v {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f7332c;

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            try {
                M0.this.f7260b.x(this.f7349a, this.f7332c);
            } catch (RetryableMountingLayerException e4) {
                ReactSoftExceptionLogger.logSoftException(M0.f7258A, e4);
            }
        }

        private o(int i3, int i4) {
            super(i3);
            this.f7332c = i4;
        }
    }

    private class p implements r {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final boolean f7334a;

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            M0.this.f7260b.z(this.f7334a);
        }

        private p(boolean z3) {
            this.f7334a = z3;
        }
    }

    private class q implements r {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final F0 f7336a;

        public q(F0 f02) {
            this.f7336a = f02;
        }

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            this.f7336a.a(M0.this.f7260b);
        }
    }

    public interface r {
        void a();
    }

    private final class s extends v {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f7338c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final int f7339d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final int f7340e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final int f7341f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private final int f7342g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private final com.facebook.yoga.h f7343h;

        public s(int i3, int i4, int i5, int i6, int i7, int i8, com.facebook.yoga.h hVar) {
            super(i4);
            this.f7338c = i3;
            this.f7339d = i5;
            this.f7340e = i6;
            this.f7341f = i7;
            this.f7342g = i8;
            this.f7343h = hVar;
            C0518a.l(0L, "updateLayout", this.f7349a);
        }

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            C0518a.f(0L, "updateLayout", this.f7349a);
            M0.this.f7260b.A(this.f7338c, this.f7349a, this.f7339d, this.f7340e, this.f7341f, this.f7342g, this.f7343h);
        }
    }

    private final class t extends v {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final C0454s0 f7345c;

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            M0.this.f7260b.C(this.f7349a, this.f7345c);
        }

        private t(int i3, C0454s0 c0454s0) {
            super(i3);
            this.f7345c = c0454s0;
        }
    }

    private final class u extends v {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final Object f7347c;

        public u(int i3, Object obj) {
            super(i3);
            this.f7347c = obj;
        }

        @Override // com.facebook.react.uimanager.M0.r
        public void a() {
            M0.this.f7260b.D(this.f7349a, this.f7347c);
        }
    }

    private abstract class v implements r {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public int f7349a;

        public v(int i3) {
            this.f7349a = i3;
        }
    }

    public M0(ReactApplicationContext reactApplicationContext, C0421b0 c0421b0, int i3) {
        this.f7260b = c0421b0;
        this.f7263e = new i(reactApplicationContext, i3 == -1 ? 8 : i3);
        this.f7264f = reactApplicationContext;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void R() {
        if (this.f7271m) {
            Y.a.I("ReactNative", "Not flushing pending UI operations because of previously thrown Exception");
            return;
        }
        synchronized (this.f7261c) {
            if (this.f7267i.isEmpty()) {
                return;
            }
            ArrayList arrayList = this.f7267i;
            this.f7267i = new ArrayList();
            long jUptimeMillis = SystemClock.uptimeMillis();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((Runnable) it.next()).run();
            }
            if (this.f7272n) {
                this.f7280v = SystemClock.uptimeMillis() - jUptimeMillis;
                this.f7281w = this.f7273o;
                this.f7272n = false;
                C0518a.b(0L, "batchedExecutionTime", 0, jUptimeMillis * 1000000);
                C0518a.g(0L, "batchedExecutionTime", 0);
            }
            this.f7273o = 0L;
        }
    }

    public void A() {
        this.f7266h.add(new c(0, 0, true, false));
    }

    public void B(ReadableMap readableMap, Callback callback) {
        this.f7266h.add(new d(readableMap, callback));
    }

    public void C(B0 b02, int i3, String str, C0454s0 c0454s0) {
        synchronized (this.f7262d) {
            this.f7283y++;
            this.f7268j.addLast(new e(b02, i3, str, c0454s0));
        }
    }

    public void D(int i3, int i4, ReadableArray readableArray) {
        this.f7265g.add(new f(i3, i4, readableArray));
    }

    public void E(int i3, String str, ReadableArray readableArray) {
        this.f7265g.add(new h(i3, str, readableArray));
    }

    public void F(int i3, float f3, float f4, Callback callback) {
        this.f7266h.add(new j(i3, f3, f4, callback));
    }

    public void G(int i3, int[] iArr, O0[] o0Arr, int[] iArr2) {
        this.f7266h.add(new k(i3, iArr, o0Arr, iArr2));
    }

    public void H(int i3, Callback callback) {
        this.f7266h.add(new m(i3, callback));
    }

    public void I(int i3, Callback callback) {
        this.f7266h.add(new l(i3, callback));
    }

    public void J(int i3) {
        this.f7266h.add(new n(i3));
    }

    public void K(int i3, int i4) {
        this.f7266h.add(new o(i3, i4));
    }

    public void L(int i3, int i4, boolean z3) {
        this.f7266h.add(new c(i3, i4, false, z3));
    }

    public void M(boolean z3) {
        this.f7266h.add(new p(z3));
    }

    public void N(F0 f02) {
        this.f7266h.add(new q(f02));
    }

    public void O(int i3, Object obj) {
        this.f7266h.add(new u(i3, obj));
    }

    public void P(int i3, int i4, int i5, int i6, int i7, int i8, com.facebook.yoga.h hVar) {
        this.f7266h.add(new s(i3, i4, i5, i6, i7, i8, hVar));
    }

    public void Q(int i3, String str, C0454s0 c0454s0) {
        this.f7284z++;
        this.f7266h.add(new t(i3, c0454s0));
    }

    C0421b0 S() {
        return this.f7260b;
    }

    public Map T() {
        HashMap map = new HashMap();
        map.put("CommitStartTime", Long.valueOf(this.f7274p));
        map.put("CommitEndTime", Long.valueOf(this.f7275q));
        map.put("LayoutTime", Long.valueOf(this.f7276r));
        map.put("DispatchViewUpdatesTime", Long.valueOf(this.f7277s));
        map.put("RunStartTime", Long.valueOf(this.f7278t));
        map.put("RunEndTime", Long.valueOf(this.f7279u));
        map.put("BatchedExecutionTime", Long.valueOf(this.f7280v));
        map.put("NonBatchedExecutionTime", Long.valueOf(this.f7281w));
        map.put("NativeModulesThreadCpuTime", Long.valueOf(this.f7282x));
        map.put("CreateViewCount", Long.valueOf(this.f7283y));
        map.put("UpdatePropsCount", Long.valueOf(this.f7284z));
        return map;
    }

    public boolean U() {
        return this.f7266h.isEmpty() && this.f7265g.isEmpty();
    }

    void V() {
        this.f7270l = false;
        com.facebook.react.modules.core.b.h().n(b.a.f6924d, this.f7263e);
        R();
    }

    public void W(F0 f02) {
        this.f7266h.add(0, new q(f02));
    }

    public void X() {
        this.f7272n = true;
        this.f7274p = 0L;
        this.f7283y = 0L;
        this.f7284z = 0L;
    }

    void Y() {
        this.f7270l = true;
        com.facebook.react.modules.core.b.h().k(b.a.f6924d, this.f7263e);
    }

    public void Z(N1.a aVar) {
        this.f7269k = aVar;
    }

    public void y(int i3, View view) {
        this.f7260b.b(i3, view);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v9 */
    public void z(int i3, long j3, long j4) throws Throwable {
        long j5;
        long jUptimeMillis;
        long jCurrentThreadTimeMillis;
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayDeque arrayDeque;
        C0519b.a(0L, "UIViewOperationQueue.dispatchViewUpdates").a("batchId", i3).c();
        try {
            jUptimeMillis = SystemClock.uptimeMillis();
            jCurrentThreadTimeMillis = SystemClock.currentThreadTimeMillis();
            j5 = 0;
            j5 = 0;
            if (this.f7265g.isEmpty()) {
                arrayList = null;
            } else {
                ArrayList arrayList3 = this.f7265g;
                this.f7265g = new ArrayList();
                arrayList = arrayList3;
            }
            if (this.f7266h.isEmpty()) {
                arrayList2 = null;
            } else {
                ArrayList arrayList4 = this.f7266h;
                this.f7266h = new ArrayList();
                arrayList2 = arrayList4;
            }
            synchronized (this.f7262d) {
                try {
                    try {
                        if (!this.f7268j.isEmpty()) {
                            ArrayDeque arrayDeque2 = this.f7268j;
                            this.f7268j = new ArrayDeque();
                            j5 = arrayDeque2;
                        }
                        arrayDeque = j5;
                    } finally {
                        th = th;
                        while (true) {
                            try {
                            } catch (Throwable th) {
                                th = th;
                            }
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            N1.a aVar = this.f7269k;
            if (aVar != null) {
                aVar.a();
            }
        } catch (Throwable th3) {
            th = th3;
            j5 = 0;
        }
        try {
            a aVar2 = new a(i3, arrayList, arrayDeque, arrayList2, j3, j4, jUptimeMillis, jCurrentThreadTimeMillis);
            j5 = 0;
            j5 = 0;
            C0519b.a(0L, "acquiring mDispatchRunnablesLock").a("batchId", i3).c();
            synchronized (this.f7261c) {
                C0518a.i(0L);
                this.f7267i.add(aVar2);
            }
            if (!this.f7270l) {
                UiThreadUtil.runOnUiThread(new b(this.f7264f));
            }
            C0518a.i(0L);
        } catch (Throwable th4) {
            th = th4;
            j5 = 0;
            C0518a.i(j5);
            throw th;
        }
    }
}

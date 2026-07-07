package com.facebook.react.fabric;

import P1.k;
import P1.p;
import android.content.Context;
import android.graphics.Point;
import android.os.SystemClock;
import android.view.View;
import com.facebook.react.bridge.ColorPropConverter;
import com.facebook.react.bridge.GuardedRunnable;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.NativeArray;
import com.facebook.react.bridge.NativeMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.bridge.UIManagerListener;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.mapbuffer.ReadableMapBuffer;
import com.facebook.react.fabric.a;
import com.facebook.react.fabric.events.EventEmitterWrapper;
import com.facebook.react.fabric.events.FabricEventEmitter;
import com.facebook.react.fabric.mounting.mountitems.MountItem;
import com.facebook.react.internal.interop.InteropEventEmitter;
import com.facebook.react.modules.core.b;
import com.facebook.react.uimanager.A0;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.C0449p0;
import com.facebook.react.uimanager.C0464x0;
import com.facebook.react.uimanager.H0;
import com.facebook.react.uimanager.InterfaceC0447o0;
import com.facebook.react.uimanager.M;
import com.facebook.react.uimanager.P;
import com.facebook.react.uimanager.R0;
import com.facebook.react.uimanager.U0;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.text.t;
import g1.C0542a;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import l1.C0591a;
import m1.InterfaceC0606a;
import n1.InterfaceC0618a;
import n1.c;
import n1.d;
import p1.InterfaceC0637b;
import r1.C0670b;

/* JADX INFO: loaded from: classes.dex */
public class FabricUIManager implements UIManager, LifecycleEventListener, m1.b, p {
    private static final a.InterfaceC0106a FABRIC_PERF_LOGGER = new a.InterfaceC0106a() { // from class: com.facebook.react.fabric.d
        @Override // com.facebook.react.fabric.a.InterfaceC0106a
        public final void a(a.b bVar) {
            FabricUIManager.lambda$static$0(bVar);
        }
    };
    public static final boolean IS_DEVELOPMENT_ENVIRONMENT = false;
    public static final String TAG = "FabricUIManager";
    private final P1.a mBatchEventDispatchedListener;
    private FabricUIManagerBinding mBinding;
    public com.facebook.react.fabric.a mDevToolsReactPerfLogger;
    private final f mDispatchUIFrameCallback;
    private final EventDispatcher mEventDispatcher;
    private C0591a mInteropUIBlockListener;
    private final n1.c mMountItemDispatcher;
    private final d.a mMountItemExecutor;
    private final n1.d mMountingManager;
    private final ReactApplicationContext mReactApplicationContext;
    private final U0 mViewManagerRegistry;
    private final CopyOnWriteArrayList<UIManagerListener> mListeners = new CopyOnWriteArrayList<>();
    private boolean mMountNotificationScheduled = false;
    private List<Integer> mSurfaceIdsWithPendingMountNotification = new ArrayList();
    private final Set<h> mSynchronousEvents = new HashSet();
    private volatile boolean mDestroyed = false;
    private boolean mDriveCxxAnimations = false;
    private long mDispatchViewUpdatesTime = 0;
    private long mCommitStartTime = 0;
    private long mLayoutTime = 0;
    private long mFinishTransactionTime = 0;
    private long mFinishTransactionCPPTime = 0;
    private int mCurrentSynchronousCommitNumber = 10000;

    class a implements d.a {
        a() {
        }

        @Override // n1.d.a
        public void a(Queue queue) {
            FabricUIManager.this.mMountItemDispatcher.f(queue);
        }
    }

    class b implements MountItem {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f6796a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ ReadableMap f6797b;

        b(int i3, ReadableMap readableMap) {
            this.f6796a = i3;
            this.f6797b = readableMap;
        }

        @Override // com.facebook.react.fabric.mounting.mountitems.MountItem
        public void execute(n1.d dVar) {
            try {
                dVar.u(this.f6796a, this.f6797b);
            } catch (Exception unused) {
            }
        }

        @Override // com.facebook.react.fabric.mounting.mountitems.MountItem
        public int getSurfaceId() {
            return -1;
        }

        public String toString() {
            return String.format("SYNC UPDATE PROPS [%d]: %s", Integer.valueOf(this.f6796a), FabricUIManager.IS_DEVELOPMENT_ENVIRONMENT ? this.f6797b.toHashMap().toString() : "<hidden>");
        }
    }

    class c extends GuardedRunnable {
        c(ReactContext reactContext) {
            super(reactContext);
        }

        @Override // com.facebook.react.bridge.GuardedRunnable
        public void runGuarded() {
            FabricUIManager.this.mMountItemDispatcher.r();
        }
    }

    class d implements MountItem {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f6800a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f6801b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6802c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ boolean f6803d;

        d(int i3, int i4, int i5, boolean z3) {
            this.f6800a = i3;
            this.f6801b = i4;
            this.f6802c = i5;
            this.f6803d = z3;
        }

        @Override // com.facebook.react.fabric.mounting.mountitems.MountItem
        public void execute(n1.d dVar) {
            n1.g gVarF = dVar.f(this.f6800a);
            if (gVarF != null) {
                gVarF.H(this.f6801b, this.f6802c, this.f6803d);
                return;
            }
            Y.a.m(FabricUIManager.TAG, "setJSResponder skipped, surface no longer available [" + this.f6800a + "]");
        }

        @Override // com.facebook.react.fabric.mounting.mountitems.MountItem
        public int getSurfaceId() {
            return this.f6800a;
        }

        public String toString() {
            return String.format("SET_JS_RESPONDER [%d] [surface:%d]", Integer.valueOf(this.f6801b), Integer.valueOf(this.f6800a));
        }
    }

    class e implements MountItem {
        e() {
        }

        @Override // com.facebook.react.fabric.mounting.mountitems.MountItem
        public void execute(n1.d dVar) {
            dVar.b();
        }

        @Override // com.facebook.react.fabric.mounting.mountitems.MountItem
        public int getSurfaceId() {
            return -1;
        }

        public String toString() {
            return "CLEAR_JS_RESPONDER";
        }
    }

    private class f extends M {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private volatile boolean f6806b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private boolean f6807c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private boolean f6808d;

        private void d() {
            if (this.f6808d || !this.f6807c) {
                return;
            }
            this.f6808d = true;
            com.facebook.react.modules.core.b.h().k(b.a.f6924d, this);
        }

        @Override // com.facebook.react.uimanager.M
        public void a(long j3) {
            this.f6808d = false;
            if (!this.f6806b) {
                Y.a.I(FabricUIManager.TAG, "Not flushing pending UI operations: exception was previously thrown");
                return;
            }
            if (FabricUIManager.this.mDestroyed) {
                Y.a.I(FabricUIManager.TAG, "Not flushing pending UI operations: FabricUIManager is destroyed");
                return;
            }
            if (FabricUIManager.this.mDriveCxxAnimations && FabricUIManager.this.mBinding != null) {
                FabricUIManager.this.mBinding.driveCxxAnimations();
            }
            if (FabricUIManager.this.mBinding != null) {
                FabricUIManager.this.mBinding.drainPreallocateViewsQueue();
            }
            try {
                try {
                    FabricUIManager.this.mMountItemDispatcher.g(j3);
                    FabricUIManager.this.mMountItemDispatcher.r();
                    d();
                    FabricUIManager.this.mSynchronousEvents.clear();
                } catch (Exception e4) {
                    Y.a.n(FabricUIManager.TAG, "Exception thrown when executing UIFrameGuarded", e4);
                    this.f6806b = false;
                    throw new RuntimeException("Exception thrown when executing UIFrameGuarded", e4);
                }
            } catch (Throwable th) {
                d();
                throw th;
            }
        }

        void b() {
            com.facebook.react.modules.core.b.h().n(b.a.f6924d, this);
            this.f6807c = false;
            this.f6808d = false;
        }

        void c() {
            this.f6807c = true;
            d();
        }

        private f(ReactContext reactContext) {
            super(reactContext);
            this.f6806b = true;
            this.f6807c = false;
            this.f6808d = false;
        }
    }

    private class g implements c.a {

        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                FabricUIManager.this.mMountNotificationScheduled = false;
                List list = FabricUIManager.this.mSurfaceIdsWithPendingMountNotification;
                FabricUIManager.this.mSurfaceIdsWithPendingMountNotification = new ArrayList();
                FabricUIManagerBinding fabricUIManagerBinding = FabricUIManager.this.mBinding;
                if (fabricUIManagerBinding == null || FabricUIManager.this.mDestroyed) {
                    return;
                }
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    fabricUIManagerBinding.reportMount(((Integer) it.next()).intValue());
                }
            }
        }

        @Override // n1.c.a
        public void a(List list) {
            Iterator it = FabricUIManager.this.mListeners.iterator();
            while (it.hasNext()) {
                ((UIManagerListener) it.next()).didMountItems(FabricUIManager.this);
            }
            if (list == null || list.isEmpty()) {
                return;
            }
            Iterator it2 = list.iterator();
            while (it2.hasNext()) {
                MountItem mountItem = (MountItem) it2.next();
                if (mountItem != null && !FabricUIManager.this.mSurfaceIdsWithPendingMountNotification.contains(Integer.valueOf(mountItem.getSurfaceId()))) {
                    FabricUIManager.this.mSurfaceIdsWithPendingMountNotification.add(Integer.valueOf(mountItem.getSurfaceId()));
                }
            }
            if (FabricUIManager.this.mMountNotificationScheduled || FabricUIManager.this.mSurfaceIdsWithPendingMountNotification.isEmpty()) {
                return;
            }
            FabricUIManager.this.mMountNotificationScheduled = true;
            UiThreadUtil.getUiThreadHandler().postAtFrontOfQueue(new a());
        }

        @Override // n1.c.a
        public void b(List list) {
            Iterator it = FabricUIManager.this.mListeners.iterator();
            while (it.hasNext()) {
                ((UIManagerListener) it.next()).willMountItems(FabricUIManager.this);
            }
        }

        @Override // n1.c.a
        public void c() {
            Iterator it = FabricUIManager.this.mListeners.iterator();
            while (it.hasNext()) {
                ((UIManagerListener) it.next()).didDispatchMountItems(FabricUIManager.this);
            }
        }

        private g() {
        }
    }

    static {
        com.facebook.react.fabric.c.a();
    }

    public FabricUIManager(ReactApplicationContext reactApplicationContext, U0 u02, P1.a aVar) {
        a aVar2 = new a();
        this.mMountItemExecutor = aVar2;
        this.mDispatchUIFrameCallback = new f(reactApplicationContext);
        this.mReactApplicationContext = reactApplicationContext;
        n1.d dVar = new n1.d(u02, aVar2);
        this.mMountingManager = dVar;
        this.mMountItemDispatcher = new n1.c(dVar, new g());
        this.mEventDispatcher = new k(reactApplicationContext);
        this.mBatchEventDispatchedListener = aVar;
        reactApplicationContext.addLifecycleEventListener(this);
        this.mViewManagerRegistry = u02;
        reactApplicationContext.registerComponentCallbacks(u02);
    }

    private MountItem createIntBufferBatchMountItem(int i3, int[] iArr, Object[] objArr, int i4) {
        if (iArr == null) {
            iArr = new int[0];
        }
        if (objArr == null) {
            objArr = new Object[0];
        }
        return com.facebook.react.fabric.mounting.mountitems.g.d(i3, iArr, objArr, i4);
    }

    private void destroyUnmountedView(int i3, int i4) {
        this.mMountItemDispatcher.b(com.facebook.react.fabric.mounting.mountitems.g.a(i3, i4));
    }

    private C0591a getInteropUIBlockListener() {
        if (this.mInteropUIBlockListener == null) {
            C0591a c0591a = new C0591a();
            this.mInteropUIBlockListener = c0591a;
            addUIManagerEventListener(c0591a);
        }
        return this.mInteropUIBlockListener;
    }

    private boolean isOnMainThread() {
        return UiThreadUtil.isOnUiThread();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$static$0(a.b bVar) {
        long jF = bVar.f();
        long jO = bVar.o();
        long j3 = bVar.j();
        long jS = bVar.s();
        long jC = bVar.c();
        com.facebook.react.fabric.g gVar = com.facebook.react.fabric.a.f6814c;
        gVar.a(jF);
        com.facebook.react.fabric.g gVar2 = com.facebook.react.fabric.a.f6815d;
        gVar2.a(jO);
        com.facebook.react.fabric.g gVar3 = com.facebook.react.fabric.a.f6816e;
        gVar3.a(j3);
        com.facebook.react.fabric.g gVar4 = com.facebook.react.fabric.a.f6817f;
        gVar4.a(jS);
        com.facebook.react.fabric.g gVar5 = com.facebook.react.fabric.a.f6818g;
        gVar5.a(jC);
        Y.a.v(TAG, "Statistics of Fabric commit #%d:\n - Total commit time: %d ms. Avg: %.2f. Median: %.2f ms. Max: %d ms.\n - Layout time: %d ms. Avg: %.2f. Median: %.2f ms. Max: %d ms.\n - Diffing time: %d ms. Avg: %.2f. Median: %.2f ms. Max: %d ms.\n - FinishTransaction (Diffing + JNI serialization): %d ms. Avg: %.2f. Median: %.2f ms. Max: %d ms.\n - Mounting: %d ms. Avg: %.2f. Median: %.2f ms. Max: %d ms.\n", Long.valueOf(bVar.h()), Long.valueOf(jF), Double.valueOf(gVar.b()), Double.valueOf(gVar.d()), Long.valueOf(gVar.c()), Long.valueOf(jO), Double.valueOf(gVar2.b()), Double.valueOf(gVar2.d()), Long.valueOf(gVar2.c()), Long.valueOf(j3), Double.valueOf(gVar3.b()), Double.valueOf(gVar3.d()), Long.valueOf(gVar3.c()), Long.valueOf(jS), Double.valueOf(gVar4.b()), Double.valueOf(gVar4.d()), Long.valueOf(gVar4.c()), Long.valueOf(jC), Double.valueOf(gVar5.b()), Double.valueOf(gVar5.d()), Long.valueOf(gVar5.c()));
    }

    private long measure(int i3, String str, ReadableMap readableMap, ReadableMap readableMap2, ReadableMap readableMap3, float f3, float f4, float f5, float f6) {
        return measure(i3, str, readableMap, readableMap2, readableMap3, f3, f4, f5, f6, null);
    }

    private NativeArray measureLines(ReadableMapBuffer readableMapBuffer, ReadableMapBuffer readableMapBuffer2, float f3, float f4) {
        return (NativeArray) t.m(this.mReactApplicationContext, readableMapBuffer, readableMapBuffer2, C0429f0.h(f3), C0429f0.h(f4));
    }

    private long measureMapBuffer(int i3, String str, ReadableMapBuffer readableMapBuffer, ReadableMapBuffer readableMapBuffer2, ReadableMapBuffer readableMapBuffer3, float f3, float f4, float f5, float f6, float[] fArr) {
        ReactContext reactContextL;
        if (i3 > 0) {
            n1.g gVarG = this.mMountingManager.g(i3, "measure");
            if (gVarG.u()) {
                return 0L;
            }
            reactContextL = gVarG.l();
        } else {
            reactContextL = this.mReactApplicationContext;
        }
        return this.mMountingManager.n(reactContextL, str, readableMapBuffer, readableMapBuffer2, readableMapBuffer3, InterfaceC0618a.c(f3, f4), InterfaceC0618a.b(f3, f4), InterfaceC0618a.c(f5, f6), InterfaceC0618a.b(f5, f6), fArr);
    }

    private void preallocateView(int i3, int i4, String str, Object obj, Object obj2, boolean z3) {
        this.mMountItemDispatcher.c(com.facebook.react.fabric.mounting.mountitems.g.e(i3, i4, str, (ReadableMap) obj, (A0) obj2, z3));
    }

    private void scheduleMountItem(MountItem mountItem, int i3, long j3, long j4, long j5, long j6, long j7, long j8, long j9, int i4) {
        long jUptimeMillis = SystemClock.uptimeMillis();
        boolean z3 = mountItem instanceof com.facebook.react.fabric.mounting.mountitems.a;
        boolean z4 = (z3 && !((com.facebook.react.fabric.mounting.mountitems.a) mountItem).a()) || !(z3 || mountItem == null);
        for (Iterator<UIManagerListener> it = this.mListeners.iterator(); it.hasNext(); it = it) {
            it.next().didScheduleMountItems(this);
        }
        if (z3) {
            this.mCommitStartTime = j3;
            this.mLayoutTime = j7 - j6;
            this.mFinishTransactionCPPTime = j9 - j8;
            this.mFinishTransactionTime = jUptimeMillis - j8;
            this.mDispatchViewUpdatesTime = SystemClock.uptimeMillis();
        }
        if (z4) {
            this.mMountItemDispatcher.b(mountItem);
            c cVar = new c(this.mReactApplicationContext);
            if (UiThreadUtil.isOnUiThread()) {
                cVar.run();
            }
        }
        if (z3) {
            ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_COMMIT_START, null, i3, j3);
            ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_FINISH_TRANSACTION_START, null, i3, j8);
            ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_FINISH_TRANSACTION_END, null, i3, j9);
            ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_DIFF_START, null, i3, j4);
            ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_DIFF_END, null, i3, j5);
            ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_LAYOUT_START, null, i3, j6);
            ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_LAYOUT_END, null, i3, j7);
            ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_LAYOUT_AFFECTED_NODES, null, i3, j7, i4);
            ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_COMMIT_END, null, i3);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.facebook.react.bridge.UIManager
    @Deprecated
    public <T extends View> int addRootView(T t3, WritableMap writableMap) {
        String str = TAG;
        ReactSoftExceptionLogger.logSoftException(str, new P("Do not call addRootView in Fabric; it is unsupported. Call startSurface instead."));
        InterfaceC0447o0 interfaceC0447o0 = (InterfaceC0447o0) t3;
        int rootViewTag = interfaceC0447o0.getRootViewTag();
        this.mMountingManager.r(rootViewTag, new B0(this.mReactApplicationContext, t3.getContext(), interfaceC0447o0.getSurfaceID(), rootViewTag), t3);
        String jSModuleName = interfaceC0447o0.getJSModuleName();
        if (C0670b.e()) {
            Y.a.d(str, "Starting surface for module: %s and reactTag: %d", jSModuleName, Integer.valueOf(rootViewTag));
        }
        this.mBinding.startSurface(rootViewTag, jSModuleName, (NativeMap) writableMap);
        return rootViewTag;
    }

    public void addUIBlock(InterfaceC0606a interfaceC0606a) {
        if (C0670b.p()) {
            getInteropUIBlockListener().a(interfaceC0606a);
        }
    }

    @Override // com.facebook.react.bridge.UIManager
    public void addUIManagerEventListener(UIManagerListener uIManagerListener) {
        this.mListeners.add(uIManagerListener);
    }

    public void attachRootView(InterfaceC0637b interfaceC0637b, View view) {
        this.mMountingManager.a(interfaceC0637b.getSurfaceId(), view, new B0(this.mReactApplicationContext, view.getContext(), interfaceC0637b.a(), interfaceC0637b.getSurfaceId()));
        interfaceC0637b.h(true);
    }

    public void clearJSResponder() {
        this.mMountItemDispatcher.b(new e());
    }

    com.facebook.react.fabric.mounting.mountitems.c createDispatchCommandMountItemForInterop(int i3, int i4, String str, ReadableArray readableArray) {
        try {
            return com.facebook.react.fabric.mounting.mountitems.g.b(i3, i4, Integer.parseInt(str), readableArray);
        } catch (NumberFormatException unused) {
            return com.facebook.react.fabric.mounting.mountitems.g.c(i3, i4, str, readableArray);
        }
    }

    @Override // com.facebook.react.bridge.UIManager
    @Deprecated
    public void dispatchCommand(int i3, int i4, ReadableArray readableArray) {
        throw new UnsupportedOperationException("dispatchCommand called without surfaceId - Fabric dispatchCommand must be called through Fabric JSI API");
    }

    public void experimental_prefetchResource(String str, int i3, int i4, ReadableMapBuffer readableMapBuffer) {
        this.mMountingManager.d(this.mReactApplicationContext, str, i3, i4, readableMapBuffer);
    }

    public int getColor(int i3, String[] strArr) {
        B0 b0L = this.mMountingManager.g(i3, "getColor").l();
        if (b0L == null) {
            return 0;
        }
        for (String str : strArr) {
            Integer numResolveResourcePath = ColorPropConverter.resolveResourcePath(b0L, str);
            if (numResolveResourcePath != null) {
                return numResolveResourcePath.intValue();
            }
        }
        return 0;
    }

    @Override // com.facebook.react.bridge.UIManager
    public EventDispatcher getEventDispatcher() {
        return this.mEventDispatcher;
    }

    @Override // com.facebook.react.bridge.PerformanceCounter
    public Map<String, Long> getPerformanceCounters() {
        HashMap map = new HashMap();
        map.put("CommitStartTime", Long.valueOf(this.mCommitStartTime));
        map.put("LayoutTime", Long.valueOf(this.mLayoutTime));
        map.put("DispatchViewUpdatesTime", Long.valueOf(this.mDispatchViewUpdatesTime));
        map.put("RunStartTime", Long.valueOf(this.mMountItemDispatcher.o()));
        map.put("BatchedExecutionTime", Long.valueOf(this.mMountItemDispatcher.n()));
        map.put("FinishFabricTransactionTime", Long.valueOf(this.mFinishTransactionTime));
        map.put("FinishFabricTransactionCPPTime", Long.valueOf(this.mFinishTransactionCPPTime));
        return map;
    }

    public boolean getThemeData(int i3, float[] fArr) {
        n1.g gVarF = this.mMountingManager.f(i3);
        B0 b0L = gVarF != null ? gVarF.l() : null;
        if (b0L == null) {
            Y.a.K(TAG, "Couldn't get context for surfaceId %d in getThemeData", Integer.valueOf(i3));
            return false;
        }
        float[] fArrA = H0.a(b0L);
        fArr[0] = fArrA[0];
        fArr[1] = fArrA[1];
        fArr[2] = fArrA[2];
        fArr[3] = fArrA[3];
        return true;
    }

    @Override // com.facebook.react.bridge.UIManager
    public void initialize() {
        this.mEventDispatcher.f(2, new FabricEventEmitter(this));
        this.mEventDispatcher.d(this.mBatchEventDispatchedListener);
        if (C0670b.e()) {
            com.facebook.react.fabric.a aVar = new com.facebook.react.fabric.a();
            this.mDevToolsReactPerfLogger = aVar;
            aVar.a(FABRIC_PERF_LOGGER);
            ReactMarker.addFabricListener(this.mDevToolsReactPerfLogger);
        }
        if (C0670b.p()) {
            this.mReactApplicationContext.internal_registerInteropModule(RCTEventEmitter.class, new InteropEventEmitter(this.mReactApplicationContext));
        }
    }

    @Override // com.facebook.react.bridge.UIManager
    public void invalidate() {
        String str = TAG;
        Y.a.s(str, "FabricUIManager.invalidate");
        com.facebook.react.fabric.a aVar = this.mDevToolsReactPerfLogger;
        if (aVar != null) {
            aVar.d(FABRIC_PERF_LOGGER);
            ReactMarker.removeFabricListener(this.mDevToolsReactPerfLogger);
        }
        if (this.mDestroyed) {
            ReactSoftExceptionLogger.logSoftException(str, new IllegalStateException("Cannot double-destroy FabricUIManager"));
            return;
        }
        this.mDestroyed = true;
        this.mEventDispatcher.e(this.mBatchEventDispatchedListener);
        this.mEventDispatcher.g(2);
        this.mReactApplicationContext.unregisterComponentCallbacks(this.mViewManagerRegistry);
        this.mViewManagerRegistry.f();
        this.mReactApplicationContext.removeLifecycleEventListener(this);
        onHostPause();
        this.mBinding.j();
        this.mBinding = null;
        R0.b();
        if (C0670b.c()) {
            return;
        }
        this.mEventDispatcher.c();
    }

    @Override // com.facebook.react.bridge.UIManager
    public void markActiveTouchForTag(int i3, int i4) {
        n1.g gVarF = this.mMountingManager.f(i3);
        if (gVarF != null) {
            gVarF.y(i4);
        }
    }

    public void onAllAnimationsComplete() {
        this.mDriveCxxAnimations = false;
    }

    public void onAnimationStarted() {
        this.mDriveCxxAnimations = true;
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostDestroy() {
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostPause() {
        this.mDispatchUIFrameCallback.b();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostResume() {
        this.mDispatchUIFrameCallback.c();
    }

    public void onRequestEventBeat() {
        this.mEventDispatcher.h();
    }

    public void prependUIBlock(InterfaceC0606a interfaceC0606a) {
        if (C0670b.p()) {
            getInteropUIBlockListener().b(interfaceC0606a);
        }
    }

    @Override // com.facebook.react.bridge.PerformanceCounter
    public void profileNextBatch() {
    }

    @Override // com.facebook.react.bridge.UIManager
    public void receiveEvent(int i3, String str, WritableMap writableMap) {
        receiveEvent(-1, i3, str, false, writableMap, 2);
    }

    @Override // com.facebook.react.bridge.UIManager
    public void removeUIManagerEventListener(UIManagerListener uIManagerListener) {
        this.mListeners.remove(uIManagerListener);
    }

    @Override // com.facebook.react.bridge.UIManager
    @Deprecated
    public String resolveCustomDirectEventName(String str) {
        if (str == null) {
            return null;
        }
        if (!str.startsWith("top")) {
            return str;
        }
        return "on" + str.substring(3);
    }

    @Override // com.facebook.react.bridge.UIManager
    public View resolveView(int i3) {
        UiThreadUtil.assertOnUiThread();
        n1.g gVarH = this.mMountingManager.h(i3);
        if (gVarH == null) {
            return null;
        }
        return gVarH.p(i3);
    }

    @Override // com.facebook.react.bridge.UIManager
    public void sendAccessibilityEvent(int i3, int i4) {
        this.mMountItemDispatcher.b(com.facebook.react.fabric.mounting.mountitems.g.f(-1, i3, i4));
    }

    public void sendAccessibilityEventFromJS(int i3, int i4, String str) {
        int i5;
        if ("focus".equals(str)) {
            i5 = 8;
        } else if ("windowStateChange".equals(str)) {
            i5 = 32;
        } else if ("click".equals(str)) {
            i5 = 1;
        } else {
            if (!"viewHoverEnter".equals(str)) {
                throw new IllegalArgumentException("sendAccessibilityEventFromJS: invalid eventType " + str);
            }
            i5 = 128;
        }
        this.mMountItemDispatcher.b(com.facebook.react.fabric.mounting.mountitems.g.f(i3, i4, i5));
    }

    void setBinding(FabricUIManagerBinding fabricUIManagerBinding) {
        this.mBinding = fabricUIManagerBinding;
    }

    public void setJSResponder(int i3, int i4, int i5, boolean z3) {
        this.mMountItemDispatcher.b(new d(i3, i4, i5, z3));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.facebook.react.bridge.UIManager
    public <T extends View> int startSurface(T t3, String str, WritableMap writableMap, int i3, int i4) {
        int rootViewTag = ((InterfaceC0447o0) t3).getRootViewTag();
        Context context = t3.getContext();
        B0 b02 = new B0(this.mReactApplicationContext, context, str, rootViewTag);
        if (C0670b.e()) {
            Y.a.d(TAG, "Starting surface for module: %s and reactTag: %d", str, Integer.valueOf(rootViewTag));
        }
        this.mMountingManager.r(rootViewTag, b02, t3);
        Point pointB = UiThreadUtil.isOnUiThread() ? C0464x0.b(t3) : new Point(0, 0);
        this.mBinding.startSurfaceWithConstraints(rootViewTag, str, (NativeMap) writableMap, InterfaceC0618a.a(i3), InterfaceC0618a.d(i3), InterfaceC0618a.a(i4), InterfaceC0618a.d(i4), pointB.x, pointB.y, com.facebook.react.modules.i18nmanager.a.f().i(context), com.facebook.react.modules.i18nmanager.a.f().d(context));
        return rootViewTag;
    }

    public void stopSurface(InterfaceC0637b interfaceC0637b) {
        if (!interfaceC0637b.isRunning()) {
            ReactSoftExceptionLogger.logSoftException(TAG, new IllegalStateException("Trying to stop surface that hasn't started yet"));
            return;
        }
        this.mMountingManager.s(interfaceC0637b.getSurfaceId());
        if (!(interfaceC0637b instanceof SurfaceHandlerBinding)) {
            throw new IllegalArgumentException("Invalid SurfaceHandler");
        }
        this.mBinding.stopSurfaceWithSurfaceHandler((SurfaceHandlerBinding) interfaceC0637b);
    }

    @Override // com.facebook.react.bridge.UIManager
    public void sweepActiveTouchForTag(int i3, int i4) {
        n1.g gVarF = this.mMountingManager.f(i3);
        if (gVarF != null) {
            gVarF.J(i4);
        }
    }

    @Override // com.facebook.react.bridge.UIManager
    public void synchronouslyUpdateViewOnUIThread(int i3, ReadableMap readableMap) {
        UiThreadUtil.assertOnUiThread();
        int i4 = this.mCurrentSynchronousCommitNumber;
        this.mCurrentSynchronousCommitNumber = i4 + 1;
        b bVar = new b(i3, readableMap);
        if (!this.mMountingManager.k(i3)) {
            this.mMountItemDispatcher.b(bVar);
            return;
        }
        ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_UPDATE_UI_MAIN_THREAD_START, null, i4);
        if (C0670b.e()) {
            Y.a.d(TAG, "SynchronouslyUpdateViewOnUIThread for tag %d: %s", Integer.valueOf(i3), IS_DEVELOPMENT_ENVIRONMENT ? readableMap.toHashMap().toString() : "<hidden>");
        }
        bVar.execute(this.mMountingManager);
        ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_UPDATE_UI_MAIN_THREAD_END, null, i4);
    }

    @Override // com.facebook.react.bridge.UIManager
    public void updateRootLayoutSpecs(int i3, int i4, int i5, int i6, int i7) {
        boolean z3;
        boolean zD;
        if (C0670b.e()) {
            Y.a.c(TAG, "Updating Root Layout Specs for [%d]", Integer.valueOf(i3));
        }
        n1.g gVarF = this.mMountingManager.f(i3);
        if (gVarF == null) {
            ReactSoftExceptionLogger.logSoftException(TAG, new P("Cannot updateRootLayoutSpecs on surfaceId that does not exist: " + i3));
            return;
        }
        B0 b0L = gVarF.l();
        if (b0L != null) {
            boolean zI = com.facebook.react.modules.i18nmanager.a.f().i(b0L);
            zD = com.facebook.react.modules.i18nmanager.a.f().d(b0L);
            z3 = zI;
        } else {
            z3 = false;
            zD = false;
        }
        this.mBinding.setConstraints(i3, InterfaceC0618a.a(i4), InterfaceC0618a.d(i4), InterfaceC0618a.a(i5), InterfaceC0618a.d(i5), i6, i7, z3, zD);
    }

    private long measure(int i3, String str, ReadableMap readableMap, ReadableMap readableMap2, ReadableMap readableMap3, float f3, float f4, float f5, float f6, float[] fArr) {
        ReactContext reactContextL;
        if (i3 > 0) {
            n1.g gVarG = this.mMountingManager.g(i3, "measure");
            if (gVarG.u()) {
                return 0L;
            }
            reactContextL = gVarG.l();
        } else {
            reactContextL = this.mReactApplicationContext;
        }
        return this.mMountingManager.m(reactContextL, str, readableMap, readableMap2, readableMap3, InterfaceC0618a.c(f3, f4), InterfaceC0618a.b(f3, f4), InterfaceC0618a.c(f5, f6), InterfaceC0618a.b(f5, f6), fArr);
    }

    @Override // com.facebook.react.bridge.UIManager
    @Deprecated
    public void dispatchCommand(int i3, String str, ReadableArray readableArray) {
        throw new UnsupportedOperationException("dispatchCommand called without surfaceId - Fabric dispatchCommand must be called through Fabric JSI API");
    }

    @Override // com.facebook.react.bridge.UIManager
    public void receiveEvent(int i3, int i4, String str, WritableMap writableMap) {
        receiveEvent(i3, i4, str, false, writableMap, 2);
    }

    @Deprecated
    public void dispatchCommand(int i3, int i4, int i5, ReadableArray readableArray) {
        this.mMountItemDispatcher.d(com.facebook.react.fabric.mounting.mountitems.g.b(i3, i4, i5, readableArray));
    }

    public void receiveEvent(int i3, int i4, String str, boolean z3, WritableMap writableMap, int i5) {
        receiveEvent(i3, i4, str, z3, writableMap, i5, false);
    }

    @Override // P1.p
    public void receiveEvent(int i3, int i4, String str, boolean z3, WritableMap writableMap, int i5, boolean z4) {
        if (C0542a.f9423b && i3 == -1) {
            Y.a.d(TAG, "Emitted event without surfaceId: [%d] %s", Integer.valueOf(i4), str);
        }
        if (this.mDestroyed) {
            Y.a.m(TAG, "Attempted to receiveEvent after destruction");
            return;
        }
        EventEmitterWrapper eventEmitterWrapperE = this.mMountingManager.e(i3, i4);
        if (eventEmitterWrapperE == null) {
            if (this.mMountingManager.k(i4)) {
                this.mMountingManager.c(i3, i4, str, z3, writableMap, i5);
                return;
            }
            Y.a.s(TAG, "Unable to invoke event: " + str + " for reactTag: " + i4);
            return;
        }
        if (z4) {
            UiThreadUtil.assertOnUiThread();
            if (this.mSynchronousEvents.add(new h(i3, i4, str))) {
                eventEmitterWrapperE.dispatchEventSynchronously(str, writableMap);
                return;
            }
            return;
        }
        if (z3) {
            eventEmitterWrapperE.dispatchUnique(str, writableMap);
        } else {
            eventEmitterWrapperE.dispatch(str, writableMap, i5);
        }
    }

    public void dispatchCommand(int i3, int i4, String str, ReadableArray readableArray) {
        if (C0670b.p()) {
            this.mMountItemDispatcher.d(createDispatchCommandMountItemForInterop(i3, i4, str, readableArray));
        } else {
            this.mMountItemDispatcher.d(com.facebook.react.fabric.mounting.mountitems.g.c(i3, i4, str, readableArray));
        }
    }

    @Override // com.facebook.react.bridge.UIManager
    public void stopSurface(int i3) {
        this.mMountingManager.s(i3);
        this.mBinding.stopSurface(i3);
    }

    public void startSurface(InterfaceC0637b interfaceC0637b, Context context, View view) {
        int iA = C0449p0.a();
        this.mMountingManager.r(iA, new B0(this.mReactApplicationContext, context, interfaceC0637b.a(), iA), view);
        if (interfaceC0637b instanceof SurfaceHandlerBinding) {
            this.mBinding.startSurfaceWithSurfaceHandler(iA, (SurfaceHandlerBinding) interfaceC0637b, view != null);
            return;
        }
        throw new IllegalArgumentException("Invalid SurfaceHandler");
    }
}

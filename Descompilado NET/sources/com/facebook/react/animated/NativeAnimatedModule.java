package com.facebook.react.animated;

import a1.C0210a;
import com.facebook.fbreact.specs.NativeAnimatedModuleSpec;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.bridge.UIManagerListener;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.b;
import com.facebook.react.uimanager.C0421b0;
import com.facebook.react.uimanager.F0;
import com.facebook.react.uimanager.H0;
import com.facebook.react.uimanager.M;
import com.facebook.react.uimanager.UIManagerModule;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import r1.C0670b;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = NativeAnimatedModuleSpec.NAME)
public class NativeAnimatedModule extends NativeAnimatedModuleSpec implements LifecycleEventListener, UIManagerListener {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final boolean ANIMATED_MODULE_DEBUG = false;
    private final M mAnimatedFrameCallback;
    private boolean mBatchingControlledByJS;
    private volatile long mCurrentBatchNumber;
    private volatile long mCurrentFrameNumber;
    private boolean mEnqueuedAnimationOnFrame;
    private boolean mInitializedForFabric;
    private boolean mInitializedForNonFabric;
    private final AtomicReference<com.facebook.react.animated.o> mNodesManager;
    private int mNumFabricAnimations;
    private int mNumNonFabricAnimations;
    private final A mOperations;
    private final A mPreOperations;
    private final com.facebook.react.modules.core.b mReactChoreographer;
    private int mUIManagerType;

    private class A {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Queue f6278a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private B f6279b;

        /* JADX WARN: Removed duplicated region for block: B:15:0x002c  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0036 A[EDGE_INSN: B:22:0x0036->B:18:0x0036 BREAK  A[LOOP:0: B:6:0x000d->B:19:0x0037], SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private java.util.List b(long r6) {
            /*
                r5 = this;
                boolean r0 = r5.d()
                r1 = 0
                if (r0 == 0) goto L8
                return r1
            L8:
                java.util.ArrayList r0 = new java.util.ArrayList
                r0.<init>()
            Ld:
                com.facebook.react.animated.NativeAnimatedModule$B r2 = r5.f6279b
                if (r2 == 0) goto L21
                long r2 = r2.b()
                int r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
                if (r2 <= 0) goto L1a
                goto L36
            L1a:
                com.facebook.react.animated.NativeAnimatedModule$B r2 = r5.f6279b
                r0.add(r2)
                r5.f6279b = r1
            L21:
                java.util.Queue r2 = r5.f6278a
                java.lang.Object r2 = r2.poll()
                com.facebook.react.animated.NativeAnimatedModule$B r2 = (com.facebook.react.animated.NativeAnimatedModule.B) r2
                if (r2 != 0) goto L2c
                goto L36
            L2c:
                long r3 = r2.b()
                int r3 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1))
                if (r3 <= 0) goto L37
                r5.f6279b = r2
            L36:
                return r0
            L37:
                r0.add(r2)
                goto Ld
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.animated.NativeAnimatedModule.A.b(long):java.util.List");
        }

        void a(B b4) {
            this.f6278a.add(b4);
        }

        void c(long j3, com.facebook.react.animated.o oVar) {
            List listB = b(j3);
            if (listB != null) {
                Iterator it = listB.iterator();
                while (it.hasNext()) {
                    ((B) it.next()).a(oVar);
                }
            }
        }

        boolean d() {
            return this.f6278a.isEmpty() && this.f6279b == null;
        }

        private A() {
            this.f6278a = new ConcurrentLinkedQueue();
            this.f6279b = null;
        }
    }

    private abstract class B {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        long f6281a;

        abstract void a(com.facebook.react.animated.o oVar);

        public long b() {
            return this.f6281a;
        }

        public void c(long j3) {
            this.f6281a = j3;
        }

        private B() {
            this.f6281a = -1L;
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.animated.NativeAnimatedModule$a, reason: case insensitive filesystem */
    class C0366a extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6283c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ double f6284d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C0366a(int i3, double d4) {
            super();
            this.f6283c = i3;
            this.f6284d = d4;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.w(this.f6283c, this.f6284d);
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.animated.NativeAnimatedModule$b, reason: case insensitive filesystem */
    class C0367b extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6286c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ double f6287d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C0367b(int i3, double d4) {
            super();
            this.f6286c = i3;
            this.f6287d = d4;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.v(this.f6286c, this.f6287d);
        }
    }

    class c extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6289c;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(int i3) {
            super();
            this.f6289c = i3;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.k(this.f6289c);
        }
    }

    class d extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6291c;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(int i3) {
            super();
            this.f6291c = i3;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.j(this.f6291c);
        }
    }

    class e extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6293c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f6294d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ ReadableMap f6295e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        final /* synthetic */ Callback f6296f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        e(int i3, int i4, ReadableMap readableMap, Callback callback) {
            super();
            this.f6293c = i3;
            this.f6294d = i4;
            this.f6295e = readableMap;
            this.f6296f = callback;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.x(this.f6293c, this.f6294d, this.f6295e, this.f6296f);
        }
    }

    class f extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6298c;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        f(int i3) {
            super();
            this.f6298c = i3;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.z(this.f6298c);
        }
    }

    class g extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6300c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f6301d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        g(int i3, int i4) {
            super();
            this.f6300c = i3;
            this.f6301d = i4;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.e(this.f6300c, this.f6301d);
        }
    }

    class h extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6303c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f6304d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        h(int i3, int i4) {
            super();
            this.f6303c = i3;
            this.f6304d = i4;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.h(this.f6303c, this.f6304d);
        }
    }

    class i extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6306c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f6307d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        i(int i3, int i4) {
            super();
            this.f6306c = i3;
            this.f6307d = i4;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.d(this.f6306c, this.f6307d);
        }
    }

    class j extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6309c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f6310d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        j(int i3, int i4) {
            super();
            this.f6309c = i3;
            this.f6310d = i4;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.g(this.f6309c, this.f6310d);
        }
    }

    class k extends M {
        k(ReactContext reactContext) {
            super(reactContext);
        }

        @Override // com.facebook.react.uimanager.M
        protected void a(long j3) {
            try {
                NativeAnimatedModule.this.mEnqueuedAnimationOnFrame = false;
                com.facebook.react.animated.o nodesManager = NativeAnimatedModule.this.getNodesManager();
                if (nodesManager != null && nodesManager.p()) {
                    nodesManager.u(j3);
                }
                if (nodesManager != null && NativeAnimatedModule.this.mReactChoreographer != null) {
                    if (!C0670b.m() || nodesManager.p()) {
                        NativeAnimatedModule.this.enqueueFrameCallback();
                    }
                }
            } catch (Exception e4) {
                throw new RuntimeException(e4);
            }
        }
    }

    class l extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6313c;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        l(int i3) {
            super();
            this.f6313c = i3;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.t(this.f6313c);
        }
    }

    class m extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6315c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ String f6316d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ ReadableMap f6317e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        m(int i3, String str, ReadableMap readableMap) {
            super();
            this.f6315c = i3;
            this.f6316d = str;
            this.f6317e = readableMap;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.c(this.f6315c, this.f6316d, this.f6317e);
        }
    }

    class n extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6319c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ String f6320d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f6321e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        n(int i3, String str, int i4) {
            super();
            this.f6319c = i3;
            this.f6320d = str;
            this.f6321e = i4;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.s(this.f6319c, this.f6320d, this.f6321e);
        }
    }

    class o extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6323c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ Callback f6324d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        o(int i3, Callback callback) {
            super();
            this.f6323c = i3;
            this.f6324d = callback;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.n(this.f6323c, this.f6324d);
        }
    }

    class p extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6326c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ ReadableArray f6327d;

        class a implements com.facebook.react.animated.c {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ int f6329a;

            a(int i3) {
                this.f6329a = i3;
            }

            @Override // com.facebook.react.animated.c
            public void a(double d4) {
                WritableMap writableMapCreateMap = Arguments.createMap();
                writableMapCreateMap.putInt("tag", this.f6329a);
                writableMapCreateMap.putDouble("value", d4);
                ReactApplicationContext reactApplicationContextIfActiveOrWarn = NativeAnimatedModule.this.getReactApplicationContextIfActiveOrWarn();
                if (reactApplicationContextIfActiveOrWarn != null) {
                    reactApplicationContextIfActiveOrWarn.emitDeviceEvent("onAnimatedValueUpdate", writableMapCreateMap);
                }
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        p(int i3, ReadableArray readableArray) {
            super();
            this.f6326c = i3;
            this.f6327d = readableArray;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            NativeAnimatedModule.this.getReactApplicationContextIfActiveOrWarn();
            int i3 = 0;
            while (i3 < this.f6326c) {
                int i4 = i3 + 1;
                switch (q.f6331a[z.b(this.f6327d.getInt(i3)).ordinal()]) {
                    case 1:
                        i3 += 2;
                        oVar.n(this.f6327d.getInt(i4), null);
                        break;
                    case 2:
                        i3 += 2;
                        int i5 = this.f6327d.getInt(i4);
                        oVar.y(i5, new a(i5));
                        break;
                    case 3:
                        i3 += 2;
                        oVar.B(this.f6327d.getInt(i4));
                        break;
                    case 4:
                        i3 += 2;
                        oVar.z(this.f6327d.getInt(i4));
                        break;
                    case 5:
                        i3 += 2;
                        oVar.k(this.f6327d.getInt(i4));
                        break;
                    case 6:
                        i3 += 2;
                        oVar.j(this.f6327d.getInt(i4));
                        break;
                    case 7:
                        i3 += 2;
                        oVar.t(this.f6327d.getInt(i4));
                        break;
                    case 8:
                        i3 += 2;
                        oVar.i(this.f6327d.getInt(i4));
                        break;
                    case 9:
                    case 10:
                        i3 += 2;
                        break;
                    case 11:
                        int i6 = i3 + 2;
                        i3 += 3;
                        oVar.f(this.f6327d.getInt(i4), this.f6327d.getMap(i6));
                        break;
                    case 12:
                        int i7 = i3 + 2;
                        i3 += 3;
                        oVar.C(this.f6327d.getInt(i4), this.f6327d.getMap(i7));
                        break;
                    case 13:
                        int i8 = i3 + 2;
                        i3 += 3;
                        oVar.e(this.f6327d.getInt(i4), this.f6327d.getInt(i8));
                        break;
                    case 14:
                        int i9 = i3 + 2;
                        i3 += 3;
                        oVar.h(this.f6327d.getInt(i4), this.f6327d.getInt(i9));
                        break;
                    case 15:
                        int i10 = i3 + 2;
                        i3 += 3;
                        oVar.w(this.f6327d.getInt(i4), this.f6327d.getDouble(i10));
                        break;
                    case 16:
                        int i11 = i3 + 2;
                        i3 += 3;
                        oVar.w(this.f6327d.getInt(i4), this.f6327d.getDouble(i11));
                        break;
                    case 17:
                        int i12 = i3 + 2;
                        int i13 = this.f6327d.getInt(i4);
                        i3 += 3;
                        int i14 = this.f6327d.getInt(i12);
                        NativeAnimatedModule.this.decrementInFlightAnimationsForViewTag(i14);
                        oVar.g(i13, i14);
                        break;
                    case 18:
                        if (C0670b.m()) {
                            NativeAnimatedModule.this.enqueueFrameCallback();
                        }
                        int i15 = this.f6327d.getInt(i4);
                        int i16 = i3 + 3;
                        int i17 = this.f6327d.getInt(i3 + 2);
                        i3 += 4;
                        oVar.x(i15, i17, this.f6327d.getMap(i16), null);
                        break;
                    case 19:
                        int i18 = this.f6327d.getInt(i4);
                        NativeAnimatedModule.this.decrementInFlightAnimationsForViewTag(i18);
                        int i19 = i3 + 3;
                        String string = this.f6327d.getString(i3 + 2);
                        i3 += 4;
                        oVar.s(i18, string, this.f6327d.getInt(i19));
                        break;
                    case 20:
                        int i20 = i3 + 2;
                        i3 += 3;
                        oVar.d(this.f6327d.getInt(i4), this.f6327d.getInt(i20));
                        break;
                    case 21:
                        int i21 = this.f6327d.getInt(i4);
                        int i22 = i3 + 3;
                        String string2 = this.f6327d.getString(i3 + 2);
                        i3 += 4;
                        oVar.c(i21, string2, this.f6327d.getMap(i22));
                        break;
                    default:
                        throw new IllegalArgumentException("Batch animation execution op: unknown op code");
                }
            }
        }
    }

    static /* synthetic */ class q {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f6331a;

        static {
            int[] iArr = new int[z.values().length];
            f6331a = iArr;
            try {
                iArr[z.OP_CODE_GET_VALUE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f6331a[z.OP_START_LISTENING_TO_ANIMATED_NODE_VALUE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f6331a[z.OP_STOP_LISTENING_TO_ANIMATED_NODE_VALUE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f6331a[z.OP_CODE_STOP_ANIMATION.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f6331a[z.OP_CODE_FLATTEN_ANIMATED_NODE_OFFSET.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f6331a[z.OP_CODE_EXTRACT_ANIMATED_NODE_OFFSET.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f6331a[z.OP_CODE_RESTORE_DEFAULT_VALUES.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f6331a[z.OP_CODE_DROP_ANIMATED_NODE.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f6331a[z.OP_CODE_ADD_LISTENER.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f6331a[z.OP_CODE_REMOVE_LISTENERS.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                f6331a[z.OP_CODE_CREATE_ANIMATED_NODE.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                f6331a[z.OP_CODE_UPDATE_ANIMATED_NODE_CONFIG.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                f6331a[z.OP_CODE_CONNECT_ANIMATED_NODES.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                f6331a[z.OP_CODE_DISCONNECT_ANIMATED_NODES.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                f6331a[z.OP_CODE_SET_ANIMATED_NODE_VALUE.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                f6331a[z.OP_CODE_SET_ANIMATED_NODE_OFFSET.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                f6331a[z.OP_CODE_DISCONNECT_ANIMATED_NODE_FROM_VIEW.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                f6331a[z.OP_CODE_START_ANIMATING_NODE.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                f6331a[z.OP_CODE_REMOVE_ANIMATED_EVENT_FROM_VIEW.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                f6331a[z.OP_CODE_CONNECT_ANIMATED_NODE_TO_VIEW.ordinal()] = 20;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                f6331a[z.OP_CODE_ADD_ANIMATED_EVENT_TO_VIEW.ordinal()] = 21;
            } catch (NoSuchFieldError unused21) {
            }
        }
    }

    class r implements F0 {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ long f6332a;

        r(long j3) {
            this.f6332a = j3;
        }

        @Override // com.facebook.react.uimanager.F0
        public void a(C0421b0 c0421b0) {
            NativeAnimatedModule.this.mPreOperations.c(this.f6332a, NativeAnimatedModule.this.getNodesManager());
        }
    }

    class s implements F0 {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ long f6334a;

        s(long j3) {
            this.f6334a = j3;
        }

        @Override // com.facebook.react.uimanager.F0
        public void a(C0421b0 c0421b0) {
            NativeAnimatedModule.this.mOperations.c(this.f6334a, NativeAnimatedModule.this.getNodesManager());
        }
    }

    class t extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6336c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ ReadableMap f6337d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        t(int i3, ReadableMap readableMap) {
            super();
            this.f6336c = i3;
            this.f6337d = readableMap;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.f(this.f6336c, this.f6337d);
        }
    }

    class u extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6339c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ ReadableMap f6340d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        u(int i3, ReadableMap readableMap) {
            super();
            this.f6339c = i3;
            this.f6340d = readableMap;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.C(this.f6339c, this.f6340d);
        }
    }

    class v implements com.facebook.react.animated.c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f6342a;

        v(int i3) {
            this.f6342a = i3;
        }

        @Override // com.facebook.react.animated.c
        public void a(double d4) {
            WritableMap writableMapCreateMap = Arguments.createMap();
            writableMapCreateMap.putInt("tag", this.f6342a);
            writableMapCreateMap.putDouble("value", d4);
            ReactApplicationContext reactApplicationContextIfActiveOrWarn = NativeAnimatedModule.this.getReactApplicationContextIfActiveOrWarn();
            if (reactApplicationContextIfActiveOrWarn != null) {
                reactApplicationContextIfActiveOrWarn.emitDeviceEvent("onAnimatedValueUpdate", writableMapCreateMap);
            }
        }
    }

    class w extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6344c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ com.facebook.react.animated.c f6345d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        w(int i3, com.facebook.react.animated.c cVar) {
            super();
            this.f6344c = i3;
            this.f6345d = cVar;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.y(this.f6344c, this.f6345d);
        }
    }

    class x extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6347c;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        x(int i3) {
            super();
            this.f6347c = i3;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.B(this.f6347c);
        }
    }

    class y extends B {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f6349c;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        y(int i3) {
            super();
            this.f6349c = i3;
        }

        @Override // com.facebook.react.animated.NativeAnimatedModule.B
        public void a(com.facebook.react.animated.o oVar) {
            oVar.i(this.f6349c);
        }
    }

    private enum z {
        OP_CODE_CREATE_ANIMATED_NODE(1),
        OP_CODE_UPDATE_ANIMATED_NODE_CONFIG(2),
        OP_CODE_GET_VALUE(3),
        OP_START_LISTENING_TO_ANIMATED_NODE_VALUE(4),
        OP_STOP_LISTENING_TO_ANIMATED_NODE_VALUE(5),
        OP_CODE_CONNECT_ANIMATED_NODES(6),
        OP_CODE_DISCONNECT_ANIMATED_NODES(7),
        OP_CODE_START_ANIMATING_NODE(8),
        OP_CODE_STOP_ANIMATION(9),
        OP_CODE_SET_ANIMATED_NODE_VALUE(10),
        OP_CODE_SET_ANIMATED_NODE_OFFSET(11),
        OP_CODE_FLATTEN_ANIMATED_NODE_OFFSET(12),
        OP_CODE_EXTRACT_ANIMATED_NODE_OFFSET(13),
        OP_CODE_CONNECT_ANIMATED_NODE_TO_VIEW(14),
        OP_CODE_DISCONNECT_ANIMATED_NODE_FROM_VIEW(15),
        OP_CODE_RESTORE_DEFAULT_VALUES(16),
        OP_CODE_DROP_ANIMATED_NODE(17),
        OP_CODE_ADD_ANIMATED_EVENT_TO_VIEW(18),
        OP_CODE_REMOVE_ANIMATED_EVENT_FROM_VIEW(19),
        OP_CODE_ADD_LISTENER(20),
        OP_CODE_REMOVE_LISTENERS(21);


        /* JADX INFO: renamed from: x, reason: collision with root package name */
        private static z[] f6372x = null;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int f6374b;

        z(int i3) {
            this.f6374b = i3;
        }

        public static z b(int i3) {
            if (f6372x == null) {
                f6372x = values();
            }
            return f6372x[i3 - 1];
        }
    }

    public NativeAnimatedModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.mOperations = new A();
        this.mPreOperations = new A();
        this.mNodesManager = new AtomicReference<>();
        this.mBatchingControlledByJS = false;
        this.mInitializedForFabric = false;
        this.mInitializedForNonFabric = false;
        this.mEnqueuedAnimationOnFrame = false;
        this.mUIManagerType = 1;
        this.mNumFabricAnimations = 0;
        this.mNumNonFabricAnimations = 0;
        this.mReactChoreographer = com.facebook.react.modules.core.b.h();
        this.mAnimatedFrameCallback = new k(reactApplicationContext);
    }

    private void addOperation(B b4) {
        b4.c(this.mCurrentBatchNumber);
        this.mOperations.a(b4);
    }

    private void addPreOperation(B b4) {
        b4.c(this.mCurrentBatchNumber);
        this.mPreOperations.a(b4);
    }

    private void addUnbatchedOperation(B b4) {
        b4.c(-1L);
        this.mOperations.a(b4);
    }

    private void clearFrameCallback() {
        ((com.facebook.react.modules.core.b) C0210a.c(this.mReactChoreographer)).n(b.a.f6925e, this.mAnimatedFrameCallback);
        this.mEnqueuedAnimationOnFrame = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void decrementInFlightAnimationsForViewTag(int i3) {
        if (M1.a.a(i3) == 2) {
            this.mNumFabricAnimations--;
        } else {
            this.mNumNonFabricAnimations--;
        }
        int i4 = this.mNumNonFabricAnimations;
        if (i4 == 0 && this.mNumFabricAnimations > 0 && this.mUIManagerType != 2) {
            this.mUIManagerType = 2;
        } else {
            if (this.mNumFabricAnimations != 0 || i4 <= 0 || this.mUIManagerType == 1) {
                return;
            }
            this.mUIManagerType = 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enqueueFrameCallback() {
        if (this.mEnqueuedAnimationOnFrame) {
            return;
        }
        ((com.facebook.react.modules.core.b) C0210a.c(this.mReactChoreographer)).k(b.a.f6925e, this.mAnimatedFrameCallback);
        this.mEnqueuedAnimationOnFrame = true;
    }

    private void initializeLifecycleEventListenersForViewTag(int i3) {
        UIManager uIManagerG;
        int iA = M1.a.a(i3);
        this.mUIManagerType = iA;
        if (iA == 2) {
            this.mNumFabricAnimations++;
        } else {
            this.mNumNonFabricAnimations++;
        }
        com.facebook.react.animated.o nodesManager = getNodesManager();
        if (nodesManager != null) {
            nodesManager.q(this.mUIManagerType);
        } else {
            ReactSoftExceptionLogger.logSoftException(NativeAnimatedModuleSpec.NAME, new RuntimeException("initializeLifecycleEventListenersForViewTag could not get NativeAnimatedNodesManager"));
        }
        if (this.mUIManagerType == 2) {
            if (this.mInitializedForFabric) {
                return;
            }
        } else if (this.mInitializedForNonFabric) {
            return;
        }
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        if (reactApplicationContext == null || (uIManagerG = H0.g(reactApplicationContext, this.mUIManagerType)) == null) {
            return;
        }
        uIManagerG.addUIManagerEventListener(this);
        if (this.mUIManagerType == 2) {
            this.mInitializedForFabric = true;
        } else {
            this.mInitializedForNonFabric = true;
        }
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void addAnimatedEventToView(double d4, String str, ReadableMap readableMap) {
        int i3 = (int) d4;
        initializeLifecycleEventListenersForViewTag(i3);
        addOperation(new m(i3, str, readableMap));
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void addListener(String str) {
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void connectAnimatedNodeToView(double d4, double d5) {
        int i3 = (int) d5;
        initializeLifecycleEventListenersForViewTag(i3);
        addOperation(new i((int) d4, i3));
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void connectAnimatedNodes(double d4, double d5) {
        addOperation(new g((int) d4, (int) d5));
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void createAnimatedNode(double d4, ReadableMap readableMap) {
        addOperation(new t((int) d4, readableMap));
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void didDispatchMountItems(UIManager uIManager) {
        if (this.mUIManagerType != 2) {
            return;
        }
        long j3 = this.mCurrentBatchNumber - 1;
        if (!this.mBatchingControlledByJS) {
            this.mCurrentFrameNumber++;
            if (this.mCurrentFrameNumber - this.mCurrentBatchNumber > 2) {
                this.mCurrentBatchNumber = this.mCurrentFrameNumber;
                j3 = this.mCurrentBatchNumber;
            }
        }
        this.mPreOperations.c(j3, getNodesManager());
        this.mOperations.c(j3, getNodesManager());
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void didMountItems(UIManager uIManager) {
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void didScheduleMountItems(UIManager uIManager) {
        this.mCurrentFrameNumber++;
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void disconnectAnimatedNodeFromView(double d4, double d5) {
        int i3 = (int) d5;
        decrementInFlightAnimationsForViewTag(i3);
        addOperation(new j((int) d4, i3));
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void disconnectAnimatedNodes(double d4, double d5) {
        addOperation(new h((int) d4, (int) d5));
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void dropAnimatedNode(double d4) {
        addOperation(new y((int) d4));
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void extractAnimatedNodeOffset(double d4) {
        addOperation(new d((int) d4));
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void finishOperationBatch() {
        this.mBatchingControlledByJS = false;
        this.mCurrentBatchNumber++;
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void flattenAnimatedNodeOffset(double d4) {
        addOperation(new c((int) d4));
    }

    public com.facebook.react.animated.o getNodesManager() {
        ReactApplicationContext reactApplicationContextIfActiveOrWarn;
        if (this.mNodesManager.get() == null && (reactApplicationContextIfActiveOrWarn = getReactApplicationContextIfActiveOrWarn()) != null) {
            com.facebook.jni.a.a(this.mNodesManager, null, new com.facebook.react.animated.o(reactApplicationContextIfActiveOrWarn));
        }
        return this.mNodesManager.get();
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void getValue(double d4, Callback callback) {
        addOperation(new o((int) d4, callback));
    }

    @Override // com.facebook.react.bridge.BaseJavaModule, com.facebook.react.bridge.NativeModule, com.facebook.react.turbomodule.core.interfaces.TurboModule
    public void initialize() {
        super.initialize();
        getReactApplicationContext().addLifecycleEventListener(this);
    }

    @Override // com.facebook.react.bridge.BaseJavaModule, com.facebook.react.bridge.NativeModule, com.facebook.react.turbomodule.core.interfaces.TurboModule
    public void invalidate() {
        super.invalidate();
        getReactApplicationContext().removeLifecycleEventListener(this);
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostDestroy() {
        clearFrameCallback();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostPause() {
        clearFrameCallback();
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostResume() {
        enqueueFrameCallback();
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void queueAndExecuteBatchedOperations(ReadableArray readableArray) {
        int size = readableArray.size();
        int i3 = 0;
        while (i3 < size) {
            int i4 = i3 + 1;
            switch (q.f6331a[z.b(readableArray.getInt(i3)).ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    i3 += 2;
                    continue;
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                    i3 += 3;
                    continue;
                case 18:
                case 19:
                    break;
                case 20:
                    int i5 = i3 + 2;
                    i3 += 3;
                    initializeLifecycleEventListenersForViewTag(readableArray.getInt(i5));
                    continue;
                case 21:
                    initializeLifecycleEventListenersForViewTag(readableArray.getInt(i4));
                    break;
                default:
                    throw new IllegalArgumentException("Batch animation execution op: fetching viewTag: unknown op code");
            }
            i3 += 4;
        }
        startOperationBatch();
        addUnbatchedOperation(new p(size, readableArray));
        finishOperationBatch();
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void removeAnimatedEventFromView(double d4, String str, double d5) {
        int i3 = (int) d4;
        decrementInFlightAnimationsForViewTag(i3);
        addOperation(new n(i3, str, (int) d5));
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void removeListeners(double d4) {
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void restoreDefaultValues(double d4) {
        addPreOperation(new l((int) d4));
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void setAnimatedNodeOffset(double d4, double d5) {
        addOperation(new C0367b((int) d4, d5));
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void setAnimatedNodeValue(double d4, double d5) {
        addOperation(new C0366a((int) d4, d5));
    }

    public void setNodesManager(com.facebook.react.animated.o oVar) {
        this.mNodesManager.set(oVar);
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void startAnimatingNode(double d4, double d5, ReadableMap readableMap, Callback callback) {
        addUnbatchedOperation(new e((int) d4, (int) d5, readableMap, callback));
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void startListeningToAnimatedNodeValue(double d4) {
        int i3 = (int) d4;
        addOperation(new w(i3, new v(i3)));
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void startOperationBatch() {
        this.mBatchingControlledByJS = true;
        this.mCurrentBatchNumber++;
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void stopAnimation(double d4) {
        addOperation(new f((int) d4));
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void stopListeningToAnimatedNodeValue(double d4) {
        addOperation(new x((int) d4));
    }

    @Override // com.facebook.fbreact.specs.NativeAnimatedModuleSpec
    public void updateAnimatedNodeConfig(double d4, ReadableMap readableMap) {
        addOperation(new u((int) d4, readableMap));
    }

    public void userDrivenScrollEnded(int i3) {
        com.facebook.react.animated.o oVar = this.mNodesManager.get();
        if (oVar == null) {
            return;
        }
        Set setM = oVar.m(i3, "topScrollEnded");
        if (setM.isEmpty()) {
            return;
        }
        WritableArray writableArrayCreateArray = Arguments.createArray();
        Iterator it = setM.iterator();
        while (it.hasNext()) {
            writableArrayCreateArray.pushInt(((Integer) it.next()).intValue());
        }
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putArray("tags", writableArrayCreateArray);
        ReactApplicationContext reactApplicationContextIfActiveOrWarn = getReactApplicationContextIfActiveOrWarn();
        if (reactApplicationContextIfActiveOrWarn != null) {
            reactApplicationContextIfActiveOrWarn.emitDeviceEvent("onUserDrivenAnimationEnded", writableMapCreateMap);
        }
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void willDispatchViewUpdates(UIManager uIManager) {
        if ((this.mOperations.d() && this.mPreOperations.d()) || this.mUIManagerType == 2) {
            return;
        }
        long j3 = this.mCurrentBatchNumber;
        this.mCurrentBatchNumber = 1 + j3;
        r rVar = new r(j3);
        s sVar = new s(j3);
        UIManagerModule uIManagerModule = (UIManagerModule) uIManager;
        uIManagerModule.prependUIBlock(rVar);
        uIManagerModule.addUIBlock(sVar);
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void willMountItems(UIManager uIManager) {
    }
}

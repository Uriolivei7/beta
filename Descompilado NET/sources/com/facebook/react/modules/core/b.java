package com.facebook.react.modules.core;

import D2.h;
import a1.C0210a;
import android.view.Choreographer;
import com.facebook.react.bridge.UiThreadUtil;
import java.util.ArrayDeque;
import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import q1.InterfaceC0651b;
import r2.r;
import w2.AbstractC0764a;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final C0107b f6916f = new C0107b(null);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static b f6917g;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private InterfaceC0651b.a f6918a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ArrayDeque[] f6919b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f6920c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f6921d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Choreographer.FrameCallback f6922e;

    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    public static final class a {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public static final a f6923c = new a("PERF_MARKERS", 0, 0);

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public static final a f6924d = new a("DISPATCH_UI", 1, 1);

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public static final a f6925e = new a("NATIVE_ANIMATED_MODULE", 2, 2);

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public static final a f6926f = new a("TIMERS_EVENTS", 3, 3);

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public static final a f6927g = new a("IDLE_EVENT", 4, 4);

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private static final /* synthetic */ a[] f6928h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private static final /* synthetic */ EnumEntries f6929i;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int f6930b;

        static {
            a[] aVarArrA = a();
            f6928h = aVarArrA;
            f6929i = AbstractC0764a.a(aVarArrA);
        }

        private a(String str, int i3, int i4) {
            this.f6930b = i4;
        }

        private static final /* synthetic */ a[] a() {
            return new a[]{f6923c, f6924d, f6925e, f6926f, f6927g};
        }

        public static EnumEntries b() {
            return f6929i;
        }

        public static a valueOf(String str) {
            return (a) Enum.valueOf(a.class, str);
        }

        public static a[] values() {
            return (a[]) f6928h.clone();
        }

        public final int c() {
            return this.f6930b;
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.modules.core.b$b, reason: collision with other inner class name */
    public static final class C0107b {
        public /* synthetic */ C0107b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final b a() {
            b bVar = b.f6917g;
            if (bVar != null) {
                return bVar;
            }
            throw new IllegalStateException("ReactChoreographer needs to be initialized.");
        }

        public final void b(InterfaceC0651b interfaceC0651b) {
            h.f(interfaceC0651b, "choreographerProvider");
            if (b.f6917g == null) {
                b.f6917g = new b(interfaceC0651b, null);
            }
        }

        private C0107b() {
        }
    }

    public /* synthetic */ b(InterfaceC0651b interfaceC0651b, DefaultConstructorMarker defaultConstructorMarker) {
        this(interfaceC0651b);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void d(b bVar, InterfaceC0651b interfaceC0651b) {
        bVar.f6918a = interfaceC0651b.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void g(b bVar, long j3) {
        synchronized (bVar.f6919b) {
            try {
                bVar.f6921d = false;
                int length = bVar.f6919b.length;
                for (int i3 = 0; i3 < length; i3++) {
                    ArrayDeque arrayDeque = bVar.f6919b[i3];
                    int size = arrayDeque.size();
                    for (int i4 = 0; i4 < size; i4++) {
                        Choreographer.FrameCallback frameCallback = (Choreographer.FrameCallback) arrayDeque.pollFirst();
                        if (frameCallback != null) {
                            frameCallback.doFrame(j3);
                            bVar.f6920c--;
                        } else {
                            Y.a.m("ReactNative", "Tried to execute non-existent frame callback");
                        }
                    }
                }
                bVar.j();
                r rVar = r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static final b h() {
        return f6916f.a();
    }

    public static final void i(InterfaceC0651b interfaceC0651b) {
        f6916f.b(interfaceC0651b);
    }

    private final void j() {
        C0210a.a(this.f6920c >= 0);
        if (this.f6920c == 0 && this.f6921d) {
            InterfaceC0651b.a aVar = this.f6918a;
            if (aVar != null) {
                aVar.b(this.f6922e);
            }
            this.f6921d = false;
        }
    }

    private final void l() {
        if (this.f6921d) {
            return;
        }
        InterfaceC0651b.a aVar = this.f6918a;
        if (aVar == null) {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: B1.j
                @Override // java.lang.Runnable
                public final void run() {
                    com.facebook.react.modules.core.b.m(this.f87b);
                }
            });
        } else {
            aVar.a(this.f6922e);
            this.f6921d = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void m(b bVar) {
        synchronized (bVar.f6919b) {
            bVar.l();
            r rVar = r.f10584a;
        }
    }

    public final void k(a aVar, Choreographer.FrameCallback frameCallback) {
        h.f(aVar, "type");
        h.f(frameCallback, "callback");
        synchronized (this.f6919b) {
            this.f6919b[aVar.c()].addLast(frameCallback);
            boolean z3 = true;
            int i3 = this.f6920c + 1;
            this.f6920c = i3;
            if (i3 <= 0) {
                z3 = false;
            }
            C0210a.a(z3);
            l();
            r rVar = r.f10584a;
        }
    }

    public final void n(a aVar, Choreographer.FrameCallback frameCallback) {
        h.f(aVar, "type");
        synchronized (this.f6919b) {
            try {
                if (this.f6919b[aVar.c()].removeFirstOccurrence(frameCallback)) {
                    this.f6920c--;
                    j();
                } else {
                    Y.a.m("ReactNative", "Tried to remove non-existent frame callback");
                }
                r rVar = r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private b(final InterfaceC0651b interfaceC0651b) {
        int size = a.b().size();
        ArrayDeque[] arrayDequeArr = new ArrayDeque[size];
        for (int i3 = 0; i3 < size; i3++) {
            arrayDequeArr[i3] = new ArrayDeque();
        }
        this.f6919b = arrayDequeArr;
        this.f6922e = new Choreographer.FrameCallback() { // from class: B1.h
            @Override // android.view.Choreographer.FrameCallback
            public final void doFrame(long j3) {
                com.facebook.react.modules.core.b.g(this.f84a, j3);
            }
        };
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: B1.i
            @Override // java.lang.Runnable
            public final void run() {
                com.facebook.react.modules.core.b.d(this.f85b, interfaceC0651b);
            }
        });
    }
}

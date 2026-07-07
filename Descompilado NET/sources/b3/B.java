package b3;

import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes.dex */
public final class B {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final int f5599c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final AtomicReference[] f5600d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final B f5601e = new B();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final int f5597a = 65536;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final A f5598b = new A(new byte[0], 0, 0, false, false);

    static {
        int iHighestOneBit = Integer.highestOneBit((Runtime.getRuntime().availableProcessors() * 2) - 1);
        f5599c = iHighestOneBit;
        AtomicReference[] atomicReferenceArr = new AtomicReference[iHighestOneBit];
        for (int i3 = 0; i3 < iHighestOneBit; i3++) {
            atomicReferenceArr[i3] = new AtomicReference();
        }
        f5600d = atomicReferenceArr;
    }

    private B() {
    }

    private final AtomicReference a() {
        Thread threadCurrentThread = Thread.currentThread();
        D2.h.e(threadCurrentThread, "Thread.currentThread()");
        return f5600d[(int) (threadCurrentThread.getId() & (((long) f5599c) - 1))];
    }

    public static final void b(A a4) {
        D2.h.f(a4, "segment");
        if (!(a4.f5595f == null && a4.f5596g == null)) {
            throw new IllegalArgumentException("Failed requirement.");
        }
        if (a4.f5593d) {
            return;
        }
        AtomicReference atomicReferenceA = f5601e.a();
        A a5 = (A) atomicReferenceA.get();
        if (a5 == f5598b) {
            return;
        }
        int i3 = a5 != null ? a5.f5592c : 0;
        if (i3 >= f5597a) {
            return;
        }
        a4.f5595f = a5;
        a4.f5591b = 0;
        a4.f5592c = i3 + 8192;
        if (com.facebook.jni.a.a(atomicReferenceA, a5, a4)) {
            return;
        }
        a4.f5595f = null;
    }

    public static final A c() {
        AtomicReference atomicReferenceA = f5601e.a();
        A a4 = f5598b;
        A a5 = (A) atomicReferenceA.getAndSet(a4);
        if (a5 == a4) {
            return new A();
        }
        if (a5 == null) {
            atomicReferenceA.set(null);
            return new A();
        }
        atomicReferenceA.set(a5.f5595f);
        a5.f5595f = null;
        a5.f5592c = 0;
        return a5;
    }
}

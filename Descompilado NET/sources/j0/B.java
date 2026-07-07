package J0;

import android.os.Process;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class B implements ThreadFactory {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f479a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f480b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final boolean f481c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final AtomicInteger f482d;

    public B(int i3) {
        this(i3, null, false, 6, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void b(B b4, Runnable runnable) {
        D2.h.f(b4, "this$0");
        D2.h.f(runnable, "$runnable");
        try {
            Process.setThreadPriority(b4.f479a);
        } catch (Throwable unused) {
        }
        runnable.run();
    }

    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(final Runnable runnable) {
        String str;
        D2.h.f(runnable, "runnable");
        Runnable runnable2 = new Runnable() { // from class: J0.A
            @Override // java.lang.Runnable
            public final void run() {
                B.b(this.f477b, runnable);
            }
        };
        if (this.f481c) {
            str = this.f480b + "-" + this.f482d.getAndIncrement();
        } else {
            str = this.f480b;
        }
        return new Thread(runnable2, str);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public B(int i3, String str) {
        this(i3, str, false, 4, null);
        D2.h.f(str, "prefix");
    }

    public B(int i3, String str, boolean z3) {
        D2.h.f(str, "prefix");
        this.f479a = i3;
        this.f480b = str;
        this.f481c = z3;
        this.f482d = new AtomicInteger(1);
    }

    public /* synthetic */ B(int i3, String str, boolean z3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(i3, (i4 & 2) != 0 ? "PriorityThreadFactory" : str, (i4 & 4) != 0 ? true : z3);
    }
}

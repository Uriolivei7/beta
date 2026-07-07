package Q2;

import D2.h;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public abstract class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private d f1857a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private long f1858b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f1859c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final boolean f1860d;

    public a(String str, boolean z3) {
        h.f(str, "name");
        this.f1859c = str;
        this.f1860d = z3;
        this.f1858b = -1L;
    }

    public final boolean a() {
        return this.f1860d;
    }

    public final String b() {
        return this.f1859c;
    }

    public final long c() {
        return this.f1858b;
    }

    public final d d() {
        return this.f1857a;
    }

    public final void e(d dVar) {
        h.f(dVar, "queue");
        d dVar2 = this.f1857a;
        if (dVar2 == dVar) {
            return;
        }
        if (!(dVar2 == null)) {
            throw new IllegalStateException("task is in multiple queues");
        }
        this.f1857a = dVar;
    }

    public abstract long f();

    public final void g(long j3) {
        this.f1858b = j3;
    }

    public String toString() {
        return this.f1859c;
    }

    public /* synthetic */ a(String str, boolean z3, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i3 & 2) != 0 ? true : z3);
    }
}

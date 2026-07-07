package r2;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Lazy;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
final class l implements Lazy, Serializable {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final a f10574e = new a(null);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final AtomicReferenceFieldUpdater f10575f = AtomicReferenceFieldUpdater.newUpdater(l.class, Object.class, "c");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private volatile C2.a f10576b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile Object f10577c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Object f10578d;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public l(C2.a aVar) {
        D2.h.f(aVar, "initializer");
        this.f10576b = aVar;
        o oVar = o.f10582a;
        this.f10577c = oVar;
        this.f10578d = oVar;
    }

    public boolean a() {
        return this.f10577c != o.f10582a;
    }

    @Override // kotlin.Lazy
    public Object getValue() {
        Object obj = this.f10577c;
        o oVar = o.f10582a;
        if (obj != oVar) {
            return obj;
        }
        C2.a aVar = this.f10576b;
        if (aVar != null) {
            Object objA = aVar.a();
            if (androidx.concurrent.futures.b.a(f10575f, this, oVar, objA)) {
                this.f10576b = null;
                return objA;
            }
        }
        return this.f10577c;
    }

    public String toString() {
        return a() ? String.valueOf(getValue()) : "Lazy value not initialized yet.";
    }
}

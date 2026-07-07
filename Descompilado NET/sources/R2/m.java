package r2;

import java.io.Serializable;
import kotlin.Lazy;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
final class m implements Lazy, Serializable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private C2.a f10579b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile Object f10580c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Object f10581d;

    public m(C2.a aVar, Object obj) {
        D2.h.f(aVar, "initializer");
        this.f10579b = aVar;
        this.f10580c = o.f10582a;
        this.f10581d = obj == null ? this : obj;
    }

    public boolean a() {
        return this.f10580c != o.f10582a;
    }

    @Override // kotlin.Lazy
    public Object getValue() {
        Object objA;
        Object obj = this.f10580c;
        o oVar = o.f10582a;
        if (obj != oVar) {
            return obj;
        }
        synchronized (this.f10581d) {
            objA = this.f10580c;
            if (objA == oVar) {
                C2.a aVar = this.f10579b;
                D2.h.c(aVar);
                objA = aVar.a();
                this.f10580c = objA;
                this.f10579b = null;
            }
        }
        return objA;
    }

    public String toString() {
        return a() ? String.valueOf(getValue()) : "Lazy value not initialized yet.";
    }

    public /* synthetic */ m(C2.a aVar, Object obj, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(aVar, (i3 & 2) != 0 ? null : obj);
    }
}

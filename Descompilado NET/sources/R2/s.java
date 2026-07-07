package r2;

import java.io.Serializable;
import kotlin.Lazy;

/* JADX INFO: loaded from: classes.dex */
public final class s implements Lazy, Serializable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private C2.a f10585b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Object f10586c;

    public s(C2.a aVar) {
        D2.h.f(aVar, "initializer");
        this.f10585b = aVar;
        this.f10586c = o.f10582a;
    }

    public boolean a() {
        return this.f10586c != o.f10582a;
    }

    @Override // kotlin.Lazy
    public Object getValue() {
        if (this.f10586c == o.f10582a) {
            C2.a aVar = this.f10585b;
            D2.h.c(aVar);
            this.f10586c = aVar.a();
            this.f10585b = null;
        }
        return this.f10586c;
    }

    public String toString() {
        return a() ? String.valueOf(getValue()) : "Lazy value not initialized yet.";
    }
}

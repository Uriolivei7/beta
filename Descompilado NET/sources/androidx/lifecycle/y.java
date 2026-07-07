package androidx.lifecycle;

import F.a;
import android.os.Bundle;
import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.E;
import androidx.savedstate.a;

/* JADX INFO: loaded from: classes.dex */
public abstract class y {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a.b f5368a = new b();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a.b f5369b = new c();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a.b f5370c = new a();

    public static final class a implements a.b {
        a() {
        }
    }

    public static final class b implements a.b {
        b() {
        }
    }

    public static final class c implements a.b {
        c() {
        }
    }

    static final class d extends D2.i implements C2.l {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public static final d f5371c = new d();

        d() {
            super(1);
        }

        @Override // C2.l
        /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
        public final A d(F.a aVar) {
            D2.h.f(aVar, "$this$initializer");
            return new A();
        }
    }

    public static final x a(F.a aVar) {
        D2.h.f(aVar, "<this>");
        G.d dVar = (G.d) aVar.a(f5368a);
        if (dVar == null) {
            throw new IllegalArgumentException("CreationExtras must have a value by `SAVED_STATE_REGISTRY_OWNER_KEY`");
        }
        H h3 = (H) aVar.a(f5369b);
        if (h3 == null) {
            throw new IllegalArgumentException("CreationExtras must have a value by `VIEW_MODEL_STORE_OWNER_KEY`");
        }
        Bundle bundle = (Bundle) aVar.a(f5370c);
        String str = (String) aVar.a(E.c.f5279d);
        if (str != null) {
            return b(dVar, h3, str, bundle);
        }
        throw new IllegalArgumentException("CreationExtras must have a value by `VIEW_MODEL_KEY`");
    }

    private static final x b(G.d dVar, H h3, String str, Bundle bundle) {
        z zVarD = d(dVar);
        A aE = e(h3);
        x xVar = (x) aE.f().get(str);
        if (xVar != null) {
            return xVar;
        }
        x xVarA = x.f5361f.a(zVarD.b(str), bundle);
        aE.f().put(str, xVarA);
        return xVarA;
    }

    public static final void c(G.d dVar) {
        D2.h.f(dVar, "<this>");
        AbstractC0299g.b bVarB = dVar.t().b();
        if (bVarB != AbstractC0299g.b.INITIALIZED && bVarB != AbstractC0299g.b.CREATED) {
            throw new IllegalArgumentException("Failed requirement.");
        }
        if (dVar.b().c("androidx.lifecycle.internal.SavedStateHandlesProvider") == null) {
            z zVar = new z(dVar.b(), (H) dVar);
            dVar.b().h("androidx.lifecycle.internal.SavedStateHandlesProvider", zVar);
            dVar.t().a(new SavedStateHandleAttacher(zVar));
        }
    }

    public static final z d(G.d dVar) {
        D2.h.f(dVar, "<this>");
        a.c cVarC = dVar.b().c("androidx.lifecycle.internal.SavedStateHandlesProvider");
        z zVar = cVarC instanceof z ? (z) cVarC : null;
        if (zVar != null) {
            return zVar;
        }
        throw new IllegalStateException("enableSavedStateHandles() wasn't called prior to createSavedStateHandle() call");
    }

    public static final A e(H h3) {
        D2.h.f(h3, "<this>");
        F.c cVar = new F.c();
        cVar.a(D2.s.a(A.class), d.f5371c);
        return (A) new E(h3, cVar.b()).b("androidx.lifecycle.internal.SavedStateHandlesVM", A.class);
    }
}

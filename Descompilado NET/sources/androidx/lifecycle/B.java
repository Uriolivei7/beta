package androidx.lifecycle;

import android.app.Application;
import android.os.Bundle;
import androidx.lifecycle.E;
import java.lang.reflect.Constructor;

/* JADX INFO: loaded from: classes.dex */
public final class B extends E.d implements E.b {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Application f5253b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final E.b f5254c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Bundle f5255d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private AbstractC0299g f5256e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private androidx.savedstate.a f5257f;

    public B() {
        this.f5254c = new E.a();
    }

    @Override // androidx.lifecycle.E.b
    public D a(Class cls) {
        D2.h.f(cls, "modelClass");
        String canonicalName = cls.getCanonicalName();
        if (canonicalName != null) {
            return d(canonicalName, cls);
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    @Override // androidx.lifecycle.E.b
    public D b(Class cls, F.a aVar) {
        D2.h.f(cls, "modelClass");
        D2.h.f(aVar, "extras");
        String str = (String) aVar.a(E.c.f5279d);
        if (str == null) {
            throw new IllegalStateException("VIEW_MODEL_KEY must always be provided by ViewModelProvider");
        }
        if (aVar.a(y.f5368a) == null || aVar.a(y.f5369b) == null) {
            if (this.f5256e != null) {
                return d(str, cls);
            }
            throw new IllegalStateException("SAVED_STATE_REGISTRY_OWNER_KEY andVIEW_MODEL_STORE_OWNER_KEY must be provided in the creation extras tosuccessfully create a ViewModel.");
        }
        Application application = (Application) aVar.a(E.a.f5272h);
        boolean zIsAssignableFrom = C0293a.class.isAssignableFrom(cls);
        Constructor constructorC = (!zIsAssignableFrom || application == null) ? C.c(cls, C.f5259b) : C.c(cls, C.f5258a);
        return constructorC == null ? this.f5254c.b(cls, aVar) : (!zIsAssignableFrom || application == null) ? C.d(cls, constructorC, y.a(aVar)) : C.d(cls, constructorC, application, y.a(aVar));
    }

    @Override // androidx.lifecycle.E.d
    public void c(D d4) {
        D2.h.f(d4, "viewModel");
        if (this.f5256e != null) {
            androidx.savedstate.a aVar = this.f5257f;
            D2.h.c(aVar);
            AbstractC0299g abstractC0299g = this.f5256e;
            D2.h.c(abstractC0299g);
            LegacySavedStateHandleController.a(d4, aVar, abstractC0299g);
        }
    }

    public final D d(String str, Class cls) {
        D d4;
        Application application;
        D2.h.f(str, "key");
        D2.h.f(cls, "modelClass");
        AbstractC0299g abstractC0299g = this.f5256e;
        if (abstractC0299g == null) {
            throw new UnsupportedOperationException("SavedStateViewModelFactory constructed with empty constructor supports only calls to create(modelClass: Class<T>, extras: CreationExtras).");
        }
        boolean zIsAssignableFrom = C0293a.class.isAssignableFrom(cls);
        Constructor constructorC = (!zIsAssignableFrom || this.f5253b == null) ? C.c(cls, C.f5259b) : C.c(cls, C.f5258a);
        if (constructorC == null) {
            return this.f5253b != null ? this.f5254c.a(cls) : E.c.f5277b.a().a(cls);
        }
        androidx.savedstate.a aVar = this.f5257f;
        D2.h.c(aVar);
        SavedStateHandleController savedStateHandleControllerB = LegacySavedStateHandleController.b(aVar, abstractC0299g, str, this.f5255d);
        if (!zIsAssignableFrom || (application = this.f5253b) == null) {
            d4 = C.d(cls, constructorC, savedStateHandleControllerB.i());
        } else {
            D2.h.c(application);
            d4 = C.d(cls, constructorC, application, savedStateHandleControllerB.i());
        }
        d4.e("androidx.lifecycle.savedstate.vm.tag", savedStateHandleControllerB);
        return d4;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public B(Application application, G.d dVar) {
        this(application, dVar, null);
        D2.h.f(dVar, "owner");
    }

    public B(Application application, G.d dVar, Bundle bundle) {
        E.a aVar;
        D2.h.f(dVar, "owner");
        this.f5257f = dVar.b();
        this.f5256e = dVar.t();
        this.f5255d = bundle;
        this.f5253b = application;
        if (application != null) {
            aVar = E.a.f5270f.b(application);
        } else {
            aVar = new E.a();
        }
        this.f5254c = aVar;
    }
}

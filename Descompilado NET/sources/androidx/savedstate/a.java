package androidx.savedstate;

import D2.h;
import G.d;
import android.os.Bundle;
import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.InterfaceC0302j;
import androidx.lifecycle.l;
import androidx.savedstate.Recreator;
import java.util.Map;
import k.C0581b;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final b f5449g = new b(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f5451b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Bundle f5452c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f5453d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Recreator.b f5454e;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final C0581b f5450a = new C0581b();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f5455f = true;

    /* JADX INFO: renamed from: androidx.savedstate.a$a, reason: collision with other inner class name */
    public interface InterfaceC0083a {
        void a(d dVar);
    }

    private static final class b {
        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private b() {
        }
    }

    public interface c {
        Bundle a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void d(a aVar, l lVar, AbstractC0299g.a aVar2) {
        h.f(aVar, "this$0");
        h.f(lVar, "<anonymous parameter 0>");
        h.f(aVar2, "event");
        if (aVar2 == AbstractC0299g.a.ON_START) {
            aVar.f5455f = true;
        } else if (aVar2 == AbstractC0299g.a.ON_STOP) {
            aVar.f5455f = false;
        }
    }

    public final Bundle b(String str) {
        h.f(str, "key");
        if (!this.f5453d) {
            throw new IllegalStateException("You can consumeRestoredStateForKey only after super.onCreate of corresponding component");
        }
        Bundle bundle = this.f5452c;
        if (bundle == null) {
            return null;
        }
        Bundle bundle2 = bundle != null ? bundle.getBundle(str) : null;
        Bundle bundle3 = this.f5452c;
        if (bundle3 != null) {
            bundle3.remove(str);
        }
        Bundle bundle4 = this.f5452c;
        if (bundle4 == null || bundle4.isEmpty()) {
            this.f5452c = null;
        }
        return bundle2;
    }

    public final c c(String str) {
        h.f(str, "key");
        for (Map.Entry entry : this.f5450a) {
            h.e(entry, "components");
            String str2 = (String) entry.getKey();
            c cVar = (c) entry.getValue();
            if (h.b(str2, str)) {
                return cVar;
            }
        }
        return null;
    }

    public final void e(AbstractC0299g abstractC0299g) {
        h.f(abstractC0299g, "lifecycle");
        if (this.f5451b) {
            throw new IllegalStateException("SavedStateRegistry was already attached.");
        }
        abstractC0299g.a(new InterfaceC0302j() { // from class: G.b
            @Override // androidx.lifecycle.InterfaceC0302j
            public final void d(l lVar, AbstractC0299g.a aVar) {
                androidx.savedstate.a.d(this.f248a, lVar, aVar);
            }
        });
        this.f5451b = true;
    }

    public final void f(Bundle bundle) {
        if (!this.f5451b) {
            throw new IllegalStateException("You must call performAttach() before calling performRestore(Bundle).");
        }
        if (this.f5453d) {
            throw new IllegalStateException("SavedStateRegistry was already restored.");
        }
        this.f5452c = bundle != null ? bundle.getBundle("androidx.lifecycle.BundlableSavedStateRegistry.key") : null;
        this.f5453d = true;
    }

    public final void g(Bundle bundle) {
        h.f(bundle, "outBundle");
        Bundle bundle2 = new Bundle();
        Bundle bundle3 = this.f5452c;
        if (bundle3 != null) {
            bundle2.putAll(bundle3);
        }
        C0581b.d dVarE = this.f5450a.e();
        h.e(dVarE, "this.components.iteratorWithAdditions()");
        while (dVarE.hasNext()) {
            Map.Entry entry = (Map.Entry) dVarE.next();
            bundle2.putBundle((String) entry.getKey(), ((c) entry.getValue()).a());
        }
        if (bundle2.isEmpty()) {
            return;
        }
        bundle.putBundle("androidx.lifecycle.BundlableSavedStateRegistry.key", bundle2);
    }

    public final void h(String str, c cVar) {
        h.f(str, "key");
        h.f(cVar, "provider");
        if (((c) this.f5450a.i(str, cVar)) != null) {
            throw new IllegalArgumentException("SavedStateProvider with the given key is already registered");
        }
    }

    public final void i(Class cls) {
        h.f(cls, "clazz");
        if (!this.f5455f) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
        Recreator.b bVar = this.f5454e;
        if (bVar == null) {
            bVar = new Recreator.b(this);
        }
        this.f5454e = bVar;
        try {
            cls.getDeclaredConstructor(new Class[0]);
            Recreator.b bVar2 = this.f5454e;
            if (bVar2 != null) {
                String name = cls.getName();
                h.e(name, "clazz.name");
                bVar2.b(name);
            }
        } catch (NoSuchMethodException e4) {
            throw new IllegalArgumentException("Class " + cls.getSimpleName() + " must have default constructor in order to be automatically recreated", e4);
        }
    }
}

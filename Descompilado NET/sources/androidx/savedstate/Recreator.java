package androidx.savedstate;

import D2.h;
import G.d;
import android.os.Bundle;
import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.InterfaceC0302j;
import androidx.lifecycle.l;
import androidx.savedstate.a;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class Recreator implements InterfaceC0302j {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f5446b = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final d f5447a;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public static final class b implements a.c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Set f5448a;

        public b(androidx.savedstate.a aVar) {
            h.f(aVar, "registry");
            this.f5448a = new LinkedHashSet();
            aVar.h("androidx.savedstate.Restarter", this);
        }

        @Override // androidx.savedstate.a.c
        public Bundle a() {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("classes_to_restore", new ArrayList<>(this.f5448a));
            return bundle;
        }

        public final void b(String str) {
            h.f(str, "className");
            this.f5448a.add(str);
        }
    }

    public Recreator(d dVar) {
        h.f(dVar, "owner");
        this.f5447a = dVar;
    }

    private final void h(String str) {
        try {
            Class<? extends U> clsAsSubclass = Class.forName(str, false, Recreator.class.getClassLoader()).asSubclass(a.InterfaceC0083a.class);
            h.e(clsAsSubclass, "{\n                Class.…class.java)\n            }");
            try {
                Constructor declaredConstructor = clsAsSubclass.getDeclaredConstructor(new Class[0]);
                declaredConstructor.setAccessible(true);
                try {
                    Object objNewInstance = declaredConstructor.newInstance(new Object[0]);
                    h.e(objNewInstance, "{\n                constr…wInstance()\n            }");
                    ((a.InterfaceC0083a) objNewInstance).a(this.f5447a);
                } catch (Exception e4) {
                    throw new RuntimeException("Failed to instantiate " + str, e4);
                }
            } catch (NoSuchMethodException e5) {
                throw new IllegalStateException("Class " + clsAsSubclass.getSimpleName() + " must have default constructor in order to be automatically recreated", e5);
            }
        } catch (ClassNotFoundException e6) {
            throw new RuntimeException("Class " + str + " wasn't found", e6);
        }
    }

    @Override // androidx.lifecycle.InterfaceC0302j
    public void d(l lVar, AbstractC0299g.a aVar) {
        h.f(lVar, "source");
        h.f(aVar, "event");
        if (aVar != AbstractC0299g.a.ON_CREATE) {
            throw new AssertionError("Next event must be ON_CREATE");
        }
        lVar.t().c(this);
        Bundle bundleB = this.f5447a.b().b("androidx.savedstate.Restarter");
        if (bundleB == null) {
            return;
        }
        ArrayList<String> stringArrayList = bundleB.getStringArrayList("classes_to_restore");
        if (stringArrayList == null) {
            throw new IllegalStateException("Bundle with restored state for the component \"androidx.savedstate.Restarter\" must contain list of strings by the key \"classes_to_restore\"");
        }
        Iterator<String> it = stringArrayList.iterator();
        while (it.hasNext()) {
            h(it.next());
        }
    }
}

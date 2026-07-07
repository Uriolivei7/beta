package C;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.x;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0696D;
import s2.AbstractC0717n;
import s2.L;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final c f91a = new c();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static C0001c f92b = C0001c.f103d;

    public enum a {
        PENALTY_LOG,
        PENALTY_DEATH,
        DETECT_FRAGMENT_REUSE,
        DETECT_FRAGMENT_TAG_USAGE,
        DETECT_RETAIN_INSTANCE_USAGE,
        DETECT_SET_USER_VISIBLE_HINT,
        DETECT_TARGET_FRAGMENT_USAGE,
        DETECT_WRONG_FRAGMENT_CONTAINER
    }

    public interface b {
    }

    /* JADX INFO: renamed from: C.c$c, reason: collision with other inner class name */
    public static final class C0001c {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public static final a f102c = new a(null);

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public static final C0001c f103d = new C0001c(L.b(), null, AbstractC0696D.f());

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Set f104a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Map f105b;

        /* JADX INFO: renamed from: C.c$c$a */
        public static final class a {
            public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private a() {
            }
        }

        public C0001c(Set<? extends a> set, b bVar, Map<String, ? extends Set<Class<? extends g>>> map) {
            D2.h.f(set, "flags");
            D2.h.f(map, "allowedViolations");
            this.f104a = set;
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            for (Map.Entry<String, ? extends Set<Class<? extends g>>> entry : map.entrySet()) {
                linkedHashMap.put(entry.getKey(), entry.getValue());
            }
            this.f105b = linkedHashMap;
        }

        public final Set a() {
            return this.f104a;
        }

        public final b b() {
            return null;
        }

        public final Map c() {
            return this.f105b;
        }
    }

    private c() {
    }

    private final C0001c b(Fragment fragment) {
        while (fragment != null) {
            if (fragment.W()) {
                x xVarE = fragment.E();
                D2.h.e(xVarE, "declaringFragment.parentFragmentManager");
                if (xVarE.z0() != null) {
                    C0001c c0001cZ0 = xVarE.z0();
                    D2.h.c(c0001cZ0);
                    return c0001cZ0;
                }
            }
            fragment = fragment.D();
        }
        return f92b;
    }

    private final void c(C0001c c0001c, final g gVar) {
        Fragment fragmentA = gVar.a();
        final String name = fragmentA.getClass().getName();
        if (c0001c.a().contains(a.PENALTY_LOG)) {
            Log.d("FragmentStrictMode", "Policy violation in " + name, gVar);
        }
        c0001c.b();
        if (c0001c.a().contains(a.PENALTY_DEATH)) {
            j(fragmentA, new Runnable() { // from class: C.b
                @Override // java.lang.Runnable
                public final void run() {
                    c.d(name, gVar);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void d(String str, g gVar) {
        D2.h.f(gVar, "$violation");
        Log.e("FragmentStrictMode", "Policy violation with PENALTY_DEATH in " + str, gVar);
        throw gVar;
    }

    private final void e(g gVar) {
        if (x.G0(3)) {
            Log.d("FragmentManager", "StrictMode violation in " + gVar.a().getClass().getName(), gVar);
        }
    }

    public static final void f(Fragment fragment, String str) {
        D2.h.f(fragment, "fragment");
        D2.h.f(str, "previousFragmentId");
        C.a aVar = new C.a(fragment, str);
        c cVar = f91a;
        cVar.e(aVar);
        C0001c c0001cB = cVar.b(fragment);
        if (c0001cB.a().contains(a.DETECT_FRAGMENT_REUSE) && cVar.k(c0001cB, fragment.getClass(), aVar.getClass())) {
            cVar.c(c0001cB, aVar);
        }
    }

    public static final void g(Fragment fragment, ViewGroup viewGroup) {
        D2.h.f(fragment, "fragment");
        d dVar = new d(fragment, viewGroup);
        c cVar = f91a;
        cVar.e(dVar);
        C0001c c0001cB = cVar.b(fragment);
        if (c0001cB.a().contains(a.DETECT_FRAGMENT_TAG_USAGE) && cVar.k(c0001cB, fragment.getClass(), dVar.getClass())) {
            cVar.c(c0001cB, dVar);
        }
    }

    public static final void h(Fragment fragment) {
        D2.h.f(fragment, "fragment");
        e eVar = new e(fragment);
        c cVar = f91a;
        cVar.e(eVar);
        C0001c c0001cB = cVar.b(fragment);
        if (c0001cB.a().contains(a.DETECT_TARGET_FRAGMENT_USAGE) && cVar.k(c0001cB, fragment.getClass(), eVar.getClass())) {
            cVar.c(c0001cB, eVar);
        }
    }

    public static final void i(Fragment fragment, ViewGroup viewGroup) {
        D2.h.f(fragment, "fragment");
        D2.h.f(viewGroup, "container");
        h hVar = new h(fragment, viewGroup);
        c cVar = f91a;
        cVar.e(hVar);
        C0001c c0001cB = cVar.b(fragment);
        if (c0001cB.a().contains(a.DETECT_WRONG_FRAGMENT_CONTAINER) && cVar.k(c0001cB, fragment.getClass(), hVar.getClass())) {
            cVar.c(c0001cB, hVar);
        }
    }

    private final void j(Fragment fragment, Runnable runnable) {
        if (!fragment.W()) {
            runnable.run();
            return;
        }
        Handler handlerM = fragment.E().t0().m();
        D2.h.e(handlerM, "fragment.parentFragmentManager.host.handler");
        if (D2.h.b(handlerM.getLooper(), Looper.myLooper())) {
            runnable.run();
        } else {
            handlerM.post(runnable);
        }
    }

    private final boolean k(C0001c c0001c, Class cls, Class cls2) {
        Set set = (Set) c0001c.c().get(cls.getName());
        if (set == null) {
            return true;
        }
        if (D2.h.b(cls2.getSuperclass(), g.class) || !AbstractC0717n.J(set, cls2.getSuperclass())) {
            return !set.contains(cls2);
        }
        return false;
    }
}

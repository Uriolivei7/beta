package androidx.fragment.app;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.os.d;
import androidx.core.view.Z;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
abstract class L {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ViewGroup f5027a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final ArrayList f5028b = new ArrayList();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final ArrayList f5029c = new ArrayList();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    boolean f5030d = false;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    boolean f5031e = false;

    class a implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ d f5032b;

        a(d dVar) {
            this.f5032b = dVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (L.this.f5028b.contains(this.f5032b)) {
                this.f5032b.e().a(this.f5032b.f().f4920J);
            }
        }
    }

    class b implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ d f5034b;

        b(d dVar) {
            this.f5034b = dVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            L.this.f5028b.remove(this.f5034b);
            L.this.f5029c.remove(this.f5034b);
        }
    }

    static /* synthetic */ class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f5036a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        static final /* synthetic */ int[] f5037b;

        static {
            int[] iArr = new int[e.b.values().length];
            f5037b = iArr;
            try {
                iArr[e.b.ADDING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5037b[e.b.REMOVING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f5037b[e.b.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[e.c.values().length];
            f5036a = iArr2;
            try {
                iArr2[e.c.REMOVED.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f5036a[e.c.VISIBLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f5036a[e.c.GONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f5036a[e.c.INVISIBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    private static class d extends e {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private final D f5038h;

        d(e.c cVar, e.b bVar, D d4, androidx.core.os.d dVar) {
            super(cVar, bVar, d4.k(), dVar);
            this.f5038h = d4;
        }

        @Override // androidx.fragment.app.L.e
        public void c() {
            super.c();
            this.f5038h.m();
        }

        @Override // androidx.fragment.app.L.e
        void l() {
            if (g() != e.b.ADDING) {
                if (g() == e.b.REMOVING) {
                    Fragment fragmentK = this.f5038h.k();
                    View viewN1 = fragmentK.n1();
                    if (x.G0(2)) {
                        Log.v("FragmentManager", "Clearing focus " + viewN1.findFocus() + " on view " + viewN1 + " for Fragment " + fragmentK);
                    }
                    viewN1.clearFocus();
                    return;
                }
                return;
            }
            Fragment fragmentK2 = this.f5038h.k();
            View viewFindFocus = fragmentK2.f4920J.findFocus();
            if (viewFindFocus != null) {
                fragmentK2.t1(viewFindFocus);
                if (x.G0(2)) {
                    Log.v("FragmentManager", "requestFocus: Saved focused view " + viewFindFocus + " for Fragment " + fragmentK2);
                }
            }
            View viewN12 = f().n1();
            if (viewN12.getParent() == null) {
                this.f5038h.b();
                viewN12.setAlpha(0.0f);
            }
            if (viewN12.getAlpha() == 0.0f && viewN12.getVisibility() == 0) {
                viewN12.setVisibility(4);
            }
            viewN12.setAlpha(fragmentK2.I());
        }
    }

    static class e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private c f5039a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private b f5040b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final Fragment f5041c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final List f5042d = new ArrayList();

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final HashSet f5043e = new HashSet();

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private boolean f5044f = false;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private boolean f5045g = false;

        class a implements d.a {
            a() {
            }

            @Override // androidx.core.os.d.a
            public void a() {
                e.this.b();
            }
        }

        enum b {
            NONE,
            ADDING,
            REMOVING
        }

        enum c {
            REMOVED,
            VISIBLE,
            GONE,
            INVISIBLE;

            static c b(int i3) {
                if (i3 == 0) {
                    return VISIBLE;
                }
                if (i3 == 4) {
                    return INVISIBLE;
                }
                if (i3 == 8) {
                    return GONE;
                }
                throw new IllegalArgumentException("Unknown visibility " + i3);
            }

            static c c(View view) {
                return (view.getAlpha() == 0.0f && view.getVisibility() == 0) ? INVISIBLE : b(view.getVisibility());
            }

            void a(View view) {
                int i3 = c.f5036a[ordinal()];
                if (i3 == 1) {
                    ViewGroup viewGroup = (ViewGroup) view.getParent();
                    if (viewGroup != null) {
                        if (x.G0(2)) {
                            Log.v("FragmentManager", "SpecialEffectsController: Removing view " + view + " from container " + viewGroup);
                        }
                        viewGroup.removeView(view);
                        return;
                    }
                    return;
                }
                if (i3 == 2) {
                    if (x.G0(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to VISIBLE");
                    }
                    view.setVisibility(0);
                    return;
                }
                if (i3 == 3) {
                    if (x.G0(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to GONE");
                    }
                    view.setVisibility(8);
                    return;
                }
                if (i3 != 4) {
                    return;
                }
                if (x.G0(2)) {
                    Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to INVISIBLE");
                }
                view.setVisibility(4);
            }
        }

        e(c cVar, b bVar, Fragment fragment, androidx.core.os.d dVar) {
            this.f5039a = cVar;
            this.f5040b = bVar;
            this.f5041c = fragment;
            dVar.b(new a());
        }

        final void a(Runnable runnable) {
            this.f5042d.add(runnable);
        }

        final void b() {
            if (h()) {
                return;
            }
            this.f5044f = true;
            if (this.f5043e.isEmpty()) {
                c();
                return;
            }
            Iterator it = new ArrayList(this.f5043e).iterator();
            while (it.hasNext()) {
                ((androidx.core.os.d) it.next()).a();
            }
        }

        public void c() {
            if (this.f5045g) {
                return;
            }
            if (x.G0(2)) {
                Log.v("FragmentManager", "SpecialEffectsController: " + this + " has called complete.");
            }
            this.f5045g = true;
            Iterator it = this.f5042d.iterator();
            while (it.hasNext()) {
                ((Runnable) it.next()).run();
            }
        }

        public final void d(androidx.core.os.d dVar) {
            if (this.f5043e.remove(dVar) && this.f5043e.isEmpty()) {
                c();
            }
        }

        public c e() {
            return this.f5039a;
        }

        public final Fragment f() {
            return this.f5041c;
        }

        b g() {
            return this.f5040b;
        }

        final boolean h() {
            return this.f5044f;
        }

        final boolean i() {
            return this.f5045g;
        }

        public final void j(androidx.core.os.d dVar) {
            l();
            this.f5043e.add(dVar);
        }

        final void k(c cVar, b bVar) {
            int i3 = c.f5037b[bVar.ordinal()];
            if (i3 == 1) {
                if (this.f5039a == c.REMOVED) {
                    if (x.G0(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.f5041c + " mFinalState = REMOVED -> VISIBLE. mLifecycleImpact = " + this.f5040b + " to ADDING.");
                    }
                    this.f5039a = c.VISIBLE;
                    this.f5040b = b.ADDING;
                    return;
                }
                return;
            }
            if (i3 == 2) {
                if (x.G0(2)) {
                    Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.f5041c + " mFinalState = " + this.f5039a + " -> REMOVED. mLifecycleImpact  = " + this.f5040b + " to REMOVING.");
                }
                this.f5039a = c.REMOVED;
                this.f5040b = b.REMOVING;
                return;
            }
            if (i3 == 3 && this.f5039a != c.REMOVED) {
                if (x.G0(2)) {
                    Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.f5041c + " mFinalState = " + this.f5039a + " -> " + cVar + ". ");
                }
                this.f5039a = cVar;
            }
        }

        abstract void l();

        public String toString() {
            return "Operation {" + Integer.toHexString(System.identityHashCode(this)) + "} {mFinalState = " + this.f5039a + "} {mLifecycleImpact = " + this.f5040b + "} {mFragment = " + this.f5041c + "}";
        }
    }

    L(ViewGroup viewGroup) {
        this.f5027a = viewGroup;
    }

    private void a(e.c cVar, e.b bVar, D d4) {
        synchronized (this.f5028b) {
            try {
                androidx.core.os.d dVar = new androidx.core.os.d();
                e eVarH = h(d4.k());
                if (eVarH != null) {
                    eVarH.k(cVar, bVar);
                    return;
                }
                d dVar2 = new d(cVar, bVar, d4, dVar);
                this.f5028b.add(dVar2);
                dVar2.a(new a(dVar2));
                dVar2.a(new b(dVar2));
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private e h(Fragment fragment) {
        for (e eVar : this.f5028b) {
            if (eVar.f().equals(fragment) && !eVar.h()) {
                return eVar;
            }
        }
        return null;
    }

    private e i(Fragment fragment) {
        for (e eVar : this.f5029c) {
            if (eVar.f().equals(fragment) && !eVar.h()) {
                return eVar;
            }
        }
        return null;
    }

    static L n(ViewGroup viewGroup, x xVar) {
        return o(viewGroup, xVar.y0());
    }

    static L o(ViewGroup viewGroup, M m3) {
        Object tag = viewGroup.getTag(B.b.f43b);
        if (tag instanceof L) {
            return (L) tag;
        }
        L lA = m3.a(viewGroup);
        viewGroup.setTag(B.b.f43b, lA);
        return lA;
    }

    private void q() {
        for (e eVar : this.f5028b) {
            if (eVar.g() == e.b.ADDING) {
                eVar.k(e.c.b(eVar.f().n1().getVisibility()), e.b.NONE);
            }
        }
    }

    void b(e.c cVar, D d4) {
        if (x.G0(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing add operation for fragment " + d4.k());
        }
        a(cVar, e.b.ADDING, d4);
    }

    void c(D d4) {
        if (x.G0(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing hide operation for fragment " + d4.k());
        }
        a(e.c.GONE, e.b.NONE, d4);
    }

    void d(D d4) {
        if (x.G0(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing remove operation for fragment " + d4.k());
        }
        a(e.c.REMOVED, e.b.REMOVING, d4);
    }

    void e(D d4) {
        if (x.G0(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing show operation for fragment " + d4.k());
        }
        a(e.c.VISIBLE, e.b.NONE, d4);
    }

    abstract void f(List list, boolean z3);

    void g() {
        if (this.f5031e) {
            return;
        }
        if (!Z.E(this.f5027a)) {
            j();
            this.f5030d = false;
            return;
        }
        synchronized (this.f5028b) {
            try {
                if (!this.f5028b.isEmpty()) {
                    ArrayList<e> arrayList = new ArrayList(this.f5029c);
                    this.f5029c.clear();
                    for (e eVar : arrayList) {
                        if (x.G0(2)) {
                            Log.v("FragmentManager", "SpecialEffectsController: Cancelling operation " + eVar);
                        }
                        eVar.b();
                        if (!eVar.i()) {
                            this.f5029c.add(eVar);
                        }
                    }
                    q();
                    ArrayList arrayList2 = new ArrayList(this.f5028b);
                    this.f5028b.clear();
                    this.f5029c.addAll(arrayList2);
                    if (x.G0(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Executing pending operations");
                    }
                    Iterator it = arrayList2.iterator();
                    while (it.hasNext()) {
                        ((e) it.next()).l();
                    }
                    f(arrayList2, this.f5030d);
                    this.f5030d = false;
                    if (x.G0(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Finished executing pending operations");
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    void j() {
        if (x.G0(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Forcing all operations to complete");
        }
        boolean zE = Z.E(this.f5027a);
        synchronized (this.f5028b) {
            try {
                q();
                Iterator it = this.f5028b.iterator();
                while (it.hasNext()) {
                    ((e) it.next()).l();
                }
                for (e eVar : new ArrayList(this.f5029c)) {
                    if (x.G0(2)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("SpecialEffectsController: ");
                        sb.append(zE ? "" : "Container " + this.f5027a + " is not attached to window. ");
                        sb.append("Cancelling running operation ");
                        sb.append(eVar);
                        Log.v("FragmentManager", sb.toString());
                    }
                    eVar.b();
                }
                for (e eVar2 : new ArrayList(this.f5028b)) {
                    if (x.G0(2)) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("SpecialEffectsController: ");
                        sb2.append(zE ? "" : "Container " + this.f5027a + " is not attached to window. ");
                        sb2.append("Cancelling pending operation ");
                        sb2.append(eVar2);
                        Log.v("FragmentManager", sb2.toString());
                    }
                    eVar2.b();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    void k() {
        if (this.f5031e) {
            if (x.G0(2)) {
                Log.v("FragmentManager", "SpecialEffectsController: Forcing postponed operations");
            }
            this.f5031e = false;
            g();
        }
    }

    e.b l(D d4) {
        e eVarH = h(d4.k());
        e.b bVarG = eVarH != null ? eVarH.g() : null;
        e eVarI = i(d4.k());
        return (eVarI == null || !(bVarG == null || bVarG == e.b.NONE)) ? bVarG : eVarI.g();
    }

    public ViewGroup m() {
        return this.f5027a;
    }

    void p() {
        synchronized (this.f5028b) {
            try {
                q();
                this.f5031e = false;
                int size = this.f5028b.size() - 1;
                while (true) {
                    if (size < 0) {
                        break;
                    }
                    e eVar = (e) this.f5028b.get(size);
                    e.c cVarC = e.c.c(eVar.f().f4920J);
                    e.c cVarE = eVar.e();
                    e.c cVar = e.c.VISIBLE;
                    if (cVarE == cVar && cVarC != cVar) {
                        this.f5031e = eVar.f().a0();
                        break;
                    }
                    size--;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    void r(boolean z3) {
        this.f5030d = z3;
    }
}

package androidx.lifecycle;

import androidx.lifecycle.AbstractC0299g;
import j.C0566c;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import k.C0580a;
import k.C0581b;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public class m extends AbstractC0299g {

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final a f5330j = new a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final boolean f5331b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private C0580a f5332c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private AbstractC0299g.b f5333d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final WeakReference f5334e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f5335f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f5336g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f5337h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private ArrayList f5338i;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final AbstractC0299g.b a(AbstractC0299g.b bVar, AbstractC0299g.b bVar2) {
            D2.h.f(bVar, "state1");
            return (bVar2 == null || bVar2.compareTo(bVar) >= 0) ? bVar : bVar2;
        }

        private a() {
        }
    }

    public static final class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private AbstractC0299g.b f5339a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private InterfaceC0302j f5340b;

        public b(k kVar, AbstractC0299g.b bVar) {
            D2.h.f(bVar, "initialState");
            D2.h.c(kVar);
            this.f5340b = n.f(kVar);
            this.f5339a = bVar;
        }

        public final void a(l lVar, AbstractC0299g.a aVar) {
            D2.h.f(aVar, "event");
            AbstractC0299g.b bVarB = aVar.b();
            this.f5339a = m.f5330j.a(this.f5339a, bVarB);
            InterfaceC0302j interfaceC0302j = this.f5340b;
            D2.h.c(lVar);
            interfaceC0302j.d(lVar, aVar);
            this.f5339a = bVarB;
        }

        public final AbstractC0299g.b b() {
            return this.f5339a;
        }
    }

    public /* synthetic */ m(l lVar, boolean z3, DefaultConstructorMarker defaultConstructorMarker) {
        this(lVar, z3);
    }

    private final void d(l lVar) {
        Iterator itA = this.f5332c.a();
        D2.h.e(itA, "observerMap.descendingIterator()");
        while (itA.hasNext() && !this.f5337h) {
            Map.Entry entry = (Map.Entry) itA.next();
            D2.h.e(entry, "next()");
            k kVar = (k) entry.getKey();
            b bVar = (b) entry.getValue();
            while (bVar.b().compareTo(this.f5333d) > 0 && !this.f5337h && this.f5332c.contains(kVar)) {
                AbstractC0299g.a aVarA = AbstractC0299g.a.Companion.a(bVar.b());
                if (aVarA == null) {
                    throw new IllegalStateException("no event down from " + bVar.b());
                }
                l(aVarA.b());
                bVar.a(lVar, aVarA);
                k();
            }
        }
    }

    private final AbstractC0299g.b e(k kVar) {
        b bVar;
        Map.Entry entryK = this.f5332c.k(kVar);
        AbstractC0299g.b bVar2 = null;
        AbstractC0299g.b bVarB = (entryK == null || (bVar = (b) entryK.getValue()) == null) ? null : bVar.b();
        if (!this.f5338i.isEmpty()) {
            bVar2 = (AbstractC0299g.b) this.f5338i.get(r0.size() - 1);
        }
        a aVar = f5330j;
        return aVar.a(aVar.a(this.f5333d, bVarB), bVar2);
    }

    private final void f(String str) {
        if (!this.f5331b || C0566c.f().b()) {
            return;
        }
        throw new IllegalStateException(("Method " + str + " must be called on the main thread").toString());
    }

    private final void g(l lVar) {
        C0581b.d dVarE = this.f5332c.e();
        D2.h.e(dVarE, "observerMap.iteratorWithAdditions()");
        while (dVarE.hasNext() && !this.f5337h) {
            Map.Entry entry = (Map.Entry) dVarE.next();
            k kVar = (k) entry.getKey();
            b bVar = (b) entry.getValue();
            while (bVar.b().compareTo(this.f5333d) < 0 && !this.f5337h && this.f5332c.contains(kVar)) {
                l(bVar.b());
                AbstractC0299g.a aVarB = AbstractC0299g.a.Companion.b(bVar.b());
                if (aVarB == null) {
                    throw new IllegalStateException("no event up from " + bVar.b());
                }
                bVar.a(lVar, aVarB);
                k();
            }
        }
    }

    private final boolean i() {
        if (this.f5332c.size() == 0) {
            return true;
        }
        Map.Entry entryB = this.f5332c.b();
        D2.h.c(entryB);
        AbstractC0299g.b bVarB = ((b) entryB.getValue()).b();
        Map.Entry entryF = this.f5332c.f();
        D2.h.c(entryF);
        AbstractC0299g.b bVarB2 = ((b) entryF.getValue()).b();
        return bVarB == bVarB2 && this.f5333d == bVarB2;
    }

    private final void j(AbstractC0299g.b bVar) {
        AbstractC0299g.b bVar2 = this.f5333d;
        if (bVar2 == bVar) {
            return;
        }
        if (bVar2 == AbstractC0299g.b.INITIALIZED && bVar == AbstractC0299g.b.DESTROYED) {
            throw new IllegalStateException(("no event down from " + this.f5333d + " in component " + this.f5334e.get()).toString());
        }
        this.f5333d = bVar;
        if (this.f5336g || this.f5335f != 0) {
            this.f5337h = true;
            return;
        }
        this.f5336g = true;
        n();
        this.f5336g = false;
        if (this.f5333d == AbstractC0299g.b.DESTROYED) {
            this.f5332c = new C0580a();
        }
    }

    private final void k() {
        this.f5338i.remove(r0.size() - 1);
    }

    private final void l(AbstractC0299g.b bVar) {
        this.f5338i.add(bVar);
    }

    private final void n() {
        l lVar = (l) this.f5334e.get();
        if (lVar == null) {
            throw new IllegalStateException("LifecycleOwner of this LifecycleRegistry is already garbage collected. It is too late to change lifecycle state.");
        }
        while (!i()) {
            this.f5337h = false;
            AbstractC0299g.b bVar = this.f5333d;
            Map.Entry entryB = this.f5332c.b();
            D2.h.c(entryB);
            if (bVar.compareTo(((b) entryB.getValue()).b()) < 0) {
                d(lVar);
            }
            Map.Entry entryF = this.f5332c.f();
            if (!this.f5337h && entryF != null && this.f5333d.compareTo(((b) entryF.getValue()).b()) > 0) {
                g(lVar);
            }
        }
        this.f5337h = false;
    }

    @Override // androidx.lifecycle.AbstractC0299g
    public void a(k kVar) {
        l lVar;
        D2.h.f(kVar, "observer");
        f("addObserver");
        AbstractC0299g.b bVar = this.f5333d;
        AbstractC0299g.b bVar2 = AbstractC0299g.b.DESTROYED;
        if (bVar != bVar2) {
            bVar2 = AbstractC0299g.b.INITIALIZED;
        }
        b bVar3 = new b(kVar, bVar2);
        if (((b) this.f5332c.i(kVar, bVar3)) == null && (lVar = (l) this.f5334e.get()) != null) {
            boolean z3 = this.f5335f != 0 || this.f5336g;
            AbstractC0299g.b bVarE = e(kVar);
            this.f5335f++;
            while (bVar3.b().compareTo(bVarE) < 0 && this.f5332c.contains(kVar)) {
                l(bVar3.b());
                AbstractC0299g.a aVarB = AbstractC0299g.a.Companion.b(bVar3.b());
                if (aVarB == null) {
                    throw new IllegalStateException("no event up from " + bVar3.b());
                }
                bVar3.a(lVar, aVarB);
                k();
                bVarE = e(kVar);
            }
            if (!z3) {
                n();
            }
            this.f5335f--;
        }
    }

    @Override // androidx.lifecycle.AbstractC0299g
    public AbstractC0299g.b b() {
        return this.f5333d;
    }

    @Override // androidx.lifecycle.AbstractC0299g
    public void c(k kVar) {
        D2.h.f(kVar, "observer");
        f("removeObserver");
        this.f5332c.j(kVar);
    }

    public void h(AbstractC0299g.a aVar) {
        D2.h.f(aVar, "event");
        f("handleLifecycleEvent");
        j(aVar.b());
    }

    public void m(AbstractC0299g.b bVar) {
        D2.h.f(bVar, "state");
        f("setCurrentState");
        j(bVar);
    }

    private m(l lVar, boolean z3) {
        this.f5331b = z3;
        this.f5332c = new C0580a();
        this.f5333d = AbstractC0299g.b.INITIALIZED;
        this.f5338i = new ArrayList();
        this.f5334e = new WeakReference(lVar);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public m(l lVar) {
        this(lVar, true);
        D2.h.f(lVar, "provider");
    }
}

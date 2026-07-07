package Q0;

import D2.h;
import com.facebook.imagepipeline.producers.f0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0711h;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class b implements d {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f1796b = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final List f1797a;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public b(Set<? extends d> set) {
        if (set == null) {
            this.f1797a = new ArrayList();
            return;
        }
        ArrayList arrayList = new ArrayList(set.size());
        this.f1797a = arrayList;
        AbstractC0717n.L(set, arrayList);
    }

    @Override // Q0.d
    public void a(f0 f0Var) {
        h.f(f0Var, "producerContext");
        Iterator it = this.f1797a.iterator();
        while (it.hasNext()) {
            try {
                ((d) it.next()).a(f0Var);
            } catch (Exception e4) {
                Y.a.n("ForwardingRequestListener2", "InternalListener exception in onRequestCancellation", e4);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.h0
    public void b(f0 f0Var, String str, String str2) {
        h.f(f0Var, "producerContext");
        h.f(str, "producerName");
        h.f(str2, "producerEventName");
        Iterator it = this.f1797a.iterator();
        while (it.hasNext()) {
            try {
                ((d) it.next()).b(f0Var, str, str2);
            } catch (Exception e4) {
                Y.a.n("ForwardingRequestListener2", "InternalListener exception in onIntermediateChunkStart", e4);
            }
        }
    }

    @Override // Q0.d
    public void c(f0 f0Var) {
        h.f(f0Var, "producerContext");
        Iterator it = this.f1797a.iterator();
        while (it.hasNext()) {
            try {
                ((d) it.next()).c(f0Var);
            } catch (Exception e4) {
                Y.a.n("ForwardingRequestListener2", "InternalListener exception in onRequestStart", e4);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.h0
    public void d(f0 f0Var, String str, Map map) {
        Iterator it = this.f1797a.iterator();
        while (it.hasNext()) {
            try {
                ((d) it.next()).d(f0Var, str, map);
            } catch (Exception e4) {
                Y.a.n("ForwardingRequestListener2", "InternalListener exception in onProducerFinishWithSuccess", e4);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.h0
    public void e(f0 f0Var, String str, boolean z3) {
        h.f(f0Var, "producerContext");
        h.f(str, "producerName");
        Iterator it = this.f1797a.iterator();
        while (it.hasNext()) {
            try {
                ((d) it.next()).e(f0Var, str, z3);
            } catch (Exception e4) {
                Y.a.n("ForwardingRequestListener2", "InternalListener exception in onProducerFinishWithSuccess", e4);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.h0
    public void f(f0 f0Var, String str, Map map) {
        Iterator it = this.f1797a.iterator();
        while (it.hasNext()) {
            try {
                ((d) it.next()).f(f0Var, str, map);
            } catch (Exception e4) {
                Y.a.n("ForwardingRequestListener2", "InternalListener exception in onProducerFinishWithCancellation", e4);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.h0
    public void g(f0 f0Var, String str) {
        h.f(f0Var, "producerContext");
        h.f(str, "producerName");
        Iterator it = this.f1797a.iterator();
        while (it.hasNext()) {
            try {
                ((d) it.next()).g(f0Var, str);
            } catch (Exception e4) {
                Y.a.n("ForwardingRequestListener2", "InternalListener exception in onProducerStart", e4);
            }
        }
    }

    @Override // Q0.d
    public void h(f0 f0Var) {
        h.f(f0Var, "producerContext");
        Iterator it = this.f1797a.iterator();
        while (it.hasNext()) {
            try {
                ((d) it.next()).h(f0Var);
            } catch (Exception e4) {
                Y.a.n("ForwardingRequestListener2", "InternalListener exception in onRequestSuccess", e4);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.h0
    public void i(f0 f0Var, String str, Throwable th, Map map) {
        Iterator it = this.f1797a.iterator();
        while (it.hasNext()) {
            try {
                ((d) it.next()).i(f0Var, str, th, map);
            } catch (Exception e4) {
                Y.a.n("ForwardingRequestListener2", "InternalListener exception in onProducerFinishWithFailure", e4);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.h0
    public boolean j(f0 f0Var, String str) {
        h.f(f0Var, "producerContext");
        h.f(str, "producerName");
        List list = this.f1797a;
        if (list != null && list.isEmpty()) {
            return false;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (((d) it.next()).j(f0Var, str)) {
                return true;
            }
        }
        return false;
    }

    @Override // Q0.d
    public void k(f0 f0Var, Throwable th) {
        h.f(f0Var, "producerContext");
        h.f(th, "throwable");
        Iterator it = this.f1797a.iterator();
        while (it.hasNext()) {
            try {
                ((d) it.next()).k(f0Var, th);
            } catch (Exception e4) {
                Y.a.n("ForwardingRequestListener2", "InternalListener exception in onRequestFailure", e4);
            }
        }
    }

    public b(d... dVarArr) {
        h.f(dVarArr, "listenersToAdd");
        ArrayList arrayList = new ArrayList(dVarArr.length);
        this.f1797a = arrayList;
        AbstractC0711h.n(dVarArr, arrayList);
    }
}

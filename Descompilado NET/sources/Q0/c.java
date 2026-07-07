package Q0;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class c implements e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final List f1798a;

    public c(Set<e> set) {
        this.f1798a = new ArrayList(set.size());
        for (e eVar : set) {
            if (eVar != null) {
                this.f1798a.add(eVar);
            }
        }
    }

    private void l(String str, Throwable th) {
        Y.a.n("ForwardingRequestListener", str, th);
    }

    @Override // Q0.e
    public void a(U0.b bVar, Object obj, String str, boolean z3) {
        int size = this.f1798a.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                ((e) this.f1798a.get(i3)).a(bVar, obj, str, z3);
            } catch (Exception e4) {
                l("InternalListener exception in onRequestStart", e4);
            }
        }
    }

    @Override // Q0.e
    public void b(U0.b bVar, String str, boolean z3) {
        int size = this.f1798a.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                ((e) this.f1798a.get(i3)).b(bVar, str, z3);
            } catch (Exception e4) {
                l("InternalListener exception in onRequestSuccess", e4);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.i0
    public boolean c(String str) {
        int size = this.f1798a.size();
        for (int i3 = 0; i3 < size; i3++) {
            if (((e) this.f1798a.get(i3)).c(str)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.facebook.imagepipeline.producers.i0
    public void d(String str, String str2, String str3) {
        int size = this.f1798a.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                ((e) this.f1798a.get(i3)).d(str, str2, str3);
            } catch (Exception e4) {
                l("InternalListener exception in onIntermediateChunkStart", e4);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.i0
    public void e(String str, String str2, Map map) {
        int size = this.f1798a.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                ((e) this.f1798a.get(i3)).e(str, str2, map);
            } catch (Exception e4) {
                l("InternalListener exception in onProducerFinishWithSuccess", e4);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.i0
    public void f(String str, String str2) {
        int size = this.f1798a.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                ((e) this.f1798a.get(i3)).f(str, str2);
            } catch (Exception e4) {
                l("InternalListener exception in onProducerStart", e4);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.i0
    public void g(String str, String str2, Throwable th, Map map) {
        int size = this.f1798a.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                ((e) this.f1798a.get(i3)).g(str, str2, th, map);
            } catch (Exception e4) {
                l("InternalListener exception in onProducerFinishWithFailure", e4);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.i0
    public void h(String str, String str2, Map map) {
        int size = this.f1798a.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                ((e) this.f1798a.get(i3)).h(str, str2, map);
            } catch (Exception e4) {
                l("InternalListener exception in onProducerFinishWithCancellation", e4);
            }
        }
    }

    @Override // Q0.e
    public void i(String str) {
        int size = this.f1798a.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                ((e) this.f1798a.get(i3)).i(str);
            } catch (Exception e4) {
                l("InternalListener exception in onRequestCancellation", e4);
            }
        }
    }

    @Override // com.facebook.imagepipeline.producers.i0
    public void j(String str, String str2, boolean z3) {
        int size = this.f1798a.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                ((e) this.f1798a.get(i3)).j(str, str2, z3);
            } catch (Exception e4) {
                l("InternalListener exception in onProducerFinishWithSuccess", e4);
            }
        }
    }

    @Override // Q0.e
    public void k(U0.b bVar, String str, Throwable th, boolean z3) {
        int size = this.f1798a.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                ((e) this.f1798a.get(i3)).k(bVar, str, th, z3);
            } catch (Exception e4) {
                l("InternalListener exception in onRequestFailure", e4);
            }
        }
    }

    public c(e... eVarArr) {
        this.f1798a = new ArrayList(eVarArr.length);
        for (e eVar : eVarArr) {
            if (eVar != null) {
                this.f1798a.add(eVar);
            }
        }
    }
}

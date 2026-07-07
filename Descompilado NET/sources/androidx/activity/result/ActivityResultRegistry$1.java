package androidx.activity.result;

import androidx.activity.result.e;
import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.InterfaceC0302j;
import androidx.lifecycle.l;
import b.AbstractC0303a;

/* JADX INFO: loaded from: classes.dex */
class ActivityResultRegistry$1 implements InterfaceC0302j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f3040a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ b f3041b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ AbstractC0303a f3042c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final /* synthetic */ e f3043d;

    @Override // androidx.lifecycle.InterfaceC0302j
    public void d(l lVar, AbstractC0299g.a aVar) {
        if (!AbstractC0299g.a.ON_START.equals(aVar)) {
            if (AbstractC0299g.a.ON_STOP.equals(aVar)) {
                this.f3043d.f3051f.remove(this.f3040a);
                return;
            } else {
                if (AbstractC0299g.a.ON_DESTROY.equals(aVar)) {
                    this.f3043d.i(this.f3040a);
                    return;
                }
                return;
            }
        }
        this.f3043d.f3051f.put(this.f3040a, new e.b(this.f3041b, this.f3042c));
        if (this.f3043d.f3052g.containsKey(this.f3040a)) {
            Object obj = this.f3043d.f3052g.get(this.f3040a);
            this.f3043d.f3052g.remove(this.f3040a);
            this.f3041b.a(obj);
        }
        a aVar2 = (a) this.f3043d.f3053h.getParcelable(this.f3040a);
        if (aVar2 != null) {
            this.f3043d.f3053h.remove(this.f3040a);
            this.f3041b.a(this.f3042c.a(aVar2.b(), aVar2.a()));
        }
    }
}

package androidx.fragment.app;

import android.os.Bundle;
import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.InterfaceC0302j;

/* JADX INFO: loaded from: classes.dex */
class FragmentManager$6 implements InterfaceC0302j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f4993a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ AbstractC0299g f4994b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ x f4995c;

    @Override // androidx.lifecycle.InterfaceC0302j
    public void d(androidx.lifecycle.l lVar, AbstractC0299g.a aVar) {
        if (aVar == AbstractC0299g.a.ON_START && ((Bundle) this.f4995c.f5212k.get(this.f4993a)) != null) {
            throw null;
        }
        if (aVar == AbstractC0299g.a.ON_DESTROY) {
            this.f4994b.c(this);
            this.f4995c.f5213l.remove(this.f4993a);
        }
    }
}

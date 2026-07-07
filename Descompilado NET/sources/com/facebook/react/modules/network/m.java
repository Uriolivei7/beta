package com.facebook.react.modules.network;

import M2.t;
import M2.u;
import java.util.ArrayList;
import java.util.List;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class m implements a {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private M2.n f7025c;

    @Override // M2.n
    public void a(u uVar, List list) {
        D2.h.f(uVar, "url");
        D2.h.f(list, "cookies");
        M2.n nVar = this.f7025c;
        if (nVar != null) {
            nVar.a(uVar, list);
        }
    }

    @Override // com.facebook.react.modules.network.a
    public void b() {
        this.f7025c = null;
    }

    @Override // M2.n
    public List c(u uVar) {
        D2.h.f(uVar, "url");
        M2.n nVar = this.f7025c;
        if (nVar == null) {
            return AbstractC0717n.g();
        }
        List<M2.m> listC = nVar.c(uVar);
        ArrayList arrayList = new ArrayList();
        for (M2.m mVar : listC) {
            try {
                new t.a().a(mVar.a(), mVar.b());
                arrayList.add(mVar);
            } catch (IllegalArgumentException unused) {
            }
        }
        return arrayList;
    }

    @Override // com.facebook.react.modules.network.a
    public void d(M2.n nVar) {
        D2.h.f(nVar, "cookieJar");
        this.f7025c = nVar;
    }
}

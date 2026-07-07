package k;

import java.util.HashMap;
import java.util.Map;
import k.C0581b;

/* JADX INFO: renamed from: k.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0580a extends C0581b {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final HashMap f9564f = new HashMap();

    @Override // k.C0581b
    protected C0581b.c c(Object obj) {
        return (C0581b.c) this.f9564f.get(obj);
    }

    public boolean contains(Object obj) {
        return this.f9564f.containsKey(obj);
    }

    @Override // k.C0581b
    public Object i(Object obj, Object obj2) {
        C0581b.c cVarC = c(obj);
        if (cVarC != null) {
            return cVarC.f9570c;
        }
        this.f9564f.put(obj, h(obj, obj2));
        return null;
    }

    @Override // k.C0581b
    public Object j(Object obj) {
        Object objJ = super.j(obj);
        this.f9564f.remove(obj);
        return objJ;
    }

    public Map.Entry k(Object obj) {
        if (contains(obj)) {
            return ((C0581b.c) this.f9564f.get(obj)).f9572e;
        }
        return null;
    }
}

package G2;

import D2.h;
import I2.f;

/* JADX INFO: loaded from: classes.dex */
public abstract class a implements b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Object f257a;

    public a(Object obj) {
        this.f257a = obj;
    }

    @Override // G2.b
    public Object a(Object obj, f fVar) {
        h.f(fVar, "property");
        return this.f257a;
    }

    @Override // G2.b
    public void b(Object obj, f fVar, Object obj2) {
        h.f(fVar, "property");
        Object obj3 = this.f257a;
        if (d(fVar, obj3, obj2)) {
            this.f257a = obj2;
            c(fVar, obj3, obj2);
        }
    }

    protected void c(f fVar, Object obj, Object obj2) {
        h.f(fVar, "property");
    }

    protected boolean d(f fVar, Object obj, Object obj2) {
        h.f(fVar, "property");
        return true;
    }

    public String toString() {
        return "ObservableProperty(value=" + this.f257a + ')';
    }
}

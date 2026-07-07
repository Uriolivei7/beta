package h0;

import X.n;

/* JADX INFO: renamed from: h0.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0549d {

    /* JADX INFO: renamed from: h0.d$a */
    class a implements n {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Throwable f9467a;

        a(Throwable th) {
            this.f9467a = th;
        }

        @Override // X.n
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public InterfaceC0548c get() {
            return AbstractC0549d.b(this.f9467a);
        }
    }

    public static n a(Throwable th) {
        return new a(th);
    }

    public static InterfaceC0548c b(Throwable th) {
        C0554i c0554iY = C0554i.y();
        c0554iY.q(th);
        return c0554iY;
    }
}

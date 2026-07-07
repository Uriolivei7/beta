package H0;

import a0.InterfaceC0209c;
import b0.AbstractC0306a;

/* JADX INFO: loaded from: classes.dex */
public interface n extends x, InterfaceC0209c {

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final Object f302a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final AbstractC0306a f303b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public int f304c = 0;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public boolean f305d = false;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public int f306e = 0;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public int f307f;

        private a(Object obj, AbstractC0306a abstractC0306a, b bVar, int i3) {
            this.f302a = X.k.g(obj);
            this.f303b = (AbstractC0306a) X.k.g(AbstractC0306a.A(abstractC0306a));
            this.f307f = i3;
        }

        public static a a(Object obj, AbstractC0306a abstractC0306a, int i3, b bVar) {
            return new a(obj, abstractC0306a, bVar, i3);
        }

        public static a b(Object obj, AbstractC0306a abstractC0306a, b bVar) {
            return a(obj, abstractC0306a, -1, bVar);
        }
    }

    public interface b {
    }
}

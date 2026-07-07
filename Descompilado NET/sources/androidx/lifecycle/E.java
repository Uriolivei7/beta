package androidx.lifecycle;

import F.a;
import android.app.Application;
import java.lang.reflect.InvocationTargetException;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public class E {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final G f5267a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final b f5268b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final F.a f5269c;

    public interface b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final a f5275a = a.f5276a;

        public static final class a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            static final /* synthetic */ a f5276a = new a();

            private a() {
            }
        }

        default D a(Class cls) {
            D2.h.f(cls, "modelClass");
            throw new UnsupportedOperationException("Factory.create(String) is unsupported.  This Factory requires `CreationExtras` to be passed into `create` method.");
        }

        default D b(Class cls, F.a aVar) {
            D2.h.f(cls, "modelClass");
            D2.h.f(aVar, "extras");
            return a(cls);
        }
    }

    public static class c implements b {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private static c f5278c;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public static final a f5277b = new a(null);

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public static final a.b f5279d = a.C0077a.f5280a;

        public static final class a {

            /* JADX INFO: renamed from: androidx.lifecycle.E$c$a$a, reason: collision with other inner class name */
            private static final class C0077a implements a.b {

                /* JADX INFO: renamed from: a, reason: collision with root package name */
                public static final C0077a f5280a = new C0077a();

                private C0077a() {
                }
            }

            public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public final c a() {
                if (c.f5278c == null) {
                    c.f5278c = new c();
                }
                c cVar = c.f5278c;
                D2.h.c(cVar);
                return cVar;
            }

            private a() {
            }
        }

        @Override // androidx.lifecycle.E.b
        public D a(Class cls) throws InvocationTargetException {
            D2.h.f(cls, "modelClass");
            try {
                Object objNewInstance = cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                D2.h.e(objNewInstance, "{\n                modelC…wInstance()\n            }");
                return (D) objNewInstance;
            } catch (IllegalAccessException e4) {
                throw new RuntimeException("Cannot create an instance of " + cls, e4);
            } catch (InstantiationException e5) {
                throw new RuntimeException("Cannot create an instance of " + cls, e5);
            } catch (NoSuchMethodException e6) {
                throw new RuntimeException("Cannot create an instance of " + cls, e6);
            }
        }
    }

    public static class d {
        public void c(D d4) {
            D2.h.f(d4, "viewModel");
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public E(G g3, b bVar) {
        this(g3, bVar, null, 4, null);
        D2.h.f(g3, "store");
        D2.h.f(bVar, "factory");
    }

    public D a(Class cls) {
        D2.h.f(cls, "modelClass");
        String canonicalName = cls.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        }
        return b("androidx.lifecycle.ViewModelProvider.DefaultKey:" + canonicalName, cls);
    }

    public D b(String str, Class cls) {
        D dA;
        D2.h.f(str, "key");
        D2.h.f(cls, "modelClass");
        D dB = this.f5267a.b(str);
        if (!cls.isInstance(dB)) {
            F.d dVar = new F.d(this.f5269c);
            dVar.c(c.f5279d, str);
            try {
                dA = this.f5268b.b(cls, dVar);
            } catch (AbstractMethodError unused) {
                dA = this.f5268b.a(cls);
            }
            this.f5267a.d(str, dA);
            return dA;
        }
        Object obj = this.f5268b;
        d dVar2 = obj instanceof d ? (d) obj : null;
        if (dVar2 != null) {
            D2.h.c(dB);
            dVar2.c(dB);
        }
        D2.h.d(dB, "null cannot be cast to non-null type T of androidx.lifecycle.ViewModelProvider.get");
        return dB;
    }

    public static class a extends c {

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private static a f5271g;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final Application f5273e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public static final C0075a f5270f = new C0075a(null);

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public static final a.b f5272h = C0075a.C0076a.f5274a;

        /* JADX INFO: renamed from: androidx.lifecycle.E$a$a, reason: collision with other inner class name */
        public static final class C0075a {

            /* JADX INFO: renamed from: androidx.lifecycle.E$a$a$a, reason: collision with other inner class name */
            private static final class C0076a implements a.b {

                /* JADX INFO: renamed from: a, reason: collision with root package name */
                public static final C0076a f5274a = new C0076a();

                private C0076a() {
                }
            }

            public /* synthetic */ C0075a(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public final b a(H h3) {
                D2.h.f(h3, "owner");
                return h3 instanceof InterfaceC0298f ? ((InterfaceC0298f) h3).j() : c.f5277b.a();
            }

            public final a b(Application application) {
                D2.h.f(application, "application");
                if (a.f5271g == null) {
                    a.f5271g = new a(application);
                }
                a aVar = a.f5271g;
                D2.h.c(aVar);
                return aVar;
            }

            private C0075a() {
            }
        }

        private a(Application application, int i3) {
            this.f5273e = application;
        }

        private final D g(Class cls, Application application) {
            if (!C0293a.class.isAssignableFrom(cls)) {
                return super.a(cls);
            }
            try {
                D d4 = (D) cls.getConstructor(Application.class).newInstance(application);
                D2.h.e(d4, "{\n                try {\n…          }\n            }");
                return d4;
            } catch (IllegalAccessException e4) {
                throw new RuntimeException("Cannot create an instance of " + cls, e4);
            } catch (InstantiationException e5) {
                throw new RuntimeException("Cannot create an instance of " + cls, e5);
            } catch (NoSuchMethodException e6) {
                throw new RuntimeException("Cannot create an instance of " + cls, e6);
            } catch (InvocationTargetException e7) {
                throw new RuntimeException("Cannot create an instance of " + cls, e7);
            }
        }

        @Override // androidx.lifecycle.E.c, androidx.lifecycle.E.b
        public D a(Class cls) {
            D2.h.f(cls, "modelClass");
            Application application = this.f5273e;
            if (application != null) {
                return g(cls, application);
            }
            throw new UnsupportedOperationException("AndroidViewModelFactory constructed with empty constructor works only with create(modelClass: Class<T>, extras: CreationExtras).");
        }

        @Override // androidx.lifecycle.E.b
        public D b(Class cls, F.a aVar) {
            D2.h.f(cls, "modelClass");
            D2.h.f(aVar, "extras");
            if (this.f5273e != null) {
                return a(cls);
            }
            Application application = (Application) aVar.a(f5272h);
            if (application != null) {
                return g(cls, application);
            }
            if (C0293a.class.isAssignableFrom(cls)) {
                throw new IllegalArgumentException("CreationExtras must have an application by `APPLICATION_KEY`");
            }
            return super.a(cls);
        }

        public a() {
            this(null, 0);
        }

        /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
        public a(Application application) {
            this(application, 0);
            D2.h.f(application, "application");
        }
    }

    public E(G g3, b bVar, F.a aVar) {
        D2.h.f(g3, "store");
        D2.h.f(bVar, "factory");
        D2.h.f(aVar, "defaultCreationExtras");
        this.f5267a = g3;
        this.f5268b = bVar;
        this.f5269c = aVar;
    }

    public /* synthetic */ E(G g3, b bVar, F.a aVar, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(g3, bVar, (i3 & 4) != 0 ? a.C0003a.f211b : aVar);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public E(H h3) {
        this(h3.s(), a.f5270f.a(h3), F.a(h3));
        D2.h.f(h3, "owner");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public E(H h3, b bVar) {
        this(h3.s(), bVar, F.a(h3));
        D2.h.f(h3, "owner");
        D2.h.f(bVar, "factory");
    }
}

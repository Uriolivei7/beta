package p;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import p.g;
import q.InterfaceC0643a;

/* JADX INFO: loaded from: classes.dex */
abstract class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static final l.e f10233a = new l.e(16);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final ExecutorService f10234b = h.a("fonts-androidx", 10, 10000);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    static final Object f10235c = new Object();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    static final l.g f10236d = new l.g();

    class a implements Callable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f10237a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ Context f10238b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ p.e f10239c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f10240d;

        a(String str, Context context, p.e eVar, int i3) {
            this.f10237a = str;
            this.f10238b = context;
            this.f10239c = eVar;
            this.f10240d = i3;
        }

        @Override // java.util.concurrent.Callable
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public e call() {
            return f.c(this.f10237a, this.f10238b, this.f10239c, this.f10240d);
        }
    }

    class b implements InterfaceC0643a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ C0632a f10241a;

        b(C0632a c0632a) {
            this.f10241a = c0632a;
        }

        @Override // q.InterfaceC0643a
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(e eVar) {
            if (eVar == null) {
                eVar = new e(-3);
            }
            this.f10241a.b(eVar);
        }
    }

    class c implements Callable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f10242a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ Context f10243b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ p.e f10244c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f10245d;

        c(String str, Context context, p.e eVar, int i3) {
            this.f10242a = str;
            this.f10243b = context;
            this.f10244c = eVar;
            this.f10245d = i3;
        }

        @Override // java.util.concurrent.Callable
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public e call() {
            try {
                return f.c(this.f10242a, this.f10243b, this.f10244c, this.f10245d);
            } catch (Throwable unused) {
                return new e(-3);
            }
        }
    }

    class d implements InterfaceC0643a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f10246a;

        d(String str) {
            this.f10246a = str;
        }

        @Override // q.InterfaceC0643a
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(e eVar) {
            synchronized (f.f10235c) {
                try {
                    l.g gVar = f.f10236d;
                    ArrayList arrayList = (ArrayList) gVar.get(this.f10246a);
                    if (arrayList == null) {
                        return;
                    }
                    gVar.remove(this.f10246a);
                    for (int i3 = 0; i3 < arrayList.size(); i3++) {
                        ((InterfaceC0643a) arrayList.get(i3)).a(eVar);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private static String a(p.e eVar, int i3) {
        return eVar.d() + "-" + i3;
    }

    private static int b(g.a aVar) {
        int i3 = 1;
        if (aVar.c() != 0) {
            return aVar.c() != 1 ? -3 : -2;
        }
        g.b[] bVarArrB = aVar.b();
        if (bVarArrB != null && bVarArrB.length != 0) {
            i3 = 0;
            for (g.b bVar : bVarArrB) {
                int iB = bVar.b();
                if (iB != 0) {
                    if (iB < 0) {
                        return -3;
                    }
                    return iB;
                }
            }
        }
        return i3;
    }

    static e c(String str, Context context, p.e eVar, int i3) {
        l.e eVar2 = f10233a;
        Typeface typeface = (Typeface) eVar2.c(str);
        if (typeface != null) {
            return new e(typeface);
        }
        try {
            g.a aVarE = p.d.e(context, eVar, null);
            int iB = b(aVarE);
            if (iB != 0) {
                return new e(iB);
            }
            Typeface typefaceB = androidx.core.graphics.d.b(context, null, aVarE.b(), i3);
            if (typefaceB == null) {
                return new e(-3);
            }
            eVar2.d(str, typefaceB);
            return new e(typefaceB);
        } catch (PackageManager.NameNotFoundException unused) {
            return new e(-1);
        }
    }

    static Typeface d(Context context, p.e eVar, int i3, Executor executor, C0632a c0632a) {
        String strA = a(eVar, i3);
        Typeface typeface = (Typeface) f10233a.c(strA);
        if (typeface != null) {
            c0632a.b(new e(typeface));
            return typeface;
        }
        b bVar = new b(c0632a);
        synchronized (f10235c) {
            try {
                l.g gVar = f10236d;
                ArrayList arrayList = (ArrayList) gVar.get(strA);
                if (arrayList != null) {
                    arrayList.add(bVar);
                    return null;
                }
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add(bVar);
                gVar.put(strA, arrayList2);
                c cVar = new c(strA, context, eVar, i3);
                if (executor == null) {
                    executor = f10234b;
                }
                h.b(executor, cVar, new d(strA));
                return null;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    static Typeface e(Context context, p.e eVar, C0632a c0632a, int i3, int i4) {
        String strA = a(eVar, i3);
        Typeface typeface = (Typeface) f10233a.c(strA);
        if (typeface != null) {
            c0632a.b(new e(typeface));
            return typeface;
        }
        if (i4 == -1) {
            e eVarC = c(strA, context, eVar, i3);
            c0632a.b(eVarC);
            return eVarC.f10247a;
        }
        try {
            e eVar2 = (e) h.c(f10234b, new a(strA, context, eVar, i3), i4);
            c0632a.b(eVar2);
            return eVar2.f10247a;
        } catch (InterruptedException unused) {
            c0632a.b(new e(-3));
            return null;
        }
    }

    static final class e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final Typeface f10247a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final int f10248b;

        e(int i3) {
            this.f10247a = null;
            this.f10248b = i3;
        }

        boolean a() {
            return this.f10248b == 0;
        }

        e(Typeface typeface) {
            this.f10247a = typeface;
            this.f10248b = 0;
        }
    }
}

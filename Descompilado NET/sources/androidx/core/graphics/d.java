package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import androidx.core.content.res.d;
import androidx.core.content.res.f;
import p.g;

/* JADX INFO: loaded from: classes.dex */
public abstract class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final j f4477a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final l.e f4478b;

    public static class a extends g.c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private f.e f4479a;

        public a(f.e eVar) {
            this.f4479a = eVar;
        }

        @Override // p.g.c
        public void a(int i3) {
            f.e eVar = this.f4479a;
            if (eVar != null) {
                eVar.f(i3);
            }
        }

        @Override // p.g.c
        public void b(Typeface typeface) {
            f.e eVar = this.f4479a;
            if (eVar != null) {
                eVar.g(typeface);
            }
        }
    }

    static {
        int i3 = Build.VERSION.SDK_INT;
        if (i3 >= 29) {
            f4477a = new i();
        } else if (i3 >= 28) {
            f4477a = new h();
        } else if (i3 >= 26) {
            f4477a = new g();
        } else if (f.j()) {
            f4477a = new f();
        } else {
            f4477a = new e();
        }
        f4478b = new l.e(16);
    }

    public static Typeface a(Context context, Typeface typeface, int i3) {
        if (context != null) {
            return Typeface.create(typeface, i3);
        }
        throw new IllegalArgumentException("Context cannot be null");
    }

    public static Typeface b(Context context, CancellationSignal cancellationSignal, g.b[] bVarArr, int i3) {
        return f4477a.b(context, cancellationSignal, bVarArr, i3);
    }

    public static Typeface c(Context context, d.b bVar, Resources resources, int i3, String str, int i4, int i5, f.e eVar, Handler handler, boolean z3) {
        Typeface typefaceA;
        if (bVar instanceof d.e) {
            d.e eVar2 = (d.e) bVar;
            Typeface typefaceG = g(eVar2.c());
            if (typefaceG != null) {
                if (eVar != null) {
                    eVar.d(typefaceG, handler);
                }
                return typefaceG;
            }
            typefaceA = p.g.c(context, eVar2.b(), i5, !z3 ? eVar != null : eVar2.a() != 0, z3 ? eVar2.d() : -1, f.e.e(handler), new a(eVar));
        } else {
            typefaceA = f4477a.a(context, (d.c) bVar, resources, i5);
            if (eVar != null) {
                if (typefaceA != null) {
                    eVar.d(typefaceA, handler);
                } else {
                    eVar.c(-3, handler);
                }
            }
        }
        if (typefaceA != null) {
            f4478b.d(e(resources, i3, str, i4, i5), typefaceA);
        }
        return typefaceA;
    }

    public static Typeface d(Context context, Resources resources, int i3, String str, int i4, int i5) {
        Typeface typefaceD = f4477a.d(context, resources, i3, str, i5);
        if (typefaceD != null) {
            f4478b.d(e(resources, i3, str, i4, i5), typefaceD);
        }
        return typefaceD;
    }

    private static String e(Resources resources, int i3, String str, int i4, int i5) {
        return resources.getResourcePackageName(i3) + '-' + str + '-' + i4 + '-' + i3 + '-' + i5;
    }

    public static Typeface f(Resources resources, int i3, String str, int i4, int i5) {
        return (Typeface) f4478b.c(e(resources, i3, str, i4, i5));
    }

    private static Typeface g(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        Typeface typefaceCreate = Typeface.create(str, 0);
        Typeface typefaceCreate2 = Typeface.create(Typeface.DEFAULT, 0);
        if (typefaceCreate == null || typefaceCreate.equals(typefaceCreate2)) {
            return null;
        }
        return typefaceCreate;
    }
}

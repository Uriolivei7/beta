package m0;

import J0.C0185t;
import J0.C0186u;
import J0.y;
import J0.z;
import a2.AbstractC0211a;
import android.content.Context;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: renamed from: m0.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0601d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final Class f9805a = AbstractC0601d.class;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static C0604g f9806b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static volatile boolean f9807c = false;

    public static C0185t a() {
        return b().j();
    }

    public static y b() {
        return y.l();
    }

    public static void c(Context context, C0186u c0186u, C0599b c0599b) {
        d(context, c0186u, c0599b, true);
    }

    public static void d(Context context, C0186u c0186u, C0599b c0599b, boolean z3) {
        if (V0.b.d()) {
            V0.b.a("Fresco#initialize");
        }
        if (f9807c) {
            Y.a.E(f9805a, "Fresco has already been initialized! `Fresco.initialize(...)` should only be called 1 single time to avoid memory leaks!");
        } else {
            f9807c = true;
        }
        z.b(z3);
        if (!AbstractC0211a.c()) {
            if (V0.b.d()) {
                V0.b.a("Fresco.initialize->SoLoader.init");
            }
            try {
                try {
                    try {
                        try {
                            Class.forName("com.facebook.imagepipeline.nativecode.NativeCodeInitializer").getMethod("init", Context.class).invoke(null, context);
                        } catch (ClassNotFoundException unused) {
                            AbstractC0211a.b(new a2.c());
                            if (V0.b.d()) {
                            }
                        }
                    } catch (NoSuchMethodException unused2) {
                        AbstractC0211a.b(new a2.c());
                        if (V0.b.d()) {
                        }
                    }
                } catch (IllegalAccessException unused3) {
                    AbstractC0211a.b(new a2.c());
                    if (V0.b.d()) {
                    }
                } catch (InvocationTargetException unused4) {
                    AbstractC0211a.b(new a2.c());
                    if (V0.b.d()) {
                    }
                }
                if (V0.b.d()) {
                    V0.b.b();
                }
            } catch (Throwable th) {
                if (V0.b.d()) {
                    V0.b.b();
                }
                throw th;
            }
        }
        Context applicationContext = context.getApplicationContext();
        if (c0186u == null) {
            y.t(applicationContext);
        } else {
            y.s(c0186u);
        }
        e(applicationContext, c0599b);
        if (V0.b.d()) {
            V0.b.b();
        }
    }

    private static void e(Context context, C0599b c0599b) {
        if (V0.b.d()) {
            V0.b.a("Fresco.initializeDrawee");
        }
        C0604g c0604g = new C0604g(context, c0599b);
        f9806b = c0604g;
        x0.e.i(c0604g);
        if (V0.b.d()) {
            V0.b.b();
        }
    }

    public static C0603f f() {
        return f9806b.get();
    }
}

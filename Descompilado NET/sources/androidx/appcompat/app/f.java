package androidx.appcompat.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.LocaleManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.window.OnBackInvokedDispatcher;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public abstract class f {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    static c f3170b = new c(new d());

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static int f3171c = -100;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static androidx.core.os.e f3172d = null;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static androidx.core.os.e f3173e = null;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static Boolean f3174f = null;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static boolean f3175g = false;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final l.b f3176h = new l.b();

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final Object f3177i = new Object();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static final Object f3178j = new Object();

    static class a {
        static LocaleList a(String str) {
            return LocaleList.forLanguageTags(str);
        }
    }

    static class b {
        static LocaleList a(Object obj) {
            return ((LocaleManager) obj).getApplicationLocales();
        }

        static void b(Object obj, LocaleList localeList) {
            ((LocaleManager) obj).setApplicationLocales(localeList);
        }
    }

    static class c implements Executor {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Object f3179b = new Object();

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final Queue f3180c = new ArrayDeque();

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final Executor f3181d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        Runnable f3182e;

        c(Executor executor) {
            this.f3181d = executor;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void c(Runnable runnable) {
            try {
                runnable.run();
            } finally {
                d();
            }
        }

        protected void d() {
            synchronized (this.f3179b) {
                try {
                    Runnable runnable = (Runnable) this.f3180c.poll();
                    this.f3182e = runnable;
                    if (runnable != null) {
                        this.f3181d.execute(runnable);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // java.util.concurrent.Executor
        public void execute(final Runnable runnable) {
            synchronized (this.f3179b) {
                try {
                    this.f3180c.add(new Runnable() { // from class: androidx.appcompat.app.g
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f3183b.c(runnable);
                        }
                    });
                    if (this.f3182e == null) {
                        d();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    static class d implements Executor {
        d() {
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            new Thread(runnable).start();
        }
    }

    f() {
    }

    static void G(f fVar) {
        synchronized (f3177i) {
            H(fVar);
        }
    }

    private static void H(f fVar) {
        synchronized (f3177i) {
            try {
                Iterator it = f3176h.iterator();
                while (it.hasNext()) {
                    f fVar2 = (f) ((WeakReference) it.next()).get();
                    if (fVar2 == fVar || fVar2 == null) {
                        it.remove();
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void M(int i3) {
        if (i3 != -1 && i3 != 0 && i3 != 1 && i3 != 2 && i3 != 3) {
            Log.d("AppCompatDelegate", "setDefaultNightMode() called with an unknown mode");
        } else if (f3171c != i3) {
            f3171c = i3;
            g();
        }
    }

    static void Q(Context context) {
        if (Build.VERSION.SDK_INT >= 33) {
            ComponentName componentName = new ComponentName(context, "androidx.appcompat.app.AppLocalesMetadataHolderService");
            if (context.getPackageManager().getComponentEnabledSetting(componentName) != 1) {
                if (m().e()) {
                    String strB = androidx.core.app.d.b(context);
                    Object systemService = context.getSystemService("locale");
                    if (systemService != null) {
                        b.b(systemService, a.a(strB));
                    }
                }
                context.getPackageManager().setComponentEnabledSetting(componentName, 1, 1);
            }
        }
    }

    static void R(final Context context) {
        if (w(context)) {
            if (Build.VERSION.SDK_INT >= 33) {
                if (f3175g) {
                    return;
                }
                f3170b.execute(new Runnable() { // from class: androidx.appcompat.app.e
                    @Override // java.lang.Runnable
                    public final void run() {
                        f.x(context);
                    }
                });
                return;
            }
            synchronized (f3178j) {
                try {
                    androidx.core.os.e eVar = f3172d;
                    if (eVar == null) {
                        if (f3173e == null) {
                            f3173e = androidx.core.os.e.b(androidx.core.app.d.b(context));
                        }
                        if (f3173e.e()) {
                        } else {
                            f3172d = f3173e;
                        }
                    } else if (!eVar.equals(f3173e)) {
                        androidx.core.os.e eVar2 = f3172d;
                        f3173e = eVar2;
                        androidx.core.app.d.a(context, eVar2.g());
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    static void d(f fVar) {
        synchronized (f3177i) {
            H(fVar);
            f3176h.add(new WeakReference(fVar));
        }
    }

    private static void g() {
        synchronized (f3177i) {
            try {
                Iterator it = f3176h.iterator();
                while (it.hasNext()) {
                    f fVar = (f) ((WeakReference) it.next()).get();
                    if (fVar != null) {
                        fVar.f();
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static f j(Activity activity, androidx.appcompat.app.d dVar) {
        return new h(activity, dVar);
    }

    public static f k(Dialog dialog, androidx.appcompat.app.d dVar) {
        return new h(dialog, dVar);
    }

    public static androidx.core.os.e m() {
        if (Build.VERSION.SDK_INT >= 33) {
            Object objQ = q();
            if (objQ != null) {
                return androidx.core.os.e.h(b.a(objQ));
            }
        } else {
            androidx.core.os.e eVar = f3172d;
            if (eVar != null) {
                return eVar;
            }
        }
        return androidx.core.os.e.d();
    }

    public static int o() {
        return f3171c;
    }

    static Object q() {
        Context contextN;
        Iterator it = f3176h.iterator();
        while (it.hasNext()) {
            f fVar = (f) ((WeakReference) it.next()).get();
            if (fVar != null && (contextN = fVar.n()) != null) {
                return contextN.getSystemService("locale");
            }
        }
        return null;
    }

    static androidx.core.os.e s() {
        return f3172d;
    }

    static boolean w(Context context) {
        if (f3174f == null) {
            try {
                Bundle bundle = t.a(context).metaData;
                if (bundle != null) {
                    f3174f = Boolean.valueOf(bundle.getBoolean("autoStoreLocales"));
                }
            } catch (PackageManager.NameNotFoundException unused) {
                Log.d("AppCompatDelegate", "Checking for metadata for AppLocalesMetadataHolderService : Service not found");
                f3174f = Boolean.FALSE;
            }
        }
        return f3174f.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void x(Context context) {
        Q(context);
        f3175g = true;
    }

    public abstract void A();

    public abstract void B(Bundle bundle);

    public abstract void C();

    public abstract void D(Bundle bundle);

    public abstract void E();

    public abstract void F();

    public abstract boolean I(int i3);

    public abstract void J(int i3);

    public abstract void K(View view);

    public abstract void L(View view, ViewGroup.LayoutParams layoutParams);

    public void N(OnBackInvokedDispatcher onBackInvokedDispatcher) {
    }

    public abstract void O(int i3);

    public abstract void P(CharSequence charSequence);

    public abstract void e(View view, ViewGroup.LayoutParams layoutParams);

    public abstract boolean f();

    public void h(Context context) {
    }

    public Context i(Context context) {
        h(context);
        return context;
    }

    public abstract View l(int i3);

    public abstract Context n();

    public abstract int p();

    public abstract MenuInflater r();

    public abstract androidx.appcompat.app.a t();

    public abstract void u();

    public abstract void v();

    public abstract void y(Configuration configuration);

    public abstract void z(Bundle bundle);
}

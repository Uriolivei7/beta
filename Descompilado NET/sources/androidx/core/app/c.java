package androidx.core.app;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
abstract class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected static final Class f4376a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected static final Field f4377b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected static final Field f4378c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected static final Method f4379d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    protected static final Method f4380e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    protected static final Method f4381f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final Handler f4382g = new Handler(Looper.getMainLooper());

    class a implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ d f4383b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Object f4384c;

        a(d dVar, Object obj) {
            this.f4383b = dVar;
            this.f4384c = obj;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f4383b.f4389a = this.f4384c;
        }
    }

    class b implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ Application f4385b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ d f4386c;

        b(Application application, d dVar) {
            this.f4385b = application;
            this.f4386c = dVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f4385b.unregisterActivityLifecycleCallbacks(this.f4386c);
        }
    }

    /* JADX INFO: renamed from: androidx.core.app.c$c, reason: collision with other inner class name */
    class RunnableC0057c implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ Object f4387b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Object f4388c;

        RunnableC0057c(Object obj, Object obj2) {
            this.f4387b = obj;
            this.f4388c = obj2;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Method method = c.f4379d;
                if (method != null) {
                    method.invoke(this.f4387b, this.f4388c, Boolean.FALSE, "AppCompat recreation");
                } else {
                    c.f4380e.invoke(this.f4387b, this.f4388c, Boolean.FALSE);
                }
            } catch (RuntimeException e4) {
                if (e4.getClass() == RuntimeException.class && e4.getMessage() != null && e4.getMessage().startsWith("Unable to stop")) {
                    throw e4;
                }
            } catch (Throwable th) {
                Log.e("ActivityRecreator", "Exception while invoking performStopActivity", th);
            }
        }
    }

    private static final class d implements Application.ActivityLifecycleCallbacks {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        Object f4389a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private Activity f4390b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f4391c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private boolean f4392d = false;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private boolean f4393e = false;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private boolean f4394f = false;

        d(Activity activity) {
            this.f4390b = activity;
            this.f4391c = activity.hashCode();
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity activity) {
            if (this.f4390b == activity) {
                this.f4390b = null;
                this.f4393e = true;
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity) {
            if (!this.f4393e || this.f4394f || this.f4392d || !c.h(this.f4389a, this.f4391c, activity)) {
                return;
            }
            this.f4394f = true;
            this.f4389a = null;
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity activity) {
            if (this.f4390b == activity) {
                this.f4392d = true;
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity) {
        }
    }

    static {
        Class clsA = a();
        f4376a = clsA;
        f4377b = b();
        f4378c = f();
        f4379d = d(clsA);
        f4380e = c(clsA);
        f4381f = e(clsA);
    }

    private static Class a() {
        try {
            return Class.forName("android.app.ActivityThread");
        } catch (Throwable unused) {
            return null;
        }
    }

    private static Field b() {
        try {
            Field declaredField = Activity.class.getDeclaredField("mMainThread");
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Throwable unused) {
            return null;
        }
    }

    private static Method c(Class cls) {
        if (cls == null) {
            return null;
        }
        try {
            Method declaredMethod = cls.getDeclaredMethod("performStopActivity", IBinder.class, Boolean.TYPE);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (Throwable unused) {
            return null;
        }
    }

    private static Method d(Class cls) {
        if (cls == null) {
            return null;
        }
        try {
            Method declaredMethod = cls.getDeclaredMethod("performStopActivity", IBinder.class, Boolean.TYPE, String.class);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (Throwable unused) {
            return null;
        }
    }

    private static Method e(Class cls) {
        if (g() && cls != null) {
            try {
                Class cls2 = Integer.TYPE;
                Class cls3 = Boolean.TYPE;
                Method declaredMethod = cls.getDeclaredMethod("requestRelaunchActivity", IBinder.class, List.class, List.class, cls2, cls3, Configuration.class, Configuration.class, cls3, cls3);
                declaredMethod.setAccessible(true);
                return declaredMethod;
            } catch (Throwable unused) {
            }
        }
        return null;
    }

    private static Field f() {
        try {
            Field declaredField = Activity.class.getDeclaredField("mToken");
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Throwable unused) {
            return null;
        }
    }

    private static boolean g() {
        int i3 = Build.VERSION.SDK_INT;
        return i3 == 26 || i3 == 27;
    }

    protected static boolean h(Object obj, int i3, Activity activity) {
        try {
            Object obj2 = f4378c.get(activity);
            if (obj2 == obj && activity.hashCode() == i3) {
                f4382g.postAtFrontOfQueue(new RunnableC0057c(f4377b.get(activity), obj2));
                return true;
            }
            return false;
        } catch (Throwable th) {
            Log.e("ActivityRecreator", "Exception while fetching field values", th);
            return false;
        }
    }

    static boolean i(Activity activity) {
        Object obj;
        if (Build.VERSION.SDK_INT >= 28) {
            activity.recreate();
            return true;
        }
        if (g() && f4381f == null) {
            return false;
        }
        if (f4380e == null && f4379d == null) {
            return false;
        }
        try {
            Object obj2 = f4378c.get(activity);
            if (obj2 == null || (obj = f4377b.get(activity)) == null) {
                return false;
            }
            Application application = activity.getApplication();
            d dVar = new d(activity);
            application.registerActivityLifecycleCallbacks(dVar);
            Handler handler = f4382g;
            handler.post(new a(dVar, obj2));
            try {
                if (g()) {
                    Method method = f4381f;
                    Boolean bool = Boolean.FALSE;
                    method.invoke(obj, obj2, null, null, 0, bool, null, null, bool, bool);
                } else {
                    activity.recreate();
                }
                handler.post(new b(application, dVar));
                return true;
            } catch (Throwable th) {
                f4382g.post(new b(application, dVar));
                throw th;
            }
        } catch (Throwable unused) {
            return false;
        }
    }
}

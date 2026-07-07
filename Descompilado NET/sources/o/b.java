package O;

import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/* JADX INFO: loaded from: classes.dex */
final class b {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final b f1419d = new b();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ExecutorService f1420a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ScheduledExecutorService f1421b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Executor f1422c;

    /* JADX INFO: renamed from: O.b$b, reason: collision with other inner class name */
    private static class ExecutorC0024b implements Executor {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private ThreadLocal f1423b;

        private ExecutorC0024b() {
            this.f1423b = new ThreadLocal();
        }

        private int b() {
            Integer num = (Integer) this.f1423b.get();
            if (num == null) {
                num = 0;
            }
            int iIntValue = num.intValue() - 1;
            if (iIntValue == 0) {
                this.f1423b.remove();
            } else {
                this.f1423b.set(Integer.valueOf(iIntValue));
            }
            return iIntValue;
        }

        private int c() {
            Integer num = (Integer) this.f1423b.get();
            if (num == null) {
                num = 0;
            }
            int iIntValue = num.intValue() + 1;
            this.f1423b.set(Integer.valueOf(iIntValue));
            return iIntValue;
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            try {
                if (c() <= 15) {
                    runnable.run();
                } else {
                    b.a().execute(runnable);
                }
                b();
            } catch (Throwable th) {
                b();
                throw th;
            }
        }
    }

    private b() {
        this.f1420a = !c() ? Executors.newCachedThreadPool() : O.a.b();
        this.f1421b = Executors.newSingleThreadScheduledExecutor();
        this.f1422c = new ExecutorC0024b();
    }

    public static ExecutorService a() {
        return f1419d.f1420a;
    }

    static Executor b() {
        return f1419d.f1422c;
    }

    private static boolean c() {
        String property = System.getProperty("java.runtime.name");
        if (property == null) {
            return false;
        }
        return property.toLowerCase(Locale.US).contains("android");
    }
}

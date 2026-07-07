package p;

import android.os.Handler;
import android.os.Process;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import q.InterfaceC0643a;

/* JADX INFO: loaded from: classes.dex */
abstract class h {

    private static class a implements ThreadFactory {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private String f10256a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f10257b;

        /* JADX INFO: renamed from: p.h$a$a, reason: collision with other inner class name */
        private static class C0139a extends Thread {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            private final int f10258b;

            C0139a(Runnable runnable, String str, int i3) {
                super(runnable, str);
                this.f10258b = i3;
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Process.setThreadPriority(this.f10258b);
                super.run();
            }
        }

        a(String str, int i3) {
            this.f10256a = str;
            this.f10257b = i3;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new C0139a(runnable, this.f10256a, this.f10257b);
        }
    }

    private static class b implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private Callable f10259b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private InterfaceC0643a f10260c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private Handler f10261d;

        class a implements Runnable {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            final /* synthetic */ InterfaceC0643a f10262b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            final /* synthetic */ Object f10263c;

            a(InterfaceC0643a interfaceC0643a, Object obj) {
                this.f10262b = interfaceC0643a;
                this.f10263c = obj;
            }

            @Override // java.lang.Runnable
            public void run() {
                this.f10262b.a(this.f10263c);
            }
        }

        b(Handler handler, Callable callable, InterfaceC0643a interfaceC0643a) {
            this.f10259b = callable;
            this.f10260c = interfaceC0643a;
            this.f10261d = handler;
        }

        @Override // java.lang.Runnable
        public void run() {
            Object objCall;
            try {
                objCall = this.f10259b.call();
            } catch (Exception unused) {
                objCall = null;
            }
            this.f10261d.post(new a(this.f10260c, objCall));
        }
    }

    static ThreadPoolExecutor a(String str, int i3, int i4) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, i4, TimeUnit.MILLISECONDS, new LinkedBlockingDeque(), new a(str, i3));
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }

    static void b(Executor executor, Callable callable, InterfaceC0643a interfaceC0643a) {
        executor.execute(new b(AbstractC0633b.a(), callable, interfaceC0643a));
    }

    static Object c(ExecutorService executorService, Callable callable, int i3) throws InterruptedException {
        try {
            return executorService.submit(callable).get(i3, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e4) {
            throw e4;
        } catch (ExecutionException e5) {
            throw new RuntimeException(e5);
        } catch (TimeoutException unused) {
            throw new InterruptedException("timeout");
        }
    }
}

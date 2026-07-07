package I1;

import D2.h;
import com.facebook.react.bridge.UiThreadUtil;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final c f431a = new c();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final Executor f432b = new b();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final Executor f433c = new a();

    private static final class a implements Executor {
        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            h.f(runnable, "command");
            runnable.run();
        }
    }

    private static final class b implements Executor {
        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            h.f(runnable, "command");
            UiThreadUtil.runOnUiThread(runnable);
        }
    }

    private c() {
    }
}

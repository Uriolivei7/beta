package J0;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: J0.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0168b implements InterfaceC0182p {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final a f565f = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Executor f566a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Executor f567b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Executor f568c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Executor f569d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final ScheduledExecutorService f570e;

    /* JADX INFO: renamed from: J0.b$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public C0168b(int i3) {
        ExecutorService executorServiceNewFixedThreadPool = Executors.newFixedThreadPool(2, new B(10, "FrescoIoBoundExecutor", true));
        D2.h.e(executorServiceNewFixedThreadPool, "newFixedThreadPool(...)");
        this.f566a = executorServiceNewFixedThreadPool;
        ExecutorService executorServiceNewFixedThreadPool2 = Executors.newFixedThreadPool(i3, new B(10, "FrescoDecodeExecutor", true));
        D2.h.e(executorServiceNewFixedThreadPool2, "newFixedThreadPool(...)");
        this.f567b = executorServiceNewFixedThreadPool2;
        ExecutorService executorServiceNewFixedThreadPool3 = Executors.newFixedThreadPool(i3, new B(10, "FrescoBackgroundExecutor", true));
        D2.h.e(executorServiceNewFixedThreadPool3, "newFixedThreadPool(...)");
        this.f568c = executorServiceNewFixedThreadPool3;
        ExecutorService executorServiceNewFixedThreadPool4 = Executors.newFixedThreadPool(1, new B(10, "FrescoLightWeightBackgroundExecutor", true));
        D2.h.e(executorServiceNewFixedThreadPool4, "newFixedThreadPool(...)");
        this.f569d = executorServiceNewFixedThreadPool4;
        ScheduledExecutorService scheduledExecutorServiceNewScheduledThreadPool = Executors.newScheduledThreadPool(i3, new B(10, "FrescoBackgroundExecutor", true));
        D2.h.e(scheduledExecutorServiceNewScheduledThreadPool, "newScheduledThreadPool(...)");
        this.f570e = scheduledExecutorServiceNewScheduledThreadPool;
    }

    @Override // J0.InterfaceC0182p
    public Executor a() {
        return this.f567b;
    }

    @Override // J0.InterfaceC0182p
    public Executor b() {
        return this.f569d;
    }

    @Override // J0.InterfaceC0182p
    public Executor c() {
        return this.f566a;
    }

    @Override // J0.InterfaceC0182p
    public Executor d() {
        return this.f566a;
    }

    @Override // J0.InterfaceC0182p
    public Executor e() {
        return this.f568c;
    }

    @Override // J0.InterfaceC0182p
    public Executor f() {
        return this.f566a;
    }

    @Override // J0.InterfaceC0182p
    public ScheduledExecutorService g() {
        return this.f570e;
    }
}
